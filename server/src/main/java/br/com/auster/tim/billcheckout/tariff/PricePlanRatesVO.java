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
 * Created on 19/04/2010
 */
package br.com.auster.tim.billcheckout.tariff;

import java.io.Serializable;
import java.util.Date;

/**
 * @author anardo
 *
 */
public class PricePlanRatesVO implements Serializable{

	private static final long serialVersionUID = -4703805833235548083L;
	
	private long planUid;
	private long packageUid;
	private long serviceUid;
	private long tariffZoneUid;
	private Date effectiveDate;
	private String umCode;
	private long endRange1;
	private double amtRange1;
	private long endRange2;
	private double amtRange2;
	private long endRange3;
	private double amtRange3;
	private long endRange4;
	private double amtRange4;
	private long endRange5;
	private double amtRange5;
	private double amtRangeMax;
	
	/**
	 * @return the planUid
	 */
	public long getPlanUid() {
		return planUid;
	}
	/**
	 * @param planUid the planUid to set
	 */
	public void setPlanUid(long planUid) {
		this.planUid = planUid;
	}
	/**
	 * @return the packageUid
	 */
	public long getPackageUid() {
		return packageUid;
	}
	/**
	 * @param packageUid the packageUid to set
	 */
	public void setPackageUid(long packageUid) {
		this.packageUid = packageUid;
	}
	/**
	 * @return the serviceUid
	 */
	public long getServiceUid() {
		return serviceUid;
	}
	/**
	 * @param serviceUid the serviceUid to set
	 */
	public void setServiceUid(long serviceUid) {
		this.serviceUid = serviceUid;
	}
	/**
	 * @return the tariffZoneUid
	 */
	public long getTariffZoneUid() {
		return tariffZoneUid;
	}
	/**
	 * @param tariffZoneUid the tariffZoneUid to set
	 */
	public void setTariffZoneUid(long tariffZoneUid) {
		this.tariffZoneUid = tariffZoneUid;
	}
	/**
	 * @return the effectiveDate
	 */
	public Date getEffectiveDate() {
		return effectiveDate;
	}
	/**
	 * @param effectiveDate the effectiveDate to set
	 */
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	/**
	 * @return the umCode
	 */
	public String getUmCode() {
		return umCode;
	}
	/**
	 * @param umCode the umCode to set
	 */
	public void setUmCode(String umCode) {
		this.umCode = umCode;
	}
	/**
	 * @return the endRange1
	 */
	public long getEndRange1() {
		return endRange1;
	}
	/**
	 * @param endRange1 the endRange1 to set
	 */
	public void setEndRange1(long endRange1) {
		this.endRange1 = endRange1;
	}
	/**
	 * @return the amtRange1
	 */
	public double getAmtRange1() {
		return amtRange1;
	}
	/**
	 * @param amtRange1 the amtRange1 to set
	 */
	public void setAmtRange1(double amtRange1) {
		this.amtRange1 = amtRange1;
	}
	/**
	 * @return the endRange2
	 */
	public long getEndRange2() {
		return endRange2;
	}
	/**
	 * @param endRange2 the endRange2 to set
	 */
	public void setEndRange2(long endRange2) {
		this.endRange2 = endRange2;
	}
	/**
	 * @return the amtRange2
	 */
	public double getAmtRange2() {
		return amtRange2;
	}
	/**
	 * @param amtRange2 the amtRange2 to set
	 */
	public void setAmtRange2(double amtRange2) {
		this.amtRange2 = amtRange2;
	}
	/**
	 * @return the endRange3
	 */
	public long getEndRange3() {
		return endRange3;
	}
	/**
	 * @param endRange3 the endRange3 to set
	 */
	public void setEndRange3(long endRange3) {
		this.endRange3 = endRange3;
	}
	/**
	 * @return the amtRange3
	 */
	public double getAmtRange3() {
		return amtRange3;
	}
	/**
	 * @param amtRange3 the amtRange3 to set
	 */
	public void setAmtRange3(double amtRange3) {
		this.amtRange3 = amtRange3;
	}
	/**
	 * @return the endRange4
	 */
	public long getEndRange4() {
		return endRange4;
	}
	/**
	 * @param endRange4 the endRange4 to set
	 */
	public void setEndRange4(long endRange4) {
		this.endRange4 = endRange4;
	}
	/**
	 * @return the amtRange4
	 */
	public double getAmtRange4() {
		return amtRange4;
	}
	/**
	 * @param amtRange4 the amtRange4 to set
	 */
	public void setAmtRange4(double amtRange4) {
		this.amtRange4 = amtRange4;
	}
	/**
	 * @return the endRange5
	 */
	public long getEndRange5() {
		return endRange5;
	}
	/**
	 * @param endRange5 the endRange5 to set
	 */
	public void setEndRange5(long endRange5) {
		this.endRange5 = endRange5;
	}
	/**
	 * @return the amtRange5
	 */
	public double getAmtRange5() {
		return amtRange5;
	}
	/**
	 * @param amtRange5 the amtRange5 to set
	 */
	public void setAmtRange5(double amtRange5) {
		this.amtRange5 = amtRange5;
	}
	/**
	 * @return the amtRangeMax
	 */
	public double getAmtRangeMax() {
		return amtRangeMax;
	}
	/**
	 * @param amtRangeMax the amtRangeMax to set
	 */
	public void setAmtRangeMax(double amtRangeMax) {
		this.amtRangeMax = amtRangeMax;
	}
}
