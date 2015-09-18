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
 * Created on 26/03/2008
 */
package br.com.auster.tim.billcheckout.util;

import java.io.Serializable;
import java.util.Date;

import br.com.auster.om.util.UnitCounter;

/**
 * This class will handle all FreeUnit accumulated information, which are:
 *
 * accountID - FU account ID
 * periodStart - start date for cycle
 * periodEnd - end date for cycle
 * shortDescription - short description for the package
 * type - the type of events this account handles
 * volume - sum of all reductionVolume
 * minDatetime - lower date/time for usage that this accountID reducted
 * maxDatetime - greater date/time for usage that this accountID reducted
 *
 * @author framos
 * @version $Id$
 *
 */
public class FreeUnitAccumulator implements Serializable {



	private static final long serialVersionUID = 1L;



	private String accountId;
	private String custCode;
	private Date periodStart;
	private Date periodEnd;

	// if null then its a shared package
	private String contractId;

	private String shortDescription;
	private String description;
	private String type;
	private Date minDatetime;
	private Date maxDatetime;
	private UnitCounter volume;


	public FreeUnitAccumulator(String _accountId, Date _periodStart, String _custCode) {
		this(_accountId, _periodStart, _custCode, null);
	}

	public FreeUnitAccumulator(String _accountId, Date _periodStart, String _custCode, Date _periodEnd) {
		this.accountId = _accountId;
		this.periodStart = _periodStart;
		this.custCode = _custCode;
		this.periodEnd = _periodEnd;
	}


	public final String getAccountId() { return this.accountId; }
	public final String getCustCode() { return this.custCode; }
	public final Date getPeriodStart() { return this.periodStart; }

	public final Date getPeriodEnd() { return this.periodEnd; }
	public final void setPeriodEnd(Date _date) { this.periodEnd = _date; }

	public final Date getMinDatetime() { return this.minDatetime; }
	public final void setMinDatetime(Date _date) { this.minDatetime = _date; }
	public final Date getMaxDatetime() { return this.maxDatetime; }
	public final void setMaxDatetime(Date _date) { this.maxDatetime = _date; }

	public final String getShortDescription() { return this.shortDescription; }
	public final void setShortDescription(String _shdes) { this.shortDescription = _shdes; }
	public final String getFullDescription() { return this.description; }
	public final void setFullDescription(String _desc) { this.description = _desc; }

	public final String getTypeOfUsage() { return this.type; }
	public final void setTypeOfUsage(String _type) { this.type = _type; }

	public final UnitCounter getTotalVolume() { return this.volume; }
	public final void setTotalVolume(UnitCounter _volume) { this.volume = _volume; }
	public final void addTotalVolume(UnitCounter _volume) {
		if (this.volume != null) {
			this.volume.addUnits(_volume.getUnits());
		} else {
			this.setTotalVolume(_volume);
		}
	}

	public final void setContractId(String _contractId) { this.contractId = _contractId; }
	public final String getContractId() { return this.contractId; }


	public String toString() {
		StringBuffer bf = new StringBuffer();
		bf.append("[ accId=");
		bf.append(this.accountId);
		bf.append("/ startDt=");
		bf.append(this.periodStart);
		bf.append("] = { endDt = ");
		bf.append(this.periodEnd);
		bf.append(", maxDatetime = ");
		bf.append(this.maxDatetime);
		bf.append(", minDatetime = ");
		bf.append(this.minDatetime);
		bf.append(", shDes = ");
		bf.append(this.shortDescription);
		bf.append(", fullDes = ");
		bf.append(this.description);
		bf.append(", type = ");
		bf.append(this.type);
		bf.append(", volume = ");
		bf.append(this.volume);
		bf.append(" }");
		return bf.toString();
	}

	public int hashCode() {
		int hash = this.accountId.hashCode();
		hash += this.periodStart.hashCode();
		return hash;
	}

	public boolean equals(Object _other) {
		try {
			FreeUnitAccumulator other = (FreeUnitAccumulator) _other;
			boolean result = this.accountId.equals(other.accountId);
			result = result && (this.periodStart.equals(other.periodStart));
			return result;
		} catch (ClassCastException cce) {
			return false;
		}
	}

}
