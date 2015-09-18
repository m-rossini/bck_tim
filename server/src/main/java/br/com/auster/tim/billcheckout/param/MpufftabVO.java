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
 * Created on 11/10/2007
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
public class MpufftabVO extends CustomizableEntity implements CacheableVO {

	/**
	 *
	 */

	// ---------------------------
	// atributes
	// ---------------------------
	private static final long serialVersionUID = 1L;
	private String contractNumber;
	private String promotionCode;
	private double priceValue;
	private String scalefactorStr;
	private double scalefactorDbl;
	private String snCode;
	private String znCode;
	private String ffCode;
	private double userLimit;
	private String limitedFlag;
	private String description;
	private int umcode;

	// generated keys
	private CacheableKey  naturalKey;

	/**
	 *
	 */

	// ---------------------------
	// Constructors
	// ---------------------------
	public MpufftabVO() {
		this(0);
	}

	/**
	 * @param uid
	 */
	public MpufftabVO(long _uid) {
		setUid(_uid);
	}

	// ---------------------------
	// Accessors
	// ---------------------------

	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	public double getPriceValue() {
		return priceValue;
	}

	public void setPriceValue(double priceValue) {
		this.priceValue = priceValue;
	}	

	public int getUmcode() {
		return umcode;
	}

	public void setUmcode(int umcode) {
		this.umcode = umcode;
	}

	public String getPromotionCode() {
		return promotionCode;
	}

	public void setPromotionCode(String promotionCode) {
		this.promotionCode = promotionCode;
	}

	public double getScalefactorDbl() {
		return scalefactorDbl;
	}

	public void setScalefactorDbl(double scalefactorDbl) {
		this.scalefactorDbl = scalefactorDbl;
	}

	public String getScalefactorStr() {
		return scalefactorStr;
	}

	public void setScalefactorStr(String scalefactorStr) {
		this.scalefactorStr = scalefactorStr;
	}

	public String getSnCode() {
		return snCode;
	}

	public void setSnCode(String snCode) {
		this.snCode = snCode;
	}

	public String getZnCode() {
		return znCode;
	}

	public void setZnCode(String znCode) {
		this.znCode = znCode;
	}

	public String getFfCode() {
		return ffCode;
	}

	public void setFfCode(String ffCode) {
		this.ffCode = ffCode;
	}

	public String getLimitedFlag() {
		return limitedFlag;
	}

	public void setLimitedFlag(String limitedFlag) {
		this.limitedFlag = limitedFlag;
	}

	public double getUserLimit() {
		return userLimit;
	}

	public void setUserLimit(double userLimit) {
		this.userLimit = userLimit;
	}

	public final String getDescription() {
		return description;
	}

	public final void setDescription(String description) {
		this.description = description;
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
			hashcode = this.promotionCode.hashCode();
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
		return null;
	}

	// ---------------------------
	// Helper methods
	// ---------------------------

	public static final CacheableKey createAlternateKey( String contractNumber, String promotionCode ) {
		return new InnerAlternateKey( contractNumber, promotionCode );
	}



}
