/**
 * Copyright (c) 2004-2009 Auster Solutions. All Rights Reserved.
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
 * Created on Jun 17, 2009
 */
package br.com.auster.tim.billcheckout.param;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.NamingException;

import org.apache.log4j.Logger;

import br.com.auster.billcheckout.caches.CacheableKey;
import br.com.auster.billcheckout.caches.CacheableVO;
import br.com.auster.billcheckout.caches.ReferenceDataCache;
import br.com.auster.common.sql.SQLConnectionManager;

/**
 * @author Nardo
 * @version $Id$
 * @since JDK1.4
 */
public class ElegibilityCache extends ReferenceDataCache{
	
	private static final Logger log = Logger.getLogger(ElegibilityCache.class);
	
	protected static final String SELECT_ALTERNATE_LAZY_STATEMENT = "EligibilityFlatCache.loadCache";
	protected static final String SELECT_LAZY_STATEMENT = "EligibilityLACache.loadCache";

	@Override
	protected CacheableVO createVO(ResultSet _rset) throws SQLException {
		if (_rset == null) { return null; }
		ElegibilityVO vo = new ElegibilityVO();
		
		vo.setCodPromo(_rset.getLong("COD_PROMO"));
		vo.setIndEstrutura(_rset.getString("IND_ESTRUTURA"));
		vo.setCodPlanoIndiv(_rset.getString("COD_PLANO_INDIVIDUAL"));
		vo.setCodServIndiv(_rset.getString("COD_SERV_INDIVIDUAL"));
		vo.setCodPcteIndiv(_rset.getString("COD_PCTE_INDIVIDUAL"));
		vo.setCodPlanoLA(_rset.getString("COD_PLANO_LA"));
		vo.setCodServLA(_rset.getString("COD_SERV_LA"));
		vo.setCodPcteLA(_rset.getString("COD_PCTE_LA"));
		vo.setCodServDesvio(_rset.getString("COD_SERV_DESVIO"));
		vo.setVlrDesvio(_rset.getLong("VLR_DESVIO"));
		vo.setDataInicioVig(_rset.getDate("DATA_INICIO_VIGENCIA"));
		vo.setDataFimVig(_rset.getDate("DATA_FIM_VIGENCIA"));
		
		return vo;
	}

	@Override
	protected String getAlternateLazySQL() {//quando alternate==true
		try {
			SQLConnectionManager sqlConn = SQLConnectionManager.getInstance(this.poolName);
			return sqlConn.getStatement(SELECT_ALTERNATE_LAZY_STATEMENT).getStatementText();
		} catch (NamingException ne) {
			log.error("Cannot load statement:" + SELECT_ALTERNATE_LAZY_STATEMENT, ne);
		}
		return null;
	}

	@Override
	protected String getLazySQL() { //quando alternate==false
		try {
		SQLConnectionManager sqlConn = SQLConnectionManager.getInstance(this.poolName);
		return sqlConn.getStatement(SELECT_LAZY_STATEMENT).getStatementText();
	} catch (NamingException ne) {
		log.error("Cannot load statement:" + SELECT_LAZY_STATEMENT, ne);
	}
	return null;
	}

	@Override
	protected String getNonLazySQL() { //lazy-cache="false", não usado
		throw new IllegalStateException("Cannot use this cache in non-lazy mode");
	}

	@Override
	protected void setAlternateLazySQLParameters(PreparedStatement _stmt, CacheableKey _key) throws SQLException {
		ElegibilityVO.InnerAlternateKey key = (ElegibilityVO.InnerAlternateKey) _key;
		_stmt.setString(1, "%" + key.getPlanShort() + "%");
	}

	@Override
	protected void setLazySQLParameters(PreparedStatement _stmt, CacheableKey _key) throws SQLException {
		this.setAlternateLazySQLParameters(_stmt, _key);
	}
	
	/**
	 * Sobrecarga deste método para setar parâmetro useAlternate, substituindo use-alternate="" das duas querys
	 * do arquivo bscs-sql.xml. Assim, utilizo o mesmo cache, variando as querys conforme o tipo de cliente (Flat-F ou Large Account-L)
	 */
	public ArrayList<CacheableVO> getFromCache(CacheableKey key, String clientType) {
		try {
			if ("F".equalsIgnoreCase(clientType)){			
				useAlternate = true;
			}else if("L".equalsIgnoreCase(clientType)){
				  	useAlternate = false;
			}
			lock.readLock().lockInterruptibly();
			log.debug("Searching for key " + key);
			ArrayList<CacheableVO> listVO = (ArrayList<CacheableVO>) this.cache.get(key);
			if (listVO == null) {
				log.debug("Key " + key + " not found in cache. Going to database");
				if (this.notFoundSet.contains(key)) {
					log.debug("Key " + key + " found in not-found set. Resuming search.");
					return null;
				}
				this.loadFromDatabase(key);
				listVO = (ArrayList<CacheableVO>) this.cache.get(key);
				if (listVO == null) {
					log.debug("Key " + key + " not found in database. Added to not-found set");
					this.notFoundSet.add(key);
				} else {
					log.debug("Key " + key + " found in database.");
				}
			}
			return listVO;
		} catch (InterruptedException ie) {
			throw new IllegalStateException(ie);
		} finally {
			lock.readLock().unlock();
		}
	}
	
	protected void loadFromDatabase(CacheableKey _key) {
		Connection conn=null;
		PreparedStatement stmt=null;
		try {
			conn = this.getConnection();
			if (this.useAlternate) {
				stmt = conn.prepareStatement(this.getAlternateLazySQL());
				this.setAlternateLazySQLParameters(stmt, _key);
			} else {
				stmt = conn.prepareStatement(this.getLazySQL());
				this.setLazySQLParameters(stmt, _key);
			}
			loadIntoCache(stmt, true, _key);
		} catch (Exception e) {
			log.error("Could not load non-lazy cache", e);
		} finally {
				try {
					if (stmt != null) {	stmt.close(); }
					if (conn != null) {	conn.close(); }
			} catch (SQLException e) {
				log.error("Exception populating cache", e);
			}
		}
	}

	protected void loadIntoCache(PreparedStatement _stmt, boolean _overflowAllowed, CacheableKey _key) throws SQLException {
		ResultSet rs = null;
		ArrayList<CacheableVO> list = new ArrayList<CacheableVO>();
		try {
			rs = _stmt.executeQuery();
			while (rs.next()) {
				CacheableVO obj = createVO(rs);
				list.add(obj);
			}
			if (!readAndAddToCache(list, _overflowAllowed, _key)){
				return; //não põe em cache se overflow
			}
		} finally {
			try {
				if (rs != null) {	rs.close();	}
			} catch (SQLException e) {
				log.error("Exception populating cache", e);
			}
		}
	}
	
	protected final boolean readAndAddToCache(ArrayList<CacheableVO> list, boolean _overflowAllowed, CacheableKey _key) throws SQLException {
//		Se cache cheio, e _overflowAllowed==true(padrao em ReferenceDataCache), vai sobrescrevendo o cache, com max de cache-size do XML
		if ((this.cache.isFull()) && (!_overflowAllowed)) { 
			log.warn("Cache for key is full. Leaving the hungry populate.");
			return false;
		}
		this.putInCache(_key, list);
		return true;
	}
	
	public void putInCache(CacheableKey key, ArrayList<CacheableVO> obj) {
		cache.put(key, obj);
		log.debug("Cache has size of:" + this.cache.size() + " and a max size of:"+ this.cache.maxSize() + ".Obj:[" + obj + "]");
	}
}