/**
 * Copyright (c) 2004-2009 Auster Solutions. All Rights Reserved.
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
 * Created on May 26, 2009
 */
package br.com.auster.tim.billcheckout.rules;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import br.com.auster.common.sql.SQLConnectionManager;
import br.com.auster.common.xml.DOMUtils;
import br.com.auster.om.invoice.Account;
import br.com.auster.tim.billcheckout.bscs.ContractExistsPackCache;
import br.com.auster.tim.billcheckout.bscs.ScanStructureLevelCache;
import br.com.auster.tim.billcheckout.param.ContractExistsServicesCache;
import br.com.auster.tim.billcheckout.param.ElegibilityCache;
import br.com.auster.tim.billcheckout.param.PlansCache;

/**
 * @author Nardo
 * @version $Id$
 * @since JDK1.4
 */
public class Rule33_1_Test extends BaseRuleTest {
	
	private static final Logger log = Logger.getLogger(Rule33_1_Test.class);
	private String[] RULES = { "src/main/conf/rules/R33.1-discount-implicit.drl" };
	
	protected void createGlobals() {
    	super.createGlobals();
    	try {
    		Class.forName("org.apache.commons.dbcp.PoolingDriver");
    		Class.forName("oracle.jdbc.driver.OracleDriver");
    		SQLConnectionManager.init(DOMUtils.openDocument("bgh/guiding/pool.xml", false));
    		
    		// use alternate true cache configuration
	    	Element conf = DOMUtils.openDocument("bgh/guiding/caches.xml", false);
	    	// use alternate false cache configuration
	    	Element conf2 = DOMUtils.openDocument("bgh/guiding/caches2.xml", false);
	    	
	    	PlansCache plansCache = new PlansCache();
	    	plansCache.configure(conf);
	    	workingMemory.setGlobal("planCache", plansCache);
	    	
	    	ElegibilityCache elegibilityCache = new ElegibilityCache();
	    	elegibilityCache.configure(conf);
	    	workingMemory.setGlobal("elegibCache", elegibilityCache);
	    	
	    	ContractExistsServicesCache contrExistsServiceCache = new ContractExistsServicesCache();
	    	contrExistsServiceCache.configure(conf);
	    	workingMemory.setGlobal("contrExistsServCache", contrExistsServiceCache);
	    	
	    	ContractExistsPackCache contrExistsPack = new ContractExistsPackCache();
	    	contrExistsPack.configure(conf);
	    	workingMemory.setGlobal("contrExistsPackCache", contrExistsPack);
	    	
	    	ScanStructureLevelCache scanStructure = new ScanStructureLevelCache();
	    	scanStructure.configure(conf2);
	    	workingMemory.setGlobal("scanStructLevelCache", scanStructure);
	    	
    	} catch (Exception e) {
    		e.printStackTrace();
    		fail();
    	}
    }
	/* Conta do tipo FLAT, com aplicação de desvio em assinatura do plano, pois o contrato possuiu elegibilidade.
	 */
	public void testAccount1() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
			Account act = this.loadBGHTestFile("bgh/r33_1/Pacote_TIM_Casa_Flex.BGH");
			this.assertAccount(act);
			this.workingMemory.fireAllRules();
			
			//imprimindo consequências ak, pois se depois do assert e o numero de consequencias não bater, não imprime
			log.info(results);
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/*
	 * Conta do tipo FLAT simulada, onde tenho cobrança na tag 60000000 para a assinatura do plano
	 * divergente do existente na tabela ACC_PDA_ELEGIBILIDADE, coluna VLR_DESVIO, ao possuir um contrato eleito. 
	 * 
	 * Adicionado esta linha no BGH original-anterior:
	 *	60000000^021-8303-3870^53051356^0.01
	 */
	
	public void testAccount2() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
			Account act = this.loadBGHTestFile("bgh/r33_1/Pacote_TIM_Casa_Flex_Alter.BGH");
			this.assertAccount(act);
			this.workingMemory.fireAllRules();
			
			log.info(results);
			assertEquals(1, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/*
	 * Simulando para Q1 não retornar, para combinação Plan:
	 * 
	 * Tag: 00000000, estado RT - Alterado RJ por este RT
	 * 		51000000, plano Plano TIM Flex
	 */
	
	public void testAccount3() {
		try {
			this.startupRuleEngine(RULES);
			Account act = this.loadBGHTestFile("bgh/r33_1/Pacote_TIM_Casa_Flex_PlanInexist.BGH");
			this.assertAccount(act);
			this.workingMemory.fireAllRules();
			
			log.info(results);
			assertEquals(2, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	/*	Large Account - Conta de nível 10(custCode = 6.586431)
		Extraído do arquivo 30OD20081109225847-00.BGH
		Deve ter aplicação de desvio, pois possui contrato(52145408) com Plano Nosso Link, sem cobrança em tag 60000000.
		
		Para este BGH, houve aplicação de desvio, mas não foi detectado elegibilidade nos registros do DB.
		Porém, não havendo a tag 60000000, nenhuma critica eh gerada, conforme diz a ET, qdo da não existencia desta tag,
		e, também qdo da não existência de elegibilidade, nenhuma crítica é gerada.
	*/
	public void testAccount4() {
		try {
			this.startupRuleEngine(RULES);
			Account act = this.loadBGHTestFile("bgh/r33_1/PlanoNossoLink_nivel_10.BGH");
			this.assertAccount(act);
			this.workingMemory.fireAllRules();
			
			log.info(results);
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	/*	
	  Large Account - Conta de nível 40(custCode = 6.586432.00.00.100000)
	  Extraído do arquivo 20OD20081109225847-00.BGH
	  Houve elegibilidade individual e LA para o contrato 52145386 com Plano Nosso Link. Sem cobrança em tag 60000000.
	  Registro eleito, onde VLR_DESVIO == valor Zero, da tabela elegibilidade, conforme visto abaixo:
		4 L DADAC,DADAL,DADAM,DADAP,DADBA,DADCE,DADDF,DADES,DADGO,DADMA,DADMG,DADMS,DADMT,DADPA,DADPB,DADPE,DADPI,DADPR,
		    DADRJ,DADRN,DADRO,DADRR,DADRS,DADSC,DADSE,DADSP,DADTO,DADBR 	1FS10 	TS11,SGT01 	0 	17-nov-2008 14:21:33                     
	 */
	public void testAccount5() {
		try {
			this.startupRuleEngine(RULES);
			Account act = this.loadBGHTestFile("bgh/r33_1/PlanoNossoLink_nivel_40.BGH");
			this.assertAccount(act);
			this.workingMemory.fireAllRules();
			
			log.info(results);
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
		/*	
		Large Account - Conta de nível 40(custCode = 6.586432.00.00.100000)
		Extraído do arquivo 20OD20081109225847-00.BGH
		Houve elegibilidade Individual e LA para o contrato(52145386) com Plano Nosso Link, com COD_PROMO = 4, e VLR_DESVIO=0.
		No BGH original, a tag 60000000 nao aparece em virtude deste valor zerado, conforme consta em Doc's da TIM.
		Portanto simulei cobrança com a tag 60000000, fazendo gerar a critica.
		*/
		public void testAccount6() {
			try {
				this.startupRuleEngine(RULES);
				Account act = this.loadBGHTestFile("bgh/r33_1/PlanoNossoLink_nivel_40_Alter.BGH");
				this.assertAccount(act);
				this.workingMemory.fireAllRules();
				
				log.info(results);
				assertEquals(1, this.results.size());
			} catch (Exception e) {
				e.printStackTrace();
				fail();
			  }
		}
}
