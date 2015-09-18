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

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.w3c.dom.Element;

import br.com.auster.billcheckout.caches.CacheableKey;
import br.com.auster.billcheckout.caches.CacheableVO;
import br.com.auster.billcheckout.consequence.Consequence;
import br.com.auster.billcheckout.consequence.telco.TelcoConsequence;
import br.com.auster.common.sql.SQLConnectionManager;
import br.com.auster.common.xml.DOMUtils;
import br.com.auster.om.invoice.Account;
import br.com.auster.tim.billcheckout.param.ContractServicesPeriodCache;
import br.com.auster.tim.billcheckout.param.CustomerSituationCache;
import br.com.auster.tim.billcheckout.param.CustomerSituationVO;
import br.com.auster.tim.billcheckout.tariff.MegaTIMCache;
import br.com.auster.tim.billcheckout.tariff.MegaTIMVO;



/**
 * @author framos
 * @version $Id$
 *
 */
public class Rule34_1_ValidationTest extends BaseRuleTest {


	private String[] RULES = { "src/main/conf/rules/R34.1-megatim-init.drl", 
							   "src/main/conf/rules/R34.1-megatim-by-usage.drl", 
							   "src/main/conf/rules/R34.2-megatim-by-account.drl" };

	
    protected void createGlobals() {
        super.createGlobals();
        try {
	        Class.forName("org.apache.commons.dbcp.PoolingDriver");
			Class.forName("oracle.jdbc.driver.OracleDriver");
			SQLConnectionManager.init(DOMUtils.openDocument("bgh/guiding/pool.xml", false));
	
	    	Element lazyAlternateCfg = DOMUtils.openDocument("bgh/guiding/caches.xml", false);
	    	Element lazyNaturalCfg = DOMUtils.openDocument("bgh/guiding/caches2.xml", false);
	        
	    	MegaTIMCache megatimCache = new MegaTIMCache();
	        megatimCache.configure(lazyNaturalCfg);
	    	this.workingMemory.setGlobal("megatimCache", megatimCache);
	    	
	    	CustomerSituationCache customerSituationCache = new CustomerSituationCache();
	    	customerSituationCache.configure(lazyNaturalCfg);
	    	this.workingMemory.setGlobal("customerSituationCache", customerSituationCache);

	    	ContractServicesPeriodCache contractServicesPeriodCache = new ContractServicesPeriodCache();
	    	contractServicesPeriodCache.configure(lazyAlternateCfg);
	    	this.workingMemory.setGlobal("contractServicesPeriodCache", contractServicesPeriodCache);
	    	
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }	
	
    
	/**
	 * Account with no MegaTIM package.
	 * 
	 * Just making sure all is OK with the rules.
	 */
	public void testAccount1() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r34_1/ACCOUNT1.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = { "R34.1 - Find configured Megatim package",
					               "R34.1 - Init. R34.1 NamedHashMap" };
			int[] AGENDA_COUNT = { 5, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Account with one MegaTIM package.
	 * 
	 * This contract has no information about the service activation.
	 */
	public void testAccount2() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r34_1/ACCOUNT2.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = { "R34.1 - Find configured Megatim package",
					               "R34.1 - Init. R34.1 NamedHashMap" };
			int[] AGENDA_COUNT = { 2, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			// running over results list to check if consequences where ok
			assertEquals(1, this.results.size());
			TelcoConsequence c = (TelcoConsequence) this.results.get(0);
			assertEquals("A promoção MegaTIM não está ativada para este contrato.", c.getDescription());
			assertEquals("54360137", c.getAttributes().getAttributeValue1());
			assertEquals("MegaTIM Mensagens 2009", c.getAttributes().getAttributeValue4());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/**
	 * Account with one MegaTIM package and service is activated.
	 * 
	 * In this case, there is no OCC with the expected description, charging the MegaTIM package.
	 */
	public void testAccount3() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r34_1/ACCOUNT3.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = { "R34.1 - Find configured Megatim package",
					               "R34.1 - Init. R34.1 NamedHashMap",
					               "R34.2 - Beneffited more than configured for a service",
					               "R34.2 - Charged customer using uneligible promotion",
					               "R34.2 - Not charged customer when it was supposed to" };
			int[] AGENDA_COUNT = { 2, 1, 1, 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			// running over results list to check if consequences where ok
			assertEquals(1, this.results.size());
			TelcoConsequence c = (TelcoConsequence) this.results.get(0);
			assertEquals("A ativação da promoção Mega TIM não foi cobrada.", c.getDescription());
			assertEquals("54360138", c.getAttributes().getAttributeValue1());
			assertEquals("MegaTIM Mensagens 2009", c.getAttributes().getAttributeValue4());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/**
	 * Account with one MegaTIM package and service is activated.
	 * 
	 * Same version as ACCOUNT3, but now we have the OCC with the correct value.
	 */
	public void testAccount4() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r34_1/ACCOUNT4.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = { "R34.1 - Find configured Megatim package",
					               "R34.1 - Init. R34.1 NamedHashMap",
					               "R34.2 - Beneffited more than configured for a service",
					               "R34.2 - Charged customer using uneligible promotion",
					               "R34.2 - Charged customer but value is incorrect" };
			int[] AGENDA_COUNT = { 2, 1, 1, 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}	

	/**
	 * Account with one MegaTIM package and service is activated.
	 * 
	 * Same version as ACCOUNT4, but the cache for OCC values is modified to retrieve -1 for current customers.
	 * 
	 * IMPORTANT: There is no need for a different file, since we are modifying the cache implementation.
	 */
	public void testAccount4_NoCharge_OldUser() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r34_1/ACCOUNT4.BGH");
			
			//
			// modifying cache implementation
			//
			MegaTIMCache megatimCache = new MegaTIMCache() {
				
				@Override
				public CacheableVO getFromCache(CacheableKey key) {
					MegaTIMVO vo = (MegaTIMVO) super.getFromCache(key);
					if (vo != null) {
						vo.setCurrentCustomerFee(-1);
					}
					return vo;
				}
			};
	        megatimCache.configure(DOMUtils.openDocument("bgh/guiding/caches2.xml", false));	        
	    	this.workingMemory.setGlobal("megatimCache", megatimCache);
	    	//
	    	// back to the testcase implementation
			//
	    	
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = { "R34.1 - Find configured Megatim package",
					               "R34.1 - Init. R34.1 NamedHashMap",
					               "R34.2 - Beneffited more than configured for a service",
					               "R34.2 - Charged customer using uneligible promotion",
					               "R34.2 - Charged customer but value is incorrect" };
			int[] AGENDA_COUNT = { 2, 1, 1, 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			// running over results list to check if consequences where ok
			DecimalFormat df = new DecimalFormat("#0.0000");
			
			assertEquals(1, this.results.size());
			TelcoConsequence c = (TelcoConsequence) this.results.get(0);
			assertEquals("Não existe tarifa configurada para clientes pré-existentes na base.", c.getDescription());
			assertEquals("54360138", c.getAttributes().getAttributeValue1());
			assertEquals("MegaTIM Mensagens 2009", c.getAttributes().getAttributeValue4());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}		
	
	/**
	 * Account with one MegaTIM package and service is activated.
	 * 
	 * Same version as ACCOUNT4, but the cache for OCC values is modified to retrieve -1 for new customers, and so is
	 *   the cache to identify if the customer is new/old.
	 * 
	 * IMPORTANT: There is no need for a different file, since we are modifying the caches implementations.
	 */
	public void testAccount4_NoCharge_NewUser() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r34_1/ACCOUNT4.BGH");
			
			//
			// modifying cache implementation
			//
			MegaTIMCache megatimCache = new MegaTIMCache() {
				
				@Override
				public CacheableVO getFromCache(CacheableKey key) {
					MegaTIMVO vo = (MegaTIMVO) super.getFromCache(key);
					if (vo != null) { 
						vo.setNewCustomerFee(-1);
					}
					return vo;
				}
			};
	        megatimCache.configure(DOMUtils.openDocument("bgh/guiding/caches2.xml", false));	        
	    	this.workingMemory.setGlobal("megatimCache", megatimCache);
	    	
	    	CustomerSituationCache customerSituationCache = new CustomerSituationCache() {
	    		
	    		@Override
	    		public CacheableVO getFromCache(CacheableKey key) {
	    			CustomerSituationVO vo = (CustomerSituationVO) super.getFromCache(key);
	    			if (vo != null) {
		    			try {
			    			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
			    			vo.setInsertDate(sdf.parse("20/06/2009"));
		    			} catch (ParseException pe) {
		    				pe.printStackTrace();
		    			}
	    			}
	    			return vo;
	    		}
	    	};
	    	customerSituationCache.configure(DOMUtils.openDocument("bgh/guiding/caches2.xml", false));
	    	this.workingMemory.setGlobal("customerSituationCache", customerSituationCache);
	    	//
	    	// back to the testcase implementation
			//
	    	
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = { "R34.1 - Find configured Megatim package",
					               "R34.1 - Init. R34.1 NamedHashMap",
					               "R34.2 - Beneffited more than configured for a service",
					               "R34.2 - Charged customer using uneligible promotion",
					               "R34.2 - Charged customer but value is incorrect" };
			int[] AGENDA_COUNT = { 2, 1, 1, 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			// running over results list to check if consequences where ok
			DecimalFormat df = new DecimalFormat("#0.0000");
			
			assertEquals(1, this.results.size());
			TelcoConsequence c = (TelcoConsequence) this.results.get(0);
			assertEquals("Não existe tarifa configurada para clientes novos na base.", c.getDescription());
			assertEquals("54360138", c.getAttributes().getAttributeValue1());
			assertEquals("MegaTIM Mensagens 2009", c.getAttributes().getAttributeValue4());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}	
	
	/**
	 * Account with one MegaTIM package and service is activated.
	 * 
	 * Same version as ACCOUNT4, but now we the OCC has an incorrect value.
	 */
	public void testAccount5() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r34_1/ACCOUNT5.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = { "R34.1 - Find configured Megatim package",
					               "R34.1 - Init. R34.1 NamedHashMap",
					               "R34.2 - Beneffited more than configured for a service",
					               "R34.2 - Charged customer using uneligible promotion",
					               "R34.2 - Charged customer but value is incorrect" };
			int[] AGENDA_COUNT = { 2, 1, 1, 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			// running over results list to check if consequences where ok
			DecimalFormat df = new DecimalFormat("#0.0000");
			
			assertEquals(1, this.results.size());
			TelcoConsequence c = (TelcoConsequence) this.results.get(0);
			assertEquals("Valor cobrado para a ativação não confere com o cadastrado.", c.getDescription());
			assertEquals("54360138", c.getAttributes().getAttributeValue1());
			assertEquals("MegaTIM Mensagens 2009", c.getAttributes().getAttributeValue4());
			assertEquals(9.0d, df.parse(c.getAttributes().getAttributeValue5()).doubleValue(), 0.01);
			assertEquals(12.0d, df.parse(c.getAttributes().getAttributeValue6()).doubleValue(), 0.01);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}	

	/**
	 * Account with one MegaTIM package and service is activated.
	 * 
	 * Same version as ACCOUNT3, and all usage events are in the invoice too.
	 */
	public void testAccount6() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r34_1/ACCOUNT6.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = { "R34.1 - Find configured Megatim package",
					               "R34.1 - Init. R34.1 NamedHashMap",
					               "R34.2 - Beneffited more than configured for a service",
					               "R34.2 - Charged customer using uneligible promotion",
					               "R34.2 - Charged customer but value is incorrect",
					               "R34.2 - Sum all benefitted usages" };
			int[] AGENDA_COUNT = { 2, 1, 1, 1, 1, 200 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/**
	 * Same version as ACCOUNT6, but the cache for usage limits was modified to return the limit 10, always.
	 * 
	 * IMPORTANT: There is no need for a different file, since we are modifying the cache implementation.
	 */
	public void testAccount6_LimitExceeded() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r34_1/ACCOUNT6.BGH");
			this.assertAccount(acc);
			//
			// modifying cache implementation
			//
			MegaTIMCache megatimCache = new MegaTIMCache() {
				
				@Override
				public CacheableVO getFromCache(CacheableKey key) {
					MegaTIMVO vo = (MegaTIMVO) super.getFromCache(key);
					if (vo != null) {
						for (String svc : vo.getAllUsageLimits().keySet()) {
							vo.addUsageLimit(svc, new Integer(100));
						}
					}
					return vo;
				}
			};
	        megatimCache.configure(DOMUtils.openDocument("bgh/guiding/caches2.xml", false));	        
	    	this.workingMemory.setGlobal("megatimCache", megatimCache);
	    	//
	    	// back to the testcase implementation
			//
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = { "R34.1 - Find configured Megatim package",
		               "R34.1 - Init. R34.1 NamedHashMap",
		               "R34.2 - Beneffited more than configured for a service",
		               "R34.2 - Charged customer using uneligible promotion",
		               "R34.2 - Charged customer but value is incorrect",
		               "R34.2 - Sum all benefitted usages" };
			int[] AGENDA_COUNT = { 2, 1, 1, 1, 1, 200 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			// running over results list to check if consequences where ok			
			DecimalFormat df = new DecimalFormat("#0.0000");
			
			assertEquals(1, this.results.size());
			Consequence c = this.results.get(0);
			assertEquals("O volume de benefícios destinados a este contrato excederam o limite configurado.", c.getDescription());
			assertEquals("54360138", c.getAttributes().getAttributeValue1());
			assertEquals("MegaTIM Mensagens 2009", c.getAttributes().getAttributeValue4());
			assertEquals(200, df.parse(c.getAttributes().getAttributeValue7()).intValue());
			assertEquals(100, df.parse(c.getAttributes().getAttributeValue8()).intValue());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}	
	
	/**
	 * Account with one MegaTIM package and service is activated.
	 * 
	 * Same version as ACCOUNT6, but two events from 17/06 had their service modified from TS22 to TS11
	 */
	public void testAccount7() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r34_1/ACCOUNT7.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = { "R34.1 - Find configured Megatim package",
					               "R34.1 - Init. R34.1 NamedHashMap",
					               "R34.2 - Beneffited more than configured for a service",
					               "R34.2 - Charged customer using uneligible promotion",
					               "R34.2 - Charged customer but value is incorrect",
					               "R34.2 - Sum all benefitted usages",
					               "R34.1 - Used service does not belong to configured list of available services" };
			int[] AGENDA_COUNT = { 2, 1, 1, 1, 1, 200, 2 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			// running over results list to check if consequences where ok
			assertEquals(2, this.results.size());
			for (Consequence c : this.results) {
				assertEquals("O evento beneficiado não é elegível para a promoção.", c.getDescription());
				assertEquals("54360138", c.getAttributes().getAttributeValue1());
				assertEquals("2009-06-17T00:10:00", c.getAttributes().getAttributeValue3());
				assertEquals("MegaTIM Mensagens 2009", c.getAttributes().getAttributeValue9());
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/**
	 * Account with one MegaTIM package and service is activated.
	 * 
	 * Same version as ACCOUNT6, but two events from 17/06/09 had their year modified to 2008.
	 */
	public void testAccount8() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r34_1/ACCOUNT8.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = { "R34.1 - Find configured Megatim package",
					               "R34.1 - Init. R34.1 NamedHashMap",
					               "R34.2 - Beneffited more than configured for a service",
					               "R34.2 - Charged customer using uneligible promotion",
					               "R34.2 - Charged customer but value is incorrect",
					               "R34.2 - Sum all benefitted usages",
					               "R34.1 - MegaTIM benefit on usage after expiration date" };
			int[] AGENDA_COUNT = { 2, 1, 1, 1, 1, 200, 2 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			// running over results list to check if consequences where ok
			assertEquals(2, this.results.size());
			for (Consequence c : this.results) {
				assertEquals("O evento beneficiado encontra-se fora do período de validade da promoção.", c.getDescription());
				assertEquals("54360138", c.getAttributes().getAttributeValue1());
				assertEquals("2008-06-17T00:10:00", c.getAttributes().getAttributeValue3());
				assertEquals("MegaTIM Mensagens 2009", c.getAttributes().getAttributeValue9());
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}


	/**
	 * From ticket #388
	 */
	public void testAccount9() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r34_1/ACCOUNT9.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
//			String[] RULE_LIST = { "R34.1 - Find configured Megatim package",
//					               "R34.1 - Init. R34.1 NamedHashMap",
//					               "R34.2 - Beneffited more than configured for a service",
//					               "R34.2 - Charged customer using uneligible promotion",
//					               "R34.2 - Charged customer but value is incorrect",
//					               "R34.2 - Sum all benefitted usages",
//					               "R34.1 - MegaTIM benefit on usage after expiration date" };
//			int[] AGENDA_COUNT = { 2, 1, 1, 1, 1, 200, 2 };
//			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			// running over results list to check if consequences where ok
			assertEquals(2, this.results.size());
//			for (Consequence c : this.results) {
//				assertEquals("O evento beneficiado encontra-se fora do período de validade da promoção.", c.getDescription());
//				assertEquals("54360138", c.getAttributes().getAttributeValue1());
//				assertEquals("2008-06-17T00:10:00", c.getAttributes().getAttributeValue3());
//				assertEquals("MegaTIM Mensagens 2009", c.getAttributes().getAttributeValue9());
//			}
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
}

