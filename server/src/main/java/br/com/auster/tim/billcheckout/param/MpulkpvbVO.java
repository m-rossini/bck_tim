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
 * Created on 25/10/2007
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
public class MpulkpvbVO extends CustomizableEntity implements CacheableVO {
	
	/**
	 * 
	 */
	
	// ---------------------------
	// atributes
	// ---------------------------
	private static final long serialVersionUID = 1L;
	
	private double subscript;
	private long tmCode;
	private String snCode;
	private long spCode;
	private long prmValueId;

	private CacheableKey  naturalKey;

	/**
	 * 
	 */

	// ---------------------------
	// Constructors
	// ---------------------------

	public MpulkpvbVO() {
	
	}
	
	/**
	 * Creates a new instance of the class <code>ContractServicesVO</code>.
	 * 
	 * @param contractNumber
	 * @param planCode
	 * @param csStatChng
	 * @param snCode
	 */
	public MpulkpvbVO(double subscript, long tmCode, long spCode, long prmValueId, String snCode) {		
		setSubscript(subscript);
		setTmCode(tmCode);
		setSpCode(spCode);
		setPrmValueId(prmValueId);
		setSnCode(snCode);
	}

	// ---------------------------
	// Accessors
	// ---------------------------
	

	public long getTmCode() {
		return tmCode;
	}

	public void setTmCode(long tmCode) {
		this.tmCode = tmCode;
	}

	public String getSnCode() {
		return snCode;
	}

	public void setSnCode(String snCode) {
		this.snCode = snCode;
	}		
	
	public long getSpCode() {
		return spCode;
	}

	public void setSpCode(long spCode) {
		this.spCode = spCode;
	}

	public double getSubscript() {
		return subscript;
	}

	public void setSubscript(double subscript) {
		this.subscript = subscript;
	}
	
	public long getPrmValueId() {
		return prmValueId;
	}

	public void setPrmValueId(long prmValueId) {
		this.prmValueId = prmValueId;
	}	
	
	/**
	 * @see br.com.auster.billcheckout.caches.CacheableVO#getAlternateKey()
	 */
	public CacheableKey getAlternateKey() {
		if (this.naturalKey == null) {
			// actually creates alternate key using Plan description
			// instead of short description
			this.naturalKey = createAlternateKey(this.getTmCode(), this.getSpCode(), this.getSnCode(), this.getPrmValueId() );
		}
		return this.naturalKey;
	}	
	
	/**
	 * Inner class to handle the alternate key for PlansVO instances
	 */
	protected static final class InnerAlternateKey implements CacheableKey {
		
		private long tmCode;
		private String snCode;
		private long spCode;
		private long prmValueId;
		
		public InnerAlternateKey( long tmCode, long spCode, long prmValueId, String snCode ) {
			this.spCode = spCode;
			this.snCode = snCode;
			this.tmCode = tmCode;
			this.prmValueId = prmValueId;
		}
		
		public boolean equals(Object _other) {
			if ( _other instanceof InnerAlternateKey ) {
				InnerAlternateKey other = (InnerAlternateKey) _other;
				return  this.spCode == other.spCode 
						&& this.snCode.equals(other.snCode) 
						&& this.tmCode == other.tmCode
						&& this.prmValueId == other.prmValueId;
			}
			else 
				return false;
		}

		public int hashCode() {
			int hashcode = 0;
			hashcode += 37 + ((int) (17*this.spCode));
			hashcode += 17*this.tmCode;
			hashcode += 17*this.prmValueId;
			hashcode += this.snCode.hashCode();
						
			return hashcode;
		}		

		public long getSpCode() {
			return spCode;
		}

		public void setSpCode(long spCode) {
			this.spCode = spCode;
		}

		public long getTmCode() {
			return tmCode;
		}

		public void setTmCode(long tmCode) {
			this.tmCode = tmCode;
		}

		public String getSnCode() {
			return snCode;
		}

		public void setSnCode(String snCode) {
			this.snCode = snCode;
		}
	
		public long getPrmValueId() {
			return prmValueId;
		}

		public void setPrmValueId(long prmValueId) {
			this.prmValueId = prmValueId;
		}						
		
	}
	
	public static final CacheableKey createAlternateKey( long tmCode, long spCode, String snCode, long prmValueId ) {
		return new InnerAlternateKey( tmCode, spCode, prmValueId, snCode );
	}

	public CacheableKey getKey() {
		// TODO Auto-generated method stub
		return null;
	}
	
}