package br.com.auster.tim.billcheckout.rules;

import java.util.HashMap;

import org.w3c.dom.Element;

import br.com.auster.billcheckout.consequence.Consequence;
import br.com.auster.billcheckout.consequence.telco.TelcoConsequence;
import br.com.auster.common.sql.SQLConnectionManager;
import br.com.auster.common.xml.DOMUtils;
import br.com.auster.om.invoice.Account;
import br.com.auster.tim.billcheckout.tariff.ServiceRatesCache;

/**
 * TODO What this class is responsible for
 *
 * @author William Soares
 * @version $Id$
 * @since JDK1.4
 */
public class Rule01_22_Test extends BaseRuleTest {

	private String[] RULES = { "src/main/conf/rules/R01.22-plans-services-packages-tariff.drl" };

	protected void createGlobals() {
		super.createGlobals();
		try {

			Class.forName("org.apache.commons.dbcp.PoolingDriver");
			Class.forName("oracle.jdbc.driver.OracleDriver");
			SQLConnectionManager.init(DOMUtils.openDocument(
					"bgh/guiding/pool.xml", false));

			Element conf = DOMUtils.openDocument("bgh/guiding/caches.xml",false);

			ServiceRatesCache alternateServiceRatesCache = new ServiceRatesCache();
			alternateServiceRatesCache.configure(conf);
			this.workingMemory.setGlobal("alternateServiceRatesCache", alternateServiceRatesCache);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * There is no configuration for the rateplan.
	 * There are no services in this invoice.
	 */
	public void testAccount1() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r01_22/ACCOUNT1.BGH");
			// firing rules
			this.startupRuleEngine(RULES, true);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = { "Regra 1.22 - Validating plan fees"};
			int[] AGENDA_COUNT = { 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			// check final consequence count
			assertEquals(1, this.results.size());
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
			assertEquals(1, counters.size());
			assertTrue(counters.containsKey("Tarifa para este plano não está configurada na base."));
			assertEquals(1, counters.get("Tarifa para este plano não está configurada na base.").intValue());
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * There are services and rateplans in this invoice. Both do not have reference data.
	 */
	public void testAccount2() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r01_22/ACCOUNT2.BGH");
			// firing rules
			this.startupRuleEngine(RULES, true);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = { "Regra 1.22 - Validating plan fees",
					               "Regra 1.22 - Validating service fees"};
			int[] AGENDA_COUNT = { 1, 2 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			// check final consequence count
			assertEquals(3, this.results.size());
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
			assertEquals(2, counters.size());
			assertTrue(counters.containsKey("Tarifa para este plano não está configurada na base."));
			assertEquals(1, counters.get("Tarifa para este plano não está configurada na base.").intValue());
			assertTrue(counters.containsKey("Tarifa para este serviço não está configurada na base."));
			assertEquals(2, counters.get("Tarifa para este serviço não está configurada na base.").intValue());
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Rateplan is configured and with the correct value.
	 * There are no services in this invoice.
	 */
	public void testAccount3() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r01_22/ACCOUNT3.BGH");
			// firing rules
			this.startupRuleEngine(RULES, true);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = { "Regra 1.22 - Validating plan fees"};
			int[] AGENDA_COUNT = { 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			// check final consequence count
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}	

	/**
	 * There are services and rateplans in this invoice. Both are configured and with the correct values.
	 */
	public void testAccount4() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r01_22/ACCOUNT4.BGH");
			// firing rules
			this.startupRuleEngine(RULES, true);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = { "Regra 1.22 - Validating plan fees",
            					   "Regra 1.22 - Validating service fees"};
			int[] AGENDA_COUNT = { 1, 2 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			// check final consequence count
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/**
	 * There are services and rateplans in this invoice. Both are configured and with the correct values.
	 */
	public void testAccount5() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r01_22/ACCOUNT5.BGH");
			// firing rules
			this.startupRuleEngine(RULES, true);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = { "Regra 1.22 - Validating service fees"};
			int[] AGENDA_COUNT = { 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			// check final consequence count
			assertEquals(1, this.results.size());
			TelcoConsequence c = (TelcoConsequence)this.results.get(0);
			assertEquals("Valor do serviço cobrado diverge do configurado na base.", c.getDescription());
			assertEquals("Novo Pacote Minutos Nacional", c.getAttributes().getAttributeValue5());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}	
	
	/**
	 * There are services and rateplans in this invoice. All are configured and with the correct values,
	 * 	except Tarifa Zero 41 service.
	 */
	public void testAccount6() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r01_22/ACCOUNT6.BGH");
			// firing rules
			this.startupRuleEngine(RULES, true);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = { "Regra 1.22 - Validating plan fees",
            					   "Regra 1.22 - Validating service fees"};
			int[] AGENDA_COUNT = { 1, 2 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			// check final consequence count
			assertEquals(1, this.results.size());
			TelcoConsequence c = (TelcoConsequence)this.results.get(0);
			assertEquals("Valor do serviço cobrado diverge do configurado na base.", c.getDescription());
			assertEquals("Tarifa Zero 41", c.getAttributes().getAttributeValue5());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}		

	/**
	 * There are services and rateplans in this invoice. All services are configured and with the correct
	 * 	values, but not the rateplan.
	 */
	public void testAccount7() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r01_22/ACCOUNT7.BGH");
			// firing rules
			this.startupRuleEngine(RULES, true);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = { "Regra 1.22 - Validating plan fees",
            					   "Regra 1.22 - Validating service fees"};
			int[] AGENDA_COUNT = { 1, 2 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			// check final consequence count
			assertEquals(1, this.results.size());
			TelcoConsequence c = (TelcoConsequence)this.results.get(0);
			assertEquals("Valor da mensalidade cobrada diverge do configurado na base.", c.getDescription());
			assertEquals("TIM Empresa Nacional", c.getAttributes().getAttributeValue3());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}		

	
	/**
	 * There are services and rateplans in this invoice. Rateplan an service 'Tarifa Zero 41' are with
	 *   incorrect charge values.
	 */
	public void testAccount8() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r01_22/ACCOUNT8.BGH");
			// firing rules
			this.startupRuleEngine(RULES, true);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = { "Regra 1.22 - Validating plan fees",
            					   "Regra 1.22 - Validating service fees"};
			int[] AGENDA_COUNT = { 1, 2 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			// check final consequence count
			assertEquals(2, this.results.size());
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
			assertEquals(2, counters.size());
			assertTrue(counters.containsKey("Valor da mensalidade cobrada diverge do configurado na base."));
			assertEquals(1, counters.get("Valor da mensalidade cobrada diverge do configurado na base.").intValue());
			assertTrue(counters.containsKey("Valor do serviço cobrado diverge do configurado na base."));
			assertEquals(1, counters.get("Valor do serviço cobrado diverge do configurado na base.").intValue());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}		
	
}