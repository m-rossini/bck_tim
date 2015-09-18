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
 * Created on 24/10/2007
 */
package br.com.auster.tim.billcheckout.param;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
public class MpufftabListCache extends ReferenceDataCache {

	@Override
	public CacheableVO getFromCache(CacheableKey key) {
		// TODO Auto-generated method stub
		return super.getFromCache(key);
	}

	private static final Logger log = Logger.getLogger(MpufftabListCache.class);
	
	protected static final String SELECT_STATEMENT = 
		"SELECT a.FFCODE, a.CO_ID, a.SHDES, a.PRICE_VALUE, a.SCALEFACTOR, a.SNCODE, a.ZNCODE, a.USER_LIMIT, a.LIMITED_FLAG, a.UMCODE " +
		"FROM MPUFFTAB a ";	
	
	protected static final String SELECT_ALTERNATE_LAZY_STATEMENT = 
		SELECT_STATEMENT +
		"WHERE a.CO_ID = ? ";
	
	@Override
	protected CacheableVO createVO(ResultSet _rset) throws SQLException {
		if (_rset == null) { return null; }

		MpufftabListVO vo = new MpufftabListVO();
		MpufftabVO mpufftabVO;		
				
		while(_rset.next()) {
			vo.setContractNumber(_rset.getString(2));
			mpufftabVO = new MpufftabVO();
			mpufftabVO.setFfCode(_rset.getString(1));
			mpufftabVO.setContractNumber(_rset.getString(2));
			mpufftabVO.setPromotionCode(_rset.getString(3));
			mpufftabVO.setPriceValue(_rset.getDouble(4));
			mpufftabVO.setScalefactorStr(_rset.getString(5));
			mpufftabVO.setScalefactorDbl(_rset.getDouble(5));			
			mpufftabVO.setSnCode(_rset.getString(6));		
			mpufftabVO.setZnCode(_rset.getString(7));
			mpufftabVO.setUserLimit(_rset.getDouble(8));
			mpufftabVO.setLimitedFlag(_rset.getString(9));
			mpufftabVO.setUmcode(_rset.getInt(10));
			vo.add(mpufftabVO);
		}
		
		return vo;
	}

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#getAlternateLazySQL()
	 */
	protected String getAlternateLazySQL() {
		return SELECT_ALTERNATE_LAZY_STATEMENT;
	}

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#getLazySQL()
	 */
	protected String getLazySQL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getNonLazySQL() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#setLazySQLParameters(PreparedStatement, CacheableKey)
	 */
	protected void setLazySQLParameters(PreparedStatement _stmt, CacheableKey _key) throws SQLException {
		// TODO Auto-generated method stub
	}
	
	@Override
	protected void setAlternateLazySQLParameters(PreparedStatement _stmt, CacheableKey _key) throws SQLException {
		MpufftabListVO.InnerAlternateKey key = (MpufftabListVO.InnerAlternateKey) _key;
		_stmt.setString(1, key.getContractNumber());
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

}
