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

import java.text.DecimalFormat;

import br.com.auster.billcheckout.consequence.Consequence;
import br.com.auster.om.invoice.Account;
import br.com.auster.tim.billcheckout.util.XStreamLoader;



/**
 * @author framos
 * @version $Id$
 *
 */
public class Rule04_3_Validation2Test extends BaseRuleTest {


	private String[] RULES = { "src/main/conf/rules/R04.3-credcorp-validation.drl"};

	/**
	 * Ticket #177
	 *
	 * CustCode: 7.312058.10
	 * 2x Cobrança 50$
	 *
	 * Childs:
	 * 		7.312058.10.10 -> +40$, +20$
	 *
	 */
	public void testAccount10() {
		try {
			DecimalFormat formatter = new DecimalFormat("000.00");
			System.setProperty("cglib.disabled", "true");
			System.setProperty(XStreamLoader.ROOT_DIR_SYSPROP, "src/test/resources/bgh/r04_3/credcorp");
			System.setProperty(XStreamLoader.FILENAME_SYSPROP, "credcorp.xml.gz");
			// firing rules
			this.startupRuleEngine(RULES);
			Account acc = this.loadBGHTestFile("bgh/r04_3/ACCOUNT10.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(1, this.results.size());
			Consequence consequence = this.results.iterator().next();
			assertEquals(0, formatter.parse(consequence.getAttributes().getAttributeValue1()).longValue());
			assertEquals(1, formatter.parse(consequence.getAttributes().getAttributeValue2()).longValue());
			assertEquals(100, formatter.parse(consequence.getAttributes().getAttributeValue3()).longValue());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Ticket #177
	 *
	 * CustCode: 7.312058.10.10
	 * 1x Cobrança 40$
	 * 1x Cobrança 20$
	 *
	 * Childs:
	 * 		7.312058.10.10.100000 -> -50$
	 *      7.312058.10.10.100001 ->   0$
	 *
	 */
	public void testAccount11() {
		try {
			DecimalFormat formatter = new DecimalFormat("000.00");
			System.setProperty("cglib.disabled", "true");
			System.setProperty(XStreamLoader.ROOT_DIR_SYSPROP, "src/test/resources/bgh/r04_3/credcorp");
			System.setProperty(XStreamLoader.FILENAME_SYSPROP, "credcorp.xml.gz");
			// firing rules
			this.startupRuleEngine(RULES);
			Account acc = this.loadBGHTestFile("bgh/r04_3/ACCOUNT11.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(1, this.results.size());
			Consequence consequence = this.results.iterator().next();
			assertEquals(-50, formatter.parse(consequence.getAttributes().getAttributeValue1()).longValue());
			assertEquals(1, formatter.parse(consequence.getAttributes().getAttributeValue2()).longValue());
			assertEquals(60, formatter.parse(consequence.getAttributes().getAttributeValue3()).longValue());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Ticket #177
	 *
	 * CustCode: 7.312058
	 * Cobrança 0$
	 *
	 * Childs:
	 * 		7.312058.10 -> +100$
	 *
	 */
	public void testAccount12() {
		try {
			System.setProperty("cglib.disabled", "true");
			System.setProperty(XStreamLoader.ROOT_DIR_SYSPROP, "src/test/resources/bgh/r04_3/credcorp");
			System.setProperty(XStreamLoader.FILENAME_SYSPROP, "credcorp.xml.gz");
			// firing rules
			this.startupRuleEngine(RULES);
			Account acc = this.loadBGHTestFile("bgh/r04_3/ACCOUNT12.BGH");
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
	 * Ticket #177
	 *
	 * CustCode: 7.312058.10.10.100000
	 * Cobrança 0$
	 *
	 * Childs:
	 * 		No Childs
	 *
	 */
	public void testAccount13() {
		try {
			System.setProperty("cglib.disabled", "true");
			System.setProperty(XStreamLoader.ROOT_DIR_SYSPROP, "src/test/resources/bgh/r04_3/credcorp");
			System.setProperty(XStreamLoader.FILENAME_SYSPROP, "credcorp.xml.gz");
			// firing rules
			this.startupRuleEngine(RULES);
			Account acc = this.loadBGHTestFile("bgh/r04_3/ACCOUNT13.BGH");
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
	 * Ticket #177
	 *
	 * CustCode: 7.312058.10.10.100001
	 * Cobrança 0$
	 *
	 * Childs:
	 * 		No Childs
	 *
	 */
	public void testAccount14() {
		try {
			System.setProperty("cglib.disabled", "true");
			System.setProperty(XStreamLoader.ROOT_DIR_SYSPROP, "src/test/resources/bgh/r04_3/credcorp");
			System.setProperty(XStreamLoader.FILENAME_SYSPROP, "credcorp.xml.gz");
			// firing rules
			this.startupRuleEngine(RULES);
			Account acc = this.loadBGHTestFile("bgh/r04_3/ACCOUNT14.BGH");
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
	 * Ticket #177
	 *
	 * CustCode: 7.312087.10
	 * 1x Cobrança 39$
	 * 1x Cobrança 28$
	 *
	 * Childs:
	 * 		No Childs
	 *
	 */
	public void testAccount15() {
		try {
			DecimalFormat formatter = new DecimalFormat("000.00");
			System.setProperty("cglib.disabled", "true");
			System.setProperty(XStreamLoader.ROOT_DIR_SYSPROP, "src/test/resources/bgh/r04_3/credcorp");
			System.setProperty(XStreamLoader.FILENAME_SYSPROP, "credcorp.xml.gz");
			// firing rules
			this.startupRuleEngine(RULES);
			Account acc = this.loadBGHTestFile("bgh/r04_3/ACCOUNT15.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(1, this.results.size());
			Consequence consequence = this.results.iterator().next();
			assertEquals(0, formatter.parse(consequence.getAttributes().getAttributeValue1()).longValue());
			assertEquals(0, formatter.parse(consequence.getAttributes().getAttributeValue2()).longValue());
			assertEquals(67, formatter.parse(consequence.getAttributes().getAttributeValue3()).longValue());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Ticket #177
	 *
	 * CustCode: 7.312087
	 * Cobrança 0$
	 *
	 * Childs:
	 * 	 7.312087.10 -> +67$
	 *
	 */
	public void testAccount16() {
		try {
			System.setProperty("cglib.disabled", "true");
			System.setProperty(XStreamLoader.ROOT_DIR_SYSPROP, "src/test/resources/bgh/r04_3/credcorp");
			System.setProperty(XStreamLoader.FILENAME_SYSPROP, "credcorp.xml.gz");
			// firing rules
			this.startupRuleEngine(RULES);
			Account acc = this.loadBGHTestFile("bgh/r04_3/ACCOUNT16.BGH");
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
	 * Ticket #177
	 *
	 * CustCode: 7.312087.10.00.100000
	 * Cobrança 0$
	 *
	 * Childs:
	 * 	 No Childs
	 *
	 */
	public void testAccount17() {
		try {
			System.setProperty("cglib.disabled", "true");
			System.setProperty(XStreamLoader.ROOT_DIR_SYSPROP, "src/test/resources/bgh/r04_3/credcorp");
			System.setProperty(XStreamLoader.FILENAME_SYSPROP, "credcorp.xml.gz");
			// firing rules
			this.startupRuleEngine(RULES);
			Account acc = this.loadBGHTestFile("bgh/r04_3/ACCOUNT17.BGH");
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
	 * Ticket #177
	 *
	 * CustCode: 7.312087.00.100001
	 * Cobrança 0$
	 *
	 * Childs:
	 * 	 No Childs
	 *
	 */
	public void testAccount18() {
		try {
			System.setProperty("cglib.disabled", "true");
			System.setProperty(XStreamLoader.ROOT_DIR_SYSPROP, "src/test/resources/bgh/r04_3/credcorp");
			System.setProperty(XStreamLoader.FILENAME_SYSPROP, "credcorp.xml.gz");
			// firing rules
			this.startupRuleEngine(RULES);
			Account acc = this.loadBGHTestFile("bgh/r04_3/ACCOUNT18.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}
