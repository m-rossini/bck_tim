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
 * @author framos
 * @version $Id$
 *
 */
public class ServicePlanCache extends ReferenceDataCache {



	private static final Logger log = Logger.getLogger(ServicePlanCache.class);




	protected static final String SELECT_STATEMENT =
		"SELECT a.OBJID, a.PLANS_UID, a.SERVICES_UID, a.CONTAIN_FLG, a.CUSTOM_1, "+
		"a.CUSTOM_2, a.CUSTOM_3, b.SHORT_DESC, c.PLAN_NAME, c.STATE " +
		"FROM QLF_SERVICES_PLANS a " +
		"JOIN QLF_SERVICES b ON a.SERVICES_UID = b.OBJID " +
		"JOIN QLF_PLANS c ON a.PLANS_UID = c.OBJID";

	protected static final String SELECT_LAZY_STATEMENT =
		SELECT_STATEMENT +
		" WHERE a.SERVICES_UID = ? and a.PLANS_UID = ?";

	protected static final String SELECT_ALTERNATE_LAZY_STATEMENT =
		SELECT_STATEMENT +
		" WHERE b.SHORT_DESC = ? " +
		" AND c.PLAN_NAME = ? " +
	    " AND ( c.STATE = ? " +
	    "   OR  c.STATE is NULL ) " +
	    " ORDER BY c.STATE" ;




	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#createVO(java.sql.ResultSet)
	 */
	protected CacheableVO createVO(ResultSet _rset) throws SQLException {
		if (_rset == null) { return null; }
		int colIdx = 1;
		ServicePlanVO sp = new ServicePlanVO();
		sp.setUid(_rset.getLong(colIdx++));
		sp.setPlanUid(_rset.getLong(colIdx++));
		sp.setServiceUid(_rset.getLong(colIdx++));
		sp.setContained(charToBool(_rset.getString(colIdx++)));
		sp.setCustom1(_rset.getString(colIdx++));
		sp.setCustom2(_rset.getString(colIdx++));
		sp.setCustom3(_rset.getString(colIdx++));
		sp.setServiceCode(_rset.getString(colIdx++));
		sp.setPlanCode(_rset.getString(colIdx++));
		sp.setPlanState(_rset.getString(colIdx++));
		return sp;
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
		ServicePlanVO.ServicePlanNaturalKey key = (ServicePlanVO.ServicePlanNaturalKey) _key;
		_stmt.setLong(1, key.getService());
		_stmt.setLong(2, key.getPlan());
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
		ServicePlanVO.ServicePlanAlternateKey key = (ServicePlanVO.ServicePlanAlternateKey) _key;
		_stmt.setString(1, key.getService());
		_stmt.setString(2, key.getPlan());
		_stmt.setString(3, key.getState());
	}


	private final boolean charToBool(String _bool) {
		return ("T".equalsIgnoreCase(_bool));
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
