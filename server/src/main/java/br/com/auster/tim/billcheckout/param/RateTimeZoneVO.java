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
package br.com.auster.tim.billcheckout.param;

import br.com.auster.om.reference.CustomizableEntity;
import br.com.auster.billcheckout.caches.CacheableVO;
import br.com.auster.billcheckout.caches.CacheableKey;

/**
 * @author mruao
 * @version $Id$
 *
 */
public class RateTimeZoneVO extends CustomizableEntity implements CacheableVO {
	/**
	 *
	 */

	// ---------------------------
	// atributes
	// ---------------------------
	private static final long serialVersionUID = 1L;
	private long	ttCode;
	private String	description;
	private String	shortDesc;
	private String	billFileCode;
	private CacheableKey  logicKey;
	private CacheableKey  naturalKey;

	/**
	 *
	 */

	// ---------------------------
	// Constructors
	// ---------------------------
	public RateTimeZoneVO() {
		this(0);
	}

	/**
	 * @param uid
	 */
	public RateTimeZoneVO(long _uid) {
		setUid(_uid);
	}

	// ---------------------------
	// Accessors
	// ---------------------------

	public long getTtCode() {
		return ttCode;
	}

	public void setTtCode(long _ttCode) {
		this.ttCode = _ttCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String _description) {
		this.description = _description;
	}

	public String getShortDesc() {
		return shortDesc;
	}

	public void setShortDesc(String _shortDesc) {
		this.shortDesc = _shortDesc;
	}

	public String getBillFileCode() {
		return billFileCode;
	}

	public void setBillFileCode(String _billFileCode) {
		this.billFileCode = _billFileCode;
	}


	// ---------------------------
	// CacheableVO interface methods
	// ---------------------------

	/**
	 * @see br.com.auster.billcheckout.caches.CacheableVO#getAlternateKey()
	 */
	public CacheableKey getAlternateKey() {
		if (this.naturalKey == null) {
			// actually creates alternate key using billFileCode
			// instead of short description
			this.naturalKey = createAlternateKey( this.getBillFileCode() );
		}
		return this.naturalKey;
	}

	/**
	 * Inner class to handle the alternate key for RateTimeZoneVO instances
	 */
	protected static final class InnerAlternateKey implements CacheableKey {

		private String billFileCode;

		public InnerAlternateKey(String _billFileCode ) {
			this.billFileCode = _billFileCode;
		}

		public boolean equals(Object _other) {
			if ( _other instanceof InnerAlternateKey ) {
				InnerAlternateKey other = (InnerAlternateKey) _other;
				if (this.billFileCode == null) {
					return (other.billFileCode == null);
				}
				return this.billFileCode.equals(other.billFileCode);
			}
			else
				return false;
		}

		public int hashCode() {
			int hashcode = ( this.billFileCode == null ? 17 : this.billFileCode.hashCode());
			return hashcode;
		}

		public String getBillFileCode() { return this.billFileCode; }
	}

	/**
	 * @see br.com.auster.billcheckout.caches.CacheableVO#getKey()
	 */
	public CacheableKey getKey() {
		if (this.logicKey == null) {
			this.logicKey = createKey( this.getUid() );
		}
		return this.logicKey;
	}

	/**
	 * Inner class to handle the alternate key for ServicePlan instances
	 */
	protected static final class InnerKey implements CacheableKey {

		private long uid;

		public InnerKey(long _uid ) {
			this.uid = _uid;
		}

		public boolean equals(Object _other) {
			if ( _other instanceof InnerKey ) {
				InnerKey other = (InnerKey) _other;
				return (this.uid == other.uid);
			}
			else
				return false;
		}

		public int hashCode() {
			int hashcode = 37 + ((int) (17*this.uid));
			return hashcode;
		}

		public long getUid() { return this.uid; }
	}



	// ---------------------------
	// Helper methods
	// ---------------------------

	public static final CacheableKey createKey(long _uid ) {
		return new InnerKey(_uid);
	}

	public static final CacheableKey createAlternateKey(String _billFileCode ) {
		return new InnerAlternateKey( _billFileCode );
	}
}
