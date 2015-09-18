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
 * Created on 11/04/2008
 */
package br.com.auster.tim.billcheckout.param;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import javax.naming.NamingException;

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
public class ProgressiveDiscountCache extends ReferenceDataCache {

	private static final Logger log = Logger.getLogger(ProgressiveDiscountCache.class);

	protected static final String SELECT_STATEMENT =
		"SELECT A.objid, A.disc_description, A.service_shdes, A.state, B.range_name, C.lower_limit, C.upper_limit, C.discount_rate " +
		"FROM PROG_DISCOUNT_DESC A, PROG_DISCOUNT B, PROG_DISCOUNT_RATES C ";

	protected static final String SELECT_ALTERNATE_LAZY_STATEMENT =
		SELECT_STATEMENT +
		"WHERE A.disc_uid = B.objid " +
		"AND C.disc_uid = B.objid " +
		"AND A.disc_description = ? " +
		"AND (A.state = ? OR A.state IS NULL) " +
		"order by A.state, C.objid";


	protected static final String SELECT_PLANS_STATEMENT =
		"select distinct plan_name from qlf_plans " +
		" join prog_discount_plans on prog_discount_plans.plan_uid = qlf_plans.objid " +
		" join prog_discount_desc on  prog_discount_plans.discount_uid = prog_discount_desc.objid " +
		" where prog_discount_desc.objid = ?";

	@Override
	protected CacheableVO createVO(ResultSet _rset) throws SQLException {
		if (_rset == null) { return null; }

		ProgressiveDiscountVO vo = new ProgressiveDiscountVO();
		Set limitsSet = new HashSet();
		ProgDiscountLimitsVO limits;
		String state = "";
		boolean isNewVo = false;
		do {
			limits = new ProgDiscountLimitsVO(0);
			//verifies if it is the first line or it is a new VO
			if(_rset.isFirst()) {
				state = (_rset.getString(4)==null)?"":_rset.getString(4);
				vo.setUid(_rset.getLong(1));
				vo.setDiscountDesc(_rset.getString(2));
				vo.setShdes(_rset.getString(3));
				vo.setState(state);
				vo.setRangeName(_rset.getString(5));
				limits.setLowerLimit(_rset.getDouble(6));
				limits.setUpperLimit(_rset.getDouble(7));
				limits.setDiscRate(_rset.getDouble(8));
			} else {
				if(!state.equals((_rset.getString(4)==null)?"":_rset.getString(4))) {
					vo.setLimits(limitsSet);
					this.putInCache((this.isUseAlternateKey())? vo.getAlternateKey() : vo.getKey(), vo);
					limitsSet = new HashSet();
					vo = new ProgressiveDiscountVO();
					isNewVo = true;
					state = (_rset.getString(4)==null)?"":_rset.getString(4);
					vo.setDiscountDesc(_rset.getString(2));
					vo.setShdes(_rset.getString(3));
					vo.setState(state);
					vo.setRangeName(_rset.getString(5));
					limits.setLowerLimit(_rset.getDouble(6));
					limits.setUpperLimit(_rset.getDouble(7));
					limits.setDiscRate(_rset.getDouble(8));
				} else {
					limits.setLowerLimit(_rset.getDouble(6));
					limits.setUpperLimit(_rset.getDouble(7));
					limits.setDiscRate(_rset.getDouble(8));
				}
			}
			limitsSet.add(limits);
		} while(_rset.next());
		vo.setLimits(limitsSet);
		vo.setAllowedPlans(loadAllowedPlans(vo.getUid()));
		return vo;
	}

	@Override
	protected void loadIntoCache(PreparedStatement _stmt, boolean _overflowAllowed) throws SQLException {
		ResultSet rs = null;
		try {
			rs = _stmt.executeQuery();
			//verifies if there is at least one result
			if (rs.next()) {
				readAndAddToCache(rs, _overflowAllowed);
			}
		} finally {
			try {
				if (rs != null) {	rs.close();	}
			} catch (SQLException e) {
				log.error("Exception populating cache", e);
			}
		}
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
		return null;
	}

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#setLazySQLParameters(PreparedStatement, CacheableKey)
	 */
	protected void setLazySQLParameters(PreparedStatement _stmt, CacheableKey _key) throws SQLException {
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
		ProgressiveDiscountVO.InnerAlternateKey key = (ProgressiveDiscountVO.InnerAlternateKey) _key;
		_stmt.setString(1, key.getDiscountDesc());
		_stmt.setString(2, key.getState());
	}


	private Set<String> loadAllowedPlans(long _objid) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rset = null;
		Connection conn = null;
		TreeSet<String> l = new TreeSet<String>();
		try {
			conn = getConnection();
			stmt = conn.prepareStatement(SELECT_PLANS_STATEMENT);
			stmt.setLong(1, _objid);
			rset = stmt.executeQuery();
			while (rset.next()) {
				l.add(rset.getString(1));
			}
		} catch (NamingException ne) {
			log.error("Error while running loadAllowedPlans()", ne);
			throw new SQLException(ne.getLocalizedMessage());
		} finally {
			if (rset != null) { rset.close(); }
			if (stmt != null) { stmt.close(); }
			if (conn != null) { conn.close(); }
		}
		return l;
	}
}