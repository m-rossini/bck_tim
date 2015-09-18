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
import java.util.HashMap;

import org.apache.log4j.xml.DOMConfigurator;
import org.w3c.dom.Element;

import br.com.auster.billcheckout.consequence.Consequence;
import br.com.auster.common.sql.SQLConnectionManager;
import br.com.auster.common.xml.DOMUtils;
import br.com.auster.om.invoice.Account;
import br.com.auster.tim.billcheckout.param.PlansCache;
import br.com.auster.tim.billcheckout.param.RateTimeZoneCache;
import br.com.auster.tim.billcheckout.param.ServicePlanCache;
import br.com.auster.tim.billcheckout.param.ServicesCache;
import br.com.auster.tim.billcheckout.param.TariffZoneCache;
import br.com.auster.tim.billcheckout.param.TariffZoneUsageGroupCache;
import br.com.auster.tim.billcheckout.param.UsageGroupCache;
import br.com.auster.tim.billcheckout.param.UsageGroupLDCache;
import br.com.auster.tim.billcheckout.tariff.PricePlanRatesCache;



/**
 * @author framos
 * @version $Id$
 *
 */
public class Rule01_23_Test extends BaseRuleTest {


	private String[] GUIDING = { "src/test/resources/bgh/guiding/guiding.drl" };
	
	private String[] RULES = { "src/main/conf/rules/R01.23-data-rating.drl" };

	
	private boolean runningGuiding = false;

	@Override
	protected void setUp() throws Exception {
    	DOMConfigurator.configure("src/test/resources/log4j.xml");
	}
	
