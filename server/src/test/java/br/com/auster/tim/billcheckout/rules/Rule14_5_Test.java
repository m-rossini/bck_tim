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
import br.com.auster.tim.om.invoice.TIMAccount;


/**
 * @author framos
 * @version $Id$
 *
 */
public class Rule14_5_Test extends BaseRuleTest {


	private String[] RULES = { "src/main/conf/rules/R14.5-check-carrier-in-paystub.drl" };
	
	protected void setUp() throws Exception {
		NFNumberHelper.clearNumbers();
	}
	
	/**
	 * This is a correct file, with address info in the NFHeader tag
	 */
	public void testAccount1() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
			TIMAccount acc = (TIMAccount) this.loadBGHTestFile("bgh/r14_5/ACCOUNT1.BGH");
	        this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
			assertEquals("TIM Nordeste S.A.", acc.getCarrierName());
			assertNotNull(acc.getCarrierAddress());
			assertEquals("Av. Ayrton Senna da Silva", acc.getCarrierAddress().getAddressStreet());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/**
	 * This is a correct file, with address info in the Header tag
	 */
	public void testAccount2() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
			TIMAccount acc = (TIMAccount) this.loadBGHTestFile("bgh/r14_5/ACCOUNT2.BGH");
	        this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
			assertEquals("TIM Nordeste S.A.", acc.getCarrierName());
			assertNotNull(acc.getCarrierAddress());
			assertEquals("Av. Raja Gabaglia", acc.getCarrierAddress().getAddressStreet());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/**
	 * This is a correct file, with address info in the Header tag.
	 * NFHeader information was modified to be diff. from Header
	 */
	public void testAccount3() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
			TIMAccount acc = (TIMAccount) this.loadBGHTestFile("bgh/r14_5/ACCOUNT3.BGH");
	        this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
			assertEquals("TIM Nordeste S.A.", acc.getCarrierName());
			assertNotNull(acc.getCarrierAddress());
			assertEquals("Av. Raja Gabaglia", acc.getCarrierAddress().getAddressStreet());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
		
	//TIM Sudeste S.A.^Av. Raja Kabaglia^^30350-540^Belo Horizonte^MX
	/**
	 * This file uses the Header tag, with the following errors:
	 *  - Name was modified to "TIM Sudeste S.A."
	 *  - Address was modified to "Av. Raja Kabaglia"
	 *  - Address number was removed
	 *  - ZIP code was left correct
	 *  - City was modified to "Belo Horizonte"
	 *  - State was modified to "MX"
	 *  
	 *  NFHeader was left unmodified.
	 */
	public void testAccount4() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
			TIMAccount acc = (TIMAccount) this.loadBGHTestFile("bgh/r14_5/ACCOUNT4.BGH");
	        this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(5, this.results.size());
			assertEquals("TIM Sudeste S.A.", acc.getCarrierName());
			assertNotNull(acc.getCarrierAddress());
			assertEquals("Av. Raja Kabaglia", acc.getCarrierAddress().getAddressStreet());
			
			HashSet<String> wrongAttrs = new HashSet<String>();
			for (Consequence c : this.results) {
				wrongAttrs.add(c.getAttributes().getAttributeValue1());
			}
			assertEquals(5, wrongAttrs.size());
			assertTrue(wrongAttrs.contains("Nome da Operadora"));
			assertTrue(wrongAttrs.contains("Nome da Rua"));
			assertTrue(wrongAttrs.contains("Número no Endereço"));
			assertTrue(wrongAttrs.contains("Cidade"));
			assertTrue(wrongAttrs.contains("Estado"));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/**
	 * This file uses the Header tag, with the following errors:
	 *  - Name was removed
	 *  
	 *  NFHeader was left unmodified.
	 */
	public void testAccount5() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
			TIMAccount acc = (TIMAccount) this.loadBGHTestFile("bgh/r14_5/ACCOUNT5.BGH");
	        this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(1, this.results.size());
			assertNull(acc.getCarrierName());
			// check if exception is really about carrier name 
			Consequence c = this.results.iterator().next();
			assertEquals("Nome da Operadora", c.getAttributes().getAttributeValue1());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}
