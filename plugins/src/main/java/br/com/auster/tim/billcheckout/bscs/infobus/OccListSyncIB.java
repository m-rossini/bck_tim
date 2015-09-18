package br.com.auster.tim.billcheckout.bscs.infobus;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class OccListSyncIB extends SyncIB {	
	
    private static OccListSyncIB instance = null;
    
    //TODO definir nome do serviço
    private final static String INFOBUS_SERVICE_NAME = "BCBSBCOCCQRY";
    
    /**
     * Class implements singleton pattern.
     * @return instance
     */
    public static OccListSyncIB getInstance() {
        if (instance == null) { instance = new OccListSyncIB(); }
        return instance;
    }	
	
    private OccListSyncIB() {}
    
	public String process(String parameter, String infobusUrl) {

		//invoke infobus service
		IBResponseVO ibResponse = communicate(parameter, INFOBUS_SERVICE_NAME, infobusUrl);

		return  ibResponse.getData();
		
			/*--- JUST FOR TESTING ---*/
//			log.debug("Response XML from '" + INFOBUS_SERVICE_NAME + ":" + '\n' + FakeXMLForInfobusTest.OCC_LIST_RESPONSE_XML);
//			return FakeXMLForInfobusTest.OCC_LIST_RESPONSE_XML;
			/*------------------------*/

	}

	protected Element createXMLTree(String parameter, Document doc) {

		// Creating the XML tree
		Element nodeIb = doc.createElement("ib-msg");
		doc.appendChild(nodeIb);

		Element nodeData = doc.createElement("data");
		nodeIb.appendChild(nodeData);
		
		Element nodeCustomer = doc.createElement("customer");
		nodeData.appendChild(nodeCustomer);
		
		Element nodeCode = doc.createElement("code");
		nodeCode.appendChild(doc.createTextNode(parameter));
		
		nodeCustomer.appendChild(nodeCode);

		return nodeIb;
	}

}
