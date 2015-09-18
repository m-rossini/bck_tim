/*
 * Copyright (c) 2004-2006 Auster Solutions. All Rights Reserved.
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
 * Created on 02/12/2006
 */
package br.com.auster.tim.billcheckout.bscs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.auster.billcheckout.caches.CacheableKey;
import br.com.auster.billcheckout.caches.CacheableVO;
import br.com.auster.billcheckout.caches.ReferenceDataCache;
import br.com.auster.common.datastruct.RangeMap;

/**
 * @author framos
 * @version $Id$
 *
 */
public class SGTCustomCache extends ReferenceDataCache {


	private static final Logger log = Logger.getLogger(SGTCustomCache.class);


	protected static final String SELECT_STATEMENT =
		"SELECT a.CLIENTE, a.DATA_ATIVACAO, a.VALOR_DESVIO_ASSIN_PLANO " +
		"FROM ACC_SGT_REL_CUSTOMIZACOES a ";

	protected static final String SELECT_LAZY_STATEMENT =
		SELECT_STATEMENT +
		" WHERE a.CLIENTE = ? ";



	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#createVO(java.sql.ResultSet)
	 */
	protected CacheableVO createVO(ResultSet _rset) throws SQLException {
		if (_rset == null) { return null; }
		int colIdx = 1;
		SGTCustomVO vo = new SGTCustomVO();
		vo.setCustCode(_rset.getString(colIdx++));
		vo.setActivationDate(_rset.getDate(colIdx++));
		vo.setSubscriptionFee(_rset.getDouble(colIdx++));
		log.debug("Loaded rate with objid " + vo.getUid());
		return vo;
	}

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#getNonLazySQL()
	 */
	protected String getNonLazySQL() {
		return SELECT_STATEMENT;
	}

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#getLazySQL()
	 */
	protected String getLazySQL() {
		return SELECT_LAZY_STATEMENT;
	}

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#setLazySQLParameters(PreparedStatement, CacheableKey)
	 */
	protected void setLazySQLParameters(PreparedStatement _stmt, CacheableKey _key) throws SQLException {
		SGTCustomVO.InnerKey key = (SGTCustomVO.InnerKey) _key;
		_stmt.setString(1, key.getCustCode());
	}

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#getAlternateLazySQL()
	 */
	protected String getAlternateLazySQL() {
		return SELECT_LAZY_STATEMENT;
	}

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#setAlternateLazySQLParameters(PreparedStatement, CacheableKey)
	 */
	protected void setAlternateLazySQLParameters(PreparedStatement _stmt, CacheableKey _key) throws SQLException  {
		this.setLazySQLParameters(_stmt, _key);
	}


	// ---------------------------
	// Overrided methods
	//
	// This is needed due to the complexity of caching date intervals
	// ---------------------------

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
		try {
			rs = _stmt.executeQuery();
			while (rs.next()) {
				if (!readAndAddToCache(rs, _overflowAllowed, _key)) {
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

	protected final boolean readAndAddToCache(ResultSet _rset, boolean _overflowAllowed, CacheableKey _key) throws SQLException {
		SGTCustomVO obj = (SGTCustomVO) createVO(_rset);
		// cloning key so it has no dates
		SGTCustomVO.InnerKey altKey = (SGTCustomVO.InnerKey) _key;
		CacheableKey clonedKey = SGTCustomVO.createAlternateKey(altKey.getCustCode(), null);
		RangeMap cachedMap  = (RangeMap) this.cache.get(clonedKey);
		if (cachedMap == null) {
			if ((this.cache.isFull()) && (!_overflowAllowed)) {
				log.warn("Cache for key is full. Leaving the hungry populate.");
				return false;
			}
			cachedMap = new RangeMap();
			this.cache.put(clonedKey, cachedMap);
		}
		long from = obj.getActivationDate().getTime();
		cachedMap.add(from, Long.MAX_VALUE, obj);
		log.debug("Cache has size of:" + this.cache.size() + " and a max size of:"+ this.cache.maxSize() + ".Obj:[" + obj + "]");
		return true;
	}

	public CacheableVO getFromCache(CacheableKey key) {
		try {
			lock.readLock().lockInterruptibly();
			log.debug("Searching for key " + key);
			// cloning key so it has no dates
			SGTCustomVO.InnerKey altKey = (SGTCustomVO.InnerKey) key;
			CacheableKey clonedKey = SGTCustomVO.createAlternateKey(altKey.getCustCode(), null);
			RangeMap cachedMap = (RangeMap) this.cache.get(clonedKey);
			if (cachedMap == null) {
				log.debug("Key " + key + " not found in cache. Going to database");
				if (this.notFoundSet.contains(clonedKey)) {
					log.debug("Key " + key + " found in not-found set. Resuming search.");
					return null;
				}
				this.loadFromDatabase(key);
				cachedMap = (RangeMap) this.cache.get(clonedKey);
				if (cachedMap == null) {
					log.debug("Key " + key + " not found in database. Added to not-found set");
					this.notFoundSet.add(clonedKey);
					return null;
				} else {
					log.debug("Key " + key + " found in database.");
				}
			}
			List<SGTCustomVO> results = cachedMap.get(altKey.getActivationDate().getTime());
			// this is probably faster then sorting the collection, since there might be fewer then 5-10 records
			long minDate = Long.MIN_VALUE;
			SGTCustomVO resultedVO = null;
			if (results != null) {
				for (SGTCustomVO vo : results) {
					// search the max date which is less then the altKey act.Date
					if ((minDate < vo.getActivationDate().getTime()) &&
						(vo.getActivationDate().before(altKey.getActivationDate()))) {
						minDate = vo.getActivationDate().getTime();
						resultedVO = vo;
					}
				}
			}
			return resultedVO;
		} catch (InterruptedException ie) {
			throw new IllegalStateException(ie);
		} finally {
			lock.readLock().unlock();
		}
	}

	public boolean isUseAlternateKey() {
		return useAlternate;
	}

}
