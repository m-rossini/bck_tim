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

import br.com.auster.om.invoice.Account;



/**
 * @author framos
 * @version $Id$
 *
 */
public class Rule09_4_Test extends BaseRuleTest {

	
	
	private String[] RULES = { "src/main/conf/rules/R09.4-directdebit-validation.drl" };
	
	

	/**
	 * This is the original account, where the direct debit flag is P
	 * Nothing is wrong.
	 */
	public void testAccount1() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
			this.assertAccount(this.loadBGHTestFile("bgh/r09_4/ACCOUNT1.BGH"));
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * This is the original account, where the direct debit flag was modified to D.
	 * Still nothing is wrong.
	 */
	public void testAccount2() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
			Account acc = this.loadBGHTestFile("bgh/r09_4/ACCOUNT2.BGH");
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
	 * This is the original account, where the direct debit flag was modified to D and the custcode
	 * 	information was changed to.
	 * This should raise an error.
	 */
	public void testAccount3() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
			Account acc = this.loadBGHTestFile("bgh/r09_4/ACCOUNT3.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(1, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/**
	 * This is the original account, where the direct debit flag was is P and the debit info was removed.
	 * Nothing is wrong.
	 */
	public void testAccount4() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
			Account acc = this.loadBGHTestFile("bgh/r09_4/ACCOUNT4.BGH");
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
	 * This is the original account, where the direct debit flag was is D and the debit info was removed.
	 * Nothing is wrong.
	 */
	public void testAccount5() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
			Account acc = this.loadBGHTestFile("bgh/r09_4/ACCOUNT5.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(1, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}		
	
}


