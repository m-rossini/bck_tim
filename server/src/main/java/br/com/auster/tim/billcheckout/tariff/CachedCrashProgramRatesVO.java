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
public class CachedCrashProgramRatesVO extends CustomizableEntity implements CacheableVO {

	/**
	 *
	 */

	// ---------------------------
	// atributes
	// ---------------------------
	private static final long serialVersionUID = 1L;

	private String costId;
	private String csgCode;

	private RangeMap map;
	
	// generated keys
	private CacheableKey  naturalKey;
	private CacheableKey  alternateKey;

	/**
	 *
	 */

	// ---------------------------
	// Constructors
	// ---------------------------
	public CachedCrashProgramRatesVO() {
		this(0);
	}

	/**
	 * @param uid
	 */
	public CachedCrashProgramRatesVO(long _uid) {
		setUid(_uid);
		
		this.map = new RangeMap();
	}

	// ---------------------------
	// Accessors
	// ---------------------------


	public final String getCostId() { return costId; }
	public final void setCostId(String costId) { this.costId = costId; }

	public final String getCsgCode() { return csgCode; }
	public final void setCsgCode(String csgCode) { this.csgCode = csgCode; }
	
	
	@SuppressWarnings("unchecked")
	public CrashProgramRatesVO getRate(long _volume) {
		List l = this.map.get(_volume);
		if (l != null) {
			return (CrashProgramRatesVO) l.get(0);
		}
		return null;
	}
	
	public int getNumberOfRanges() {
		return this.map.getSize();
	}
	
	public void addRate(CrashProgramRatesVO _vo) {
		this.map.add(_vo.getInitVolume(), _vo.getEndVolume(), _vo);
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
			this.naturalKey = createKey( this.costId, this.csgCode );
		}
		return this.naturalKey;
	}

	/**
	 * @see br.com.auster.billcheckout.caches.CacheableVO#getKey()
	 */
	public CacheableKey getAlternateKey() {
		return null;
	}

	/**
	 * Inner class to handle the alternate key for PlansVO instances
	 */
	protected static final class InnerKey implements CacheableKey {

		private String costId;
		private String csgCode;

		public InnerKey( String _costId, String _csgCode) {
			this.costId = _costId;
			this.csgCode = _csgCode;
		}

		public boolean equals(Object _other) {
			if ( _other instanceof InnerKey ) {
				InnerKey other = (InnerKey) _other;
				boolean result = true;
				if (this.costId == null) {
					result &= (other.costId == null);
				} else {
					result &= (this.costId.equals(other.costId));
				}
				if (this.csgCode == null) {
					result &= (other.csgCode == null);
				} else {
					result &= (this.csgCode.equals(other.csgCode));
				}
				return result;
			} 
			return false;
		}

		public int hashCode() {
			int hashcode =  17 * this.costId.hashCode();
			hashcode += 17 * this.csgCode.hashCode();
			return hashcode;
		}

		public final String getCostId() { return costId; }
		public final String getCsgCode() { return csgCode; }

	}


	
	// ---------------------------
	// Helper methods
	// ---------------------------

	public static final CacheableKey createKey( String _costId, String _csgCode) {
		return new InnerKey( _costId, _csgCode );
	}
}
