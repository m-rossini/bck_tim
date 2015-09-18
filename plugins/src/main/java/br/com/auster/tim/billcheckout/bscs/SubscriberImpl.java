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
 * Created on 19/11/2007
 */
package br.com.auster.tim.billcheckout.bscs;

import java.io.ByteArrayInputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import br.com.auster.common.text.DateFormat;
import br.com.auster.common.xml.DOMUtils;
import br.com.auster.dware.console.error.ErrorMessageException;
import br.com.auster.facelift.services.ConfigurationException;
import br.com.auster.tim.billcheckout.bscs.infobus.AccountStructureSyncIB;
import br.com.auster.tim.billcheckout.bscs.infobus.ClientInfoSyncIB;
import br.com.auster.tim.billcheckout.bscs.infobus.ContractInfoSyncIB;
import br.com.auster.tim.billcheckout.bscs.infobus.PackageListSyncIB;
import br.com.auster.tim.billcheckout.bscs.vo.ContractVO;
import br.com.auster.tim.billcheckout.bscs.vo.GeneralInfoVO;
import br.com.auster.tim.billcheckout.bscs.vo.PackageVO;
import br.com.auster.tim.billcheckout.bscs.vo.ServiceVO;

/**
 * TODO What this class is responsible for
 *
 * @author William Soares
 * @version $Id$
 * @since JDK1.4
 */
public class SubscriberImpl implements Subscriber {

	private static final Logger log = Logger.getLogger(SubscriberImpl.class);
	
	private static final char ACTIVATE_STATE = 'a';
	private static final char SUSPENSE_STATE = 's';
	private static final char DEACTIVATE_STATE = 'd';
	
	private String infobusUrl; 
	
	
	public ContractVO getContractVO(String parameterName, String parameterValue) {
		
		String xmlResponse;		
		
		ContractVO contractVO = new ContractVO();		
					
		//first get the generalInfo, customer id and services from infobus 
		xmlResponse = ContractInfoSyncIB.getInstance().process(parameterValue, this.infobusUrl);
		contractVO.setContractNumber(parameterValue);
		contractVO.setGeneralInfo(getGeneralInfo(xmlResponse));		
		List servicesList = getServicesHistory(xmlResponse);
		String customerId = getCustomerId(xmlResponse);
		contractVO.setServicesHistory(servicesList);
		contractVO.setActiveServiceList(getActiveServiceList(servicesList));
		//then get the packages list 
		xmlResponse = PackageListSyncIB.getInstance().process(parameterValue, this.infobusUrl);
		contractVO.setPackageList(getPackageList(xmlResponse));		
		//now get the custcode and then get the account structure for that custcode
		xmlResponse = ClientInfoSyncIB.getInstance().process(customerId, this.infobusUrl);
		xmlResponse = AccountStructureSyncIB.getInstance().process(getCustCode(xmlResponse), this.infobusUrl);
		contractVO.setHierarchyStructure(getAccountStructure(xmlResponse));

		return contractVO;
		
	}

	/**
	 * Returns the general info from the xml response.
	 * 
	 * @param xmlResponse
	 * @return generalInfo
	 * @throws InfoBusException 
	 * @see br.com.auster.tim.billcheckout.bscs.Subscriber#getGeneralInfo(java.lang.String)
	 */
	private GeneralInfoVO getGeneralInfo(String xmlResponse) {
		
		GeneralInfoVO generalInfo = new GeneralInfoVO();
		Element root;
		
		try {	
			root = DOMUtils.openDocument(new ByteArrayInputStream(xmlResponse.getBytes()));
			Element data = DOMUtils.getElement(root, "data", true);
			
			//sets the subscriber name
			Element customerInfo = DOMUtils.getElement(data, "customer", true);
			customerInfo = DOMUtils.getElement(customerInfo, "address", true);
			customerInfo = DOMUtils.getElement(customerInfo, "contact-name", true);			
			generalInfo.setSubscriberName(DOMUtils.getText(customerInfo).toString());
			
			//sets the contract status
			Element contract = DOMUtils.getElement(data, "contract", true);
			Element contractSubElement = DOMUtils.getElement(contract, "status", true);
			generalInfo.setStatus(formatStatus(DOMUtils.getText(contractSubElement).toString()));
			
			//sets the activation date
			contractSubElement = DOMUtils.getElement(contract, "activation-date", true); 	
			generalInfo.setActivactionDate(DOMUtils.getText(contractSubElement).toString());
			
			//sets the access number
			contractSubElement = DOMUtils.getElement(contract, "msisdn", true); 	
			generalInfo.setAcessNumber(DOMUtils.getText(contractSubElement).toString());			
			
			//sets the plan name
			contractSubElement = DOMUtils.getElement(contract, "rate-plan", true);
			contractSubElement = DOMUtils.getElement(contractSubElement, "desc", true);
			generalInfo.setPlanName(DOMUtils.getText(contractSubElement).toString());
			
		} catch(Exception e) {
			log.error("Erro encontrado no XML de resposta do Infobus.", e);
			throw new ErrorMessageException("Erro encontrado no XML de resposta do Infobus.", e);
		}
		
		return generalInfo;		
		
	}	
	
