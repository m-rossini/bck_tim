/*
 * Copyright (c) 2004-2008 Auster Solutions. All Rights Reserved.
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
 * Created on 14/04/2008
 */
package br.com.auster.tim.billcheckout.rules;

import org.w3c.dom.Element;

import br.com.auster.billcheckout.consequence.Consequence;
import br.com.auster.common.sql.SQLConnectionManager;
import br.com.auster.common.xml.DOMUtils;
import br.com.auster.om.invoice.Account;
import br.com.auster.tim.billcheckout.param.ProgressiveDiscountCache;

/**
 * TODO What this class is responsible for
 *
 * @author William Soares
 * @version $Id$
 * @since JDK1.4
 */
public class Rule02_1_Test extends BaseRuleTest {

	private String[] RULES = { "src/main/conf/rules/R02.1-progressive-discount-contracts.drl",
			                   "src/main/conf/rules/R02.1-progressive-discount-usage.drl" };

    protected void createGlobals() {
    	super.createGlobals();
    	try {
    		Class.forName("org.apache.commons.dbcp.PoolingDriver");
    		Class.forName("oracle.jdbc.driver.OracleDriver");
    		SQLConnectionManager.init(DOMUtils.openDocument("bgh/guiding/pool.xml", false));

	    	Element arg0 = DOMUtils.openDocument("bgh/guiding/caches.xml", false);

	    	ProgressiveDiscountCache progressiveDiscountCache = new ProgressiveDiscountCache();
	    	progressiveDiscountCache.configure(arg0);
	    	this.workingMemory.setGlobal("progressiveDiscountCache", progressiveDiscountCache);

    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

    /**
     * Correct file.
     *
     * This file contains two Progressive discounts: "LDI Econômica 41 TIM" and "LDN Econômica 41 TIM".
     * 		The discount rate (10.0 and 5.0 respectively);
     * 		The discount amount (11.19 and 0.52 respectively) are correct.
     *
     */
    public void testAccount1() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r02_1/ACCOUNT1.BGH");
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
     * This file contains one error.
     *
     * This file contains two Progressive discounts: "LDN Econômica 41 TIM" and "Desconto Assinatura Nosso Modo".
     * 		The discount rate and discount amount for "Desconto Assinatura Nosso Modo" are correct: 10.0 and 0.80 respectively.
     * 	 	The discount amount for "LDN Econômica 41 TIM" is wrong:
     * 			Discount rate: 5.0
     * 			Discount amount in BGH: 1.89
     * 			Discount amount expected: 1.82
     *
     */
    public void testAccount2() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r02_1/ACCOUNT2.BGH");
			// firing rules
			this.startupRuleEngine(RULES);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();

			// running over results list to check if consequences where ok
			Consequence c = this.results.get(0);
			assertEquals("Valor de desconto não confere com o calculado.", c.getDescription());
			//Nome do Pacote/Serviço
			assertEquals("LDN Econômica 41 TIM", c.getAttributes().getAttribute(0));
			//Valor Base para Desconto
			assertEquals("39.42", c.getAttributes().getAttribute(1).replaceAll(",", "."));
			//Medida Usada como Base
			assertEquals("Reais", c.getAttributes().getAttribute(2));
			//Percentual de Desconto na Fatura
			assertEquals("5.00", c.getAttributes().getAttribute(3).replaceAll(",", "."));
			//Percentual de Desconto Esperado
			assertEquals("5.00", c.getAttributes().getAttribute(4).replaceAll(",", "."));
			//Valor de Desconto na Fatura
			assertEquals("1.82", c.getAttributes().getAttribute(5).replaceAll(",", "."));
			//Valor de Descontos Calculado
			assertEquals("1.89", c.getAttributes().getAttribute(6).replaceAll(",", "."));

			assertEquals(1, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

    /**
     * This file contains one error.
     *
     * This file contains one Progressive discounts: "Desconto Assinatura Nosso Modo".
     * 	 	The discount rate for "Desconto Assinatura Nosso Modo" is wrong:
     * 			Discount rate in BGH: 11.0
     * 			Discount rate expected: 10.0
     *
     */
    public void testAccount3() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r02_1/ACCOUNT3.BGH");
			// firing rules
			this.startupRuleEngine(RULES);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();

			// running over results list to check if consequences where ok
			Consequence c = this.results.get(0);
			assertEquals("Percentual de desconto não confere com o cadastrado na Base.", c.getDescription());
			//Nome do Pacote/Serviço
			assertEquals("Desconto Assinatura Nosso Modo", c.getAttributes().getAttribute(0));
			//Valor Base para Desconto
			assertEquals("1.00", c.getAttributes().getAttribute(1).replaceAll(",", "."));
			//Medida Usada como Base
			assertEquals("Assinaturas", c.getAttributes().getAttribute(2));
			//Percentual de Desconto na Fatura
			assertEquals("11.00", c.getAttributes().getAttribute(3).replaceAll(",", "."));
			//Percentual de Desconto Esperado
			assertEquals("10.00", c.getAttributes().getAttribute(4).replaceAll(",", "."));

			assertEquals(1, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

    /**
     * This file contains two errors.
     *
     * This file contains two Progressive discounts: "LDN Econômica 41 TIM" and "Desconto Assinatura Nosso Modo".
     * 		The discount rate and dicount amount for "Desconto Assinatura Nosso Modo" is wrong:
     *  		Discount rate: 10.0
     *  		Discount amount in BGH: 53.99
     *  		Discount amount expected: 52.65
     * 	 	The discount amount for "LDN Econômica 41 TIM" is wrong:
     * 			Discount rate: 5.0
     * 			Discount amount in BGH: 1.89
     * 			Discount amount expected: 1.82
     *
     */
    public void testAccount4() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r02_1/ACCOUNT4.BGH");
			// firing rules
			this.startupRuleEngine(RULES);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();

			Consequence c = this.results.get(0);
			assertEquals("Valor de desconto não confere com o calculado.", c.getDescription());
			//Nome do Pacote/Serviço
			assertEquals("Desconto Assinatura Nosso Modo", c.getAttributes().getAttribute(0));
			//Valor Base para Desconto
			assertEquals("40.00", c.getAttributes().getAttribute(1).replaceAll(",", "."));
			//Medida Usada como Base
			assertEquals("Assinaturas", c.getAttributes().getAttribute(2));
			//Percentual de Desconto na Fatura
			assertEquals("10.00", c.getAttributes().getAttribute(3).replaceAll(",", "."));
			//Percentual de Desconto Esperado
			assertEquals("10.00", c.getAttributes().getAttribute(4).replaceAll(",", "."));
			//Valor de Desconto na Fatura
			assertEquals("53.99", c.getAttributes().getAttribute(5).replaceAll(",", "."));
			//Valor de Descontos Calculado
			assertEquals("52.64", c.getAttributes().getAttribute(6).replaceAll(",", "."));

			// running over results list to check if consequences where ok
			c = this.results.get(1);
			assertEquals("Valor de desconto não confere com o calculado.", c.getDescription());
			//Nome do Pacote/Serviço
			assertEquals("LDN Econômica 41 TIM", c.getAttributes().getAttribute(0));
			//Valor Base para Desconto
			assertEquals("249.55", c.getAttributes().getAttribute(1).replaceAll(",", "."));
			//Medida Usada como Base
			assertEquals("Reais", c.getAttributes().getAttribute(2));
			//Percentual de Desconto na Fatura
			assertEquals("20.00", c.getAttributes().getAttribute(3).replaceAll(",", "."));
			//Percentual de Desconto Esperado
			assertEquals("20.00", c.getAttributes().getAttribute(4).replaceAll(",", "."));
			//Valor de Desconto na Fatura
			assertEquals("49.35", c.getAttributes().getAttribute(5).replaceAll(",", "."));
			//Valor de Descontos Calculado
			assertEquals("49.33", c.getAttributes().getAttribute(6).replaceAll(",", "."));

			assertEquals(2, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

    /**
     * This file contains one error.
     *
     * This file contains two Progressive discounts: "LDN Econômica 41 TIM" and "Desconto Assinatura Nosso Modo".
     * 		The discount rate and discount amount for "Desconto Assinatura Nosso Modo" are correct:
     *  		Discount rate: 10.0
     *  		Discount amount: 53.99
     * 	 	The discount rate for "LDN Econômica 41 TIM" is wrong:
     * 			Discount rate in BGH: 20.0
     * 			Discount rate expected: 5.0
     *
     */
    public void testAccount5() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r02_1/ACCOUNT5.BGH");
			// firing rules
			this.startupRuleEngine(RULES);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();

			// running over results list to check if consequences where ok
			Consequence c = this.results.get(0);
			assertEquals("Percentual de desconto não confere com o cadastrado na Base.", c.getDescription());
			//Nome do Pacote/Serviço
			assertEquals("LDN Econômica 41 TIM", c.getAttributes().getAttribute(0));
			//Valor Base para Desconto
			assertEquals("91.61", c.getAttributes().getAttribute(1).replaceAll(",", "."));
			//Medida Usada como Base
			assertEquals("Reais", c.getAttributes().getAttribute(2));
			//Percentual de Desconto na Fatura
			assertEquals("20.00", c.getAttributes().getAttribute(3).replaceAll(",", "."));
			//Percentual de Desconto Esperado
			assertEquals("5.00", c.getAttributes().getAttribute(4).replaceAll(",", "."));

			assertEquals(1, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

    /**
     * This file does not generate critical reports.
     *
     * This file contains two Progressive discounts: "LDN Econômica 41 TIM" and "LDI 41 TIM".
     * 		The base value (501,12) is included in the last discount range which does not have configured upperLimit.
     *  		Discount rate: 5.0
     *  	The discount "LDI 41 TIM" does not exist in the database
     *
     */
    public void testAccount6() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r02_1/ACCOUNT6.BGH");
			// firing rules
			this.startupRuleEngine(RULES);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();

			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

    /**
     * This file contains one error.
     *
     * This file contains one Progressive discounts: "Desconto Assinatura Nosso Modo".
     *  	The discount rate for the base value(195) does not exist in the database.
     *
     */
    public void testAccount7() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r02_1/ACCOUNT7.BGH");
			// firing rules
			this.startupRuleEngine(RULES);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();

			// running over results list to check if consequences where ok
			Consequence c = this.results.get(0);
			assertEquals("Percentual de desconto não encontrado na Base.", c.getDescription());
			//Nome do Pacote/Serviço
			assertEquals("Desconto Assinatura Nosso Modo", c.getAttributes().getAttribute(0));
			//Valor Base para Desconto
			assertEquals("195.00", c.getAttributes().getAttribute(1).replaceAll(",", "."));
			//Medida Usada como Base
			assertEquals("Assinaturas", c.getAttributes().getAttribute(2));
			//Percentual de Desconto na Fatura
			assertEquals("11.00", c.getAttributes().getAttribute(3).replaceAll(",", "."));

			assertEquals(1, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

    /**
     * This file contains one error.
     *
     * This file contains one Progressive discounts: "Desconto Assinatura Nosso Modo".
     *  	The discount rate for the base value(195) does not exist in the database.
     *
     */
    public void testAccount8() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r02_1/ACCOUNT8.BGH");
			// firing rules
			this.startupRuleEngine(RULES);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();

			assertEquals(2, this.results.size());

			Consequence c = this.results.get(0);
			assertEquals("LDN Econômica 41 TIM", c.getAttributes().getAttributeValue1());

			c = this.results.get(1);
			assertEquals("LDI Econômica 41 TIM", c.getAttributes().getAttributeValue1());


		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

    /**
     * Ticket #244
     */
    public void testAccount9() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r02_1/ACCOUNT9.BGH");
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

    /**
     * The same file that is used on test01 but it is used the discount by UF ('GO'). refs #267
     */
    public void testAccount10() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r02_1/ACCOUNT10.BGH");
			// firing rules
			this.startupRuleEngine(RULES);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(2, this.results.size());

			for (Consequence c : this.results) {
				assertEquals("Percentual de desconto não confere com o cadastrado na Base.", c.getDescription());
				if (c.getAttributes().getAttributeValue1().equals("LDN Econômica 41 TIM")) {
					//Percentual de Desconto na Fatura
					assertEquals("5.00", c.getAttributes().getAttribute(3).replaceAll(",", "."));
					//Percentual de Desconto Esperado
					assertEquals("10.00", c.getAttributes().getAttribute(4).replaceAll(",", "."));
				} else {
					//Percentual de Desconto na Fatura
					assertEquals("10.00", c.getAttributes().getAttribute(3).replaceAll(",", "."));
					//Percentual de Desconto Esperado
					assertEquals("5.00", c.getAttributes().getAttribute(4).replaceAll(",", "."));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}


    /**
     * From ticket #300.
     *
     * There is one progressive discount: 'Desconto Assinatura Nosso Modo' and 18 contracts in the Invoice. But only
     * 	12 should be computed. It we compute all 18, the % rate may be the same, but the discounted amount will be
     *  different.
     */
    public void testAccount11() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r02_1/ACCOUNT11.BGH");
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
     * Ticket #309
     */
    public void testAccount12() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r02_1/ACCOUNT12.BGH");
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
     * Ticket #309
     */
    public void testAccount13() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r02_1/ACCOUNT13.BGH");
			// firing rules
			this.startupRuleEngine(RULES);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(1, this.results.size());
			assertEquals("Percentual de desconto não confere com o cadastrado na Base.", this.results.get(0).getDescription());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}
