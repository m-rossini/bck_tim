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
package br.com.auster.tim.billcheckout.rules;

import java.util.HashMap;

import org.w3c.dom.Element;

import br.com.auster.billcheckout.consequence.Consequence;
import br.com.auster.billcheckout.model.ModelLoader;
import br.com.auster.common.sql.SQLConnectionManager;
import br.com.auster.common.xml.DOMUtils;
import br.com.auster.om.invoice.Account;



/**
 * @author framos
 * @version $Id$
 *
 */
public class Rule27_1_Test extends BaseRuleTest {


	private String[] RULES = {"src/main/conf/rules/guiding/load-carrier-code.drl",
							  "src/main/conf/rules/R27.1-OCCs-thresholds.drl" };


    protected void createGlobals() {
    	super.createGlobals();
    	try {
    		Class.forName("org.apache.commons.dbcp.PoolingDriver");
    		Class.forName("oracle.jdbc.driver.OracleDriver");
    		SQLConnectionManager.init(DOMUtils.openDocument("bgh/guiding/pool.xml", false));

    		ModelLoader modelLoader = new ModelLoader();
    		Element configuration = DOMUtils.openDocument("bgh/thresholds/model-caches.xml", false);
    		modelLoader.configure(configuration);
    		workingMemory.setGlobal( "modelLoader", modelLoader );
    	} catch (Exception e) {
    		e.printStackTrace();
    		fail();
    	}
    }

    /**
     * No limits violated
     *
     */
	public void testAccount1() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
			this.assertAccount(this.loadBGHTestFile("bgh/r27_1/ACCOUNT1.BGH"));
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Upper limit violated in 'Golden Number'
	 */
	public void testAccount2() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
			Account acc = this.loadBGHTestFile("bgh/r27_1/ACCOUNT2.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashMap<String, String> descriptions = new HashMap<String, String>();
			for (Consequence c : this.results) {
				descriptions.put(c.getAttributes().getAttributeValue1(), c.getDescription());
			}
			assertEquals(1, this.results.size());
			assertTrue( descriptions.containsKey("Golden Number"));
			assertEquals("OCC com valor acima ao limite superior.", descriptions.get("Golden Number"));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * No OCCs in this account
	 */
	public void testAccount3() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
			Account acc = this.loadBGHTestFile("bgh/r27_1/ACCOUNT3.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Lower limit violated in 'Ajuste de Uso de Serviços'
	 * Upper limit violated in 'Pacote Embratel'
	 */
	public void testAccount4() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
			Account acc = this.loadBGHTestFile("bgh/r27_1/ACCOUNT4.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashMap<String, String> descriptions = new HashMap<String, String>();
			for (Consequence c : this.results) {
				descriptions.put(c.getAttributes().getAttributeValue1(), c.getDescription());
			}
			assertEquals(2, this.results.size());
			assertTrue( descriptions.containsKey("Pacote Embratel"));
			assertEquals("OCC com valor acima ao limite superior.", descriptions.get("Pacote Embratel"));
			assertTrue( descriptions.containsKey("Ajuste de Uso de Serviços"));
			assertEquals("OCC com valor abaixo ao limite inferior.", descriptions.get("Ajuste de Uso de Serviços"));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	//
}


