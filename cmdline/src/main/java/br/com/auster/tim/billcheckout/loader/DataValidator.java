/*
 * Copyright (c) 2004-2010 Auster Solutions. All Rights Reserved.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Created on 08/03/2010
 */
package br.com.auster.tim.billcheckout.loader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import javax.naming.NamingException;

import org.apache.log4j.Logger;

import br.com.auster.common.sql.SQLConnectionManager;
import br.com.auster.common.util.I18n;
import br.com.auster.tim.billcheckout.loader.validator.DiscardedRowsLogger;

/**
 * TODO What this class is responsible for
 *
 * @author Frederico Ramos
 * @version $Id$
 * @since JDK1.4
 */
public class DataValidator {

	
	
	private static final I18n i18n = I18n.getInstance(DataValidator.class);
	private static final Logger logger = Logger.getLogger(DataValidator.class);	
	
	protected LayoutReader layout;
	protected boolean lenient;
	protected PreparedStatement[] fkQueries;
	protected int rows=0;
	protected int errors=0;


	
	public DataValidator(Connection _conn, SQLConnectionManager _sqlManager, LayoutReader _reader) {
		this(_conn, _sqlManager, _reader, true);
	}
	
	public DataValidator(Connection _conn, SQLConnectionManager _sqlManager, LayoutReader _reader, boolean _lenient) {
		this.layout = _reader;
		this.lenient = _lenient;
		
		int colSize = this.layout.getIntProperty(LayoutReader.CFG_LAYOUT_USED_COLUMNS);
		this.fkQueries = new PreparedStatement[colSize+1];
		for (int i=1; i <= colSize; i++) {
			if (this.layout.getProperty(LayoutReader.CFG_LAYOUT_COL_FKQUERY, i) != null) {
				try {
					SQLConnectionManager sqlManager = _sqlManager;
					Connection conn = _conn;
					if (this.layout.getProperty(LayoutReader.CFG_LAYOUT_COL_FKPOOL, i) != null) {
						String dbName = this.layout.getProperty(LayoutReader.CFG_LAYOUT_COL_FKPOOL, i);
						sqlManager = SQLConnectionManager.getInstance(dbName);
						conn = sqlManager.getConnection(); 
						logger.debug("Switching database to " + dbName);
					}
					String sql = sqlManager.getStatement(this.layout.getProperty(LayoutReader.CFG_LAYOUT_COL_FKQUERY, i)).getStatementText();
					this.fkQueries[i] = conn.prepareStatement(sql);
				} catch (NamingException ne) {
					logger.error("Could not build fk queries:", ne);
				} catch (SQLException sqle) {
					logger.error("Could not build fk queries:", sqle);
				}
			}
		}
	}
	
	public int getErrorCount() { return this.errors; }
	public int getRowCount() { return this.rows; }
	
	
	
