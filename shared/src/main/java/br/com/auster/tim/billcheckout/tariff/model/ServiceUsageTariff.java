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
 * Created on 30/04/2010
 */
package br.com.auster.tim.billcheckout.tariff.model;

import java.util.Date;

/**
 * @author anardo
 */
public class ServiceUsageTariff {
	
	private long   uid; //pacote ou serviço
	private String description; //pacote ou serviço
	private String shortDesc;
	private String planDescr;
	private double accessFee;
	private double oneTime;
	private double subscrRate;
	private Date   loadedDate;
	private Date   effectiveDate;
	
	public ServiceUsageTariff(long serviceUid,String shortDesc, String serviceDescr, String planDescr, 
							  double accessFee, double oneTime, double subscrRate, Date effectiveDate,
							  Date loadedDate) {
		this.uid = serviceUid;
		this.description = serviceDescr;
		this.shortDesc = shortDesc;
		this.planDescr = planDescr;
		this.accessFee = accessFee;
		this.oneTime = oneTime;
		this.subscrRate = subscrRate;
		this.effectiveDate = effectiveDate;
		this.loadedDate = loadedDate;
	}
	public ServiceUsageTariff(long packageUid, String packageDescr, String planDescr, double accessFee,
							  double subscrRate, Date loadedDate, Date effectiveDate) {
		this.uid = packageUid;
		this.description = packageDescr;
		this.planDescr = planDescr;
		this.accessFee = accessFee;
		this.subscrRate = subscrRate;
		this.loadedDate = loadedDate;
		this.effectiveDate = effectiveDate;
	}
	
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getShortDesc() {
		return shortDesc;
	}
	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}
	public String getPlanDescr() {
		return planDescr;
	}
	public void setPlanDescr(String planDescr) {
		this.planDescr = planDescr;
	}
	public double getAccessFee() {
		return accessFee;
	}
	public void setAccessFee(double accessFee) {
		this.accessFee = accessFee;
	}
	public double getOneTime() {
		return oneTime;
	}
	public void setOneTime(double oneTime) {
		this.oneTime = oneTime;
	}
	public double getSubscrRate() {
		return subscrRate;
	}
	public void setSubscrRate(double subscrRate) {
		this.subscrRate = subscrRate;
	}
	public Date getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public Date getLoadedDate() {
		return loadedDate;
	}
	public void setLoadedDate(Date loadedDate) {
		this.loadedDate = loadedDate;
	}
	
}
