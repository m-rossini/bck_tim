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
public class Rule01_8_Test extends BaseRuleTest {



	private String[] RULES_1 = { "src/test/resources/bgh/guiding/guiding.drl" };
	private String[] RULES_2 = { "src/main/conf/rules/R01.8-expired-usage-validation.drl",
			                     "src/test/resources/bgh/r01_8/usage-types.drl" };

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
			Account acc = this.loadBGHTestFile("bgh/r01_8/ACCOUNT1.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			this.customized = false;
			this.startupRuleEngine(RULES_2);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashMap<String, Integer> counters = new HashMap<String, Integer>();
			for (Consequence c : this.results) {
				String dt = c.getAttributes().getAttributeValue3();
				if (!counters.containsKey(dt)) {
					counters.put(dt, new Integer(1));
				} else {
					Integer curr = counters.get(dt);
					counters.put(dt, new Integer(curr.intValue() + 1));
				}
			}
			assertEquals(4, this.results.size());
			assertNotNull( counters.get("2006-05-01"));
			assertEquals(1, counters.get("2006-05-01").intValue());
			assertNotNull( counters.get("2006-03-02"));
			assertEquals(1, counters.get("2006-03-02").intValue());
			assertNotNull( counters.get("2006-03-03"));
			assertEquals(1, counters.get("2006-03-03").intValue());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}


