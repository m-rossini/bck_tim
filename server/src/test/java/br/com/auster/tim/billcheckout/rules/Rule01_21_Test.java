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
import br.com.auster.tim.billcheckout.tariff.MyDreamRatesCache;

/**
 * @author anardo
 *
 */
public class Rule01_21_Test extends BaseRuleTest {
	
	/**
	 * NecessÃ¡ria aplicaÃ§Ã£o do guiding.drl pois ele ajusta valor do tipo de evento(voz,dados ou mensagem), 
	 * verifica se Ã© um evento de microcÃ©lula entre outros.
	 */
	private String[] RULES_1 = { "src/test/resources/bgh/guiding/guiding.drl" };
	private String[] RULES_2 = { "src/main/conf/rules/R01.21-my-dream-plan.drl" };

	protected boolean customized = false;

    protected void createGlobals() {
    	super.createGlobals();
    	try {
        	if (customized) {
	    		Class.forName("org.apache.commons.dbcp.PoolingDriver");
	    		Class.forName("oracle.jdbc.driver.OracleDriver");
	    		SQLConnectionManager.init(DOMUtils.openDocument("bgh/guiding/pool.xml", false));
	
		    	Element configCache = DOMUtils.openDocument("bgh/guiding/caches.xml", false);
	
		    	ServicesCache svcCache = new ServicesCache();
		    	svcCache.configure(configCache);
		    	this.workingMemory.setGlobal("serviceCache", svcCache);
	
		    	PlansCache planCache = new PlansCache();
		    	planCache.configure(configCache);
		    	this.workingMemory.setGlobal("planCache", planCache);
	
		    	TariffZoneCache tZoneCache = new TariffZoneCache();
		    	tZoneCache.configure(configCache);
		    	this.workingMemory.setGlobal("tariffZoneCache", tZoneCache);
	
		    	UsageGroupCache ugCache = new UsageGroupCache();
		    	ugCache.configure(configCache);
		    	this.workingMemory.setGlobal("usageGroupCache", ugCache);
	
		    	RateTimeZoneCache rtzCache = new RateTimeZoneCache();
		    	rtzCache.configure(configCache);
		    	this.workingMemory.setGlobal("rateZoneCache", rtzCache);
	
		    	ServicePlanCache spcache = new ServicePlanCache();
		    	spcache.configure(configCache);
		    	this.workingMemory.setGlobal("servicePlanCache", spcache);
	
		    	TariffZoneUsageGroupCache tzugCache = new TariffZoneUsageGroupCache();
		    	tzugCache.configure(configCache);
		    	this.workingMemory.setGlobal("tariffZoneUsageGroupCache", tzugCache);
	
		    	UsageGroupLDCache usageGroupLDCache = new UsageGroupLDCache();
		    	usageGroupLDCache.configure(configCache);
		    	this.workingMemory.setGlobal("usageGroupLDCache", usageGroupLDCache);
        	} else {
		    	Element conf = DOMUtils.openDocument("bgh/guiding/caches2.xml", false);
		    	MyDreamRatesCache myDreamCache = new MyDreamRatesCache();
		    	myDreamCache.configure(conf);
		    	this.workingMemory.setGlobal("myDreamCache", myDreamCache);
	
        	}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    /**
     * Cenário:
     * a) Conta com Plano Meu Sonho
     * b) Seções sendo contabilizadas: Chamadas Locais para Telefones Fixos, Chamadas Locais para Outros Celulares e
     *    Chamadas Locais para Celulares TIM
     * 
     * Conforme vemos na chamada abaixo, item B, foi consumido minutos da franquia do Pacote Meu Sonho.
     * Portanto, apesar de não fazer validação, considerando este tipo de chamada na contabilização para definir
     * o plano a ser tarifado: Meu Sonho 125, Meu Sonho 250 ou Meu Sonho 500.
     */
	public void testAccount1() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r01_21/ACCOUNT1.BGH");
			// firing rules
			this.customized = true;
			this.startupRuleEngine(RULES_1);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			this.customized = false;
			this.startupRuleEngine(RULES_2);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// check final consequence count
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	/**
	 *	Esta conta é o ACCOUNT1.BGH modificado nas seguintes linhas:
	 * 	51110000^31/08/06^19:37:58^SP AREA 11^SP AREA 11^9624-8761^N^^00:00:48^00:00:47^0.30^TS11^VCMC^H^2^C^0.00^N^U^^0^0^0.30^0.00 --Antes 0.28
	 *	51110000^01/09/06^11:48:46^SP AREA 11^SP AREA 11^011-9481-8665^N^^00:01:36^00:01:36^0.59^TS11^VCMC^H^2^C^0.00^N^U^^0^0^0.59^0.00 --Antes 0.57
	 *
	 *	CHAMADAS QUE SOFRERÃO VALIDAÇÃO DE TARIFAS, NESTE BGH:
	 *	51110000^31/08/06^19:07:10^SP AREA 11^SP AREA 11^011-9317-1956^N^^00:00:30^00:00:30^0.18^TS11^VCMC^H^2^C^0.00^N^U^^0^0^0.18^0.00
	 *	51110000^31/08/06^19:37:58^SP AREA 11^SP AREA 11^9624-8761^N^^00:00:48^00:00:47^0.28^TS11^VCMC^H^2^C^0.00^N^U^^0^0^0.28^0.00
	 * 	51110000^31/08/06^19:53:33^SP AREA 11^SP AREA 11^011-8585-4690^N^^00:00:30^00:00:25^0.18^TS11^VCTC^H^0^C^0.00^N^U^^0^0^0.18^0.00
	 *	51110000^31/08/06^19:57:31^SP AREA 11^SP AREA 11^011-8585-4690^N^^00:00:30^00:00:09^0.18^TS11^VCTC^H^0^C^0.00^N^U^^0^0^0.18^0.00

	 * 	51110000^01/09/06^11:48:46^SP AREA 11^SP AREA 11^011-9481-8665^N^^00:01:36^00:01:36^0.57^TS11^VCMC^H^2^C^0.00^N^U^^0^0^0.57^0.00
	 *	51110000^01/09/06^11:56:10^SP AREA 11^SP AREA 11^011-9481-8665^N^^00:00:30^00:00:11^0.18^TS11^VCMC^H^2^C^0.00^N^U^^0^0^0.18^0.00
	 *	51110000^01/09/06^12:12:28^SP AREA 11^SP AREA 11^011-9355-8434^N^^00:00:30^00:00:09^0.18^TS11^VCMC^H^2^C^0.00^N^U^^0^0^0.18^0.00
	 */
	public void testAccount2() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r01_21/ACCOUNT2.BGH");
			// firing rules
			this.customized = true;
			this.startupRuleEngine(RULES_1);
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
			// check final consequence count
			assertEquals(2, this.results.size());
			assertNotNull(counters.containsKey("O valor cobrado neste evento não condiz com a tarifa especificada."));
			assertEquals(2,counters.get("O valor cobrado neste evento não condiz com a tarifa especificada.").intValue());
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/**
     * We have 17 voice calls for Meu Sonho 250, in VCMC tariff zone, with the correct amount. 
	 */
	public void testAccount3() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r01_21/ACCOUNT3.BGH");
			// firing rules
			this.customized = true;
			this.startupRuleEngine(RULES_1);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			this.customized = false;
			this.startupRuleEngine(RULES_2);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// check final consequence count
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/**
     * From ticket #389 
	 */
	public void testAccount4() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r01_21/ACCOUNT4.BGH");
			// firing rules
			this.customized = true;
			this.startupRuleEngine(RULES_1);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			this.customized = false;
			this.startupRuleEngine(RULES_2);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// check final consequence count
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}	
}
