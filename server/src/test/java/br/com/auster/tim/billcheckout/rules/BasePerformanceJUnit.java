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

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;

import org.drools.event.ActivationCancelledEvent;
import org.drools.event.ActivationCreatedEvent;
import org.drools.event.AfterActivationFiredEvent;
import org.drools.event.AgendaEventListener;
import org.drools.event.BeforeActivationFiredEvent;
import org.drools.event.ObjectAssertedEvent;
import org.drools.event.ObjectModifiedEvent;
import org.drools.event.ObjectRetractedEvent;
import org.drools.event.WorkingMemoryEventListener;
import org.w3c.dom.Element;

import br.com.auster.billcheckout.model.cache.TaxRateCache;
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
 * This JUnit test is meant to be run only on development environment.
 *
 * IMPORANT: Do not change the class name to run it on release time due to the
 *    amount of time it takes to finish.
 *
 * @author framos
 * @version $Id$
 *
 */
public abstract class BasePerformanceJUnit extends BaseRuleTest {


	protected static final String BASE_RULE_DIR = "src/main/conf/rules/";

	protected static final String[] RULES_1 = { "src/test/resources/bgh/guiding/guiding.drl" };

	// ---- Fase 1 ----
	protected static final String[] RULES_R01_4 = { BASE_RULE_DIR + "R1.4-below-zero-usage-validation.drl" };
	protected static final String[] RULES_R01_5_AND_6 =
	                                              { BASE_RULE_DIR + "R01.5-usage-threshold-validation.drl",
													BASE_RULE_DIR + "R01.6-usage-duration-threshold-validation.drl" };
	protected static final String[] RULES_R01_7 = { BASE_RULE_DIR + "R1.7-usage-charge-info-validation.drl" };
	protected static final String[] RULES_R01_8 = { BASE_RULE_DIR + "R01.8-expired-usage-validation.drl" };
	protected static final String[] RULES_R01_9 = { BASE_RULE_DIR + "R01.9-duplicated-usage-validation.drl" };
	protected static final String[] RULES_R12_1 = { BASE_RULE_DIR + "R12.1-sections-totals-validation.drl"};
	// not tested separately
	protected static final String[] RULES_R10_x = { BASE_RULE_DIR + "RX.1-date-format-validation.drl" };
	protected static final String[] RULES_R12_2 = { BASE_RULE_DIR + "R12.2-notafiscal-validation.drl" };
	protected static final String[] RULES_R14_1 = { BASE_RULE_DIR + "R14.1-barcode-validation.drl" };
	protected static final String[] RULES_R14_3 = { BASE_RULE_DIR + "R14.3-paymentdata-validation.drl" };
	protected static final String[] RULES_R14_5 = { BASE_RULE_DIR + "R14.5-check-carrier-in-paystub.drl" };
	protected static final String[] RULES_R17_2 = { BASE_RULE_DIR + "R17.2-invoice-total-validation.drl" };

	// ---- Fase 2 ----
	protected static final String[] RULES_R01_12 = { BASE_RULE_DIR + "R01.12-resolucao-316.drl" };
	protected static final String[] RULES_R01_18 = { BASE_RULE_DIR + "R01.18-sliced-calls-detection.drl" };
	protected static final String[] RULES_R02_3  = { BASE_RULE_DIR + "R02.3-service-doesnot-exist.drl" };
	protected static final String[] RULES_R04_5  = { BASE_RULE_DIR + "R04-5-duplicated-usage-latecalls.drl" };
	protected static final String[] RULES_R04_6  = { BASE_RULE_DIR + "R04.6-plan-doesnot-contain-service.drl" };
	protected static final String[] RULES_R15_1  = { BASE_RULE_DIR + "R15.1-validate-usage-icms.drl" };
	// not tested separately
	protected static final String[] RULES_R05_x = { BASE_RULE_DIR + "R05.2_3_4-validate-carrier-info.drl" };
	protected static final String[] RULES_R06_1 = { BASE_RULE_DIR + "R06.1-validate-nf-number.drl" } ;
	protected static final String[] RULES_R09_3 = { BASE_RULE_DIR + "R09.3-custcode-validation.drl" };
	protected static final String[] RULES_R09_4 = { BASE_RULE_DIR + "R09.4-directdebit-validation.drl" };
	protected static final String[] RULES_R15_2 = { BASE_RULE_DIR + "R15.2-validate-service-taxes.drl" };
	protected static final String[] RULES_R16_1 = { BASE_RULE_DIR + "R16.1-hashcode-validation.drl" };
	protected static final String[] RULES_R27_1 = { BASE_RULE_DIR + "R27.1-OCCs-thresholds.drl" } ;
	protected static final String[] RULES_R27_4 = { BASE_RULE_DIR + "R27.4-duplicated-OCCs.drl" } ;
	protected static final String[] RULES_R28_8 = { BASE_RULE_DIR + "R28.8-corporate-diff-duedates.drl" };