	public void testAccount2() {
		try {
			// firing rules
			this.customized = true;
			this.startupRuleEngine(RULES_1);
			Account acc = this.loadBGHTestFile("bgh/r01_8/ACCOUNT2.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			this.customized = false;
			this.startupRuleEngine(RULES_2);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashMap<String, Integer> counters = new HashMap<String, Integer>();
			for (Consequence c : this.results) {
				String dt = c.getAttributes().getAttributeValue3();
				if (!counters.containsKey(dt)) {
					counters.put(dt, new Integer(1));
				} else {
					Integer curr = counters.get(dt);
					counters.put(dt, new Integer(curr.intValue() + 1));
				}
			}
			assertEquals(5, this.results.size());
			assertNotNull( counters.get("2007-01-08"));
			assertEquals(2, counters.get("2007-01-08").intValue());
			assertNotNull( counters.get("2006-11-10"));
			assertEquals(1, counters.get("2006-11-10").intValue());
			assertNotNull( counters.get("2006-11-12"));
			assertEquals(1, counters.get("2006-11-12").intValue());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * All usage details are NOT expired! We have changed to cycle start date information
	 * 		to make sure none will be expired.
	 * We are expecting to find only 3 errors, all related to a malformed originCity attribute
	 */
	public void testAccount3() {
		try {
			// firing rules
			this.customized = true;
			this.startupRuleEngine(RULES_1);
			Account acc = this.loadBGHTestFile("bgh/r01_8/ACCOUNT3.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			this.customized = false;
			this.startupRuleEngine(RULES_2);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashMap<String, Integer> counters = new HashMap<String, Integer>();
			for (Consequence c : this.results) {
				String key = c.getDescription();
				if (!counters.containsKey(key)) {
					counters.put(key, new Integer(1));
				} else {
					Integer curr = counters.get(key);
					counters.put(key, new Integer(curr.intValue() + 1));
				}
			}
			assertEquals(5, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * 2 local non-cobilling expired
	 * 3 data with wrong format for origin
	 * 2 ldn cobiiling (23) expired
	 * 1 ldi cobilling (15) expired
	 */
	public void testAccount4() {
		try {
			// firing rules
			this.customized = true;
			this.startupRuleEngine(RULES_1);
			Account acc = this.loadBGHTestFile("bgh/r01_8/ACCOUNT4.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			this.customized = false;
			this.startupRuleEngine(RULES_2);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashMap<String, Integer> counters = new HashMap<String, Integer>();
			for (Consequence c : this.results) {
				String key = c.getDescription();
				if (!counters.containsKey(key)) {
					counters.put(key, new Integer(1));
				} else {
					Integer curr = counters.get(key);
					counters.put(key, new Integer(curr.intValue() + 1));
				}
			}
			assertEquals(5, this.results.size());
			assertNotNull( counters.get("Chamada local em decurso de prazo."));
			assertEquals(2, counters.get("Chamada local em decurso de prazo.").intValue());
			assertNotNull( counters.get("Chamada LDN com co-billing em decurso de prazo."));
			assertEquals(2, counters.get("Chamada LDN com co-billing em decurso de prazo.").intValue());
			assertNotNull( counters.get("Chamada LDI com co-billing em decurso de prazo."));
			assertEquals(1, counters.get("Chamada LDI com co-billing em decurso de prazo.").intValue());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * 2 local non-cobilling expired
	 * 2 data with wrong format for origin
	 */
	public void testAccount5() {
		try {
			// firing rules
			this.customized = true;
			this.startupRuleEngine(RULES_1);
			Account acc = this.loadBGHTestFile("bgh/r01_8/ACCOUNT5.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			this.customized = false;
			this.startupRuleEngine(RULES_2);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashMap<String, Integer> counters = new HashMap<String, Integer>();
			for (Consequence c : this.results) {
				String key = c.getDescription();
				if (!counters.containsKey(key)) {
					counters.put(key, new Integer(1));
				} else {
					Integer curr = counters.get(key);
					counters.put(key, new Integer(curr.intValue() + 1));
				}
			}
			assertEquals(2, this.results.size());
			assertNotNull( counters.get("Chamada local em decurso de prazo."));
			assertEquals(2, counters.get("Chamada local em decurso de prazo.").intValue());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * This file is related to STFC. This is the original file.
	 */
	public void testAccountSTFC1() {
		try {
			// firing rules
			this.customized = true;
			this.startupRuleEngine(RULES_1);
			Account acc = this.loadBGHTestFile("bgh/stfc/ACCOUNT1.BGH");
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
	 * This file is related to STFC. It contains 2 calls that should be reported, IF
	 *   they where mobile calls. Since this rule should ignore non-mobile calls, it
	 *   will not report any error.
	 */
	public void testAccountSTFC1Modified() {
		try {
			// firing rules
			this.customized = true;
			this.startupRuleEngine(RULES_1);
			Account acc = this.loadBGHTestFile("bgh/stfc/ACCOUNT1A.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			this.customized = false;
			this.startupRuleEngine(RULES_2);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(2, this.results.size());
			HashMap<String, Integer> counters = new HashMap<String, Integer>();
			for (Consequence c : this.results) {
				String key = c.getDescription();
				if (!counters.containsKey(key)) {
					counters.put(key, new Integer(1));
				} else {
					Integer curr = counters.get(key);
					counters.put(key, new Integer(curr.intValue() + 1));
				}
			}
			assertNotNull( counters.get("Chamada local em decurso de prazo."));
			assertEquals(1, counters.get("Chamada local em decurso de prazo.").intValue());
			assertNotNull( counters.get("Chamada LDN sem co-billing em decurso de prazo."));
			assertEquals(1, counters.get("Chamada LDN sem co-billing em decurso de prazo.").intValue());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * This file is related to STFC. This is the original file.
	 */
	public void testAccountSTFC2() {
		try {
			// firing rules
			this.customized = true;
			this.startupRuleEngine(RULES_1);
			Account acc = this.loadBGHTestFile("bgh/stfc/ACCOUNT2.BGH");
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
	 * This file is related to STFC. This account contains both mobile and non-mobile access numbers.
	 * Each one of them have two calls that should be presented by this rule, but only the mobile
	 * ones will be.
	 */
	public void testAccountSTFC2Modified() {
		try {
			// firing rules
			this.customized = true;
			this.startupRuleEngine(RULES_1);
			Account acc = this.loadBGHTestFile("bgh/stfc/ACCOUNT2A.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			this.customized = false;
			this.startupRuleEngine(RULES_2);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * This file was sent by Bruno
	 */
	public void testAccountSTFC3() {
		try {
			// firing rules
			this.customized = true;
			this.startupRuleEngine(RULES_1);
			Account acc = this.loadBGHTestFile("bgh/stfc/ACCOUNT3.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			this.customized = false;
			this.startupRuleEngine(RULES_2);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(4, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * This file was sent by Bruno, but we removed the second contract. Now, all
	 *     calls are non-mobile
	 */
	public void testAccountSTFC3Modified() {
		try {
			// firing rules
			this.customized = true;
			this.startupRuleEngine(RULES_1);
			Account acc = this.loadBGHTestFile("bgh/stfc/ACCOUNT3A.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			this.customized = false;
			this.startupRuleEngine(RULES_2);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(7, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Ticket #296
	 */
	public void testAccount6() {
		try {
			// firing rules
			this.customized = true;
			this.startupRuleEngine(RULES_1);
			Account acc = this.loadBGHTestFile("bgh/r01_8/ACCOUNT6.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			this.customized = false;
			this.startupRuleEngine(RULES_2);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashMap<String, Integer> counters = new HashMap<String, Integer>();
			for (Consequence c : this.results) {
				String key = c.getDescription();
				if (!counters.containsKey(key)) {
					counters.put(key, new Integer(1));
				} else {
					Integer curr = counters.get(key);
					counters.put(key, new Integer(curr.intValue() + 1));
				}
			}
			assertEquals(27, this.results.size());
			assertNotNull( counters.get("Chamada local em decurso de prazo."));
			assertEquals(27, counters.get("Chamada local em decurso de prazo.").intValue());
			// making sure they are all GENTF
			counters.clear();
			for (Consequence c : this.results) {
				String key = c.getAttributes().getAttributeValue9();
				if (!counters.containsKey(key)) {
					counters.put(key, new Integer(1));
				} else {
					Integer curr = counters.get(key);
					counters.put(key, new Integer(curr.intValue() + 1));
				}
			}
			assertEquals(1, counters.size());
			assertNotNull( counters.get("GENTF"));
			assertEquals(27, counters.get("GENTF").intValue());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

}
