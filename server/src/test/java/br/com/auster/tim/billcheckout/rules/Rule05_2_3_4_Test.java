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
import br.com.auster.billcheckout.consequence.telco.hibernate.HibernateTelcoDimensionsFacadeImpl;
import br.com.auster.billcheckout.model.cache.CarrierDataCache;
import br.com.auster.common.sql.SQLConnectionManager;
import br.com.auster.common.xml.DOMUtils;
import br.com.auster.dware.graph.Request;
import br.com.auster.om.invoice.Account;



/**
 * @author framos
 * @version $Id$
 *
 */
public class Rule05_2_3_4_Test extends BaseRuleTest {


	private String[] RULES = { "src/main/conf/rules/guiding/load-carrier-code.drl",
								"src/main/conf/rules/R05.2_3_4-validate-carrier-info.drl" };


    protected void createGlobals() {
    	super.createGlobals();
    	try {
    		Class.forName("org.apache.commons.dbcp.PoolingDriver");
    		Class.forName("oracle.jdbc.driver.OracleDriver");
    		SQLConnectionManager.init(DOMUtils.openDocument("bgh/guiding/pool.xml", false));

	    	Element conf = DOMUtils.openDocument("bgh/guiding/caches.xml", false);
    		CarrierDataCache carrierData =  new CarrierDataCache();
    		carrierData.configure(conf);
    		workingMemory.setGlobal( "carrierDataCache", carrierData );
    	} catch (Exception e) {
    		e.printStackTrace();
    		fail();
    	}
    }

	/**
	 * This is the correct file
	 */
	public void testAccount1() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
			Account acc = this.loadBGHTestFile("bgh/r05_2_3_4/ACCOUNT1.BGH");
			this.assertAccount(acc, new Request() {
				public long getWeight() { return 0; }}
			);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashMap<String, Integer> counters = new HashMap<String, Integer>();
			HashMap<String, String> counters2 = new HashMap<String, String>();
			for (Consequence c : this.results) {
				String k = c.getDescription();
				if (! counters.containsKey(k)) {
					counters.put(k, new Integer(0));
				}
				Integer d = counters.get(k);
				counters.put(k, new Integer(d.intValue() + 1));
				// counters2
				counters2.put(c.getAttributes().getAttributeValue3(), c.getAttributes().getAttributeValue4());
			}
			assertEquals(2, this.results.size());
			assertNotNull(counters.get("O CEP do endereço não coincide com o registrado na tabela de referência."));
			assertNotNull(counters.get("O endereço da operadora não coincide com o registrado na tabela de referência."));

			assertEquals(2, counters2.size());
			assertNotNull(counters2.get("Av. Giovani Gronchi"));
			assertEquals("Av. Giovanni Gronchi", counters2.get("Av. Giovani Gronchi"));
			assertNotNull(counters2.get("05724-006"));
			assertEquals("", counters2.get("05724-006"));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testAccount2() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
			Account acc = this.loadBGHTestFile("bgh/r05_2_3_4/ACCOUNT2.BGH");
			this.assertAccount(acc, new Request() {
				public long getWeight() { return 0; }}
			);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashMap<String, Integer> counters = new HashMap<String, Integer>();
			HashMap<String, String> counters2 = new HashMap<String, String>();
			for (Consequence c : this.results) {
				String k = c.getDescription();
				if (! counters.containsKey(k)) {
					counters.put(k, new Integer(0));
				}
				Integer d = counters.get(k);
				counters.put(k, new Integer(d.intValue() + 1));
				// counters2
				counters2.put(c.getAttributes().getAttributeValue3(), c.getAttributes().getAttributeValue4());
			}
			assertEquals(2, this.results.size());
			assertNotNull(counters.get("A inscrição estadual da operadora não coincide com o registrado na tabela de referência."));
			assertNotNull(counters.get("O CNPJ da operadora não coincide com o registrado na tabela de referência."));