	/**
	 * Returns the active services list info from the xml response.
	 * 
	 * @param xmlResponse
	 * @return  activeServiceList
	 */
	private List getActiveServiceList(List servicesList) {

		List activeServiceList = new ArrayList();
		
		ServiceVO activeService;		
		
		if (servicesList != null) {
			for (Iterator iterator = servicesList.iterator(); iterator.hasNext();) {
				activeService = (ServiceVO) iterator.next();
				if (activeService.getStatus() != null) {
					if (activeService.getStatus().trim().equalsIgnoreCase("Ativado")) {
						activeServiceList.add(activeService);
					}
				}
			}				
		}
		
		return activeServiceList;
		
	}	

	/**
	 * 
	 * Returns all the services list info from the xml response.
	 * 
	 * @param xmlResponse
	 * @return serviceList
	 * @throws InfoBusException 
	 */
	private List getServicesHistory(String xmlResponse) {

		List serviceList = new ArrayList();
		
		ServiceVO activeService;
		
		try {
			
			Element root;
			root = DOMUtils.openDocument(new ByteArrayInputStream(xmlResponse.getBytes()));
			Element element = DOMUtils.getElement(root, "data", true);
			element = DOMUtils.getElement(element, "contract", true);
			
			NodeList serviceNodeList = DOMUtils.getElements(element, "service");
			Element subElement;
			for (int i = 0; i < serviceNodeList.getLength(); i++) {
				element = (Element) serviceNodeList.item(i);
				activeService = new ServiceVO();
				//sets the sncode
				subElement = DOMUtils.getElement(element, "code", true);
				activeService.setSnCode(DOMUtils.getText(subElement).toString());
				//sets the description
				subElement = DOMUtils.getElement(element, "desc", true);
				activeService.setDescription(DOMUtils.getText(subElement).toString());
				//sets the service history
				subElement = DOMUtils.getElement(element, "contract-svc", true);
				subElement = DOMUtils.getElement(subElement, "status-hist", true);
				activeService.setServiceHistory(getServiceHistory(DOMUtils.getText(subElement).toString()));
				//sets the service activation
				activeService.setServiceActivation(getActivationDate(DOMUtils.getText(subElement).toString()));
				//sets the service status
				activeService.setStatus(getServiceCurrentStatus(DOMUtils.getText(subElement).toString()));
				
				serviceList.add(activeService);
			}
			
		} catch(Exception e) {
			log.error("Erro encontrado no XML de resposta do Infobus.", e);
			throw new ErrorMessageException("Erro encontrado no XML de resposta do Infobus.", e);
		}
		
		return serviceList;						
		
	}		
	
