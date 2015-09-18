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
 * Created on 26/10/2007
 */
package br.com.auster.tim.billcheckout.param;

import java.util.Date;

/**
 * TODO What this class is responsible for
 *
 * @author William Soares
 * @version $Id$
 * @since JDK1.4
 */
public class ServicesByContractVO extends ServicesVO {

	
	/** 
	 * Used to store the values of  <code>serialVersionUID</code>.
	 */
	private static final long serialVersionUID = 1L;
	
	private Date activationDate;
	
	private Date expirationDate;
	private double subscriptionAmount;
	private String contractNumber;
	

	public ServicesByContractVO(ServicesVO servicesVO) {
		this(servicesVO.getDescription(), servicesVO.getShortDesc(), servicesVO.getSvcCode(), servicesVO.getUid());
	}
	
	public ServicesByContractVO(String _desc, String _shdes, long _sncode, long _uid) {
		setDescription(_desc);
		setShortDesc(_shdes);
		setSvcCode(_sncode);
		setUid(_uid);
	}
	
	public String getContractNumber() { return contractNumber; }
	public void setContractNumber(String contractNumber) { this.contractNumber = contractNumber; }
	
	public Date getActivationDate() { return activationDate; }
	public void setActivationDate(Date activationDate) { this.activationDate = activationDate; }

	public Date getExpirationDate() { return expirationDate; }
	public void setExpirationDate(Date expirationDate) { this.expirationDate = expirationDate; }

	public double getSubscriptionAmount() { return this.subscriptionAmount; }
	public void setSubscriptionAmount(double _amt) { this.subscriptionAmount = _amt; }
}