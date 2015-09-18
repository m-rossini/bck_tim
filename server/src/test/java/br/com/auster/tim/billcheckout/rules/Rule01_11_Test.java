package br.com.auster.tim.billcheckout.rules;

import org.w3c.dom.Element;

import br.com.auster.billcheckout.consequence.Consequence;
import br.com.auster.common.sql.SQLConnectionManager;
import br.com.auster.common.xml.DOMUtils;
import br.com.auster.om.invoice.Account;
import br.com.auster.tim.billcheckout.param.PlansCache;
import br.com.auster.tim.billcheckout.param.RateTimeZoneCache;
import br.com.auster.tim.billcheckout.param.RoamingAgreeCache;
import br.com.auster.tim.billcheckout.param.ServicePlanCache;
import br.com.auster.tim.billcheckout.param.ServicesCache;
import br.com.auster.tim.billcheckout.param.TariffZoneCache;
import br.com.auster.tim.billcheckout.param.TariffZoneUsageGroupCache;
import br.com.auster.tim.billcheckout.param.UsageGroupCache;
import br.com.auster.tim.billcheckout.param.UsageGroupLDCache;

public class Rule01_11_Test extends BaseRuleTest {

	private String[] GUIDING = { "src/test/resources/bgh/guiding/guiding.drl" };
	private String[] RULES = { "src/main/conf/rules/R01.11-free-calls-without-benefit.drl" };


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

		    	RoamingAgreeCache roamingAgreeCache =  new RoamingAgreeCache();
		    	roamingAgreeCache.configure(conf);
	    		workingMemory.setGlobal( "roamingAgreeCache", roamingAgreeCache );
        	}

    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }


	/**
	 * This is the file that does not match the requirements of the rules filter.
	 */
	public void testAccount1() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r01_11/ACCOUNT1.BGH");
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
	 * Free local call without promotion (!AC)
	 * 51110000^08/01/07^22:10:00
	 */
	public void testAccount2() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r01_11/ACCOUNT2.BGH");
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
			assertEquals(1, this.results.size());
			Consequence c = this.results.iterator().next();
			assertEquals("Chamadas Locais para Celulares TIM", c.getAttributes().getAttributeValue3());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Free call in Roaming without promotion (!AC). The country destination has the roaming agree with TIM.
	 * 51110000^10/01/07^22:26:32
	 */
	public void testAccount3() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r01_11/ACCOUNT3.BGH");
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
			assertEquals(1, this.results.size());
			Consequence c = this.results.iterator().next();
			assertEquals("Roaming Internacional - Chamadas Originadas", c.getAttributes().getAttributeValue3());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Free local call without promotion (AC)
	 * 51110000^08/01/07^22:10:00
	 */
	public void testAccount4() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r01_11/ACCOUNT4.BGH");
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
			assertEquals(1, this.results.size());
			Consequence c = this.results.iterator().next();
			assertEquals("Chamadas Locais para Celulares TIM", c.getAttributes().getAttributeValue3());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Free call in Roaming without promotion (!AC). The country destination has NOT the roaming agree with TIM.
	 * 51110000^10/01/07^22:26:32
	 */
	public void testAccount5() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r01_11/ACCOUNT5.BGH");
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
			assertEquals(1, this.results.size());
			Consequence c = this.results.iterator().next();
			assertEquals("Roaming Internacional - Chamadas Originadas", c.getAttributes().getAttributeValue3());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Free local call with promotion (!AC)
	 */
	public void testAccount6() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r01_11/ACCOUNT6.BGH");
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
	 * Free event usage without promotion
	 */
	public void testAccount7() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r01_11/ACCOUNT7.BGH");
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
			assertEquals(1, this.results.size());
			Consequence c = this.results.iterator().next();
			assertEquals("TIM Torpedo", c.getAttributes().getAttributeValue3());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Free data usage without promotion
	 */
	public void testAccount8() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r01_11/ACCOUNT8.BGH");
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
			assertEquals(1, this.results.size());
			Consequence c = this.results.iterator().next();
			assertEquals("TIM Wap Fast", c.getAttributes().getAttributeValue3());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * From ticket #175.
	 * No errors, since none of them are zeroed
	 */
	public void testAccount9() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r01_11/ACCOUNT9.BGH");
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
	 * Test case regarding ticket #175.
	 */
	public void testAccount10() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r01_11/ACCOUNT10.BGH");
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
	 * Test case regarding ticket #229.
	 */
	public void testAccount11() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r01_11/ACCOUNT11.BGH");
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
			assertEquals(1, this.results.size());
			Consequence c = this.results.iterator().next();
			assertEquals("Cham. Originada no Exterior - Ásia", c.getAttributes().getAttributeValue3());
			assertEquals("3.00", c.getAttributes().getAttributeValue4().replaceAll(",", "."));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

}