	/**
	 * Returns all the actives packages from the xml response.
	 * 
	 * @param parameter
	 * @return packageList 
	 * @throws InfoBusException 
	 */
	private List getPackageList(String xmlResponse) {

		List packageList = new ArrayList();
		PackageVO packageVO;
		Element root;
		
		try {
			root = DOMUtils.openDocument(new ByteArrayInputStream(xmlResponse.getBytes()));
			Element element = DOMUtils.getElement(root, "data", true);
			
			NodeList packageNodeList = DOMUtils.getElements(element, "fu-pack");
			Element subElement;

			for (int i = 0; i < packageNodeList.getLength(); i++) {
				element = (Element) packageNodeList.item(i);
				packageVO = new PackageVO();
				//sets the package name
				subElement = DOMUtils.getElement(element, "desc", true);
				packageVO.setName(DOMUtils.getText(subElement).toString());
				//sets the package activation date
				subElement = DOMUtils.getElement(element, "activation-date", true);
				if (DOMUtils.getText(subElement).toString() == null || DOMUtils.getText(subElement).toString().length() <= 0) { 
					packageVO.setActivationDate("");
				} else {
					packageVO.setActivationDate(DOMUtils.getText(subElement).toString());
				}
				
				packageList.add(packageVO);
			}
			
		} catch(Exception e) {
			log.error("Erro encontrado no XML de resposta do Infobus.", e);
			throw new ErrorMessageException("Erro encontrado no XML de resposta do Infobus.", e);
		}
		
		return packageList;		
		
	}	

	/**
	 * 
	 * Returns a map(Key=status, Value=date) containing the service history.
	 * 
	 * @param csStatChng
	 * @return serviceHistoryMap
	 * @throws InfoBusException 
	 */
	private Map getServiceHistory(String csStatChng) {
		
		Map serviceHistoryMap = new HashMap();		
		char serviceStatus;
		String serviceStatusDate;	

		if (csStatChng != null) {
			String[] serviceHistoryArray = csStatChng.split("[|]");		
			for (int i = 0; i < serviceHistoryArray.length; i++) {
				serviceStatusDate = serviceHistoryArray[i];
				serviceStatus = serviceStatusDate.charAt(serviceStatusDate.length()-1);
				try {
					if (serviceStatus == ACTIVATE_STATE){
						serviceHistoryMap.put("Ativado", DateFormat.format(serviceStatusDate.substring(0, serviceStatusDate.length()-1), "yymmdd", "dd/mm/yyyy"));
					} else if (serviceStatus == SUSPENSE_STATE) {
						serviceHistoryMap.put("Suspenso",  DateFormat.format(serviceStatusDate.substring(0, serviceStatusDate.length()-1), "yymmdd", "dd/mm/yyyy"));
					} else if (serviceStatus == DEACTIVATE_STATE) {
						serviceHistoryMap.put("Desativado",  DateFormat.format(serviceStatusDate.substring(0, serviceStatusDate.length()-1), "yymmdd", "dd/mm/yyyy"));
					} else {
						serviceHistoryMap.put("",  DateFormat.format(serviceStatusDate.substring(0, serviceStatusDate.length()-1), "yymmdd", "dd/mm/yyyy"));
					}
				} catch(ParseException e) {
					log.error("Erro encontrado no XML de resposta do Infobus.", e);
					throw new ErrorMessageException("Erro encontrado no XML de resposta do Infobus.", e);
				}
			}
		}		

		return serviceHistoryMap;
		
	}

	/**
	 * 
	 * Returns the customer id from the xmlResponse.
	 * 
	 * @param xmlResponse
	 * @return customerId
	 */
	private String getCustomerId(String xmlResponse) {
		
		String customerId = ""; 
		Element root;
		
		try {
			
			root = DOMUtils.openDocument(new ByteArrayInputStream(xmlResponse.getBytes()));
			Element data = DOMUtils.getElement(root, "data", true);
			
			//sets the subscriber name
			Element customerInfo = DOMUtils.getElement(data, "customer", true);
			customerInfo = DOMUtils.getElement(customerInfo, "id", true);
		
			customerId = DOMUtils.getText(customerInfo).toString();
			
		} catch(Exception e) {
			log.error("Erro encontrado no XML de resposta do Infobus.", e);
			throw new ErrorMessageException("Erro encontrado no XML de resposta do Infobus.", e);
		}	
		
		return customerId;
		
	}
	
