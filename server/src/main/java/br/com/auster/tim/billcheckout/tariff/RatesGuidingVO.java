/*
 * Copyright (c) 2004-2010 Auster Solutions. All Rights Reserved.
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
 * Created on 18/03/2010
 */
package br.com.auster.tim.billcheckout.tariff;

import java.io.Serializable;
import java.util.Date;

/**
 * @author anardo
 * 18/03/2010
 */
public class RatesGuidingVO implements Serializable {

	private static final long serialVersionUID = -6989952419571357668L;
	
	private long tariffZoneUid;
	private long planUid;
	private long serviceUid;
	private long rateTimezoneUid;
	private String incomingFlag;
	private int initVolume;
	private int originalEndVolume;
	private double endVolume;
	private int stepVolume;
	private double stepCost;
	private Date effectiveDate;
	private String tariffZoneCode; //b.SHORT_DESC
	private String planName;
	private String state;
	private String serviceCode; //d.SHORT_DESC
	private String rateTimezoneCode; //e.BILL_FILE_CODE

	public long getTariffZoneUid() {
		return tariffZoneUid;
	}

	public void setTariffZoneUid(long tariffZoneUid) {
		this.tariffZoneUid = tariffZoneUid;
	}

	public long getPlanUid() {
		return planUid;
	}

	public void setPlanUid(long planUid) {
		this.planUid = planUid;
	}

	public long getServiceUid() {
		return serviceUid;
	}

	public void setServiceUid(long svcUid) {
		this.serviceUid = svcUid;
	}

	public long getRateTimezoneUid() {
		return rateTimezoneUid;
	}

	public void setRateTimezoneUid(long rateTimezoneUid) {
		this.rateTimezoneUid = rateTimezoneUid;
	}

	public String getIncomingFlag() {
		return incomingFlag;
	}

	public void setIncomingFlag(String incomingFlag) {
		this.incomingFlag = incomingFlag;
	}

	public int getInitVolume() {
		return initVolume;
	}

	public void setInitVolume(int initVolume) {
		this.initVolume = initVolume;
	}

	public double getEndVolume() {
		return endVolume;
	}
	/**
	 * TODO: Valor de EndVolume(final do periodo), em segundos
	 */
	public void setEndVolume() {
		this.endVolume = (this.originalEndVolume * this.stepVolume) + this.initVolume;
	}

	public int getStepVolume() {
		return stepVolume;
	}

	public void setStepVolume(int stepVolume) {
		this.stepVolume = stepVolume;
	}

	public double getStepCost() {
		return stepCost;
	}

	public void setStepCost(double stepCost) {
		this.stepCost = stepCost;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getTariffZoneCode() {
		return tariffZoneCode;
	}

	public void setTariffZoneCode(String tariffZoneCode) {
		this.tariffZoneCode = tariffZoneCode;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getRateTimezoneCode() {
		return rateTimezoneCode;
	}

	public void setRateTimezoneCode(String rateTimezoneCode) {
		this.rateTimezoneCode = rateTimezoneCode;
	}
	public int getOriginalEndVolume() {
		return originalEndVolume;
	}
	public void setOriginalEndVolume(int originalEndVolume) {
		this.originalEndVolume = originalEndVolume;
	}
}
