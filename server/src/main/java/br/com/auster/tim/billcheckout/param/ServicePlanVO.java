/*
 * Copyright (c) 2004-2006 Auster Solutions. All Rights Reserved.
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
 * Created on 01/12/2006
 */
package br.com.auster.tim.billcheckout.param;

import br.com.auster.billcheckout.caches.CacheableKey;
import br.com.auster.billcheckout.caches.CacheableVO;
import br.com.auster.om.reference.CustomizableEntity;

/**
 * @author framos
 * @version $Id$
 *
 */
public class ServicePlanVO extends CustomizableEntity implements CacheableVO {



	// ---------------------------
	// Instance variables
	// ---------------------------

	private static final long serialVersionUID = 1L;
	// TODO will these next two really be used ???
	protected long planUid;
	protected long serviceUid;
	protected boolean containFlag;
	// alternate keys for this relation
	protected String serviceCode;
	protected String planCode;
	protected String planState;
	// generated keys
	private CacheableKey alternateKey;
	private CacheableKey naturalKey;



	// ---------------------------
	// Public methods
	// ---------------------------

	public final long getPlanUid() {
		return this.planUid;
	}
	public final void setPlanUid(long _uid) {
		this.planUid = _uid;
	}

	public final long getServiceUid() {
		return this.serviceUid;
	}
	public final void setServiceUid(long _uid) {
		this.serviceUid = _uid;
	}

	public final boolean isContained() {
		return this.containFlag;
	}
	public final void setContained(boolean _flag) {
		this.containFlag = _flag;
	}

	public final String getPlanCode() {
		return this.planCode;
	}
	public final void setPlanCode(String _code) {
		this.planCode = _code;
	}

	public final String getPlanState() {
		return this.planState;
	}
	public final void setPlanState(String _state) {
		this.planState = _state;
	}

	public final String getServiceCode() {
		return this.serviceCode;
	}
	public final void setServiceCode(String _code) {
		this.serviceCode = _code;
	}


	// ---------------------------
	// CacheableVO interface methods
	// ---------------------------

	/**
	 * @see br.com.auster.billcheckout.caches.CacheableVO#getAlternateKey()
	 */
	public CacheableKey getAlternateKey() {
		if (this.alternateKey == null) {
			this.alternateKey = createAlternateKey(this.getServiceCode(), this.getPlanCode(), this.getPlanState());
		}
		return this.alternateKey;
	}

	/**
	 * Inner class to handle the alternate key for ServicePlan instances
	 */
	protected static final class ServicePlanAlternateKey implements CacheableKey {

		private String plan;
		private String state;
		private String service;

		public ServicePlanAlternateKey(String _service, String _plan, String _state) {
			this.plan = _plan;
			this.state = _state;
			this.service = _service;
		}

		public boolean equals(Object _other) {
			ServicePlanAlternateKey other = (ServicePlanAlternateKey) _other;
			boolean result =  this.plan.equals(other.plan) &&
				   			  this.service.equals(other.service);
			if (this.state != null) {
				result = result && this.state.equals(other.state);
			} else {
				result = result && (other.state == null);
			}
			return result;
		}

		public int hashCode() {
			int hashcode = this.plan.hashCode();
			hashcode += 17 * this.service.hashCode();
			hashcode += 17 * (this.state == null ? 1 : this.state.hashCode()) ;
			return hashcode;
		}

		public String toString() {
			return "ServicePlanAlternateKey {" +
			" plan=" + this.plan +
			" ; state = " + this.state +
			" ; service = " + this.service + " } ";
		}

		public String getPlan() { return this.plan; }
		public String getService() { return this.service; }
		public String getState() { return this.state; }

	}

	/**
	 * @see br.com.auster.billcheckout.caches.CacheableVO#getKey()
	 */
	public CacheableKey getKey() {
		if (this.naturalKey == null) {
			this.naturalKey = createNaturalKey(this.getServiceUid(), this.getPlanUid());
		}
		return this.naturalKey;
	}

	/**
	 * Inner class to handle the alternate key for ServicePlan instances
	 */
	protected static final class ServicePlanNaturalKey implements CacheableKey {

		private long plan;
		private long service;

		public ServicePlanNaturalKey(long _service, long _plan) {
			this.plan = _plan;
			this.service = _service;
		}

		public boolean equals(Object _other) {
			ServicePlanNaturalKey other = (ServicePlanNaturalKey) _other;
			return ((this.plan == other.plan) &&
				    (this.service == other.service));
		}

		public int hashCode() {
			int hashcode = 37 + ((int) (17*this.plan));
			hashcode += 17 * this.service;
			return hashcode;
		}

		public long getPlan() { return this.plan; }
		public long getService() { return this.service; }

	}



	// ---------------------------
	// Helper methods
	// ---------------------------




	// ---------------------------
	// Helper methods
	// ---------------------------

	public static final CacheableKey createNaturalKey(long _service, long _plan) {
		return new ServicePlanNaturalKey(_service, _plan);
	}

	public static final CacheableKey createAlternateKey(String _service, String _plan, String _state) {
		return new ServicePlanAlternateKey(_service, _plan, _state);
	}

}
