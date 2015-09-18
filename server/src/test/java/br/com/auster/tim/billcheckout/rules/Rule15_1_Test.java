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

import br.com.auster.billcheckout.model.TaxRate;
import br.com.auster.billcheckout.model.cache.TaxRateCache;
import br.com.auster.common.sql.SQLConnectionManager;
import br.com.auster.common.xml.DOMUtils;



/**
 * @author framos
 * @version $Id$
 *
 */
public class Rule15_1_Test extends BaseRuleTest {

	
	
	private String[] RULES = { "src/main/conf/rules/R15.1-validate-usage-icms.drl" };
	
	
    protected void createGlobals() {
    	super.createGlobals();
    	try {
    		Class.forName("org.apache.commons.dbcp.PoolingDriver");
    		Class.forName("oracle.jdbc.driver.OracleDriver");
    		SQLConnectionManager.init(DOMUtils.openDocument("bgh/guiding/pool.xml", false));

	    	Element conf = DOMUtils.openDocument("bgh/guiding/caches.xml", false);
	    	
    		TaxRateCache taxRateCache =  new TaxRateCache();
	    	taxRateCache.configure(conf);    		
    		workingMemory.setGlobal( "taxRateCache", taxRateCache );
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    		fail();
    	}
    }	
	
	/**
	 * This is the correct file 
	 */
	public void testAccount1() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
			this.assertAccount(this.loadBGHTestFile("bgh/r15_1/ACCOUNT1.BGH"));
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Tax information not found - should not raise errors
	 */
	public void testAccount2() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
			this.assertAccount(this.loadBGHTestFile("bgh/r15_1/ACCOUNT2.BGH"));
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Tax information invalid - ISENTO for all
	 */
	public void testAccount3() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
			this.assertAccount(this.loadBGHTestFile("bgh/r15_1/ACCOUNT3.BGH"));
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Tax information incorrect - should raise errors.
	 * There are 2 calls with ICMS as 12,75% instead of 25%
	 */
	public void testAccount4() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
			this.assertAccount(this.loadBGHTestFile("bgh/r15_1/ACCOUNT4.BGH"));
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(2, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testFindTaxRate() {
    	try {
    		Class.forName("org.apache.commons.dbcp.PoolingDriver");
    		Class.forName("oracle.jdbc.driver.OracleDriver");
    		SQLConnectionManager.init(DOMUtils.openDocument("bgh/guiding/pool.xml", false));

	    	Element conf = DOMUtils.openDocument("bgh/guiding/caches.xml", false);
	    	
    		TaxRateCache taxRateCache =  new TaxRateCache();
	    	taxRateCache.configure(conf);    		

	    	TaxRate 
	    	tr = (TaxRate) taxRateCache.getFromCache(TaxRate.createAlternateKey("MG", "ICMS", null));
	    	assertNotNull(tr);
	    	assertEquals(25, tr.getTaxRate(), 0.01);

	    	tr = (TaxRate) taxRateCache.getFromCache(TaxRate.createAlternateKey("SP", "ICMS", null));
	    	assertNotNull(tr);
	    	assertEquals(25, tr.getTaxRate(), 0.01);
	    	
	    	tr = (TaxRate) taxRateCache.getFromCache(TaxRate.createAlternateKey("PE", "ICMS", null));
	    	assertNotNull(tr);
	    	assertEquals(28, tr.getTaxRate(), 0.01);
	    	
    	} catch (Exception e) {
    		e.printStackTrace();
    		fail();
    	}

	}
}
