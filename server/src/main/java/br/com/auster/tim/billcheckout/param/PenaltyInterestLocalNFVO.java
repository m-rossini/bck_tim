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
 * Created on 27/03/2008
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
public class PenaltyInterestLocalNFVO extends CustomizableEntity implements CacheableVO {
	
	// ---------------------------
	// atributes
	// ---------------------------
	private static final long serialVersionUID = 1L;
	
	private double invoiceValue; 
	private String customerId;
	private String dueDate;

	// generated keys
	private CacheableKey  logicKey;
	private CacheableKey  naturalKey;

	

	// ---------------------------
	// Constructors
	// ---------------------------
	public PenaltyInterestLocalNFVO() {
		this(0);
	}
	
	/**
	 * @param uid
	 */
	public PenaltyInterestLocalNFVO(long _uid) {
		setUid(_uid);
	}

	// ---------------------------
	// Accessors
	// ---------------------------
	
	public double getInvoiceValue() {
		return invoiceValue;
	}

	public void setInvoiceValue(double invoiceValue) {
		this.invoiceValue = invoiceValue;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
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
			this.naturalKey = createAlternateKey( this.getCustomerId(), this.getDueDate() );
		}
		return this.naturalKey;
	}	
	
	/**
	 * Inner class to handle the alternate key for PlansVO instances
	 */
	protected static final class InnerAlternateKey implements CacheableKey {
		
		private String customerId;		
		private String dueDate;
		
		public InnerAlternateKey( String customerId, String dueDate) {
			this.customerId = customerId;
			this.dueDate = dueDate;
		}
		
		public boolean equals(Object _other) {
			if ( _other instanceof InnerAlternateKey ) {
				InnerAlternateKey other = (InnerAlternateKey) _other;
				if (this.customerId == null || this.dueDate == null) {
					return false;
				}
				return  customerId.equals(other.customerId) && dueDate.equals(other.dueDate);				
			}
			else 
				return false;
		}

		public int hashCode() {
			int hashcode = this.customerId == null ? 0 : this.customerId.hashCode();
			hashcode += this.dueDate == null ? 0 : this.dueDate.hashCode();
			
			return hashcode;
		}

		public String getCustomerId() {
			return customerId;
		}

		public String getDueDate() {
			return dueDate;
		}

	}
	
	/**
	 * @see br.com.auster.billcheckout.caches.CacheableVO#getKey()
	 */
	public CacheableKey getKey() {
		if (this.logicKey == null) {
			this.logicKey = createKey( this.getCustomerId() );
		}
		return this.logicKey;
	}
	
	/**
	 * Inner class to handle the alternate key for ServicePlan instances
	 */
	protected static final class InnerKey implements CacheableKey {
		
		private String customerId;
		
		public InnerKey(String customerId){
			this.customerId = customerId;
		}
		
		public boolean equals(Object _other) {
			if ( _other instanceof InnerKey ) {
				InnerKey other = (InnerKey) _other;
				return (this.customerId == other.customerId);
			}
			else 
				return false;
		}

		public int hashCode() {
			int hashcode = this.customerId == null ? 0 : this.customerId.hashCode();
			return hashcode;
		}
		
		public String getCustomerId() { return this.customerId; }
	}	


	
	// ---------------------------
	// Helper methods
	// ---------------------------

	public static final CacheableKey createKey(String customerId) {
		return new InnerKey(customerId);
	}
		
	public static final CacheableKey createAlternateKey( String customerId, String dueDate ) {
		return new InnerAlternateKey( customerId, dueDate );
	}

}