	/**
	 * 
	 * Returns the custcode from the xmlResponse
	 * 
	 * @param xmlResponse
	 * @return custCode
	 */
	private String getCustCode(String xmlResponse) {
		
		String custCode = ""; 
		Element root;
		
		try {
			
			root = DOMUtils.openDocument(new ByteArrayInputStream(xmlResponse.getBytes()));
			Element data = DOMUtils.getElement(root, "data", true);
			
			//sets the subscriber name
			Element customerInfo = DOMUtils.getElement(data, "customer", true);
			customerInfo = DOMUtils.getElement(customerInfo, "code", true);
		
			custCode = DOMUtils.getText(customerInfo).toString();
			
		} catch(Exception e) {
			log.error("Erro encontrado no XML de resposta do Infobus.", e);
			throw new ErrorMessageException("Erro encontrado no XML de resposta do Infobus.", e);
		}	
		
		return custCode;

	}
	
	/**
	 * 
	 * Returns the family account structure from the xmlResponse.
	 * 
	 * @param xmlResponse
	 * @return accountStructure
	 * @throws InfoBusException 
	 */
	private Map getAccountStructure(String xmlResponse) {
		
		Map accountStructure = new LinkedHashMap();
		Element root;
		
		try {
			root = DOMUtils.openDocument(new ByteArrayInputStream(xmlResponse.getBytes()));
			
			//second level variables
			NodeList secondLevelNodeList;	
			NodeList secondLevelParamNodeList;
			Element secondLevelElement;
			Element secondLevelParamElement;
			//third level variables
			NodeList thirdLevelNodeList;
			NodeList thirdLevelParamNodeList;
			Element thirdLevelElement;
			Element thirdLevelParamElement;
			//fourth level variables
			NodeList fourthLevelNodeList;
			NodeList fourthLevelParamNodeList;
			Element fourthLevelElement;
			Element fourthLevelParamElement;
			
			//strings to help on hierarchy map filling
			String firstLevelCustCode = "";
			String secondLevelCustCode = "";			
			String thirdLevelCustCode = "";
			String fourthLevelCustCode = "";
			
			//First verify if there are nodes in the tree
			Element recordGroup = DOMUtils.getElement(root, "RECORD_GROUP", true);
			String recordGroupStr = DOMUtils.getAttribute(recordGroup, "size", true);
			if (recordGroupStr != null && Integer.parseInt(recordGroupStr) > 0) {
			
				//first level
				Element firsLevelElement = DOMUtils.getElement(root, "RECORD", true);
				Element firsLevelParamElement;
				NodeList firsLevelParamNodeList = DOMUtils.getElements(firsLevelElement, "PARAM");
				for (int i = 0; i < firsLevelParamNodeList.getLength(); i++) {
					firsLevelParamElement = (Element) firsLevelParamNodeList.item(i);
					if(DOMUtils.getAttribute(firsLevelParamElement, "name", true).equalsIgnoreCase("CS_CODE")) {
						firstLevelCustCode = DOMUtils.getAttribute(firsLevelParamElement, "value", true);
						accountStructure.put(firstLevelCustCode, "");
						break;
					}
				}
				
				//second, third and fourth levels
				secondLevelNodeList = DOMUtils.getElements(firsLevelElement, "RECORD");	
				for (int i = 0; i < secondLevelNodeList.getLength(); i++) {
					secondLevelElement = (Element) secondLevelNodeList.item(i);
					secondLevelParamNodeList = DOMUtils.getElements(secondLevelElement, "PARAM");
					for (int j = 0; j < secondLevelParamNodeList.getLength(); j++) {
						secondLevelParamElement = (Element) secondLevelParamNodeList.item(j);
						if(DOMUtils.getAttribute(secondLevelParamElement, "name", true).equalsIgnoreCase("CS_CODE")) {
							secondLevelCustCode = DOMUtils.getAttribute(secondLevelParamElement, "value", true);
							accountStructure.put(secondLevelCustCode, firstLevelCustCode);
							break;
						}
					}
					thirdLevelNodeList = DOMUtils.getElements(secondLevelElement, "RECORD");	
					for (int k = 0; k < thirdLevelNodeList.getLength(); k++) {
						thirdLevelElement = (Element) thirdLevelNodeList.item(k);
						thirdLevelParamNodeList = DOMUtils.getElements(thirdLevelElement, "PARAM");
						for (int l = 0; l < thirdLevelParamNodeList.getLength(); l++) {
							thirdLevelParamElement = (Element) thirdLevelParamNodeList.item(l);
							if(DOMUtils.getAttribute(thirdLevelParamElement, "name", true).equalsIgnoreCase("CS_CODE")) {
								thirdLevelCustCode = DOMUtils.getAttribute(thirdLevelParamElement, "value", true);
								accountStructure.put(thirdLevelCustCode, secondLevelCustCode);
								break;
							}
						}
						fourthLevelNodeList = DOMUtils.getElements(thirdLevelElement, "RECORD");
						for (int m = 0; m < fourthLevelNodeList.getLength(); m++) {
							fourthLevelElement = (Element) fourthLevelNodeList.item(m);
							fourthLevelParamNodeList = DOMUtils.getElements(fourthLevelElement, "PARAM");
							for (int l = 0; l < fourthLevelParamNodeList.getLength(); l++) {
								fourthLevelParamElement = (Element) fourthLevelParamNodeList.item(l);
								if(DOMUtils.getAttribute(fourthLevelParamElement, "name", true).equalsIgnoreCase("CS_CODE")) {
									fourthLevelCustCode = DOMUtils.getAttribute(fourthLevelParamElement, "value", true);
									accountStructure.put(fourthLevelCustCode, thirdLevelCustCode);
									break;
								}
							}
						}
					}							
				}
			}
		} catch(Exception e) {
			log.error("Erro encontrado no XML de resposta do Infobus.", e);
			throw new ErrorMessageException("Erro encontrado no XML de resposta do Infobus.", e);
		}	
		
		return accountStructure;
	}			
	
