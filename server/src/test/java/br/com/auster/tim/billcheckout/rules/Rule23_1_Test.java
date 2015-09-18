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
 * Created on 10/10/2007
 */
package br.com.auster.tim.billcheckout.rules;

import org.w3c.dom.Element;

import br.com.auster.billcheckout.consequence.Consequence;
import br.com.auster.common.sql.SQLConnectionManager;
import br.com.auster.common.xml.DOMUtils;
import br.com.auster.om.invoice.Account;
import br.com.auster.tim.billcheckout.param.ContractServicesCache;
import br.com.auster.tim.billcheckout.param.DwPromoMcCache;
import br.com.auster.tim.billcheckout.param.PlansCache;
import br.com.auster.tim.billcheckout.param.PromotionExclusivityCache;

/**
 * TODO What this class is responsible for
 *
 * @author William Soares
 * @version $Id$
 * @since JDK1.4
 */
public class Rule23_1_Test extends BaseRuleTest {

	private String[] RULES = { "src/main/conf/rules/guiding/load-carrier-code.drl",
								"src/main/conf/rules/R23.1-incompatible-services.drl" };


    protected void createGlobals() {
    	super.createGlobals();
    	try {

	    		Class.forName("org.apache.commons.dbcp.PoolingDriver");
	    		Class.forName("oracle.jdbc.driver.OracleDriver");
	    		SQLConnectionManager.init(DOMUtils.openDocument("bgh/guiding/pool.xml", false));

		    	Element arg0 = DOMUtils.openDocument("bgh/guiding/caches.xml", false);

		    	PromotionExclusivityCache promotionExclusivityCache = new PromotionExclusivityCache();
		    	promotionExclusivityCache.configure(arg0);
		    	this.workingMemory.setGlobal("promotionExclusivityCache", promotionExclusivityCache);

		    	DwPromoMcCache dwPromoMcCache = new DwPromoMcCache();
		    	dwPromoMcCache.configure(arg0);
		    	this.workingMemory.setGlobal("dwPromoMcCache", dwPromoMcCache);

		    	ContractServicesCache contractServicesCache = new ContractServicesCache();
		    	contractServicesCache.configure(arg0);
		    	this.workingMemory.setGlobal("contractServicesCache", contractServicesCache);

		    	PlansCache plansCache = new PlansCache();
		    	plansCache.configure(arg0);
		    	this.workingMemory.setGlobal("planCache", plansCache);

    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }


	/**
	 * The promotions in this file are not exclusive.
	 */
	public void testAccount1() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r23_1/ACCOUNT1.BGH");
			// firing rules
			this.startupRuleEngine(RULES);
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
	 * This file contains only one exlusive promotion: 'NAT06', that is on
	 * DW_PROMO_MC but it there must be more than one promotion
	 * exclusive to perform the comparisons.
	 */
	public void testAccount2() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r23_1/ACCOUNT2.BGH");
			// firing rules
			this.startupRuleEngine(RULES);
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
	 * This file contains two exclusive promotions: 'P2' and 'P3', that are not
	 * on DW_PROMO_MC.
	 */
	public void testAccount3() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r23_1/ACCOUNT3.BGH");
			// firing rules
			this.startupRuleEngine(RULES);
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
	 * 1 Error
	 *
	 * This file contains 3 exclusive promotions:
	 * 		- PLD07: Activation Date: 22/04/2008, Expiration Date: 20/05/2008
	 * 		- PTP07: Activation Date: 22/06/2008, Expiration Date: 20/07/2008
	 * 		- NAT06: Activation Date: 20/04/2008, Expiration Date: NULL
	 *
	 * - 'NAT06' does not exist on CONTR_SERVICES
	 */
	public void testAccount4() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r23_1/ACCOUNT4.BGH");
			// firing rules
			this.startupRuleEngine(RULES);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			Consequence c = this.results.get(0);
			assertEquals("Promoção constante na fatura não encontrada no cadastro do cliente.", c.getDescription());
			//contract
			assertEquals("8343005", c.getAttributes().getAttribute(0));
			//accesss number
			assertEquals("031-9277-4365", c.getAttributes().getAttribute(1));
			//promotion description
			assertEquals("Natal 2006", c.getAttributes().getAttribute(2));

			assertEquals(1, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * 2 Errors
	 *
	 * This file contains 5 exclusive promotions:
	 * 		- PLD07: Activation Date: 22/04/2008, Expiration Date: 20/05/2008
	 * 		- PTP07: Activation Date: 22/06/2008, Expiration Date: 20/07/2008
	 * 	  	- M07CI: Activation Date: 23/04/2008, Expiration Date: 21/05/2008
	 * 		- MAE07: Activation Date: 24/04/2008, Expiration Date: 22/05/2008
	 * 		- NAT06: Activation Date: 20/04/2008, Expiration Date: NULL
	 *
	 * - 'NAT06' does not exist on CONTR_SERVICES
	 * - 'M07CI' and 'MAE07' were activated at the same time.
	 *
	 */
	public void testAccount5() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r23_1/ACCOUNT5.BGH");
			// firing rules
			this.startupRuleEngine(RULES);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			Consequence c = this.results.get(0);
			assertEquals("Promoção constante na fatura não encontrada no cadastro do cliente.", c.getDescription());
			//contract
			assertEquals("8343005", c.getAttributes().getAttribute(0));
			//accesss number
			assertEquals("031-9277-4365", c.getAttributes().getAttribute(1));
			//promotion description
			assertEquals("Natal 2006", c.getAttributes().getAttribute(2));

			c = this.results.get(1);
			assertEquals("Promoções conflitantes estão ativadas num mesmo período.", c.getDescription());
			//contract
			assertEquals("8343005", c.getAttributes().getAttribute(0));
			//accesss number
			assertEquals("031-9277-4365", c.getAttributes().getAttribute(1));
			//promotion1 description
			assertEquals("M07CI 2007", c.getAttributes().getAttribute(2));
			//promotion1 activation date
			assertEquals("2008-04-23", c.getAttributes().getAttribute(3));
			//promotion1 expiration date
			assertEquals("2008-05-21", c.getAttributes().getAttribute(4));
			//promotion2 description
			assertEquals("MAE07 2007", c.getAttributes().getAttribute(5));
			//promotion2 activation date
			assertEquals("2008-04-24", c.getAttributes().getAttribute(6));
			//promotion2 expiration date
			assertEquals("2008-05-22", c.getAttributes().getAttribute(7));

			assertEquals(2, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * 3 Errors
	 *
	 * This file contains 5 exclusive promotions:
	 * 		- PLD07: Activation Date: 22/04/2008, Expiration Date: 20/05/2008
	 * 		- PTP07: Activation Date: 22/06/2008, Expiration Date: 20/07/2008
	 * 	  	- M07CI: Activation Date: 23/04/2008, Expiration Date: 21/05/2008
	 * 		- MAE07: Activation Date: 24/04/2008, Expiration Date: 22/05/2008
	 * 		- PTZB: Activation Date: 24/04/2008, Expiration Date: NULL
	 * 		- NAT06: Activation Date: 20/04/2008, Expiration Date: NULL
	 *
	 * - 'NAT06' does not exist on CONTR_SERVICES
	 * - 'M07CI' and 'MAE07' were activated at the same time.
	 * - 'M07CI' and 'PTZB' were activated at the same time.
	 *
	 */
	public void testAccount6() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r23_1/ACCOUNT6.BGH");
			// firing rules
			this.startupRuleEngine(RULES);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			Consequence c = null;

			for (int i = 0; i < 3; i++) {
				c = this.results.get(i);
				if(c.getAttributes().getAttribute(5) == null || c.getAttributes().getAttribute(5).trim().equals("")) {
					assertEquals("Promoção constante na fatura não encontrada no cadastro do cliente.", c.getDescription());
					//contract
					assertEquals("8343005", c.getAttributes().getAttribute(0));
					//accesss number
					assertEquals("031-9277-4365", c.getAttributes().getAttribute(1));
					//promotion description
					assertEquals("Natal 2006", c.getAttributes().getAttribute(2));
				} else if(c.getAttributes().getAttribute(5).startsWith("PTZB")) {
					assertEquals("Promoções conflitantes estão ativadas num mesmo período.", c.getDescription());
					//contract
					assertEquals("8343005", c.getAttributes().getAttribute(0));
					//accesss number
					assertEquals("031-9277-4365", c.getAttributes().getAttribute(1));
					//promotion1 description
					assertEquals("M07CI 2007", c.getAttributes().getAttribute(2));
					//promotion1 activation date
					assertEquals("2008-04-23", c.getAttributes().getAttribute(3));
					//promotion1 expiration date
					assertEquals("2008-05-21", c.getAttributes().getAttribute(4));
					//promotion2 description
					assertEquals("PTZB 2007", c.getAttributes().getAttribute(5));
					//promotion2 activation date
					assertEquals("2008-04-24", c.getAttributes().getAttribute(6));
				} else if(c.getAttributes().getAttribute(5).startsWith("MAE07")) {
					assertEquals("Promoções conflitantes estão ativadas num mesmo período.", c.getDescription());
					//contract
					assertEquals("8343005", c.getAttributes().getAttribute(0));
					//accesss number
					assertEquals("031-9277-4365", c.getAttributes().getAttribute(1));
					//promotion1 description
					assertEquals("M07CI 2007", c.getAttributes().getAttribute(2));
					//promotion1 activation date
					assertEquals("2008-04-23", c.getAttributes().getAttribute(3));
					//promotion1 expiration date
					assertEquals("2008-05-21", c.getAttributes().getAttribute(4));
					//promotion2 description
					assertEquals("MAE07 2007", c.getAttributes().getAttribute(5));
					//promotion2 activation date
					assertEquals("2008-04-24", c.getAttributes().getAttribute(6));
					//promotion2 expiration date
					assertEquals("2008-05-22", c.getAttributes().getAttribute(7));
				}
			}

			assertEquals(3, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Ticket #311 - NPE
	 */
	public void testAccount7() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r23_1/ACCOUNT7.BGH");
			// firing rules
			this.startupRuleEngine(RULES);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			assertEquals(1, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}
