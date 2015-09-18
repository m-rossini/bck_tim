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
import br.com.auster.billcheckout.model.ModelLoader;
import br.com.auster.common.sql.SQLConnectionManager;
import br.com.auster.common.xml.DOMUtils;
import br.com.auster.dware.graph.Request;
import br.com.auster.om.invoice.Account;
import br.com.auster.tim.billcheckout.bscs.AnatelCodeCache;
import br.com.auster.tim.billcheckout.bscs.ContractExistsPackCache;
import br.com.auster.tim.billcheckout.bscs.RateplanHistCache;
import br.com.auster.tim.billcheckout.bscs.SGTCustomCache;
import br.com.auster.tim.billcheckout.bscs.ScanStructureLevelCache;
import br.com.auster.tim.billcheckout.crashprogram.CostCenterCache;
import br.com.auster.tim.billcheckout.npack.NPackPackageInfoCache;
import br.com.auster.tim.billcheckout.npack.NPackRateCache;
import br.com.auster.tim.billcheckout.param.CBCFContractDAO;
import br.com.auster.tim.billcheckout.param.ContractExistsServicesCache;
import br.com.auster.tim.billcheckout.param.ContractPromotionsCache;
import br.com.auster.tim.billcheckout.param.ContractServicesCache;
import br.com.auster.tim.billcheckout.param.ContractServicesDAO;
import br.com.auster.tim.billcheckout.param.CustomerSituationCache;
import br.com.auster.tim.billcheckout.param.DwPromoMcCache;
import br.com.auster.tim.billcheckout.param.ElegibilityCache;
import br.com.auster.tim.billcheckout.param.MpufftabCache;
import br.com.auster.tim.billcheckout.param.MpufftabListCache;
import br.com.auster.tim.billcheckout.param.MpulkfxoCache;
import br.com.auster.tim.billcheckout.param.MpulkpvbCache;
import br.com.auster.tim.billcheckout.param.MpulktmbCache;
import br.com.auster.tim.billcheckout.param.OCCContractBreakCache;
import br.com.auster.tim.billcheckout.param.PenaltyInterestLDNFCache;
import br.com.auster.tim.billcheckout.param.PenaltyInterestLocalNFCache;
import br.com.auster.tim.billcheckout.param.PlansCache;
import br.com.auster.tim.billcheckout.param.ProgressiveDiscountCache;
import br.com.auster.tim.billcheckout.tariff.CrashProgramRatesCache;
import br.com.auster.tim.billcheckout.tariff.MegaTIMCache;
import br.com.auster.tim.billcheckout.tariff.MicrocellRatesCache;
import br.com.auster.tim.billcheckout.tariff.MicrocellSGTRatesCache;
import br.com.auster.tim.billcheckout.tariff.MyDreamRatesCache;
import br.com.auster.tim.billcheckout.tariff.PricePlanRatesCache;
import br.com.auster.tim.billcheckout.tariff.RatesGuidingCache;
import br.com.auster.tim.billcheckout.tariff.ServiceRatesCache;
import br.com.auster.tim.billcheckout.util.FreeUnitHelper;
import br.com.auster.tim.billcheckout.util.UsageGroupingCache;



/**
 * @author mruao
 * @version $Id$
 *
 */
public class Rule16_1_Test extends BaseRuleTest {


	private static String[] RULES_FILES = { "src/main/conf/rules/INIT-reset-step.drl",
											"src/main/conf/rules/R16.1-hashcode-validation.drl" };


