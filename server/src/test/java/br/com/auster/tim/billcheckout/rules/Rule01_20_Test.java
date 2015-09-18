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
 * 16/04/2010
 */
public class Rule01_20_Test extends BaseRuleTest {
	
	/**
	 * Necessária aplicação do guiding.drl pois ele ajusta valor do tipo de evento(voz,dados ou mensagem), 
	 * verifica se é um evento de microcélula entre outros.
	 */
	private String[] RULES_1 = { "src/test/resources/bgh/guiding/guiding.drl" };
	private String[] RULES_2 = { "src/main/conf/rules/R01.20-half-rate-plan.drl" };

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
		    	Element conf = DOMUtils.openDocument("bgh/guiding/caches.xml", false);
		    	RatesGuidingCache rgCache = new RatesGuidingCache();
		    	rgCache.configure(conf);
		    	this.workingMemory.setGlobal("ratingsCache", rgCache);
	
        	}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    /**
     * Conta com estas duas chamadas cobradas, mas somente valida do dia 19/09/06, pois não há aplicação de franquia.
     * Custo de 0,45 centavos para cada 30 segundos, sendo que os 2 primeiros minutos do dia tarifa cheia e os
     * demais a 0,45/2==0,225 centavos a cada 30 segundos. Cobrança correta, portanto não gera crítica.
     * 
     * Para a chamada do dia 14/09/06, não se valida mais se houvesse outra chamada local desta que cobrasse, de
     * forma errada feita pelo BSCS, cobraria um valor errado e geraria critica. Acontece que quando acaba a
     * franquia, estão se contabilizando os minutos usados desta franquia nos dois primeiros, mas que por defini-
     * ção deste plano, deveria ocorrer a contagem a partir do término da franquia.
     * 
     * Chamadas locais com cobrança:
     * 
     * 51110000^14/09/06^18:29:43^SP AREA 11^SP FIXO - AREA 11^3871-9635^DI^^00:02:30^00:02:26^1.78^TS11^VCFC^H^9^C^0.24^P^U^^0^0^1.78^0.00
	 * 51110010^ICMS^25.00
	 * 51110020^4577933^23^DCH01^TIM Meia Tarifa 20^1^1^00:00:18^00:00:18^Sec^27000^0.24
	 * 51110000^19/09/06^15:08:21^SP AREA 11^SP FIXO - AREA 11^3829-8100^DI^^00:10:30^00:10:28^5.62^TS11^VCFC^H^9^C^0.00^N^U^^0^0^5.62^0.00
	 * 51110010^ICMS^25.00
	 * 51120000^0000:25:42^0000:24:53^7.40^7.40^9^7.55^0.00
     */
    
    public void testAccount1() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r01_20/ACCOUNT1.BGH");
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
			assertEquals(0, this.results.size());
			assertFalse(counters.containsKey("O valor cobrado neste evento não condiz com a tarifa especificada."));
			assertNull(counters.get("O valor cobrado neste evento não condiz com a tarifa especificada."));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
    
    /**
     * Conta com "Plano Nosso Modo". Como para esta regra somente permissão para plano "TIM Meia Tarifa",
     * eventos são descartados e nem entram na regra para procurar tarifa e posteriormente validar.
     * No BGH:
     * 51000000^044-9923-4443^4618517^Plano Nosso Modo^MARCIA REGINA CHEFER BOTTENCOURT
     */
	public void testAccount2() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r01_20/ACCOUNT2.BGH");
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
			assertEquals(0, this.results.size());
			assertFalse(counters.containsKey("O valor cobrado neste evento não condiz com a tarifa especificada."));
			assertNull(counters.get("O valor cobrado neste evento não condiz com a tarifa especificada."));
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	/**
	 * Esta conta é original, e é a mesma conta do path: bgh-tools\src\test\resources\examples\ACCOUNT5.BGH
	 * 
	 * Observações deste BGH:
	 * 
	 * A) 34000100^048-9978-1930^4619125^TIM Meia Tarifa 10^1^1^Sec^00:00:00^00:06:20^00:06:20^00:00:00^25/06/06^24/07/06^17807283^1
	 * Considerando que a vigência deste pacote FreeUnits visto na chave 34000100, é do período de 25/06/06 a
	 * 24/07/06, a chamada abaixo que gerava crítica de cálculo de meia tarifa é desconsiderada, pois nesta
	 * fatura não sabemos se a chamada de 17/06/06 foi tarifada em vigência do plano Meia Tarifa.
	 * 51110000^17/06/06^15:59:01^SC AREA 48^SC AREA 48^9123-5363^N^^00:02:30^00:02:28^1.95^TS11^VCMS^H^0^C^0.00^N^U^^0^0^1.95^0.00
	 * 
	 * B) Identificado neste BGH que está sendo considerado os dois primeiros minutos de uma chamada do dia que
	 * teve uma aplicação de franquia, e já em seguida aplica-se a meia tarifa para as demais chamadas deste dia.
	 * À princípio entendíamos que seria contabilizado esses dois primeiros à partir do fim do pacote de franquia,
	 * e não incluindo estes primeiros minutos de uma chamada que foi franqueada.
	 * 
	 * Em chamadas de cobrança parcial, contabiliza os minutos não franqueados, para este dia,visto abaixo.
	 * Abaixo, dados deste problema, que provocará críticas indevidas,pois o consumo diário dos 2 minutos é afetado:
	 * 51110000^12/07/06^09:36:46^SC AREA 48^SC AREA 48^048-9994-8110^DI^^00:01:06^00:01:06^0.00^TS11^VCTS^H^8^C^0.96^F^U^^0^0^0.00^0.00
	 * 51110000^12/07/06^10:27:18^SC AREA 48^SC AREA 48^048-9994-8110^DI^^00:02:30^00:02:30^0.58^TS11^VCTS^H^9^C^0.91^P^U^^0^0^0.58^0.00
	 * 51110010^ICMS^25.00
	 * 51110020^17807283^1^DCH03^TIM Meia Tarifa 10^1^1^00:01:32^00:01:32^Sec^134933^0.91
	 * 51110000^12/07/06^10:41:01^SC AREA 48^SC AREA 48^048-9994-8110^DI^^00:01:24^00:01:23^0.61^TS11^VCTS^H^9^C^0.00^N^U^^0^0^0.61^0.00
	 */
	public void testAccount3() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r01_20/ACCOUNT3.BGH");
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
			System.out.println(this.results);
			// check final consequence count
			assertEquals(1, this.results.size());
			assertNotNull(counters.containsKey("O valor cobrado neste evento não condiz com a tarifa especificada."));
			assertEquals(1,counters.get("O valor cobrado neste evento não condiz com a tarifa especificada.").intValue());
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/**
	 * Critica gerada no mesmo cenário em que o BSCS está considerando para a primeira chamada do dia,
	 * o consumo dos dois minutos iniciais, ainda dentro do pacote de franquia.
	 * 
	   51110000^02/02/10^12:10:05^SP AREA 11^SP FIXO - AREA 11^2693-6265^DI^^00:02:36^00:02:33^0.60^TS11^VCFC^H^9^C^1.97^P^U^^0^0^0.60^0.00^724031100C707
	   51110010^ICMS^25.00
	   51110020^51776047^19^DCH01^TIM Meia Tarifa 20^1^1^00:01:58^00:02:00^Sec^224000^1.97
	   51110000^02/02/10^17:50:02^SP AREA 11^SP FIXO - AREA 11^2693-6265^DI^^00:01:18^00:01:17^0.72^TS11^VCFC^H^9^C^0.00^N^U^^0^0^0.72^0.00^724031100C707
	   51110010^ICMS^25.00
	 */
	public void testAccount4() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r01_20/ACCOUNT4.BGH");
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
			System.out.println(this.results);
			// check final consequence count
			assertEquals(1, this.results.size());
			assertNotNull(counters.containsKey("O valor cobrado neste evento não condiz com a tarifa especificada."));
			assertEquals(1,counters.get("O valor cobrado neste evento não condiz com a tarifa especificada.").intValue());
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/**
	 * Consideração nesta conta:
	 * Existem chamadas locais(12/01 e 13/01) fora do ciclo(19/01 a 18/02) que poderiam não ser locais a cobrar,
	 * mas serem locais originadas sem aplicação de franquia, e estar gerando critica indevidamente, pois não
	 * se aplicaria o cálculo de aplicação do Meia Tarifa.
	 * 
	 * É uma chamada parcial, onde erradamente BSCS contabiliza um consumo de 2 mins. ainda estando na franquia.
	 * Acredito ainda que, não somente com chamada parcial ocorrendo terá esse tipo de problema, mas provável que
	 * o BSCS está considerando 2 mins. diários em qualquer das chamadas franqueadas.
	 * 
	 * Trecho do BGH:
	 * 51110000^22/01/10^09:16:24^PR AREA 41^PR MOVEL TIM - AREA 41^9931-4210^DI^^00:01:12^00:01:12^0.00^TS11^VCTS^H^8^C^1.20^F^U^^0^0^0.00^0.00^724044100492A
	 * 51110000^22/01/10^09:36:47^PR AREA 41^PR MOVEL TIM - AREA 41^9931-4210^DI^^00:00:30^00:00:20^0.20^TS11^VCTS^H^8^C^0.30^P^U^^0^0^0.20^0.00^724044100A934
	 * 51110000^22/01/10^10:56:54^PR AREA 41^PR MOVEL TIM - AREA 41^9615-7172^DI^^00:01:00^00:00:56^0.65^TS11^VCTS^H^9^C^0.00^N^U^^0^0^0.65^0.00^724044100212F
	 * 51110000^22/01/10^11:31:36^PR AREA 41^PR MOVEL TIM - AREA 41^9615-7172^DI^^00:00:30^00:00:13^0.25^TS11^VCTS^H^9^C^0.00^N^U^^0^0^0.25^0.00^724044100212F
	 * 51110000^22/01/10^12:06:55^PR AREA 41^PR FIXO - AREA 41^3085-3487^DI^^00:00:54^00:00:53^0.45^TS11^VCFS^H^9^C^0.00^N^U^^0^0^0.45^0.00^724044100492A
	 * 
	 * Críticas:
	 * [TelcoConsequence/0 : Description=[O valor cobrado neste evento não condiz com a tarifa especificada.].Rule=[0].Attrs=ConsequenceAttributeList { [0] Número Origem=041-9928-4294, }ConsequenceAttributeList { [1] Número Destino=9615-7172, }ConsequenceAttributeList { [2] Data/Hora=2010-01-22T10:56:54, }ConsequenceAttributeList { [3] Duração Cobrada=00h01m00s, }ConsequenceAttributeList { [4] Plano=TIM Meia Tarifa, }ConsequenceAttributeList { [5] Serviço=Serviço de Voz, }ConsequenceAttributeList { [6] Tariff Zone=VCTS, }ConsequenceAttributeList { [7] Rate TimeZone=DI, }ConsequenceAttributeList { [8] Orientação da Chamada=originada(N), }ConsequenceAttributeList { [9] Valor cobrado=0,65, }ConsequenceAttributeList { [10] Valor esperado=1,00, }ConsequenceAttributeList {  }ConsequenceAttributeList {  }ConsequenceAttributeList {  }ConsequenceAttributeList {  }ConsequenceAttributeList {  }ConsequenceAttributeList {  }ConsequenceAttributeList {  }ConsequenceAttributeList {  }ConsequenceAttributeList {  }.Account=[-].Cycle=[-].Carrier=[-].Geo=[-].Time=[-]
	 * [TelcoConsequence/0 : Description=[O valor cobrado neste evento não condiz com a tarifa especificada.].Rule=[0].Attrs=ConsequenceAttributeList { [0] Número Origem=041-9928-4294, }ConsequenceAttributeList { [1] Número Destino=9615-7172, }ConsequenceAttributeList { [2] Data/Hora=2010-01-22T11:31:36, }ConsequenceAttributeList { [3] Duração Cobrada=00h00m30s, }ConsequenceAttributeList { [4] Plano=TIM Meia Tarifa, }ConsequenceAttributeList { [5] Serviço=Serviço de Voz, }ConsequenceAttributeList { [6] Tariff Zone=VCTS, }ConsequenceAttributeList { [7] Rate TimeZone=DI, }ConsequenceAttributeList { [8] Orientação da Chamada=originada(N), }ConsequenceAttributeList { [9] Valor cobrado=0,25, }ConsequenceAttributeList { [10] Valor esperado=0,50, }ConsequenceAttributeList {  }ConsequenceAttributeList {  }ConsequenceAttributeList {  }ConsequenceAttributeList {  }ConsequenceAttributeList {  }ConsequenceAttributeList {  }ConsequenceAttributeList {  }ConsequenceAttributeList {  }ConsequenceAttributeList {  }.Account=[-].Cycle=[-].Carrier=[-].Geo=[-].Time=[-]
	 * [TelcoConsequence/0 : Description=[O valor cobrado neste evento não condiz com a tarifa especificada.].Rule=[0].Attrs=ConsequenceAttributeList { [0] Número Origem=041-9928-4294, }ConsequenceAttributeList { [1] Número Destino=3085-3487, }ConsequenceAttributeList { [2] Data/Hora=2010-01-22T12:06:55, }ConsequenceAttributeList { [3] Duração Cobrada=00h00m54s, }ConsequenceAttributeList { [4] Plano=TIM Meia Tarifa, }ConsequenceAttributeList { [5] Serviço=Serviço de Voz, }ConsequenceAttributeList { [6] Tariff Zone=VCFS, }ConsequenceAttributeList { [7] Rate TimeZone=DI, }ConsequenceAttributeList { [8] Orientação da Chamada=originada(N), }ConsequenceAttributeList { [9] Valor cobrado=0,45, }ConsequenceAttributeList { [10] Valor esperado=0,60, }ConsequenceAttributeList {  }ConsequenceAttributeList {  }ConsequenceAttributeList {  }ConsequenceAttributeList {  }ConsequenceAttributeList {  }ConsequenceAttributeList {  }ConsequenceAttributeList {  }ConsequenceAttributeList {  }ConsequenceAttributeList {  }.Account=[-].Cycle=[-].Carrier=[-].Geo=[-].Time=[-]
	 */
	public void testAccount5() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r01_20/ACCOUNT5.BGH");
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
			System.out.println(this.results);
			// check final consequence count
			assertEquals(3, this.results.size());
			assertNotNull(counters.containsKey("O valor cobrado neste evento não condiz com a tarifa especificada."));
			assertEquals(3,counters.get("O valor cobrado neste evento não condiz com a tarifa especificada.").intValue());			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}