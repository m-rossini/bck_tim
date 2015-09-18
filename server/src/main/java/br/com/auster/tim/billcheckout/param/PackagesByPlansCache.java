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
 * Created on 03/10/2007
 */
package br.com.auster.tim.billcheckout.param;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import br.com.auster.billcheckout.caches.CacheableKey;
import br.com.auster.billcheckout.caches.CacheableVO;
import br.com.auster.billcheckout.caches.ReferenceDataCache;

/**
 * TODO What this class is responsible for
 *
 * @author William Soares
 * @version $Id$
 * @since JDK1.4
 */

public class PackagesByPlansCache extends ReferenceDataCache {


	private static final Logger log = Logger.getLogger(PackagesByPlansCache.class);


	protected static final String SELECT_STATEMENT =
		"SELECT c.PLAN_UID, c.PACKAGE_UID, a.PLAN_NAME, a.STATE, b.DESCRIPTION, a.TMCODE " +
		"FROM QLF_PLANS a, QLF_PACKAGE b, QLF_PLANS_PACKAGES c";

	protected static final String SELECT_LAZY_STATEMENT =
		SELECT_STATEMENT +
		" WHERE a.PLAN_UID = ? ";

	protected static final String SELECT_ALTERNATE_LAZY_STATEMENT =
		SELECT_STATEMENT +
		" WHERE a.OBJID = c.PLAN_UID " +
	    "       AND b.OBJID = c.PACKAGE_UID " +
	    "       AND a.TMCODE = ? " +
	    "       AND ( a.STATE = ? " +
	    "          OR  ('/'||TRIM(a.STATE)||'/') = '//' ) " +
	    "       AND b.DESCRIPTION = ? " +
	    " ORDER BY a.STATE" ;

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#createVO(java.sql.ResultSet)
	 */
	protected CacheableVO createVO(ResultSet _rset) throws SQLException {
		if (_rset == null) { return null; }
		PackagesByPlansVO vo = new PackagesByPlansVO();
		vo.setPlanId(_rset.getLong(1));
		vo.setPackageId(_rset.getLong(2));
		vo.setPlanName(_rset.getString(3));
		vo.setState(_rset.getString(4));
		vo.setDescription(_rset.getString(5));
		vo.setTMCode(_rset.getInt(6));
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
		PackagesByPlansVO.InnerKey key = (PackagesByPlansVO.InnerKey) _key;
		_stmt.setString(1, key.getPlanName());
	}

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#getAlternateLazySQL()
	 */
	protected String getAlternateLazySQL() {
		return SELECT_ALTERNATE_LAZY_STATEMENT;
	}

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#setAlternateLazySQLParameters(PreparedStatement, CacheableKey)
	 */
	protected void setAlternateLazySQLParameters(PreparedStatement _stmt, CacheableKey _key) throws SQLException  {
		PackagesByPlansVO.InnerAlternateKey key = (PackagesByPlansVO.InnerAlternateKey) _key;
		_stmt.setInt(1, key.getTMCode());
		_stmt.setString(2, key.getState());
		_stmt.setString(3, key.getDescription());
	}


	/**
	 * Due to the possibility of STATE null, we need to force these next three overrides
	 */
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
				if (!localReadAndAddToCache(rs, _overflowAllowed, _key)) {
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

	private boolean localReadAndAddToCache(ResultSet _rset, boolean _overflowAllowed, CacheableKey _key) throws SQLException {
		CacheableVO obj = createVO(_rset);
		if ((this.cache.isFull()) && (!_overflowAllowed)) {
			log.warn("Cache for key is full. Leaving the hungry populate.");
			return false;
		}
		this.putInCache(_key, obj);
		return true;
	}


}