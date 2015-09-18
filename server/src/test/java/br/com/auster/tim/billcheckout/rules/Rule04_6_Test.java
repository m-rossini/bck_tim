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



/**
 * @author framos
 * @version $Id$
 *
 */
public class Rule04_6_Test extends BaseRuleTest {


	private String[] RULES_1 = { "src/test/resources/bgh/guiding/guiding.drl" };
	private String[] RULES_2 = { "src/main/conf/rules/R04.6-plan-doesnot-contain-service.drl" };

	protected boolean customized = false;

    protected void createGlobals() {
    	super.createGlobals();
    	if (!customized) { return; }
    	try {
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
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }


	/**
	 * This is the correct file
	 */
	public void testAccount1() {
		try {
			// firing rules
			this.customized = true;
			this.startupRuleEngine(RULES_1);
			Account acc = this.loadBGHTestFile("bgh/r04_6/ACCOUNT1.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			this.customized = false;
			this.startupRuleEngine(RULES_2);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(18, this.results.size());
			for (Consequence c : this.results) {
				TelcoConsequence tc = (TelcoConsequence)c;
				assertEquals("Plano", tc.getAttributes().getAttributeName1());
				assertEquals("Plano Nosso Modo", tc.getAttributes().getAttributeValue1());
				assertEquals("Serviço", tc.getAttributes().getAttributeName2());
				assertEquals("", tc.getAttributes().getAttributeValue2());
				assertEquals("ID de Serviço", tc.getAttributes().getAttributeName3());
				assertEquals("BSG2", tc.getAttributes().getAttributeValue3());
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * This file was added due to ticket #97
	 */
	public void testAccount2() {
		try {
			// firing rules
			this.customized = true;
			this.startupRuleEngine(RULES_1);
			Account acc = this.loadBGHTestFile("bgh/r04_6/ACCOUNT2.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			this.customized = false;
			this.startupRuleEngine(RULES_2);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashMap<String, Integer> counters  = new HashMap<String, Integer>();
			for (Consequence c : this.results) {
				String key = c.getAttributes().getAttributeValue2();
				if (!counters.containsKey(key)) {
					counters.put(key, new Integer(1));
					continue;
				}
				Integer i = counters.get(key);
				counters.put(key, new Integer(i.intValue() + 1));
			}
			assertEquals(0, this.results.size());

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * This file was added due to ticket #97
	 */
	public void testAccount3() {
		try {
			// firing rules
			this.customized = true;
			this.startupRuleEngine(RULES_1);
			Account acc = this.loadBGHTestFile("bgh/r04_6/ACCOUNT3.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			this.customized = false;
			this.startupRuleEngine(RULES_2);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashMap<String, Integer> counters  = new HashMap<String, Integer>();
			for (Consequence c : this.results) {
				String key = c.getAttributes().getAttributeValue2();
				if (!counters.containsKey(key)) {
					counters.put(key, new Integer(1));
					continue;
				}
				Integer i = counters.get(key);
				counters.put(key, new Integer(i.intValue() + 1));
			}
			assertEquals(1, this.results.size());
			assertEquals("Transf.quando Desligado", this.results.iterator().next().getAttributes().getAttributeValue2());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * This file was added due to ticket #245
	 */
	public void testAccount4() {
		try {
			// firing rules
			this.customized = true;
			this.startupRuleEngine(RULES_1);
			Account acc = this.loadBGHTestFile("bgh/r04_6/ACCOUNT4.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			this.customized = false;
			this.startupRuleEngine(RULES_2);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			assertEquals(5, this.results.size());
			// running over results list to check if consequences where ok
			HashMap<String, Integer> counters  = new HashMap<String, Integer>();
			for (Consequence c : this.results) {
				String key = c.getAttributes().getAttributeValue3();
				if (!counters.containsKey(key)) {
					counters.put(key, new Integer(1));
					continue;
				}
				Integer i = counters.get(key);
				counters.put(key, new Integer(i.intValue() + 1));
			}
			assertTrue(counters.containsKey("TS12"));
			assertEquals(2, counters.get("TS12").intValue());
			assertTrue(counters.containsKey("TS23"));
			assertEquals(2, counters.get("TS23").intValue());
			assertTrue(counters.containsKey("TS24"));
			assertEquals(1, counters.get("TS24").intValue());

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

}
