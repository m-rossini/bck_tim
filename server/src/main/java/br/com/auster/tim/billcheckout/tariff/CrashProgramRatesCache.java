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
 * Created on 11/10/2007
 */
package br.com.auster.tim.billcheckout.tariff;

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
public class CrashProgramRatesCache extends ReferenceDataCache {

	protected static final String SELECT_STATEMENT = "CrashProgramRatesCache.select";
	
	
	private static final Logger log = Logger.getLogger(CrashProgramRatesCache.class);
	

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#createVO(java.sql.ResultSet)
	 */
	protected CacheableVO createVO(ResultSet _rset) throws SQLException {
		if (_rset == null) { return null; }

		CachedCrashProgramRatesVO vo = new CachedCrashProgramRatesVO();
		
		int col=1;
		vo.setCostId( _rset.getString(col++) );
		vo.setCsgCode( _rset.getString(col++) );
		CrashProgramRatesVO inner = null;
		do {
			int innerCol = col;
			inner = new CrashProgramRatesVO();
			inner.setCostId( vo.getCostId() );
			inner.setCsgCode( vo.getCsgCode() );
			// now we get the other columns from the database
			inner.setInitVolume( _rset.getLong(innerCol++) );
			inner.setEndVolume( _rset.getLong(innerCol++) );
			inner.setPPM( _rset.getDouble(innerCol++) );
			vo.addRate(inner);
		} while (_rset.next());
		return vo;
	}

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#getNonLazySQL()
	 */
	protected String getNonLazySQL() {
		return null;
	}

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#getLazySQL()
	 */
	protected String getLazySQL() {
		try {
			return SQLConnectionManager.getInstance(this.poolName).getStatement(SELECT_STATEMENT).getStatementText();
		} catch (NamingException e) {
			log.error("Could not open statement: ", e);
		}
		return null;
	}

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#setLazySQLParameters(PreparedStatement, CacheableKey)
	 */
	protected void setLazySQLParameters(PreparedStatement _stmt, CacheableKey _key) throws SQLException {
		CachedCrashProgramRatesVO.InnerKey key = (CachedCrashProgramRatesVO.InnerKey) _key;
		_stmt.setString(1, key.getCostId());
		_stmt.setString(2, key.getCsgCode());
	}

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#getAlternateLazySQL()
	 */
	protected String getAlternateLazySQL() {
		return null;
	}

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#setAlternateLazySQLParameters(PreparedStatement, CacheableKey)
	 */
	protected void setAlternateLazySQLParameters(PreparedStatement _stmt, CacheableKey _key) throws SQLException  {
	}

}