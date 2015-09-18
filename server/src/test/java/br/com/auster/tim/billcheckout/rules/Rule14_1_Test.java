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

import br.com.auster.billcheckout.consequence.Consequence;


/**
 * @author framos
 * @version $Id$
 *
 */
public class Rule14_1_Test extends BaseRuleTest {


	private String[] RULES = { "src/main/conf/rules/R14.1-barcode-validation.drl",
			                   "src/test/resources/bgh/r14_1/debug-rule.drl" };
	
	public void testAccount1() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
	        this.assertAccount(this.loadBGHTestFile("bgh/r14_1/ACCOUNT1.BGH"));
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertTrue(this.results.size() == 0);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	public void testAccount2() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
	        this.assertAccount(this.loadBGHTestFile("bgh/r14_1/ACCOUNT2.BGH"));
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashMap<String, Integer>  counters = new HashMap<String, Integer>();
			for (Consequence c : this.results) {
				String k = c.getDescription();
				if (! counters.containsKey(k)) {
					counters.put(k, new Integer(0));
				}
				Integer d = counters.get(k);
				counters.put(k, new Integer(d.intValue() + 1));
			}
			assertEquals(2, this.results.size());
			assertEquals(1, counters.get("Valor da Fatura não confere com código de barras.").intValue());
			assertEquals(1, counters.get("Dígito verificador do código de barras não confere.").intValue());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testAccount3() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
	        this.assertAccount(this.loadBGHTestFile("bgh/r14_1/ACCOUNT3.BGH"));
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashMap<String, Integer>  counters = new HashMap<String, Integer>();
			for (Consequence c : this.results) {
				String k = c.getDescription();
				if (! counters.containsKey(k)) {
					counters.put(k, new Integer(0));
				}
				Integer d = counters.get(k);
				counters.put(k, new Integer(d.intValue() + 1));
			}
			assertEquals(1, this.results.size());
			assertEquals(1, counters.get("Dígito verificador do código de barras não confere.").intValue());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testAccount4() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
	        this.assertAccount(this.loadBGHTestFile("bgh/r14_1/ACCOUNT4.BGH"));
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashMap<String, Integer>  counters = new HashMap<String, Integer>();
			for (Consequence c : this.results) {
				String k = c.getDescription();
				if (! counters.containsKey(k)) {
					counters.put(k, new Integer(0));
				}
				Integer d = counters.get(k);
				counters.put(k, new Integer(d.intValue() + 1));
			}
			assertEquals(2, this.results.size());
			assertEquals(1, counters.get("Dígito verificador do código de barras não confere.").intValue());
			assertEquals(1, counters.get("Identificador de segmento de telecomunicações inválido no código de barras.").intValue());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		
		
	}

	public void testAccount5() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
	        this.assertAccount(this.loadBGHTestFile("bgh/r14_1/ACCOUNT5.BGH"));
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashMap<String, Integer>  counters = new HashMap<String, Integer>();
			for (Consequence c : this.results) {
				String k = c.getDescription();
				if (! counters.containsKey(k)) {
					counters.put(k, new Integer(0));
				}
				Integer d = counters.get(k);
				counters.put(k, new Integer(d.intValue() + 1));
			}
			assertEquals(1, this.results.size());
			assertEquals(1, counters.get("Dígito verificador do código de barras não confere.").intValue());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testAccount6() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
	        this.assertAccount(this.loadBGHTestFile("bgh/r14_1/ACCOUNT6.BGH"));
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashMap<String, Integer>  counters = new HashMap<String, Integer>();
			for (Consequence c : this.results) {
				String k = c.getDescription();
				if (! counters.containsKey(k)) {
					counters.put(k, new Integer(0));
				}
				Integer d = counters.get(k);
				counters.put(k, new Integer(d.intValue() + 1));
			}
			assertEquals(2, this.results.size());
			assertEquals(1, counters.get("Identificação de empresa inválida no código de  barras.").intValue());
			assertEquals(1, counters.get("Dígito verificador do código de barras não confere.").intValue());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testAccount7() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
	        this.assertAccount(this.loadBGHTestFile("bgh/r14_1/ACCOUNT7.BGH"));
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertTrue(this.results.size() == 0);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testAccount8() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
	        this.assertAccount(this.loadBGHTestFile("bgh/r14_1/ACCOUNT8.BGH"));
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertTrue(this.results.size() == 0);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testAccount9() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
	        this.assertAccount(this.loadBGHTestFile("bgh/r14_1/ACCOUNT9.BGH"));
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertTrue(this.results.size() == 0);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testAccount10() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
	        this.assertAccount(this.loadBGHTestFile("bgh/r14_1/ACCOUNT10.BGH"));
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertTrue(this.results.size() == 0);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testAccount11() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
	        this.assertAccount(this.loadBGHTestFile("bgh/r14_1/ACCOUNT11.BGH"));
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertTrue(this.results.size() == 0);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	public void testAccount12() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
	        this.assertAccount(this.loadBGHTestFile("bgh/r14_1/ACCOUNT12.BGH"));
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(1, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	public void testAccount13() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
	        this.assertAccount(this.loadBGHTestFile("bgh/r14_1/ACCOUNT13.BGH"));
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(3, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}	

	public void testAccount14() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
	        this.assertAccount(this.loadBGHTestFile("bgh/r14_1/ACCOUNT14.BGH"));
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(5, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}	

	public void testAccount15() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
	        this.assertAccount(this.loadBGHTestFile("bgh/r14_1/ACCOUNT15.BGH"));
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(2, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}	

	public void testAccount16() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
	        this.assertAccount(this.loadBGHTestFile("bgh/r14_1/ACCOUNT16.BGH"));
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(5, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	public void testAccount17() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
	        this.assertAccount(this.loadBGHTestFile("bgh/r14_1/ACCOUNT17.BGH"));
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}	
}
