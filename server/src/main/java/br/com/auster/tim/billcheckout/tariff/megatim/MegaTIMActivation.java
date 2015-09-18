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
 * Created on 26/03/2010
 */
package br.com.auster.tim.billcheckout.tariff.megatim;

import java.util.Calendar;
import java.util.Date;

import br.com.auster.tim.billcheckout.tariff.MegaTIMVO;

/**
 * TODO What this class is responsible for
 *
 * @author Frederico Ramos
 * @version $Id$
 * @since JDK1.4
 */
public class MegaTIMActivation {

	private MegaTIMVO vo;
	private String fuAccountId;
	private String contractId;
	private Date activationDate;
	
	
	public MegaTIMActivation(String _fuAccountId, String _contractId, Date _activationDate) {
		this.fuAccountId = _fuAccountId;
		this.contractId = _contractId;
		this.activationDate = _activationDate;
	}


	public final MegaTIMVO getMegaTIMVO() { return vo; }
	public final void setMegaTIMVO(MegaTIMVO _vo) { this.vo = _vo; }

	public final String getFuAccountId() { return fuAccountId; }
	public final void setFuAccountId(String fuAccountId) { this.fuAccountId = fuAccountId; }

	public final String getContractId() { return contractId; }
	public final void setContractId(String contractId) { this.contractId = contractId; }

	public final Date getActivationDate() { return activationDate; }
	public final void setActivationDate(Date activationDate) { this.activationDate = activationDate; }
	
	public final Date getActivationExpirationDate() {		
		Calendar c = Calendar.getInstance();
		c.setTime(this.activationDate);
		c.add(Calendar.DAY_OF_YEAR, vo.getValidPeriod());
		return c.getTime();
		
	}
}
