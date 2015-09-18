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

import br.com.auster.tim.billcheckout.tariff.model.MeuSonhoTariff;
import br.com.auster.tim.billcheckout.tariff.model.NPackTariff;

/**
 * @author anardo 
 */
public class MeuSonhoTariffDAO {

	private static final Logger log = Logger.getLogger(MeuSonhoTariffDAO.class);

	private static final String SELECT_ALL = 
		" SELECT a.PACKAGE_NAME, a.LOADED_DATE, a.PPM, a.STATE, b.SHORT_DESC "+
		" FROM   MEUSONHO_RATES a " +
		" JOIN QLF_TARIFF_ZONE b ON a.TARIFF_ZONE_UID = b.OBJID " +
		" WHERE  a.STATE = ? ";
	
	private static final String FILTERBY_PACKAGE = " and a.PACKAGE_NAME = ? ";
	
	private static final String ORDERBY = 
		" ORDER BY a.PACKAGE_NAME, a.LOADED_DATE, a.PPM";


	
	public List listRates(Connection _conn, String _uf, String _selectedRow) throws SQLException {
		PreparedStatement stmt = null;
		try {
			String sql = SELECT_ALL;
			if (_selectedRow != null) {
				sql += FILTERBY_PACKAGE;
			}
			sql += ORDERBY;
			
			stmt = _conn.prepareStatement(sql);
			stmt.setString(1, _uf);
			if (_selectedRow != null) {
				stmt.setString(2, _selectedRow);
			}
			ResultSet rs = stmt.executeQuery();			
			List results = new LinkedList(); 		
			MeuSonhoTariff previousVO = null;
			while(rs.next()) {
				int col=1;
				// planUid, intRange, endRange
				//   effectiveDate, accessFee
				MeuSonhoTariff currentVO =  new MeuSonhoTariff(rs.getString(col++), 
					                                           rs. getDate(col++),
					                                           rs.getDouble(col++),
					                                           rs.getString(col++));
				
				if ((previousVO != null) && (previousVO.isSameKey(currentVO))) {
					previousVO.addTariffZone(rs.getString(col));
				} else {
					currentVO.addTariffZone(rs.getString(col));
					results.add(currentVO);
					previousVO = currentVO;
				}
			}
			log.debug("Loaded " + results.size() + " rows from MEUSONHO_RATES.");			
			return results;
		} finally {
			if (stmt != null) { stmt.close(); }
		}
	}
}
