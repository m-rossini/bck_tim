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

import br.com.auster.common.sql.SQLConnectionManager;
import br.com.auster.common.xml.DOMUtils;
import br.com.auster.om.invoice.Account;
import br.com.auster.tim.billcheckout.param.CBCFContractDAO;
import br.com.auster.tim.billcheckout.param.OCCContractBreakCache;



/**
 * @author framos
 * @version $Id$
 *
 */
public class Rule24_1_Test extends BaseRuleTest {


	private String[] RULES = { "src/main/conf/rules/R24.1-cancelation-charges.drl"};


	protected void createGlobals() {
		super.createGlobals();

		try {
			Class.forName("org.apache.commons.dbcp.PoolingDriver");
			Class.forName("oracle.jdbc.driver.OracleDriver");
			SQLConnectionManager.init(DOMUtils.openDocument("bgh/guiding/pool.xml", false));

			Element conf = DOMUtils.openDocument("bgh/guiding/caches.xml",false);

			OCCContractBreakCache OCCDescriptions = new OCCContractBreakCache();
			OCCDescriptions.configure(conf);
			this.workingMemory.setGlobal("OCCDescriptions", OCCDescriptions);

			CBCFContractDAO CBCFInterface = new CBCFContractDAO();
			CBCFInterface.configure(conf);
			this.workingMemory.setGlobal("CBCFInterface", CBCFInterface);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	/**
	 * ACCOUNT1.BGH is an original file and has no errors
	 */
	public void testAccount1() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r24_1/ACCOUNT1.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = {
					"R24.1 - Load CBCF Info for Invoice",
					"R24.1 - Checking Contract Break OCCs",
					"R24.1 - Checking Contract Break Amounts"};
			int[] AGENDA_COUNT = { 1,  0,  1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);

			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * ACCOUNT1.BGH is an original file and has no errors.
	 *
	 * The customerID was modified to simulate a ContaFixa customer
	 */
	public void testAccount1A() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r24_1/ACCOUNT1A.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = {
					"R24.1 - Load CBCF Info for Invoice",
					"R24.1 - Checking Contract Break OCCs",
					"R24.1 - Checking Contract Break Amounts"};
			int[] AGENDA_COUNT = { 1,  0,  1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);

			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * ACCOUNT2.BGH is an original file but no data was loaded into the mock tables.
	 *
	 * It works the same for normal and ContaFixa customers!
	 */
	public void testAccount2() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r24_1/ACCOUNT2.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			// Third rule is not here since it is neved added
			String[] RULE_LIST = {
					"R24.1 - Load CBCF Info for Invoice",
					"R24.1 - Checking Contract Break OCCs"};
			int[] AGENDA_COUNT = { 1,  1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);

			// running over results list to check if consequences where ok
			assertEquals(1, this.results.size());
			assertEquals("Quebra de fidelização cobrada indevidamente.", this.results.get(0).getDescription());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * ACCOUNT3.BGH is an original file but its OCC description has type diff. from 3
	 *
	 * It works the same for normal and ContaFixa customers!
	 */
	public void testAccount3() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r24_1/ACCOUNT3.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			// Second and third rules are not here since they are neved added
			String[] RULE_LIST = {
					"R24.1 - Load CBCF Info for Invoice"};
			int[] AGENDA_COUNT = { 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);

			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * ACCOUNT4.BGH is an original file but its amount in the database was modified so that it will raise a Consequence
	 */
	public void testAccount4() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r24_1/ACCOUNT4.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = {
					"R24.1 - Load CBCF Info for Invoice",
					"R24.1 - Checking Contract Break OCCs",
					"R24.1 - Checking Contract Break Amounts"};
			int[] AGENDA_COUNT = { 1,  0,  1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);

			// running over results list to check if consequences where ok
			assertEquals(1, this.results.size());
			assertEquals("Valor da quebra de fidelização não confere.", this.results.get(0).getDescription());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * ACCOUNT4.BGH is an original file but its amount in the database was modified so that it will raise a Consequence
	 *
	 * The customerID was modified to simulate a ContaFixa customer
	 */
	public void testAccount4A() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r24_1/ACCOUNT4A.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = {
					"R24.1 - Load CBCF Info for Invoice",
					"R24.1 - Checking Contract Break OCCs",
					"R24.1 - Checking Contract Break Amounts"};
			int[] AGENDA_COUNT = { 1,  0,  1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);

			// running over results list to check if consequences where ok
			assertEquals(1, this.results.size());
			assertEquals("Valor da quebra de fidelização não confere.", this.results.get(0).getDescription());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * ACCOUNT5.BGH is an original file but its data in the database has diff. dates
	 * It will behave just like ACCOUNT2.BGH
	 */
	public void testAccount5() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r24_1/ACCOUNT5.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			// Third rule is not here since it is neved added
			String[] RULE_LIST = {
					"R24.1 - Load CBCF Info for Invoice",
					"R24.1 - Checking Contract Break OCCs"};
			int[] AGENDA_COUNT = { 1,  1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);

			// running over results list to check if consequences where ok
			assertEquals(1, this.results.size());
			assertEquals("Quebra de fidelização cobrada indevidamente.", this.results.get(0).getDescription());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * ACCOUNT5.BGH is an original file but its data in the database has diff. dates
	 * It will behave just like ACCOUNT2.BGH
	 *
	 * The customerID was modified to simulate a ContaFixa customer
	 */
	public void testAccount5A() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r24_1/ACCOUNT5A.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			// Third rule is not here since it is neved added
			String[] RULE_LIST = {
					"R24.1 - Load CBCF Info for Invoice",
					"R24.1 - Checking Contract Break OCCs"};
			int[] AGENDA_COUNT = { 1,  1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);

			// running over results list to check if consequences where ok
			assertEquals(1, this.results.size());
			assertEquals("Quebra de fidelização cobrada indevidamente.", this.results.get(0).getDescription());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}


	/**
	 * ACCOUNT6.BGH - Ticket #232
	 *
	 * Checking the eventCount difference
	 */
	public void testAccount6() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r24_1/ACCOUNT6.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			// Third rule is not here since it is neved added
			String[] RULE_LIST = {
					"R24.1 - Load CBCF Info for Invoice",
					"R24.1 - Checking Contract Break OCCs",
					"R24.1 - Checking Contract Break Amounts"};
			int[] AGENDA_COUNT = { 1,  0, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);

			// running over results list to check if consequences where ok
			assertEquals(1, this.results.size());
			assertEquals("Quantidade de eventos da quebra de fidelização não confere.", this.results.get(0).getDescription());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * ACCOUNT6.BGH - Ticket #231
	 *
	 * Checking the eventCount difference
	 */
	public void testAccount7() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r24_1/ACCOUNT7.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			// Third rule is not here since it is neved added
			String[] RULE_LIST = {
					"R24.1 - Load CBCF Info for Invoice",
					"R24.1 - Checking Contract Break OCCs",
					"R24.1 - Checking Contract Break Amounts"};
			int[] AGENDA_COUNT = { 1,  0, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);

			// running over results list to check if consequences where ok
			assertEquals(1, this.results.size());
			assertEquals("Valor da quebra de fidelização não confere.", this.results.get(0).getDescription());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

}
