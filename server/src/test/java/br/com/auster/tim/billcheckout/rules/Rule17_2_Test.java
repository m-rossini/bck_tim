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
import br.com.auster.billcheckout.consequence.telco.TelcoConsequence;
import br.com.auster.om.invoice.Account;



/**
 * @author framos
 * @version $Id$
 *
 */
public class Rule17_2_Test extends BaseRuleTest {


	private String[] RULES = { "src/main/conf/rules/R17.2-invoice-total-validation.drl" };

	/**
	 * This is the correct file
	 */
	public void testAccount1() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account act = this.loadBGHTestFile("bgh/r17_2/ACCOUNT1.BGH");
			this.assertAccount(act);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * This file is OK, but has IndividualNF
	 */
	public void testAccount2() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account act = this.loadBGHTestFile("bgh/r17_2/ACCOUNT2.BGH");
			this.assertAccount(act);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Another correct file with IndividualNF and no NFs.
	 * Regarding ticket #191.
	 */
	public void testAccount3() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account act = this.loadBGHTestFile("bgh/r17_2/ACCOUNT3.BGH");
			this.assertAccount(act);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * This account has IndividualNF and NFs.
	 * Regarding ticket #191.
	 *
	 * Should only sum NFs, and no errors would be reported.
	 */
	public void testAccount4() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account act = this.loadBGHTestFile("bgh/r17_2/ACCOUNT4.BGH");
			this.assertAccount(act);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

}
