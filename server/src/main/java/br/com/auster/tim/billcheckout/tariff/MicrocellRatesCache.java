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
public class MicrocellRatesCache extends ReferenceDataCache {

	protected static final String SELECT_STATEMENT = "MicrocellRatesCache.select";

	
	private static final Logger log = Logger.getLogger(MicrocellRatesCache.class);
	

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#createVO(java.sql.ResultSet)
	 */
	protected CacheableVO createVO(ResultSet _rset) throws SQLException {
		if (_rset == null) { return null; }

		CachedMicrocellRatesVO vo = new CachedMicrocellRatesVO();
		
		int col=1;
		vo.setPlanName( _rset.getString(col++) );
		vo.setState( _rset.getString(col++) );
		vo.setSnCode( _rset.getString(col++) );
		vo.setDescription( _rset.getString(col++) );
		MicrocellRatesVO previousInner = null;
		do {
			int innerCol = col;
			MicrocellRatesVO innerVO = new MicrocellRatesVO();
			innerVO.setPlanName( vo.getPlanName() );
			innerVO.setState( vo.getState() );
			innerVO.setSnCode( vo.getSnCode() );
			// now we get the other columns from the database
			innerVO.setPlanUid( _rset.getLong(innerCol++) );
			innerVO.setEffectiveDate( _rset.getDate(innerCol++) );
			innerVO.setPriceValue( _rset.getDouble(innerCol++) );
			// getting SCALEFACTOR twice
			innerVO.setScalefactorDbl( _rset.getDouble(innerCol) );
			innerVO.setScalefactorStr( _rset.getString(innerCol++) );
			innerVO.setUmcode( _rset.getInt(innerCol++) );
			// adding inner vo to range map
			if (previousInner != null) {
				vo.addMicrocell( previousInner, innerVO.getEffectiveDate());
			}
			previousInner = innerVO;
		} while (_rset.next());
		// adding the last element
		if (previousInner != null) {
			vo.addMicrocell( previousInner, Long.MAX_VALUE-1);
		}		
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
		CachedMicrocellRatesVO.InnerKey key = (CachedMicrocellRatesVO.InnerKey) _key;
		_stmt.setString(1, key.getSncode());
		_stmt.setString(2, key.getPlanName());
		_stmt.setString(3, key.getState());
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