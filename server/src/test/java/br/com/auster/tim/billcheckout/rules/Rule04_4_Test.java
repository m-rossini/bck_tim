package br.com.auster.tim.billcheckout.rules;

import java.util.HashMap;

import org.w3c.dom.Element;

import br.com.auster.billcheckout.consequence.Consequence;
import br.com.auster.billcheckout.consequence.telco.TelcoConsequence;
import br.com.auster.common.sql.SQLConnectionManager;
import br.com.auster.common.xml.DOMUtils;
import br.com.auster.om.invoice.Account;
import br.com.auster.tim.billcheckout.param.ContractServicesCache;
import br.com.auster.tim.billcheckout.param.DwPromoMcCache;
import br.com.auster.tim.billcheckout.param.MpufftabCache;
import br.com.auster.tim.billcheckout.param.MpulkfxoCache;
import br.com.auster.tim.billcheckout.param.PlansCache;
import br.com.auster.tim.billcheckout.param.RateTimeZoneCache;
import br.com.auster.tim.billcheckout.param.ServicePlanCache;
import br.com.auster.tim.billcheckout.param.ServicesCache;
import br.com.auster.tim.billcheckout.param.TariffZoneCache;
import br.com.auster.tim.billcheckout.param.TariffZoneUsageGroupCache;
import br.com.auster.tim.billcheckout.param.UsageGroupCache;
import br.com.auster.tim.billcheckout.param.UsageGroupLDCache;
import br.com.auster.tim.billcheckout.tariff.MicrocellMappingCache;
import br.com.auster.tim.billcheckout.tariff.MicrocellRatesCache;
import br.com.auster.tim.billcheckout.tariff.MicrocellSGTRatesCache;

/**
 * TODO What this class is responsible for
 *
 * @author William Soares
 * @version $Id$
 * @since JDK1.4
 */
public class Rule04_4_Test extends BaseRuleTest {

	private String[] RULES = { "src/main/conf/rules/R04.4-validate-calls-in-mc.drl" };

	private String[] GUIDING = { "src/test/resources/bgh/guiding/guiding.drl" };

	protected boolean customized = false;

	protected void createGlobals() {
		super.createGlobals();
		try {
			if (customized) {

				Class.forName("org.apache.commons.dbcp.PoolingDriver");
				Class.forName("oracle.jdbc.driver.OracleDriver");
				SQLConnectionManager.init(DOMUtils.openDocument(
						"bgh/guiding/pool.xml", false));

				Element arg0 = DOMUtils.openDocument("bgh/guiding/caches.xml",
						false);

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
				this.workingMemory.setGlobal("tariffZoneUsageGroupCache",
						tzugCache);

		    	UsageGroupLDCache usageGroupLDCache = new UsageGroupLDCache();
		    	usageGroupLDCache.configure(arg0);
		    	this.workingMemory.setGlobal("usageGroupLDCache", usageGroupLDCache);
		    	
		    	Element conf2 = DOMUtils.openDocument("bgh/guiding/caches2.xml",false);
		    	MicrocellMappingCache mcMapping = new MicrocellMappingCache();
		    	mcMapping.configure(conf2);
		    	this.workingMemory.setGlobal("mcMapping", mcMapping);
			} else {

				Class.forName("org.apache.commons.dbcp.PoolingDriver");
				Class.forName("oracle.jdbc.driver.OracleDriver");
				SQLConnectionManager.init(DOMUtils.openDocument(
						"bgh/guiding/pool.xml", false));

				Element conf = DOMUtils.openDocument("bgh/guiding/caches.xml",false);

				MpufftabCache mpufftabCache = new MpufftabCache();
				mpufftabCache.configure(conf);
				this.workingMemory.setGlobal("mpufftabCache", mpufftabCache);

				MpulkfxoCache mpulkfxoCache = new MpulkfxoCache();
				mpulkfxoCache.configure(conf);
				this.workingMemory.setGlobal("mpulkfxoCache", mpulkfxoCache);
				
				ContractServicesCache contractServicesCache = new ContractServicesCache();
				contractServicesCache.configure(conf);
				this.workingMemory.setGlobal("contractServicesCache", contractServicesCache);				

				DwPromoMcCache dwPromoMcCache = new DwPromoMcCache();
				dwPromoMcCache.configure(conf);
				this.workingMemory.setGlobal("dwPromoMcCache", dwPromoMcCache);
				
				PlansCache planCache = new PlansCache();
				planCache.configure(conf);
				this.workingMemory.setGlobal("planCache", planCache);	


		    	Element conf2 = DOMUtils.openDocument("bgh/guiding/caches2.xml",false);
		    	MicrocellRatesCache microcellRatesCache = new MicrocellRatesCache();
		    	microcellRatesCache.configure(conf2);
		    	this.workingMemory.setGlobal("microcellRatesCache", microcellRatesCache);

		    	MicrocellSGTRatesCache microcellSGTRatesCache = new MicrocellSGTRatesCache();
		    	microcellSGTRatesCache.configure(conf2);
		    	this.workingMemory.setGlobal("microcellSGTRatesCache", microcellSGTRatesCache);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 */
	public void testAccount7() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r04_4/ACCOUNT7.BGH");
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
				String l = c.getAttributes().getAttributeValue4();
				if (! counters.containsKey(l + "-" + k)) {
					counters.put(l + "-" + k, new Integer(0));
				}
				Integer d = counters.get(l + "-" + k);
				counters.put(l + "-" + k, new Integer(d.intValue() + 1));
			}

			assertEquals(5, this.results.size());		
			assertEquals(1, counters.get("2007-07-04" + "-" + "Valor encontrado do desconto da micro-célula diverge do calculado.").intValue());
			assertEquals(1, counters.get("2007-07-05" + "-" + "Valor encontrado do desconto da micro-célula diverge do calculado.").intValue());
			assertEquals(1, counters.get("2007-07-06" + "-" + "Valor encontrado do desconto da micro-célula diverge do calculado.").intValue());
			assertEquals(1, counters.get("2007-07-06" + "-" + "Número de destino da chamada não é elegível para esta micro-célula.").intValue());
			assertEquals(1, counters.get("2006-05-26" + "-" + "Chamada realizada fora do período de vigência da micro-célula.").intValue());
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 *
	 */
	public void testAccount8() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r04_4/ACCOUNT8.BGH");
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

			assertEquals(1, this.results.size());
			assertEquals("Valor encontrado do desconto da micro-célula diverge do calculado.", this.results.get(0).getDescription());
			assertEquals("PALD", this.results.get(0).getAttributes().getAttributeValue7());

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * BGH from ticket #209 (Situação 02)
	 */
	public void testAccount9() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r04_4/ACCOUNT9.BGH");
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

			assertEquals(11, this.results.size());
			for(int i=0; i<this.results.size(); i++) {
				Consequence c = (TelcoConsequence)this.results.get(i);
				assertEquals("Número de destino da chamada não é elegível para esta micro-célula.",c.getDescription());
			}

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * The same BGH that ACCOUNT8 but the MC was changed to 'LD41F',
	 * and now this rule is not considering 'LD41F' microcell, regarding ticket #209 (Situação 01)
	 */
	public void testAccount10() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r04_4/ACCOUNT10.BGH");
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

			assertEquals(0, this.results.size());

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * This BGH contains two MC in mpufftab: 'DESC1' and 'DESC2'
	 * 		- 'DESC1' has 0,2 in 'scalefactor' and it was calculated the discount using
	 *		  this percent (20%).
	 * 		- 'DESC2' has 20 in 'scalefactor' and it was calculated the discount using this value
	 * 		  as R$0.20*umcode, in this case umcode is second.
	 * The two calls using these MC was calculated correctly.
	 *
	 * There are no errors regarding discounts calculations.
	 */
	public void testAccount11() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r04_4/ACCOUNT11.BGH");
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

			assertEquals(2, this.results.size());
			assertEquals("Número de destino da chamada não é elegível para esta micro-célula.", this.results.get(0).getDescription());
			assertEquals("DESC1", this.results.get(0).getAttributes().getAttributeValue7());
			assertEquals("Número de destino da chamada não é elegível para esta micro-célula.", this.results.get(1).getDescription());
			assertEquals("DESC2", this.results.get(1).getAttributes().getAttributeValue7());

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}


	/**
	 * All seems ok.
	 *
	 * There are many MCed calls, but only three (from 18/06/2007) have discount>0 and beforePromotion>0. And the all look ok.
	 */
	public void testAccount12() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r04_4/ACCOUNT12.BGH");
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

			assertEquals(0, this.results.size());

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}


