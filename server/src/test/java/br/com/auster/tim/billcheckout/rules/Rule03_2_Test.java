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

import br.com.auster.billcheckout.consequence.telco.TelcoConsequence;
import br.com.auster.common.sql.SQLConnectionManager;
import br.com.auster.common.xml.DOMUtils;
import br.com.auster.om.invoice.Account;
import br.com.auster.tim.billcheckout.bscs.SGTCustomCache;



/**
 * @author framos
 * @version $Id$
 *
 * For SGT table, we used the following insert:
 *
 * insert into ACC_SGT_REL_CUSTOMIZACOES values ( '5.48559.11', to_date('01/01/06', 'DD/MM/YY'), null);
 *
 */
public class Rule03_2_Test extends BaseRuleTest {


	private static String[] RULES_FILES = { "src/main/conf/rules/R03.2-benefit-without-promotion-charges.drl" };


    protected void createGlobals() {
    	super.createGlobals();
    	try {
    		Class.forName("org.apache.commons.dbcp.PoolingDriver");
    		Class.forName("oracle.jdbc.driver.OracleDriver");
    		SQLConnectionManager.init(DOMUtils.openDocument("bgh/guiding/pool.xml", false));

	    	Element arg0 = DOMUtils.openDocument("bgh/guiding/caches.xml", false);

	    	SGTCustomCache sgtCache = new SGTCustomCache();
	    	sgtCache.configure(arg0);
	    	this.workingMemory.setGlobal("sgtCache", sgtCache);

    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }




	/**
	 * This is a flat account, with subscription and no details with promotions
	 */
	public void testAccount1() {
		try {
			// firing rules
			Account acc = this.loadBGHTestFile("bgh/r03_2/ACCOUNT1.BGH");
			this.startupRuleEngine(RULES_FILES, true);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = { "Regra R03.2 - Initializing counters",
								   "Regra 03.2 - Build Consequences"};
			int[] AGENDA_COUNT = { 1, 6 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}


	/**
	 * This is a flat account, with subscription and details with promotions. So all is OK
	 */
	public void testAccount2() {
		try {
			// firing rules
			Account acc = this.loadBGHTestFile("bgh/r03_2/ACCOUNT2.BGH");
			this.startupRuleEngine(RULES_FILES, true);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = { "Regra R03.2 - Initializing counters",
								   "Regra 03.2 - Build Consequences"};
			int[] AGENDA_COUNT = { 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * This is the same as ACCOUNT2.BGH, but the subscription was removed
	 */
	public void testAccount3() {
		try {
			// firing rules
			Account acc = this.loadBGHTestFile("bgh/r03_2/ACCOUNT3.BGH");
			this.startupRuleEngine(RULES_FILES, true);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = { "Regra R03.2 - Initializing counters",
								   "Regra 03.2 - Checking Flat Accounts",
								   "Regra 03.2 - Build Consequences"};
			int[] AGENDA_COUNT = { 1, 40, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			// running over results list to check if consequences where ok
			assertEquals(1, this.results.size());
			TelcoConsequence consequence = (TelcoConsequence)this.results.iterator().next();
			assertEquals("TIM Torpedo", consequence.getAttributes().getAttributeValue5());
			assertTrue(consequence.getAttributes().getAttributeValue6().startsWith("40"));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * This is a large account, with subscription and details with promotions
	 */
	public void testAccount4() {
		try {
			// firing rules
			Account acc = this.loadBGHTestFile("bgh/r03_2/ACCOUNT4.BGH");
			this.startupRuleEngine(RULES_FILES, true);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = { "Regra R03.2 - Initializing counters",
								   "Regra 03.2 - Build Consequences"};
			int[] AGENDA_COUNT = { 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * This is a large account, without subscription. This custCode will not have a negotiated price in SGT.
	 */
	public void testAccount5() {
		try {
			// firing rules
			Account acc = this.loadBGHTestFile("bgh/r03_2/ACCOUNT5.BGH");
			this.startupRuleEngine(RULES_FILES, true);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = { "Regra R03.2 - Initializing counters",
								   "Regra 03.2 - Build Consequences"};
			int[] AGENDA_COUNT = { 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
		// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
//			Previously we had 7 errors due to LDN/LDI discounts, but these two cannot be handled by R03.2
//			TelcoConsequence consequence = (TelcoConsequence)this.results.iterator().next();
//			assertEquals("Chamadas Longa Distância: TIM LD 41", consequence.getAttributes().getAttributeValue5());
//			assertTrue(consequence.getAttributes().getAttributeValue6().startsWith("7"));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * This is a large account, without subscription. But this custCode has NULL negotiated subscription fee
	 */
	public void testAccount6() {
		try {
			// firing rules
			Account acc = this.loadBGHTestFile("bgh/r03_2/ACCOUNT6.BGH");
			this.startupRuleEngine(RULES_FILES, true);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = { "Regra R03.2 - Initializing counters",
								   "Regra 03.2 - Build Consequences"};
			int[] AGENDA_COUNT = { 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * This is a large account, same situation as testAccount6(), but one of the usage details has modified
	 * 	so that the call date is prior to the activation of the subscription fee
	 */
	public void testAccount7() {
		try {
			// firing rules
			Account acc = this.loadBGHTestFile("bgh/r03_2/ACCOUNT7.BGH");
			this.startupRuleEngine(RULES_FILES, true);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = { "Regra R03.2 - Initializing counters",
								   "Regra 03.2 - Build Consequences"};
			int[] AGENDA_COUNT = { 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
//			Previously we had 7 errors due to LDN/LDI discounts, but these two cannot be handled by R03.2
//			TelcoConsequence consequence = (TelcoConsequence)this.results.iterator().next();
//			assertEquals("Chamadas Longa Distância: TIM LD 41", consequence.getAttributes().getAttributeValue5());
//			assertTrue(consequence.getAttributes().getAttributeValue6().startsWith("1"));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

}
