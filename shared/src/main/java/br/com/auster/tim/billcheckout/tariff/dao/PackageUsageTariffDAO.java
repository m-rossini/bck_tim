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
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.auster.tim.billcheckout.tariff.model.PackageUsageTariff;

/**
 * @author anardo 
 */
public class PackageUsageTariffDAO {

	private static final Logger log = Logger.getLogger(PackageUsageTariffDAO.class);

	public static final String SELECT_ALL = 
		"SELECT a.PACKAGE_UID, e.DESCRIPTION, d.DESCRIPTION, a.ACCESS_FEE, " +
		"		a.SUBSCRIPTION_RATE, a.EFFECTIVE_DATE, a.LOADED_DATE " +
		"FROM   PACKAGE_RATES a " +
		"       JOIN QLF_PLANS d ON a.PLAN_UID = d.OBJID " +
		"       JOIN QLF_PACKAGE e ON a.PACKAGE_UID = e.OBJID " +
		"WHERE  d.OBJID = ? ";
	
	public static final String FILTER_MAX_DATE = " AND a.EFFECTIVE_DATE = " +
												 " (SELECT MAX(EFFECTIVE_DATE) FROM PACKAGE_RATES " +
												 " WHERE PLAN_UID = d.OBJID AND PACKAGE_UID = e.OBJID ) ";
	
	private static final String FILTER_ROW = " AND a.PACKAGE_UID = ? ORDER BY a.EFFECTIVE_DATE ";

	public List listRates(Connection _conn, Long _rateplan, String _selectedRow) throws SQLException {
		PreparedStatement stmt = null;
		try {
			if (_selectedRow != null){
				stmt = _conn.prepareStatement(SELECT_ALL + FILTER_ROW);
			}else{
				stmt = _conn.prepareStatement(SELECT_ALL + FILTER_MAX_DATE);
			}
			stmt.setLong(1, _rateplan.longValue());
			if (_selectedRow != null){
				stmt.setLong(2, new Long(_selectedRow).longValue());
			}
			ResultSet rs = stmt.executeQuery();
			
			List results = new ArrayList(); 			
			while(rs.next()) {
				int col=1;
				results.add( new PackageUsageTariff(rs.getLong(col++),
													rs.getString(col++),
											   		rs.getString(col++), 
											   		rs.getDouble(col++), 
											   		rs.getDouble(col++), 
											   		rs.getDate(col++),
											   		rs.getDate(col++)) );
			}
			log.debug("Loaded " + results.size() + " rows from PACKAGE_RATES.");			
			return results;
		} finally {
			if (stmt != null) { stmt.close(); }
		}
	}
}