	public void validateLine(RowRead _row, BlockingQueue<String[]> _outQueue) throws InterruptedException {
		String[] columns = _row.columns;
		if (this.lenient) { 
			logger.debug("Validation is lenient; skipping row");
			return; 
		}
		
		// validating row length
		if (columns.length != this.layout.getIntProperty(LayoutReader.CFG_LAYOUT_TOTAL_COLUMNS)) {
			this.errors++;
			DiscardedRowsLogger.discardRow("validation.error.1", 
											new Object[] { _row.rowNumber,
														   String.valueOf(columns.length), 
	                   									   this.layout.getProperty(LayoutReader.CFG_LAYOUT_TOTAL_COLUMNS)}
					                   );
			return;
		}
		
		// saving list of possible values for FK columns
		String[][] fkValues = new String[this.layout.getIntProperty(LayoutReader.CFG_LAYOUT_USED_COLUMNS)+1][];
		int fksFound = 0;
		String[] initialPossibility = new String[columns.length];
		
		// for each column, check if its OK
		for (int i=0; i < this.layout.getIntProperty(LayoutReader.CFG_LAYOUT_USED_COLUMNS); i++) {
			int pos = i+1;
			
			String value = columns[this.layout.getIntProperty(LayoutReader.CFG_LAYOUT_COL_POSITION, pos)-1]; 
			String type = this.layout.getProperty(LayoutReader.CFG_LAYOUT_COL_TYPE, pos);
			String format = this.layout.getProperty(LayoutReader.CFG_LAYOUT_COL_FORMAT, pos);
						
			// if we are halding a FK column, then just build the list of possible values and we will validate later			
			if (this.layout.getProperty(LayoutReader.CFG_LAYOUT_COL_FKQUERY, pos) != null) {
				String[] newValue = this.findFKValue(pos, value, type, format);
				if (newValue == null) {
					this.errors++;
					String[] params = { String.valueOf(_row.rowNumber), String.valueOf(pos), type, format, value};
					DiscardedRowsLogger.discardRow("validation.error.3", params);
					return;
				} 
				fksFound += newValue.length;
				// if all OK, update incoming columns and iterate over them to save each value
				type = this.layout.getProperty(LayoutReader.CFG_LAYOUT_COL_FKTYPE, pos);
				format = this.layout.getProperty(LayoutReader.CFG_LAYOUT_COL_FKFORMAT, pos);
				for (int j=0; j < newValue.length; j++) {
					if (!validateColumnValue(newValue[j], type, format, pos, _row.rowNumber)) {
						return;
					}
				}
				// saving possible values
				fkValues[pos] = newValue;
				// saving first value for this FK
				initialPossibility[this.layout.getIntProperty(LayoutReader.CFG_LAYOUT_COL_POSITION, pos)-1] = newValue[0];
				
			} else if (this.layout.getProperty(LayoutReader.CFG_LAYOUT_COL_LOV_SEPARATOR, pos) != null) {
				
				String[] newValue = value.split(this.layout.getProperty(LayoutReader.CFG_LAYOUT_COL_LOV_SEPARATOR, pos));
				fksFound += newValue.length;
				// if all OK, update incoming columns and iterate over them to save each value
				type = this.layout.getProperty(LayoutReader.CFG_LAYOUT_COL_TYPE, pos);
				format = this.layout.getProperty(LayoutReader.CFG_LAYOUT_COL_FORMAT, pos);
				for (int j=0; j < newValue.length; j++) {
					if (!validateColumnValue(newValue[j], type, format, pos, _row.rowNumber)) {
						return;
					}
				}
				// saving possible values
				fkValues[pos] = newValue;
				// saving first value for this FK
				initialPossibility[this.layout.getIntProperty(LayoutReader.CFG_LAYOUT_COL_POSITION, pos)-1] = newValue[0];
				
			// Non-FK columns
			} else {	
				initialPossibility[this.layout.getIntProperty(LayoutReader.CFG_LAYOUT_COL_POSITION, pos)-1] = value;
				if (!validateColumnValue(value, type, format, pos, _row.rowNumber)) {
					return;
				}
			}			
		}
		
		// finish if no FK configured
		if (fksFound == 0) { 
			enqueueColumns(columns, _outQueue);
			return;
		}

		// now ALL values where validated, including FKs. This next step will multiply all possibilites
		//  like FK1 * FK2 * ... * FKn * non-FKs
		replicateRowByFKs(initialPossibility, 0, fkValues, _outQueue);
	}
	
	
	/**
	 * Will replicate all posiibilities of FK, based on the values found
	 * @throws InterruptedException 
	 */
	private void replicateRowByFKs(String[] _columns, int _initIdx, String[][] _fkValues, BlockingQueue<String[]> _outQueue) throws InterruptedException {

		boolean replicated = false;
		for (int i=_initIdx; i < this.layout.getIntProperty(LayoutReader.CFG_LAYOUT_USED_COLUMNS); i++) {
			int pos = i+1;
			String[] possibleValues = _fkValues[pos];			
			if (possibleValues != null) {
				replicated = true;
				 replicateRowByFKs(_columns, i+1, _fkValues, _outQueue);
				 for (int j=1; j < possibleValues.length; j++) {
					 String[] tempColumns = new String[_columns.length];
					 System.arraycopy(_columns, 0, tempColumns, 0, _columns.length);
					 int idx = this.layout.getIntProperty(LayoutReader.CFG_LAYOUT_COL_POSITION, i+1);
					 tempColumns[idx-1] = _fkValues[pos][j];
					 replicateRowByFKs(tempColumns, i+1, _fkValues, _outQueue);
				 }
				 return;
			}
		}
		if (!replicated) {
			enqueueColumns(_columns, _outQueue);
		}
	}

	private boolean validateColumnValue(String _value, String _type, String _format, int _colPos, int _rowNumber) {
		if (!DataTypes.canDecode( _value, _type, _format )) {
			String[] params = { String.valueOf(_rowNumber), String.valueOf(_colPos), _type, _format, _value };
			DiscardedRowsLogger.discardRow("validation.error.2", params);
			this.errors++;
			return false;
		}
		return true;

	}

	private void enqueueColumns(String[] _columns, BlockingQueue<String[]> _outQueue) throws InterruptedException {
		this.rows++;
		_outQueue.put( _columns );
		logger.debug("Line validated; procedding to save.");		
	}
		
	private String[] findFKValue(int _pos, String _value, String _type, String _format) {
		PreparedStatement stmt = this.fkQueries[_pos];
		ResultSet rset = null;
		Object valueToSet = DataTypes.convertIntoDBObject(_value, _type, _format);
		List<String> resultList = new ArrayList<String>();
		try {
			if (valueToSet == null) {
				stmt.setNull(1, DataTypes.TYPES_TO_SQL.get(_type));
			} else {
				stmt.setObject(1, valueToSet, DataTypes.TYPES_TO_SQL.get(_type));
			}

			rset = stmt.executeQuery();
			while (rset.next()) {
				Object finalvalue = rset.getObject(1);
				resultList.add( finalvalue == null ? null : finalvalue.toString() );
			}
			if (resultList.size() == 0) {
				return null;
			} else {
				return resultList.toArray(new String[] {});
			}
		} catch (SQLException sqle) {
			logger.error("SQLException:" , sqle);
			return null;
		} finally {
			try {
				// closing resultset
				if (rset != null) { rset.close(); }
				//clearing statement
				stmt.clearParameters();
			} catch (SQLException sqle) {
				logger.error("SQLException:" , sqle);
			}
		}
	}
}
