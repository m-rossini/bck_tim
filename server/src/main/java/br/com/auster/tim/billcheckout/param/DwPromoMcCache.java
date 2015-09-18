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
 * Created on 22/04/2008
 */
package br.com.auster.tim.billcheckout.param;

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
public class DwPromoMcCache extends ReferenceDataCache {

	
	private static final Logger log = Logger.getLogger(DwPromoMcCache.class);
	
	protected static final String SELECT_ALTERNATE_LAZY_STATEMENT = "DwPromoMcCache.loadCache";
	
	
	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#createVO(java.sql.ResultSet)
	 */
	protected CacheableVO createVO(ResultSet _rset) throws SQLException {
		if (_rset == null) { return null; }		
		
		DwPromoMcVO vo = new DwPromoMcVO();
		
		String serviceCode = "";
		boolean firstLine = true;
		while(_rset.next()) {	
			if(firstLine) {
				vo.setShdes(_rset.getString(1));
				firstLine = false;
			} 
			serviceCode += _rset.getInt(2) + ",";
		}
		if(serviceCode != null && !serviceCode.trim().equals("")) {
			serviceCode = new String(serviceCode.substring(0, serviceCode.lastIndexOf(",")));
		}
		vo.setServiceCode(serviceCode);
		
		return vo;
	}

	@Override
	protected void loadIntoCache(PreparedStatement _stmt, boolean _overflowAllowed) throws SQLException {
		ResultSet rs = null;
		try {
			rs = _stmt.executeQuery();
			readAndAddToCache(rs, _overflowAllowed);
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

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#getAlternateLazySQL()
	 */
	protected String getAlternateLazySQL() {
		try {
			return SQLConnectionManager.getInstance(this.poolName).getStatement(SELECT_ALTERNATE_LAZY_STATEMENT).getStatementText();
		} catch (NamingException ne) {
			log.error("Could not load sql statement", ne);
		}
		return null;
	}

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#setAlternateLazySQLParameters(PreparedStatement, CacheableKey)
	 */
	protected void setAlternateLazySQLParameters(PreparedStatement _stmt, CacheableKey _key) throws SQLException  {
		DwPromoMcVO.InnerAlternateKey key = (DwPromoMcVO.InnerAlternateKey) _key;
		_stmt.setString(1, key.getShdes());		
	}
	
}
