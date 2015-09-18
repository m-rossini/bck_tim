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
 * Created on 18/05/2010
 */
package br.com.auster.tim.billcheckout.tariff.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.auster.tim.billcheckout.tariff.model.VoiceUsageTariff;

/**
 * @author anardo
 *
 */
public class VoiceUsageTariffDAO {

	private static final Logger log = Logger.getLogger(VoiceUsageTariffDAO.class);

	public static final String SELECT_ALL = 
	"SELECT a.TARIFF_ZONE_UID, a.SERVICES_UID, a.RATE_TIME_ZONE_UID, " +
	"		b.DESCRIPTION, c.DESCRIPTION, e.DESCRIPTION, a.EFFECTIVE_DATE, a.INCOMING_FLAG, a.INIT_VOLUME, "+
	"       a.END_VOLUME, a.STEP_VOLUME, a.STEP_COST, a.LOADED_DATE "+
	"FROM   USAGE_RATES a "+
	"       JOIN QLF_TARIFF_ZONE b ON a.TARIFF_ZONE_UID = b.OBJID "+
	"       JOIN QLF_RATE_TIME_ZONE c ON a.RATE_TIME_ZONE_UID = c.OBJID "+ 
	"       JOIN QLF_PLANS d ON a.PLANS_UID = d.OBJID "+
	"       JOIN QLF_SERVICES e ON a.SERVICES_UID = e.OBJID "+ 
	"WHERE  d.OBJID = ? ";
	
	private static final String FILTER_MAX_DATE = " AND a.EFFECTIVE_DATE = "+
	"          (SELECT MAX(EFFECTIVE_DATE) "+ 
	"             FROM   USAGE_RATES f "+
	"            WHERE   TARIFF_ZONE_UID = b.OBJID AND "+
	"                    RATE_TIME_ZONE_UID = c.OBJID AND "+ 
	"                    PLANS_UID = d.OBJID AND "+
	"                    SERVICES_UID = e.OBJID AND" +
	"					 f.INCOMING_FLAG = a.INCOMING_FLAG) "+
	"ORDER BY e.DESCRIPTION, b.DESCRIPTION, c.DESCRIPTION, a.INCOMING_FLAG, a.INIT_VOLUME ASC";
	
	private static final String FILTER_ROW = " AND a.TARIFF_ZONE_UID = ?   AND a.SERVICES_UID   = ? " +
											 " AND a.RATE_TIME_ZONE_UID= ? AND a.INCOMING_FLAG = ? " +
											 " ORDER BY a.EFFECTIVE_DATE ";

	public List listRates(Connection _conn, Long _rateplan, String _selectedRow) throws SQLException {
		PreparedStatement stmt = null;
		String[] array = null;
		try {
			if (_selectedRow != null){
				stmt = _conn.prepareStatement(SELECT_ALL + FILTER_ROW);
				array = _selectedRow.split("-");
			}else{
				stmt = _conn.prepareStatement(SELECT_ALL + FILTER_MAX_DATE);
			}
			stmt.setLong(1, _rateplan.longValue());
			if (array != null){
				if (array.length == 4){
					stmt.setLong(2, new Long(array[0]).longValue()); //tariffZoneUid
					stmt.setLong(3, new Long(array[1]).longValue()); //serviceUid
					stmt.setLong(4, new Long(array[2]).longValue()); //rateTimeZoneUid
					stmt.setString(5, boolToString(array[3])); //incomingFlag
				}else{
					return null;
				}
			}
			
			ResultSet rs = stmt.executeQuery();
			
			List results = new ArrayList(); 			
			while(rs.next()) {
				int col=1; 
				results.add( new VoiceUsageTariff(
												  rs.getLong(col++),
												  rs.getLong(col++),
												  rs.getLong(col++),
												  rs.getString(col++),
												  rs.getString(col++), 
												  rs.getString(col++), 
												  rs.getDate(col++),
												  StringToBool( rs.getString(col++) ), 
												  rs.getInt(col++), 
												  rs.getInt(col++),
												  rs.getInt(col++),
												  rs.getDouble(col++),
												  rs.getDate(col++)) );
			}
			log.debug("Loaded " + results.size() + " rows from USAGE_RATES.");			
			return results;
		} finally {
			if (stmt != null) { stmt.close(); }
		}
	}
	
	
	private boolean StringToBool(String _char) {
		return "Y".equalsIgnoreCase(_char);
	}
	private String boolToString(String _bool) {
		if("true".equalsIgnoreCase(_bool)){
			return "Y";
		}
		return "N";
	}
}
