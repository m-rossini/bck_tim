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
 * Created on 25/03/2010
 */
package br.com.auster.tim.billcheckout.npack;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import br.com.auster.billcheckout.caches.CacheableKey;
import br.com.auster.billcheckout.caches.CacheableVO;
import br.com.auster.om.reference.CustomizableEntity;
import br.com.auster.om.util.UnitCounter;

/**
 * TODO What this class is responsible for
 *
 * @author Frederico Ramos
 * @version $Id$
 * @since JDK1.4
 */
public class NPackPackageInfoVO extends CustomizableEntity implements CacheableVO {

	
	
	// ---------------------------
	// Atributes
	// ---------------------------
	private static final long serialVersionUID = 1L;

	private String customerId;
	private Date initDate;	
	private String packageName;
	private Set<String> plans;
	private String pattern;
	private long fuom;
	
	private Map<String, UnitCounter> accumulatedUsage;
	
	private CacheableKey  naturalKey;
	
	
	
	// ---------------------------
	// Constructors
	// ---------------------------
	public NPackPackageInfoVO() {
		this(0);
	}

	public NPackPackageInfoVO(long _uid) {
		super(_uid);
		this.plans = new HashSet<String>();
		this.accumulatedUsage = new HashMap<String, UnitCounter>();
	}
	
	

	// ---------------------------
	// Accessors
	// ---------------------------

	public final String getCustomerId() { return this.customerId; }
	public final void setCustomerId(String _id) { this.customerId = _id; }

	public final String getPackageName() { return this.packageName; }
	public final void setPackageName(String _des) { this.packageName = _des; }

	public final Date geInitDate() { return this.initDate; }
	public final void setInitDate(Date _dt) { this.initDate = _dt; }
	
	public final String getOCCPattern() { return this.pattern; }
	public final void setOCCPattern(String _pattern) { this.pattern = _pattern; }
	
	public final void addPlan(String _planName) { this.plans.add(_planName); }
	public final boolean hasPlan(String _planName) { return this.plans.contains(_planName); }
	public final Set<String> getAllPlans() { return this.plans; }

	public final void addTotalMinutes(long _hours) { this.fuom = _hours * 60; }
	public final long getTotalMinutes() { return this.fuom; }
	
	public final void addUsage(String _coid, UnitCounter _usage) {
		UnitCounter newUC = null;
		if (!this.accumulatedUsage.containsKey(_coid)) {
			newUC = new UnitCounter(_usage.getType());
			this.accumulatedUsage.put(_coid, newUC);
		} else {
			newUC = this.accumulatedUsage.get(_coid);
		}
		newUC.addUnits(_usage.getUnits());
		return;
	}
	
	public final UnitCounter getUsage(String _coid) {
		return this.accumulatedUsage.get(_coid);
	}
	
	public final Map<String, UnitCounter> getContractsAndUsages() {
		return this.accumulatedUsage;
	}
	
	
	/**
	 * @see br.com.auster.billcheckout.caches.CacheableVO#getAlternateKey()
	 */
	public CacheableKey getKey() {
		if (this.naturalKey == null) {
			// actually creates alternate key using Plan description
			// instead of short description
			this.naturalKey = createKey( this.customerId, this.initDate );
		}
		return this.naturalKey;
	}



	/**
	 * Inner class to handle the alternate key for PlansVO instances
	 */
	protected static final class InnerKey implements CacheableKey {

		private String customerId;
		private Date dtInit;
		

		public InnerKey( String _customerId, Date _dtInit ) {
			this.customerId = _customerId;
			this.dtInit = _dtInit;
		}

		public boolean equals(Object _other) {
			if ( _other instanceof InnerKey ) {
				InnerKey other = (InnerKey) _other;
				return other.customerId.equals(this.customerId);
			} 
			return false;
		}

		public int hashCode() {
			return this.customerId.hashCode();
		}

		public final String getCustomerId() { return this.customerId; }
		public final void setCustomerId(String _id) { this.customerId = _id; }

		public final Date getInitDate() { return this.dtInit; }
		public final void setInitDate(Date _dtInit) { this.dtInit = _dtInit; }
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

	public static final CacheableKey createKey( String _customerId, Date _dtInit ) {
		return new InnerKey( _customerId, _dtInit );
	}	
}
