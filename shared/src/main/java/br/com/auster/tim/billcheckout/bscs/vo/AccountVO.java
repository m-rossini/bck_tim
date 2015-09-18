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
 * Created on 26/11/2007
 */
package br.com.auster.tim.billcheckout.bscs.vo;

import java.util.List;
import java.util.Map;

/**
 * TODO What this class is responsible for
 *
 * @author William Soares
 * @version $Id$
 * @since JDK1.4
 */
public class AccountVO {
	
	private String accountNumber;	
	
	private GeneralInfoVO generalInfo;
	
	private List occList;
	
	private List contractVOList;
	
	private Map hierarchyStructure;

	
	public GeneralInfoVO getGeneralInfo() {
		return generalInfo;
	}

	public void setGeneralInfo(GeneralInfoVO generalInfo) {
		this.generalInfo = generalInfo;
	}

	public Map getHierarchyStructure() {
		return hierarchyStructure;
	}

	public void setHierarchyStructure(Map hierarchyStructure) {
		this.hierarchyStructure = hierarchyStructure;
	}

	public List getContractVOList() {
		return contractVOList;
	}

	public void setContractVOList(List contractVOList) {
		this.contractVOList = contractVOList;
	}

	public List getOccList() {
		return occList;
	}

	public void setOccList(List occList) {
		this.occList = occList;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	
}