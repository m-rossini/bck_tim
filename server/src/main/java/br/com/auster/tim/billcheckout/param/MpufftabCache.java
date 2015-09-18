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
package br.com.auster.tim.billcheckout.param;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
public class MpufftabCache extends ReferenceDataCache {

	protected static final String SELECT_STATEMENT =
		"SELECT a.FFCODE, a.CO_ID, a.SHDES, a.PRICE_VALUE, a.SCALEFACTOR, a.SNCODE, a.ZNCODE, a.USER_LIMIT, a.LIMITED_FLAG, a.DES, a.UMCODE " +		
		"FROM MPUFFTAB a ";

	protected static final String SELECT_ALTERNATE_LAZY_STATEMENT =
		SELECT_STATEMENT +
		"WHERE a.CO_ID  = ? AND a.SHDES = ? ";

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#createVO(java.sql.ResultSet)
	 */
	protected CacheableVO createVO(ResultSet _rset) throws SQLException {
		if (_rset == null) { return null; }

		MpufftabVO vo = new MpufftabVO();
		vo.setFfCode(_rset.getString(1));
		vo.setContractNumber(_rset.getString(2));
		vo.setPromotionCode(_rset.getString(3));
		vo.setPriceValue(_rset.getDouble(4));
		vo.setScalefactorStr(_rset.getString(5));
		vo.setScalefactorDbl(_rset.getDouble(5));
		vo.setSnCode(_rset.getString(6));	
		vo.setZnCode(_rset.getString(7));
		vo.setUserLimit(_rset.getLong(8));
		vo.setLimitedFlag(_rset.getString(9));
		vo.setDescription(_rset.getString(10));
		vo.setUmcode(_rset.getInt(11));
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
		MpufftabVO.InnerAlternateKey key = (MpufftabVO.InnerAlternateKey) _key;
		_stmt.setString(1, key.getContractNumber());
		_stmt.setString(2, key.getPromotionCode());
	}

}