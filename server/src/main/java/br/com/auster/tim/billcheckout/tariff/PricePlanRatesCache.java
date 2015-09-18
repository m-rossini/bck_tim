/*
 * Copyright (c) 2004-2010 Auster Solutions. All Rights Reserved.
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
 * Created on 20/04/2010
 */
package br.com.auster.tim.billcheckout.tariff;

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
 * @author anardo
 * @version $Id$
 * @since JDK1.4
 */
public class PricePlanRatesCache extends ReferenceDataCache {
	
	private static final String SELECT_LAZY_STATEMENT = "PricePlanRatesCache.select";
	private static final String  SELECT_LAZY_ALTERNATE_STATEMENT = "PricePlanRatesCache.alternateSelect";
	
	private static final Logger log = Logger.getLogger(PricePlanRatesCache.class);
	
	@Override
	protected CacheableVO createVO(ResultSet _rset) throws SQLException {
		if (_rset == null){ return null; }
		CachedPricePlanRatesVO cached = new CachedPricePlanRatesVO();
		//informations for key of cache
		cached.setPlanName(_rset.getString("PLAN_NAME"));
		cached.setState(_rset.getString("STATE"));
		cached.setServiceShdes(_rset.getString("SHDES_SERVICE"));
		cached.setTariffZoneShdes(_rset.getString("SHDES_TARIFF_ZONE"));
		cached.setPackageDescr(_rset.getString("DESCR_PACKAGE"));
		PricePlanRatesVO previousInner = null;
		do {
			PricePlanRatesVO innerVO = new PricePlanRatesVO();
			innerVO.setPlanUid(_rset.getLong("PLAN_UID"));
			innerVO.setPackageUid(_rset.getLong("PACKAGE_UID"));
			innerVO.setServiceUid(_rset.getLong("SERVICE_UID"));
			innerVO.setTariffZoneUid(_rset.getLong("TARIFF_ZONE_UID"));
			innerVO.setEffectiveDate(_rset.getDate("EFFECTIVE_DATE"));
			innerVO.setUmCode(_rset.getString("UMCODE"));
			innerVO.setEndRange1(_rset.getLong("END_RANGE_1"));
			innerVO.setAmtRange1(_rset.getDouble("AMT_RANGE_1"));
			innerVO.setEndRange2(_rset.getLong("END_RANGE_2"));
			innerVO.setAmtRange2(_rset.getDouble("AMT_RANGE_2"));
			innerVO.setEndRange3(_rset.getLong("END_RANGE_3"));
			innerVO.setAmtRange3(_rset.getDouble("AMT_RANGE_3"));
			innerVO.setEndRange4(_rset.getLong("END_RANGE_4"));
			innerVO.setAmtRange4(_rset.getDouble("AMT_RANGE_4"));
			innerVO.setEndRange5(_rset.getLong("END_RANGE_5"));
			innerVO.setAmtRange5(_rset.getDouble("AMT_RANGE_5"));
			innerVO.setAmtRangeMax(_rset.getDouble("AMT_RANGE_MAX"));
			// adding inner vo to range map
			if (previousInner != null) {
				cached.addPricePlanRating(previousInner, innerVO.getEffectiveDate());
			}
			previousInner = innerVO;
		} while (_rset.next());
		// adding the last element
		if (previousInner != null) {
			cached.addPricePlanRating( previousInner, Long.MAX_VALUE-1);
		}		
		return cached;
	}

	@Override
	protected String getAlternateLazySQL() {
		try {
			return SQLConnectionManager.getInstance(poolName).getStatement(SELECT_LAZY_ALTERNATE_STATEMENT).getStatementText();
		} catch (NamingException e) {
			log.error("Could not load sql statement" + e);
		}
		return null;
	}

	@Override
	protected String getLazySQL() {
		try {
			return SQLConnectionManager.getInstance(poolName).getStatement(SELECT_LAZY_STATEMENT).getStatementText();
		} catch (NamingException e) {
			log.error("Could not load sql statement" + e);
		}
		return null;
	}

	@Override
	protected String getNonLazySQL() {
		throw new NoSuchMethodError("Must run with lazy implementations.");
	}

	@Override
	protected void setAlternateLazySQLParameters(PreparedStatement _stmt, CacheableKey _key) throws SQLException {
		CachedPricePlanRatesVO.InnerKey key = (CachedPricePlanRatesVO.InnerKey) _key;
		int col = 1;
		_stmt.setString(col++, key.getPlanName());
		_stmt.setString(col++, key.getState());
		_stmt.setString(col++, key.getServiceShdes());
		_stmt.setString(col++, key.getTariffZoneShdes());
	}

	@Override
	protected void setLazySQLParameters(PreparedStatement _stmt, CacheableKey _key) throws SQLException {
		CachedPricePlanRatesVO.InnerKey key = (CachedPricePlanRatesVO.InnerKey) _key;
		int col = 1;
		_stmt.setString(col++, key.getPlanName());
		_stmt.setString(col++, key.getState());
		_stmt.setString(col++, key.getServiceShdes());
		_stmt.setString(col++, key.getTariffZoneShdes());
		_stmt.setString(col++, key.getPackageDescr());
	}
}
