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

import org.apache.log4j.Logger;

import br.com.auster.billcheckout.caches.CacheableKey;

/**
 * TODO What this class is responsible for
 *
 * @author William Soares
 * @version $Id$
 * @since JDK1.4
 */
public class ContractServicesVO extends ContractServicesPeriodVO {


	private static final Logger log = Logger.getLogger(ContractServicesVO.class);

	// ---------------------------
	// atributes
	// ---------------------------
	protected static final long serialVersionUID = 1L;

	protected long tmCode;
	protected long spCode;
	protected String serviceShdes;
	protected long prmValueId;



	// ---------------------------
	// Accessors
	// ---------------------------


	public long getTmCode() {
		return tmCode;
	}

	public void setTmCode(long tmCode) {
		this.tmCode = tmCode;
	}

	public long getSpCode() {
		return spCode;
	}

	public void setSpCode(long spCode) {
		this.spCode = spCode;
	}

	public long getPrmValueId() {
		return prmValueId;
	}

	public void setPrmValueId(long prmValueId) {
		this.prmValueId = prmValueId;
	}

	public void setServiceShDes(String _shdes) { this.serviceShdes = _shdes; }
	public String getServiceShDes() { return this.serviceShdes; }

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
			this.naturalKey = createAlternateKey( this.getContractNumber(), this.getSnCode(), this.getTmCode() );
		}
		return this.naturalKey;
	}

	/**
	 * Inner class to handle the alternate key for PlansVO instances
	 */
	protected static final class InnerAlternateKey implements CacheableKey {

		private String contractNumber;
		private String snCode;
		private long tmCode;


		public InnerAlternateKey( String contractNumber, String snCode, long tmCode ) {
			this.contractNumber = contractNumber;
			this.snCode = snCode;
			this.tmCode = tmCode;
		}

		public boolean equals(Object _other) {
			if ( _other instanceof InnerAlternateKey ) {
				InnerAlternateKey other = (InnerAlternateKey) _other;
				return  this.contractNumber.equals(other.contractNumber) && this.snCode.equals(other.snCode)
						&& this.tmCode == other.tmCode;
			}
			else
				return false;
		}

		public int hashCode() {
			int hashcode = this.contractNumber.hashCode();
			hashcode += this.snCode.hashCode();
			hashcode += 17 * this.tmCode;

			return hashcode;
		}

		public String getSnCode() {
			return snCode;
		}

		public String getContractNumber() {
			return contractNumber;
		}

		public long getTmCode() {
			return tmCode;
		}

	}

	/**
	 * @see br.com.auster.billcheckout.caches.CacheableVO#getKey()
	 */
	public CacheableKey getKey() {
		if (this.logicKey == null) {
			// actually creates alternate key using Plan description
			// instead of short description
			this.logicKey = createKey( this.getContractNumber() );
		}
		return this.logicKey;
	}

	/**
	 * Inner class to handle the alternate key for PlansVO instances
	 */
	protected static final class InnerKey implements CacheableKey {

		private String contractNumber;


		public InnerKey( String contractNumber ) {
			this.contractNumber = contractNumber;
		}

		public boolean equals(Object _other) {
			if ( _other instanceof InnerAlternateKey ) {
				InnerAlternateKey other = (InnerAlternateKey) _other;
				return  this.contractNumber.equals(other.contractNumber);
			}
			else
				return false;
		}

		public int hashCode() {
			int hashcode = this.contractNumber.hashCode();

			return hashcode;
		}

		public String getContractNumber() {
			return contractNumber;
		}

	}

	// ---------------------------
	// Helper methods
	// ---------------------------

	public static final CacheableKey createKey(String contractNumber) {
		return new InnerKey( contractNumber );
	}

	public static final CacheableKey createAlternateKey( String contractNumber, String snCode, long tmCode ) {
		return new InnerAlternateKey( contractNumber, snCode, tmCode );
	}
}