			assertEquals(2, counters2.size());
			assertNotNull(counters2.get("41.887.461/0001-05"));
			assertEquals("01.009.686/0001-44", counters2.get("41.887.461/0001-05"));
			assertNotNull(counters2.get("41.1.580.0243582-6"));
			assertEquals("18.1.580.0243582-6", counters2.get("41.1.580.0243582-6"));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testAccount3() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
			Account acc = this.loadBGHTestFile("bgh/r05_2_3_4/ACCOUNT3.BGH");
			this.assertAccount(acc, new Request() {
				public long getWeight() { return 0; }}
			);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashMap<String, Integer> counters = new HashMap<String, Integer>();
			for (Consequence c : this.results) {
				String k = c.getDescription();
				if (! counters.containsKey(k)) {
					counters.put(k, new Integer(0));
				}
				Integer d = counters.get(k);
				counters.put(k, new Integer(d.intValue() + 1));
			}
			assertEquals(2, this.results.size());
			assertNotNull(counters.get("O número do endereço não coincide com o registrado na tabela de referência."));
			assertNotNull(counters.get("A inscrição estadual da operadora não coincide com o registrado na tabela de referência."));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testAccount4() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
			Account acc = this.loadBGHTestFile("bgh/r05_2_3_4/ACCOUNT4.BGH");
			this.assertAccount(acc, new Request() {
				public long getWeight() { return 0; }}
			);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashMap<String, Integer> counters = new HashMap<String, Integer>();
			for (Consequence c : this.results) {
				String k = c.getDescription();
				if (! counters.containsKey(k)) {
					counters.put(k, new Integer(0));
				}
				Integer d = counters.get(k);
				counters.put(k, new Integer(d.intValue() + 1));
			}
			assertEquals(4, this.results.size());
			assertNotNull(counters.get("A cidade do endereço não coincide com o registrado na tabela de referência."));
			assertNotNull(counters.get("O CNPJ da operadora não coincide com o registrado na tabela de referência."));
			assertNotNull(counters.get("O endereço da operadora não coincide com o registrado na tabela de referência."));
			assertNotNull(counters.get("O nome da operadora não coincide com o registrado na tabela de referência."));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testAccount5() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
			Account acc = this.loadBGHTestFile("bgh/r05_2_3_4/ACCOUNT5.BGH");
			this.assertAccount(acc, new Request() {
				public long getWeight() { return 0; }}
			);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashMap<String, Integer> counters = new HashMap<String, Integer>();
			for (Consequence c : this.results) {
				String k = c.getDescription();
				if (! counters.containsKey(k)) {
					counters.put(k, new Integer(0));
				}
				Integer d = counters.get(k);
				counters.put(k, new Integer(d.intValue() + 1));
			}
			assertEquals(10, this.results.size());
			assertNotNull(counters.get("O endereço da operadora não coincide com o registrado na tabela de referência."));
			assertEquals(2, counters.get("O endereço da operadora não coincide com o registrado na tabela de referência.").intValue());
			assertNotNull(counters.get("O número do endereço não coincide com o registrado na tabela de referência."));
			assertEquals(1, counters.get("O número do endereço não coincide com o registrado na tabela de referência.").intValue());
			assertNotNull(counters.get("A cidade do endereço não coincide com o registrado na tabela de referência."));
			assertEquals(2, counters.get("A cidade do endereço não coincide com o registrado na tabela de referência.").intValue());
			assertNotNull(counters.get("O CEP do endereço não coincide com o registrado na tabela de referência."));
			assertEquals(2, counters.get("O CEP do endereço não coincide com o registrado na tabela de referência.").intValue());
			assertNotNull(counters.get("A inscrição estadual da operadora não coincide com o registrado na tabela de referência."));
			assertEquals(1, counters.get("A inscrição estadual da operadora não coincide com o registrado na tabela de referência.").intValue());
			assertNotNull(counters.get("O CNPJ da operadora não coincide com o registrado na tabela de referência."));
			assertEquals(1, counters.get("O CNPJ da operadora não coincide com o registrado na tabela de referência.").intValue());
			assertNotNull(counters.get("O nome da operadora não coincide com o registrado na tabela de referência."));
			assertEquals(1, counters.get("O nome da operadora não coincide com o registrado na tabela de referência.").intValue());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testAccount6() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
			Account acc = this.loadBGHTestFile("bgh/r05_2_3_4/ACCOUNT6.BGH");
			this.assertAccount(acc, new Request() {
				public long getWeight() { return 0; }}
			);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashMap<String, Integer> counters = new HashMap<String, Integer>();
			for (Consequence c : this.results) {
				String k = c.getDescription();
				if (! counters.containsKey(k)) {
					counters.put(k, new Integer(0));
				}
				Integer d = counters.get(k);
				counters.put(k, new Integer(d.intValue() + 1));
			}
			assertEquals(8, this.results.size());
			assertNotNull(counters.get("O endereço da operadora não coincide com o registrado na tabela de referência."));
			assertEquals(2, counters.get("O endereço da operadora não coincide com o registrado na tabela de referência.").intValue());
			assertNotNull(counters.get("O número do endereço não coincide com o registrado na tabela de referência."));
			assertEquals(2, counters.get("O número do endereço não coincide com o registrado na tabela de referência.").intValue());
			assertNotNull(counters.get("A inscrição estadual da operadora não coincide com o registrado na tabela de referência."));
			assertEquals(1, counters.get("A inscrição estadual da operadora não coincide com o registrado na tabela de referência.").intValue());
			assertNotNull(counters.get("O nome da operadora não coincide com o registrado na tabela de referência."));
			assertEquals(1, counters.get("O nome da operadora não coincide com o registrado na tabela de referência.").intValue());
			assertNotNull(counters.get("A cidade do endereço não coincide com o registrado na tabela de referência."));
			assertEquals(1, counters.get("A cidade do endereço não coincide com o registrado na tabela de referência.").intValue());
			assertNotNull(counters.get("O CNPJ da operadora não coincide com o registrado na tabela de referência."));
			assertEquals(1, counters.get("O CNPJ da operadora não coincide com o registrado na tabela de referência.").intValue());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * This is the correct file
	 */
	public void testAccount7() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
			Account acc = this.loadBGHTestFile("bgh/r05_2_3_4/ACCOUNT7.BGH");
			this.assertAccount(acc, new Request() {
				public long getWeight() { return 0; }}
			);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(1, this.results.size());
			assertEquals("O CEP do endereço não coincide com o registrado na tabela de referência.", this.results.iterator().next().getDescription());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Account from ticket #98
	 */
	public void testAccount8() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
			Account acc = this.loadBGHTestFile("bgh/r05_2_3_4/ACCOUNT8.BGH");
			this.assertAccount(acc, new Request() {
				public long getWeight() { return 0; }}
			);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			// running over results list to check if consequences where ok
			HashMap<String, Integer> counters = new HashMap<String, Integer>();
			for (Consequence c : this.results) {
				String k = c.getDescription();
				if (! counters.containsKey(k)) {
					counters.put(k, new Integer(0));
				}
				Integer d = counters.get(k);
				counters.put(k, new Integer(d.intValue() + 1));
			}
			assertEquals(3, this.results.size());
			assertEquals(2, counters.size());
			assertNotNull(counters.get("O endereço da operadora não coincide com o registrado na tabela de referência."));
			assertEquals(1, counters.get("O endereço da operadora não coincide com o registrado na tabela de referência.").intValue());
			assertNotNull(counters.get("A cidade do endereço não coincide com o registrado na tabela de referência."));
			assertEquals(2, counters.get("A cidade do endereço não coincide com o registrado na tabela de referência.").intValue());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Account from ticket #99
	 */
	public void testAccount9() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
			Account acc = this.loadBGHTestFile("bgh/r05_2_3_4/ACCOUNT9.BGH");
			this.assertAccount(acc, new Request() {
				public long getWeight() { return 0; }}
			);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashMap<String, Integer> counters = new HashMap<String, Integer>();
			HashMap<String, String> counters2 = new HashMap<String, String>();
			for (Consequence c : this.results) {
				String k = c.getDescription();
				if (! counters.containsKey(k)) {
					counters.put(k, new Integer(0));
				}
				Integer d = counters.get(k);
				counters.put(k, new Integer(d.intValue() + 1));
				// counters2
				counters2.put(c.getAttributes().getAttributeValue3(), c.getAttributes().getAttributeValue4());
			}
			assertEquals(4, this.results.size());
			assertEquals(4, counters.size());
			assertNotNull(counters.get("O nome da operadora não coincide com o registrado na tabela de referência."));
			assertEquals(1, counters.get("O nome da operadora não coincide com o registrado na tabela de referência.").intValue());
			assertNotNull(counters.get("Não foi encontrado dados desta operadora na tabela de referência."));
			assertEquals(1, counters.get("Não foi encontrado dados desta operadora na tabela de referência.").intValue());
			assertNotNull(counters.get("A cidade do endereço não coincide com o registrado na tabela de referência."));
			assertEquals(1, counters.get("A cidade do endereço não coincide com o registrado na tabela de referência.").intValue());
			// The other six are related to two LDC address, CNPJ and IE

