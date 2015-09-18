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
 * Created on 19/05/2010
 */
package br.com.auster.tim.billcheckout.tariff.model;

import java.util.Date;


/**
 * @author anardo
 */
public class DataUsageTariff {

	private long tariffZoneUid;
	private String tariffZone;
	private long serviceUid;
	private String service;
	private Date effectiveDate;
	private String umcode;
	private long packageUid;
	private String packageName;
	private int range1;
	private double range1Amount;
	private int range2;
	private double range2Amount;
	private int range3;
	private double range3Amount;
	private int range4;
	private double range4Amount;
	private int range5;
	private double range5Amount;
	private double afterRange5Amount;
	private Date loadedDate;

	
	
	public DataUsageTariff(long tariffZoneUid, long serviceUid, long packageUid,
						   String tariffZone, String service, Date effectiveDate,
						   String umcode, String packageName, int range1,
						   double range1Amount, int range2, double range2Amount,
						   int range3, double range3Amount, int range4,
						   double range4Amount, int range5, double range5Amount, 
						   double afterRange5Amount, Date loadedDate) {
		this.tariffZoneUid = tariffZoneUid;
		this.serviceUid = serviceUid;
		this.packageUid = packageUid;
		this.tariffZone = tariffZone;
		this.service = service;
		this.effectiveDate = effectiveDate;
		this.umcode = umcode;
		this.packageName = packageName;
		this.range1 = range1;
		this.range1Amount = range1Amount;
		this.range2 = range2;
		this.range2Amount = range2Amount;
		this.range3 = range3;
		this.range3Amount = range3Amount;
		this.range4 = range4;
		this.range4Amount = range4Amount;
		this.range5 = range5;
		this.range5Amount = range5Amount;
		this.afterRange5Amount = afterRange5Amount;
		this.loadedDate = loadedDate;
	}
	
	public long getTariffZoneUid() {
		return tariffZoneUid;
	}
	public void setTariffZoneUid(long tariffZoneUid) {
		this.tariffZoneUid = tariffZoneUid;
	}
	public long getServiceUid() {
		return serviceUid;
	}
	public void setServiceUid(long serviceUid) {
		this.serviceUid = serviceUid;
	}
	public long getPackageUid() {
		return packageUid;
	}
	public void setPackageUid(long packageUid) {
		this.packageUid = packageUid;
	}
	public final String getPackageName() {
		return packageName;
	}
	public final void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public final String getTariffZone() {
		return tariffZone;
	}
	public final void setTariffZone(String tariffZone) {
		this.tariffZone = tariffZone;
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
	public final String getUmcode() {
		return umcode;
	}
	public final void setUmcode(String umcode) {
		this.umcode = umcode;
	}
	public final Date getLoadedDate() {
		return loadedDate;
	}
	public final void setLoadedDate(Date loadedDate) {
		this.loadedDate = loadedDate;
	}
	public final int getRange1() {
		return range1;
	}
	public final void setRange1(int range1) {
		this.range1 = range1;
	}
	public final double getRange1Amount() {
		return range1Amount;
	}
	public final void setRange1Amount(double range1Amount) {
		this.range1Amount = range1Amount;
	}
	public final int getRange2() {
		return range2;
	}
	public final void setRange2(int range2) {
		this.range2 = range2;
	}
	public final double getRange2Amount() {
		return range2Amount;
	}
	public final void setRange2Amount(double range2Amount) {
		this.range2Amount = range2Amount;
	}
	public final int getRange3() {
		return range3;
	}
	public final void setRange3(int range3) {
		this.range3 = range3;
	}
	public final double getRange3Amount() {
		return range3Amount;
	}
	public final void setRange3Amount(double range3Amount) {
		this.range3Amount = range3Amount;
	}
	public final int getRange4() {
		return range4;
	}
	public final void setRange4(int range4) {
		this.range4 = range4;
	}
	public final double getRange4Amount() {
		return range4Amount;
	}
	public final void setRange4Amount(double range4Amount) {
		this.range4Amount = range4Amount;
	}
	public final int getRange5() {
		return range5;
	}
	public final void setRange5(int range5) {
		this.range5 = range5;
	}
	public final double getRange5Amount() {
		return range5Amount;
	}
	public final void setRange5Amount(double range5Amount) {
		this.range5Amount = range5Amount;
	}
	public final double getAfterRange5Amount() {
		return afterRange5Amount;
	}
	public final void setAfterRange5Amount(double afterRange5Amount) {
		this.afterRange5Amount = afterRange5Amount;
	}
}
