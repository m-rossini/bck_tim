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
package br.com.auster.tim.billcheckout.tariff;

import java.util.Date;
import java.util.List;

import br.com.auster.billcheckout.caches.CacheableKey;
import br.com.auster.billcheckout.caches.CacheableVO;
import br.com.auster.common.datastruct.RangeMap;
import br.com.auster.om.reference.CustomizableEntity;

/**
 * TODO What this class is responsible for
 *
 * @author William Soares
 * @version $Id$
 * @since JDK1.4
 */
public class CachedMicrocellRatesVO extends CustomizableEntity implements CacheableVO {

	/**
	 *
	 */

	// ---------------------------
	// atributes
	// ---------------------------
	private static final long serialVersionUID = 1L;

	private String planName;
	private String state;
	private String custCode;
	private String snCode;
	private String description;

	private RangeMap map;
	private int ratesCount;
	
	// generated keys
	private CacheableKey  naturalKey;
	private CacheableKey  alternateKey;

	/**
	 *
	 */

	// ---------------------------
	// Constructors
	// ---------------------------
	public CachedMicrocellRatesVO() {
		this(0);
	}

	/**
	 * @param uid
	 */
	public CachedMicrocellRatesVO(long _uid) {
		setUid(_uid);
		
		this.map = new RangeMap();
	}

	// ---------------------------
	// Accessors
	// ---------------------------

	

	public String getSnCode() { return this.snCode; }
	public void setSnCode(String snCode) { this.snCode = snCode; }

	public String getPlanName() { return this.planName; }
	public void setPlanName(String _plan) { this.planName = _plan; }

	public String getState() { return this.state; }
	public void setState(String _state) { this.state = _state; }

	public String getDescription() { return this.description; }
	public void setDescription(String _desc) { this.description = _desc; }

	public String getCustCode() { return this.custCode; }
	public void setCustCode(String _custCode) { this.custCode = _custCode; }
	
	
	@SuppressWarnings("unchecked")
	public MicrocellRatesVO getMicrocell(Date _effectiveDate) {
		List l = this.map.get(_effectiveDate.getTime());
		if (l != null) {
			return (MicrocellRatesVO) l.get(0);
		}
		return null;
	}
	
	public int getNumberOfRanges() {
		return this.ratesCount;
	}
	
	public void addMicrocell(MicrocellRatesVO _vo, Date _limit) {
		addMicrocell(_vo, _limit.getTime());
	}
	
	public void addMicrocell(MicrocellRatesVO _vo, long _limit) {
		this.map.add(_vo.getEffectiveDate().getTime(), _limit+1, _vo);
		this.ratesCount++;
	}
	

	
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
			this.naturalKey = createKey( this.snCode, this.planName, this.state );
		}
		return this.naturalKey;
	}



	/**
	 * Inner class to handle the alternate key for PlansVO instances
	 */
	protected static final class InnerKey implements CacheableKey {

		private String planName;
		private String state;
		private String sncode;

		public InnerKey( String _sncode, String _planname, String _state ) {
			this.sncode = _sncode;
			this.planName = _planname;
			this.state = _state;
		}

		public boolean equals(Object _other) {
			if ( _other instanceof InnerKey ) {
				InnerKey other = (InnerKey) _other;
				boolean result = true;
				if (this.sncode == null) {
					result &= (other.sncode == null);
				} else {
					result &= (this.sncode.equals(other.sncode));
				}
				if (this.planName == null) {
					result &= (other.planName == null);
				} else {
					result &= (this.planName.equals(other.planName));
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
			int hashcode =  17 * this.sncode.hashCode();
			hashcode += 17 * this.planName.hashCode();
			hashcode += 17 * this.state.hashCode();
			return hashcode;
		}

		public String getState() { return this.state; }
		public void setState(String _uf) { this.state = _uf; }
		public String getPlanName() { return this.planName; }
		public void setPlanName(String _plan) { this.planName = _plan; }
		public String getSncode() { return this.sncode; }
		public void setSncode(String _sncode) { this.sncode = _sncode; }
	}

	/**
	 * @see br.com.auster.billcheckout.caches.CacheableVO#getKey()
	 */
	public CacheableKey getAlternateKey() {
		if (this.alternateKey == null) {
			// actually creates alternate key using Plan description
			// instead of short description
			this.alternateKey = createAlternateKey( this.snCode, this.description, this.custCode );
		}
		return this.alternateKey;
	}
	
	/**
	 * Inner class to handle the alternate key for PlansVO instances
	 */
	protected static final class InnerAlternateKey implements CacheableKey {

		private String description;
		private String sncode;
		private String custCode;

		public InnerAlternateKey( String _sncode, String _description, String _custCode ) {
			this.sncode = _sncode;
			this.description = _description;
			this.custCode = _custCode;
		}

		public boolean equals(Object _other) {
			if ( _other instanceof InnerAlternateKey ) {
				InnerAlternateKey other = (InnerAlternateKey) _other;
				boolean result = true;
				if (this.sncode == null) {
					result &= (other.sncode == null);
				} else {
					result &= (this.sncode.equals(other.sncode));
				}
				if (this.description == null) {
					result &= (other.description == null);
				} else {
					result &= (this.description.equals(other.description));
				}
				if (this.custCode == null) {
					result &= (other.custCode == null);
				} else {
					result &= (this.custCode.equals(other.custCode));
				}
				return result;
			} 
			return false;
		}

		public int hashCode() {
			int hashcode =  17 * this.sncode.hashCode();
			hashcode += 17 * this.description.hashCode();
			hashcode += 17 * this.custCode.hashCode();
			return hashcode;
		}

		public String getDescription() { return this.description; }
		public void setSDescription(String _description) { this.description = _description; }
		public String getSncode() { return this.sncode; }
		public void setSncode(String _sncode) { this.sncode = _sncode; }
		public String getCustCode() { return this.custCode; }
		public void setCustCode(String _custCode) { this.custCode = _custCode; }
	}	
	

	// ---------------------------
	// Helper methods
	// ---------------------------

	public static final CacheableKey createKey( String _sncode, String _plan, String _state) {
		return new InnerKey( _sncode, _plan, _state );
	}

	public static final CacheableKey createAlternateKey( String _sncode, String _description, String _custCode) {
		return new InnerAlternateKey( _sncode, _description, _custCode );
	}


}
