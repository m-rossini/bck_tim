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
public class MpulkfxoCache extends ReferenceDataCache{

	protected static final String SELECT_STATEMENT =
		"SELECT DISTINCT a.FFCODE, a.DESTINATION, b.CO_ID, b.SHDES " + 
		"FROM MPULKFXO a, MPUFFTAB b ";
	
	protected static final String SELECT_LAZY_STATEMENT = 
		SELECT_STATEMENT +		
		"WHERE a.FFCODE = ? ";
	
	protected static final String SELECT_ALTERNATE_LAZY_STATEMENT = 
		SELECT_STATEMENT +
		"WHERE a.FFCODE = b.FFCODE " +
	    "AND b.co_id = ? " +                                
	    "AND b.shdes = ?";
	
	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#createVO(java.sql.ResultSet)
	 */
	protected CacheableVO createVO(ResultSet _rset) throws SQLException {
		if (_rset == null) { return null; }
		int colIdx = 1;

		MpulkfxoVO vo = new MpulkfxoVO();			
		vo.setFfCode(_rset.getString(colIdx++));
		vo.setDestination(_rset.getString(colIdx++));
		vo.setContractNumber(_rset.getString(colIdx++));
		vo.setPromotionCode(_rset.getString(colIdx++));
	
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
		return SELECT_LAZY_STATEMENT;
	}

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#setLazySQLParameters(PreparedStatement, CacheableKey)
	 */
	protected void setLazySQLParameters(PreparedStatement _stmt, CacheableKey _key) throws SQLException {
		MpulkfxoVO.InnerKey key = (MpulkfxoVO.InnerKey) _key;
		_stmt.setString(1, key.getFfCode());
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
		MpulkfxoVO.InnerAlternateKey key = (MpulkfxoVO.InnerAlternateKey) _key;
		_stmt.setString(1, key.getContractNumber());
		_stmt.setString(2, key.getPromotionCode());
	}	
	
}