	/**
	 * 
	 * Returns a activation date from a CS_STAT_CHNG.
	 * 
	 * @param serviceHistoryStr
	 * @return activationDate
	 * @throws InfoBusException 
	 */
	private String getActivationDate(String csStatChng) {
		
		String activationDate = "";
		
		if (csStatChng != null) {
			int lastIndexOf = csStatChng.lastIndexOf(ACTIVATE_STATE);
			
			try {
				activationDate = DateFormat.format(csStatChng.substring(lastIndexOf-6, lastIndexOf), "yymmdd", "dd/mm/yyyy");
			} catch (ParseException e) {
				log.error("Erro encontrado no XML de resposta do Infobus.", e);
				throw new ErrorMessageException("Erro encontrado no XML de resposta do Infobus.", e);
			}
		}
		
		return activationDate;	
	}	
	
	/**
	 * 
	 * Returns a service status from a CS_STAT_CHN.
	 * 
	 * @param serviceHistory
	 * @return serviceStatusStr
	 */
	private String getServiceCurrentStatus(String csStatChng) {
				
		char serviceStatus;
		String serviceStatusStr = "";				
		
		if (csStatChng != null) {
			serviceStatus = csStatChng.charAt(csStatChng.length()-1);
			if (serviceStatus == ACTIVATE_STATE){
				serviceStatusStr = "Ativado";
			} else if (serviceStatus == SUSPENSE_STATE) {
				serviceStatusStr = "Suspenso";
			} else {
				serviceStatusStr = "Desativado";
			}			
		}	
		
		return serviceStatusStr;
	}
	
	/**
	 * 
	 * Format the status.
	 * 
	 * @return a->'Ativado', d->'Desativado', s->'Suspenso'.
	 */
	private String formatStatus(String statusChar) {
		
		if (statusChar != null) {
			if (statusChar.trim().equalsIgnoreCase(String.valueOf(ACTIVATE_STATE))) {
				return "Ativado";
			} else if (statusChar.trim().equalsIgnoreCase(String.valueOf(DEACTIVATE_STATE))) {
				return "Desativado";
			} else if (statusChar.trim().equalsIgnoreCase(String.valueOf(SUSPENSE_STATE))){
				return "Suspenso";
			} else return "";
		} 
		return "";
		
	}
	
	/**
	 * TODO why this methods was overriden, and what's the new expected behavior.
	 * 
	 * @param arg0
	 * @throws ConfigurationException
	 * @see br.com.auster.facelift.services.Service#init(org.w3c.dom.Element)
	 */
	public void init(Element element) throws ConfigurationException {

    	this.infobusUrl = DOMUtils.getAttribute(element, "infobusUrl", true);

	}
}