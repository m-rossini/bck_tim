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
 * Created on 09/10/2007
 */
package br.com.auster.tim.billcheckout.rules;

import org.w3c.dom.Element;

import br.com.auster.common.sql.SQLConnectionManager;
import br.com.auster.common.xml.DOMUtils;
import br.com.auster.dware.graph.Request;
import br.com.auster.om.invoice.Account;
import br.com.auster.tim.billcheckout.param.PackageCache;
import br.com.auster.tim.billcheckout.param.PlansCache;

/**
 * TODO What this class is responsible for
 *
 * @author William Soares
 * @version $Id$
 * @since JDK1.4
 */
public class Rule28_2_Test extends BaseRuleTest {

	private String[] RULES = { "src/main/conf/rules/R28.2-plan-without-mandatory-package.drl" };

    protected void createGlobals() {
    	super.createGlobals();
    	try {
    		Class.forName("org.apache.commons.dbcp.PoolingDriver");
    		Class.forName("oracle.jdbc.driver.OracleDriver");
    		SQLConnectionManager.init(DOMUtils.openDocument("bgh/guiding/pool.xml", false));

	    	Element conf = DOMUtils.openDocument("bgh/guiding/caches.xml", false);

	    	PlansCache planCache =  new PlansCache();
	    	planCache.configure(conf);
    		this.workingMemory.setGlobal( "planCache", planCache );

    		PackageCache packageCache =  new PackageCache();
    		packageCache.configure(conf);
    		this.workingMemory.setGlobal( "packageCache", packageCache );

    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }


	/**
	 * This is the correct file, there is a package.
	 * The package IS MANDATORY.
	 */
	public void testAccount1() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r28_2/ACCOUNT1.BGH");
			this.startupRuleEngine(RULES);
			this.assertAccount(acc, new Request() {
				public long getWeight() { return 0; }}
			);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * This is wrong file, there is no package
	 * The package IS MANDATORY.
	 */
	public void testAccount2() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r28_2/ACCOUNT2.BGH");
			// firing guiding
			this.startupRuleEngine(RULES);
			this.assertAccount(acc, new Request() {
				public long getWeight() { return 0; }}
			);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(1, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * This is the correct file, there is a package.
	 * The package IS NOT MANDATORY.
	 */
	public void testAccount3() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r28_2/ACCOUNT3.BGH");
			// firing guiding
			this.startupRuleEngine(RULES);
			this.assertAccount(acc, new Request() {
				public long getWeight() { return 0; }}
			);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * This is wrong file, there is no package
	 * The package IS NOT MANDATORY.
	 */
	public void testAccount4() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r28_2/ACCOUNT4.BGH");
			// firing guiding
			this.startupRuleEngine(RULES);
			this.assertAccount(acc, new Request() {
				public long getWeight() { return 0; }}
			);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * This file contains a plan that does not exist in the table QLF_PLANS.
	 */
	public void testAccount5() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r28_2/ACCOUNT5.BGH");
			this.startupRuleEngine(RULES);
			this.assertAccount(acc, new Request() {
				public long getWeight() { return 0; }}
			);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Test case regarding ticket #259 
	 * There is only a shared package and the plan does not require a package.
	 */
	public void testAccount6() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r28_2/ACCOUNT6.BGH");
			this.startupRuleEngine(RULES);
			this.assertAccount(acc, new Request() {
				public long getWeight() { return 0; }}
			);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/**
	 * Test case regarding ticket #259 (shared package)
	 * There is only a shared package and the plan requires a package.
	 */
	public void testAccount7() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r28_2/ACCOUNT7.BGH");
			this.startupRuleEngine(RULES);
			this.assertAccount(acc, new Request() {
				public long getWeight() { return 0; }}
			);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/**
	 * Test case regarding ticket #259 (shared package)
	 * The plan requires a package and there are two packages (shared and another one)
	 */
	public void testAccount8() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r28_2/ACCOUNT8.BGH");
			this.startupRuleEngine(RULES);
			this.assertAccount(acc, new Request() {
				public long getWeight() { return 0; }}
			);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
}