	// ---- Fase 3 ----
	protected static final String[] RULES_R01_10 = { BASE_RULE_DIR + "R01.10-incorrect-usagegroup-for-call.drl" };
	protected static final String[] RULES_R01_11 = { BASE_RULE_DIR + "R01.11-free-calls-without-benefit.drl" };
	protected static final String[] RULES_R03_2  = { BASE_RULE_DIR + "R03.2-benefit-without-promotion-charges.drl" };
	protected static final String[] RULES_R04_4  = { BASE_RULE_DIR + "R04.4-validate-calls-in-mc.drl" };
	protected static final String[] RULES_R15_3  = { BASE_RULE_DIR + "R15.3-icms-verification.drl" };
	protected static final String[] RULES_R19_1  = { BASE_RULE_DIR + "R19.1-promotions-values-validation.drl" };
	protected static final String[] RULES_R19_3  = { BASE_RULE_DIR + "R19.3-promotions-limits-validation.drl" };
	protected static final String[] RULES_R19_6  = { BASE_RULE_DIR + "R19.6-promotion-outof-validity.drl" };
	// not tested separately
	protected static final String[] RULES_R04_3  = { BASE_RULE_DIR + "R04.3-credcorp-validation.drl" };
	protected static final String[] RULES_R06_2  = { BASE_RULE_DIR + "R06.2-validate-nf-series.drl" };
	protected static final String[] RULES_R25_1  = { BASE_RULE_DIR + "R25.1-validate-service-activation-debit.drl" };
	protected static final String[] RULES_R28_1  = { BASE_RULE_DIR + "R28.1-package-different-plan-detection.drl" };
	protected static final String[] RULES_R28_2  = { BASE_RULE_DIR + "R28.2-plan-without-mandatory-package.drl" };


	protected static final String[] RULES_ALL = new String[14];

	protected boolean customized = false;
	protected boolean needTaxRate = false;

	protected static final HashMap times = new HashMap();


	static {
		// Fase 1
		int col = 0;
		RULES_ALL[col++] = RULES_R01_4[0];
		RULES_ALL[col++] = RULES_R01_5_AND_6[0];
		RULES_ALL[col++] = RULES_R01_5_AND_6[1];
		RULES_ALL[col++] = RULES_R01_7[0];
		RULES_ALL[col++] = RULES_R01_8[0];
		RULES_ALL[col++] = RULES_R01_9[0];
		RULES_ALL[col++] = RULES_R12_1[0];

		RULES_ALL[col++] = RULES_R10_x[0];
		//RULES_ALL[col++] = RULES_R12_2[0];
		//RULES_ALL[col++] = RULES_R14_1[0];
		//RULES_ALL[col++] = RULES_R14_3[0];
		//RULES_ALL[col++] = RULES_R14_5[0];
		//RULES_ALL[col++] = RULES_R17_2[0];
		// Fase 2
		RULES_ALL[col++] = RULES_R01_12[0];
		RULES_ALL[col++] = RULES_R01_18[0];
		RULES_ALL[col++] = RULES_R02_3[0];
		RULES_ALL[col++] = RULES_R04_5[0];
		RULES_ALL[col++] = RULES_R04_6[0];
		RULES_ALL[col++] = RULES_R15_1[0];

		//RULES_ALL[col++] = RULES_R05_x[0];
		//RULES_ALL[col++] = RULES_R06_1[0];
		//RULES_ALL[col++] = RULES_R09_3[0];
		//RULES_ALL[col++] = RULES_R09_4[0];
		//RULES_ALL[col++] = RULES_R15_2[0];
		//RULES_ALL[col++] = RULES_R16_1[0];
		//RULES_ALL[col++] = RULES_R27_1[0];
		//RULES_ALL[col++] = RULES_R27_4[0];
		//RULES_ALL[col++] = RULES_R28_8[0];
	}


