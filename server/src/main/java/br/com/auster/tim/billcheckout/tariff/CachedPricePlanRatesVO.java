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
 * Created on 19/04/2010
 */
package br.com.auster.tim.billcheckout.tariff;

import java.util.Date;
import java.util.List;

import br.com.auster.billcheckout.caches.CacheableKey;
import br.com.auster.billcheckout.caches.CacheableVO;
import br.com.auster.common.datastruct.RangeMap;

/**
 * @author anardo
 *
 */
public class CachedPricePlanRatesVO implements CacheableVO {

	private String planName;
	private String state;
	private String serviceShdes;
	private String tariffZoneShdes;
	private String packageDescr;
	
	private RangeMap rangeMap;
	private CacheableKey naturaKey;
	private CacheableKey alternateKey;
	
	
	
	public CachedPricePlanRatesVO() {
		this.rangeMap = new RangeMap();
	}
	
	public String getPlanName() { return planName; }
	public void setPlanName(String planName) { this.planName = planName; }
	public String getState() { return state; }
	public void setState(String state) { this.state = state; }
	public String getServiceShdes() { return serviceShdes; }
	public void setServiceShdes(String serviceShdes) { this.serviceShdes = serviceShdes; }
	public String getTariffZoneShdes() { return tariffZoneShdes; }
	public void setTariffZoneShdes(String tariffZoneShdes) { this.tariffZoneShdes = tariffZoneShdes; }
	public String getPackageDescr() { return packageDescr; }
	public void setPackageDescr(String packageDescr) { this.packageDescr = packageDescr; }

	@SuppressWarnings("unchecked")
	public PricePlanRatesVO getPricePlanRates(Date _effectiveDate){
		List listPricePlans = this.rangeMap.get(_effectiveDate.getTime());
		if (listPricePlans != null) {
			// Nessa estrutura montada para o RangeMap, ao efetuar o get sempre teremos
			//o retorno de "UM" nó da lista de Ratings com a maior data de efetivação que seja
			//menor que a data da chamada.
			return (PricePlanRatesVO) listPricePlans.get(0);
		}
		return null;
	}
	
	/**
	 * Método que adiciona uma lista de tarifas de uma chamada com datas de efetivação distintas,
	 * para um RangeMap
	 * 
	 * @param _vo
	 * @param _limit
	 */
	public void addPricePlanRating(PricePlanRatesVO _vo, Date _limit) {
		addPricePlanRating(_vo, _limit.getTime());
	}
	
	public void addPricePlanRating(PricePlanRatesVO _vo, long _limit) {
		this.rangeMap.add(_vo.getEffectiveDate().getTime(), _limit+1, _vo);
	}
	
	protected static final class InnerKey implements CacheableKey {
		
		private String planName;
		private String state;
		private String serviceShdes;
		private String tariffZoneShdes;
		private String packageDescr;
		
		public InnerKey(String _planName, String _state, String _serviceShdes,String _tariffZoneShdes, 
						String _packageDescr) {
			super();
			this.planName = _planName;
			this.state = _state;
			this.serviceShdes = _serviceShdes;
			this.tariffZoneShdes = _tariffZoneShdes;
			this.packageDescr = _packageDescr;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result	+ ((packageDescr == null) ? 0 : packageDescr.hashCode());
			result = prime * result	+ ((planName == null) ? 0 : planName.hashCode());
			result = prime * result	+ ((serviceShdes == null) ? 0 : serviceShdes.hashCode());
			result = prime * result + ((state == null) ? 0 : state.hashCode());
			result = prime * result	+ ((tariffZoneShdes == null) ? 0 : tariffZoneShdes.hashCode());
			
			return result;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object _other) {
			if (_other instanceof InnerKey){
				InnerKey other = (InnerKey) _other;
				boolean result = true;
				
				if (this.packageDescr == null){
					result &= (other.packageDescr == null);
				}else {
					result &= (this.packageDescr.equals(other.packageDescr));
				}
				if (this.planName == null) {
					result &= (other.planName == null);
				}else{
					result &= (this.planName.equals(other.planName));
				}
				if (this.serviceShdes == null) {
					result &= (other.serviceShdes == null);
				} else {
					result &= (serviceShdes.equals(other.serviceShdes));
				}
				if (state == null) {
					result &= other.state == null;
				} else {
					result &= (state.equals(other.state));
				}
				if (tariffZoneShdes == null) {
					result &= (other.tariffZoneShdes == null);
				} else {
					result &= (tariffZoneShdes.equals(other.tariffZoneShdes));
				}
				return result;
			}
			return false;
		}

		public String getPlanName() { return planName; }
		public void setPlanName(String planName) { this.planName = planName; }
		public String getState() { return state; }
		public void setState(String state) { this.state = state; }
		public String getServiceShdes() { return serviceShdes; }
		public void setServiceShdes(String serviceShdes) { this.serviceShdes = serviceShdes; }
		public String getTariffZoneShdes() { return tariffZoneShdes; }
		public void setTariffZoneShdes(String tariffZoneShdes) { this.tariffZoneShdes = tariffZoneShdes; }
		public String getPackageDescr() { return packageDescr; }
		public void setPackageDescr(String packageDescr) { this.packageDescr = packageDescr; }
	}
	
	
	public CacheableKey getKey() {
		if (this.naturaKey == null){
			this.naturaKey = createKey(this.planName, this.state, this.serviceShdes, this.tariffZoneShdes,
									   this.packageDescr); 
		}
		return this.naturaKey;
	}
	
	public CacheableKey getAlternateKey() {
		if (this.alternateKey == null){
			this.alternateKey = createAlternateKey(this.planName, this.state, this.serviceShdes, this.tariffZoneShdes); 
		}
		return this.alternateKey;
	}
	
	public static final CacheableKey createKey(String _planName, String _state, String _serviceShdes, String _tariffZoneShdes, String _packageDescr){
			return new InnerKey(_planName, _state, _serviceShdes, _tariffZoneShdes, _packageDescr);
	}

	public static final CacheableKey createAlternateKey(String _planName, String _state, String _serviceShdes, String _tariffZoneShdes){
		return new InnerKey(_planName, _state, _serviceShdes, _tariffZoneShdes, null);
	}
}
