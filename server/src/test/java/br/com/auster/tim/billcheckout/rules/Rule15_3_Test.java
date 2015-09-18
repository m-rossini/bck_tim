/*
 * Copyright (c) 2004-2007 Auster Solutions. All Rights Reserved.
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
 * Created on 13/09/2007
 */
package br.com.auster.tim.billcheckout.rules;

import br.com.auster.billcheckout.consequence.Consequence;
import br.com.auster.om.invoice.Account;


/**
 * TODO What this class is responsible for
 *
 * @author William Soares
 * @version $Id$
 * @since JDK1.4
 */
public class Rule15_3_Test extends BaseRuleTest {

	private String[] RULES = { "src/main/conf/rules/R15.3-icms-verification.drl" };


	/**
	 * This is the correct file
	 */
	public void testAccount1() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
			this.assertAccount(this.loadBGHTestFile("bgh/r15_3/ACCOUNT1.BGH"));
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Wrong taxableAmount
	 */
	public void testAccount2() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
			this.assertAccount(this.loadBGHTestFile("bgh/r15_3/ACCOUNT2.BGH"));
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(1, this.results.size());
			Consequence c = this.results.iterator().next();
			assertEquals("Base de cálculo não confere com a apresentada na nota fiscal.", c.getDescription());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Wrong taxAmount
	 */
	public void testAccount3() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
			this.assertAccount(this.loadBGHTestFile("bgh/r15_3/ACCOUNT3.BGH"));
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(1, this.results.size());
			Consequence c = this.results.iterator().next();
			assertEquals("Valor do imposto não confere com o apresentado na nota fiscal.", c.getDescription());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}


	/**
	 * This is the correct file
	 */
	public void testAccount4() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
			this.assertAccount(this.loadBGHTestFile("bgh/r15_3/ACCOUNT4.BGH"));
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}


	/**
	 * Same as ACCOUNT5.BGH, with the following errors:
	 *   - LDC (Telecomunicações de São Paulo S/A. - TELESP) : taxableAmount
	 *   - LDC (EMBRATEL) - totalAmount
	 *   - LDC (TIM Celular S/A) - taxableAmount and totalAmount
	 */
	public void testAccount5() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
			this.assertAccount(this.loadBGHTestFile("bgh/r15_3/ACCOUNT5.BGH"));
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(4, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Now this account does not create critical reports anymore because
	 * 0.01 differences is not an error. refs ticket #206
	 */
	public void testAccount6() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
			this.assertAccount(this.loadBGHTestFile("bgh/r15_3/ACCOUNT6.BGH"));
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}


	/**
	 * From TIAMAT
	 */
	public void testAccount7() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
			Account acc = this.loadBGHTestFile("bgh/r15_3/ACCOUNT7.BGH");
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
	 * This is a correct account that was created a wrong critical report.
	 * due calculation errors.
	 * Now these errors does not happen anymore because the rule is using
	 * BigDecimal on calculation.
	 */
	public void testAccount8() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
			Account acc = this.loadBGHTestFile("bgh/r15_3/ACCOUNT8.BGH");
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
	 * This account shows the error caused by the incomplete TAG 34101000,
	 * the missing atribute is 'roamingIndicator'.
	 *
	 */
	public void testAccount9() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
			Account acc = this.loadBGHTestFile("bgh/r15_3/ACCOUNT9.BGH");
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
	 * The next two are related to correcting the roamingIndicator bug
	 *    in the BGH
	 */
	public void testAccount10() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
			Account acc = this.loadBGHTestFile("bgh/r15_3/ACCOUNT10.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	public void testAccount11() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
			Account acc = this.loadBGHTestFile("bgh/r15_3/ACCOUNT11.BGH");
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
