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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Stack;

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
public class RateplanHistCache extends ReferenceDataCache {


	private static final Logger log = Logger.getLogger(RateplanHistCache.class);

	protected static final String SELECT_STATEMENT = "RateplanHistCache.loadCache";



	/*
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#createVO(java.sql.ResultSet)
	 */
	@Override
	protected CacheableVO createVO(ResultSet _rset) throws SQLException {
		RateplanHistVO vo = new RateplanHistVO();
		// reading info
		Stack<Integer> tmcodes = new Stack<Integer>();
		Stack<Date> dates = new Stack<Date>();
		vo.setContractNumber(_rset.getString(3));
		do {
			tmcodes.push(new Integer(_rset.getInt(1)));
			dates.push(_rset.getDate(2));
		} while (_rset.next());
		// building vo
		Date end = null;
		int seqno=tmcodes.size();
		while (!tmcodes.isEmpty()) {
			Date start = dates.pop();
			Integer tmcode = tmcodes.pop();
			vo.addRange(start, end, seqno--, tmcode);
			end = start;
		}
		return vo;
	}

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#getAlternateLazySQL()
	 */
	protected String getAlternateLazySQL() {
		try {
			return SQLConnectionManager.getInstance(this.poolName).getStatement(SELECT_STATEMENT).getStatementText();
		} catch (NamingException ne) {
			log.error("Could not load sql statement", ne);
		}
		return null;

	}

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#getLazySQL()
	 */
	protected String getLazySQL() {
		throw new IllegalArgumentException("Cannot use this cache with key; only alternateKey");
	}

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#getNonLazySQL()
	 */
	protected String getNonLazySQL() {
		throw new IllegalArgumentException("Cannot use this cache as non-lazy; only lazy with alternateKey");
	}

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#setAlternateLazySQLParameters(java.sql.PreparedStatement, br.com.auster.billcheckout.caches.CacheableKey)
	 */
	protected void setAlternateLazySQLParameters(PreparedStatement _stmt, CacheableKey _key) throws SQLException {
		RateplanHistVO.InnerKey innerKey = (RateplanHistVO.InnerKey) _key;
		_stmt.setString(1, innerKey.getContractNumber());
	}

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#setLazySQLParameters(java.sql.PreparedStatement, br.com.auster.billcheckout.caches.CacheableKey)
	 */
	protected void setLazySQLParameters(PreparedStatement _stmt, CacheableKey _key) throws SQLException {
		throw new IllegalArgumentException("Cannot use this cache with key; only alternateKey");
	}

}