	protected void createGlobals() {
		super.createGlobals();
		try {

			Class.forName("org.apache.commons.dbcp.PoolingDriver");
			Class.forName("oracle.jdbc.driver.OracleDriver");
			SQLConnectionManager.init(DOMUtils.openDocument(
					"bgh/guiding/pool.xml", false));

			Element arg0 = DOMUtils.openDocument("bgh/guiding/caches.xml",
					false);

			SGTCustomCache sgtCache = new SGTCustomCache();
			sgtCache.configure(arg0);
			this.workingMemory.setGlobal("sgtCache", sgtCache);

			MpufftabCache mpufftabCache = new MpufftabCache();
			mpufftabCache.configure(arg0);
			this.workingMemory.setGlobal("mpufftabCache", mpufftabCache);

			MpulkfxoCache mpulkfxoCache = new MpulkfxoCache();
			mpulkfxoCache.configure(arg0);
			this.workingMemory.setGlobal("mpulkfxoCache", mpulkfxoCache);

			MpulktmbCache mpulktmbCache = new MpulktmbCache();
			mpulktmbCache.configure(arg0);
			this.workingMemory.setGlobal("mpulktmbCache", mpulktmbCache);

			MpulkpvbCache mpulkpvbCache = new MpulkpvbCache();
			mpulkpvbCache.configure(arg0);
			this.workingMemory.setGlobal("mpulkpvbCache", mpulkpvbCache);

			ContractServicesCache contractServicesCache = new ContractServicesCache();
			contractServicesCache.configure(arg0);
			this.workingMemory.setGlobal("contractServicesCache", contractServicesCache);

			MpufftabListCache mpufftabListCache = new MpufftabListCache();
			mpufftabListCache.configure(arg0);
			this.workingMemory.setGlobal("mpufftabListCache", mpufftabListCache);

			ContractPromotionsCache contractPromotionsCache = new ContractPromotionsCache();
			contractPromotionsCache.configure(arg0);
			this.workingMemory.setGlobal("contractPromotionsCache", contractPromotionsCache);

			OCCContractBreakCache OCCDescriptions = new OCCContractBreakCache();
			OCCDescriptions.configure(arg0);
			this.workingMemory.setGlobal("OCCDescriptions", OCCDescriptions);

			PenaltyInterestLDNFCache penaltyInterestLDNFCache = new PenaltyInterestLDNFCache();
			penaltyInterestLDNFCache.configure(arg0);
			this.workingMemory.setGlobal("penaltyInterestLDNFCache", penaltyInterestLDNFCache);

			PenaltyInterestLocalNFCache penaltyInterestLocalNFCache = new PenaltyInterestLocalNFCache();
			penaltyInterestLocalNFCache.configure(arg0);
			this.workingMemory.setGlobal("penaltyInterestLocalNFCache", penaltyInterestLocalNFCache);

			UsageGroupingCache bghUsageGroupingCache = new UsageGroupingCache();
			bghUsageGroupingCache.configure(arg0);
			this.workingMemory.setGlobal("bghUsageGroupingCache", bghUsageGroupingCache);

			CBCFContractDAO CBCFInterface = new CBCFContractDAO();
			CBCFInterface.configure(arg0);
			this.workingMemory.setGlobal("CBCFInterface", CBCFInterface);

			ContractServicesDAO contrServicesDAO = new ContractServicesDAO();
			contrServicesDAO.configure(arg0);
			this.workingMemory.setGlobal("contrServicesDAO", contrServicesDAO);

			FreeUnitHelper fuHelper = new FreeUnitHelper();
			fuHelper.configure(arg0);
			this.workingMemory.setGlobal("fuHelper", fuHelper);

    		ModelLoader modelLoader =  new ModelLoader();
    		Element configuration = DOMUtils.openDocument("bgh/thresholds/model-caches.xml", false);
    		modelLoader.configure(configuration);
    		workingMemory.setGlobal( "modelLoader", modelLoader );

    		DwPromoMcCache dwPromoMcCache = new DwPromoMcCache();
    		dwPromoMcCache.configure(arg0);
			this.workingMemory.setGlobal("dwPromoMcCache", dwPromoMcCache);

			ProgressiveDiscountCache progressiveDiscountCache = new ProgressiveDiscountCache();
			progressiveDiscountCache.configure(arg0);
			this.workingMemory.setGlobal("progressiveDiscountCache", progressiveDiscountCache);

			RateplanHistCache rateplanHist = new RateplanHistCache();
			rateplanHist.configure(arg0);
			this.workingMemory.setGlobal("rateplanHist", rateplanHist);


			// Melhorias F2
			ElegibilityCache elegibCache = new ElegibilityCache();
			elegibCache.configure(arg0);
			this.workingMemory.setGlobal("elegibCache", elegibCache);
			
			ContractExistsServicesCache contrExistsServCache = new ContractExistsServicesCache();
			contrExistsServCache.configure(arg0);
			this.workingMemory.setGlobal("contrExistsServCache", contrExistsServCache);
			
			ContractExistsPackCache contrExistsPackCache = new ContractExistsPackCache();
			contrExistsPackCache.configure(arg0);
			this.workingMemory.setGlobal("contrExistsPackCache", contrExistsPackCache);
			
			ScanStructureLevelCache scanStructLevelCache = new ScanStructureLevelCache();
			scanStructLevelCache.configure(arg0);
			this.workingMemory.setGlobal("scanStructLevelCache", scanStructLevelCache);
			
			AnatelCodeCache anatelCodeCache = new AnatelCodeCache();
			anatelCodeCache.configure(arg0);
			this.workingMemory.setGlobal("anatelCodeCache", anatelCodeCache);
			
			// Melhorias F3
			MicrocellRatesCache microcellRatesCache = new MicrocellRatesCache();
			microcellRatesCache.configure(arg0);
			this.workingMemory.setGlobal("microcellRatesCache", microcellRatesCache);
			
			MicrocellSGTRatesCache microcellSGTRatesCache = new MicrocellSGTRatesCache();
			microcellSGTRatesCache.configure(arg0);
			this.workingMemory.setGlobal("microcellSGTRatesCache", microcellSGTRatesCache);
			
			ServiceRatesCache serviceRatesCache = new ServiceRatesCache();
			serviceRatesCache.configure(arg0);
			this.workingMemory.setGlobal("serviceRatesCache", serviceRatesCache);
			
			RatesGuidingCache ratingsCache = new RatesGuidingCache();
			ratingsCache.configure(arg0);
			this.workingMemory.setGlobal("ratingsCache", ratingsCache);
			
			NPackPackageInfoCache npackInfoCache = new NPackPackageInfoCache();
			npackInfoCache.configure(arg0);
			this.workingMemory.setGlobal("npackInfoCache", npackInfoCache);
			
			NPackRateCache npackRateCache = new NPackRateCache();
			npackRateCache.configure(arg0);
			this.workingMemory.setGlobal("npackRateCache", npackRateCache);
			
			CostCenterCache costCenterCache = new CostCenterCache();
			costCenterCache.configure(arg0);
			this.workingMemory.setGlobal("costCenterCache", costCenterCache);
			
			CrashProgramRatesCache cashProgramRatesCache = new CrashProgramRatesCache();
			cashProgramRatesCache.configure(arg0);
			this.workingMemory.setGlobal("cashProgramRatesCache", cashProgramRatesCache);
			
			ServiceRatesCache alternateServiceRatesCache = new ServiceRatesCache();
			alternateServiceRatesCache.configure(arg0);
			this.workingMemory.setGlobal("alternateServiceRatesCache", alternateServiceRatesCache);
			
			MegaTIMCache megatimCache = new MegaTIMCache();
			megatimCache.configure(arg0);
			this.workingMemory.setGlobal("megatimCache", megatimCache);
			
			CustomerSituationCache customerSituationCache = new CustomerSituationCache();
			customerSituationCache.configure(arg0);
			this.workingMemory.setGlobal("customerSituationCache", customerSituationCache);
			
			PlansCache plansCache = new PlansCache();
			plansCache.configure(arg0);
			this.workingMemory.setGlobal("plansCache", plansCache);
			
			PricePlanRatesCache pricePlanCache = new PricePlanRatesCache();
			pricePlanCache.configure(arg0);
			this.workingMemory.setGlobal("pricePlanCache", pricePlanCache);
			
			PricePlanRatesCache alternatePricePlanCache = new PricePlanRatesCache();
			alternatePricePlanCache.configure(arg0);
			this.workingMemory.setGlobal("alternatePricePlanCache", alternatePricePlanCache);
			
			MyDreamRatesCache myDreamCache = new MyDreamRatesCache();
			myDreamCache.configure(arg0);
			this.workingMemory.setGlobal("myDreamCache", myDreamCache);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testAccount1Step2() {
		try {
			// firing rules
			this.startupRuleEngine(RULES_FILES);
			Account acc = this.loadBGHTestFile( "bgh/r16_1/ACCOUNT1.BGH" );
			this.assertAccount(acc, buildRequest("S2"));
			this.workingMemory.fireAllRules();
			HashMap<String, Integer> counters = new HashMap<String, Integer>();
			for (Consequence c : this.results) {
				String k = c.getDescription();
				if (! counters.containsKey(k)) {
					counters.put(k, new Integer(0));
				}
				Integer d = counters.get(k);
				counters.put(k, new Integer(d.intValue() + 1));
			}
			// we expect only one error item
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testAccount2Step2() {
		try {
			// firing rules
			this.startupRuleEngine(RULES_FILES);
			Account acc = this.loadBGHTestFile( "bgh/r16_1/ACCOUNT2.BGH" );
			this.assertAccount(acc, buildRequest("S2"));
			this.workingMemory.fireAllRules();
			HashMap<String, Integer> counters = new HashMap<String, Integer>();
			for (Consequence c : this.results) {
				String k = c.getDescription();
				if (! counters.containsKey(k)) {
					counters.put(k, new Integer(0));
				}
				Integer d = counters.get(k);
				counters.put(k, new Integer(d.intValue() + 1));
			}
			// we expect only one error item
			assertEquals(2, this.results.size());
			assertNotNull(counters.get("O hashcode desta nota fiscal está vazio."));
			assertEquals(1, counters.get("O hashcode desta nota fiscal está vazio.").intValue());
			assertNotNull(counters.get("O hashcode desta nota fiscal não foi gerado corretamente."));
			assertEquals(1, counters.get("O hashcode desta nota fiscal não foi gerado corretamente.").intValue());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * 	Modified account UAT20OD20070511111018-00.BGH from TIM testcase
	 *
	 * Shouldnt have any errors since it does not contain any hashcode
	 */
	public void testAccount3Step2() {
		try {
			// firing rules
			this.startupRuleEngine(RULES_FILES);
			Account acc = this.loadBGHTestFile( "bgh/r16_1/ACCOUNT3.BGH" );
			this.assertAccount(acc, buildRequest("S2"));
			this.workingMemory.fireAllRules();
			// we expect only one error item
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * 	Added due to modifications in BGH file. Tag 11300000 (Billing Address) was added
	 */
	public void testAccount4Step2() {
		try {
			// firing rules
			this.startupRuleEngine(RULES_FILES);
			Account acc = this.loadBGHTestFile( "bgh/r16_1/ACCOUNT4.BGH" );
			this.assertAccount(acc, buildRequest("S2"));
			this.workingMemory.fireAllRules();
			// we expect only one error item
			assertEquals(1, this.results.size());
			// This was the hashcode generated prior to the BGH layout modification
			assertFalse( "5B9F.2B9A.BA3B.7DC2.E0EC.A4B1.C2C9.57E6".equals(this.results.get(0).getAttributes().getAttributeValue3()) );
			// This was the hashcode generated prior to the BGH layout modification
			assertTrue( "C213.9BCB.F556.6670.62B9.B433.DBFD.D792".equals(this.results.get(0).getAttributes().getAttributeValue3()) );
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * 	Added due ticket #xxxx
	 *  This is the ACCOUNT6.BGH file, with modifications
	 */
	public void testAccount5Step2() {
		try {
			// firing rules
			this.startupRuleEngine(RULES_FILES);
			Account acc = this.loadBGHTestFile( "bgh/r16_1/ACCOUNT5.BGH" );
			this.assertAccount(acc, buildRequest("S2"));
			this.workingMemory.fireAllRules();
			// we expect only one error item
			assertEquals(1, this.results.size());
			// This was the hashcode generated prior to the BGH layout modification
			assertFalse( "DE47.AAAA.6F0C.F9C6.7C4F.4081.25CE.F175".equals(this.results.get(0).getAttributes().getAttributeValue3()) );
			// This was the hashcode generated prior to the BGH layout modification
			assertTrue( "DE47.EA5E.6F0C.F9C6.7C4F.4081.25CE.F175".equals(this.results.get(0).getAttributes().getAttributeValue3()) );
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * 	Added due ticket #xxxx
	 */
	public void testAccount6Step2() {
		try {
			// firing rules
			this.startupRuleEngine(RULES_FILES);
			Account acc = this.loadBGHTestFile( "bgh/r16_1/ACCOUNT6.BGH" );
			this.assertAccount(acc, buildRequest("S2"));
			this.workingMemory.fireAllRules();
			// we expect only one error item
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testAccount1Step3() {
		try {
			// firing rules
			this.startupRuleEngine(RULES_FILES);
			Account acc = this.loadBGHTestFile( "bgh/r16_1/ACCOUNT1.BGH" );
			this.assertAccount(acc, buildRequest("S3"));
			this.workingMemory.fireAllRules();
			HashMap<String, Integer> counters = new HashMap<String, Integer>();
			for (Consequence c : this.results) {
				String k = c.getDescription();
				if (! counters.containsKey(k)) {
					counters.put(k, new Integer(0));
				}
				Integer d = counters.get(k);
				counters.put(k, new Integer(d.intValue() + 1));
			}
			// there should be no errors, due to step3 mode
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testAccount2Step3() {
		try {
			// firing rules
			this.startupRuleEngine(RULES_FILES);
			Account acc = this.loadBGHTestFile( "bgh/r16_1/ACCOUNT2.BGH" );
			this.assertAccount(acc, buildRequest("S3"));
			this.workingMemory.fireAllRules();
			HashMap<String, Integer> counters = new HashMap<String, Integer>();
			for (Consequence c : this.results) {
				String k = c.getDescription();
				if (! counters.containsKey(k)) {
					counters.put(k, new Integer(0));
				}
				Integer d = counters.get(k);
				counters.put(k, new Integer(d.intValue() + 1));
			}
			// there should be no errors, due to step3 mode
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testAccount3Step3() {
		try {
			// firing rules
			this.startupRuleEngine(RULES_FILES);
			Account acc = this.loadBGHTestFile( "bgh/r16_1/ACCOUNT3.BGH" );
			this.assertAccount(acc, buildRequest("S3"));
			this.workingMemory.fireAllRules();
			// there should be no errors, due to step3 mode
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testAccount4Step3() {
		try {
			// firing rules
			this.startupRuleEngine(RULES_FILES);
			Account acc = this.loadBGHTestFile( "bgh/r16_1/ACCOUNT4.BGH" );
			this.assertAccount(acc, buildRequest("S3"));
			this.workingMemory.fireAllRules();
			// there should be no errors, due to step3 mode
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}




	private Request buildRequest(String _mode) {
		Request r = new Request() {
			public long getWeight() { return 0; }

		};
		r.getAttributes().put("mode.id", _mode);
		return r;
	}

}
