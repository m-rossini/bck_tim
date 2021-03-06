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
 * Created on 29/10/2007
 */
package br.com.auster.tim.billcheckout.param;

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
public class UsageGroupLDVO extends CustomizableEntity implements CacheableVO {

	// ---------------------------
	// atributes
	// ---------------------------
	private static final long serialVersionUID = 1L;
	private String		  name;
	private String 		  cspId;
	private CacheableKey  logicKey;
	private CacheableKey  naturalKey;

	/**
	 *
	 */

	// ---------------------------
	// Constructors
	// ---------------------------
	public UsageGroupLDVO() {
		this(0);
	}

	/**
	 * @param uid
	 */
	public UsageGroupLDVO(long _uid) {
		setUid(_uid);
	}

	// ---------------------------
	// Accessors
	// ---------------------------

	public String getName() {
		return name;
	}

	public void setName(String _name) {
		this.name = _name;
	}

	public String getCspId() {
		return cspId;
	}

	public void setCspId(String cspId) {
		this.cspId = cspId;
	}

	// ---------------------------
	// CacheableVO interface methods
	// ---------------------------

	/**
	 * @see br.com.auster.billcheckout.caches.CacheableVO#getAlternateKey()
	 */
	public CacheableKey getAlternateKey() {
		if (this.naturalKey == null) {
			// creates alternate key using Usage Group name
			this.naturalKey = createAlternateKey( this.getName(), this.getCspId() );
		}
		return this.naturalKey;
	}

	/**
	 * Inner class to handle the alternate key for UsageGroupVO instances
	 */
	protected static final class InnerAlternateKey implements CacheableKey {

		private String groupName;
		private String cspId;

		public InnerAlternateKey(String groupName, String cspId) {
			this.groupName = groupName;
			this.cspId = cspId;
		}

		public boolean equals(Object _other) {
			if ( _other instanceof InnerAlternateKey ) {
				InnerAlternateKey other = (InnerAlternateKey) _other;
				boolean result = this.groupName.equals(other.groupName);
				if (result && (this.cspId == null)) {
					return (other.cspId == null);
				} else if (result && (this.cspId != null)) {
					return this.cspId.equals(other.cspId);
				}
				return result;
			}
			else
				return false;
		}

		public int hashCode() {
			int hashcode = this.groupName.hashCode();
			hashcode =+ (this.cspId != null ? this.cspId.hashCode() : 0);
			return hashcode;
		}

		public String getGroupName() { return this.groupName; }

		public String getCspId() {
			return cspId;
		}
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

	public static final CacheableKey createAlternateKey(String groupName, String cspId) {
		return new InnerAlternateKey(groupName, cspId);
	}

}