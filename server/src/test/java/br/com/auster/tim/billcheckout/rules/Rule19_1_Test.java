package br.com.auster.tim.billcheckout.rules;

import java.util.HashMap;

import org.w3c.dom.Element;

import br.com.auster.billcheckout.consequence.Consequence;
import br.com.auster.billcheckout.consequence.telco.TelcoConsequence;
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
import br.com.auster.tim.billcheckout.tariff.MicrocellRatesCache;

public class Rule19_1_Test extends BaseRuleTest {

	private String[] RULES = { "src/main/conf/rules/R19.1-promotions-values-validation.drl" };

	private String[] GUIDING = { "src/test/resources/bgh/guiding/guiding.drl" };

	protected boolean customized = false;


    protected void createGlobals() {
    	super.createGlobals();
    	try {
        	if (customized) {

	    		Class.forName("org.apache.commons.dbcp.PoolingDriver");
	    		Class.forName("oracle.jdbc.driver.OracleDriver");
	    		SQLConnectionManager.init(DOMUtils.openDocument("bgh/guiding/pool.xml", false));

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

	    		Class.forName("org.apache.commons.dbcp.PoolingDriver");
	    		Class.forName("oracle.jdbc.driver.OracleDriver");
	    		SQLConnectionManager.init(DOMUtils.openDocument("bgh/guiding/pool.xml", false));

		    	Element conf = DOMUtils.openDocument("bgh/guiding/caches2.xml", false);

				MicrocellRatesCache microcellRatesCache = new MicrocellRatesCache();
				microcellRatesCache.configure(conf);
	    		this.workingMemory.setGlobal( "microcellRatesCache", microcellRatesCache);
        	}

    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

	/**
	 * This is the correct file with Event Usage in Promotion.
	 *
	 * Amount After Promotion = 3.0, calculated value = 3.0
	 * Price Value = 6
	 * Rounded Minutes = 0.5
	 */
	public void testAccount1() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r19_1/ACCOUNT1.BGH");
			// firing guiding
			this.customized = true;
			this.startupRuleEngine(GUIDING);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// firing rules
			this.customized = false;
			this.startupRuleEngine(RULES, true);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = { "Regra 19.1 - Validating promotion value"};
			int[] AGENDA_COUNT = { 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);			
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * This is the wrong file with Event Usage in Promotion but the amount after promotion was changed.
	 *
	 * Amount After Promotion = .0, calculated value = 3.0
	 * Price Value = 6
	 * Rounded Minutes = 0.5
	 */
	public void testAccount2() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r19_1/ACCOUNT2.BGH");
			// firing guiding
			this.customized = true;
			this.startupRuleEngine(GUIDING);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// firing rules
			this.customized = false;
			this.startupRuleEngine(RULES, true);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = { "Regra 19.1 - Validating promotion value"};
			int[] AGENDA_COUNT = { 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);			
			// running over results list to check if consequences where ok
			assertEquals(1, this.results.size());
			TelcoConsequence consequence = (TelcoConsequence) this.results.iterator().next();
			assertTrue( consequence.getAttributes().getAttributeValue8().startsWith("3") );
			assertTrue( consequence.getAttributes().getAttributeValue9().startsWith("2") );
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/**
	 * This file contains a MC that does not exist in the table MPUFFTAB
	 */
	public void testAccount3() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r19_1/ACCOUNT3.BGH");
			// firing guiding
			this.customized = true;
			this.startupRuleEngine(GUIDING);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// firing rules
			this.customized = false;
			this.startupRuleEngine(RULES, true);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = { "Regra 19.1 - Validating promotion value"};
			int[] AGENDA_COUNT = { 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);			
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/**
	 * This file contains one call in MC with the correct value and another one with wrong value.
	 * 
	 * Wrong call values:
	 	 * Scalefactor = 0.5
	 	 * Amount Before Promotion = 1.0
		 * Amount After Promotion = 0.5, calculated value = 0.6
	 */
	public void testAccount4() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r19_1/ACCOUNT4.BGH");
			// firing guiding
			this.customized = true;
			this.startupRuleEngine(GUIDING);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// firing rules
			this.customized = false;
			this.startupRuleEngine(RULES, true);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = { "Regra 19.1 - Validating promotion value"};
			int[] AGENDA_COUNT = { 2 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);			
			// running over results list to check if consequences where ok
			HashMap<String, Integer> counters = new HashMap<String, Integer>();
			for (Consequence c : this.results) {
				String k = c.getDescription();
				if (! counters.containsKey(k)) {
					counters.put(k, new Integer(0));
				}
				Integer d = counters.get(k);
				counters.put(k, new Integer(d.intValue() + 1));
			}
			assertEquals(1, this.results.size());
			assertNotNull(counters.get("Valor cobrado pela chamada não condiz com a tarifa promocional"));
			assertEquals(1, counters.get("Valor cobrado pela chamada não condiz com a tarifa promocional").intValue());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}	
	
}