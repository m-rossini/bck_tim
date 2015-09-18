package br.com.auster.tim.billcheckout.bscs.infobus;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.security.PrivilegedAction;

import javax.security.auth.Subject;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import weblogic.security.Security;
import br.com.auster.common.xml.DOMUtils;
import br.com.auster.dware.console.error.ErrorMessageException;
import br.com.timbrasil.infobus.j2ee.connector.IBJConnector;
import br.com.timbrasil.infobus.j2ee.connector.IBResponse;


public abstract class SyncIB {

	private static final Logger log = Logger.getLogger(SyncIB.class);
	
	public static final int INFOBUS_CODE_OK = 1;
	
	//externalizar os parametros abaixo para um arquivo de configuracoes
	public static final String SYSTEM_ID = "BILLCHECK";
	
	
	protected SyncIB() {}

	protected class MyPrivilegedAction implements PrivilegedAction {
		private String serviceID;
		private String xmlRequest;
		private String infobusUrl;

		public MyPrivilegedAction(String serviceID, String xmlRequest, String infobusUrl) {
			this.serviceID = serviceID;
			this.xmlRequest = xmlRequest;
			this.infobusUrl = infobusUrl;
		}
		
		public Object run() {
			try {
				log.debug("Connectting to INFOBUS on '" + infobusUrl + "' ...");
				IBJConnector conn = new IBJConnector(SYSTEM_ID, infobusUrl);
				log.debug("Connected to INFOBUS: " + conn);
				IBResponse iBResponse = conn.requestReply(this.serviceID, this.xmlRequest);
				if (iBResponse == null) {
					throw new ErrorMessageException("Falha desconhecida ao processar requisicao. Tente novamente.");
				} else {
					IBResponseVO response = new IBResponseVO();
					response.setAppCode(iBResponse.getAppCode());
					//JUST FOR TESTING
	//				response.setAppCode(1);	
					response.setData(iBResponse.getDataAsString());
					response.setTID(iBResponse.getTID());
					log.debug("Response XML from '" + this.serviceID + ":" + '\n' + response.getData());
					if (response.getAppCode() == INFOBUS_CODE_OK) {
						return response;
					} else {
						String errorMessage = "Falha na requisicao ao InfoBus";
						if (response.getData() != null) {
							errorMessage = getErrorMessageFromXml(response.getData());
						} 
//						throw new ErrorMessageException("Falha na requisicao ao InfoBus. TID=[" + response.getTID() + "]  Código de Erro=[" + 
//													response.getAppCode() +"]  Mensagem de Erro=[" + errorMessage + "].");
						throw new ErrorMessageException(errorMessage);
					} 
				}
			} catch (Exception e) { 
				log.error(e);
				throw new ErrorMessageException(e.getMessage(), e); 
			}
		}
		
	};
	
	protected IBResponseVO communicate(String parameter, String serviceName, String infobusUrl) {
		String xmlRequest = createXMLRequest(parameter);
		
		log.debug("Request XML to '" + serviceName + "':" + '\n' + xmlRequest);	

		IBResponseVO resp = (IBResponseVO) Security.runAs(new Subject(), new MyPrivilegedAction(serviceName, xmlRequest, infobusUrl));										
			
		return resp; 

	}

	private String getErrorMessageFromXml(String xml) {
		Element root;
		
		try {
			root = DOMUtils.openDocument(new ByteArrayInputStream(xml.getBytes()));
			Element element = DOMUtils.getElement(root, "ib-info", true);
		
			element = DOMUtils.getElement(element, "result", true);
			element = DOMUtils.getElement(element, "desc", true);
			
			return DOMUtils.getText(element).toString();
		} catch (Exception e) {
			return "";
		}
	}

	private String createXMLRequest(String parameter) {
		try {
			// Creating an empty XML Document
			DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			
			createXMLTree(parameter, doc);

			// Output the XML. set up a transformer
			TransformerFactory transfac = TransformerFactory.newInstance();
			Transformer trans = transfac.newTransformer();
			trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			trans.setOutputProperty(OutputKeys.INDENT, "yes");
			
			//create string from xml tree
			StringWriter sw = new StringWriter();
			StreamResult result = new StreamResult(sw);
			DOMSource source = new DOMSource(doc);
			trans.transform(source, result);
			return(sw.toString());
		} 
		catch (Exception e) { 
			throw new ErrorMessageException(e); 
		}

	}

	protected abstract Element createXMLTree(String parameter, Document doc);

}

