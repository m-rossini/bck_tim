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
 * Created on 04/12/2006
 */
package br.com.auster.tim.billcheckout.param;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.auster.billcheckout.caches.CacheableKey;
import br.com.auster.billcheckout.caches.CacheableVO;
import br.com.auster.billcheckout.caches.ReferenceDataCache;

/**
 * @author framos
 * @version $Id$
 *
 */
public class TariffZoneUsageGroupCache extends ReferenceDataCache {

	
	
	
	protected static final String SELECT_STATEMENT = 
		"SELECT a.OBJID, a.TARIFF_ZONE_UID, a.USAGE_GROUP_UID, a.ALLOWED_SECTION, a.CUSTOM_1, "+
		"a.CUSTOM_2, a.CUSTOM_3, b.SHORT_DESC, c.NAME  " +
		"FROM QLF_TARIFFZONE_USAGEGROUP a " +
		"JOIN QLF_TARIFF_ZONE b ON a.TARIFF_ZONE_UID = b.OBJID " +
		"JOIN QLF_USAGE_GROUP c ON a.USAGE_GROUP_UID = c.OBJID ";
	
	protected static final String SELECT_LAZY_STATEMENT = 
		SELECT_STATEMENT +
		" WHERE a.TARIFF_ZONE_UID = ? and a.USAGE_GROUP_UID = ?";
	
	protected static final String SELECT_ALTERNATE_LAZY_STATEMENT = 
		SELECT_STATEMENT +
		" WHERE b.SHORT_DESC = ? and c.NAME = ?";


	
	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#createVO(java.sql.ResultSet)
	 */
	protected CacheableVO createVO(ResultSet _rset) throws SQLException {
		TariffZoneUsageGroupVO vo = new TariffZoneUsageGroupVO();
		int colIdx=1;
		vo.setUid(_rset.getLong(colIdx++));
		vo.setTariffZoneUid(_rset.getLong(colIdx++));
		vo.setUsageGroupUid(_rset.getLong(colIdx++));
		vo.setAllowed(charToBool(_rset.getString(colIdx++)));
		vo.setCustom1(_rset.getString((colIdx++)));
		vo.setCustom2(_rset.getString((colIdx++)));
		vo.setCustom3(_rset.getString((colIdx++)));
		vo.setTariffZoneCode(_rset.getString((colIdx++)));
		vo.setUsageGroupCode(_rset.getString((colIdx++)));
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
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#setLazySQLParameters(java.sql.PreparedStatement, br.com.auster.billcheckout.caches.CacheableKey)
	 */
	protected void setLazySQLParameters(PreparedStatement _stmt, CacheableKey _key) throws SQLException {
		TariffZoneUsageGroupVO.TariffZoneUsageGroupNaturalKey key = (TariffZoneUsageGroupVO.TariffZoneUsageGroupNaturalKey) _key;
		_stmt.setLong(1, key.getTarrifZone());
		_stmt.setLong(2, key.getUsageGroup());
	}
	

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#getAlternateLazySQL()
	 */
	protected String getAlternateLazySQL() {
		return SELECT_ALTERNATE_LAZY_STATEMENT;
	}

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#setAlternateLazySQLParameters(java.sql.PreparedStatement, br.com.auster.billcheckout.caches.CacheableKey)
	 */
	protected void setAlternateLazySQLParameters(PreparedStatement _stmt, CacheableKey _key) throws SQLException {
		TariffZoneUsageGroupVO.TariffZoneUsageGroupAlternateKey key = (TariffZoneUsageGroupVO.TariffZoneUsageGroupAlternateKey) _key;
		_stmt.setString(1, key.getTarrifZone());
		_stmt.setString(2, key.getUsageGroup());
	}


	
	private final boolean charToBool(String _bool) {
		return ("T".equalsIgnoreCase(_bool));
	}
}
