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
public class Rule_A11_Test extends BaseRuleTest {


	private static String[] RULES_FILES = { "src/main/conf/rules/A11-nf-threshold.drl",
											"src/main/conf/rules/guiding/load-carrier-code.drl"};



    protected void createGlobals() {
    	super.createGlobals();
    	try {
    		Class.forName("org.apache.commons.dbcp.PoolingDriver");
    		Class.forName("oracle.jdbc.driver.OracleDriver");
    		SQLConnectionManager.init(DOMUtils.openDocument("bgh/guiding/pool.xml", false));

    		ModelLoader modelLoader =  new ModelLoader();
    		Element configuration = DOMUtils.openDocument("bgh/thresholds/model-caches.xml", false);
    		modelLoader.configure(configuration);
    		workingMemory.setGlobal( "modelLoader", modelLoader );
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }


	/**
	 * This is a correct file. Both local and LD NFs are within the configured ranges
	 */
	public void testAccount1() {
		try {
			this.startupRuleEngine(RULES_FILES);
			Account acc = this.loadBGHTestFile("bgh/A11/ACCOUNT1.BGH");
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
	 * Here we have a upper limit exceeded in the Local NF, and a lower limit in the LD NF
	 */
	public void testAccount2() {
		try {
			this.startupRuleEngine(RULES_FILES);
			Account acc = this.loadBGHTestFile("bgh/A11/ACCOUNT2.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(2, this.results.size());
			for (Consequence c : this.results) {
				String description = c.getDescription();
				if (description.equals("O limite superior para valores de NF foi extrapolado.")) {
					assertEquals("0", c.getAttributes().getAttributeValue5());
				} else if (description.equals("O limite inferior para valores de NF foi extrapolado.")) {
					assertEquals("1", c.getAttributes().getAttributeValue5());
				} else {
					fail("Description for this consequence does not match expected ones: " + description);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Here we have a upper limit exceeded in the LD NF
	 */
	public void testAccount3() {
		try {
			this.startupRuleEngine(RULES_FILES);
			Account acc = this.loadBGHTestFile("bgh/A11/ACCOUNT3.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(1, this.results.size());
			for (Consequence c : this.results) {
				String description = c.getDescription();
				if (description.equals("O limite superior para valores de NF foi extrapolado.")) {
					assertEquals("1", c.getAttributes().getAttributeValue5());
				} else {
					fail("Description for this consequence does not match expected ones: " + description);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Here we have a upper limit exceeded in the LD NF
	 */
	public void testAccount4() {
		try {
			this.startupRuleEngine(RULES_FILES);
			Account acc = this.loadBGHTestFile("bgh/A11/ACCOUNT4.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(1, this.results.size());
			for (Consequence c : this.results) {
				String description = c.getDescription();
				if (description.equals("O limite superior para valores de NF foi extrapolado.")) {
					assertEquals("1", c.getAttributes().getAttributeValue5());
				} else {
					fail("Description for this consequence does not match expected ones: " + description);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Here we only have the Local NF, and it is within the configured ranges
	 */
	public void testAccount5() {
		try {
			this.startupRuleEngine(RULES_FILES);
			Account acc = this.loadBGHTestFile("bgh/A11/ACCOUNT5.BGH");
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
	 * Here we have a upper limit exceeded in the Local NF
	 */
	public void testAccount6() {
		try {
			this.startupRuleEngine(RULES_FILES);
			Account acc = this.loadBGHTestFile("bgh/A11/ACCOUNT6.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(1, this.results.size());
			for (Consequence c : this.results) {
				String description = c.getDescription();
				if (description.equals("O limite superior para valores de NF foi extrapolado.")) {
					assertEquals("000.001.867-AB", c.getAttributes().getAttributeValue5());
				} else {
					fail("Description for this consequence does not match expected ones: " + description);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Ticket #262
	 */
	public void testAccount7() {
		try {
			this.startupRuleEngine(RULES_FILES);
			Account acc = this.loadBGHTestFile("bgh/A11/ACCOUNT7.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(1, this.results.size());
			for (Consequence c : this.results) {
				String description = c.getDescription();
				if (description.equals("O limite superior para valores de NF foi extrapolado.")) {
					assertEquals("0", c.getAttributes().getAttributeValue5());
				} else {
					fail("Description for this consequence does not match expected ones: " + description);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

}
