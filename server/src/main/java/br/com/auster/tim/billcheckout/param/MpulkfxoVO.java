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
 * Created on 17/10/2007
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
public class MpulkfxoVO extends CustomizableEntity implements CacheableVO {

	/**
	 * 
	 */
	
	// ---------------------------
	// atributes
	// ---------------------------
	private static final long serialVersionUID = 1L;
	
	private String promotionCode;
	private String contractNumber;
	private String ffCode;
	private String destination;
	
	// generated keys
	private CacheableKey  logicKey;
	private CacheableKey  naturalKey;

	/**
	 * 
	 */

	// ---------------------------
	// Constructors
	// ---------------------------
	public MpulkfxoVO() {
		this(0);
	}
	
	/**
	 * @param uid
	 */
	public MpulkfxoVO(long _uid) {
		setUid(_uid);
	}

	// ---------------------------
	// Accessors
	// ---------------------------

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getFfCode() {
		return ffCode;
	}

	public void setFfCode(String ffCode) {
		this.ffCode = ffCode;
	}		
	
	public String getPromotionCode() {
		return promotionCode;
	}

	public void setPromotionCode(String promotionCode) {
		this.promotionCode = promotionCode;
	}

	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
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
			this.naturalKey = createAlternateKey( this.getContractNumber(), this.getPromotionCode());
		}
		return this.naturalKey;
	}	
	
	/**
	 * Inner class to handle the alternate key for PlansVO instances
	 */
	protected static final class InnerAlternateKey implements CacheableKey {
		
		private String promotionCode;
		private String contractNumber;	

		public InnerAlternateKey( String contractNumber, String promotionCode ) {
			this.contractNumber = contractNumber;			
			this.promotionCode = promotionCode;
		}
		
		public boolean equals(Object _other) {
			if ( _other instanceof InnerAlternateKey ) {
				InnerAlternateKey other = (InnerAlternateKey) _other;
				return  this.contractNumber.equals(other.contractNumber) && this.promotionCode.equals(other.promotionCode);
			}
			else 
				return false;
		}

		public int hashCode() {
			int hashCode = this.contractNumber.hashCode();
			hashCode += this.promotionCode.hashCode();
			
			return hashCode;
		}

		public String getPromotionCode() {
			return promotionCode;
		}

		public String getContractNumber() {
			return contractNumber;
		}

	}
	
	/**
	 * @see br.com.auster.billcheckout.caches.CacheableVO#getKey()
	 */
	public CacheableKey getKey() {
		if (this.logicKey == null) {
			this.logicKey = createKey( this.ffCode );
		}
		return this.logicKey;
	}
	
	/**
	 * Inner class to handle the alternate key for ServicePlan instances
	 */
	protected static final class InnerKey implements CacheableKey {
		
		private String ffCode;
		
		public InnerKey(String ffCode) {
			this.ffCode = ffCode;
		}
		
		public boolean equals(Object _other) {
			if ( _other instanceof InnerKey ) {
				InnerKey other = (InnerKey) _other;
				return (this.ffCode.equals(other.ffCode));
			}
			else 
				return false;
		}

		public int hashCode() {
			return this.ffCode.hashCode();	
		}
		
		public String getFfCode() { 
			return this.ffCode; 
		}

	}	

	
	// ---------------------------
	// Helper methods
	// ---------------------------

	public static final CacheableKey createKey( String ffCode ) {
		return new InnerKey( ffCode );
	}
		
	public static final CacheableKey createAlternateKey( String contractNumber, String promotionCode ) {
		return new InnerAlternateKey( contractNumber, promotionCode );
	}
	
}