    protected void createGlobals() {
    	super.createGlobals();
    	try {
        	if (!customized) {

        		if (needTaxRate) {
        			System.out.println("Setting taxRateCache global!!!");
		    		Class.forName("org.apache.commons.dbcp.PoolingDriver");
		    		Class.forName("oracle.jdbc.driver.OracleDriver");
		    		SQLConnectionManager.init(DOMUtils.openDocument("bgh/guiding/pool.xml", false));

			    	Element conf = DOMUtils.openDocument("bgh/guiding/caches.xml", false);

		    		TaxRateCache taxRateCache =  new TaxRateCache();
			    	taxRateCache.configure(conf);
		    		workingMemory.setGlobal( "taxRateCache", taxRateCache );
        		}
        	} else {

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
        	}

    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

	protected void tearDown() throws Exception {
		super.tearDown();
		System.out.println(this.times);
	}

	protected void runTest(String[] _rules, String _bghFile) {
		runTest(_rules, _bghFile, null);
	}

	protected void runTest(String[] rules, String _bghFile, String _dumpFile) {
		try {
			// firing rules
			long init1 = System.currentTimeMillis();
			long mem1 = Runtime.getRuntime().totalMemory();
			this.customized = true;
			this.startupRuleEngine(RULES_1);
			if (_dumpFile != null) {
				EventDumper dumper = new EventDumper(new FileOutputStream(_dumpFile+".guiding"));
				this.workingMemory.addEventListener((WorkingMemoryEventListener) dumper);
				this.workingMemory.addEventListener((AgendaEventListener) dumper);
			}
			Account acc = this.loadBGHTestFile(_bghFile);
			long init2 = System.currentTimeMillis();
			long mem2 = Runtime.getRuntime().totalMemory();
			System.out.println("Loading time: " + (init2-init1) + "ms ; used memory = " + (mem2-mem1)/1024 + "KB");

			this.assertAccount(acc);
			long init3 = System.currentTimeMillis();
			long mem3 = Runtime.getRuntime().totalMemory();
			System.out.println("Assert guiding: " + (init3-init2) + "ms ; used memory = " + (mem3-mem2)/1024 + "KB");

			this.workingMemory.fireAllRules();
			long init4 = System.currentTimeMillis();
			long mem4 = Runtime.getRuntime().totalMemory();
			System.out.println("Fire guiding: " + (init4-init3) + "ms ; used memory = " + (mem4-mem3)/1024 + "KB");

			this.customized = false;
			this.startupRuleEngine(rules);
			// dumping rete & adding listeners
			if (_dumpFile != null) {
				EventDumper dumper = new EventDumper(new FileOutputStream(_dumpFile));
				this.workingMemory.addEventListener((WorkingMemoryEventListener) dumper);
				this.workingMemory.addEventListener((AgendaEventListener) dumper);
			}
			this.assertAccount(acc);
			long init5 = System.currentTimeMillis();
			long mem5 = Runtime.getRuntime().totalMemory();
			System.out.println("Assert rules: " + (init5-init4) + "ms ; used memory = " + (mem5-mem4)/1024 + "KB");

			this.workingMemory.fireAllRules();
			long init6 = System.currentTimeMillis();
			long mem6 = Runtime.getRuntime().totalMemory();
			System.out.println("Fire rules: " + (init6-init5) + "ms ; used memory = " + (mem6-mem5)/1024 + "KB");

			System.out.println("Total time: " + (init6-init1) + "ms");
			System.out.println("Total mem: " + (mem6-mem1)/1024 + "KB");

			// helping gc to remove old references
			acc = null;
			this.ruleBase = null;
			this.workingMemory = null;
			System.gc();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

}



class EventDumper implements WorkingMemoryEventListener, AgendaEventListener {


	private PrintStream out;

	public EventDumper() {
		this(System.out);
	}
	public EventDumper(OutputStream _out) {
		this.out = new PrintStream(_out);
	}

	public void objectAsserted(ObjectAssertedEvent _event) {
		this.out.println(_event);
	}

	public void objectModified(ObjectModifiedEvent _event) {
		this.out.println(_event);
	}

	public void objectRetracted(ObjectRetractedEvent _event) {
		this.out.println(_event);
	}

	public void activationCancelled(ActivationCancelledEvent _event) {
		this.out.println(_event);
	}

	public void activationCreated(ActivationCreatedEvent _event) {
		this.out.println(_event);
	}

	public void afterActivationFired(AfterActivationFiredEvent _event) {
		this.out.println(_event);
	}

	public void beforeActivationFired(BeforeActivationFiredEvent _event) {
		this.out.println(_event);
	}
}
