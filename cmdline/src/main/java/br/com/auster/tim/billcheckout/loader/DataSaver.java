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
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

/**
 * TODO What this class is responsible for
 *
 * @author Frederico Ramos
 * @version $Id$
 * @since JDK1.4
 */
public class DataSaver {
	
	
	
	private static final Logger logger = Logger.getLogger(DataSaver.class);
	
	protected LayoutReader layout;
	protected Connection connection;
	protected PreparedStatement statement;
	protected int commitCount;
	protected int commitLimit;
	protected int commitsExecuted;	
	
	
	
	public DataSaver(LayoutReader _reader, Connection _conn, String _sql) throws SQLException {
		this(_reader, _conn, _sql, 1);
	}
	
	public DataSaver(LayoutReader _reader, Connection _conn, String _sql, int _commitLimit) throws SQLException {
		this.layout = _reader;		
		this.connection = _conn;
		if (!LoaderMain.DRYRUN) { 
			logger.debug("Running sql " + _sql);
			this.statement = this.connection.prepareStatement(_sql);
		}
		this.commitCount = 0;
		this.commitLimit = _commitLimit;
		this.commitsExecuted = 0;
	}
			
	public void saveLine(String[] _columns) throws SQLException {
		// if dry-run on, then just skip all sql operations		
		if (!LoaderMain.DRYRUN) {
			// setting values
			for (int col=1; col <= this.layout.getIntProperty(LayoutReader.CFG_LAYOUT_USED_COLUMNS); col++) {
				String value = _columns[this.layout.getIntProperty(LayoutReader.CFG_LAYOUT_COL_POSITION, col)-1];
				String type =  this.layout.getProperty(LayoutReader.CFG_LAYOUT_COL_TYPE, col);
				String format = this.layout.getProperty(LayoutReader.CFG_LAYOUT_COL_FORMAT, col);
				// if we have a FK configured, then update type & format to the ones defined as FK
				if (this.layout.getProperty(LayoutReader.CFG_LAYOUT_COL_FKQUERY, col) != null) {
					logger.debug("Value from column #" + col + " comes from a FK query.");
					type =  this.layout.getProperty(LayoutReader.CFG_LAYOUT_COL_FKTYPE, col);
					format = this.layout.getProperty(LayoutReader.CFG_LAYOUT_COL_FKFORMAT, col);
				}
				
				Object valueToSet = DataTypes.convertIntoDBObject(value, type, format);
				if (valueToSet == null) {
					logger.debug("Setting NULL for column #" + col);
					this.statement.setNull(col, DataTypes.TYPES_TO_SQL.get(type));
				} else {
					logger.debug("Setting value '" + valueToSet + "'/" + valueToSet.getClass() + " for column #" + col + " as SQL type " + DataTypes.TYPES_TO_SQL.get(type));
					this.statement.setObject(col, valueToSet, DataTypes.TYPES_TO_SQL.get(type));
				}
			}
			// saving to commit
			this.statement.addBatch();
			this.statement.clearParameters();
		}
		logger.debug("Another line added to batch update.");
		// check if its time to commit
		this.commitCount++;
		this.handleCommit();
	}
	
	public void handleCommit() throws SQLException {
		if (this.commitCount == this.commitLimit) {
			try {
				if (!LoaderMain.DRYRUN) {
					this.statement.executeBatch();				
					this.connection.commit();
					logger.debug("Transaction #" + this.commitsExecuted + " saved rows to database.");
				}
			} finally {
				this.commitsExecuted++;
				this.commitCount=0;
			}
		}
	}
	
	public void runStatements(String _sql) {
			Statement stmt = null;
			try {
				stmt = this.connection.createStatement();
				stmt.execute(_sql);
				if (!LoaderMain.DRYRUN) {
					this.connection.commit();
				}
			} catch (SQLException sqle) {
				logger.error("Error running pre/pos statement", sqle);
		 	} finally {
		 		try {
		 			if (stmt != null) { stmt.close(); }
		 		} catch (SQLException sqle) {
		 			logger.error("Error running pre/pos statement", sqle);
		 	}
		}
	}
}
