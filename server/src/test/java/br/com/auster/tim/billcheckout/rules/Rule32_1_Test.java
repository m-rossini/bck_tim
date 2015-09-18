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

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import br.com.auster.billcheckout.consequence.Consequence;
import br.com.auster.common.sql.SQLConnectionManager;
import br.com.auster.common.xml.DOMUtils;
import br.com.auster.om.invoice.Account;
import br.com.auster.tim.billcheckout.bscs.AnatelCodeCache;

/**
 * @author Gustavo Portugal
 * @version $Id$
 * @since JDK1.4
 */
public class Rule32_1_Test extends BaseRuleTest {

	private String[] RULES = { "src/main/conf/rules/R32.1-codigo-anatel-subscription.drl", 
			                   "src/main/conf/rules/R32.1-codigo-anatel-contract.drl" };
	
	private static final Logger log = Logger.getLogger(Rule32_1_Test.class);

    protected void createGlobals() {
    	super.createGlobals();
    	try {
    		Class.forName("org.apache.commons.dbcp.PoolingDriver");
    		Class.forName("oracle.jdbc.driver.OracleDriver");
    		SQLConnectionManager.init(DOMUtils.openDocument("bgh/guiding/pool.xml", false));

    		// use non-alternate cache configuration
	    	Element arg0 = DOMUtils.openDocument("bgh/guiding/caches2.xml", false);

	    	AnatelCodeCache anatelCodeCache = new AnatelCodeCache();
	    	anatelCodeCache.configure(arg0);
	    	this.workingMemory.setGlobal("anatelCodeCache", anatelCodeCache);

    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
	/**
	 * Original file.
	 * 
	 *  All 51000000 are missing the AnatelCode. Since we do not have 12100000 tags, they will
	 *     all be caught in the last rule.
	 */
	public void testAccount1() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r32_1/ACCOUNT1.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// all contracts do not have the code to match the package
			assertEquals(302, this.results.size());
			// checking asserting/firing
			String[] RULE_LIST = {
					"R32-1 - Validate when package does not have a rateplan - Step 1",
					"R32-1 - Validate when package does not have a rateplan - Step 2",
					"R32-1 - Validate when package does not have a rateplan - Step 3"};
			int[] AGENDA_COUNT = { 1, 302, 302 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * All OK
	 */
	public void testAccount2() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r32_1/ACCOUNT2.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			assertEquals(0, this.results.size());
			// checking asserting/firing
			String[] RULE_LIST = {
					"R32-1 - Validate when no package is found - Contract version",
					"R32-1 - Validate when there is a package - Contract version",
					"R32-1 - Validate when package does not have a rateplan - Step 1",
					"R32-1 - Validate when package does not have a rateplan - Step 3"};
			int[] AGENDA_COUNT = { 0, 1, 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}	
	
	/**
	 * ACCOUNT2.BGH with different state
	 */
	public void testAccount2_1() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r32_1/ACCOUNT2.1.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			assertEquals(2, this.results.size());
			Iterator<Consequence> it = this.results.iterator(); 
			assertEquals("A combinação plano/pacote/UF deste Contrato não possui Código Anatel cadastrado.", 
					     it.next().getDescription());
			assertEquals("A combinação plano/pacote/UF deste Contrato não possui Código Anatel cadastrado.", 
				     	 it.next().getDescription());
			// checking asserting/firing
			String[] RULE_LIST = {
					"R32-1 - Validate when no package is found - Contract version",
					"R32-1 - Validate when there is a package - Contract version",
					"R32-1 - Validate when package does not have a rateplan - Step 1",
					"R32-1 - Validate when package does not have a rateplan - Step 3"};
			int[] AGENDA_COUNT = { 0, 2, 1, 2 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}	

	/**
	 * ACCOUNT2.BGH where package has wrong anatel code
	 */
	public void testAccount2_2() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r32_1/ACCOUNT2.2.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			assertEquals(2, this.results.size());
			Iterator<Consequence> it = this.results.iterator();
			assertEquals("O Código Anatel do Pacote não corresponde ao código cadastrado.", 
					     it.next().getDescription());
			assertEquals("O Código Anatel do Pacote não corresponde ao código cadastrado.", 
				     	 it.next().getDescription());
			// checking asserting/firing
			String[] RULE_LIST = {
					"R32-1 - Validate when no package is found - Contract version",
					"R32-1 - Validate when there is a package - Contract version",
					"R32-1 - Validate when package does not have a rateplan - Step 1",
					"R32-1 - Validate when package does not have a rateplan - Step 3"};
			int[] AGENDA_COUNT = { 0, 2, 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}		
	
	/**
	 * ACCOUNT2.BGH where contract has wrong anatel code
	 */
	public void testAccount2_3() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r32_1/ACCOUNT2.3.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			assertEquals(1, this.results.size());
			assertEquals("O Código Anatel do Plano não corresponde ao código cadastrado.", 
					     this.results.iterator().next().getDescription());
			// checking asserting/firing
			String[] RULE_LIST = {
					"R32-1 - Validate when no package is found - Contract version",
					"R32-1 - Validate when there is a package - Contract version",
					"R32-1 - Validate when package does not have a rateplan - Step 1",
					"R32-1 - Validate when package does not have a rateplan - Step 3"};
			int[] AGENDA_COUNT = { 0, 1, 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}	
	
	/**
	 * ACCOUNT2.BGH where contract & package have wrong anatel code, but they are the same 
	 */
	public void testAccount2_4() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r32_1/ACCOUNT2.4.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			assertEquals(1, this.results.size());
			assertEquals("Código Anatel da combinação Plano/Pacote/UF não corresponde ao código cadastrado.", 
					     this.results.iterator().next().getDescription());
			// checking asserting/firing
			String[] RULE_LIST = {
					"R32-1 - Validate when no package is found - Contract version",
					"R32-1 - Validate when there is a package - Contract version",
					"R32-1 - Validate when package does not have a rateplan - Step 1",
					"R32-1 - Validate when package does not have a rateplan - Step 3"};
			int[] AGENDA_COUNT = { 0, 1, 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/**
	 * ACCOUNT2.BGH where contract & package have wrong anatel code, and they are the different from each other 
	 */
	public void testAccount2_5() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r32_1/ACCOUNT2.5.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			assertEquals(1, this.results.size());
			assertEquals("Os Códigos Anatel do Plano e do Pacote não correspondem ao código cadastrado.", 
					     this.results.iterator().next().getDescription());
			// checking asserting/firing
			String[] RULE_LIST = {
					"R32-1 - Validate when no package is found - Contract version",
					"R32-1 - Validate when there is a package - Contract version",
					"R32-1 - Validate when package does not have a rateplan - Step 1",
					"R32-1 - Validate when package does not have a rateplan - Step 3"};
			int[] AGENDA_COUNT = { 0, 1, 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}		
	
	/**
	 * ACCOUNT2.BGH - Same situation as 2.2, but now with 2 contracts and 4 packages
	 */
	public void testAccount2_6() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r32_1/ACCOUNT2.6.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			assertEquals(8, this.results.size());
			Iterator<Consequence> it = this.results.iterator();
			while (it.hasNext()) {
			assertEquals("O Código Anatel do Pacote não corresponde ao código cadastrado.", 
					     it.next().getDescription());
			}
			// checking asserting/firing
			String[] RULE_LIST = {
					"R32-1 - Validate when no package is found - Contract version",
					"R32-1 - Validate when there is a package - Contract version",
					"R32-1 - Validate when package does not have a rateplan - Step 1",
					"R32-1 - Validate when package does not have a rateplan - Step 3"};
			int[] AGENDA_COUNT = { 0, 8, 1, 4 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}		
	
	/**
	 * This account does not have any package with anatel code, but the rateplan does not support such combination
	 */
	public void testAccount3() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r32_1/ACCOUNT3.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			assertEquals(1, this.results.size());
			assertEquals("A combinação plano/pacote/UF deste Contrato não possui Código Anatel cadastrado.", 
					     this.results.iterator().next().getDescription());
			// checking asserting/firing
			String[] RULE_LIST = {
					"R32-1 - Validate when no package is found - Contract version",
					"R32-1 - Validate when package does not have a rateplan - Step 1"};
			int[] AGENDA_COUNT = { 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	public void testAccount3_1() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r32_1/ACCOUNT3.1.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			log.info(results);
			assertEquals(1, this.results.size());
			assertEquals("Código Anatel da combinação Plano/Pacote/UF não corresponde ao código cadastrado.", 
					     this.results.iterator().next().getDescription());
			// checking asserting/firing
			String[] RULE_LIST = {
					"R32-1 - Validate when no package is found - Contract version",
					"R32-1 - Validate when package does not have a rateplan - Step 1"};
			int[] AGENDA_COUNT = { 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/**
	 * Now ACCOUNT3 file is correct.
	 */
	public void testAccount3_2() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r32_1/ACCOUNT3.2.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			assertEquals(0, this.results.size());
			// checking asserting/firing
			String[] RULE_LIST = {
					"R32-1 - Validate when no package is found - Contract version",
					"R32-1 - Validate when package does not have a rateplan - Step 1"};
			int[] AGENDA_COUNT = { 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}		

	/**
	 * Now ACCOUNT4 blocks any consequences since there is no package, and the rateplan NOT have more than 1
	 *   package configured in the database.
	 */
	public void testAccount4() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r32_1/ACCOUNT4.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			log.info(results);
			assertEquals(2, this.results.size());
			// checking asserting/firing
			String[] RULE_LIST = {
					"R32-1 - Validate when no package is found",
					"R32-1 - Validate when no package is found - Contract version",
					"R32-1 - Validate when package does not have a rateplan - Step 1"};
			int[] AGENDA_COUNT = { 1, 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}		
	
	/**
	 * Changed rateplan to one that has only 1 package configured. Now we will get 2 consequences,
	 *    since the combination is not ok.
	 */
	public void testAccount4_1() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r32_1/ACCOUNT4.1.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			assertEquals(2, this.results.size());
			Iterator<Consequence> it = this.results.iterator();
			while (it.hasNext()) {
			assertEquals("A combinação plano/pacote/UF deste Contrato não possui Código Anatel cadastrado.", 
					     it.next().getDescription());
			}
			// checking asserting/firing
			String[] RULE_LIST = {
					"R32-1 - Validate when no package is found",
					"R32-1 - Validate when no package is found - Contract version",
					"R32-1 - Validate when package does not have a rateplan - Step 1"};
			int[] AGENDA_COUNT = { 1, 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/**
	 * ACCOUNT5.BGH is OK
	 */
	public void testAccount5() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r32_1/ACCOUNT5.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			assertEquals(0, this.results.size());
			// checking asserting/firing
			String[] RULE_LIST = {
					"R32-1 - Validate when no package is found",
					"R32-1 - Validate when there is a package",
					"R32-1 - Validate when package does not have a rateplan - Step 1",
					"R32-1 - Validate when package does not have a rateplan - Step 2"};
			int[] AGENDA_COUNT = { 0, 1, 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}		
	
	/**
	 * ACCOUNT5.BGH package code modified
	 */
	public void testAccount5_1() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r32_1/ACCOUNT5.1.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			assertEquals(1, this.results.size());
			assertEquals("A combinação plano/pacote/UF deste Contrato não possui Código Anatel cadastrado.", 
				         this.results.iterator().next().getDescription());
			// checking asserting/firing
			String[] RULE_LIST = {
					"R32-1 - Validate when no package is found",
					"R32-1 - Validate when there is a package",
					"R32-1 - Validate when package does not have a rateplan - Step 1",
					"R32-1 - Validate when package does not have a rateplan - Step 2"};
			int[] AGENDA_COUNT = { 0, 1, 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/**
	 * ACCOUNT5.BGH package/subscription anatel code are equal, but dont match the database
	 */
	public void testAccount5_2() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r32_1/ACCOUNT5.2.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			assertEquals(1, this.results.size());
			assertEquals("Código Anatel da combinação Plano/Pacote/UF não corresponde ao código cadastrado.", 
				         this.results.iterator().next().getDescription());
			// checking asserting/firing
			String[] RULE_LIST = {
					"R32-1 - Validate when no package is found",
					"R32-1 - Validate when there is a package",
					"R32-1 - Validate when package does not have a rateplan - Step 1",
					"R32-1 - Validate when package does not have a rateplan - Step 2"};
			int[] AGENDA_COUNT = { 0, 1, 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}	
	
	/**
	 * ACCOUNT5.BGH package/subscription anatel code are different, and both dont match the database
	 */
	public void testAccount5_3() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r32_1/ACCOUNT5.3.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			assertEquals(1, this.results.size());
			assertEquals("Os Códigos Anatel do Plano e do Pacote não correspondem ao código cadastrado.", 
				         this.results.iterator().next().getDescription());
			// checking asserting/firing
			String[] RULE_LIST = {
					"R32-1 - Validate when no package is found",
					"R32-1 - Validate when there is a package",
					"R32-1 - Validate when package does not have a rateplan - Step 1",
					"R32-1 - Validate when package does not have a rateplan - Step 2"};
			int[] AGENDA_COUNT = { 0, 1, 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * ACCOUNT5.BGH package/subscription anatel code are different, and only subscription is OK
	 */
	public void testAccount5_4() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r32_1/ACCOUNT5.4.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			assertEquals(1, this.results.size());
			assertEquals("O Código Anatel do Pacote não corresponde ao código cadastrado.", 
				         this.results.iterator().next().getDescription());
			// checking asserting/firing
			String[] RULE_LIST = {
					"R32-1 - Validate when no package is found",
					"R32-1 - Validate when there is a package",
					"R32-1 - Validate when package does not have a rateplan - Step 1",
					"R32-1 - Validate when package does not have a rateplan - Step 2"};
			int[] AGENDA_COUNT = { 0, 1, 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}		
	

	/**
	 * ACCOUNT5.BGH package/subscription anatel code are different, and only package is OK
	 */
	public void testAccount5_5() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r32_1/ACCOUNT5.5.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			assertEquals(1, this.results.size());
			assertEquals("O Código Anatel do Plano não corresponde ao código cadastrado.", 
				         this.results.iterator().next().getDescription());
			// checking asserting/firing
			String[] RULE_LIST = {
					"R32-1 - Validate when no package is found",
					"R32-1 - Validate when there is a package",
					"R32-1 - Validate when package does not have a rateplan - Step 1",
					"R32-1 - Validate when package does not have a rateplan - Step 2"};
			int[] AGENDA_COUNT = { 0, 1, 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	/**
	 * #341 - Arquivo original com suposto erro, o que em teste não foi constatado a crítica indevida.
	 * Gerando a crítica: A combinação plano/pacote/UF deste Contrato não possui Código Anatel cadastrado.
	 * 
	 * Detectado para o teste da TIM: Faltou o owner SYSADM, não encontrando a tabela, e, mesmo dando exception,
	 * entrou na regra e gerou a crítica.
	 */
	public void testAccount6() {
		try {
			this.startupRuleEngine(RULES);
			Account act = this.loadBGHTestFile("bgh/r32_1/ACCOUNT6.BGH");
			this.assertAccount(act);
			this.workingMemory.fireAllRules();
			
			log.info(results);
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		  }
	}
	/**
	 * #342: BGH (30OD20090727161321-00_erro.BGH) já alterado o código Anatel, na seguinte linha:
	 * 12200000^Pacote TIM iPhone 100 min^011-8460-4455^54380635^31^1.00^93.00^93.00^037/PÓS/SMP^NPC1^NPASP
	 * 
	 *  Original era:
	 *  12200000^Pacote TIM iPhone 100 min^011-8460-4455^54380635^31^1.00^93.00^93.00^036/PÓS/SMP^NPC1^NPASP
	 *  
	 *  Não estava gerando crítica, mesmo código Anatel divergente, visto haver para o mesmo Plano + Estado, mais que uma
	 *  combinação de pacote. Query select count()... configurada para a regra retorna 3.
	 *  
	 *  Solução: Bug corrigido, alterando a query citada, colocando distinct.
	 */
	public void testAccount7() {
		try {
			this.startupRuleEngine(RULES);
			Account act = this.loadBGHTestFile("bgh/r32_1/ACCOUNT7.BGH");
			this.assertAccount(act);
			this.workingMemory.fireAllRules();
			
			log.info(results);
			assertEquals(1, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	/**
	 *	#343 - BGH sem existência da tag de pacote 12200000, que gerava crítica indevida, mesmo com registro na base.
	 *	Nome do Arquivo: 50OD20090722020447-00.BGH
	 *	Crítica que gerava: A combinação plano/pacote/UF deste Contrato não possui Código Anatel cadastrado. 
	 *	Solução: Bug corrigido, por parâmetro passado erradamente. Ver Show History da validação de subscription.
	 */
	public void testAccount8() {
		try {
			this.startupRuleEngine(RULES);
			Account act = this.loadBGHTestFile("bgh/r32_1/ACCOUNT8.BGH");
			this.assertAccount(act);
			this.workingMemory.fireAllRules();
			
			log.info(results);
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}


