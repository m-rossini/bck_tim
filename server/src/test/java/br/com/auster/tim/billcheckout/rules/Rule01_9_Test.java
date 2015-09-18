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
public class Rule01_9_Test extends BaseRuleTest {


	private String[] RULES_1 = { "src/test/resources/bgh/guiding/guiding.drl" };
	private String[] RULES_2 = { "src/main/conf/rules/R01.9-duplicated-usage-validation.drl" };

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
			Account acc = this.loadBGHTestFile("bgh/r01_9/ACCOUNT1.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			this.customized = false;
			this.startupRuleEngine(RULES_2);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashMap<String, Integer> counters = new HashMap<String, Integer>();
			HashMap<String, Integer> dates = new HashMap<String, Integer>();
			for (Consequence c : this.results) {
				String k = c.getDescription();
				if (! counters.containsKey(k)) {
					counters.put(k, new Integer(0));
				}
				if (k.equals("Detalhes de Uso de Voz Sobrepostos")) {
					String k2 = ((TelcoConsequence)c).getAttributes().getAttributeValue5() + "|" +
					            ((TelcoConsequence)c).getAttributes().getAttributeValue8();
					if (! dates.containsKey(k2)) {
						dates.put(k2, new Integer(0));
					}
					Integer d2 = dates.get(k2);
					dates.put(k2, new Integer(d2.intValue() + 1));
				}
				Integer d = counters.get(k);
				counters.put(k, new Integer(d.intValue() + 1));
			}
			assertEquals(6, this.results.size());
			assertEquals(6, counters.get("Detalhes de Uso de Voz Sobrepostos").intValue());
			assertNotNull(dates.get("2006-11-11|Não"));
			assertEquals(2, dates.get("2006-11-11|Não").intValue());
			assertNotNull(dates.get("2006-11-07|Não"));
			assertEquals(2, dates.get("2006-11-07|Não").intValue());
			assertNotNull(dates.get("2006-11-09|Sim"));
			assertEquals(2, dates.get("2006-11-09|Sim").intValue());

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
			Account acc = this.loadBGHTestFile("bgh/r01_9/ACCOUNT2.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			this.customized = false;
			this.startupRuleEngine(RULES_2);
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
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testAccount3() {
		try {
			// firing rules
			this.customized = true;
			this.startupRuleEngine(RULES_1);
			Account acc = this.loadBGHTestFile("bgh/r01_9/ACCOUNT3.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			this.customized = false;
			this.startupRuleEngine(RULES_2);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashMap<String, Integer> counters = new HashMap<String, Integer>();
			HashMap<String, Integer> dates = new HashMap<String, Integer>();
			for (Consequence c : this.results) {
				String k = c.getDescription();
				if (!counters.containsKey(k)) {
					counters.put(k, new Integer(0));
				}
				if (k.equals("Detalhes de Uso de Voz Sobrepostos")) {
					String k2 = ((TelcoConsequence) c).getAttributes().getAttributeValue5();
					if (!dates.containsKey(k2)) {
						dates.put(k2, new Integer(0));
					}
					Integer d2 = dates.get(k2);
					dates.put(k2, new Integer(d2.intValue() + 1));
				}
				Integer d = counters.get(k);
				counters.put(k, new Integer(d.intValue() + 1));
			}
			assertEquals(5, this.results.size());
			assertEquals(5, counters.get("Detalhes de Uso de Voz Sobrepostos").intValue());
			assertEquals(5, dates.get("2006-11-13").intValue());
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
			Account acc = this.loadBGHTestFile("bgh/r01_9/ACCOUNT4.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			this.customized = false;
			this.startupRuleEngine(RULES_2);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashMap<String, Integer> counters = new HashMap<String, Integer>();
			for (Consequence c : this.results) {
				String k = c.getDescription();
				if (!counters.containsKey(k)) {
					counters.put(k, new Integer(0));
				}
				Integer d = counters.get(k);
				counters.put(k, new Integer(d.intValue() + 1));
			}
			assertEquals(29, this.results.size());
			// 5 are overlaping events
			assertNotNull(counters.get("Detalhes de Uso de Voz Duplicados"));
			assertEquals(14, counters.get("Detalhes de Uso de Voz Duplicados").intValue());
			assertNotNull(counters.get("Detalhes de Uso de Dados Duplicados"));
			assertEquals(4, counters.get("Detalhes de Uso de Dados Duplicados").intValue());
			assertNotNull(counters.get("Detalhes de Uso de Eventos Duplicados"));
			assertEquals(6, counters.get("Detalhes de Uso de Eventos Duplicados").intValue());

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testAccount5() {
		try {
			// firing rules
			this.customized = true;
			this.startupRuleEngine(RULES_1);
			Account acc = this.loadBGHTestFile("bgh/r01_9/ACCOUNT5.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			this.customized = false;
			this.startupRuleEngine(RULES_2);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashMap<String, Integer> counters = new HashMap<String, Integer>();
			for (Consequence c : this.results) {
				String k = c.getDescription();
				if (!counters.containsKey(k)) {
					counters.put(k, new Integer(0));
				}
				Integer d = counters.get(k);
				counters.put(k, new Integer(d.intValue() + 1));
			}
			assertEquals(52, this.results.size());
			assertNotNull(counters.get("Detalhes de Uso de Voz Sobrepostos"));
			assertEquals(35, counters.get("Detalhes de Uso de Voz Sobrepostos").intValue());
			assertNotNull(counters.get("Detalhes de Uso de Voz Duplicados"));
			assertEquals(17, counters.get("Detalhes de Uso de Voz Duplicados").intValue());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testAccount6() {
		try {
			// firing rules
			this.customized = true;
			this.startupRuleEngine(RULES_1);
			Account acc = this.loadBGHTestFile("bgh/r01_9/ACCOUNT6.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			this.customized = false;
			this.startupRuleEngine(RULES_2);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashMap<String, Integer> counters = new HashMap<String, Integer>();
			HashMap<String, Integer> dates = new HashMap<String, Integer>();
			for (Consequence c : this.results) {
				String k = c.getDescription();
				if (!counters.containsKey(k)) {
					counters.put(k, new Integer(0));
				}
				Integer d = counters.get(k);
				counters.put(k, new Integer(d.intValue() + 1));
				// dates counter
				if (k.equals("Detalhes de Uso de Voz Sobrepostos")) {
					String k2 = ((TelcoConsequence) c).getAttributes().getAttributeValue5();
					if (!dates.containsKey(k2)) {
						dates.put(k2, new Integer(0));
					}
					Integer d2 = dates.get(k2);
					dates.put(k2, new Integer(d2.intValue() + 1));
				}
			}
			assertEquals(14, this.results.size());
			// CALLS bellow are no longer overlaped
			//  DATE     TIME        CALLED           DUR.      SVC      TZ  $$     END TIME
			//27/4/2007	12:50:40	045-9967-4224	00:02:00	TS11	V2T	0.00	12:52:40
			//27/4/2007	12:52:40	045-9967-4224	00:02:00	TS11	V2T	0.00	12:54:40
			//27/4/2007	12:54:40	045-9967-4224	00:00:37	TS11	V2T	0.00	12:55:17
			assertNull(counters.get("Detalhes de Uso de Voz Duplicados"));
			assertTrue(counters.containsKey("Detalhes de Uso de Voz Sobrepostos"));
			assertEquals(14, counters.get("Detalhes de Uso de Voz Sobrepostos").intValue());
			// dates only
			assertTrue(dates.containsKey("2007-05-07"));
			assertEquals(2, dates.get("2007-05-07").intValue());
			assertTrue(dates.containsKey("2007-05-02"));
			assertEquals(2, dates.get("2007-05-02").intValue());
			// THESE ARE THE REMOVED CONSEQUENCES .. SEE ABOVE
			//assertTrue(dates.containsKey("2007-04-27"));
			//assertEquals(3, dates.get("2007-04-27").intValue());
			assertTrue(dates.containsKey("2007-04-26"));
			assertEquals(2, dates.get("2007-04-26").intValue());
			assertTrue(dates.containsKey("2007-04-24"));
			assertEquals(2, dates.get("2007-04-24").intValue());
			assertTrue(dates.containsKey("2007-04-18"));
			assertEquals(4, dates.get("2007-04-18").intValue());
			assertTrue(dates.containsKey("2007-04-16"));
			assertEquals(2, dates.get("2007-04-16").intValue());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testAccount7() {
		try {
			// firing rules
			this.customized = true;
			this.startupRuleEngine(RULES_1);
			Account acc = this.loadBGHTestFile("bgh/r01_9/ACCOUNT7.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			this.customized = false;
			this.startupRuleEngine(RULES_2);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testAccount8() {
		try {
			// firing rules
			this.customized = true;
			this.startupRuleEngine(RULES_1);
			Account acc = this.loadBGHTestFile("bgh/r01_9/ACCOUNT8.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			this.customized = false;
			this.startupRuleEngine(RULES_2);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashMap<String, Integer> counters = new HashMap<String, Integer>();
			for (Consequence c : this.results) {
				String k = c.getDescription();
				if (!counters.containsKey(k)) {
					counters.put(k, new Integer(0));
				}
				Integer d = counters.get(k);
				counters.put(k, new Integer(d.intValue() + 1));
			}
			// Should not find any duplicated voice usage
			assertNull(counters.get("Detalhes de Uso de Voz Duplicados"));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}