	/**
	 *
	 * There are many MCed calls, but only three (from 05/07/2007) have discount>0 and beforePromotion>0. And these 3
	 * calls are after the expiration date configured on CONTR_SERVICES: 04/07/2007
	 * 
	 * 
	 */
	public void testAccount13() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r04_4/ACCOUNT13.BGH");
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
				String l = c.getAttributes().getAttributeValue4();
				if (! counters.containsKey(l + "-" + k)) {
					counters.put(l + "-" + k, new Integer(0));
				}
				Integer d = counters.get(l + "-" + k);
				counters.put(l + "-" + k, new Integer(d.intValue() + 1));
			}	
			assertEquals(3, this.results.size());
			assertEquals(3, counters.get("2007-07-05" + "-" + "Chamada realizada fora do período de vigência da micro-célula.").intValue());

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/**
	 * The same file that is used on testCase07, but the call that its callDate was before the activation
	 * date configured on CONTR_SERVICES is correct now.  
	 */
	public void testAccount14() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r04_4/ACCOUNT14.BGH");
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
				String l = c.getAttributes().getAttributeValue4();
				if (! counters.containsKey(l + "-" + k)) {
					counters.put(l + "-" + k, new Integer(0));
				}
				Integer d = counters.get(l + "-" + k);
				counters.put(l + "-" + k, new Integer(d.intValue() + 1));
			}

			assertEquals(4, this.results.size());		
			assertEquals(1, counters.get("2007-07-04" + "-" + "Valor encontrado do desconto da micro-célula diverge do calculado.").intValue());
			assertEquals(1, counters.get("2007-07-05" + "-" + "Valor encontrado do desconto da micro-célula diverge do calculado.").intValue());
			assertEquals(1, counters.get("2007-07-06" + "-" + "Valor encontrado do desconto da micro-célula diverge do calculado.").intValue());
			assertEquals(1, counters.get("2007-07-06" + "-" + "Número de destino da chamada não é elegível para esta micro-célula.").intValue());

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	public void testAccount15() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r04_4/ACCOUNT15.BGH");
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

			assertEquals(2, this.results.size());		

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/**
	 * Ticket #209
	 * 
	 */ 	  
	public void testAccount16() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r04_4/ACCOUNT16.BGH");
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

			//expecting only one type of error (after the comparison change to "result > 0.010001",
			//this account does not have calculation errors anymore)
			for (Consequence c : this.results) {
				assertEquals("Número de destino da chamada não é elegível para esta micro-célula.", c.getDescription());
			}
			assertEquals(4, this.results.size());		

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
}