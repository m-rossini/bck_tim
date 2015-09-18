/*
 * Copyright (c) 2004-2008 Auster Solutions. All Rights Reserved.
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
 * Created on 01/07/2008
 */
package br.com.auster.tim.billcheckout.bscs;

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
 * @author framos
 * @version $Id$
 *
 */
public class AnatelCodeCache extends ReferenceDataCache {


	private static final Logger log = Logger.getLogger(AnatelCodeCache.class);

	protected static final String SELECT_STATEMENT = "AnatelCodeCache.nonLazy";
	protected static final String CUSTOM_STATEMENT = "AnatelCodeCache.detectTIMFamilia";



	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#createVO(java.sql.ResultSet)
	 */
	protected CacheableVO createVO(ResultSet _rset) throws SQLException {
		AnatelCodeVO vo = new AnatelCodeVO();
		vo.setRateplanShdes(_rset.getString(1));
		vo.setPackageShortName(_rset.getString(2));
		vo.setUf(_rset.getString(3));
		vo.setAnatelCode(_rset.getString(4));
		return vo;		
	}

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#getAlternateLazySQL()
	 */
	protected String getAlternateLazySQL() {
		throw new IllegalArgumentException("Cannot use this cache as lazy alternate; only as direct lazy");
	}

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#getLazySQL()
	 */
	protected String getLazySQL() {
		try {
			return SQLConnectionManager.getInstance(this.poolName).getStatement(SELECT_STATEMENT).getStatementText();
		} catch (NamingException ne) {
			log.error("Could not load sql statement", ne);
		}
		return null;		
	}

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#getNonLazySQL()
	 */
	protected String getNonLazySQL() {
		throw new IllegalArgumentException("Cannot use this cache as non-lazy; only as direct lazy");
	}

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#setAlternateLazySQLParameters(java.sql.PreparedStatement, br.com.auster.billcheckout.caches.CacheableKey)
	 */
	protected void setAlternateLazySQLParameters(PreparedStatement _stmt, CacheableKey _key) throws SQLException {
		throw new IllegalArgumentException("Cannot use this cache as lazy alternate; only as direct lazy");
	}

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#setLazySQLParameters(java.sql.PreparedStatement, br.com.auster.billcheckout.caches.CacheableKey)
	 */
	protected void setLazySQLParameters(PreparedStatement _stmt, CacheableKey _key) throws SQLException {
		AnatelCodeVO.InnerKey innerKey = (AnatelCodeVO.InnerKey) _key;
		_stmt.setString(1, innerKey.getRateplanShdes());
		if (innerKey.getPackageShortName() == null) {
			_stmt.setString(2, "");
		} else {
			_stmt.setString(2, innerKey.getPackageShortName());
		}
		_stmt.setString(3, innerKey.getUf());
	}

	
	// customization to check if plan should be discarded
	public boolean hasMoreThanOnePackage(String _rateplan, String _uf) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			conn  = SQLConnectionManager.getInstance(this.poolName).getConnection();
			String sql = SQLConnectionManager.getInstance(this.poolName).getStatement(CUSTOM_STATEMENT).getStatementText();
			log.debug("Running statement " + sql);
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, _rateplan);
			stmt.setString(2, _uf);
			rset = stmt.executeQuery();
			// if more than 1 in count
			return (rset.next() && rset.getInt(1) > 1);
		} catch (SQLException sqle) { 
			log.error("Exception when running query", sqle);
		} catch (NamingException ne) {
			log.error("Exception when running query", ne);
		} finally {
			try {
				if (rset != null) { rset.close(); }
				if (stmt != null) { stmt.close(); }
				if (conn != null) { conn.close(); }
			} catch (SQLException sqle) {
				log.error("Exception when closing resources", sqle);
			}
		}
		return false;
	}
}
