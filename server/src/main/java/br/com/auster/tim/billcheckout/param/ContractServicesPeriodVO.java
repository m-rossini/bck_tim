/**
 * Copyright (c) 2004-2009 Auster Solutions. All Rights Reserved.
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
 * Created on Apr 8, 2009
 */
package br.com.auster.tim.billcheckout.param;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import br.com.auster.billcheckout.caches.CacheableKey;
import br.com.auster.billcheckout.caches.CacheableVO;
import br.com.auster.om.reference.CustomizableEntity;

/**
 * @author Nardo
 * @version $Id$
 * @since JDK1.4
 */
public class ContractServicesPeriodVO extends CustomizableEntity implements CacheableVO {


	private static final long serialVersionUID = 4479502532239163746L;

	private static final Logger log = Logger.getLogger(ContractServicesPeriodVO.class);

	protected static final String ACTIVATE_STATE = "a";
	protected static final String DEACTIVATE_STATE = "d";
	protected static final String SUSPENDED_STATE = "s";

	private static ThreadLocal<SimpleDateFormat> sdf = new ThreadLocal<SimpleDateFormat>() {
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyMMdd");
		};
	};

	// ---------------------------
	// atributes
	// ---------------------------
	protected String contractNumber;
	protected String csStatChng;
	protected String snCode;

	// generated keys
	protected CacheableKey  naturalKey;
	protected CacheableKey  logicKey;


	// ---------------------------
	// Accessors
	// ---------------------------

	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	public String getCsStatChng() {
		return csStatChng;
	}

	public void setCsStatChng(String csStatChng) {
		this.csStatChng = csStatChng;
	}

	public String getSnCode() {
		return snCode;
	}

	public void setSnCode(String snCode) {
		this.snCode = snCode;
	}

	// ---------------------------
	// CacheableVO interface methods
	// ---------------------------

	/**
	 * @see br.com.auster.billcheckout.caches.CacheableVO#getAlternateKey()
	 */
	public CacheableKey getAlternateKey() {
		if (this.naturalKey == null) {
			this.naturalKey = createAlternateKey( this.getContractNumber(), this.snCode);
		}
		return this.naturalKey;
	}

	/**
	 * Inner class to handle the alternate key for PlansVO instances
	 */
	protected static final class InnerAlternateKey implements CacheableKey {

		private String contractNumber;
		private String sncode;

		public InnerAlternateKey( String contractNumber, String sncode) {
			this.contractNumber = contractNumber;
			this.sncode = sncode;
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

		public void setContractNumber(String contractNumber) {
			this.contractNumber = contractNumber;
		}

		public String getSncode() {
			return sncode;
		}

		public void setSncode(String sncode) {
			this.sncode = sncode;
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
	 * Inner class to handle the alternate key for PlansVO instances
	 */
	protected static final class InnerKey implements CacheableKey {

		private String contractNumber;


		public InnerKey( String contractNumber ) {
			this.contractNumber = contractNumber;
		}

		public boolean equals(Object _other) {
			if ( _other instanceof InnerKey ) {
				InnerKey other = (InnerKey) _other;
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

	public static CacheableKey createKey(String contractNumber) {
		return new InnerKey( contractNumber );
	}

	public static CacheableKey createAlternateKey( String contractNumber, String sncode) {
		return new InnerAlternateKey(contractNumber, sncode);
	}

	public final Date getActivationDate() {
		int lastIndexOf = 0;
		try {
			lastIndexOf = this.csStatChng.lastIndexOf(ACTIVATE_STATE);
			return (Date) sdf.get().parse(this.csStatChng.substring(lastIndexOf-6, lastIndexOf));
		} catch (Exception e) {
			log.warn("Could not parse the activation date " + this.csStatChng + " - lastIndexOf: " + lastIndexOf);
		}
		return null;
	}

	public final Date getExpirationDate() {
		int lastIndexOf = 0;
		int lastIndexOfDeactivate = 0;
		int lastIndexOfSuspended = 0;
		try {
			lastIndexOf = this.csStatChng.lastIndexOf(ACTIVATE_STATE);
			lastIndexOfDeactivate = this.csStatChng.indexOf(DEACTIVATE_STATE, lastIndexOf);
			lastIndexOfSuspended = this.csStatChng.indexOf(SUSPENDED_STATE, lastIndexOf);

			lastIndexOf = lastIndexOfDeactivate > lastIndexOfSuspended ? lastIndexOfDeactivate : lastIndexOfSuspended;

			if (lastIndexOf < 0) {
				return null;
			}
			return (Date) sdf.get().parse(this.csStatChng.substring(lastIndexOf-6, lastIndexOf));
		} catch (Exception e) {
			log.warn("Could not parse the expiration date " + this.csStatChng + " - lastIndexOf: " + lastIndexOf);
		}
		return null;
	}

}
