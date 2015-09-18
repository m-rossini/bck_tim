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
import br.com.auster.tim.billcheckout.param.PenaltyInterestLDNFCache;
import br.com.auster.tim.billcheckout.param.PenaltyInterestLocalNFCache;

/**
 * TODO What this class is responsible for
 *
 * @author William Soares
 * @version $Id$
 * @since JDK1.4
 */
public class Rule29_1_Test extends BaseRuleTest {

	private String[] RULES = { "src/main/conf/rules/guiding/load-carrier-code.drl",
								"src/main/conf/rules/R29.1-penalty-interest-validation.drl" };


	protected boolean customized = false;


    protected void createGlobals() {
    	super.createGlobals();
    	try {

	    		Class.forName("org.apache.commons.dbcp.PoolingDriver");
	    		Class.forName("oracle.jdbc.driver.OracleDriver");
	    		SQLConnectionManager.init(DOMUtils.openDocument("bgh/guiding/pool.xml", false));

		    	Element arg0 = DOMUtils.openDocument("bgh/guiding/caches.xml", false);

		    	PenaltyInterestLDNFCache penaltyInterestLDNFCache = new PenaltyInterestLDNFCache();
		    	penaltyInterestLDNFCache.configure(arg0);
		    	this.workingMemory.setGlobal("penaltyInterestLDNFCache", penaltyInterestLDNFCache);

		    	PenaltyInterestLocalNFCache penaltyInterestLocalNFCache = new PenaltyInterestLocalNFCache();
		    	penaltyInterestLocalNFCache.configure(arg0);
		    	this.workingMemory.setGlobal("penaltyInterestLocalNFCache", penaltyInterestLocalNFCache);

    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }


	/**
	 * This file is correct.
	 */
	public void testAccount1() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r29_1/ACCOUNT1.BGH");
			// firing rules
			this.customized = false;
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
	 * This file contains two invalid charges because there is no data on database regarding the follow.
	 * 	- Telecomunicações de São Paulo S/A. - TELESP, due date: 15/03/08
	 * 	- TIM Celular S.A, due date: 15/02/08
	 */
	public void testAccount2() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r29_1/ACCOUNT2.BGH");
			// firing rules
			this.customized = false;
			this.startupRuleEngine(RULES);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();

			// running over results list to check if consequences where ok
			Consequence c = this.results.get(0);
			assertEquals("Foi cobrado multa e juros sem existência de uso desta operadora na data de vencimento em questão.", c.getDescription());
			assertEquals("15", c.getAttributes().getAttribute(0));
			assertEquals("Telecomunicações de São Paulo S/A. - TELESP", c.getAttributes().getAttribute(1));
			assertEquals("15/02/08", c.getAttributes().getAttribute(2));

			c = this.results.get(1);
			assertEquals("Foi cobrado multa e juros sem existência de uso desta operadora na data de vencimento em questão.", c.getDescription());
			assertEquals("41", c.getAttributes().getAttribute(0));
			assertEquals("TIM Celular S.A.", c.getAttributes().getAttribute(1));
			assertEquals("15/03/08", c.getAttributes().getAttribute(2));

			assertEquals(2, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * This file contains two wrong charges for local/LD 41 receipts.
	 * 	- JUROS -> Valor da Nota:2525.00, Valor do item:12.60, Valor calculado:12.62,
	 * 			   Vencimento:15/01/08, Pagamento:30/01/08
	 * 	- MULTAS -> Valor da Nota:2525.00, Valor do item:50.51, Valor calculado:50.50,
	 * 				Vencimento:15/01/08, Pagamento:30/01/08
	 */
	public void testAccount3() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r29_1/ACCOUNT3.BGH");
			// firing rules
			this.customized = false;
			this.startupRuleEngine(RULES);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			Consequence c = this.results.get(0);
			assertEquals("Cobrança de juros com valor incorreto.", c.getDescription());
			assertEquals("41", c.getAttributes().getAttribute(0));
			assertEquals("TIM Celular S.A.", c.getAttributes().getAttribute(1));
			assertEquals("15/01/08", c.getAttributes().getAttribute(2));
			assertEquals("30/01/08", c.getAttributes().getAttribute(3));
			assertEquals("2525.00", c.getAttributes().getAttribute(4).replaceAll(",", "."));
			assertEquals("12.60", c.getAttributes().getAttribute(5).replaceAll(",", "."));
			assertEquals("11.78", c.getAttributes().getAttribute(6).replaceAll(",", "."));

			assertEquals(1, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * This file contains two wrong charges for LD receipts.
	 *	- Operadora: Telecomunicações de São Paulo S/A. - TELESP, CSP: 15
	 *		JUROS incorretos:  Valor da Nota:66.00, Valor do item:0.33, Valor calculado:0.22,
	 * 			   			   Vencimento:15/01/08, Pagamento:25/01/08
	 *
	 *  - Operadora: EMBRATEL, CSP: 21
	 * 		MULTA incorreta:  Valor da Nota:6.50, Valor do item:0.12, Valor calculado:0.13,
	 * 						  Vencimento:15/01/08, Pagamento:30/01/0
	 */
	public void testAccount4() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r29_1/ACCOUNT4.BGH");
			// firing rules
			this.customized = false;
			this.startupRuleEngine(RULES);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			Consequence c = this.results.get(0);
			assertEquals("Cobrança de juros com valor incorreto.", c.getDescription());
			assertEquals("15", c.getAttributes().getAttribute(0));
			assertEquals("Telecomunicações de São Paulo S/A. - TELESP", c.getAttributes().getAttribute(1));
			assertEquals("15/01/08", c.getAttributes().getAttribute(2));
			assertEquals("25/01/08", c.getAttributes().getAttribute(3));
			assertEquals("66.00", c.getAttributes().getAttribute(4).replaceAll(",", "."));
			assertEquals("0.30", c.getAttributes().getAttribute(5).replaceAll(",", "."));
			assertEquals("0.19", c.getAttributes().getAttribute(6).replaceAll(",", "."));

			assertEquals(1, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testAccount5() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r29_1/ACCOUNT5.BGH");
			// firing rules
			this.customized = false;
			this.startupRuleEngine(RULES);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(1, this.results.size());

			Consequence c = this.results.get(0);
			assertEquals("Cobrança de multa com valor incorreto.", c.getDescription());
			assertEquals("1.50", c.getAttributes().getAttribute(5).replaceAll(",", "."));
			assertEquals("1.97", c.getAttributes().getAttribute(6).replaceAll(",", "."));

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testAccount7() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r29_1/ACCOUNT7.BGH");
			// firing rules
			this.customized = false;
			this.startupRuleEngine(RULES);
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
