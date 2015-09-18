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
 * Created on 22/04/2008
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
public class DwPromoMcVO extends CustomizableEntity implements CacheableVO {

	// ---------------------------
	// atributes
	// ---------------------------
	private static final long serialVersionUID = 1L;
	 
	private String serviceCode;
	private String shdes;

	// generated keys
	private CacheableKey  naturalKey;

	// ---------------------------
	// Accessors
	// ---------------------------
	
	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	
	public String getShdes() {
		return shdes;
	}

	public void setShdes(String shdes) {
		this.shdes = shdes;
	}
	
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
			this.naturalKey = createAlternateKey( this.getShdes() );
		}
		return this.naturalKey;
	}	
	
	/**
	 * Inner class to handle the alternate key for DwPromoMcVO instances
	 */
	protected static final class InnerAlternateKey implements CacheableKey {
		
		private String shdes;
		
		public InnerAlternateKey( String shdes) {
			this.shdes = shdes;
		}
		
		public boolean equals(Object _other) {
			if ( _other instanceof InnerAlternateKey ) {
				InnerAlternateKey other = (InnerAlternateKey) _other;
				return  (this.shdes==null) ? "".equals(other.shdes) : this.shdes.equals(other.shdes);
			}
			else 
				return false;
		}

		public int hashCode() { 
			return this.shdes == null ? 0 : this.shdes.hashCode();		
		}

		public String getShdes() {
			return shdes;
		}

	}
	
	/**
	 * @see br.com.auster.billcheckout.caches.CacheableVO#getKey()
	 */
	public CacheableKey getKey() {
		return null;
	}
	
	// ---------------------------
	// Helper methods
	// ---------------------------
		
	public static final CacheableKey createAlternateKey( String shdes ) {
		return new InnerAlternateKey( shdes );
	}
	
}
