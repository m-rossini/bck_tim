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
 * @author framos
 */
public class NPackTariff {
	
	private long planUid;
	private int initRange;
	private int endRange;
	private Date validFrom;
	private double accessFee;
	
	
	public NPackTariff(long _planUid, int _initRange, int _endRange, Date effectiveDate, double accessfee) {
		this.planUid = _planUid;
		this.initRange = _initRange;
		this.endRange = _endRange;
		this.validFrom = effectiveDate;
		this.accessFee = accessfee;
	}
	
	public long getPlanUid() { return planUid; }
	public void setPlanUid(long _planUid) { this.planUid = _planUid; }
	
	public int getInitRange() { return this.initRange; }
	public void setInitRange(int _initRange) { this.initRange = _initRange; }
	
	public int getEndRange() { return this.endRange; }
	public void setEndRange(int _endRange) { this.endRange = _endRange; }
	
	public final Date getValidFrom() { return validFrom; }
	public final void setValidFrom(Date effectiveDate) { this.validFrom = effectiveDate; }
	
	public final double getAccessFee() { return accessFee; }
	public final void setAccessFee(double accessFee) { this.accessFee = accessFee; }
	
}
