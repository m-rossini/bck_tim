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
public class Rule12_1_Test extends BaseRuleTest {


	private static String[] RULES_FILES = { "src/main/conf/rules/R12.1-sections-totals-validation.drl" };



	/**
	 * Modified to meet the conditions below.
	 */
	public void testAccount1_R12_1() {
		try {
			// firing rules
			this.startupRuleEngine(RULES_FILES);
			Account acc = this.loadBGHTestFile("bgh/r12_1/ACCOUNT1.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashMap<String, String> counters = new HashMap<String, String>();
			for (Consequence c : this.results) {
				if ((c.getAttributes().getAttributeName3() != null) &&
					 c.getAttributes().getAttributeName3().equals("Nome da Seção"))
				counters.put( c.getAttributes().getAttributeValue5(), c.getAttributes().getAttributeValue2());
			}
			// We are trying to find R12.1 consequences only.
			// This filter was implemented with the conditional IF in the FOR loop above
			assertEquals(0, counters.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Modified to meet the conditions below.
	 */
	public void testAccount2_R12_1() {
		try {
			// firing rules
			this.startupRuleEngine(RULES_FILES);
			Account acc = this.loadBGHTestFile("bgh/r12_1/ACCOUNT2.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashMap<String, String> counters = new HashMap<String, String>();
			for (Consequence c : this.results) {
				if (c.getAttributes().getAttributeName3().equals("Nome da Seção"))
					counters.put( c.getAttributes().getAttributeValue3(), c.getDescription());
			}
			// The number of consequences might be different, but for this testcase we are validating only R12.1 consequences
			// This filter was implemented with the conditional IF in the FOR loop above
			assertEquals(4, counters.size());
			assertTrue( counters.containsKey("Chamadas Locais para Celulares TIM"));
			assertEquals("Valor e Quantidade da Seção divergem do cálculo dos elementos dentro da mesma.", counters.get("Chamadas Locais para Celulares TIM"));
			assertTrue( counters.containsKey("Chamadas Locais para Outros Celulares"));
			assertEquals("Valor e Quantidade da Seção divergem do cálculo dos elementos dentro da mesma.", counters.get("Chamadas Locais para Outros Celulares"));
			assertTrue( counters.containsKey("Chamadas Locais para Telefones Fixos"));
			assertEquals("Quantidade de elementos da Seção diverge do cálculo da quantidade de elementos dentro da mesma.", counters.get("Chamadas Locais para Telefones Fixos"));
			assertTrue( counters.containsKey("Chamadas Longa Distância: TIM LD 41"));
			assertEquals("Valor da Seção diverge do cálculo dos valores dos elementos dentro da mesma.", counters.get("Chamadas Longa Distância: TIM LD 41"));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * From Fase2 Production Environment. Ticket #100
	 */
	public void testAccount6_R12_1() {
		try {
			// firing rules
			this.startupRuleEngine(RULES_FILES);
			Account acc = this.loadBGHTestFile("bgh/r12_1/ACCOUNT6.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashMap<String, String> counters = new HashMap<String, String>();
			for (Consequence c : this.results) {
				if ((c.getAttributes().getAttributeName3() != null) &&
					 c.getAttributes().getAttributeName3().equals("Nome da Seção"))
				counters.put( c.getAttributes().getAttributeValue5(), c.getAttributes().getAttributeValue2());
			}
			assertEquals(0, counters.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testAccount1_R12_6() {
		try {
			// firing rules
			this.startupRuleEngine(RULES_FILES);
			Account acc = this.loadBGHTestFile("bgh/r12_1/ACCOUNT1.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashMap<String, String> counters = new HashMap<String, String>();
			for (Consequence c : this.results) {
				if ((c.getAttributes().getAttributeName5() != null) &&
					 c.getAttributes().getAttributeName5().equals("Seção")) {

					counters.put( c.getAttributes().getAttributeValue5(), c.getAttributes().getAttributeValue2());
					assertEquals("Possível erro de arredondamento.", c.getAttributes().getAttributeValue6());
				}
			}
			// We are trying to find R12.6 consequences only.
			// This filter was implemented with the conditional IF in the FOR loop above
			assertEquals(1, counters.size());
			assertTrue( counters.containsKey(""));
			assertEquals("1214.00", counters.get("").replaceAll(",", "."));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Modified to meet the conditions below.
	 */
	public void testAccount3_R12_6() {
		try {
			// firing rules
			this.startupRuleEngine(RULES_FILES);
			Account acc = this.loadBGHTestFile("bgh/r12_1/ACCOUNT3.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashMap<String, String> counters = new HashMap<String, String>();
			for (Consequence c : this.results) {
				if ((c.getAttributes().getAttributeName5() != null) &&
					 c.getAttributes().getAttributeName5().equals("Seção"))
					counters.put( c.getAttributes().getAttributeValue5(), c.getAttributes().getAttributeValue2());
			}
			// The number of consequences might be different, but for this testcase we are validating only R12.6 consequences
			// This filter was implemented with the conditional IF in the FOR loop above
			assertEquals(1, counters.size());
			assertTrue( counters.containsKey("ContractServices"));
			assertEquals("7.97", counters.get("ContractServices").replaceAll(",", "."));
			// This change did not effect the NF total since the section total was not modified
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Modified to meet the conditions below.
	 */
	public void testAccount4_R12_6() {
		try {
			// firing rules
			this.startupRuleEngine(RULES_FILES);
			Account acc = this.loadBGHTestFile("bgh/r12_1/ACCOUNT4.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashMap<String, String> counters = new HashMap<String, String>();
			for (Consequence c : this.results) {
				if ((c.getAttributes().getAttributeName5() != null) &&
					 c.getAttributes().getAttributeName5().equals("Seção"))
					counters.put( c.getAttributes().getAttributeValue5(), c.getAttributes().getAttributeValue2());
			}
			// The number of consequences might be different, but for this testcase we are validating only R12.6 consequences
			// This filter was implemented with the conditional IF in the FOR loop above
			assertEquals(2, counters.size());
			assertTrue( counters.containsKey("HomeCallsGroups"));
			assertEquals("7.00", counters.get("HomeCallsGroups").replaceAll(",", "."));
			// This change effected the NF total since the section total was modified
			assertTrue( counters.containsKey(""));
			assertEquals("55.75", counters.get("").replaceAll(",", "."));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Modified to meet the conditions below.
	 */
	public void testAccount5_R12_6() {
		try {
			// firing rules
			this.startupRuleEngine(RULES_FILES);
			Account acc = this.loadBGHTestFile("bgh/r12_1/ACCOUNT5.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashMap<String, String> counters = new HashMap<String, String>();
			for (Consequence c : this.results) {
				if ((c.getAttributes().getAttributeName5() != null) &&
					 c.getAttributes().getAttributeName5().equals("Seção"))
					counters.put( c.getAttributes().getAttributeValue5(), c.getAttributes().getAttributeValue2());
			}
			// The number of consequences might be different, but for this testcase we are validating only R12.6 consequences
			// This filter was implemented with the conditional IF in the FOR loop above
			assertEquals(2, counters.size());
			assertTrue( counters.containsKey("ContractServices"));
			assertEquals("7.97", counters.get("ContractServices").replaceAll(",", "."));
			// OCC change will not trigger a consequence for such section, since its not built from BGH. But it
			//   will affect the NF total validation
			assertTrue( counters.containsKey(""));
			assertEquals("778.49", counters.get("").replaceAll(",", "."));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Modified to meet the conditions below.
	 */
	public void testAccount1_R12_4() {
		try {
			// firing rules
			this.startupRuleEngine(RULES_FILES);
			Account acc = this.loadBGHTestFile("bgh/r12_1/ACCOUNT1.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashMap<String, String> counters = new HashMap<String, String>();
			for (Consequence c : this.results) {
				if (c.getAttributes().getAttributeName2().equals("Valor Usage Groups"))
					counters.put( c.getAttributes().getAttributeValue1(), c.getAttributes().getAttributeValue2());
			}
			// The number of consequences might be different, but for this testcase we are validating only R12.4 consequences
			// This filter was implemented with the conditional IF in the FOR loop above
			assertEquals(0, counters.size());
//			assertTrue( counters.containsKey("6331625"));
//			assertEquals("770.55", counters.get("6331625").replaceAll(",", "."));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Modified to meet the conditions below.
	 */
	public void testAccount2_R12_4() {
		try {
			// firing rules
			this.startupRuleEngine(RULES_FILES);
			Account acc = this.loadBGHTestFile("bgh/r12_1/ACCOUNT2.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashMap<String, String> counters = new HashMap<String, String>();
			for (Consequence c : this.results) {
				if (c.getAttributes().getAttributeName3().equals("Valor Total da Seção"))
					counters.put( c.getAttributes().getAttributeValue2(), c.getAttributes().getAttributeValue3());
			}
			// The number of consequences might be different, but for this testcase we are validating only R12.1 consequences
			// This filter was implemented with the conditional IF in the FOR loop above
			assertEquals(1, counters.size());
			assertTrue( counters.containsKey("6331625"));
			assertEquals("770.55", counters.get("6331625").replaceAll(",", "."));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Modified to meet the conditions below.
	 */
	public void testAccount4_R12_4() {
		try {
			// firing rules
			this.startupRuleEngine(RULES_FILES);
			Account acc = this.loadBGHTestFile("bgh/r12_1/ACCOUNT4.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashMap<String, String> counters = new HashMap<String, String>();
			for (Consequence c : this.results) {
				if (c.getAttributes().getAttributeName2().equals("Valor Usage Groups"))
					counters.put( c.getAttributes().getAttributeValue1(), c.getAttributes().getAttributeValue2());
			}
			// The number of consequences might be different, but for this testcase we are validating only R12.1 consequences
			// This filter was implemented with the conditional IF in the FOR loop above
			assertEquals(0, counters.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Modified to meet the conditions below.
	 */
	public void testAccount1_R12_5_CONTRACT() {
		try {
			// firing rules
			this.startupRuleEngine(RULES_FILES);
			Account acc = this.loadBGHTestFile("bgh/r12_1/ACCOUNT1.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashMap<String, String> counters = new HashMap<String, String>();
			for (Consequence c : this.results) {
				if (c.getDescription().equals("Seção de sumário de contrato diverge em valores da soma de serviços."))
					counters.put( c.getAttributes().getAttributeValue1(), c.getAttributes().getAttributeValue2());
			}
			// The number of consequences might be different, but for this testcase we are validating only R12.5 (contracts only) consequences
			// This filter was implemented with the conditional IF in the FOR loop above
			assertEquals(0, counters.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Modified to meet the conditions below.
	 */
	public void testAccount2_R12_5_CONTRACT() {
		try {
			// firing rules
			this.startupRuleEngine(RULES_FILES);
			Account acc = this.loadBGHTestFile("bgh/r12_1/ACCOUNT2.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashMap<String, String> counters = new HashMap<String, String>();
			for (Consequence c : this.results) {
				if (c.getDescription().equals("Seção de sumário de contrato diverge em valores da soma de serviços."))
					counters.put( c.getAttributes().getAttributeValue1(), c.getAttributes().getAttributeValue2());
			}
			// The number of consequences might be different, but for this testcase we are validating only R12.5 (contracts only) consequences
			// This filter was implemented with the conditional IF in the FOR loop above
			assertEquals(0, counters.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Modified to meet the conditions below.
	 */
	public void testAccount3_R12_5_CONTRACT() {
		try {
			// firing rules
			this.startupRuleEngine(RULES_FILES);
			Account acc = this.loadBGHTestFile("bgh/r12_1/ACCOUNT3.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashMap<String, String> counters = new HashMap<String, String>();
			for (Consequence c : this.results) {
				if (c.getDescription().equals("Seção de sumário de contrato diverge em valores da soma de serviços."))
					counters.put( c.getAttributes().getAttributeValue2(), c.getAttributes().getAttributeValue3());
			}
			// The number of consequences might be different, but for this testcase we are validating only R12.5 (contracts only) consequences
			// This filter was implemented with the conditional IF in the FOR loop above
			assertEquals(1, counters.size());
			assertTrue( counters.containsKey("6331625"));
			assertEquals("65.58", counters.get("6331625").replaceAll(",", "."));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Modified to meet the conditions below.
	 */
	public void testAccount1_R12_5_INVOICE() {
		try {
			// firing rules
			this.startupRuleEngine(RULES_FILES);
			Account acc = this.loadBGHTestFile("bgh/r12_1/ACCOUNT1.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashMap<String, String> counters = new HashMap<String, String>();
			for (Consequence c : this.results) {
				if (c.getDescription().equals("Valor total da fatura diverge da soma das seções.")) {
					counters.put( c.getAttributes().getAttributeValue2(), c.getAttributes().getAttributeValue3());
					assertEquals("Possível erro de arredondamento.", c.getAttributes().getAttributeValue6());
				}
			}
			// The number of consequences might be different, but for this testcase we are validating only R12.5 (contracts only) consequences
			// This filter was implemented with the conditional IF in the FOR loop above
			assertEquals(1, counters.size());
			assertTrue( counters.containsKey("n/a"));
			assertEquals("2038.46", counters.get("n/a").replaceAll(",", "."));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}


	/**
	 * Modified to meet the conditions below.
	 */
	public void testAccount4_R12_5_INVOICE() {
		try {
			// firing rules
			this.startupRuleEngine(RULES_FILES);
			Account acc = this.loadBGHTestFile("bgh/r12_1/ACCOUNT4.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashMap<String, String> counters = new HashMap<String, String>();
			for (Consequence c : this.results) {
				if (c.getDescription().equals("Valor total da fatura diverge da soma das seções."))
					counters.put( c.getAttributes().getAttributeValue2(), c.getAttributes().getAttributeValue3());
			}
			// The number of consequences might be different, but for this testcase we are validating only R12.5 (contracts only) consequences
			// This filter was implemented with the conditional IF in the FOR loop above
			assertEquals(0, counters.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Modified to meet the conditions below.
	 */
	public void testAccount5_R12_5_INVOICE() {
		try {
			// firing rules
			this.startupRuleEngine(RULES_FILES);
			Account acc = this.loadBGHTestFile("bgh/r12_1/ACCOUNT5.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashMap<String, String> counters = new HashMap<String, String>();
			for (Consequence c : this.results) {
				if (c.getDescription().equals("Valor total da fatura diverge da soma das seções.")) {
					counters.put( c.getAttributes().getAttributeValue2(), c.getAttributes().getAttributeValue3());
				}

			}
			// The number of consequences might be different, but for this testcase we are validating only R12.5 (contracts only) consequences
			// This filter was implemented with the conditional IF in the FOR loop above
			assertEquals(1, counters.size());
			assertTrue( counters.containsKey("n/a"));
			assertEquals("837.13", counters.get("n/a").replaceAll(",", "."));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * From Fase2 Production Environment. Ticket #103
	 */
	public void testAccount7_R12_5_INVOICE() {
		try {
			// firing rules
			this.startupRuleEngine(RULES_FILES);
			Account acc = this.loadBGHTestFile("bgh/r12_1/ACCOUNT7.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashMap<String, String> counters = new HashMap<String, String>();
			for (Consequence c : this.results) {
				if (c.getDescription().equals("Valor total da fatura diverge da soma das seções.")) {
					counters.put( c.getAttributes().getAttributeValue2(), c.getAttributes().getAttributeValue3());
				}
			}
			// The number of consequences might be different, but for this testcase we are validating only R12.5 (contracts only) consequences
			// This filter was implemented with the conditional IF in the FOR loop above
			assertEquals(0, counters.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * From Fase2 Production Environment. Ticket #103
	 */
	public void testAccount8_R12_5_INVOICE() {
		try {
			// firing rules
			this.startupRuleEngine(RULES_FILES);
			Account acc = this.loadBGHTestFile("bgh/r12_1/ACCOUNT8.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashMap<String, String> counters = new HashMap<String, String>();
			for (Consequence c : this.results) {
				if (c.getDescription().equals("Valor total da fatura diverge da soma das seções.")) {
					counters.put( c.getAttributes().getAttributeValue2(), c.getAttributes().getAttributeValue3());
				}
			}
			// The number of consequences might be different, but for this testcase we are validating only R12.5 (contracts only) consequences
			// This filter was implemented with the conditional IF in the FOR loop above
			assertEquals(0, counters.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Ticket #215
	 */
	public void testAccount9_R12_6_INVOICE() {
		try {
			// firing rules
			this.startupRuleEngine(RULES_FILES);
			Account acc = this.loadBGHTestFile("bgh/r12_1/ACCOUNT9.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashMap<String, String> counters = new HashMap<String, String>();
			for (Consequence c : this.results) {
				if ((c.getAttributes().getAttributeName5() != null) &&
					 c.getAttributes().getAttributeName5().equals("Seção"))
					counters.put( c.getAttributes().getAttributeValue5(), c.getAttributes().getAttributeValue2());
			}
			// The number of consequences might be different, but for this testcase we are validating only R12.6 consequences
			// This filter was implemented with the conditional IF in the FOR loop above
			assertEquals(2, counters.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}

