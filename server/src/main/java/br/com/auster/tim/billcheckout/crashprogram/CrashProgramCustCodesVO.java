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
package br.com.auster.tim.billcheckout.crashprogram;

import br.com.auster.billcheckout.caches.CacheableKey;
import br.com.auster.billcheckout.caches.CacheableVO;
import br.com.auster.om.reference.CustomizableEntity;

/**
 * TODO What this class is responsible for
 *
 * @author Frederico Ramos
 * @version $Id$
 * @since JDK1.4
 */
public class CrashProgramCustCodesVO extends CustomizableEntity implements CacheableVO {

	
	
	// ---------------------------
	// Atributes
	// ---------------------------
	private static final long serialVersionUID = 1L;

	private String custCode;
	
	private CacheableKey  naturalKey;
	
	
	
	// ---------------------------
	// Constructors
	// ---------------------------
	public CrashProgramCustCodesVO() {
		super(0);
	}

	public CrashProgramCustCodesVO(long _uid) {
		super(_uid);
	}
	
	

	// ---------------------------
	// Accessors
	// ---------------------------

	public final String getCustCode() { return this.custCode; }
	public final void setCustCode(String _custCode) { this.custCode = _custCode; }

	/**
	 * @see br.com.auster.billcheckout.caches.CacheableVO#getAlternateKey()
	 */
	public CacheableKey getKey() {
		if (this.naturalKey == null) {
			// actually creates alternate key using Plan description
			// instead of short description
			this.naturalKey = createKey( this.custCode );
		}
		return this.naturalKey;
	}



	/**
	 * Inner class to handle the alternate key for PlansVO instances
	 */
	protected static final class InnerKey implements CacheableKey {

		private String custCode;

		public InnerKey( String _custCode ) {
			this.custCode = _custCode;
		}

		public boolean equals(Object _other) {
			if ( _other instanceof InnerKey ) {
				InnerKey other = (InnerKey) _other;
				return other.custCode.equals(this.custCode);
			} 
			return false;
		}

		public int hashCode() {
			return this.custCode.hashCode();
		}

		public String getCustCode() { return this.custCode; }
		public void setCustCode(String _custCode) { this.custCode = _custCode; }
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

	public static final CacheableKey createKey( String _custCode) {
		return new InnerKey( _custCode );
	}	
}
