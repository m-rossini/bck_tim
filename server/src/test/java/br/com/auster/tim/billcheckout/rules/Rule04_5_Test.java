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



/**
 * @author framos
 * @version $Id$
 *
 */
public class Rule04_5_Test extends BaseRuleTest {



	private String[] RULES_1 = { "src/test/resources/bgh/guiding/guiding.drl" };
	private String[] RULES_2 = { "src/main/conf/rules/R04-5-duplicated-usage-latecalls.drl",
			                     "src/test/resources/bgh/r04_5/usage-dumper.drl" };

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
	 * This file was modified to trigger 7 errors
	 */
	public void testAccount1() {
		try {
			// firing rules
			System.getProperties().setProperty("br.com.auster.billcheckout.rules.duplicity.basedir",
					                           "src/test/resources/bgh/r04_5/loaded");
			this.customized = true;
			this.startupRuleEngine(RULES_1);
			Account acc = this.loadBGHTestFile("bgh/r04_5/ACCOUNT1.BGH");
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
	 * This file was modified to trigger 4 errors
	 */
	public void testAccount2() {
		try {
			// firing rules
			System.getProperties().setProperty("br.com.auster.billcheckout.rules.duplicity.basedir",
					                           "src/test/resources/bgh/r04_5/loaded");
			this.customized = true;
			this.startupRuleEngine(RULES_1);
			Account acc = this.loadBGHTestFile("bgh/r04_5/ACCOUNT2.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			this.customized = false;
			this.startupRuleEngine(RULES_2);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashMap<String, Integer> counters = new HashMap<String, Integer>();
			for (Consequence c : this.results) {
				String dt = c.getAttributes().getAttributeValue5();
				if (!counters.containsKey(dt)) {
					counters.put(dt, new Integer(1));
				} else {
					Integer curr = counters.get(dt);
					counters.put(dt, new Integer(curr.intValue() + 1));
				}
			}
			assertEquals(1, this.results.size());
			assertNotNull( counters.get("2007-02-15"));
			assertEquals(1, counters.get("2007-02-15").intValue());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * ACCOUNT1.BGH with 2 modifications.
	 * * 27/04/07^19:10:02 into 27/01/07^19:10:02 - LATE CALL
	 * * 27/04/07^20:45:55 into 27/03/07^20:45:55 - NOT LATE
	 */
	public void testAccount3() {
		try {
			// firing rules
			System.getProperties().setProperty("br.com.auster.billcheckout.rules.duplicity.basedir",
					                           "src/test/resources/bgh/r04_5/loaded");
			this.customized = true;
			this.startupRuleEngine(RULES_1);
			Account acc = this.loadBGHTestFile("bgh/r04_5/ACCOUNT3.BGH");
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

}
