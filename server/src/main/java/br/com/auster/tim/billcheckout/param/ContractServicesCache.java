/*
 * Copyright (c) 2004-2007 Auster Solutions. All Rights Reserved.
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
 * Created on 15/10/2007
 */
package br.com.auster.tim.billcheckout.param;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.NamingException;

import org.apache.log4j.Logger;

import br.com.auster.billcheckout.caches.CacheableKey;
import br.com.auster.billcheckout.caches.CacheableVO;
import br.com.auster.billcheckout.caches.ReferenceDataCache;
import br.com.auster.common.sql.SQLConnectionManager;

/**
 * TODO What this class is responsible for
 *
 * @author William Soares
 * @version $Id$
 * @since JDK1.4
 */
public class ContractServicesCache extends ReferenceDataCache {


	private static final Logger log = Logger.getLogger(ContractServicesCache.class);

	protected static final String SELECT_ALTERNATE_LAZY_STATEMENT = "ContractServicesCache.loadCache";

	protected CacheableKey tempKey;

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#createVO(java.sql.ResultSet)
	 */
	protected CacheableVO createVO(ResultSet _rset) throws SQLException {
		if (_rset == null) { return null; }

		ContractServicesVO vo = new ContractServicesVO();
		vo.setSnCode(_rset.getString(1));
		vo.setContractNumber(_rset.getString(2));
		vo.setCsStatChng(_rset.getString(3));
		vo.setTmCode(_rset.getLong(4));

		return vo;
	}

	@Override
	protected void loadFromDatabase(CacheableKey _key) {
		this.tempKey = _key;
		Connection conn=null;
		PreparedStatement stmt=null;
		try {
			conn = this.getConnection();
			if (this.useAlternate) {
				stmt = conn.prepareStatement(this.getPreparedAlternateLazySQL(_key));
				this.setAlternateLazySQLParameters(stmt, _key);
			} else {
				stmt = conn.prepareStatement(this.getLazySQL());
				this.setLazySQLParameters(stmt, _key);
			}
			loadIntoCache(stmt, true);
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

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#getNonLazySQL()
	 */
	protected String getNonLazySQL() {
		throw new IllegalArgumentException("Cannot use this cache as non-lazy; only lazy with alternateKey");
	}

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#getLazySQL()
	 */
	protected String getLazySQL() {
		throw new IllegalArgumentException("Cannot use this cache with key; only alternateKey");
	}

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#setLazySQLParameters(PreparedStatement, CacheableKey)
	 */
	protected void setLazySQLParameters(PreparedStatement _stmt, CacheableKey _key) throws SQLException {
		throw new IllegalArgumentException("Cannot use this cache with key; only alternateKey");
	}

	protected String getAlternateLazySQL() {
		throw new IllegalArgumentException("Cannot use this cache with key; this class uses the customized preparedAlternateLazySQL");
	}

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#getAlternateLazySQL()
	 */
	protected String getPreparedAlternateLazySQL(CacheableKey _key) {
		ContractServicesVO.InnerAlternateKey key = (ContractServicesVO.InnerAlternateKey) _key;
		try {
			String preparedSelectStr = SQLConnectionManager.getInstance(this.poolName).getStatement(SELECT_ALTERNATE_LAZY_STATEMENT).getStatementText();
			preparedSelectStr = new String(preparedSelectStr.replace("%", key.getSnCode()));
			return preparedSelectStr;
		} catch (NamingException ne) {
			log.error("Could not load sql statement", ne);
		}
		return null;
	}

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#setAlternateLazySQLParameters(PreparedStatement, CacheableKey)
	 */
	protected void setAlternateLazySQLParameters(PreparedStatement _stmt, CacheableKey _key) throws SQLException  {
		ContractServicesVO.InnerAlternateKey key = (ContractServicesVO.InnerAlternateKey) _key;
		_stmt.setString(1, key.getContractNumber());
		_stmt.setLong(2, key.getTmCode());
	}


	protected void loadIntoCache(PreparedStatement _stmt, boolean _overflowAllowed) throws SQLException {
		ResultSet rs = null;
		try {
			rs = _stmt.executeQuery();
			while (rs.next()) {
				if (!readAndAddToCacheLocal(rs, _overflowAllowed)) {
					break;
				}
			}
		} finally {
			try {
				if (rs != null) {	rs.close();	}
			} catch (SQLException e) {
				log.error("Exception populating cache", e);
			}
		}
	}

	protected final boolean readAndAddToCacheLocal(ResultSet _rset, boolean _overflowAllowed) throws SQLException {
		CacheableVO obj = createVO(_rset);
		if ((this.cache.isFull()) && (!_overflowAllowed)) {
			log.warn("Cache for key is full. Leaving the hungry populate.");
			return false;
		}
		this.putInCache(this.tempKey, obj);
		return true;
	}
}