			assertEquals(4, counters2.size());
			assertNotNull(counters2.get("TIM Nordeste Telecomunicações S/A"));
			assertEquals("TIM Nordeste S.A.", counters2.get("TIM Nordeste Telecomunicações S/A"));
			assertNotNull(counters2.get("02.336.993/0001-00"));
			assertEquals("01.009.686/0001-44", counters2.get("02.336.993/0001-00"));
			assertNotNull(counters2.get("Recife"));
			assertEquals("Várzea - Recife", counters2.get("Recife"));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Test case regarding ticket #114.
	 */
	public void testAccount11() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
			Account acc = this.loadBGHTestFile("bgh/r05_2_3_4/ACCOUNT11.BGH");
			this.assertAccount(acc, new Request() {
				public long getWeight() { return 0; }}
			);
			this.workingMemory.fireAllRules();

			// running over results list to check if consequences where ok
			HashMap<String, Integer> counters = new HashMap<String, Integer>();
			for (Consequence c : this.results) {
				String k = c.getDescription();
				if (! counters.containsKey(k)) {
					counters.put(k, new Integer(0));
				}
				Integer d = counters.get(k);
				counters.put(k, new Integer(d.intValue() + 1));
			}

			assertNull(counters.get("Nome da Operadora Não Confere"));

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

}
