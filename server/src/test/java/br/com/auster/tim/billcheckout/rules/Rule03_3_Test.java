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
import br.com.auster.tim.billcheckout.util.XStreamLoader;



/**
 * @author framos
 * @version $Id$
 *
 */
public class Rule03_3_Test extends BaseRuleTest {


	private String[] RULES = { "src/main/conf/rules/R03.x-INIT.drl",
							   "src/main/conf/rules/R03.3-fu-validation.drl"};


	/**
	 * This is the original ACCOUNT1.BGH file
	 */
	public void testAccount1() {
		try {
			System.setProperty("cglib.disabled", "true");
			System.setProperty(XStreamLoader.ROOT_DIR_SYSPROP, "src/test/resources/bgh/r03_x/xstream");
			System.setProperty(XStreamLoader.FILENAME_SYSPROP, "credcorp.xml.gz");
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r03_x/ACCOUNT1.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = {
					"Regra R03.x - Loading FU Information",
					"Regra R03.3 - Accumulating FreeUnit usage - Step1",
					"Regra R03.3 - Accumulating FreeUnit usage - Step2",
					"Regra R03.3 - Validating usage over the limits of the FU Package",
					"Regra R03.3 - Validating usage diff from reported in Invoice"};
			int[] AGENDA_COUNT = { 1, 1, 1, 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);

			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * This is the ACCOUNT1.BGH file, modified so that the usedAmount does not match the
	 * 		sum of all FU details.
	 */
	public void testAccount1A() {
		try {
			System.setProperty("cglib.disabled", "true");
			System.setProperty(XStreamLoader.ROOT_DIR_SYSPROP, "src/test/resources/bgh/r03_x/xstream");
			System.setProperty(XStreamLoader.FILENAME_SYSPROP, "credcorp.xml.gz");
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r03_x/ACCOUNT1A.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = {
					"Regra R03.x - Loading FU Information",
					"Regra R03.3 - Accumulating FreeUnit usage - Step1",
					"Regra R03.3 - Accumulating FreeUnit usage - Step2",
					"Regra R03.3 - Validating usage over the limits of the FU Package",
					"Regra R03.3 - Validating usage diff from reported in Invoice"};
			int[] AGENDA_COUNT = { 1, 1, 1, 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);

			// running over results list to check if consequences where ok
			assertEquals(1, this.results.size());
			Consequence c = this.results.iterator().next();
			assertEquals("Soma do benefício das chamadas não confere com o total utilizado presente na fatura.", c.getDescription());
			assertEquals("T Você 10 min.", c.getAttributes().getAttributeValue1());
			assertEquals("00h10m00s", c.getAttributes().getAttributeValue2());
			assertEquals("00h09m00s", c.getAttributes().getAttributeValue3());
			assertEquals("00h10m00s", c.getAttributes().getAttributeValue4());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * This is the ACCOUNT1.BGH file, modified so that the usedAmount does not match the
	 * 		sum of all FU details, and also it is over the FU package limit.
	 */
	public void testAccount1B() {
		try {
			System.setProperty("cglib.disabled", "true");
			System.setProperty(XStreamLoader.ROOT_DIR_SYSPROP, "src/test/resources/bgh/r03_x/xstream");
			System.setProperty(XStreamLoader.FILENAME_SYSPROP, "credcorp.xml.gz");
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r03_x/ACCOUNT1B.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = {
					"Regra R03.x - Loading FU Information",
					"Regra R03.3 - Accumulating FreeUnit usage - Step1",
					"Regra R03.3 - Accumulating FreeUnit usage - Step2",
					"Regra R03.3 - Validating usage over the limits of the FU Package",
					"Regra R03.3 - Validating usage diff from reported in Invoice"};
			int[] AGENDA_COUNT = { 1, 1, 1, 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);

			// running over results list to check if consequences where ok
			assertEquals(2, this.results.size());
			Consequence c = this.results.get(0);
			assertEquals("Soma do benefício das chamadas não confere com o total utilizado presente na fatura.", c.getDescription());
			assertEquals("T Você 10 min.", c.getAttributes().getAttributeValue1());
			assertEquals("00h10m00s", c.getAttributes().getAttributeValue2());
			assertEquals("00h10m00s", c.getAttributes().getAttributeValue3());
			assertEquals("00h10m06s", c.getAttributes().getAttributeValue4());
			c = this.results.get(1);
			assertEquals("Benefício de franquia atribuído além do limite do pacote.", c.getDescription());
			assertEquals("T Você 10 min.", c.getAttributes().getAttributeValue1());
			assertEquals("00h10m00s", c.getAttributes().getAttributeValue2());
			assertEquals("00h10m00s", c.getAttributes().getAttributeValue3());
			assertEquals("00h10m06s", c.getAttributes().getAttributeValue4());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * This is the ACCOUNT1.BGH file, modified so that the usedAmount is over the FU package limit.
	 */
	public void testAccount1C() {
		try {
			System.setProperty("cglib.disabled", "true");
			System.setProperty(XStreamLoader.ROOT_DIR_SYSPROP, "src/test/resources/bgh/r03_x/xstream");
			System.setProperty(XStreamLoader.FILENAME_SYSPROP, "credcorp.xml.gz");
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r03_x/ACCOUNT1C.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = {
					"Regra R03.x - Loading FU Information",
					"Regra R03.3 - Accumulating FreeUnit usage - Step1",
					"Regra R03.3 - Accumulating FreeUnit usage - Step2",
					"Regra R03.3 - Validating usage over the limits of the FU Package",
					"Regra R03.3 - Validating usage diff from reported in Invoice"};
			int[] AGENDA_COUNT = { 1, 1, 1, 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);

			// running over results list to check if consequences where ok
			assertEquals(1, this.results.size());
			Consequence c = this.results.get(0);
			assertEquals("Benefício de franquia atribuído além do limite do pacote.", c.getDescription());
			assertEquals("T Você 10 min.", c.getAttributes().getAttributeValue1());
			assertEquals("00h10m00s", c.getAttributes().getAttributeValue2());
			assertEquals("00h10m06s", c.getAttributes().getAttributeValue3());
			assertEquals("00h10m06s", c.getAttributes().getAttributeValue4());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}


	/**
	 * From here on we are handling with a LA with 3 accounts:
	 *
	 *  * Level 10 : 6.316195		(ACCOUNT10.BGH)
	 *  * Level 20 : 6.316195.10	(ACCOUNT11.BGH)
	 *  * Level 20 : 6.316195.11	(ACCOUNT12.BGH)
	 *
	 *  They share 2 FU packages, assigned in Level 10, and the 6.316195.11 account
	 *  	has other two FU packages directly assigned to it.
	 *
	 *  There are three distinct scenarios, distinguishable by the name of the BGH ACCOUNT files:
	 *
	 *  - no letter: the original files. No errors.
	 *
	 *  - finished with 'A' : Added two TORPEDO details using FU Package 'Pct. 500 Min. Compartilhado' at account 6.316195.10.
	 *  	We also updated the usedCount in Level 10, so the only error is that the FU package was used over its limit.
	 *
	 *  - finished with 'B' : Updated the usedCount in Level 10, so we can simulate an error whe the sum of all FU details
	 *  	do not match the summarized info in Level 10.
	 */

	public void testAccount10() {
		try {
			System.setProperty("cglib.disabled", "true");
			System.setProperty(XStreamLoader.ROOT_DIR_SYSPROP, "src/test/resources/bgh/r03_x/xstream");
			System.setProperty(XStreamLoader.FILENAME_SYSPROP, "credcorp.xml.gz");
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r03_x/ACCOUNT10.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = {
					"Regra R03.x - Loading FU Information",
					"Regra R03.3 - Accumulating FreeUnit usage - Step1",
					"Regra R03.3 - Accumulating FreeUnit usage - Step2",
					"Regra R03.3 - Validating usage over the limits of the FU Package",
					"Regra R03.3 - Validating usage diff from reported in Invoice"};
			int[] AGENDA_COUNT = { 1, 3, 3, 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);

			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testAccount10A() {
		try {
			System.setProperty("cglib.disabled", "true");
			System.setProperty(XStreamLoader.ROOT_DIR_SYSPROP, "src/test/resources/bgh/r03_x/xstream");
			System.setProperty(XStreamLoader.FILENAME_SYSPROP, "credcorp.xml.gz");
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r03_x/ACCOUNT10A.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = {
					"Regra R03.x - Loading FU Information",
					"Regra R03.3 - Accumulating FreeUnit usage - Step1",
					"Regra R03.3 - Accumulating FreeUnit usage - Step2",
					"Regra R03.3 - Validating usage over the limits of the FU Package",
					"Regra R03.3 - Validating usage diff from reported in Invoice"};
			int[] AGENDA_COUNT = { 1, 3, 4, 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);

			// running over results list to check if consequences where ok
			assertEquals(1, this.results.size());
			Consequence c = this.results.get(0);
			assertEquals("Benefício de franquia atribuído além do limite do pacote.", c.getDescription());
			assertEquals("Pct. 500 Min. Compartilhado", c.getAttributes().getAttributeValue1());
			assertEquals("25 unidade(s)", c.getAttributes().getAttributeValue2());
			assertEquals("26 unidade(s)", c.getAttributes().getAttributeValue3());
			assertEquals("26 unidade(s)", c.getAttributes().getAttributeValue4());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testAccount10B() {
		try {
			System.setProperty("cglib.disabled", "true");
			System.setProperty(XStreamLoader.ROOT_DIR_SYSPROP, "src/test/resources/bgh/r03_x/xstream");
			System.setProperty(XStreamLoader.FILENAME_SYSPROP, "credcorp.xml.gz");
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r03_x/ACCOUNT10B.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = {
					"Regra R03.x - Loading FU Information",
					"Regra R03.3 - Accumulating FreeUnit usage - Step1",
					"Regra R03.3 - Accumulating FreeUnit usage - Step2",
					"Regra R03.3 - Validating usage over the limits of the FU Package",
					"Regra R03.3 - Validating usage diff from reported in Invoice"};
			int[] AGENDA_COUNT = { 1, 3, 3, 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);

			// running over results list to check if consequences where ok
			assertEquals(1, this.results.size());
			Consequence c = this.results.get(0);
			assertEquals("Soma do benefício das chamadas não confere com o total utilizado presente na fatura.", c.getDescription());
			assertEquals("Pct. 500 Min. Compartilhado", c.getAttributes().getAttributeValue1());
			assertEquals("25 unidade(s)", c.getAttributes().getAttributeValue2());
			assertEquals("23 unidade(s)", c.getAttributes().getAttributeValue3());
			assertEquals("24 unidade(s)", c.getAttributes().getAttributeValue4());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testAccount11() {
		try {
			System.setProperty("cglib.disabled", "true");
			System.setProperty(XStreamLoader.ROOT_DIR_SYSPROP, "src/test/resources/bgh/r03_x/xstream");
			System.setProperty(XStreamLoader.FILENAME_SYSPROP, "credcorp.xml.gz");
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r03_x/ACCOUNT11.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = { "Regra R03.x - Loading FU Information" };
			int[] AGENDA_COUNT = { 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);

			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testAccount11A() {
		try {
			System.setProperty("cglib.disabled", "true");
			System.setProperty(XStreamLoader.ROOT_DIR_SYSPROP, "src/test/resources/bgh/r03_x/xstream");
			System.setProperty(XStreamLoader.FILENAME_SYSPROP, "credcorp.xml.gz");
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r03_x/ACCOUNT11A.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = { "Regra R03.x - Loading FU Information",
					"Regra R03.3 - Accumulating FreeUnit usage - Step1",
					"Regra R03.3 - Accumulating FreeUnit usage - Step2" };
			int[] AGENDA_COUNT = { 1, 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);

			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Its the same as testAccount11()
	 */
	public void testAccount11B() {	}


	public void testAccount12() {
		try {
			System.setProperty("cglib.disabled", "true");
			System.setProperty(XStreamLoader.ROOT_DIR_SYSPROP, "src/test/resources/bgh/r03_x/xstream");
			System.setProperty(XStreamLoader.FILENAME_SYSPROP, "credcorp.xml.gz");
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r03_x/ACCOUNT12.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = {
					"Regra R03.x - Loading FU Information",
					"Regra R03.3 - Accumulating FreeUnit usage - Step1",
					"Regra R03.3 - Accumulating FreeUnit usage - Step2",
					"Regra R03.3 - Validating usage over the limits of the FU Package",
					"Regra R03.3 - Validating usage diff from reported in Invoice"};
			int[] AGENDA_COUNT = { 1, 3, 3, 2, 2 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);

			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Its the same as testAccount12()
	 */
	public void testAccount12A() {	}

	/**
	 * Its the same as testAccount12()
	 */
	public void testAccount12B() {	}

	/**
	 * ticket #275
	 */
	public void testAccount13() {
		try {
			System.setProperty("cglib.disabled", "true");
			System.setProperty(XStreamLoader.ROOT_DIR_SYSPROP, "src/test/resources/bgh/r03_x/xstream");
			System.setProperty(XStreamLoader.FILENAME_SYSPROP, "credcorp.xml.gz");
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r03_x/ACCOUNT13.BGH");
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
	 * Also from ticket #275
	 *
	 * NOTE: The information in credcorp.xml.gz for this account was generated with the original version
	 *    of the BGH file. But since its too large and raises OutOfMemory when running JUnit, we removed
	 *    part of the usage details from the BGH file.
	 *
	 *    If for some reason the original file is needed, check ticket #275 and the BGH will be zipped in
	 *    the attachment named 'Evidï¿½ncias_Validacao de uso de pacotes.zip'.
	 */
	public void testAccount3() {
		try {
			System.setProperty("cglib.disabled", "true");
			System.setProperty(XStreamLoader.ROOT_DIR_SYSPROP, "src/test/resources/bgh/r03_x/xstream");
			System.setProperty(XStreamLoader.FILENAME_SYSPROP, "credcorp.xml.gz");
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r03_x/ACCOUNT3.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = {
					"Regra R03.x - Loading FU Information",
					"Regra R03.3 - Accumulating FreeUnit usage - Step1",
					"Regra R03.3 - Accumulating FreeUnit usage - Step2",
					"Regra R03.3 - Validating usage over the limits of the FU Package",
					"Regra R03.3 - Validating usage diff from reported in Invoice"};
			int[] AGENDA_COUNT = { 1, 11, 11, 11, 11 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);

			// running over results list to check if consequences where ok
			assertEquals(2, this.results.size());
			HashMap<String, Integer> counters = new HashMap<String, Integer>();
			for (Consequence c : this.results) {
				String k = c.getDescription();
				if (! counters.containsKey(k)) {
					counters.put(k, new Integer(0));
				}
				Integer d = counters.get(k);
				counters.put(k, new Integer(d.intValue() + 1));
			}
			assertNotNull(counters.get("Benefício de franquia atribuído além do limite do pacote."));
			assertEquals(2, counters.get("Benefício de franquia atribuído além do limite do pacote.").intValue());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * ticket #383
	 * 
	 * This is the original file, we are making sure no errors exist.
	 */
	public void testAccount4() {
		try {
			System.setProperty("cglib.disabled", "true");
			System.setProperty(XStreamLoader.ROOT_DIR_SYSPROP, "src/test/resources/bgh/r03_x/xstream");
			System.setProperty(XStreamLoader.FILENAME_SYSPROP, "credcorp.xml.gz");
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r03_x/ACCOUNT4.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = {
					"Regra R03.x - Loading FU Information",
					"Regra R03.3 - Accumulating FreeUnit usage - Step1",
					"Regra R03.3 - Accumulating FreeUnit usage - Step2",
					"Regra R03.3 - Validating usage over the limits of the FU Package",
					"Regra R03.3 - Validating usage diff from reported in Invoice"};
			int[] AGENDA_COUNT = { 1, 7, 7, 6, 6 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);

			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}		
	}

	/**
	 * ticket #383
	 * 
	 * This is the original file, we are making sure no errors exist.
	 */
	public void testAccount4NOK() {
		try {
			System.setProperty("cglib.disabled", "true");
			System.setProperty(XStreamLoader.ROOT_DIR_SYSPROP, "src/test/resources/bgh/r03_x/xstream");
			System.setProperty(XStreamLoader.FILENAME_SYSPROP, "credcorp.xml.gz");
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r03_x/ACCOUNT4.NOK.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = {
					"Regra R03.x - Loading FU Information",
					"Regra R03.3 - Accumulating FreeUnit usage - Step1",
					"Regra R03.3 - Accumulating FreeUnit usage - Step2",
					"Regra R03.3 - Validating usage over the limits of the FU Package",
					"Regra R03.3 - Validating usage diff from reported in Invoice"};
			int[] AGENDA_COUNT = { 1, 7, 7, 6, 6 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);

			// running over results list to check if consequences where ok
			assertEquals(1, this.results.size());
			Consequence c = this.results.get(0);
			assertEquals("Soma do benefício das chamadas não confere com o total utilizado presente na fatura.", c.getDescription());
			assertEquals("Plano TIM Empresa Mundi 200", c.getAttributes().getAttributeValue1());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}		
	}	
}
