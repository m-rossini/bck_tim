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
 * Created on 11/04/2008
 */
package br.com.auster.tim.billcheckout.param;

import java.util.Iterator;
import java.util.Set;

import br.com.auster.billcheckout.caches.CacheableKey;
import br.com.auster.billcheckout.caches.CacheableVO;
import br.com.auster.om.reference.CustomizableEntity;

/**
 * TODO What this class is responsible for
 *
 * @author William Soares
 * @version $Id$
 * @since JDK1.4
 */
public class ProgressiveDiscountVO extends CustomizableEntity implements CacheableVO {

	// ---------------------------
	// atributes
	// ---------------------------
	private static final long serialVersionUID = 1L;

	private String discountDesc;
	private String shdes;
	private String rangeName;
	private String state;
	private Set limits;
	private Set plans;

	// generated keys
	private CacheableKey  naturalKey;

	/**
	 *
	 */

	// ---------------------------
	// Constructors
	// ---------------------------
	public ProgressiveDiscountVO() {
		this(0);
	}

	/**
	 * @param uid
	 */
	public ProgressiveDiscountVO(long _uid) {
		setUid(_uid);
	}

	// ---------------------------
	// Accessors
	// ---------------------------

	public String getDiscountDesc() {
		return discountDesc;
	}

	public void setDiscountDesc(String discountDesc) {
		this.discountDesc = discountDesc;
	}

	public String getShdes() {
		return shdes;
	}

	public void setShdes(String shdes) {
		this.shdes = shdes;
	}

	public String getRangeName() {
		return rangeName;
	}

	public void setRangeName(String rangeName) {
		this.rangeName = rangeName;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Set getLimits() {
		return limits;
	}

	public void setLimits(Set limits) {
		this.limits = limits;
	}

	public Set getAllowedPlans() {
		return plans;
	}

	public void setAllowedPlans(Set _plans) {
		this.plans = _plans;
	}

	public double getSpecificDiscRate(double baseValue) {
		ProgDiscountLimitsVO limitBase = new ProgDiscountLimitsVO(baseValue);
		ProgDiscountLimitsVO limitToCompare;
		for (Iterator it = this.limits.iterator(); it.hasNext();) {
			limitToCompare = (ProgDiscountLimitsVO) it.next();
			if (limitToCompare.equals(limitBase)){
				return limitToCompare.getDiscRate();
			}
		}
		return -1;
	}

	// ---------------------------
	// CacheableVO interface methods
	// ---------------------------

	/**
	 * @see br.com.auster.billcheckout.caches.CacheableVO#getAlternateKey()
	 */
	public CacheableKey getAlternateKey() {
		if (this.naturalKey == null) {
			// actually creates alternate key using Plan description
			// instead of short description
			this.naturalKey = createAlternateKey( this.getDiscountDesc(), this.getState() );
		}
		return this.naturalKey;
	}

	/**
	 * Inner class to handle the alternate key for PlansVO instances
	 */
	protected static final class InnerAlternateKey implements CacheableKey {

		private String discountDesc;
		private String state;

		public InnerAlternateKey( String discountDesc, String state ) {
			this.discountDesc = discountDesc;
			this.state = state;
		}

		public boolean equals(Object _other) {
			if ( _other instanceof InnerAlternateKey ) {
				InnerAlternateKey other = (InnerAlternateKey) _other;
				if (this.discountDesc != null && this.state != null) {
					return  this.discountDesc.equals(other.discountDesc) && this.state.equals(state);
				} else if (this.discountDesc != null && this.state == null) {
					return  this.discountDesc.equals(other.discountDesc);
				}
				else {
					return false;
				}
			}
			else
				return false;
		}

		public int hashCode() {
			int hashCode = 0;
			if (this.discountDesc != null) {
				hashCode = this.discountDesc.hashCode();
			}
			if (this.state != null) {
				hashCode += this.state.hashCode();
			}
			return hashCode;
		}

		public String getDiscountDesc() {
			return discountDesc;
		}

		public String getState() {
			return state;
		}

	}

	/**
	 * @see br.com.auster.billcheckout.caches.CacheableVO#getKey()
	 */
	public CacheableKey getKey() {
		return null;
	}

	public static final CacheableKey createAlternateKey( String discountDesc, String state ) {
		return new InnerAlternateKey(discountDesc,  state);
	}

}
