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
public class ServicesVO extends CustomizableEntity implements CacheableVO {
	/**
	 *
	 */

	// ---------------------------
	// atributes
	// ---------------------------
	private static final long serialVersionUID = 1L;
	private long	svcCode;
	private String	description;
	private String	shortDesc;
	private CacheableKey  logicKey;
	private CacheableKey  naturalKey;

	/**
	 *
	 */

	// ---------------------------
	// Constructors
	// ---------------------------
	public ServicesVO() {
		this(0);
	}

	/**
	 * @param uid
	 */
	public ServicesVO(long _uid) {
		setUid(_uid);
	}

	// ---------------------------
	// Accessors
	// ---------------------------

	public long getSvcCode() {
		return svcCode;
	}

	public void setSvcCode(long _svcCode) {
		this.svcCode = _svcCode;
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


	// ---------------------------
	// CacheableVO interface methods
	// ---------------------------

	/**
	 * @see br.com.auster.billcheckout.caches.CacheableVO#getAlternateKey()
	 */
	public CacheableKey getAlternateKey() {
		if (this.naturalKey == null) {
			this.naturalKey = createAlternateKey( this.getShortDesc() );
		}
		return this.naturalKey;
	}

	/**
	 * Inner class to handle the alternate key for ServicesVO instances
	 */
	protected static final class InnerAlternateKey implements CacheableKey {

		private String shortDesc;

		public InnerAlternateKey(String _shortDesc ) {
			this.shortDesc = _shortDesc;
		}

		public boolean equals(Object _other) {
			if ( _other instanceof InnerAlternateKey ) {
				InnerAlternateKey other = (InnerAlternateKey) _other;
				if (this.shortDesc == null) {
					return (other.shortDesc == null);
				}
				return this.shortDesc.equals(other.shortDesc);
			}
			else
				return false;
		}

		public int hashCode() {
			int hashcode = (this.shortDesc == null ? 17 : this.shortDesc.hashCode());
			return hashcode;
		}

		public String getShortDesc() { return this.shortDesc; }
	}

	/**
	 * @see br.com.auster.billcheckout.caches.CacheableVO#getKey()
	 */
	public CacheableKey getKey() {
		if (this.logicKey == null) {
			this.logicKey = createKey( this.getSvcCode() );
		}
		return this.logicKey;
	}

	/**
	 * Inner class to handle the alternate key for ServicePlan instances
	 */
	protected static final class InnerKey implements CacheableKey {

		private long svcCode;

		public InnerKey(long svcCode ) {
			this.svcCode = svcCode;
		}

		public boolean equals(Object _other) {
			if ( _other instanceof InnerKey ) {
				InnerKey other = (InnerKey) _other;
				return (this.svcCode == other.svcCode);
			}
			else
				return false;
		}

		public int hashCode() {
			int hashcode = 37 + ((int) (17*this.svcCode));
			return hashcode;
		}

		public long getSvcCode() { return this.svcCode; }
	}



	// ---------------------------
	// Helper methods
	// ---------------------------

	public static final CacheableKey createKey(long svcCode) {
		return new InnerKey(svcCode);
	}

	public static final CacheableKey createAlternateKey(String _shortDesc ) {
		return new InnerAlternateKey( _shortDesc );
	}
}
