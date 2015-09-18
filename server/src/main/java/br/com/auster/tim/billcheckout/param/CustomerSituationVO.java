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
 * Created on 15/10/2007
 */
package br.com.auster.tim.billcheckout.param;

import java.util.Date;

import org.apache.log4j.Logger;

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
public class CustomerSituationVO extends CustomizableEntity implements CacheableVO {


	private static final Logger log = Logger.getLogger(CustomerSituationVO.class);

	// ---------------------------
	// atributes
	// ---------------------------
	protected static final long serialVersionUID = 1L;

	protected String contractId;
	protected Date insertDate;
	// generated keys
	protected CacheableKey  logicKey;

	

	// ---------------------------
	// Accessors
	// ---------------------------


	public String getContractId() { return contractId; }
	public void setContractId(String _id) { this.contractId = _id; }

	public Date getInsertDate() { return this.insertDate; }
	public void setInsertDate(Date _dt) { this.insertDate = _dt; }
	
	public boolean isInPeriod(Date _dtInit, Date _dtEnd) {
		return (this.insertDate.before(_dtEnd) && this.insertDate.after(_dtInit));
	}
	

	
	// ---------------------------
	// CacheableVO interface methods
	// ---------------------------

	/**
	 * @see br.com.auster.billcheckout.caches.CacheableVO#getAlternateKey()
	 */
	public CacheableKey getAlternateKey() {
		return null;
	}

	/**
	 * @see br.com.auster.billcheckout.caches.CacheableVO#getKey()
	 */
	public CacheableKey getKey() {
		if (this.logicKey == null) {
			// actually creates alternate key using Plan description
			// instead of short description
			this.logicKey = createKey( this.getContractId() );
		}
		return this.logicKey;
	}

	/**
	 * Inner class to handle the alternate key for PlansVO instances
	 */
	protected static final class InnerKey implements CacheableKey {

		private String contractId;


		public InnerKey( String contractId) {
			this.contractId = contractId;
		}

		public boolean equals(Object _other) {
			if ( _other instanceof InnerKey ) {
				InnerKey other = (InnerKey) _other;
				return  this.contractId.equals(other.contractId);
			} else { 
				return false;
			}
		}

		public int hashCode() {
			int hashcode = this.contractId.hashCode();

			return hashcode;
		}

		public String getContractId() {
			return contractId;
		}

	}

	// ---------------------------
	// Helper methods
	// ---------------------------

	public static final CacheableKey createKey(String contractId) {
		return new InnerKey( contractId );
	}
}