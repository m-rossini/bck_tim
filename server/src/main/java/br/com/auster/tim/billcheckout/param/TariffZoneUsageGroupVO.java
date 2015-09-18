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
public class TariffZoneUsageGroupVO extends CustomizableEntity implements CacheableVO {

	
	
	// ---------------------------
	// Instance variables
	// ---------------------------
	
	// TODO will these next two really be used ???
	protected long tariffZoneUid;
	protected long usageGroupUid;
	protected boolean allowedFlag;
	// alternate keys for this relation	
	protected String usageGroupCode;
	protected String tariffZoneCode;
	// generated keys
	private CacheableKey alternateKey;
	private CacheableKey naturalKey;
	
	

	// ---------------------------
	// Public methods
	// ---------------------------
	
	public final long getTariffZoneUid() {
		return this.tariffZoneUid;
	}
	public final void setTariffZoneUid(long _uid) {
		this.tariffZoneUid = _uid;
	}

	public final long getUsageGroupUid() {
		return this.usageGroupUid;
	}
	public final void setUsageGroupUid(long _uid) {
		this.usageGroupUid = _uid;
	}
	
	public final boolean isAllowed() {
		return this.allowedFlag;
	}
	public final void setAllowed(boolean _flag) {
		this.allowedFlag = _flag;
	}
	
	public final String getTariffZoneCode() {
		return this.tariffZoneCode;
	}
	public final void setTariffZoneCode(String _code) {
		this.tariffZoneCode = _code;
	}

	public final String getUsageGroupCode() {
		return this.usageGroupCode;
	}
	public final void setUsageGroupCode(String _code) {
		this.usageGroupCode = _code;
	}
	
	
	// ---------------------------
	// CacheableVO interface methods
	// ---------------------------
	
	/**
	 * @see br.com.auster.billcheckout.caches.CacheableVO#getAlternateKey()
	 */
	public CacheableKey getAlternateKey() {
		if (this.alternateKey == null) {
			this.alternateKey = createAlternateKey(this.getTariffZoneCode(), this.getUsageGroupCode());
		}
		return this.alternateKey;
	}
	
	/**
	 * Inner class to handle the alternate key for ServicePlan instances
	 */
	protected static final class TariffZoneUsageGroupAlternateKey implements CacheableKey {
		
		private String tarrifZone;
		private String usageGroup;
		
		public TariffZoneUsageGroupAlternateKey(String _tarrifZone, String _usageGroup) {
			this.tarrifZone = _tarrifZone;
			this.usageGroup = _usageGroup;
		}
		
		public boolean equals(Object _other) {
			TariffZoneUsageGroupAlternateKey other = (TariffZoneUsageGroupAlternateKey) _other;
			return this.tarrifZone.equals(other.tarrifZone) &&
				   this.usageGroup.equals(other.usageGroup);
		}

		public int hashCode() {
			int hashcode = this.tarrifZone.hashCode();
			hashcode += this.usageGroup.hashCode();
			return hashcode;
		}
		
		public String getTarrifZone() { return this.tarrifZone; }
		public String getUsageGroup() { return this.usageGroup; }
	
	}	

	/**
	 * @see br.com.auster.billcheckout.caches.CacheableVO#getKey()
	 */
	public CacheableKey getKey() {
		if (this.naturalKey == null) {
			this.naturalKey = createNaturalKey(this.getTariffZoneUid(), this.getUsageGroupUid());
		}
		return this.naturalKey;
	}

	/**
	 * Inner class to handle the alternate key for RateGuiding instances
	 */
	protected static final class TariffZoneUsageGroupNaturalKey implements CacheableKey {
		
		private long tarrifZone;
		private long usageGroup;
		
		public TariffZoneUsageGroupNaturalKey(long _tarrifZone, long _usageGroup) {
			this.tarrifZone = _tarrifZone;
			this.usageGroup = _usageGroup;
		}
		
		public boolean equals(Object _other) {
			TariffZoneUsageGroupNaturalKey other = (TariffZoneUsageGroupNaturalKey) _other;
			return ((this.tarrifZone == other.tarrifZone) &&
				    (this.usageGroup == other.usageGroup));
		}

		public int hashCode() {
			int hashcode = 37 + ((int) (17*this.tarrifZone));
			hashcode += 17 * this.usageGroup;
			return hashcode;
		}
	
		public long getTarrifZone() { return this.tarrifZone; }
		public long getUsageGroup() { return this.usageGroup; }
	}		


		
	// ---------------------------
	// Helper methods
	// ---------------------------

	public static final CacheableKey createNaturalKey(long _tariff, long _usageGroup) {
		return new TariffZoneUsageGroupNaturalKey(_tariff, _usageGroup);
	}

	public static final CacheableKey createAlternateKey(String _tariff, String _usageGroup) {
		return new TariffZoneUsageGroupAlternateKey(_tariff, _usageGroup);
	}
	
	
}
