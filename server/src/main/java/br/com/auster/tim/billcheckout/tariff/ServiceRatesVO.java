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
package br.com.auster.tim.billcheckout.tariff;

import java.io.Serializable;
import java.util.Date;

/**
 * TODO What this class is responsible for
 *
 * @author William Soares
 * @version $Id$
 * @since JDK1.4
 */
public class ServiceRatesVO implements Serializable {

	

	// ---------------------------
	// Atributes
	// ---------------------------
	private static final long serialVersionUID = 1L;

	private long planUid;
	private String planName;
	private String state;
	private long serviceUid;
	private String snCode;
	private Date effectiveDate;
	private double access;
	private double onetime;
	private double subscription;
	
	

	// ---------------------------
	// Constructors
	// ---------------------------
	public ServiceRatesVO() {
	}


	
	// ---------------------------
	// Accessors
	// ---------------------------


	public long getPlanUid() { return planUid; }
	public void setPlanUid(long planUid) { this.planUid = planUid; }

	public String getPlanName() { return this.planName; }
	public void setPlanName(String _plan) { this.planName = _plan; }

	public String getState() { return this.state; }
	public void setState(String _state) { this.state = _state; }

	public Date getEffectiveDate() { return effectiveDate; }
	public void setEffectiveDate(Date effectiveDate) { this.effectiveDate = effectiveDate; }

	public double getAccessFee() { return access; }
	public void setAccessFee(double access) { this.access = access; }

	public double getSubscriptionFee() { return subscription; }
	public void setSubscriptionFee(double subscription) { this.subscription = subscription; }

	public double getOnetimeRate() { return onetime; }
	public void setOnetimeRate(double onetime) { this.onetime = onetime; }

	public long getServiceUid() { return serviceUid; }
	public void setServiceUid(long uid) { this.serviceUid = uid; }

	public String getSnCode() { return snCode; }
	public void setSnCode(String snCode) { this.snCode = snCode; }

	
	
}
