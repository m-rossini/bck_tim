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
 * Created on 03/10/2007
 */
package br.com.auster.tim.billcheckout.param;

import br.com.auster.billcheckout.caches.CacheableKey;
import br.com.auster.billcheckout.caches.CacheableVO;
import br.com.auster.om.reference.CustomizableEntity;
import br.com.auster.tim.billcheckout.param.PackageVO.InnerAlternateKey;

/**
 * TODO What this class is responsible for
 *
 * @author William Soares
 * @version $Id$
 * @since JDK1.4
 */
public class PackagesByPlansVO extends CustomizableEntity implements
		CacheableVO {

	/**
	 *
	 */

	// ---------------------------
	// atributes
	// ---------------------------
	private static final long serialVersionUID = 1L;

	private long planId;
	private long packageId;
	private String	planName;
	private int tmcode;
	private String	state;
	private String description;

	// generated keys
	private CacheableKey logicKey;
	private CacheableKey  naturalKey;

	/**
	 *
	 */

	// ---------------------------
	// Constructors
	// ---------------------------
	public PackagesByPlansVO() {
		this(0, 0, "", "", "", 0);
	}

	/**
	 * @param uid
	 */
	public PackagesByPlansVO(long planId, long packageId, String planName, String state, String description, int tmcode) {
		setPlanId(planId);
		setPackageId(packageId);
		setPlanName(planName);
		setState(state);
		setDescription(description);
		setTMCode(tmcode);
	}

	// ---------------------------
	// Accessors
	// ---------------------------

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getPackageId() {
		return packageId;
	}

	public void setPackageId(long packageId) {
		this.packageId = packageId;
	}

	public long getPlanId() {
		return planId;
	}

	public void setPlanId(long planId) {
		this.planId = planId;
	}

	public final void setTMCode(int _tmcode) { this.tmcode = _tmcode; }
	public final int getTMCode() { return this.tmcode; }

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
			this.naturalKey = createAlternateKey( this.getTMCode(), this.getState(), this.getDescription() );
		}
		return this.naturalKey;
	}

	/**
	 * Inner class to handle the alternate key for PlansVO instances
	 */
	protected static final class InnerAlternateKey implements CacheableKey {

		private int tmcode;

		private String state;

		private String description;

		public InnerAlternateKey(int tmcode, String state, String description) {
			this.tmcode = tmcode;
			this.state = state;
			this.description = description;
		}

		public boolean equals(Object _other) {
			if (_other instanceof InnerAlternateKey) {
				InnerAlternateKey other = (InnerAlternateKey) _other;
				boolean ok = ((this.tmcode == other.tmcode) &&
				        this.description.equals(other.description));
				if (ok) {
					if (this.state == null) {
						return (other.state == null);
					} else {
						return (other.state != null) && this.state.equals(other.state);
					}
				}
				return ok;
			} else
				return false;
		}

		public int hashCode() {
			int hashcode = (int) (17 * this.tmcode);
			hashcode += (this.state == null ? 0 : this.state.hashCode());
			hashcode += this.description.hashCode();
			return hashcode;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state= state;
		}

		public int getTMCode() {
			return tmcode;
		}

		public void setTMCode(int tmcode) {
			this.tmcode = tmcode;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

	}

	/**
	 * @see br.com.auster.billcheckout.caches.CacheableVO#getKey()
	 */
	public CacheableKey getKey() {
		if (this.logicKey == null) {
			this.logicKey = createKey(this.getPlanName());
		}
		return this.logicKey;
	}

	/**
	 * Inner class to handle the alternate key for ServicePlan instances
	 */
	protected static final class InnerKey implements CacheableKey {

		private String planName;

		public InnerKey(String planName) {
			this.planName = planName;
		}

		public boolean equals(Object _other) {
			if (_other instanceof InnerKey) {
				InnerKey other = (InnerKey) _other;
				return (this.planName == other.planName );
			} else
				return false;
		}

		public int hashCode() {
			int hashcode = this.planName.hashCode();
			return hashcode;
		}

		public String getPlanName() {
			return planName ;
		}

		public void setPlanName(String planName) {
			this.planName = planName;
		}

	}

	// ---------------------------
	// Helper methods
	// ---------------------------

	public static final CacheableKey createKey(String planName) {
		return new InnerKey(planName);
	}

	public static final CacheableKey createAlternateKey(int tmcode, String state, String description) {
		return new InnerAlternateKey(tmcode, state, description);
	}

}