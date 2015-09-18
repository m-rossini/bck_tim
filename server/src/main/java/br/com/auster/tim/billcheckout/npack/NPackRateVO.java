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
import java.util.List;

import br.com.auster.billcheckout.caches.CacheableKey;
import br.com.auster.billcheckout.caches.CacheableVO;
import br.com.auster.common.datastruct.RangeMap;
import br.com.auster.om.reference.CustomizableEntity;

/**
 * TODO What this class is responsible for
 *
 * @author Frederico Ramos
 * @version $Id$
 * @since JDK1.4
 */
public class NPackRateVO extends CustomizableEntity implements CacheableVO {

	
	
	// ---------------------------
	// Atributes
	// ---------------------------
	private static final long serialVersionUID = 1L;

	private String planName;
	private String uf;
	private Date effectiveDate;
	private RangeMap priceRange;
	
	private CacheableKey  naturalKey;
	
	
	
	// ---------------------------
	// Constructors
	// ---------------------------
	public NPackRateVO() {
		this(0);
	}

	public NPackRateVO(long _uid) {
		super(_uid);
		this.priceRange = new RangeMap();
	}
	
	

	// ---------------------------
	// Accessors
	// ---------------------------

	public final String getPlanName() { return this.planName; }
	public final void setPlanName(String _planName) { this.planName = _planName; }

	public final String getUF() { return this.uf; }
	public final void setUF(String _uf) { this.uf = _uf; }

	public final Date getEffectiveDate() { return this.effectiveDate; }
	public final void setEffectiveDate(Date _dt) { this.effectiveDate = _dt; }

	public final void addPriceFor(long _init, long _end, double _price) {
		this.priceRange.add(_init, _end+1, new Double(_price));
	}
	
	@SuppressWarnings("unchecked")
	public final double getPriceFor(long _amount) {
		List c = this.priceRange.get(_amount);
		return (c != null ? ((Double)c.get(0)).doubleValue() : 0);
	}
	
	
	
	/**
	 * @see br.com.auster.billcheckout.caches.CacheableVO#getAlternateKey()
	 */
	public CacheableKey getKey() {
		if (this.naturalKey == null) {
			// actually creates alternate key using Plan description
			// instead of short description
			this.naturalKey = createKey( this.planName, this.uf, this.effectiveDate );
		}
		return this.naturalKey;
	}



	/**
	 * Inner class to handle the alternate key for PlansVO instances
	 */
	protected static final class InnerKey implements CacheableKey {

		private String planName;
		private String uf;
		private Date dtInit;
		

		public InnerKey( String _planName, String _uf, Date _dtInit ) {
			this.planName = _planName;
			this.uf = _uf;
			this.dtInit = _dtInit;
		}

		public boolean equals(Object _other) {
			boolean equal = true;
			if ( _other instanceof InnerKey ) {
				InnerKey other = (InnerKey) _other;
				equal = equal && other.planName.equals(this.planName);
				equal = equal && other.uf.equals(this.uf);
			} 
			return equal;
		}

		public int hashCode() {
			int hash = 17 * this.planName.hashCode();
			hash += 17 * this.uf.hashCode();
			return hash;
		}

		public final String getPlanName() { return this.planName; }
		public final void setPlanName(String _planName) { this.planName = _planName; }

		public final String getUF() { return this.uf; }
		public final void setUF(String _uf) { this.uf = _uf; }

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

	public static final CacheableKey createKey( String _planName, String _uf, Date _dtInit ) {
		return new InnerKey( _planName, _uf, _dtInit );
	}	
}
