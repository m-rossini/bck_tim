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
 * Created on 18/03/2010
 */

/**
 * @author anardo
 *	18/03/2010
 */
package br.com.auster.tim.billcheckout.tariff;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;
import org.apache.log4j.Logger;

import br.com.auster.billcheckout.caches.CacheableKey;
import br.com.auster.billcheckout.caches.CacheableVO;
import br.com.auster.billcheckout.caches.ReferenceDataCache;
import br.com.auster.common.sql.SQLConnectionManager;

public class RatesGuidingCache extends ReferenceDataCache {
	
	protected static final String SELECT_ALTERNATE_LAZY_STATEMENT = "RatesGuidingCache.select";
	private static final Logger log = Logger.getLogger(RatesGuidingCache.class);
	
	/**
	 * Popula RangeMap, usando de uma lista de objetos que contenham uma mesma data(efetivação).
	 * Toda a sequência da adição na lista, e posteriormente, no RangeMap, considera-se que
	 * no resultSet os registros virão ordenados por "Data de Efetivação".
	 * Assim, em guiding-sql.xml, a query deverá estar com OrderBy desta data.
	 * Existe ordenação para init_volume, atendendo a lógica implementada para R01.1.
	 * 
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#createVO(java.sql.ResultSet)
	 */
	protected CacheableVO createVO(ResultSet _rset) throws SQLException {
		if (_rset == null) { return null; }
		
		CachedRatesGuidingVO vo = new CachedRatesGuidingVO();
		
		vo.setPlanUid(_rset.getLong("PLANS_UID"));
		vo.setPlanName(_rset.getString("PLAN_NAME"));
		vo.setIncomingFlag(_rset.getString("INCOMING_FLAG"));
		vo.setServiceUid(_rset.getLong("SERVICES_UID"));
		vo.setServiceCode(_rset.getString("SHDES_SERVICE"));
		vo.setTariffZoneUid(_rset.getLong("TARIFF_ZONE_UID"));
		vo.setTariffZoneCode(_rset.getString("SHDES_TARIFF_ZONE"));
		vo.setRateTimezoneUid(_rset.getLong("RATE_TIME_ZONE_UID"));
		vo.setRateTimezoneCode(_rset.getString("BILL_FILE_CODE"));
		vo.setState(_rset.getString("STATE"));
		RatesGuidingVO previousInner = null;
		List<RatesGuidingVO> listRatingOneCall = new ArrayList<RatesGuidingVO>();
		do {
			RatesGuidingVO innerVO = new RatesGuidingVO();
			innerVO.setPlanUid(vo.getPlanUid());
			innerVO.setPlanName(vo.getPlanName());
			innerVO.setIncomingFlag(vo.getIncomingFlag());
			innerVO.setServiceUid(vo.getServiceUid());
			innerVO.setServiceCode(vo.getServiceCode());
			innerVO.setTariffZoneUid(vo.getTariffZoneUid());
			innerVO.setTariffZoneCode(vo.getTariffZoneCode());
			innerVO.setRateTimezoneUid(vo.getRateTimezoneUid());
			innerVO.setRateTimezoneCode(vo.getRateTimezoneCode());
			innerVO.setState(vo.getState());
			// now we get the other columns from the database
			innerVO.setEffectiveDate(_rset.getDate("EFFECTIVE_DATE"));
			innerVO.setInitVolume(_rset.getInt("INIT_VOLUME"));
			innerVO.setOriginalEndVolume(_rset.getInt("END_VOLUME"));
			innerVO.setStepVolume(_rset.getInt("STEP_VOLUME"));
			innerVO.setStepCost(_rset.getDouble("STEP_COST"));
			//adjusting end_volume value(end of one period), in seconds
			innerVO.setEndVolume();
			
			if(listRatingOneCall.isEmpty() || 
			  (previousInner != null && (previousInner.getEffectiveDate().compareTo(innerVO.getEffectiveDate()) == 0)) ){
				listRatingOneCall.add(innerVO);
			}
			// adding inner vo to range map
			if (previousInner != null && (previousInner.getEffectiveDate().compareTo(innerVO.getEffectiveDate()) < 0) ) {
				vo.addRating(previousInner, innerVO.getEffectiveDate(), listRatingOneCall);
				listRatingOneCall = new ArrayList<RatesGuidingVO>();
				listRatingOneCall.add(innerVO);
			}
			previousInner = innerVO;
		}while(_rset.next());
		// adding the last element
		if (previousInner != null) {
			vo.addRating(previousInner, Long.MAX_VALUE-1, listRatingOneCall);
		}
		return vo;
	}

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#getAlternateLazySQL()
	 */
	protected String getAlternateLazySQL() {
		try {
			return SQLConnectionManager.getInstance(this.poolName).getStatement(SELECT_ALTERNATE_LAZY_STATEMENT).getStatementText();
		} catch (NamingException ne) {
			log.error("Could not load sql statement", ne);
		}
		return null;
	}

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#getLazySQL()
	 */
	protected String getLazySQL() {
		throw new NoSuchMethodError("Must run with alternate implementations.");
	}

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#getNonLazySQL()
	 */
	protected String getNonLazySQL() {
		throw new NoSuchMethodError("Must run with alternate implementations.");
	}

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#setAlternateLazySQLParameters(java.sql.PreparedStatement, br.com.auster.billcheckout.caches.CacheableKey)
	 */
	protected void setAlternateLazySQLParameters(PreparedStatement _stmt, CacheableKey _key) throws SQLException {
		CachedRatesGuidingVO.RateGuidingAlternateKey key = (CachedRatesGuidingVO.RateGuidingAlternateKey) _key;
		int col = 1;
		_stmt.setString(col++, key.getTarrifZone());
		_stmt.setString(col++, key.getPlan());
		_stmt.setString(col++, key.getService());
		_stmt.setString(col++, key.getState());
		_stmt.setString(col++, key.getRateZone());
		_stmt.setString(col++, key.getIncoming());
	}

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#setLazySQLParameters(java.sql.PreparedStatement, br.com.auster.billcheckout.caches.CacheableKey)
	 */
	protected void setLazySQLParameters(PreparedStatement _stmt, CacheableKey _key) throws SQLException {
		throw new NoSuchMethodError("Must run with alternate implementations.");
	}
}
