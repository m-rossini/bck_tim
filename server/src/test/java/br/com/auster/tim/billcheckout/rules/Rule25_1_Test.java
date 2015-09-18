package br.com.auster.tim.billcheckout.rules;

import org.w3c.dom.Element;

import br.com.auster.billcheckout.consequence.Consequence;
import br.com.auster.common.sql.SQLConnectionManager;
import br.com.auster.common.xml.DOMUtils;
import br.com.auster.om.invoice.Account;
import br.com.auster.tim.billcheckout.param.ContractServicesDAO;
import br.com.auster.tim.billcheckout.param.MpulkpvbCache;
import br.com.auster.tim.billcheckout.param.MpulktmbCache;
import br.com.auster.tim.billcheckout.param.ServicesCache;
import br.com.auster.tim.billcheckout.tariff.ServiceRatesCache;

public class Rule25_1_Test extends BaseRuleTest {

	private String[] RULES = { "src/main/conf/rules/R25.1-validate-service-activation-debit.drl",
							   "src/test/resources/bgh/r25_1/R25.1-debug-occs.drl" };


    protected void createGlobals() {
    	super.createGlobals();
    	try {
    		Class.forName("org.apache.commons.dbcp.PoolingDriver");
    		Class.forName("oracle.jdbc.driver.OracleDriver");
    		SQLConnectionManager.init(DOMUtils.openDocument("bgh/guiding/pool.xml", false));

	    	Element conf = DOMUtils.openDocument("bgh/guiding/caches.xml", false);
	    	Element conf2 = DOMUtils.openDocument("bgh/guiding/caches2.xml", false);

			ServicesCache servicesCache = new ServicesCache();
			servicesCache.configure(conf2);
			this.workingMemory.setGlobal("servicesCache", servicesCache);

			MpulktmbCache mpulktmbCache = new MpulktmbCache();
			mpulktmbCache.configure(conf);
			this.workingMemory.setGlobal("mpulktmbCache", mpulktmbCache);

			MpulkpvbCache mpulkpvbCache = new MpulkpvbCache();
			mpulkpvbCache.configure(conf);
			this.workingMemory.setGlobal("mpulkpvbCache", mpulkpvbCache);

			ContractServicesDAO contrServicesDAO = new ContractServicesDAO();
			contrServicesDAO.configure(conf2);
			this.workingMemory.setGlobal("contrServicesDAO", contrServicesDAO);

			ServiceRatesCache serviceRatesCache = new ServiceRatesCache();
			serviceRatesCache.configure(conf2);
			this.workingMemory.setGlobal("serviceRatesCache", serviceRatesCache);

    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

	/**
	 * This file contains a service called "Pacote de Minutos" with the activation date between the cycle period,
	 * and there is a subscription value configured, but its R$ 0,00.
	 */
	public void testAccount1() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r25_1/ACCOUNT1.BGH");
			// firing rules
			this.startupRuleEngine(RULES, true);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = { "Regra 25.1 - Asserting the services",
					               //"Regra 25.1 - Validating the OCC section",
					               "Regra 25.1 - Debug OCCs"};
			int[] AGENDA_COUNT = { 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * This file contains a service called "Promo Tarifa Zero com taxa" with subscription value of R$ 14,90.
	 * There is no OCC charging such service.
	 */
	public void testAccount3() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r25_1/ACCOUNT3.BGH");
			// firing rules
			this.startupRuleEngine(RULES, true);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = { "Regra 25.1 - Asserting the services",
					               "Regra 25.1 - Validating the OCC section",
					               "Regra 25.1 - Debug OCCs"};
			int[] AGENDA_COUNT = { 1, 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			// running over results list to check if consequences where ok
			assertEquals(1, this.results.size());
			assertEquals("Não foi encontrada taxa de adesão para o serviço ativado.", this.results.get(0).getDescription());
			assertEquals("Promo Tarifa Zero com taxa", this.results.get(0).getAttributes().getAttributeValue2());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * This file contains a service called "Pacote de Minutos" with the activation date between the cycle period,
	 * and there is no subscription configured.
	 */
	public void testAccount4() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r25_1/ACCOUNT4.BGH");
			// firing rules
			this.startupRuleEngine(RULES, true);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = { "Regra 25.1 - Asserting the services",
					               //"Regra 25.1 - Validating the OCC section",
								   "Regra 25.1 - Debug OCCs"};
			int[] AGENDA_COUNT = { 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/**
	 * This file contains a service called "Pacote de Minutos" with subscription value of R$ 0,00.
	 * There is such OCC charging the service, but value is different.
	 */
	public void testAccount5() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r25_1/ACCOUNT5.BGH");
			// firing rules
			this.startupRuleEngine(RULES, true);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = { "Regra 25.1 - Asserting the services",
					               //"Regra 25.1 - Validating the OCC section",
					               "Regra 25.1 - Debug OCCs"
								  };
			int[] AGENDA_COUNT = { 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}	

	/**
	 * This file contains a service called "Pacote Minutos Compart. Dpto." with subscription value of R$ 2,00.
	 * There is such OCC charging the service, but with amount of R$ 14,90.
	 */
	public void testAccount6() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r25_1/ACCOUNT6.BGH.tmp");
			// firing rules
			this.startupRuleEngine(RULES, true);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = { "Regra 25.1 - Asserting the services",
					               //"Regra 25.1 - Validating the OCC section",
					               "Regra 25.1 - Debug OCCs"
								  };
			int[] AGENDA_COUNT = { 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}	

	/**
	 * From ticket #377
	 */
	public void testAccount8() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r25_1/ACCOUNT8.BGH");
			// firing rules
			this.startupRuleEngine(RULES, true);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = { "Regra 25.1 - Asserting the services",
					               //"Regra 25.1 - Validating the OCC section",
								   "Regra 25.1 - Validating the OCC value",
					               "Regra 25.1 - Debug OCCs"
								  };
			int[] AGENDA_COUNT = { 1, 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			// running over results list to check if consequences where ok
			assertEquals(1, this.results.size());
			Consequence c = this.results.get(0);
			assertEquals("Valor cobrado na adesão diferente do configurado para este serviço", c.getDescription());			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}	
	
	
}