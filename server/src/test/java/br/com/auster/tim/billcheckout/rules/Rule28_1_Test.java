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
 * Created on 08/10/2007
 */
package br.com.auster.tim.billcheckout.rules;

import org.w3c.dom.Element;

import br.com.auster.billcheckout.consequence.Consequence;
import br.com.auster.common.sql.SQLConnectionManager;
import br.com.auster.common.xml.DOMUtils;
import br.com.auster.dware.graph.Request;
import br.com.auster.om.invoice.Account;
import br.com.auster.tim.billcheckout.bscs.RateplanHistCache;
import br.com.auster.tim.billcheckout.param.PackagesByPlansCache;


/**
 * TODO What this class is responsible for
 *
 * @author William Soares
 * @version $Id$
 * @since JDK1.4
 */
public class Rule28_1_Test extends BaseRuleTest {

	private String[] RULES = { "src/main/conf/rules/R28.1-package-different-plan-detection.drl" };


    protected void createGlobals() {
    	super.createGlobals();
    	try {
    		Class.forName("org.apache.commons.dbcp.PoolingDriver");
    		Class.forName("oracle.jdbc.driver.OracleDriver");
    		SQLConnectionManager.init(DOMUtils.openDocument("bgh/guiding/pool.xml", false));

	    	Element conf = DOMUtils.openDocument("bgh/guiding/caches.xml", false);

	    	RateplanHistCache rateplanHist =  new RateplanHistCache();
	    	rateplanHist.configure(conf);
    		this.workingMemory.setGlobal( "rateplanHist", rateplanHist );

    		PackagesByPlansCache packagesByPlansCache =  new PackagesByPlansCache();
    		packagesByPlansCache.configure(conf);
    		this.workingMemory.setGlobal( "packagesByPlansCache", packagesByPlansCache );

    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }


	/**
	 * This is the correct file.
	 * Package: "T Você 10 min." registered for the Plan: "Plano T Voc¿"
	 */
	public void testAccount1() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r28_1/ACCOUNT1.BGH");
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
	 * This is the file with the package name different than plan.
	 * Package: "T Você 5 min." not registered for the Plan: "Plano T Voc¿"
	 */
	public void testAccount2() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r28_1/ACCOUNT2.BGH");
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
	 * This is the file with the package name different than plan.
	 * Package: "T Você 5 min." not registered for the Plan: "Plano T Voc¿"
	 */
	public void testAccount3() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r28_1/ACCOUNT3.BGH");
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
	 * Ticket #171
	 */
	public void testAccount4() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r28_1/ACCOUNT4.BGH");
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
	 * Ticket #241
	 *
	 * This was the ticket that originated the need for RATEPLAN_HIST table.
	 */
	public void testAccount5() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r28_1/ACCOUNT5.BGH");
			// firing guiding
			this.startupRuleEngine(RULES);
			this.assertAccount(acc, new Request() {
				public long getWeight() { return 0; }}
			);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(10, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Ticket #257
	 *.
	 */
	public void testAccount6() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r28_1/ACCOUNT6.BGH");
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

	public void testAccount7() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r28_1/ACCOUNT7.BGH");
			// firing guiding
			this.startupRuleEngine(RULES);
			this.assertAccount(acc, new Request() {
				public long getWeight() { return 0; }}
			);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(1, this.results.size());
			Consequence c = (Consequence) this.results.iterator().next();
			assertEquals("Pcte 275 Reais", c.getAttributes().getAttributeValue4());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Ticket #298
	 */
	public void testAccount8() {
		try {
			Account acc = this.loadBGHTestFile("bgh/r28_1/ACCOUNT8.BGH");
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
}