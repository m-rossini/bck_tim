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
 * Created on Apr 8, 2009
 */
package br.com.auster.tim.billcheckout.rules;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import br.com.auster.common.sql.SQLConnectionManager;
import br.com.auster.common.xml.DOMUtils;
import br.com.auster.om.invoice.Account;
import br.com.auster.tim.billcheckout.param.ContractServicesPeriodCache;
import br.com.auster.tim.billcheckout.param.PackageCache;
import br.com.auster.tim.billcheckout.param.ServicesCache;
import br.com.auster.tim.billcheckout.param.UsageGroupCache;

/**
 * @author Nardo
 * @version $Id$
 * @since JDK1.4
 */
public class Rule30_1_Test extends BaseRuleTest {
	
	private String[] RULES = { "src/main/conf/rules/R30.1-month-internet.drl" };
	
	protected boolean customized = false;
	private  static final Logger log = Logger.getLogger(Rule30_1_Test.class);

	 protected void createGlobals() {
	    	super.createGlobals();
	    	try {
	    		Class.forName("org.apache.commons.dbcp.PoolingDriver");
	    		Class.forName("oracle.jdbc.driver.OracleDriver");
	    		SQLConnectionManager.init(DOMUtils.openDocument("bgh/guiding/pool.xml", false));

		    	Element conf = DOMUtils.openDocument("bgh/guiding/caches.xml", false);
		    	
	    		PackageCache packCache =  new PackageCache();
	    		packCache.configure(conf);    		
	    		workingMemory.setGlobal( "packageCache", packCache );
	    		
	    		UsageGroupCache usageGroupCache =  new UsageGroupCache();
	    		usageGroupCache.configure(conf);    		
	    		workingMemory.setGlobal( "usageGroupCache", usageGroupCache );
	    		
	    		ContractServicesPeriodCache contrServPeriodCache =  new ContractServicesPeriodCache();
	    		contrServPeriodCache.configure(conf);    		
	    		workingMemory.setGlobal( "contractServicesPeriodCache", contrServPeriodCache );
	    		
	    		ServicesCache svcCache = new ServicesCache();
		    	svcCache.configure(conf);
		    	this.workingMemory.setGlobal("servicesAlternateCache", svcCache);
	    		
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    		fail();
	    	}
	    }	
	/**
	 * Cenário-1, data anterior à ativação do pacote(período ativo).
	 */
	public void testAccount1() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
			Account act = this.loadBGHTestFile("bgh/r30_1/ACCOUNT1_ORIG-Cen1.BGH");
			this.assertAccount(act);
			this.workingMemory.fireAllRules();
			
			//imprimindo consequências ak, pois se depois do assert e o numero de consequencias não bater, não imprime
			log.info(results);
			
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	/**
	 * Cenário-1, onde as seções TIM Connect Fast(06/04/08) e TIM Wap Fast(05/05/08) possuem evento de uso
	 * nas datas mencionadas conforme está ao lado destas.
	 * Estas datas são os valores extremos de ativação para o contrato 6331667, período ativo do pacote.
	 * 
	 * Apresenta as críticas quando:
	 * a) table QLF_PACKAGE com a coluna Custom_1 com valor 1 para um dos pacotes do contrato, na chave 34000100 
	 * b) table contr_services com registro para o numero do contrato (Query 1)
	 * c) table QLF_USAGE_GROUP a coluna Custom_1 está com 1 para estas seções
	 * d) Compara-se a data da fatura (inicio - fim de corte) com o período, e estando ativo, o valor em 51120000 sendo > 0,
	 *    será criticado
	 */
	 
	 
	public void testAccount2() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
			Account act = this.loadBGHTestFile("bgh/r30_1/ACCOUNT1_AlterDate1.BGH");
			this.assertAccount(act);
			this.workingMemory.fireAllRules();
			
			//imprimindo consequências ak, pois se depois do assert, e o numero de consequencias não bater, não imprime
			log.info(results);
			
			// running over results list to check if consequences where ok
			assertEquals(1, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}			
	
	public void testAccount3() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
			this.assertAccount(this.loadBGHTestFile("bgh/r30_1/ACCOUNT11.BGH"));
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
			
