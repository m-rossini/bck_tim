package br.com.auster.tim.billcheckout.bscs;


import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import br.com.auster.common.xml.DOMUtils;
import br.com.auster.dware.console.error.ErrorMessageException;
import br.com.auster.facelift.services.ConfigurationException;
import br.com.auster.tim.billcheckout.bscs.infobus.AccountStructureSyncIB;
import br.com.auster.tim.billcheckout.bscs.infobus.ClientInfoSyncIB;
import br.com.auster.tim.billcheckout.bscs.infobus.CustIdByCustCodeSyncIB;
import br.com.auster.tim.billcheckout.bscs.infobus.FakeXMLForInfobusTest;
import br.com.auster.tim.billcheckout.bscs.infobus.OccListSyncIB;
import br.com.auster.tim.billcheckout.bscs.vo.AccountVO;
import br.com.auster.tim.billcheckout.bscs.vo.ContractVO;
import br.com.auster.tim.billcheckout.bscs.vo.GeneralInfoVO;
import br.com.auster.tim.billcheckout.bscs.vo.OccVO;

/**
 * TODO What this class is responsible for TESTING
 *
 * @author William Soares
 * @version $Id$
 * @since JDK1.4
 */
public class AccountImplTest implements Account {

	private static final String ACTIVATE_STATE = "a";
	
	private static final String DEACTIVATE_STATE = "d";
	
	private static final String SUSPENSE_STATE = "s";
	
	private String infobusUrl; 
	
	private static final Logger log = Logger.getLogger(AccountImplTest.class);
	
	
	public AccountVO getAccountVO(String parameterName, String custcode) {
		
		//buscar no BSCS o numero da conta atraves dos parametros de busca		
		
		String xmlResponse;		
		
		AccountVO accountVO = new AccountVO();
			
		//first of all we get the customer id for a custcode
//		xmlResponse = CustIdByCustCodeSyncIB.getInstance().process(custcode, this.infobusUrl);
		//then get the generalInfo, packageList and contractList calling the 'BSPMCCQUERY' sevice from infobus
		//note that this service uses customer id3
		log.debug(getCustomerId(FakeXMLForInfobusTest.CUSTID_BY_CUSTCODE_RESPONSE_XML));
//		xmlResponse = ClientInfoSyncIB.getInstance().process(getCustomerId(xmlResponse), this.infobusUrl);
		accountVO.setAccountNumber(custcode);
		accountVO.setGeneralInfo(getGeneralInfo(FakeXMLForInfobusTest.CLIENT_INFO_RESPONSE_XML));					
		accountVO.setContractVOList(getContractList(FakeXMLForInfobusTest.CLIENT_INFO_RESPONSE_XML));			
		//then we get the occList calling the 'BSPMOCCQRY' service from infobus
//		xmlResponse = OccListSyncIB.getInstance().process(custcode, this.infobusUrl);
		accountVO.setOccList(getOCCList(FakeXMLForInfobusTest.OCC_LIST_RESPONSE_XML));
		//then get the account family structure					
//		xmlResponse = AccountStructureSyncIB.getInstance().process(custcode, this.infobusUrl);
		accountVO.setHierarchyStructure(getAccountStructure(FakeXMLForInfobusTest.ACCOUNT_STRUCTURE_RESPONSE_XML));			

		return accountVO;
		
	}	

	/**
	 * 
	 * Returns a costumerId from the xml response.
	 * 
	 * @param xmlResponse
	 * @return customerId
	 * @throws InfoBusException 
	 */
	private String getCustomerId(String xmlResponse) {
		
		String customerId = "";
		Element root;
		
		try {
			root = DOMUtils.openDocument(new ByteArrayInputStream(xmlResponse.getBytes()));
			Element element = DOMUtils.getElement(root, "data", true);
			
			element = DOMUtils.getElement(element, "customer", true);
			
			element = DOMUtils.getElement(element, "id", true);
			
			customerId = DOMUtils.getText(element).toString();
			
		} catch(Exception e) {
			log.error("Erro encontrado no XML de resposta do Infobus.", e);
			throw new ErrorMessageException("Erro encontrado no XML de resposta do Infobus.", e);
		}
		
		return customerId;
		
	}	
	
