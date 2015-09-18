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
 * Created on 14/11/2007
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
public class ContractVO {

	private String contractNumber;
	
	private String subscriberName;
	
	private String accessNumber;
	
	private GeneralInfoVO generalInfo;
	
	private List packageList;	
	
	private List activeServiceList;
	
	private Map hierarchyStructure;
	
	private List servicesHistory;


	public String getAccessNumber() {
		return accessNumber;
	}

	public void setAccessNumber(String accessNumber) {
		this.accessNumber = accessNumber;
	}

	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	public String getSubscriberName() {
		return subscriberName;
	}

	public void setSubscriberName(String subscriberName) {
		this.subscriberName = subscriberName;
	}

	public List getActiveServiceList() {
		return activeServiceList;
	}

	public void setActiveServiceList(List activeServiceList) {
		this.activeServiceList = activeServiceList;
	}

	public GeneralInfoVO getGeneralInfo() {
		return generalInfo;
	}

	public void setGeneralInfo(GeneralInfoVO generalInfo) {
		this.generalInfo = generalInfo;
	}

	public List getPackageList() {
		return packageList;
	}

	public void setPackageList(List packageList) {
		this.packageList = packageList;
	}

	public Map getHierarchyStructure() {
		return hierarchyStructure;
	}

	public void setHierarchyStructure(Map hierarchyStructure) {
		this.hierarchyStructure = hierarchyStructure;
	}

	public List getServicesHistory() {
		return servicesHistory;
	}

	public void setServicesHistory(List servicesHistory) {
		this.servicesHistory = servicesHistory;
	}
	
}
