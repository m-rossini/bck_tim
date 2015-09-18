package br.com.auster.tim.billcheckout.rules;

import java.util.HashMap;

import org.w3c.dom.Element;

import br.com.auster.billcheckout.consequence.Consequence;
import br.com.auster.common.sql.SQLConnectionManager;
import br.com.auster.common.xml.DOMUtils;
import br.com.auster.om.invoice.Account;
import br.com.auster.tim.billcheckout.param.MpufftabListCache;
import br.com.auster.tim.billcheckout.param.PlansCache;
import br.com.auster.tim.billcheckout.param.RateTimeZoneCache;
import br.com.auster.tim.billcheckout.param.ServicePlanCache;
import br.com.auster.tim.billcheckout.param.ServicesCache;
import br.com.auster.tim.billcheckout.param.TariffZoneCache;
import br.com.auster.tim.billcheckout.param.TariffZoneUsageGroupCache;
import br.com.auster.tim.billcheckout.param.UsageGroupCache;
import br.com.auster.tim.billcheckout.param.UsageGroupLDCache;

public class Rule19_3_Test extends BaseRuleTest {

	private String[] RULES = { "src/main/conf/rules/R19.3-promotions-limits-validation.drl" };

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

		    	Element conf = DOMUtils.openDocument("bgh/guiding/caches.xml", false);

		    	MpufftabListCache mpufftabListCache =  new MpufftabListCache();
		    	mpufftabListCache.configure(conf);
	    		this.workingMemory.setGlobal( "mpufftabListCache", mpufftabListCache);
        	}

    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

	/**
	 * This is the correct file with the calls duration < user limit configured.
	 *
	 * Promotions enabled for the contract: ABC and DPN
	 * Limit for DPN: 60 seconds
	 * Limit for ABC: 90 seconds
	 *
	 * Total duration for calls in DPN promotion: 54 seconds
	 * Total duration for calls in ABC promotion: 60 seconds
	 *
	 */
	public void testAccount1() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r19_3/ACCOUNT1.BGH");
			// firing guiding
			this.customized = true;
			this.startupRuleEngine(GUIDING);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
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
	 * This is the wrong file with the calls duration > user limit configured.
	 *
	 * Promotions enabled for the contract: ABC and DPN
	 * Limit for DPN: 60 seconds
	 * Limit for ABC: 90 seconds
	 *
	 * Total duration for calls in DPN promotion: 114 seconds
	 * Total duration for calls in ABC promotion: 126 seconds
	 *
	 */
	public void testAccount2() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r19_3/ACCOUNT2.BGH");
			// firing guiding
			this.customized = true;
			this.startupRuleEngine(GUIDING);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// firing rules
			this.customized = false;
			this.startupRuleEngine(RULES);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(2, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/**
	 * This file contains a contract number that has no promotion in the table MPUFFTAB.
	 */
	public void testAccount3() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r19_3/ACCOUNT3.BGH");
			// firing guiding
			this.customized = true;
			this.startupRuleEngine(GUIDING);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
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
	 * This is the wrong file with the calls duration > user limit configured, but the promotion 
	 * DEF has the LIMITED_FLAG = 'N'
	 *
	 * Promotions enabled for the contract: ABC and DPN
	 * Limit for DPN: 60 seconds
	 * Limit for DEF: 90 seconds
	 *
	 * Total duration for calls in DPN promotion: 174 seconds
	 * Total duration for calls in DEF promotion: 360 seconds
	 *
	 */
	public void testAccount4() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r19_3/ACCOUNT4.BGH");
			// firing guiding
			this.customized = true;
			this.startupRuleEngine(GUIDING);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// firing rules
			this.customized = false;
			this.startupRuleEngine(RULES);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
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
			assertNotNull(counters.get("Limite configurado para promoção foi ultrapassado"));
			assertEquals(1, counters.get("Limite configurado para promoção foi ultrapassado").intValue());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

}