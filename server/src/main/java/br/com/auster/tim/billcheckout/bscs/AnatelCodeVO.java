/*
 * Copyright (c) 2004-2008 Auster Solutions. All Rights Reserved.
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
 * Created on 01/07/2008
 */
package br.com.auster.tim.billcheckout.bscs;

import br.com.auster.billcheckout.caches.CacheableKey;
import br.com.auster.billcheckout.caches.CacheableVO;

/**
 * @author framos
 * @version $Id$
 *
 */
public class AnatelCodeVO implements CacheableVO {



	private CacheableKey key;

	private String rateplanShdes;
	private String packageShortName;
	private String uf;
	private String anatelCode;
	

	


	public String getRateplanShdes() {
		return rateplanShdes;
	}

	public void setRateplanShdes(String rateplanShdes) {
		this.rateplanShdes = rateplanShdes;
	}

	public String getPackageShortName() {
		return packageShortName;
	}

	public void setPackageShortName(String packageShortName) {
		this.packageShortName = packageShortName;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getAnatelCode() {
		return anatelCode;
	}

	public void setAnatelCode(String anatelCode) {
		this.anatelCode = anatelCode;
	}

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
		if (this.key == null) {
			this.key = createKey( this.rateplanShdes, this.packageShortName, this.uf );
		}
		return this.key;
	}

	/**
	 * Inner class to handle the alternate key for PlansVO instances
	 */
	protected static final class InnerKey implements CacheableKey {

		private String rateplanCode;
		private String packageCode;
		private String ufCode;

		public InnerKey( String _rateplanCode, String _packageCode, String _ufCode) {
			this.rateplanCode = _rateplanCode;
			this.packageCode = _packageCode;
			this.ufCode = _ufCode;
		}

		public boolean equals(Object _other) {
			if ( _other instanceof InnerKey ) {
				InnerKey other = (InnerKey) _other;
				boolean result = true;
				// validate rateplan
				if (this.rateplanCode == null) {
					result = result && (other.rateplanCode == null);
				} else {
					result = result && this.rateplanCode.equals(other.rateplanCode);
				}
				// validate package
				if (this.packageCode == null) {
					result = result && (other.packageCode == null);
				} else {
					result = result && this.packageCode.equals(other.packageCode);
				}
				// validate uf
				if (this.ufCode == null) {
					result = result && (other.ufCode == null);
				} else {
					result = result && this.ufCode.equals(other.ufCode);
				}
				// returning
				return result;
			}
			else { return false; }
		}

		public int hashCode() {
			int hash = 37;
			hash += 17 * (this.rateplanCode == null ? 1 : this.rateplanCode.hashCode());
			hash += 17 * (this.packageCode == null ? 1 : this.packageCode.hashCode());
			hash += 17 * (this.ufCode == null ? 1 : this.ufCode.hashCode());
			return hash;
		}

		public String getRateplanShdes() { return this.rateplanCode; }
		public String getPackageShortName() { return this.packageCode; }
		public String getUf() { return this.ufCode; }
	}


	// ---------------------------
	// Helper methods
	// ---------------------------

	public static final CacheableKey createKey( String _rateplan, String _package, String _uf ) {
		return new InnerKey(_rateplan, _package, _uf);
	}

}
