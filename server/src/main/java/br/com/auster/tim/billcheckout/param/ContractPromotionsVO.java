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
 * Created on 10/10/2007
 */
package br.com.auster.tim.billcheckout.param;

import java.sql.Date;

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
public class ContractPromotionsVO extends CustomizableEntity implements CacheableVO {
	
	/**
	 * 
	 */
	
	// ---------------------------
	// atributes
	// ---------------------------
	private static final long serialVersionUID = 1L;
	
	private String contractNumber;
	private String promotionCode;
	private Date activationDate; 
	private Date expirationDate;
	private String promotionDesc;

	// generated keys
	private CacheableKey  logicKey;
	private CacheableKey  naturalKey;

	/**
	 * 
	 */

	// ---------------------------
	// Constructors
	// ---------------------------

	public ContractPromotionsVO() {
	
	}
	
	/**
	 * @param uid
	 */
	public ContractPromotionsVO(String contractNumber, String promotionCode, Date activationDate, Date expirationDate, String promotionDesc) {		
		setContractNumber(contractNumber);
		setPromotionCode(promotionCode);
		setActivationDate(activationDate);
		setExpirationDate(expirationDate);
		setPromotionDesc(promotionDesc);
	}

	// ---------------------------
	// Accessors
	// ---------------------------

	public Date getActivationDate() {
		return activationDate;
	}

	public void setActivationDate(Date activationDate) {
		this.activationDate = activationDate;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getPromotionDesc() {
		return promotionDesc;
	}

	public void setPromotionDesc(String promotionDesc) {
		this.promotionDesc = promotionDesc;
	}
	
	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	public String getPromotionCode() {
		return promotionCode;
	}

	public void setPromotionCode(String promotionCode) {
		this.promotionCode = promotionCode;
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
			this.naturalKey = createAlternateKey( this.getContractNumber(), this.getPromotionCode() );
		}
		return this.naturalKey;
	}	
	
	/**
	 * Inner class to handle the alternate key for PlansVO instances
	 */
	protected static final class InnerAlternateKey implements CacheableKey {
		
		private String contractNumber;
		private String promotionCode;
		
		public InnerAlternateKey( String contractNumber, String promotionCode ) {
			this.contractNumber = contractNumber;
			this.promotionCode = promotionCode;			
		}
		
		public boolean equals(Object _other) {
			if ( _other instanceof InnerAlternateKey ) {
				InnerAlternateKey other = (InnerAlternateKey) _other;
				return  this.contractNumber.equals(other.contractNumber) && this.promotionCode.equals(promotionCode);
			}
			else 
				return false;
		}

		public int hashCode() {
			int hashcode = this.contractNumber.hashCode();
			hashcode += this.promotionCode.hashCode();			
			return hashcode;
		}

		public String getContractNumber() {
			return contractNumber;
		}

		public void setContractNumber(String contractNumber) {
			this.contractNumber = contractNumber;
		}

		public String getPromotionCode() {
			return promotionCode;
		}

		public void setPromotionCode(String promotionCode) {
			this.promotionCode = promotionCode;
		}
	
	}
	
	/**
	 * @see br.com.auster.billcheckout.caches.CacheableVO#getKey()
	 */
	public CacheableKey getKey() {
		if (this.logicKey == null) {
			this.logicKey = createKey( this.getContractNumber() );
		}
		return this.logicKey;
	}
	
	/**
	 * Inner class to handle the alternate key for ServicePlan instances
	 */
	protected static final class InnerKey implements CacheableKey {
		
		private String contractNumber;
		
		public InnerKey( String contractNumber ) {
			this.contractNumber = contractNumber;
		}
		
		public boolean equals(Object _other) {
			if ( _other instanceof InnerKey ) {
				InnerKey other = (InnerKey) _other;
				return (this.contractNumber.equals(other.contractNumber));
			}
			else 
				return false;
		}

		public int hashCode() {
			return this.contractNumber.hashCode();			
		}
		
		public String getContractNumber() { return this.contractNumber; }
	}	


	
	// ---------------------------
	// Helper methods
	// ---------------------------

	public static final CacheableKey createKey(String contractNumber) {
		return new InnerKey(contractNumber);
	}
		
	public static final CacheableKey createAlternateKey( String contractNumber, String promotionCode) {
		return new InnerAlternateKey( contractNumber, promotionCode );
	}
	
}