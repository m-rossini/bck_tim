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
 * This testcase will use the same files are R01.5 since they share the same validations
 * @author framos
 * @version $Id$
 *
 */
public class Rule01_6_Test extends BaseRuleTest {



	private String[] RULES_1 = { "src/test/resources/bgh/guiding/guiding.drl" };
	private String[] RULES_2 = { "src/main/conf/rules/R01.6-usage-duration-threshold-validation.drl" };

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
			Account acc = this.loadBGHTestFile("bgh/r01_5/ACCOUNT1.BGH");
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


	public void testAccount2() {
		try {
			// firing rules
			this.customized = true;
			this.startupRuleEngine(RULES_1);
			Account acc = this.loadBGHTestFile("bgh/r01_5/ACCOUNT2.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			this.customized = false;
			this.startupRuleEngine(RULES_2);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(1, this.results.size());
			Consequence c = this.results.get(0);
			assertEquals("Uso em HOMING com duração superior ao limite", c.getDescription());
			assertEquals("2006-11-13", c.getAttributes().getAttributeValue1());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}


	/**
	 * Ticket #228. Now, looking for ORO05 call
	 */
	public void testAccount6() {
		try {
			// firing rules
			this.customized = true;
			this.startupRuleEngine(RULES_1);
			Account acc = this.loadBGHTestFile("bgh/r01_5/ACCOUNT6.BGH");
			this.assertAccount(acc);

//			Invoice inv = (Invoice)acc.getInvoiceList().iterator().next();
//			Section details= (Section)inv.getSections().get(1);
//			Section contract = (Section) details.getSubSections().get(0);
//			Section usageGroup = (Section) contract.getSubSections().get(4);
//			System.out.println("Section is " + usageGroup.getSectionName());
//			java.util.Iterator it = usageGroup.getDetails().iterator();
//			while (it.hasNext()) {
//				TIMUsageDetail detail = (TIMUsageDetail) it.next();
//				System.out.println("Usage from " + detail.getDatetime() + " is voice (" + detail.isVoiceUsage() + ") " +
//						           "area=" + detail.getArea() + " svc=" + detail.getSvcId() +
//						           "type=" + detail.getType() + "sect=" + (detail.getSection().equals(usageGroup)));
//			}
			this.workingMemory.fireAllRules();
			this.customized = false;
			this.startupRuleEngine(RULES_2);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(2, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}
