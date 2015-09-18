/*
 * Copyright (c) 2004-2007 Auster Solutions. All Rights Reserved.
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
 * Created on 10/10/2007
 */
package br.com.auster.tim.billcheckout.rules;

import org.w3c.dom.Element;

import br.com.auster.common.sql.SQLConnectionManager;
import br.com.auster.common.xml.DOMUtils;
import br.com.auster.om.invoice.Account;
import br.com.auster.tim.billcheckout.param.ContractPromotionsCache;
import br.com.auster.tim.billcheckout.param.PlansCache;
import br.com.auster.tim.billcheckout.param.RateTimeZoneCache;
import br.com.auster.tim.billcheckout.param.ServicePlanCache;
import br.com.auster.tim.billcheckout.param.ServicesCache;
import br.com.auster.tim.billcheckout.param.TariffZoneCache;
import br.com.auster.tim.billcheckout.param.TariffZoneUsageGroupCache;
import br.com.auster.tim.billcheckout.param.UsageGroupCache;
import br.com.auster.tim.billcheckout.param.UsageGroupLDCache;

/**
 * TODO What this class is responsible for
 *
 * @author William Soares
 * @version $Id$
 * @since JDK1.4
 */
public class Rule19_6_Test extends BaseRuleTest {

	private String[] RULES = { "src/main/conf/rules/R19.6-promotion-outof-validity.drl" };

	private String[] GUIDING = { "src/test/resources/bgh/guiding/guiding.drl" };

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

	    		Class.forName("org.apache.commons.dbcp.PoolingDriver");
	    		Class.forName("oracle.jdbc.driver.OracleDriver");
	    		SQLConnectionManager.init(DOMUtils.openDocument("bgh/guiding/pool.xml", false));

		    	Element conf = DOMUtils.openDocument("bgh/guiding/caches.xml", false);

		    	ContractPromotionsCache contractPromotionsCache =  new ContractPromotionsCache();
		    	contractPromotionsCache.configure(conf);
	    		this.workingMemory.setGlobal( "contractPromotionsCache", contractPromotionsCache);
        	}

    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }


	/**
	 * This is the correct file.
	 * Event Usage in Promotion "DPN" with callDate = "26/06/06".
	 * Promotion activation date = "25/06/06"
	 * Promotion expiration date = "27/06/06"
	 */
	public void testAccount1() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r19_6/ACCOUNT1.BGH");
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
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * This is the file with the event in promotion out of date.
	 * Event Usage in Promotion "DPN" with callDate = "28/06/06".
	 * Promotion activation date = "25/06/06"
	 * Promotion expiration date = "27/06/06"
	 */
	public void testAccount2() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r19_6/ACCOUNT2.BGH");
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
			assertEquals(2, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/**
	 * This file contains a contract number that has no promotion configured.
	 */
	public void testAccount3() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r19_6/ACCOUNT3.BGH");
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
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

}
