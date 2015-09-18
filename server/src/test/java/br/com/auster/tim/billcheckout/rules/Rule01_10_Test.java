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
import br.com.auster.tim.billcheckout.util.UsageGroupingCache;



/**
 * @author framos
 * @version $Id$
 *
 */
public class Rule01_10_Test extends BaseRuleTest {


	private String[] RULES_1 = { "src/test/resources/bgh/guiding/guiding.drl" };
	private String[] RULES_2 = { "src/main/conf/rules/R01.10-incorrect-usagegroup-for-call.drl" };

	protected boolean customized = false;

    protected void createGlobals() {
    	super.createGlobals();
    	try {
    		Class.forName("org.apache.commons.dbcp.PoolingDriver");
    		Class.forName("oracle.jdbc.driver.OracleDriver");
    		SQLConnectionManager.init(DOMUtils.openDocument("bgh/guiding/pool.xml", false));

	    	Element arg0 = DOMUtils.openDocument("bgh/guiding/caches.xml", false);
        	if (!customized) {
		    	UsageGroupingCache usCache = new UsageGroupingCache();
		    	usCache.configure(arg0);
		    	this.workingMemory.setGlobal("bghUsageGroupingCache", usCache);

        	} else {

		    	ServicesCache svcCache = new ServicesCache();
		    	svcCache.configure(arg0);
		    	this.workingMemory.setGlobal("serviceCache", svcCache);

		    	PlansCache planCache = new PlansCache();
		    	planCache.configure(arg0);
		    	this.workingMemory.setGlobal("planCache", planCache);

		    	TariffZoneCache tZoneCache = new TariffZoneCache();
		    	tZoneCache.configure(arg0);
		    	this.workingMemory.setGlobal("tariffZoneCache", tZoneCache);

		    	UsageGroupLDCache usageGroupLDCache =  new UsageGroupLDCache();
		    	usageGroupLDCache.configure(arg0);
	    		this.workingMemory.setGlobal( "usageGroupLDCache", usageGroupLDCache );

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
        	}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

	/**
	 * This is the original file.
	 */
	public void testAccount1() {
		try {
			// firing rules
			this.customized = true;
			this.startupRuleEngine(RULES_1);
			Account acc = this.loadBGHTestFile("bgh/r01_10/ACCOUNT1.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			this.customized = false;
			this.startupRuleEngine(RULES_2, true);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashMap<String, Integer> counters = new HashMap<String, Integer>();
			HashMap<String, Integer> counters2 = new HashMap<String, Integer>();
			for (Consequence c : this.results) {
				String k = c.getAttributes().getAttributeValue1() + "/" + c.getAttributes().getAttributeValue2();
				String k2 = c.getDescription();
				if (! counters.containsKey(k)) {
					counters.put(k, new Integer(0));
				}
				Integer d = counters.get(k);
				counters.put(k, new Integer(d.intValue() + 1));
				if (! counters2.containsKey(k2)) {
					counters2.put(k2, new Integer(0));
				}
				d = counters2.get(k2);
				counters2.put(k2, new Integer(d.intValue() + 1));
			}
			assertEquals(5, this.results.size());
			assertNotNull(counters.get("CRIOU/Roaming Internacional - Chamadas Originadas"));
			assertEquals(3, counters.get("CRIOU/Roaming Internacional - Chamadas Originadas").intValue());
			assertNotNull(counters.get("AD SP/Chamadas Locais para Outros Celulares"));
			assertEquals(2, counters.get("AD SP/Chamadas Locais para Outros Celulares").intValue());
			assertNotNull(counters2.get("A chamada de longa distância foi demonstrada na seção incorreta da fatura."));
			assertEquals(5, counters2.get("A chamada de longa distância foi demonstrada na seção incorreta da fatura.").intValue());
			// now, checking assert
			String[] RULE_LIST = { "R01-10 - Usage not allowed in UsageGroup" };
			int[] AGENDA_COUNT = { 5 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}


	/**
	 * Testcase sent by TIM.
	 */
	public void testAccount2() {
		try {
			// firing rules
			this.customized = true;
			this.startupRuleEngine(RULES_1);
			Account acc = this.loadBGHTestFile("bgh/r01_10/ACCOUNT2.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			this.customized = false;
			this.startupRuleEngine(RULES_2, true);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashMap<String, Integer> counters = new HashMap<String, Integer>();
			for (Consequence c : this.results) {
				String k = c.getAttributes().getAttributeValue1() + "/" + c.getAttributes().getAttributeValue2();
				if (! counters.containsKey(k)) {
					counters.put(k, new Integer(0));
				}
				Integer d = counters.get(k);
				counters.put(k, new Integer(d.intValue() + 1));
			}
			assertEquals(8, this.results.size());
			assertNotNull(counters.get("VCMC/Chamadas Locais para Celulares TIM"));
			assertEquals(1, counters.get("VCMC/Chamadas Locais para Celulares TIM").intValue());
			assertNotNull(counters.get("ITC02/Chamadas Locais para Celulares TIM"));
			assertEquals(1, counters.get("ITC02/Chamadas Locais para Celulares TIM").intValue());
			assertNotNull(counters.get("1ITM/Chamadas Longa Distância: TIM LD 41"));
			assertEquals(3, counters.get("1ITM/Chamadas Longa Distância: TIM LD 41").intValue());
			assertNotNull(counters.get("2TM/Chamadas Longa Distância: TIM LD 41"));
			assertEquals(2, counters.get("2TM/Chamadas Longa Distância: TIM LD 41").intValue());
			assertNotNull(counters.get("2NM/Chamadas Longa Distância: TIM LD 41"));
			assertEquals(1, counters.get("2NM/Chamadas Longa Distância: TIM LD 41").intValue());
			// now, checking assert
			String[] RULE_LIST = { "R01-10 - Usage not allowed in UsageGroup" };
			int[] AGENDA_COUNT = { 8 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Changed CSP_ID from two calls in LD 41 section
	 */
 	public void testAccount3() {
		try {
			// firing rules
			this.customized = true;
			this.startupRuleEngine(RULES_1);
			Account acc = this.loadBGHTestFile("bgh/r01_10/ACCOUNT3.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			this.customized = false;
			this.startupRuleEngine(RULES_2);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashMap<String, Integer> counters = new HashMap<String, Integer>();
			for (Consequence c : this.results) {
				String k2 = c.getDescription();
				if (! counters.containsKey(k2)) {
					counters.put(k2, new Integer(0));
				}
				Integer d = counters.get(k2);
				counters.put(k2, new Integer(d.intValue() + 1));
			}
			assertEquals(5, this.results.size());
			assertNotNull(counters.get("A chamada de longa distância foi demonstrada na seção incorreta da fatura."));
			assertEquals(5, counters.get("A chamada de longa distância foi demonstrada na seção incorreta da fatura.").intValue());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

 		public void testAccount4() {
		try {
			// firing rules
			this.customized = true;
			this.startupRuleEngine(RULES_1);
			Account acc = this.loadBGHTestFile("bgh/r01_10/ACCOUNT4.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			this.customized = false;
			this.startupRuleEngine(RULES_2);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashMap<String, Integer> counters = new HashMap<String, Integer>();
			for (Consequence c : this.results) {
				String k2 = c.getDescription();
				if (! counters.containsKey(k2)) {
					counters.put(k2, new Integer(0));
				}
				Integer d = counters.get(k2);
				counters.put(k2, new Integer(d.intValue() + 1));
			}
			assertEquals(4, this.results.size());
			assertNotNull(counters.get("A chamada de longa distância foi demonstrada na seção incorreta da fatura."));
			assertEquals(4, counters.get("A chamada de longa distância foi demonstrada na seção incorreta da fatura.").intValue());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

    /**
     * This file is OK, but there is a missing tariff-zone for LD call at TELEMAR section.
     */
	public void testAccount5() {
		try {
			// firing rules
			this.customized = true;
			this.startupRuleEngine(RULES_1);
			Account acc = this.loadBGHTestFile("bgh/r01_10/ACCOUNT5.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			this.customized = false;
			this.startupRuleEngine(RULES_2);
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
     * Ticket #174
     */
	public void testAccount6() {
		try {
			// firing rules
			this.customized = true;
			this.startupRuleEngine(RULES_1);
			Account acc = this.loadBGHTestFile("bgh/r01_10/ACCOUNT6.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			this.customized = false;
			this.startupRuleEngine(RULES_2);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashMap<String, Integer> counters = new HashMap<String, Integer>();
			for (Consequence c : this.results) {
				String k2 = c.getAttributes().getAttributeValue2();
				if (! counters.containsKey(k2)) {
					counters.put(k2, new Integer(0));
				}
				Integer d = counters.get(k2);
				counters.put(k2, new Integer(d.intValue() + 1));
			}
			// running over results list to check if consequences where ok
			assertEquals(4, this.results.size());
			assertNotNull(counters.get("Chamadas Longa Distância: TIM LD 41"));
			assertEquals(4, counters.get("Chamadas Longa Distância: TIM LD 41").intValue());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}
