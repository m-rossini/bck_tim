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
import br.com.auster.om.invoice.Account;



/**
 * @author framos
 * @version $Id$
 *
 */
public class Rule14_3_Test extends BaseRuleTest {


	private String[] RULES = { "src/main/conf/rules/R14.3-paymentdata-validation.drl" };

	/**
	 * This is the correct file
	 */
	public void testAccount1() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			this.assertAccount(this.loadBGHTestFile("bgh/r14_3/ACCOUNT1.BGH"));
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
			assertEquals(5, this.results.size());
			assertEquals(1, counters.get("Valores divergentes").intValue());
			assertEquals(1, counters.get("Data de vencimento divergente").intValue());
			assertEquals(1, counters.get("Data de emissão divergente").intValue());
			assertEquals(1, counters.get("Data de fim do ciclo divergente").intValue());
			assertEquals(1, counters.get("Data de inicio do ciclo divergente").intValue());
			// rule list
			String[] RULE_LIST = {
					"Regra 14-3 - Init. Taxes Retention",
					"Regra 14-3A", "Regra 14-3B",
					"Regra 14-3C", "Regra 14-3D",
					"Regra 14-3E" };
			int[] AGENDA_COUNT = { 1, 1, 1, 1, 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testAccount2() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			this.assertAccount(this.loadBGHTestFile("bgh/r14_3/ACCOUNT2.BGH"));
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
			assertEquals(1, this.results.size());
			assertEquals(1, counters.get("Data de inicio do ciclo divergente").intValue());
			// rule list
			String[] RULE_LIST = {
					"Regra 14-3 - Init. Taxes Retention",
					"Regra 14-3A", "Regra 14-3E" };
			int[] AGENDA_COUNT = { 1, 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testAccount3() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			this.assertAccount(this.loadBGHTestFile("bgh/r14_3/ACCOUNT3.BGH"));
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
			assertEquals(1, this.results.size());
			assertEquals(1, counters.get("Data de emissão divergente").intValue());
			// rule list
			String[] RULE_LIST = {
					"Regra 14-3 - Init. Taxes Retention",
					"Regra 14-3C", "Regra 14-3E" };
			int[] AGENDA_COUNT = { 1, 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testAccount4() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			this.assertAccount(this.loadBGHTestFile("bgh/r14_3/ACCOUNT4.BGH"));
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
			assertEquals(1, this.results.size());
			assertEquals(1, counters.get("Data de vencimento divergente").intValue());
			// rule list
			String[] RULE_LIST = {
					"Regra 14-3 - Init. Taxes Retention",
					"Regra 14-3D", "Regra 14-3E" };
			int[] AGENDA_COUNT = { 1, 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testAccount5() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			this.assertAccount(this.loadBGHTestFile("bgh/r14_3/ACCOUNT5.BGH"));
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
			assertEquals(1, this.results.size());
			assertEquals(1, counters.get("Valores divergentes").intValue());
			// rule list
			String[] RULE_LIST = { "Regra 14-3 - Init. Taxes Retention",  "Regra 14-3E" };
			int[] AGENDA_COUNT = { 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testAccount6() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			this.assertAccount(this.loadBGHTestFile("bgh/r14_3/ACCOUNT6.BGH"));
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
			assertEquals(1, counters.get("Data de fim do ciclo divergente").intValue());
			assertEquals(1, counters.get("Valor total inexistente na ficha de arrecadação").intValue());
			// rule list
			String[] RULE_LIST = {
					"Regra 14-3 - Init. Taxes Retention",
					"Regra 14-3F", "Regra 14-3B" };
			int[] AGENDA_COUNT = { 1, 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testAccount7() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			this.assertAccount(this.loadBGHTestFile("bgh/r14_3/ACCOUNT7.BGH"));
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
			assertEquals(5, this.results.size());
			assertEquals(1, counters.get("Data de vencimento divergente").intValue());
			assertEquals(1, counters.get("Data de emissão divergente").intValue());
			assertEquals(1, counters.get("Data de fim do ciclo divergente").intValue());
			assertEquals(1, counters.get("Data de inicio do ciclo divergente").intValue());
			assertEquals(1, counters.get("Valor total inexistente na ficha de arrecadação").intValue());
			// rule list
			String[] RULE_LIST = {
					"Regra 14-3 - Init. Taxes Retention",
					"Regra 14-3A", "Regra 14-3B",
					"Regra 14-3C", "Regra 14-3D",
					"Regra 14-3F"};
			int[] AGENDA_COUNT = { 1, 1, 1, 1, 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testAccount8() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			this.assertAccount(this.loadBGHTestFile("bgh/r14_3/ACCOUNT8.BGH"));
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
			assertEquals(1, this.results.size());
			assertEquals(1, counters.get("Valor total inexistente na ficha de arrecadação").intValue());
			// rule list
			String[] RULE_LIST = { "Regra 14-3 - Init. Taxes Retention", "Regra 14-3F" };
			int[] AGENDA_COUNT = { 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testAccount9() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			this.assertAccount(this.loadBGHTestFile("bgh/r14_3/ACCOUNT9.BGH"));
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
			assertEquals(1, this.results.size());
			assertEquals(1, counters.get("Data de inicio do ciclo divergente").intValue());
			// rule list
			String[] RULE_LIST = {
					"Regra 14-3 - Init. Taxes Retention",
					"Regra 14-3A", "Regra 14-3E" };
			int[] AGENDA_COUNT = { 1, 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testAccount10() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			this.assertAccount(this.loadBGHTestFile("bgh/r14_3/ACCOUNT10.BGH"));
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
			assertEquals(1, this.results.size());
			assertEquals(1, counters.get("Data de fim do ciclo divergente").intValue());
			// rule list
			String[] RULE_LIST = {
					"Regra 14-3 - Init. Taxes Retention",
					"Regra 14-3E", "Regra 14-3B" };
			int[] AGENDA_COUNT = { 1, 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testAccount11() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			this.assertAccount(this.loadBGHTestFile("bgh/r14_3/ACCOUNT11.BGH"));
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
			assertEquals(1, this.results.size());
			assertEquals(1, counters.get("Valores divergentes").intValue());
			// rule list
			String[] RULE_LIST = { "Regra 14-3 - Init. Taxes Retention", "Regra 14-3E"};
			int[] AGENDA_COUNT = { 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testAccount12() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			this.assertAccount(this.loadBGHTestFile("bgh/r14_3/ACCOUNT12.BGH"));
			this.workingMemory.fireAllRules();
			assertEquals(0, this.results.size());
			// rule list
			String[] RULE_LIST = { "Regra 14-3 - Init. Taxes Retention", "Regra 14-3E"};
			int[] AGENDA_COUNT = { 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testAccount13() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			this.assertAccount(this.loadBGHTestFile("bgh/r14_3/ACCOUNT13.BGH"));
			this.workingMemory.fireAllRules();
			assertEquals(0, this.results.size());
			// rule list
			String[] RULE_LIST = { "Regra 14-3 - Init. Taxes Retention", "Regra 14-3E"};
			int[] AGENDA_COUNT = { 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Checking for tax retention situations.
	 */
	public void testAccount14() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r14_3/ACCOUNT14.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			assertEquals(0, this.results.size());
			// rule list
			String[] RULE_LIST = {
					"Regra 14-3 - Init. Taxes Retention",
					"Regra 14-3 - Saving Taxes Retention",
					"Regra 14-3E"};
			int[] AGENDA_COUNT = { 1, 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}
