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
import br.com.auster.tim.billcheckout.param.PlansCache;
import br.com.auster.tim.billcheckout.tariff.CrashProgramRatesCache;
import br.com.auster.tim.billcheckout.util.XStreamLoader;



/**
 * @author framos
 * @version $Id$
 *
 */
public class Rule01_40_ValidationTest extends BaseRuleTest {


	private String[] RULES = { "src/main/conf/rules/R01.40-crash-program-validation.drl"};

	
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
	    	
	    	CostCenterCache costCenterCache = new CostCenterCache();
	    	costCenterCache.configure(lazyNaturalCfg);
	    	this.workingMemory.setGlobal("costCenterCache", costCenterCache);

	    	CrashProgramRatesCache cashProgramRatesCache = new CrashProgramRatesCache();
	    	cashProgramRatesCache.configure(lazyNaturalCfg);
	    	this.workingMemory.setGlobal("cashProgramRatesCache", cashProgramRatesCache);

        } catch (Exception e) {
        	e.printStackTrace();
        }
    }	
	
    
	/**
	 * All OK
	 */
	public void testAccount21A() {
		try {
			System.setProperty("cglib.disabled", "true");
			System.setProperty(XStreamLoader.ROOT_DIR_SYSPROP, "src/test/resources/bgh/r01_40/credcorp");
			System.setProperty(XStreamLoader.FILENAME_SYSPROP, "credcorp.xml.gz");
			// firing rules
			this.startupRuleEngine(RULES);
			Account acc = this.loadBGHTestFile("bgh/r01_40/ACCOUNT21A.BGH");
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
	 * Manipulated the file to add an incorrect value for "assinatura"
	 */
	public void testAccount21A_1() {
		try {
			System.setProperty("cglib.disabled", "true");
			System.setProperty(XStreamLoader.ROOT_DIR_SYSPROP, "src/test/resources/bgh/r01_40/credcorp");
			System.setProperty(XStreamLoader.FILENAME_SYSPROP, "credcorp.xml.gz");
			// firing rules
			this.startupRuleEngine(RULES);
			Account acc = this.loadBGHTestFile("bgh/r01_40/ACCOUNT21A.1.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(1, this.results.size());
			TelcoConsequence c = (TelcoConsequence) this.results.get(0);
			assertEquals("Desconto aplicado à assinatura diverge do calculado.", c.getDescription());

			DecimalFormat df = new DecimalFormat("#0.0000");
			
			assertEquals(0.004, df.parse(c.getAttributes().getAttributeValue5()).doubleValue(), 0.01);
			assertEquals(18.2, df.parse(c.getAttributes().getAttributeValue7()).doubleValue(), 0.01);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * All OK
	 */
	public void testAccount22A() {
		try {
			System.setProperty("cglib.disabled", "true");
			System.setProperty(XStreamLoader.ROOT_DIR_SYSPROP, "src/test/resources/bgh/r01_40/credcorp");
			System.setProperty(XStreamLoader.FILENAME_SYSPROP, "credcorp.xml.gz");
			// firing rules
			this.startupRuleEngine(RULES);
			Account acc = this.loadBGHTestFile("bgh/r01_40/ACCOUNT22A.BGH");
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
	 * Manipulated the file to add an incorrect value for "assinatura"
	 */
	public void testAccount22A_1() {
		try {
			System.setProperty("cglib.disabled", "true");
			System.setProperty(XStreamLoader.ROOT_DIR_SYSPROP, "src/test/resources/bgh/r01_40/credcorp");
			System.setProperty(XStreamLoader.FILENAME_SYSPROP, "credcorp.xml.gz");
			// firing rules
			this.startupRuleEngine(RULES);
			Account acc = this.loadBGHTestFile("bgh/r01_40/ACCOUNT22A.1.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(1, this.results.size());
			TelcoConsequence c = (TelcoConsequence) this.results.get(0);
			assertEquals("Desconto aplicado à assinatura diverge do calculado.", c.getDescription());

			DecimalFormat df = new DecimalFormat("#0.0000");
			
			assertEquals(0.004, df.parse(c.getAttributes().getAttributeValue5()).doubleValue(), 0.01);
			assertEquals(18.2, df.parse(c.getAttributes().getAttributeValue7()).doubleValue(), 0.01);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * All OK, but we got a pro-rata 
	 */
	public void testAccount22A_2() {
		try {
			System.setProperty("cglib.disabled", "true");
			System.setProperty(XStreamLoader.ROOT_DIR_SYSPROP, "src/test/resources/bgh/r01_40/credcorp");
			System.setProperty(XStreamLoader.FILENAME_SYSPROP, "credcorp.xml.gz");
			// firing rules
			this.startupRuleEngine(RULES);
			Account acc = this.loadBGHTestFile("bgh/r01_40/ACCOUNT22A.2.BGH");
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
	 * Here we will simulate that there is no rates for the rateplan
	 *
	 */
	public void testAccount23A() {
		try {
			
			System.setProperty("cglib.disabled", "true");
			System.setProperty(XStreamLoader.ROOT_DIR_SYSPROP, "src/test/resources/bgh/r01_40/credcorp");
			System.setProperty(XStreamLoader.FILENAME_SYSPROP, "credcorp.xml.gz");
			// firing rules
			this.startupRuleEngine(RULES);

			//
			// to overwrite the global, and simulate that the rateplan has no rates
			//
			CrashProgramRatesCache cashProgramRatesCache = new CrashProgramRatesCache() {
				
				@Override
				public CacheableVO getFromCache(CacheableKey key) {
					return null;
				}
			};
	    	cashProgramRatesCache.configure(DOMUtils.openDocument("bgh/guiding/caches2.xml", false));
	    	this.workingMemory.setGlobal("cashProgramRatesCache", cashProgramRatesCache);
			
			Account acc = this.loadBGHTestFile("bgh/r01_40/ACCOUNT23A.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(1, this.results.size());
			TelcoConsequence c = (TelcoConsequence) this.results.get(0);
			assertEquals("Não existe configuração de desvio para este plano.", c.getDescription());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Here we will simulate a rateplan that is not part of the CSGCode
	 *
	 */
	public void testAccount23A_3() {
		try {
			
			System.setProperty("cglib.disabled", "true");
			System.setProperty(XStreamLoader.ROOT_DIR_SYSPROP, "src/test/resources/bgh/r01_40/credcorp");
			System.setProperty(XStreamLoader.FILENAME_SYSPROP, "credcorp.xml.gz");
			// firing rules
			this.startupRuleEngine(RULES);
			Account acc = this.loadBGHTestFile("bgh/r01_40/ACCOUNT23A.BGH");
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
