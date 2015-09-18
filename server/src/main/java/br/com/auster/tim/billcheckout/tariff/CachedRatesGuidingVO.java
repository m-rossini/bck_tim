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
 * anardo
 */
package br.com.auster.tim.billcheckout.tariff;

import java.util.Date;
import java.util.List;

import br.com.auster.billcheckout.caches.CacheableKey;
import br.com.auster.billcheckout.caches.CacheableVO;
import br.com.auster.common.datastruct.RangeMap;
import br.com.auster.om.reference.CustomizableEntity;

public class CachedRatesGuidingVO extends CustomizableEntity implements CacheableVO {

	private static final long serialVersionUID = -6439105672643954794L;
	
	private long planUid;
	private String planName;
	private String incomingFlag;
	private long serviceUid;
	private String serviceCode; //d.SHORT_DESC
	private long tariffZoneUid;
	private String tariffZoneCode; //b.SHORT_DESC
	private long rateTimezoneUid;
	private String rateTimeZoneCode; //e.BILL_FILE_CODE
	private String state;
	
	private CacheableKey alternateKey;
	private RangeMap map;
	
	// ---------------------------
	// Constructors
	// ---------------------------
	
	public CachedRatesGuidingVO() {
		this(0);
	}
	
	public CachedRatesGuidingVO(long _uid) {
		setUid(_uid);
		this.map = new RangeMap();
	}
	
	/**
	 * Método que pega a lista de tarifas de uma chamada, de um nó do RangeMap, de acordo com a data
	 * de efetivação desta chamada. Pela estrutura deste RangeMap, retornará o registro com
	 * a maior data de efetivação, desde que a data da chamada seja maior que a de efetivação. Caso
	 * contrário, não existindo nenhum registro que atenda à isso, retornará nulo.
	 * 
	 * @param _effectiveDate
	 * @return List<RatesGuidingVO>
	 */
	@SuppressWarnings("unchecked")
	public List<RatesGuidingVO> getRating(Date _effectiveDate) {
		List l = this.map.get(_effectiveDate.getTime());
		if (l != null) {
			// Nessa estrutura montada para o RangeMap, ao efetuar o get sempre teremos
			//o retorno de "UM" nó de lista de Ratings com possível init/end_volume complementares
			//com mesma data de efetivação. O l.get(0) representa um nó do RangeMap.
			return (List<RatesGuidingVO>) l.get(0);
		}
		return null;
	}
	
	/**
	 * Método que adiciona uma lista de tarifas de uma chamada para o RangeMap
	 * 
	 * @param _vo
	 * @param _limit
	 * @param listOfOneCall
	 */
	public void addRating(RatesGuidingVO _vo, Date _limit, List<RatesGuidingVO> listOfOneCall) {
		addRating(_vo, _limit.getTime(), listOfOneCall);
	}
	
	public void addRating(RatesGuidingVO _vo, long _limit, List<RatesGuidingVO> listOfOneCall) {
		this.map.add(_vo.getEffectiveDate().getTime(), _limit+1, listOfOneCall);
	}

	// ---------------------------
	// Accessors methods
	// ---------------------------
	
	public String getPlanName() { return planName; }
	public void setPlanName(String planName) { this.planName = planName; }
	public String getIncomingFlag() { return incomingFlag; }
	public void setIncomingFlag(String incoming) { this.incomingFlag = incoming; }
	public String getServiceCode() { return serviceCode; }
	public void setServiceCode(String serviceCode) { this.serviceCode = serviceCode; }
	public String getTariffZoneCode() { return tariffZoneCode; }
	public void setTariffZoneCode(String tariffZoneCode) { this.tariffZoneCode = tariffZoneCode; }
	public long getRateTimezoneUid() { return rateTimezoneUid; }
	public void setRateTimezoneUid(long rateTimezoneUid) { this.rateTimezoneUid = rateTimezoneUid; }
	public String getRateTimezoneCode() { return rateTimeZoneCode;}
	public void setRateTimezoneCode(String rateTimezoneCode) { this.rateTimeZoneCode = rateTimezoneCode; }
	public String getState() { return state; }
	public void setState(String state) { this.state = state; }
	public long getPlanUid() { return planUid; }
	public void setPlanUid(long planUid) { this.planUid = planUid; }
	public long getServiceUid() { return serviceUid; }
	public void setServiceUid(long serviceUid) { this.serviceUid = serviceUid;}
	public long getTariffZoneUid() { return tariffZoneUid; }
	public void setTariffZoneUid(long tariffZoneUid) { this.tariffZoneUid = tariffZoneUid; }

	// ---------------------------
	// CacheableVO interface methods
	// ---------------------------
	
	/**
	 * @see br.com.auster.billcheckout.caches.CacheableVO#getAlternateKey()
	 */
	public CacheableKey getAlternateKey() {
		if (this.alternateKey == null) {
			this.alternateKey = createAlternateKey(this.planName, this.serviceCode,
					                               this.tariffZoneCode, this.incomingFlag, 
					                               this.rateTimeZoneCode, this.state);
		}
		return this.alternateKey;
	}
	
	/**
	 * @see br.com.auster.billcheckout.caches.CacheableVO#getKey()
	 */
	public CacheableKey getKey() {
		return null;
	}
	
	/**
	 * Inner class to handle the alternate key for RateGuiding instances
	 */
	protected static final class RateGuidingAlternateKey implements CacheableKey {
		
		private String plan;
		private String service;
		private String tarrifZone;
		private String incoming;
		private String rateZone;
		private String state;
		
		public RateGuidingAlternateKey(String _plan, String _svc, String _tarrifZone, String _incoming, String _rateZone, String _state) {
			this.plan = _plan;
			this.service = _svc;
			this.tarrifZone = _tarrifZone;
			this.incoming = _incoming;
			this.rateZone = _rateZone;
			this.state = _state;
		}
		
		public boolean equals(Object _other) {
			RateGuidingAlternateKey other = (RateGuidingAlternateKey) _other;
			boolean isEquals = 
				   this.plan.equals(other.plan) &&
				   this.service.equals(other.service) &&
				   this.tarrifZone.equals(other.tarrifZone) &&
				   this.incoming.equals(other.incoming) &&
				   this.rateZone.equals(other.rateZone) &&
				   this.state.equals(other.state);
			
			return isEquals;
		}

		public int hashCode() {
			int hashcode = this.plan.hashCode();
			hashcode += this.service.hashCode();
			hashcode += this.tarrifZone.hashCode();
			hashcode += this.incoming.hashCode();
			hashcode += this.rateZone.hashCode();
			hashcode += this.state.hashCode();
			return hashcode;
		}
	
		public String getPlan() { return this.plan; }
		public String getService() { return this.service; }
		public String getTarrifZone() { return this.tarrifZone; }
		public String getIncoming() { return this.incoming; }
		public String getRateZone() { return this.rateZone; }
		public String getState() { return this.state; }
	}
	
	/**
	 * Chave representando as variáveis da query: Plano do cliente,Código do serviço,Zona tarifária,
	 * Modulação horária, Estado de registro do cliente, Orientação da chamada
	 */
	public static final CacheableKey createAlternateKey(String _plan, String _svc, String _tariff, String _incoming, String _rateZone, String _state) {
		return new RateGuidingAlternateKey(_plan, _svc, _tariff, _incoming, _rateZone, _state);
	}
}
