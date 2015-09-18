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
import br.com.auster.common.sql.SQLConnectionManager;
import br.com.auster.common.xml.DOMUtils;
import br.com.auster.om.invoice.Account;
import br.com.auster.tim.billcheckout.util.FreeUnitHelper;
import br.com.auster.tim.billcheckout.util.XStreamLoader;



/**
 * @author framos
 * @version $Id$
 *
 */
public class Rule03_6_Test extends BaseRuleTest {


	private String[] RULES = { "src/main/conf/rules/R03.x-INIT.drl",
							   "src/main/conf/rules/R03.6-fu-hierarchy.drl"};



    protected void createGlobals() {
    	super.createGlobals();

    	try {
			Class.forName("org.apache.commons.dbcp.PoolingDriver");
			Class.forName("oracle.jdbc.driver.OracleDriver");
			SQLConnectionManager.init(DOMUtils.openDocument("bgh/guiding/pool.xml", false));

			Element conf = DOMUtils.openDocument("bgh/guiding/caches.xml",false);

			FreeUnitHelper fuHelper = new FreeUnitHelper();
			fuHelper.configure(conf);
			this.workingMemory.setGlobal("fuHelper", fuHelper);

    	} catch (Exception e) { e.printStackTrace(); }

    }

	/**
	 * This is the original ACCOUNT2.BGH file
	 */
	public void testAccount2() {
		try {
			System.setProperty("cglib.disabled", "true");
			System.setProperty(XStreamLoader.ROOT_DIR_SYSPROP, "src/test/resources/bgh/r03_x/xstream");
			System.setProperty(XStreamLoader.FILENAME_SYSPROP, "credcorp.xml.gz");
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r03_x/ACCOUNT2.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = {
					"Regra R03.x - Loading FU Information"//,
					//"Regra R03.6 - Validating FreeUnits Hierarchy"
					};
			int[] AGENDA_COUNT = { 1/*, 0 */ };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);

			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * This is the original ACCOUNT2.BGH file, where one of the shortDescriptions was modified to count as one match
	 *
	 */
	public void testAccount2A() {
		try {
			System.setProperty("cglib.disabled", "true");
			System.setProperty(XStreamLoader.ROOT_DIR_SYSPROP, "src/test/resources/bgh/r03_x/xstream");
			System.setProperty(XStreamLoader.FILENAME_SYSPROP, "credcorp.xml.gz");
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r03_x/ACCOUNT2A.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = {
					"Regra R03.x - Loading FU Information",
					"Regra R03.6 - Validating FreeUnits Hierarchy"
					};
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
	 * This is the original ACCOUNT12.BGH file
	 *
	 */
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
					"Regra R03.6 - Validating FreeUnits Hierarchy"
					};
			int[] AGENDA_COUNT = { 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);

			// running over results list to check if consequences where ok
			assertEquals(1, this.results.size());
			Consequence consequence = this.results.get(0);
			assertEquals("Pct. 2000 Min. Compartilhado", consequence.getAttributes().getAttributeValue1());
			assertEquals("Pct. 500 Min. Compartilhado", consequence.getAttributes().getAttributeValue3());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}


	/**
	 * This is the original ACCOUNT10.BGH file, but the short description for the second package was
	 * 		modified.
	 * So there will be no scenarios for POL1A, the match will happend but no consequence created.
	 */
	public void testAccount12C() {
		try {
			System.setProperty("cglib.disabled", "true");
			System.setProperty(XStreamLoader.ROOT_DIR_SYSPROP, "src/test/resources/bgh/r03_x/xstream");
			System.setProperty(XStreamLoader.FILENAME_SYSPROP, "credcorp.xml.gz");
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r03_x/ACCOUNT12C.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = {
					"Regra R03.x - Loading FU Information",
					"Regra R03.6 - Validating FreeUnits Hierarchy"
					};
			int[] AGENDA_COUNT = { 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);

			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

}
