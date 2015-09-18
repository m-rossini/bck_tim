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

import java.text.DecimalFormat;

import org.w3c.dom.Element;

import br.com.auster.billcheckout.caches.CacheableKey;
import br.com.auster.billcheckout.caches.CacheableVO;
import br.com.auster.billcheckout.consequence.telco.TelcoConsequence;
import br.com.auster.common.sql.SQLConnectionManager;
import br.com.auster.common.xml.DOMUtils;
import br.com.auster.om.invoice.Account;
import br.com.auster.tim.billcheckout.crashprogram.CostCenterCache;
import br.com.auster.tim.billcheckout.npack.NPackPackageInfoCache;
import br.com.auster.tim.billcheckout.npack.NPackRateCache;
import br.com.auster.tim.billcheckout.param.PlansCache;
import br.com.auster.tim.billcheckout.tariff.CrashProgramRatesCache;
import br.com.auster.tim.billcheckout.util.XStreamLoader;



/**
 * @author framos
 * @version $Id$
 *
 */
public class Rule01_30_ValidationTest extends BaseRuleTest {


	private String[] RULES = { "src/main/conf/rules/R01.30-npack-validation.drl",
							   //"src/test/resources/bgh/r01_30/debug.drl" 
							 };

	
    protected void createGlobals() {
        super.createGlobals();
        try {
	        Class.forName("org.apache.commons.dbcp.PoolingDriver");
			Class.forName("oracle.jdbc.driver.OracleDriver");
			SQLConnectionManager.init(DOMUtils.openDocument("bgh/guiding/pool.xml", false));
	
	    	Element lazyAlternateCfg = DOMUtils.openDocument("bgh/guiding/caches.xml", false);
	    	Element lazyNaturalCfg = DOMUtils.openDocument("bgh/guiding/caches2.xml", false);
	        
	        PlansCache plansCache = new PlansCache();
	    	plansCache.configure(lazyAlternateCfg);
	    	this.workingMemory.setGlobal("plansCache", plansCache);
	    	
	    	NPackPackageInfoCache npackInfoCache = new NPackPackageInfoCache();
	    	npackInfoCache.configure(lazyNaturalCfg);
	    	this.workingMemory.setGlobal("npackInfoCache", npackInfoCache);
	    	
	    	NPackRateCache npackRateCache = new NPackRateCache(); 
	    	npackRateCache.configure(lazyNaturalCfg);
	    	this.workingMemory.setGlobal("npackRateCache", npackRateCache);
	    	
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }	
	
    
	/**
	 * This customer is not configured for NPack and its invoice has no charges related to such package
	 */
	public void testAccount1() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r01_30/ACCOUNT1.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = { };
			int[] AGENDA_COUNT = { };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * This customer is not configured for NPack but its invoice has charges related to such package
	 */
	public void testAccount2() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r01_30/ACCOUNT2.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = { };
			int[] AGENDA_COUNT = { };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/**
	 * This customer is configured for NPack and its invoice has charges related to such package.
	 * 
	 * All chargers are there, and their values are OK.
	 */
	public void testAccount3() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r01_30/ACCOUNT3.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = { "Regra 01.30 - check if account is eligible",
					               "Regra 01.30 - accumulate usage by contract",
					               "Regra 01.30 - build occ description by contract",
					               //"Regra 01.30 - check if contract does not have occ",
					               "Regra 01.30 - check if contract's occ is correct"
					               };
			int[] AGENDA_COUNT = { 1, 236, 1, 2 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * This customer is configured for NPack and its invoice has charges related to such package.
	 * 
	 * All chargers are there, but one value is incorrect.
	 */
	public void testAccount4() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r01_30/ACCOUNT4.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = { "Regra 01.30 - check if account is eligible",
		               "Regra 01.30 - accumulate usage by contract",
		               "Regra 01.30 - build occ description by contract",
		               //"Regra 01.30 - check if contract does not have occ",
		               "Regra 01.30 - check if contract's occ is correct"
		               };
			int[] AGENDA_COUNT = { 1, 236, 1, 2 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			// running over results list to check if consequences where ok
			assertEquals(1, this.results.size());
			
			DecimalFormat df = new DecimalFormat("#0.0000");
			
			TelcoConsequence c = (TelcoConsequence) this.results.get(0);
			assertEquals("Valor proporcional cobrado para este NPack não corresponde ao calculado.", c.getDescription());
			assertEquals("55348161", c.getAttributes().getAttributeValue1());
			assertEquals(21.18, df.parse(c.getAttributes().getAttributeValue7()).doubleValue(), 0.01);
			assertEquals(20.18, df.parse(c.getAttributes().getAttributeValue8()).doubleValue(), 0.01);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}	

	/**
	 * This customer is configured for NPack and its invoice has charges related to such package.
	 * 
	 * There is one charge that was removed
	 */
	public void testAccount5() {
		try {
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r01_30/ACCOUNT5.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			String[] RULE_LIST = { "Regra 01.30 - check if account is eligible",
		               "Regra 01.30 - accumulate usage by contract",
		               "Regra 01.30 - build occ description by contract",
		               "Regra 01.30 - check if contract does not have occ",
		               "Regra 01.30 - check if contract's occ is correct"
		               };
			int[] AGENDA_COUNT = { 1, 236, 1, 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			// running over results list to check if consequences where ok
			assertEquals(1, this.results.size());
			
			TelcoConsequence c = (TelcoConsequence) this.results.get(0);
			assertEquals("NPack sem cobrança proporcional de minutos.", c.getDescription());
			assertEquals("55348327", c.getAttributes().getAttributeValue1());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}	
}