			//imprimindo consequências
			log.info(results);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/**
	 * Cenário-3, onde as seções TIM Connect Fast(06/04/08) e TIM Wap Fast(05/05/08) não possuem evento de uso.
	 * Contrato 6331667, com um pacote e período ativo.
	 * 
	 * Apresenta as críticas quando:
	 * a) table QLF_PACKAGE com a coluna Custom_1 com valor 1 para um dos pacotes do contrato, na chave 34000100
	 * b) table contr_services com registro para o numero do contrato (Query 1) 
	 * c) table QLF_USAGE_GROUP a coluna Custom_1 está com 0 ou null para estas seções
	 * d) Havendo o benefício, consumo > 0 e valor zerado, será criticado 
	 */
	 
	 
	public void testAccount4() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
			Account act = this.loadBGHTestFile("bgh/r30_1/ACCOUNT1_ORIG-Cen3.BGH");
			this.assertAccount(act);
			this.workingMemory.fireAllRules();
			
			//imprimindo consequências ak, pois se depois do assert e o numero de consequencias não bater, não imprime
			log.info(results);
			
			// running over results list to check if consequences where ok
			assertEquals(1, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	  Cenário-2, onde as seções TIM Connect Fast e TIM Wap Fast não possuem evento de uso.
	 * Contrato 6331667, sem nenhum pacote ativo.
	 * 
	 * Nenhum pacote ativo:
	 * A) na tag 34000100 não existir o número do contrato com um correspondente pacote
	 * B) na tag 34000100 existir o contrato e o pacote não possuir a flag ativa na tabela
	 * C) na tag 34000100 existir o contrato, o pacote possuir flag ativa na tabela QLF_PACKAGE e não haver retorno de registro para a Query 1.
	 * 
	 * Apresenta as críticas quando:
	 * a) independe da coluna Custom_1 da table QLF_USAGE_GROUP, para estas seções
	 * b) Havendo o benefício, consumo > 0 e valor zerado, será criticado
	 */
	public void testAccount5() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
			Account act = this
					.loadBGHTestFile("bgh/r30_1/ACCOUNT1_AlterContract.BGH");
			this.assertAccount(act);
			this.workingMemory.fireAllRules();
			
			//imprimindo consequências ak, pois se depois do assert e o numero de consequencias não bater, não imprime
			log.info(results);
			
			// running over results list to check if consequences where ok
			assertEquals(2, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	 /**
	  * Crítica: Cenário 2 - BGH Original, sem alterações onde a seção de dados do contrato 52180383 possui
	  * a seção de dados "Conexões Banda Larga" com consumo e valor zerado, sem possuir o pacote ativo.
	  */
	public void testAccount6() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
			Account act = this.loadBGHTestFile("bgh/r30_1/506720090214233434-00.BGH");
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
	 * Critica - Cenario 1: Pacote ativo no contrato, com cobrança em periodo ativo para uma seção ativa
	 * Critica - Cenario 3: Aplicação de consumo/valor zerado em contrato com pacote ativo, mas seção de dados não ativa
	 */
	public void testAccount7() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
			Account act = this.loadBGHTestFile("bgh/r30_1/506720090210183001-00.BGH");
			this.assertAccount(act);
			this.workingMemory.fireAllRules();
			log.info(results);
			assertEquals(2, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	/**
	* Ticket 327: Arquivo aparentemente original, enviado.
	*/
	public void testAccount8() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
			Account act = this.loadBGHTestFile("bgh/r30_1/30OD20090514234623-00.BGH");
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
	 * Ticket 327: Não critica cenário 2.
	 * Solução: Conforme cenário montado, regra comportando corretamente.
	 * 
	 * Cenário montado: 
	 * A) Seção de Dados com o detalhamento de uso(51110000, 51110010), mesmo com volume em 51120000 
	 * (name="subtotalActualCallDuration" index="3") e valor zerado (name="subTotalAmountAfterPromotionsDisc" index="5").
	 * 		51100000^TIM Wap Fast^D^D
	 * 		51110000^05/04/09^00:10:00^SP AREA 11^-^-^N^-^5.00000 MB^5.00000 MB^1.25^BSG1^GPR2^H^1^C^198.73^N^U^^0^0^0.00^0.00
	 * 		51110010^ICMS^25.00
	 * 		51120000^5.00 MB^5.00 MB^1.25^0.00^1^198.73^0.00 
	 * PARA ESTE CENÁRIO A, não gerará critica, pois existe o detalhamento de uso restrito na ET e sub-regra (eval ( $details.isEmpty() ))
	 * 
	 * B) Seção de Dados sem detalhamento(51110000, 51110010...), com volume usado e valor zerado(51120000), conforme visto abaixo:
	 * 		51100000^TIM Connect Fast^D^D
	 *		51120000^5.00 MB^5.00 MB^28.95^0.00^1^0.28^0.00
	 *PARA ESTE CENÁRIO B, gera crítica, conforme o esperado.
	 */
	public void testAccount9() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
			Account act = this.loadBGHTestFile("bgh/r30_1/30OD20090514234623-00_Alter.BGH");
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
