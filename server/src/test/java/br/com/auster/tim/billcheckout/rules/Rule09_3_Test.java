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

import java.util.HashSet;

import br.com.auster.billcheckout.consequence.Consequence;
import br.com.auster.tim.billcheckout.util.NFNumberHelper;


/**
 * @author framos
 * @version $Id$
 *
 */
public class Rule09_3_Test extends BaseRuleTest {


	private String[] RULES = { "src/main/conf/rules/R09.3-custcode-validation.drl" };
	
	protected void setUp() throws Exception {
		NFNumberHelper.clearNumbers();
	}
	
	/**
	 * GNI^F^2^0^50MG^P^5557847^1.14000519^1^
	 * 
	 * This is the ACCOUNT1.BGH original file. 
	 * 
	 * Errors: NONE! All is OK
	 */
	public void testAccount1() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
	        this.assertAccount(this.loadBGHTestFile("bgh/r09_3/ACCOUNT1.BGH"));
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/**
	 * GNI^L^2^0^50MG^P^5557847^1.14000519^1^
	 * 
	 * This is ACCOUNT1.BGH with the account type modified
	 * 
	 * Errors: 1 - O tipo desta conta deveria ser F
	 */
	public void testAccount2() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
	        this.assertAccount(this.loadBGHTestFile("bgh/r09_3/ACCOUNT2.BGH"));
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(1, this.results.size());
			assertEquals("O tipo desta conta deveria ser F", this.results.iterator().next().getDescription());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
 	
	/**
	 * GNI^F^2^0^50MG^P^5557847^1.14000519^3^
	 * 
	 * This is ACCOUNT1.BGH with the document type modified
	 * 
	 * Errors: 1 - Contas de tipo F não podem possuir faturas de tipo 3 ou 4
	 */
	public void testAccount3() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
	        this.assertAccount(this.loadBGHTestFile("bgh/r09_3/ACCOUNT3.BGH"));
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(1, this.results.size());
			assertEquals("Contas de tipo F não podem possuir faturas de tipo 3 ou 4", this.results.iterator().next().getDescription());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}	
	
	/**
	 * GNI^L^2^0^50MG^P^5557847^1.14000519^3^
	 * 
	 * This is ACCOUNT1.BGH with the document type and account type modified
	 * 
	 * Errors: 1 - O tipo desta conta deveria ser F
	 */
	public void testAccount4() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
	        this.assertAccount(this.loadBGHTestFile("bgh/r09_3/ACCOUNT4.BGH"));
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(1, this.results.size());
			assertEquals("O tipo desta conta deveria ser F", this.results.iterator().next().getDescription());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}		
	
	/**
	 * GNI^F^2^0^50MG^P^5557848^5.14000520^1^
	 * 
	 * This is ACCOUNT5.BGH original file.
	 * 
	 * Errors: 1 - O tipo desta conta deveria ser L
	 */
	public void testAccount5() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
	        this.assertAccount(this.loadBGHTestFile("bgh/r09_3/ACCOUNT5.BGH"));
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(1, this.results.size());
			assertEquals("O tipo desta conta deveria ser L", this.results.iterator().next().getDescription());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}		
	
	/**
	 * GNI^L^2^0^50MG^P^5557848^5.14000520^1^
	 * 
	 * This is ACCOUNT5.BGH with the account type modified
	 * 
	 * Errors: NONE! All is OK
	 */
	public void testAccount6() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
	        this.assertAccount(this.loadBGHTestFile("bgh/r09_3/ACCOUNT6.BGH"));
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/**
	 * GNI^F^2^0^50MG^P^5557848^5.14000520^3^
	 * 
	 * This is ACCOUNT5.BGH with the document type modified
	 * 
	 * Errors: 1 - O tipo desta conta deveria ser L
	 *         1 - Contas de tipo F não podem possuir faturas de tipo 3 ou 4
	 */
	public void testAccount7() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
	        this.assertAccount(this.loadBGHTestFile("bgh/r09_3/ACCOUNT7.BGH"));
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashSet<String> descriptions = new HashSet<String>();
			for (Consequence c : this.results) {
				descriptions.add(c.getDescription());
			}
			assertEquals(2, this.results.size());
			assertEquals(2, descriptions.size());
			assertTrue(descriptions.contains("O tipo desta conta deveria ser L"));
			assertTrue(descriptions.contains("Contas de tipo F não podem possuir faturas de tipo 3 ou 4"));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}	
	
	/**
	 * GNI^L^2^0^50MG^P^5557848^5.14000520^3^
	 * 
	 * This is ACCOUNT5.BGH with the document type and account type modified
	 * 
	 * Errors: NONE! All is OK.
	 */
	public void testAccount8() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
	        this.assertAccount(this.loadBGHTestFile("bgh/r09_3/ACCOUNT8.BGH"));
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}	

	/**
	 * GNI^L^2^0^50MG^P^5557855^6.310260.10.00.100001^2^
	 * 
	 * This is ACCOUNT9.BGH original file.
	 * 
	 * Errors: NONE! All is OK.
	 */
	public void testAccount9() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
	        this.assertAccount(this.loadBGHTestFile("bgh/r09_3/ACCOUNT9.BGH"));
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}		
	
	/**
	 * GNI^F^2^0^50MG^P^5557855^6.310260.10.00.100001^2^
	 * 
	 * This is ACCOUNT9.BGH with the account type modified
	 * 
	 * Errors: 1 - O tipo desta conta deveria ser L
	 */
	public void testAccount10() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
	        this.assertAccount(this.loadBGHTestFile("bgh/r09_3/ACCOUNT10.BGH"));
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(1, this.results.size());
			assertEquals("O tipo desta conta deveria ser L", this.results.iterator().next().getDescription());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/**
	 * GNI^L^2^0^50MG^P^5557855^6.310260.10.00.100001^4^
	 * 
	 * This is ACCOUNT9.BGH with the document type modified
	 * 
	 * Errors: NONE! All is OK.
	 */
	public void testAccount11() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
	        this.assertAccount(this.loadBGHTestFile("bgh/r09_3/ACCOUNT11.BGH"));
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}	
	
	/**
	 * GNI^F^2^0^50MG^P^5557855^6.310260.10.00.100001^4^
	 * 
	 * This is ACCOUNT4.BGH with the document type and account type modified
	 * 
	 * Errors: 1 - O tipo desta conta deveria ser L
	 *         1 - Contas de tipo F não podem possuir faturas de tipo 3 ou 4
	 */
	public void testAccount12() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
	        this.assertAccount(this.loadBGHTestFile("bgh/r09_3/ACCOUNT12.BGH"));
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashSet<String> descriptions = new HashSet<String>();
			for (Consequence c : this.results) {
				descriptions.add(c.getDescription());
			}
			assertEquals(2, this.results.size());
			assertEquals(2, descriptions.size());
			assertTrue(descriptions.contains("O tipo desta conta deveria ser L"));
			assertTrue(descriptions.contains("Contas de tipo F não podem possuir faturas de tipo 3 ou 4"));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}		
}
