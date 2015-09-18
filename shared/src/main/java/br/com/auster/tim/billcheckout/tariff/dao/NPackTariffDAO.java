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
 * Created on 30/04/2010
 */
package br.com.auster.tim.billcheckout.tariff.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.auster.tim.billcheckout.tariff.model.NPackTariff;

/**
 * @author anardo 
 */
public class NPackTariffDAO {

	private static final Logger log = Logger.getLogger(NPackTariffDAO.class);

	private static final String SELECT_ALL = 
		" SELECT a.PLAN_UID, a.INIT_RANGE, a.END_RANGE, a.EFFECTIVE_DATE, a.AMOUNT "+
		" FROM   NPACK_RATES a "+
		" WHERE  a.PLAN_UID = ? ";
	
	private static final String FILTER_MAX_DATE = 
		" AND a.EFFECTIVE_DATE = (SELECT MAX(EFFECTIVE_DATE) FROM NPACK_RATES " +
		"                     WHERE PLAN_UID = a.PLAN_UID)";
	
	
	private static final String ORDER_ROWS =
		" ORDER BY a.EFFECTIVE_DATE, a.INIT_RANGE";

//	private static final String FILTER_ROW = " AND a.TMCODE_CONTR = ? AND a.NPCODE = ? ORDER BY a.VALID_FROM ";
				                        
	public List listRates(Connection _conn, Long _rateplan, String _selectedRow) throws SQLException {
		PreparedStatement stmt = null;
		try {
			String sql = SELECT_ALL;
			if (_selectedRow != null){
				sql += FILTER_MAX_DATE;
			}
			sql += ORDER_ROWS;
			stmt = _conn.prepareStatement(sql);
			stmt.setLong(1, _rateplan.longValue());
			
			ResultSet rs = stmt.executeQuery();			
			List results = new LinkedList(); 			
			while(rs.next()) {
				int col=1;
				// planUid, intRange, endRange
				//   effectiveDate, accessFee
				results.add( new NPackTariff(rs.getLong(col++) , rs.getInt(col++), rs.getInt(col++),
											 rs.getDate(col++), rs.getDouble(col++)) );
			}
			log.debug("Loaded " + results.size() + " rows from NPACK_TM.");			
			return results;
		} finally {
			if (stmt != null) { stmt.close(); }
		}
	}
}
