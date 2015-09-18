/*
 * Copyright (c) 2004-2006 Auster Solutions do Brasil. All Rights Reserved.
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
//TODO Comment this Class
package br.com.auster.tim.billcheckout.bscs;

import java.util.Date;

import br.com.auster.om.reference.CustomizableEntity;
import br.com.auster.billcheckout.caches.CacheableVO;
import br.com.auster.billcheckout.caches.CacheableKey;

/**
 * @author mruao
 * @version $Id$
 *
 */
public class SGTCustomVO extends CustomizableEntity implements CacheableVO {



	// ---------------------------
	// attributes
	// ---------------------------
	private static final long serialVersionUID = 1L;

	private String	custCode;
	private Date actDate;
	private double subscriptionFee;
	// generated keys
	private CacheableKey  logicKey;



	// ---------------------------
	// Constructors
	// ---------------------------
	public SGTCustomVO() {
		this(0);
	}

	/**
	 * @param uid
	 */
	public SGTCustomVO(long _uid) {
		setUid(_uid);
	}

	// ---------------------------
	// Accessors
	// ---------------------------

	public final String getCustCode() {
		return custCode;
	}

	public final void setCustCode(String custCode) {
		this.custCode = custCode;
	}

	public final double getSubscriptionFee() {
		return subscriptionFee;
	}

	public final void setSubscriptionFee(double subscriptionFee) {
		this.subscriptionFee = subscriptionFee;
	}

	public final Date getActivationDate() { return this.actDate; }
	public final void setActivationDate(Date _date) { this.actDate = _date; }


	// ---------------------------
	// CacheableVO interface methods
	// ---------------------------

	/**
	 * @see br.com.auster.billcheckout.caches.CacheableVO#getAlternateKey()
	 */
	public CacheableKey getAlternateKey() {
		return this.getKey();
	}

	/**
	 * @see br.com.auster.billcheckout.caches.CacheableVO#getKey()
	 */
	public CacheableKey getKey() {
		if (this.logicKey == null) {
			this.logicKey = createKey( this.custCode, this.actDate );
		}
		return this.logicKey;
	}

	/**
	 * Inner class to handle the alternate key for PlansVO instances
	 */
	protected static final class InnerKey implements CacheableKey {

		private String custCode;
		private Date actDate;

		public InnerKey( String _customerId, Date _actDate) {
			this.custCode = _customerId;
			this.actDate = _actDate;
		}

		public boolean equals(Object _other) {
			boolean result = true;
			if ( _other instanceof InnerKey ) {
				InnerKey other = (InnerKey) _other;
				result = result &  this.custCode.equals(other.custCode);
				if (this.actDate == null) {
					return result && (other.actDate == null);
				}
				return result && this.actDate.equals(other.actDate);
			}
			else
				return false;
		}

		public int hashCode() {
			int hashcode = this.custCode.hashCode();
			hashcode += 17*(this.actDate == null ? 0 : this.actDate.hashCode());
			return hashcode;
		}

		public String getCustCode() { return this.custCode; }
		public Date getActivationDate() { return this.actDate; }
	}



	// ---------------------------
	// Helper methods
	// ---------------------------

	public static final CacheableKey createKey( String _customerId, Date _actDate ) {
		return new InnerKey(_customerId, _actDate);
	}

	public static final CacheableKey createAlternateKey( String _customerId, Date _actDate ) {
		return createKey( _customerId, _actDate );
	}
}
