/*
 * Copyright (c) 2004-2010 Auster Solutions. All Rights Reserved.
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
 * Created on 31/03/2010
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
import br.com.auster.tim.billcheckout.tariff.RatesGuidingCache;

/**
 * @author anardo
 *
 */
public class Rule01_1_Test extends BaseRuleTest {
	
	private String[] RULES_1 = { "src/test/resources/bgh/guiding/guiding.drl" };
	private String[] RULES_2 = { "src/main/conf/rules/R01.1-usage-rating.drl",
								 "src/test/resources/bgh/r01_1/freeunits-usage.drl" };

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
		    	Element arg0 = DOMUtils.openDocument("bgh/guiding/caches.xml", false);
		    	RatesGuidingCache rgCache = new RatesGuidingCache();
		    	rgCache.configure(arg0);
		    	this.workingMemory.setGlobal("ratingsCache", rgCache);
	
        	}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
	/**
	 * This is the original file.
	 * Conclusão: Nenhum registro encontrado, só tendo c.PLAN_NAME = 'Plano T Você GSM PR' como filtro da
	 * query, e que é aplicado para todas as chamadas (OneCall). Assim, nenhuma validação ocorre.
	 */
	public void testAccount1() {
		try {
			// firing rules
			this.customized = true;
			this.startupRuleEngine(RULES_1);
			Account acc = this.loadBGHTestFile("bgh/r01_1/ACCOUNT1.BGH");
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
	/**
	 * This is a copy of ACCOUNT1.BGH, with a valid plan and state. Still, all calls will be returned since the
	 * 	rate used is not the one in the database.
	 */
	public void testAccount2() {
		try {
			// firing rules
			this.customized = true;
			this.startupRuleEngine(RULES_1);
			Account acc = this.loadBGHTestFile("bgh/r01_1/ACCOUNT2.BGH");
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
			assertEquals(55, this.results.size());
			assertNotNull(counters.get("O valor do minuto cobrado nesta chamada não condiz com a tarifa especificada."));
			assertEquals(55, counters.get("O valor do minuto cobrado nesta chamada não condiz com a tarifa especificada.").intValue());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	/**
	 * This is a copy of ACCOUNT2.BGH, but some of the voice and message events where modified to meet the correct rate value.
	 * The voice calls modified are: 1 from 07/07/06, and 1 from 04/07/06.
	 */
	public void testAccount3() {
		try {
			// firing rules
			this.customized = true;
			this.startupRuleEngine(RULES_1);
			Account acc = this.loadBGHTestFile("bgh/r01_1/ACCOUNT3.BGH");
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
			assertEquals(53, this.results.size());
			assertNotNull(counters.get("O valor do minuto cobrado nesta chamada não condiz com a tarifa especificada."));
			assertEquals(53, counters.get("O valor do minuto cobrado nesta chamada não condiz com a tarifa especificada.").intValue());
			assertNull(counters.get("O valor final cobrado neste evento não condiz com a tarifa especificada."));

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	/**
	 * All calls in this BGH file, are costed as zero (R$ 0,00). So, no validations will be run and no results will be
	 * 	generated.
	 */
	public void testAccount4() {
		try {
			// firing rules
			this.customized = true;
			this.startupRuleEngine(RULES_1);
			Account acc = this.loadBGHTestFile("bgh/r01_1/ACCOUNT4.BGH");
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
	/**
	 * Here we have all details with a wrong rate used when calculated. But, there is 1 that does not exist in the database.
	 */
	public void testAccount5() {
		try {
			// firing rules
			this.customized = true;
			this.startupRuleEngine(RULES_1);
			Account acc = this.loadBGHTestFile("bgh/r01_1/ACCOUNT5.BGH");
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
			assertEquals(6, this.results.size());
			assertNotNull(counters.get("O valor do minuto cobrado nesta chamada não condiz com a tarifa especificada."));
			assertEquals(1, counters.get("O valor do minuto cobrado nesta chamada não condiz com a tarifa especificada.").intValue());
			assertNotNull(counters.get("O valor por unidade cobrado neste evento não condiz com a tarifa especificada."));
			assertEquals(5, counters.get("O valor por unidade cobrado neste evento não condiz com a tarifa especificada.").intValue());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	/**
	 * Same as ACCOUNT5.BGH, but we changed the rate for the voice call (so it should work fine), changed the dates for 2 SMS
	 * (to use the old, but correct, rate) and changed the charged value for other 2 SMSs. We still have 1 SMS wrong, and
	 * all international calls cannot be found in the database.
	 */
	public void testAccount6() {
		try {
			// firing rules
			this.customized = true;
			this.startupRuleEngine(RULES_1);
			Account acc = this.loadBGHTestFile("bgh/r01_1/ACCOUNT6.BGH");
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
			assertEquals(2, this.results.size());
			assertNotNull(counters.get("O valor por unidade cobrado neste evento não condiz com a tarifa especificada."));
			assertEquals(1, counters.get("O valor por unidade cobrado neste evento não condiz com a tarifa especificada.").intValue());
			assertEquals(1, counters.get("O valor por unidade cobrado neste evento não condiz com a tarifa especificada.").intValue());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	/**
	 	Fatura com 1.174 chamadas
		Algumas chamadas "Recebidas", onde na tabela tarifa(USAGE_RATES), incoming_flag=='Y', nesta fatura:		
		State: RS Plano: Plano Nosso Modo Serviço: TS11 Zona Tarifária: VCMC Modulação: N Data: Mon Jul 17 09:13:24 GMT-03:00 2006 Orientação: Y
		State: RS Plano: Plano Nosso Modo Serviço: TS11 Zona Tarifária: VCTC Modulação: N Data: Mon Jul 17 17:04:16 GMT-03:00 2006 Orientação: Y
		State: RS Plano: Plano Nosso Modo Serviço: TS11 Zona Tarifária: VCTC Modulação: N Data: Sun Jul 16 20:00:52 GMT-03:00 2006 Orientação: Y
		State: RS Plano: Plano Nosso Modo Serviço: TS11 Zona Tarifária: VCTC Modulação: N Data: Sat Jul 15 20:55:09 GMT-03:00 2006 Orientação: Y
		State: RS Plano: Plano Nosso Modo Serviço: TS11 Zona Tarifária: VCTC Modulação: N Data: Sat Jul 15 10:17:13 GMT-03:00 2006 Orientação: Y
		State: RS Plano: Plano Nosso Modo Serviço: TS11 Zona Tarifária: VCMC Modulação: N Data: Sun Jul 16 13:07:06 GMT-03:00 2006 Orientação: Y
	 */
	public void testAccount7() {
		try {
			this.customized = true;
			this.startupRuleEngine(RULES_1);
			Account acc = this.loadBGHTestFile("bgh/r01_1/ACCOUNT7.BGH");
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
			// Changed from 9 to 13, after validating string-format of elements. Previously we were
			// ignoring a 1 cent difference in 4 calls (V3F / TS11 )
			assertEquals(13, this.results.size());
			assertNotNull(counters.get("O valor do minuto cobrado nesta chamada não condiz com a tarifa especificada."));
			assertEquals(13, counters.get("O valor do minuto cobrado nesta chamada não condiz com a tarifa especificada.").intValue());
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
			Account acc = this.loadBGHTestFile("bgh/r01_1/ACCOUNT8.BGH");
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
			assertEquals(1, this.results.size());
			assertNotNull(counters.get("O valor do minuto cobrado nesta chamada não condiz com a tarifa especificada."));
			assertEquals(1, counters.get("O valor do minuto cobrado nesta chamada não condiz com a tarifa especificada.").intValue());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	/**
	 * É a mesma conta de ACCOUNT8.BGH, porém alterado duração para 166:39:06, conforme abaixo:
	 * 51110000^01/05/07^17:42:43^PE AREA 81^CE AREA 85^081-3361-2545^N^VCR^00:02:12^166:39:06^5.10^DSL^1ETM^V^2^C^0.00^N^U^^41^2736^5.10^0.00
	 * O limite para este caso seria 9999/60 == 166,65 horas == 166hs39min.
	 * Assim coloquei 166hs39min06segs para gerar crítica.
	 */
	public void testAccount9() {
		try {
			// firing rules
			this.customized = true;
			this.startupRuleEngine(RULES_1);
			Account acc = this.loadBGHTestFile("bgh/r01_1/ACCOUNT9.BGH");
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
			assertEquals(1, this.results.size());
			assertNotNull(counters.get("A duração deste evento é maior que o volume configurado para tarifação"));
			assertEquals(1, counters.get("A duração deste evento é maior que o volume configurado para tarifação").intValue());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Account should be OK.
	 *
	 * FOR NOW, THIS TESTCASE IS DISABLED WAITING FOR TIM DEFINITION
	 *    OF WHICH INFORMATION TO USE AS THE CALL's STATE. THIS IS
	 *    IMPORTANT TO IDENTIFY THE CORRECT RATE TO USE WHEN VALIDATING
	 *    USAGE RATES.
	 * 
	 * Não existem registros com init_rate !=null para realizar validações, em compatibilização
	 * ao modelo antigo. Regra não fará validação das chamadas, não existindo tarifas na base de dados.
	 */
	public void testAccount10() {
		try {
			// firing rules
			this.customized = true;
			this.startupRuleEngine(RULES_1);
			Account acc = this.loadBGHTestFile("bgh/r01_1/ACCOUNT10.BGH");
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
	
	/**
	 * Conta com Plano Meu Sonho, que não deve entrar na regra para possível validação, somente em suas
	 * chamadas locais.
	 * No momento, não existem tarifas cadastradas em USAGE_RATES, das demais chamadas desta conta.
	 * Assim, não há possibilidade de geração de crítica.
	 */
	public void testAccount11(){
		try {
			// firing rules
			this.customized = true;
			this.startupRuleEngine(RULES_1);
			Account acc = this.loadBGHTestFile("bgh/r01_1/ACCOUNT11.BGH");
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
}
