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

import br.com.auster.tim.billcheckout.tariff.model.MicrocellTariff;

/**
 * @author anardo 
 */
public class MicrocellTariffDAO {

	private static final Logger log = Logger.getLogger(MicrocellTariffDAO.class);

	public static final String SELECT_ALL = 
		"SELECT a.SHORT_DESC, a.DESCRIPTION, a.EFFECTIVE_DATE, a.LOADED_DATE, a.PRICE_VALUE, a.SCALEFACTOR, a.UMCODE "+
		"FROM   MICROCELL_RATES a "+
        "       JOIN QLF_PLANS b ON a.PLAN_UID = b.OBJID "+ 
		"WHERE  b.OBJID = ? ";
	
	public static final String FILTER_MAX_DATE =
        "       AND  a.EFFECTIVE_DATE = (SELECT MAX(EFFECTIVE_DATE) FROM MICROCELL_RATES "+
		"                                WHERE PLAN_UID = b.OBJID AND SHORT_DESC = a.SHORT_DESC)";
	
	public static final String FILTER_SHDES = " AND a.SHORT_DESC = ? ORDER BY a.EFFECTIVE_DATE ASC";

	public List listRates(Connection _conn, Long _rateplan, String shdesMC) throws SQLException {
		PreparedStatement stmt = null;
		try {
			if (shdesMC != null){ 
				//lista todas tarifas já cadastradas, de uma promoção, para determinado plano/estado
				stmt = _conn.prepareStatement(SELECT_ALL + FILTER_SHDES);
			}else{
				//lista a tarifa mais recente, de todas as promoções existentes para determinado plano/estado.
				stmt = _conn.prepareStatement(SELECT_ALL + FILTER_MAX_DATE); 
			}
			stmt.setLong(1, _rateplan.longValue());
			if(shdesMC != null){
				stmt.setString(2, shdesMC);
			}
			ResultSet rs = stmt.executeQuery();
			List results = new ArrayList(); 			
			while(rs.next()) {
				int col=1;
				results.add( new MicrocellTariff(rs.getString(col++),
											   	 rs.getString(col++), 
											   	 rs.getDate(col++),
											   	 rs.getDate(col++),
											   	 rs.getObject(col++),
											   	 rs.getObject(col++),
											   	 rs.getInt(col++)) );
			}
			log.debug("Loaded " + results.size() + " rows from MICROCELL_RATES.");			
			return results;
		} finally {
			if (stmt != null) { stmt.close(); }
		}
	}
}
