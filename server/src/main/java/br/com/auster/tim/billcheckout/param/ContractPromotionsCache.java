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
 * Created on 10/10/2007
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
public class ContractPromotionsCache extends ReferenceDataCache {


	private static final Logger log = Logger.getLogger(ContractPromotionsCache.class);



	protected static final String SELECT_ALTERNATE_LAZY_STATEMENT = "ContractPromotionsCache.loadCache";

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#createVO(java.sql.ResultSet)
	 */
	protected CacheableVO createVO(ResultSet _rset) throws SQLException {
		if (_rset == null) { return null; }
		int colIdx = 1;
		ContractPromotionsVO vo = new ContractPromotionsVO();
		vo.setContractNumber(_rset.getString(colIdx++));
		vo.setPromotionCode(_rset.getString(colIdx++));
		vo.setActivationDate(_rset.getDate(colIdx++));
		vo.setExpirationDate(_rset.getDate(colIdx++));
		vo.setPromotionDesc(_rset.getString(colIdx++));
		return vo;
	}

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#getNonLazySQL()
	 */
	protected String getNonLazySQL() {
		throw new IllegalStateException("Cannot use this cache in non-lazy mode");
	}

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#getLazySQL()
	 */
	protected String getLazySQL() {
		try {
			SQLConnectionManager sqlConn = SQLConnectionManager.getInstance(this.poolName);
			return sqlConn.getStatement(SELECT_ALTERNATE_LAZY_STATEMENT).getStatementText();
		} catch (NamingException ne) {
			log.error("Cannot load statement:" + SELECT_ALTERNATE_LAZY_STATEMENT, ne);
		}
		return null;
	}

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#setLazySQLParameters(PreparedStatement, CacheableKey)
	 */
	protected void setLazySQLParameters(PreparedStatement _stmt, CacheableKey _key) throws SQLException {
		ContractPromotionsVO.InnerKey key = (ContractPromotionsVO.InnerKey) _key;
		_stmt.setString(1, key.getContractNumber());
	}

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#getAlternateLazySQL()
	 */
	protected String getAlternateLazySQL() {
		try {
			SQLConnectionManager sqlConn = SQLConnectionManager.getInstance(this.poolName);
			return sqlConn.getStatement(SELECT_ALTERNATE_LAZY_STATEMENT).getStatementText();
		} catch (NamingException ne) {
			log.error("Cannot load statement:" + SELECT_ALTERNATE_LAZY_STATEMENT, ne);
		}
		return null;
	}

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#setAlternateLazySQLParameters(PreparedStatement, CacheableKey)
	 */
	protected void setAlternateLazySQLParameters(PreparedStatement _stmt, CacheableKey _key) throws SQLException  {
		ContractPromotionsVO.InnerAlternateKey key = (ContractPromotionsVO.InnerAlternateKey) _key;
		_stmt.setString(1, key.getContractNumber());
	}

}
