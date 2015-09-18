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
 * Created on 25/02/2008
 */
package br.com.auster.tim.billcheckout.bscs.infobus;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * TODO What this class is responsible for
 *
 * @author William Soares
 * @version $Id$
 * @since JDK1.4
 */
public class CustIdByCustCodeSyncIB extends SyncIB {
	
    private static CustIdByCustCodeSyncIB instance = null;
    
    private final static String INFOBUS_SERVICE_NAME = "BCBSCCIDQRY";       
    
    
    /**
     * Class implements singleton pattern.
     * @return instance
     */
    public static CustIdByCustCodeSyncIB getInstance() {
        if (instance == null) { instance = new CustIdByCustCodeSyncIB(); }
        return instance;
    }	
	
    private CustIdByCustCodeSyncIB() {}
    
	public String process(String parameter, String infobusUrl) {

		//invoke infobus service
		IBResponseVO ibResponse = communicate(parameter, INFOBUS_SERVICE_NAME, infobusUrl);
						
		return  ibResponse.getData();
		
			/*--- JUST FOR TESTING ---*/
//			log.debug("Response XML from '" + INFOBUS_SERVICE_NAME + ":" + '\n' + FakeXMLForInfobusTest.CUSTID_BY_CUSTCODE_RESPONSE_XML);		
//			return FakeXMLForInfobusTest.CUSTID_BY_CUSTCODE_RESPONSE_XML;
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
