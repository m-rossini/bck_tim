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
 * Created on 23/04/2008
 */
package br.com.auster.tim.billcheckout.param;

import java.util.Date;
import java.util.Iterator;

/**
 * TODO What this class is responsible for
 *
 * @author William Soares
 * @version $Id$
 * @since JDK1.4
 */
public class PromotionExclusivityByContractVO {

	private String contractNumber;
	private Date activationDate;
	private Date expirationDate;
	private PromotionExclusivityVO promotionExclusivityVO;
	
	
	public PromotionExclusivityByContractVO(String contractNumber, PromotionExclusivityVO promotionExclusivityVO) {
		this.contractNumber = contractNumber;
		this.promotionExclusivityVO = promotionExclusivityVO;
	}
	
	public String getContractNumber() {
		return contractNumber;
	}
	
	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}
	
	public String getShortDescription() {
		return this.promotionExclusivityVO.getShortDescription();
	}
	
	public String getDescription() {
		return this.promotionExclusivityVO.getDescription();
	}
	
	public PromotionExclusivityVO getPromotionExclusivityVO() {
		return promotionExclusivityVO;
	}
	
	public void setPromotionExclusivityVO(
			PromotionExclusivityVO promotionExclusivityVO) {
		this.promotionExclusivityVO = promotionExclusivityVO;
	}

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
	
	public boolean isExclusiveWith(PromotionExclusivityByContractVO obj) {		
		for (Iterator it = this.promotionExclusivityVO.getRelatedPromotions().iterator(); it.hasNext();) {
			PromotionExclusivityVO objToCompare = (PromotionExclusivityVO) it.next();
			if (objToCompare.equals(obj)) {
				return true;
			}
		}
		return false;
	}
	
}
