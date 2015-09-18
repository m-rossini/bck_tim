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
 * Created on 18/05/2010
 */
package br.com.auster.tim.billcheckout.tariff.model;

import java.util.Date;

/**
 * @author anardo
 */
public class VoiceUsageTariff {

	private long tariffZoneUid;
	private String tariffZone;
	private long rateTimeZoneUid;
	private String rateTimeZone;
	private long serviceUid;
	private String service;
	private Date effectiveDate;
	private boolean incomingFlag;
	private int initVolume;
	private int endVolume;
	private int stepVolume;
	private double amount;
	private Date loadedDate;
	
	public VoiceUsageTariff(long tariffZoneUid, long serviceUid, long rateTimeZoneUid, String tariffZone, 
							String rateTimeZone, String service, Date effectiveDate, boolean incomingFlag,
							int initVolume, int endVolume, int stepVolume, double amount, Date loadedDate) {
		this.tariffZoneUid = tariffZoneUid;
		this.rateTimeZoneUid = rateTimeZoneUid;
		this.serviceUid = serviceUid;
		this.tariffZone = tariffZone;
		this.rateTimeZone = rateTimeZone;
		this.service = service;
		this.effectiveDate = effectiveDate;
		this.incomingFlag = incomingFlag;
		this.initVolume = initVolume;
		this.endVolume = endVolume;
		this.stepVolume = stepVolume;
		this.amount = amount;
		this.loadedDate = loadedDate;
	}
	
	public long getTariffZoneUid() {
		return tariffZoneUid;
	}
	public void setTariffZoneUid(long tariffZoneUid) {
		this.tariffZoneUid = tariffZoneUid;
	}
	public long getRateTimeZoneUid() {
		return rateTimeZoneUid;
	}
	public void setRateTimeZoneUid(long rateTimeZoneUid) {
		this.rateTimeZoneUid = rateTimeZoneUid;
	}
	public long getServiceUid() {
		return serviceUid;
	}
	public void setServiceUid(long serviceUid) {
		this.serviceUid = serviceUid;
	}
	public final String getTariffZone() {
		return tariffZone;
	}
	public final void setTariffZone(String tariffZone) {
		this.tariffZone = tariffZone;
	}
	public final String getRateTimeZone() {
		return rateTimeZone;
	}
	public final void setRateTimeZone(String rateTimeZone) {
		this.rateTimeZone = rateTimeZone;
	}
	public final String getService() {
		return service;
	}
	public final void setService(String service) {
		this.service = service;
	}
	public final Date getEffectiveDate() {
		return effectiveDate;
	}
	public final void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public final boolean isIncomingFlag() {
		return incomingFlag;
	}
	public final void setIncomingFlag(boolean incomingFlag) {
		this.incomingFlag = incomingFlag;
	}
	public final int getInitVolume() {
		return initVolume;
	}
	public final void setInitVolume(int initVolume) {
		this.initVolume = initVolume;
	}
	public final int getEndVolume() {
		return endVolume;
	}
	public final void setEndVolume(int endVolume) {
		this.endVolume = endVolume;
	}
	public final int getStepVolume() {
		return stepVolume;
	}
	public final void setStepVolume(int stepVolume) {
		this.stepVolume = stepVolume;
	}
	public final double getAmount() {
		return amount;
	}
	public final void setAmount(double amount) {
		this.amount = amount;
	}
	public final Date getLoadedDate() {
		return loadedDate;
	}
	public final void setLoadedDate(Date loadedDate) {
		this.loadedDate = loadedDate;
	}
}
