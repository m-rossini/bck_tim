/*
 * Copyright (c) 2004-2006 Auster Solutions. All Rights Reserved.
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
 * Created on 12/12/2006
 */
package br.com.auster.tim.billcheckout.aggregation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.log4j.xml.DOMConfigurator;
import org.w3c.dom.Element;

import br.com.auster.common.data.groovy.DeclarativeAggregationEngine;
import br.com.auster.common.xml.DOMUtils;
import br.com.auster.dware.graph.Request;
import br.com.auster.om.invoice.Account;
import br.com.auster.tim.bgh.BGH2SAX;



/**
 * @author framos
 * @version $Id$
 *
 */
public class EPAggregationTest extends TestCase {

	
	public static final String EP_CONFIGURATION = "src/main/conf/ep/ep-aggregation.xml";
	
	private DeclarativeAggregationEngine engine;
	

	public void testAccount1() {
		try {
			List<Account> act = new ArrayList<Account>();
			act.add(loadBGHTestFile("bgh/ep/ACCOUNT1.BGH"));			
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("request", buildSimpleRequest());
			data.put("invoice-om", act);
			Map results = this.engine.aggregate(data);
			assertNotNull(results);
			assertNotNull(results.get("invoiceFact-list"));
			assertEquals(1, ((Collection)results.get("invoiceFact-list")).size());
			assertNotNull(results.get("contractTotalsFact-list"));
			assertEquals(4, ((Collection)results.get("contractTotalsFact-list")).size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	public void testAccount2() {
		try {
			List<Account> act = new ArrayList<Account>();
			act.add(loadBGHTestFile("bgh/ep/ACCOUNT2.BGH"));			
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("request", buildSimpleRequest());
			data.put("invoice-om", act);
			Map results = this.engine.aggregate(data);
			assertNotNull(results);
			assertNotNull(results.get("invoiceFact-list"));
			assertEquals(1, ((Collection)results.get("invoiceFact-list")).size());
			assertNotNull(results.get("contractTotalsFact-list"));
			assertEquals(7, ((Collection)results.get("contractTotalsFact-list")).size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}	
	
	public void testAccount3() {
		try {
			List<Account> act = new ArrayList<Account>();
			act.add(loadBGHTestFile("bgh/ep/ACCOUNT3.BGH"));			
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("request", buildSimpleRequest());
			data.put("invoice-om", act);
			Map results = this.engine.aggregate(data);
			assertNotNull(results);
			assertNotNull(results.get("invoiceFact-list"));
			assertEquals(1, ((Collection)results.get("invoiceFact-list")).size());
			assertNotNull(results.get("contractTotalsFact-list"));
			assertEquals(11, ((Collection)results.get("contractTotalsFact-list")).size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}	

	
	protected void setUp() throws Exception {
		DOMConfigurator.configure(DOMUtils.openDocument("src/test/resources/log4j.xml", false));
		this.engine = new DeclarativeAggregationEngine();
		Element aeConf = DOMUtils.openDocument(EP_CONFIGURATION, false);
		this.engine.configure(aeConf);
	}
	
    protected Account loadBGHTestFile(String _sourcename) throws Exception {
		BGH2SAX loader = new BGH2SAX();
		return loader.run(_sourcename);
    }	
    
    protected Request buildSimpleRequest() {
    	Request rq = new Request() {			
			public long getWeight() { return 0; }
    	};
    	return rq;
    }
}
