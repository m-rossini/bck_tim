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
import java.util.HashSet;
import java.util.Set;

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
public class PromotionExclusivityCache extends ReferenceDataCache {

	private static final Logger log = Logger.getLogger(PromotionExclusivityCache.class);
	
	protected static final String SELECT_STATEMENT = 
		"select a.*, c.short_desc, c.description " +
	    "from promotion_exclusivity a, " +
	    "promotion_exclusivity_rel b, " +
	    "promotion_exclusivity c ";
	
	protected static final String SELECT_ALTERNATE_LAZY_STATEMENT = 
		SELECT_STATEMENT +
		"where " + 
	    "a.objid = b.objid_1 and " +
	    "c.objid = b.objid_2 and " + 
	    "a.short_desc = ? " +
		"union " +
		SELECT_STATEMENT +	
		"where " +
		"a.objid = b.objid_2 and " +
		"c.objid = b.objid_1 and " + 
		"a.short_desc = ?";
	
	@Override
	protected CacheableVO createVO(ResultSet _rset) throws SQLException {
		if (_rset == null) { return null; }

		PromotionExclusivityVO vo = new PromotionExclusivityVO();	
		Set relatedPromotions = new HashSet();
		PromotionExclusivityVO relatedPromotionVo;
		
		boolean firstLine = true;
		while(_rset.next()) {
			relatedPromotionVo = new PromotionExclusivityVO();
			if(firstLine) {
				vo.setShortDescription(_rset.getString(2));
				vo.setDescription(_rset.getString(3));
				firstLine = false;
			} 
			relatedPromotionVo.setShortDescription(_rset.getString(4));
			relatedPromotionVo.setDescription(_rset.getString(5));
			relatedPromotions.add(relatedPromotionVo);			
		}
		
		vo.setRelatedPromotions(relatedPromotions);
		
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
		return SELECT_ALTERNATE_LAZY_STATEMENT;
	}

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#setAlternateLazySQLParameters(PreparedStatement, CacheableKey)
	 */
	protected void setAlternateLazySQLParameters(PreparedStatement _stmt, CacheableKey _key) throws SQLException  {
		PromotionExclusivityVO.InnerAlternateKey key = (PromotionExclusivityVO.InnerAlternateKey) _key;
		_stmt.setString(1, key.getShortDescription());	
		_stmt.setString(2, key.getShortDescription());
	}
	
}