    protected void createGlobals() {
    	
        super.createGlobals();
        try {
        	
    		Class.forName("org.apache.commons.dbcp.PoolingDriver");
			Class.forName("oracle.jdbc.driver.OracleDriver");
			SQLConnectionManager.init(DOMUtils.openDocument("bgh/guiding/pool.xml", false));
	
        	if (runningGuiding) {
	
		    	Element arg0 = DOMUtils.openDocument("bgh/guiding/caches.xml", false);
	
		    	ServicesCache svcCache = new ServicesCache();
		    	svcCache.configure(arg0);
		    	this.workingMemory.setGlobal("serviceCache", svcCache);
	
		    	PlansCache planCache = new PlansCache();
		    	planCache.configure(arg0);
		    	this.workingMemory.setGlobal("planCache", planCache);
	
		    	TariffZoneCache tZoneCache = new TariffZoneCache();
		    	tZoneCache.configure(arg0);
		    	this.workingMemory.setGlobal("tariffZoneCache", tZoneCache);
	
		    	UsageGroupCache ugCache = new UsageGroupCache();
		    	ugCache.configure(arg0);
		    	this.workingMemory.setGlobal("usageGroupCache", ugCache);
	
		    	RateTimeZoneCache rtzCache = new RateTimeZoneCache();
		    	rtzCache.configure(arg0);
		    	this.workingMemory.setGlobal("rateZoneCache", rtzCache);
	
		    	ServicePlanCache spcache = new ServicePlanCache();
		    	spcache.configure(arg0);
		    	this.workingMemory.setGlobal("servicePlanCache", spcache);
	
		    	TariffZoneUsageGroupCache tzugCache = new TariffZoneUsageGroupCache();
		    	tzugCache.configure(arg0);
		    	this.workingMemory.setGlobal("tariffZoneUsageGroupCache", tzugCache);
	
		    	UsageGroupLDCache usageGroupLDCache = new UsageGroupLDCache();
		    	usageGroupLDCache.configure(arg0);
		    	this.workingMemory.setGlobal("usageGroupLDCache", usageGroupLDCache);
		    	
        	} else {
        	
		    	Element lazyAlternateCfg = DOMUtils.openDocument("bgh/guiding/caches.xml", false);
		    	Element lazyNaturalCfg = DOMUtils.openDocument("bgh/guiding/caches2.xml", false);
		        
		        PricePlanRatesCache pricePlanCache = new PricePlanRatesCache();
		        pricePlanCache.configure(lazyNaturalCfg);
		        this.workingMemory.setGlobal("pricePlanCache",pricePlanCache);
		        
		        PricePlanRatesCache alternatePricePlanCache = new PricePlanRatesCache();
		        alternatePricePlanCache.configure(lazyAlternateCfg);
		        this.workingMemory.setGlobal("alternatePricePlanCache",alternatePricePlanCache);
        	}

        } catch (Exception e) {
        	e.printStackTrace();
        }
    }	
	
    
	/**
	 * This customer has no data usage
	 */
	public void testAccount1() {
		try {
			// loading invoice
			Account acc = this.loadBGHTestFile("bgh/r01_23/ACCOUNT1.BGH");
			// running guiding
			this.runningGuiding = true;
			this.startupRuleEngine(GUIDING);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running rules
			this.runningGuiding = false;
			this.startupRuleEngine(RULES, true);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = { "R01.23 - Build grouping map",
					               "R01.23 - Find which rate to use" 
								 };
			int[] AGENDA_COUNT = { 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/**
	 * This customer has data usage, but all TARIFF_ZONEs where modified so that there are no rate matches.
	 */
	public void testAccount2() {
		try {
			// loading invoice
			Account acc = this.loadBGHTestFile("bgh/r01_23/ACCOUNT2.BGH");
			// running guiding
			this.runningGuiding = true;
			this.startupRuleEngine(GUIDING);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running rules
			this.runningGuiding = false;
			this.startupRuleEngine(RULES, true);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = { "R01.23 - Build grouping map",
					               "R01.23 - Grouping Data Usage",
					               "R01.23 - Find which rate to use",
					               "R01.23 - Validate each tariff" 
					               };
			int[] AGENDA_COUNT = { 1, 88, 13, 88 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			// running over results list to check if consequences where ok
			assertEquals(88, this.results.size());
			for (Consequence c : this.results) {
				assertEquals("Não existe valor de tarifa configurado para este serviço.", c.getDescription());
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/**
	 * This is the ACCOUNT2.BGH invoice but a data package was added to contract 52436807.
	 * 
	 *  No modifications to its usage details were made due to the added package.
	 */
	public void testAccount3() {
		try {
			// loading invoice
			Account acc = this.loadBGHTestFile("bgh/r01_23/ACCOUNT3.BGH");
			// running guiding
			this.runningGuiding = true;
			this.startupRuleEngine(GUIDING);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running rules
			this.runningGuiding = false;
			this.startupRuleEngine(RULES, true);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = { "R01.23 - Build grouping map",
					               "R01.23 - Grouping Data Usage",
					               "R01.23 - Find which rate to use",
					               "R01.23 - Validate each tariff" 
					               };
			int[] AGENDA_COUNT = { 1, 88, 13, 88 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			// running over results list to check if consequences where ok
			assertEquals(88, this.results.size());
			for (Consequence c : this.results) {
				assertEquals("Não existe valor de tarifa configurado para este serviço.", c.getDescription());
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}	

	/**
	 * This is the ACCOUNT2.BGH invoice but a data package was added to contract 52436807.
	 * 
	 *  Three usage details where modified to be zeroed as free units from the added package.
	 */
	public void testAccount3_1() {
		try {
			// loading invoice
			Account acc = this.loadBGHTestFile("bgh/r01_23/ACCOUNT3.1.BGH");
			// running guiding
			this.runningGuiding = true;
			this.startupRuleEngine(GUIDING);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running rules
			this.runningGuiding = false;
			this.startupRuleEngine(RULES, true);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = { "R01.23 - Build grouping map",
					               "R01.23 - Grouping Data Usage",
					               "R01.23 - Find which rate to use",
					               "R01.23 - Validate each tariff" 
					               };
			int[] AGENDA_COUNT = { 1, 88, 13, 85 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			// running over results list to check if consequences where ok
			assertEquals(85, this.results.size());
			for (Consequence c : this.results) {
				assertEquals("Não existe valor de tarifa configurado para este serviço.", c.getDescription());
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}	

	/**
	 * This is only the contract 55868678 from ACCOUNT2.BGH, but all TARIFF_ZONEs are OK. 
	 * 
	 * The rates are set, and no errors are excpected.
	 */
	public void testAccount4() {
		try {
			// loading invoice
			Account acc = this.loadBGHTestFile("bgh/r01_23/ACCOUNT4.BGH");
			// running guiding
			this.runningGuiding = true;
			this.startupRuleEngine(GUIDING);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running rules
			this.runningGuiding = false;
			this.startupRuleEngine(RULES, true);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = { "R01.23 - Build grouping map",
					               "R01.23 - Grouping Data Usage",
					               "R01.23 - Find which rate to use",
					               "R01.23 - Validate each tariff" 
					               };
			int[] AGENDA_COUNT = { 1, 49, 10, 49 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}	
	
	/**
	 * This is only the contract 55868678 from ACCOUNT2.BGH, but all TARIFF_ZONEs are OK. 
	 * 
	 * The rates are set, and two details were modified to raise calculation errors.
	 */
	public void testAccount4NOK() {
		try {
			// loading invoice
			Account acc = this.loadBGHTestFile("bgh/r01_23/ACCOUNT4.NOK.BGH");
			// running guiding
			this.runningGuiding = true;
			this.startupRuleEngine(GUIDING);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running rules
			this.runningGuiding = false;
			this.startupRuleEngine(RULES, true);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = { "R01.23 - Build grouping map",
					               "R01.23 - Grouping Data Usage",
					               "R01.23 - Find which rate to use",
					               "R01.23 - Validate each tariff" 
					               };
			int[] AGENDA_COUNT = { 1, 49, 10, 49 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			// running over results list to check if consequences where ok
			assertEquals(2, this.results.size());
			
			// calculated amount map
			HashMap<String, Double> map = new HashMap<String, Double>();
			map.put("2010-02-12T02:37:27", 1.92d);
			map.put("2010-02-04T20:29:54", 4.96d);
			DecimalFormat df = new DecimalFormat("#0.0000");
			for (Consequence c : this.results) {
				assertEquals("Valor cobrado neste evento não coincide com o calculado.", c.getDescription());
				Double dbl = map.get(c.getAttributes().getAttributeValue4());
				assertNotNull(dbl);
				assertEquals(dbl.doubleValue(), df.parse(c.getAttributes().getAttributeValue7()).doubleValue(), 0.01);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/**
	 * From ticket #393
	 */
	public void testAccount5() {
		try {
			// loading invoice
			Account acc = this.loadBGHTestFile("bgh/r01_23/ACCOUNT5.BGH");
			// running guiding
			this.runningGuiding = true;
			this.startupRuleEngine(GUIDING);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running rules
			this.runningGuiding = false;
			this.startupRuleEngine(RULES, true);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = { "R01.23 - Build grouping map",
					               "R01.23 - Grouping Data Usage",
					               "R01.23 - Find which rate to use",
					               "R01.23 - Validate each tariff" 
					               };
			int[] AGENDA_COUNT = { 1, 20, 1, 12 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
//			// running over results list to check if consequences where ok
			assertEquals(3, this.results.size());
//			
//			// calculated amount map
//			HashMap<String, Double> map = new HashMap<String, Double>();
//			map.put("2010-02-12T02:37:27", 1.92d);
//			map.put("2010-02-04T20:29:54", 4.96d);
//			DecimalFormat df = new DecimalFormat("#0.0000");
//			for (Consequence c : this.results) {
//				assertEquals("Valor cobrado neste evento não coincide com o calculado.", c.getDescription());
//				Double dbl = map.get(c.getAttributes().getAttributeValue4());
//				assertNotNull(dbl);
//				assertEquals(dbl.doubleValue(), df.parse(c.getAttributes().getAttributeValue7()).doubleValue(), 0.01);
//			}
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}	
	
}
