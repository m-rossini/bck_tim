/*
 * Copyright (c) 2004-2008 Auster Solutions. All Rights Reserved.
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
 * Created on 14/04/2008
 */
package br.com.auster.tim.billcheckout.rules;

import org.w3c.dom.Element;

import br.com.auster.billcheckout.consequence.Consequence;
import br.com.auster.common.sql.SQLConnectionManager;
import br.com.auster.common.xml.DOMUtils;
import br.com.auster.om.invoice.Account;
import br.com.auster.tim.billcheckout.param.PackageCache;
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
 * @author Gustavo Portugal
 * @version $Id$
 * @since JDK1.4
 */
public class Rule31_1_Test extends BaseRuleTest {

	private String[] RULES_1 = { "src/test/resources/bgh/guiding/guiding.drl" };
	private String[] RULES_2 = { "src/main/conf/rules/R31.1-infinity.drl" };

	protected boolean customized = false;

    protected void createGlobals() {
    	super.createGlobals();
    	if (customized) {
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
    	} else {
	    	try {
	    		Class.forName("org.apache.commons.dbcp.PoolingDriver");
	    		Class.forName("oracle.jdbc.driver.OracleDriver");
	    		SQLConnectionManager.init(DOMUtils.openDocument("bgh/guiding/pool.xml", false));
	
		    	Element arg0 = DOMUtils.openDocument("bgh/guiding/caches.xml", false);
	
		    	TariffZoneCache tZoneCache = new TariffZoneCache();
		    	tZoneCache.configure(arg0);
		    	this.workingMemory.setGlobal("tariffZoneCache", tZoneCache);
		    	
	    		PackageCache packageCache =  new PackageCache();
	    		packageCache.configure(arg0);
	    		this.workingMemory.setGlobal( "packageCache", packageCache );
	
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    	}
    	}
    }
    
	/**
	 * Correct Infinity Invoice
	 *
	 * 
	 */
	public void testAccount0() {
		try {
			// firing rules
			this.customized = true;
			this.startupRuleEngine(RULES_1);
			Account acc = this.loadBGHTestFile("bgh/r31_1/ACCOUNT0.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			this.customized = false;
			this.startupRuleEngine(RULES_2);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();

			System.out.println(results);
			
			assertEquals(0, this.results.size());
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Invalid Infinity Call - Long Distance
	 *
	 * 
	 */
	public void testAccount1() {
		try {
			// firing rules
			this.customized = true;
			this.startupRuleEngine(RULES_1);
			Account acc = this.loadBGHTestFile("bgh/r31_1/ACCOUNT1.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			this.customized = false;
			this.startupRuleEngine(RULES_2);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();

			System.out.println(results);
			
			assertEquals(2, this.results.size());
			Consequence c = this.results.get(0);
			assertEquals("Chamada off-net obtendo benefício INFINITY.", c.getDescription());
			
			c = this.results.get(1);
			assertEquals("Chamada off-net obtendo benefício INFINITY.", c.getDescription());
	
						
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/**
	 * Invalid Infinity Call - Off-net
	 *
	 * 
	 */
	public void testAccount2() {
		try {
			// firing rules
			this.customized = true;
			this.startupRuleEngine(RULES_1);
			Account acc = this.loadBGHTestFile("bgh/r31_1/ACCOUNT2.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			this.customized = false;
			this.startupRuleEngine(RULES_2);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();

			System.out.println(results);
			
			Consequence c = this.results.get(0);
			assertEquals(1, this.results.size());
			assertEquals("Chamada off-net obtendo benefício INFINITY.", c.getDescription());		
			

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/**
	 * Infinity Call not included in the freeUnits
	 *
	 * 
	 */
	public void testAccount3() {
		try {
			// firing rules
			this.customized = true;
			this.startupRuleEngine(RULES_1);
			Account acc = this.loadBGHTestFile("bgh/r31_1/ACCOUNT3.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			this.customized = false;
			this.startupRuleEngine(RULES_2);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();

			System.out.println(results);
			
			Consequence c = this.results.get(0);
			assertEquals(1, this.results.size());
			assertEquals("Benefício INFINITY de franquia atribuído a menor.", c.getDescription());		
			

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	// Ticket #324
	public void testAccount4() {
		try {
			// firing rules
			this.customized = true;
			this.startupRuleEngine(RULES_1);
			Account acc = this.loadBGHTestFile("bgh/r31_1/ACCOUNT4.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			this.customized = false;
			this.startupRuleEngine(RULES_2);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();

			System.out.println(results);
			
		//	Consequence c = this.results.get(0);
		//	assertEquals(1, this.results.size());
		//	assertEquals("Benefício INFINITY de franquia atribuído a menor.", c.getDescription());		
			

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	// Ticket #324
	public void testAccount5() {
		try {
			// firing rules
			this.customized = true;
			this.startupRuleEngine(RULES_1);
			Account acc = this.loadBGHTestFile("bgh/r31_1/ACCOUNT5.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			this.customized = false;
			this.startupRuleEngine(RULES_2);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();

			System.out.println(results);
			
			Consequence c = this.results.get(0);
			assertEquals(1, this.results.size());
			assertEquals("Benefício INFINITY de franquia atribuído a menor.", c.getDescription());		
			

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/**
	 * Invalid Infinity Call - Long Distance
	 *
	 * 
	 */
	public void testAccount6() {
		try {
			// firing rules
			this.customized = true;
			this.startupRuleEngine(RULES_1);
			Account acc = this.loadBGHTestFile("bgh/r31_1/ACCOUNT6.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			this.customized = false;
			this.startupRuleEngine(RULES_2);
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();

			System.out.println(results);
			
			assertEquals(2, this.results.size());
			Consequence c = this.results.get(0);
			assertEquals("Chamada off-net obtendo benefício INFINITY.", c.getDescription());
			
			c = this.results.get(1);
			assertEquals("Chamada off-net obtendo benefício INFINITY.", c.getDescription());
	
						
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

}
