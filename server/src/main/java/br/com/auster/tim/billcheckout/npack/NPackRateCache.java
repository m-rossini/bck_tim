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
package br.com.auster.tim.billcheckout.npack;

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
public class NPackRateCache extends ReferenceDataCache {

	protected static final String SELECT_STATEMENT = "NPackRateCache.select";

	
	private static final Logger log = Logger.getLogger(NPackRateCache.class);
	

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#createVO(java.sql.ResultSet)
	 */
	protected CacheableVO createVO(ResultSet _rset) throws SQLException {
		if (_rset == null) { return null; }

		int col=1;
		NPackRateVO vo = new NPackRateVO();
		vo.setPlanName(_rset.getString(col++));
		vo.setUF(_rset.getString(col++));
		vo.setEffectiveDate(_rset.getDate(col++));
		do {
			int startAtCol = col;
			long li = _rset.getLong(startAtCol++);
			long ls = Long.MAX_VALUE-1;
			if (_rset.getObject(startAtCol++) != null) {
				_rset.getLong(startAtCol);
			}
			vo.addPriceFor(li, ls, _rset.getDouble(startAtCol++));
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
		
		int col=1;
		NPackRateVO.InnerKey key = (NPackRateVO.InnerKey) _key;
		_stmt.setString(col++, key.getPlanName());
		_stmt.setString(col++, key.getUF());
		_stmt.setDate(col++,  new java.sql.Date( key.getInitDate().getTime() ));
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