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
 * Created on 19/05/2010
 */
package br.com.auster.tim.billcheckout.tariff.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.auster.tim.billcheckout.tariff.model.DataUsageTariff;

/**
 * @author anardo
 *
 */
public class DataUsageTariffDAO {

	private static final Logger log = Logger.getLogger(DataUsageTariffDAO.class);

	public static final String SELECT_ALL = 
	"SELECT a.TARIFF_ZONE_UID, a.SERVICE_UID, a.PACKAGE_UID, " +
	"		b.DESCRIPTION, d.DESCRIPTION, a.EFFECTIVE_DATE, a.UMCODE, e.DESCRIPTION, "+
	"       a.END_RANGE_1, a.AMT_RANGE_1, a.END_RANGE_2, a.AMT_RANGE_2, a.END_RANGE_3, a.AMT_RANGE_3, "+
	"    	a.END_RANGE_4, a.AMT_RANGE_4, a.END_RANGE_5, a.AMT_RANGE_5, a.AMT_RANGE_MAX, a.LOADED_DATE "+
	"FROM   PRICEPLAN_RATES a "+
	"       JOIN QLF_TARIFF_ZONE b ON a.TARIFF_ZONE_UID = b.OBJID "+ 
	"       JOIN QLF_PLANS c ON a.PLAN_UID = c.OBJID "+
	"       JOIN QLF_SERVICES d ON a.SERVICE_UID = d.OBJID "+ 
	"       LEFT JOIN QLF_PACKAGE e ON a.PACKAGE_UID = e.OBJID "+
	"WHERE c.OBJID = ? ";

	public static final String FILTER_MAX_DATE = " AND a.EFFECTIVE_DATE = "+
	"            (SELECT MAX(EFFECTIVE_DATE) "+
	"             FROM   PRICEPLAN_RATES "+
	"            WHERE   TARIFF_ZONE_UID = b.OBJID AND "+
	"                    PLAN_UID = c.OBJID AND "+
	"                    SERVICE_UID = d.OBJID AND "+
	"                    DECODE(PACKAGE_UID, NULL, -1, PACKAGE_UID) =  DECODE(e.OBJID, NULL, -1, e.OBJID) ) "+
	"ORDER BY b.DESCRIPTION, d.DESCRIPTION, e.DESCRIPTION ASC";
	
	private static final String FILTER_ROW = " AND a.TARIFF_ZONE_UID = ? AND a.SERVICE_UID = ? " +
	 										 " AND DECODE(?, NULL, -1, ?) = DECODE(e.OBJID, NULL, -1, e.OBJID) " +
	 										 " ORDER BY a.EFFECTIVE_DATE ASC";
	
	public List listRates(Connection _conn, Long _rateplan, String _selectedRow) throws SQLException {
		PreparedStatement stmt = null;
		String[] array = null;
		try {
			
			if (_selectedRow != null){
				//lista todas tarifas de dados já cadastradas para um determinado registro da tela
				stmt = _conn.prepareStatement(SELECT_ALL + FILTER_ROW);
				array = _selectedRow.split("-");
			}else{
				//lista a tarifa de dados mais recente, de todos os registros existentes para determinado plano/estado.
				stmt = _conn.prepareStatement(SELECT_ALL + FILTER_MAX_DATE);
			}
			stmt.setLong(1, _rateplan.longValue());
			if (array != null){
				if (array.length == 3){
					stmt.setLong(2, new Long(array[0]).longValue()); //tariffZoneUid
					stmt.setLong(3, new Long(array[1]).longValue()); //serviceUid
					stmt.setLong(4, new Long(array[2]).longValue()); //packageUid
					stmt.setLong(5, new Long(array[2]).longValue()); //packageUid
				}else{
					return null;
				}
			}
			
			ResultSet rs = stmt.executeQuery();
			
			List results = new ArrayList(); 			
			while(rs.next()) {
				int col=1;
				results.add( new DataUsageTariff( rs.getLong(col++),
						  						  rs.getLong(col++),
						  						  rs.getLong(col++),
												  rs.getString(col++),
												  rs.getString(col++), 
												  rs.getDate(col++),
												  rs.getString(col++), 
												  rs.getString(col++), 
												  rs.getInt(col++),
												  rs.getDouble(col++),
												  rs.getInt(col++),
												  rs.getDouble(col++),
												  rs.getInt(col++),
												  rs.getDouble(col++),
												  rs.getInt(col++),
												  rs.getDouble(col++),
												  rs.getInt(col++),
												  rs.getDouble(col++),
												  rs.getDouble(col++),
												  rs.getDate(col++)) );
			}
			log.debug("Loaded " + results.size() + " rows from PRICEPLAN_RATES.");			
			return results;
		} finally {
			if (stmt != null) { stmt.close(); }
		}
	}
}
