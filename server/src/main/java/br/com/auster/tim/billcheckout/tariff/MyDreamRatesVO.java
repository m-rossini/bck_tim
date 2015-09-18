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
package br.com.auster.tim.billcheckout.tariff;

import br.com.auster.billcheckout.caches.CacheableKey;
import br.com.auster.billcheckout.caches.CacheableVO;

/**
 * @author anardo
 * 18/03/2010
 */
public class MyDreamRatesVO implements CacheableVO {

	private static final long serialVersionUID = -6989952419571357668L;
	
	private String tariffZoneCode; //b.SHORT_DESC
	private String state;
	private String packageName;
	private double ppm;

	private CacheableKey  naturalKey;
	
	
	
	
	public MyDreamRatesVO() {
		super();
	}
		
	
	
	public String getTariffZoneCode() { return tariffZoneCode; }
	public void setTariffZoneCode(String tariffZoneCode) { this.tariffZoneCode = tariffZoneCode; }

	public String getState() { return state; }
	public void setState(String state) { this.state = state; }

	public String getPackageName() { return this.packageName; }
	public void setPackageName(String _pckName) { this.packageName = _pckName; }
	
	public double getPPM() { return this.ppm; }
	public void setPPM(double _ppm) { this.ppm = _ppm; }
	
	
	
	// ---------------------------
	// CacheableVO interface methods
	// ---------------------------

	/**
	 * @see br.com.auster.billcheckout.caches.CacheableVO#getAlternateKey()
	 */
	public CacheableKey getKey() {
		if (this.naturalKey == null) {
			// actually creates alternate key using Plan description
			// instead of short description
			this.naturalKey = createKey( this.tariffZoneCode, this.state, this.packageName );
		}
		return this.naturalKey;
	}

	/**
	 * @see br.com.auster.billcheckout.caches.CacheableVO#getKey()
	 */
	public CacheableKey getAlternateKey() {
		return null;
	}
	
	/**
	 * Inner class to handle the alternate key for PlansVO instances
	 */
	protected static final class InnerKey implements CacheableKey {

		private String tariffZone;
		private String state;
		private String packagetName;

		public InnerKey(String _tariffZone, String _state,  String _packagetName ) {
			this.packagetName = _packagetName;
			this.tariffZone = _tariffZone;
			this.state = _state;
		}

		public boolean equals(Object _other) {
			if ( _other instanceof InnerKey ) {
				InnerKey other = (InnerKey) _other;
				boolean result = true;
				if (this.packagetName == null) {
					result &= (other.packagetName == null);
				} else {
					result &= (this.packagetName.equals(other.packagetName));
				}
				if (this.tariffZone == null) {
					result &= (other.tariffZone == null);
				} else {
					result &= (this.tariffZone.equals(other.tariffZone));
				}
				if (this.state == null) {
					result &= (other.state == null);
				} else {
					result &= (this.state.equals(other.state));
				}
				return result;
			} 
			return false;
		}

		public int hashCode() {
			int hashcode =  17 * this.packagetName.hashCode();
			hashcode += 17 * this.tariffZone.hashCode();
			hashcode += 17 * this.state.hashCode();
			return hashcode;
		}

		public String getState() { return this.state; }
		public void setState(String _uf) { this.state = _uf; }
		public String getTariffZone() { return this.tariffZone; }
		public void setTariffZone(String _plan) { this.tariffZone = _plan; }
		public String getPackageName() { return this.packagetName; }
		public void setPackageName(String _sncode) { this.packagetName = _sncode; }
	}
	
	
	
	// ---------------------------
	// Helper methods
	// ---------------------------

	public static final CacheableKey createKey( String _tariffZone, String _state, String _packageName) {
		return new InnerKey( _tariffZone, _state, _packageName );
	}
}