	/**
	 * Returns the general info from the xml response.
	 * 
	 * @param xmlResponse
	 * @return generalInfo
	 * @throws InfoBusException 
	 */	
	private GeneralInfoVO getGeneralInfo(String xmlResponse) {
			
		GeneralInfoVO generalInfo = new GeneralInfoVO();
		Element root;
		
		try {
			root = DOMUtils.openDocument(new ByteArrayInputStream(xmlResponse.getBytes()));
			Element data = DOMUtils.getElement(root, "data", true);
			data = DOMUtils.getElement(data, "customer", true);
			
			// Obtain the client status from the DOM 
			Element element;
//			Element element = DOMUtils.getElement(data, "status", true);
//			generalInfo.setStatus(formatStatus(DOMUtils.getText(element).toString()));
			//sets '' to status for a while (until this issue were defined)
			generalInfo.setStatus("");
			
			/* Address */						
			data  =  DOMUtils.getElement(data, "address", true);
			// Obtain the street type from the DOM
			element = DOMUtils.getElement(data, "street-type", true);
			String address = DOMUtils.getText(element).toString();
	
			// Obtain the street name from the DOM 
			element = DOMUtils.getElement(data, "street", true);
			address += " " + DOMUtils.getText(element).toString();
			
			// Obtain the street number from the DOM 
			element = DOMUtils.getElement(data, "street-no", true);
			address += " " + DOMUtils.getText(element).toString();
			
			// Obtain the complement from the DOM 
			element = DOMUtils.getElement(data, "complement", true);
			address += " " + DOMUtils.getText(element).toString();
			
			// Obtain the district from the DOM
			element = DOMUtils.getElement(data, "district", true);
			address += " " + DOMUtils.getText(element).toString();
						
			// Obtain the city from the DOM 
			element = DOMUtils.getElement(data, "city", true);
			address += " " + DOMUtils.getText(element).toString();
			
			// Obtain the state from the DOM 
			element = DOMUtils.getElement(data, "state", true);
			address += " " + DOMUtils.getText(element).toString();
			
			generalInfo.setAddress(address);
			/* --------- */			
			
			// Obtain the subscriber name from the DOM 
			element = DOMUtils.getElement(data, "full-name", true);
			
			generalInfo.setSubscriberName(DOMUtils.getText(element).toString());
			
		} catch(Exception e) {
			log.error("Error parsing xml response from Infobus", e);
			throw new ErrorMessageException(e);
		}

		return generalInfo;
		
	}

	/**
	 * Returns the contract list from the xml response.
	 * 
	 * @param parameter
	 * @return contractList
	 * @throws InfoBusException 
	 */
	private List getContractList(String xmlResponse) {
		
		List contractList = new ArrayList();
		ContractVO contractVO;	
		Element root;
		
		try {
			root = DOMUtils.openDocument(new ByteArrayInputStream(xmlResponse.getBytes()));
			Element data = DOMUtils.getElement(root, "data", true);
			data = DOMUtils.getElement(data, "customer", true);
			
			//first get the full name
			Element subcriberName  = DOMUtils.getElement(data, "address", true);
			subcriberName = DOMUtils.getElement(subcriberName, "full-name", true);
			String subcriberNameStr = "";
			subcriberNameStr = DOMUtils.getText(subcriberName).toString();
			
			// Obtain the packages from the DOM 
			NodeList nodeList = DOMUtils.getElements(data, "contract");			
			
			Element element;
			for (int i = 0; i < nodeList.getLength(); i++) {
				element = (Element) nodeList.item(i);
				contractVO = new ContractVO();
				Element subElementId = DOMUtils.getElement(element, "id", true);
				contractVO.setContractNumber(DOMUtils.getText(subElementId).toString());
				Element subElementAccessNum = DOMUtils.getElement(element, "msisdn", true);
				contractVO.setAccessNumber(DOMUtils.getText(subElementAccessNum).toString());
				contractVO.setSubscriberName(subcriberNameStr);
				contractList.add(contractVO);
			}
	
		} catch(Exception e) {
			log.error("Error parsing xml response from Infobus", e);
			throw new ErrorMessageException(e);
		}
		
		return contractList;
	}	
	
	/**
	 * Returns list of OCCs from the xml response.
	 * 
	 * @param parameter
	 * @return occList
	 * @throws InfoBusException 
	 */
	private List getOCCList(String xmlResponse) {

		List occList = new ArrayList();
		OccVO occVo;
		Element root;
		
		try {			
			root = DOMUtils.openDocument(new ByteArrayInputStream(xmlResponse.getBytes()));
			Element element = DOMUtils.getElement(root, "data", true);

			NodeList nodeList = DOMUtils.getElements(element, "occ");
			
			Element subElement;
			for (int i = 0; i < nodeList.getLength(); i++) {
				element = (Element) nodeList.item(i);
				occVo = new OccVO();
				subElement = DOMUtils.getElement(element, "desc", true);
				occVo.setDescription(DOMUtils.getText(subElement).toString());
				subElement = DOMUtils.getElement(element, "value", true);
				occVo.setValue(DOMUtils.getText(subElement).toString());
				subElement = DOMUtils.getElement(element, "qtd-billed", true);
				occVo.setChargedQty(DOMUtils.getText(subElement).toString());
				subElement = DOMUtils.getElement(element, "qtd-not-billed", true);
				occVo.setNotChargedQty(DOMUtils.getText(subElement).toString());
				occList.add(occVo);
			}
	
		} catch(Exception e) {
			log.error("Error parsing xml response from Infobus", e);
			throw new ErrorMessageException(e);
		}
		
		return occList;
		
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
			
		} catch(Exception e) {
			log.error("Error parsing xml response from Infobus", e);
			throw new ErrorMessageException(e);
		}	
		
		return accountStructure;
	}					
	
	/**
	 * 
	 * Format the status.
	 * 
	 * @return a->'Ativado', d->'Desativado', s->'Suspenso'.
	 */
	private String formatStatus(String statusChar) {
		
		if (statusChar != null) {		
			if (statusChar.trim().equalsIgnoreCase(ACTIVATE_STATE)) {
				return "Ativado";
			} else if (statusChar.trim().equalsIgnoreCase(DEACTIVATE_STATE)) {
				return "Desativado";
			} else if (statusChar.trim().equalsIgnoreCase(SUSPENSE_STATE)){
				return "Suspenso";
			} else return "";		
		} 
		return "";
	}
	
	public void init(Element element) throws ConfigurationException {
		
		this.infobusUrl = DOMUtils.getAttribute(element, "infobusUrl", true);
		
	}

}