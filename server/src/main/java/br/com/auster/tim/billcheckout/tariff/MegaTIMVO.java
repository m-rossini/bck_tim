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
 * Created on 26/03/2010
 */
package br.com.auster.tim.billcheckout.tariff;

import java.util.HashMap;
import java.util.Map;

import br.com.auster.billcheckout.caches.CacheableKey;
import br.com.auster.billcheckout.caches.CacheableVO;
import br.com.auster.om.reference.CustomizableEntity;


/**
 * 
 * TODO What this class is responsible for
 *
 * @author Frederico Ramos
 * @version $Id$
 * @since JDK1.4
 */
public class MegaTIMVO extends CustomizableEntity implements CacheableVO {

	

	// ---------------------------
	// Attributes
	// ---------------------------
	
	private static final long serialVersionUID = 1L;

	private int sncode;
	private boolean onnet;
	private int validperiod;
	private double newfee;
	private double customerfee;
	private String description;
	private String occDescription;
	private String state;
	private Map<String, Integer> usageLimit;
	
	// generated keys
	private CacheableKey  naturalKey;
	
	
	
	// ---------------------------
	// Constructors
	// ---------------------------

	public MegaTIMVO() {
		this(0);
	}

	/**
	 * @param uid
	 */
	public MegaTIMVO(long _uid) {
		setUid(_uid);
		
		this.usageLimit = new HashMap<String, Integer>();
	}

	
	
	// ---------------------------
	// Getters/Setters
	// ---------------------------
	
	public final int getSncode() { return sncode; }
	public final void setSncode(int sncode) { this.sncode = sncode; }
	
	public final boolean isOnNetOnly() { return onnet; }
	public final void setOnNetOnly(boolean onnet) { this.onnet = onnet; }
	
	public final int getValidPeriod() { return validperiod; }
	public final void setValidPeriod(int validperiod) { this.validperiod = validperiod; }
	
	public final double getNewCustomerFee() { return newfee; }
	public final void setNewCustomerFee(double newfee) { this.newfee = newfee; }
	
	public final double getCurrentCustomerFee() { return customerfee; }
	public final void setCurrentCustomerFee(double customerfee) { this.customerfee = customerfee; }
	
	public final String getState() { return this.state; }
	public final void setState(String _state) { this.state = _state; } 

	public final String getDescription() { return this.description; }
	public final void setDescription(String _desc) { this.description = _desc; }
	
	public final Integer getUsageLimit(String _key) { return this.usageLimit.get(_key); }
	public final void addUsageLimit(String _key, Integer _value) { this.usageLimit.put(_key, _value); }
	public final boolean hasUsageLimit(String _key) { return this.usageLimit.containsKey(_key); }
	
	public final Map<String, Integer> getAllUsageLimits() { return this.usageLimit; }
	
	public final void setOccDescription(String _desc) { this.occDescription = _desc; }
	public final String getOccDescription() { return this.occDescription; }
	
	
	
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
			this.naturalKey = createKey( this.state, this.description );
		}
		return this.naturalKey;
	}

	/**
	 * Inner class
	 */
	protected static final class InnerKey implements CacheableKey {

		private String description;
		private String state;

		public InnerKey( String _state, String _description ) {
			this.state = _state;
			this.description = _description;
		}

		public boolean equals(Object _other) {
			if ( _other instanceof InnerKey ) {
				InnerKey other = (InnerKey) _other;
				boolean result = true;
				if (this.description == null) {
					result &= (other.description == null);
				} else {
					result &= (this.description.equals(other.description));
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
			int hashcode =  17 * this.description.hashCode();
			hashcode += 17 * this.state.hashCode();
			return hashcode;
		}

		public String getState() { return this.state; }
		public void setState(String _uf) { this.state = _uf; }
		public String getDescription() { return this.description; }
		public void setDescription(String _description) { this.description = _description; }
	}	
	
	/**
	 * @see br.com.auster.billcheckout.caches.CacheableVO#getKey()
	 */
	public CacheableKey getAlternateKey() {
		return null;
	}
	
	
	
	// ---------------------------
	// Helper methods
	// ---------------------------

	public static final CacheableKey createKey( String _state, String _description) {
		return new InnerKey( _state, _description );
	}
}
