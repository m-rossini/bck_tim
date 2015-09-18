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
 * Created on 22/02/2008
 */

package br.com.auster.tim.billcheckout.bscs.infobus;

import java.io.ByteArrayInputStream;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import br.com.auster.common.xml.DOMUtils;
import br.com.auster.tim.billcheckout.bscs.vo.GeneralInfoVO;

/**
 * TODO What this class is responsible for
 *
 * @author William Soares
 * @version $Id$
 * @since JDK1.4
 */
public class PackageListSyncIB extends SyncIB {
	
	private static final Logger log = Logger.getLogger(PackageListSyncIB.class);
	
    private static PackageListSyncIB instance = null;
   
    private final static String INFOBUS_SERVICE_NAME = "BCBSPACKQRY";
    
    /**
     * Class implements singleton pattern.
     * @return instance
     */
    public static PackageListSyncIB getInstance() {
        if (instance == null) { instance = new PackageListSyncIB(); }
        return instance;
    }	
	
    private PackageListSyncIB() {}
    
	public String process(String parameter, String infobusUrl) {

		//invoke infobus service
		IBResponseVO ibResponse = communicate(parameter, INFOBUS_SERVICE_NAME, infobusUrl);	
		
		return  ibResponse.getData();
		
			/*--- JUST FOR TESTING ---*/
//			log.debug("Response XML from '" + INFOBUS_SERVICE_NAME + ":" + '\n' + FakeXMLForInfobusTest.PACKAGES_LIST_RESPONSE_XML);
//			return FakeXMLForInfobusTest.PACKAGES_LIST_RESPONSE_XML;
			/*------------------------*/
	}

	protected Element createXMLTree(String parameter, Document doc) {

		// Creating the XML tree
		Element nodeIb = doc.createElement("ib-msg");
		doc.appendChild(nodeIb);

		Element nodeData = doc.createElement("data");
		nodeIb.appendChild(nodeData);

		Element nodeContract = doc.createElement("contract");
		nodeData.appendChild(nodeContract);
		
		Element nodeId = doc.createElement("id");
		nodeId.appendChild(doc.createTextNode(parameter));
		
		nodeContract.appendChild(nodeId);

		return nodeIb;
	}

}
