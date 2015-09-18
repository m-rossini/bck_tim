/*
 * Copyright (c) 2004-2006 Auster Solutions. All Rights Reserved.
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
 * Created on 02/12/2006
 */
package br.com.auster.tim.billcheckout.param;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.auster.billcheckout.caches.CacheableKey;
import br.com.auster.billcheckout.caches.CacheableVO;
import br.com.auster.billcheckout.caches.ReferenceDataCache;

/**
 * @author mruao
 * @version $Id$
 *
 */
public class TariffZoneCache extends ReferenceDataCache {

	
	
	protected static final String SELECT_STATEMENT = 
		"SELECT a.OBJID, a.ZONE_CODE, a.DESCRIPTION, a.SHORT_DESC, a.TARIFF_TYPE, " +
		       "a.CUSTOM_1, a.CUSTOM_2, a.CUSTOM_3 " +
		"FROM QLF_TARIFF_ZONE a ";

	protected static final String SELECT_LAZY_STATEMENT = 
		SELECT_STATEMENT +
		" WHERE a.OBJID = ? ";
	
	protected static final String SELECT_ALTERNATE_LAZY_STATEMENT = 
		SELECT_STATEMENT +
		" WHERE a.SHORT_DESC = ? ";

	
	
	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#createVO(java.sql.ResultSet)
	 */
	protected CacheableVO createVO(ResultSet _rset) throws SQLException {
		if (_rset == null) { return null; }
		int colIdx = 1;
		TariffZoneVO vo = new TariffZoneVO();
		vo.setUid(_rset.getLong(colIdx++));
		vo.setZoneCode(_rset.getLong(colIdx++));
		vo.setDescription(_rset.getString(colIdx++));
		vo.setShortDesc(_rset.getString(colIdx++));
		vo.setTariffType(_rset.getString(colIdx++));
		// duplicating information into Custom1 and onNet attributes
		vo.setCustom1(_rset.getString(colIdx));
		vo.setOnNet(charToBool(_rset.getString(colIdx++)));
		// done duplicating attribute
		vo.setCustom2(_rset.getString(colIdx++));
		vo.setCustom3(_rset.getString(colIdx++));
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
		TariffZoneVO.InnerKey key = (TariffZoneVO.InnerKey) _key;
		_stmt.setLong(1, key.getUid());
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
		TariffZoneVO.InnerAlternateKey key = (TariffZoneVO.InnerAlternateKey) _key;
		_stmt.setString(1, key.getShortDesc());
	}
	

	private final boolean charToBool(String _bool) {
		return ("T".equalsIgnoreCase(_bool));
	}	
}
