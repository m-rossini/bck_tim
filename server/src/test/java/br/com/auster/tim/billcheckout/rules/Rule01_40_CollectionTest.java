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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.xml.DOMConfigurator;

import br.com.auster.billcheckout.caches.CacheableKey;
import br.com.auster.billcheckout.caches.CacheableVO;
import br.com.auster.common.lang.NamedHashMap;
import br.com.auster.dware.request.file.FileRequest;
import br.com.auster.om.filter.request.BillcheckoutRequestWrapper;
import br.com.auster.om.invoice.Account;
import br.com.auster.om.invoice.rules.InvoiceAssertionWorker;
import br.com.auster.om.util.UnitCounter;
import br.com.auster.tim.billcheckout.crashprogram.CrashProgramCustCodesCache;
import br.com.auster.tim.billcheckout.crashprogram.CrashProgramCustCodesVO;
import br.com.auster.tim.billcheckout.crashprogram.CrashProgramPackageInfoCache;
import br.com.auster.tim.billcheckout.crashprogram.CrashProgramPackageInfoVO;
import br.com.auster.tim.billcheckout.param.PlansCache;
import br.com.auster.tim.billcheckout.param.PlansVO;
import br.com.auster.tim.billcheckout.util.FreeUnitAccumulator;



/**
 * @author framos
 * @version $Id$
 *
 */
public class Rule01_40_CollectionTest extends BaseRuleTest {


	private String[] RULES = { "src/main/conf/rules/credcorp/R01.40-crash-program-collection.drl"};

	protected List<NamedHashMap> results;

    protected void createGlobals() {
        results = new ArrayList<NamedHashMap>();
        workingMemory.setGlobal( "results", results );

        // setting plan cache
        PlansCache plansCache = new PlansCache() {
        	@Override
        	public CacheableVO getFromCache(CacheableKey key) {
        		return new PlansVO(1);
        	}
        };
        workingMemory.setGlobal("planCache", plansCache);
        
        // setting crash custCode cache
        CrashProgramCustCodesCache crashCustCodesCache = new CrashProgramCustCodesCache() {
        	@Override
        	public CacheableVO getFromCache(CacheableKey key) {
        		CrashProgramCustCodesVO vo = new CrashProgramCustCodesVO();
        		vo.setCustCode("6.310348");
        		return vo;
        	}
        };
        workingMemory.setGlobal("crashCustCodesCache", crashCustCodesCache);
        
        // setting crash packageInfo cache
        CrashProgramPackageInfoCache crashPackageInfoCache = new CrashProgramPackageInfoCache() {
        	@Override
        	public CacheableVO getFromCache(CacheableKey key) {
        		CrashProgramPackageInfoVO vo = new CrashProgramPackageInfoVO();
        		vo.getCsgCodeList().add("10");
        		return vo;
        	}
        };
        workingMemory.setGlobal("crashPackageInfoCache", crashPackageInfoCache);
    }

	/**
	 * This account has only one FU AccountID in use
	 */
	public void testAccount1() {
		try {
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r01_40/ACCOUNT1.BGH");
			this.assertAccount(acc);
			this.workingMemory.fireAllRules();
			
			// checking assertion and match expectations
			String[] RULE_LIST = { "Regra 01.40 - build named hash map if account is eligible",
					               "Regra 01.40 - summarize fu for eligible account",
					               "Regra 01.40 - persiste sum built in the previous rule"};
			int[] AGENDA_COUNT = { 1, 2, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);
			
			assertEquals(1, this.results.size());
			NamedHashMap nhm = this.results.get(0);
			assertEquals("R01.40-collection-6.310348", nhm.getName());
			assertEquals(2, nhm.size());			
			Iterator it = nhm.keySet().iterator();
			for (String key = (String) it.next(); it.hasNext(); key = (String)it.next() ) {
				if ("10".equals(key)) {
					UnitCounter uc = (UnitCounter)nhm.get(key);
					assertEquals(5000, uc.getMinutes(), 0.01);
				} else if ("10.tmcodes".equals(key)) {
					assertTrue( nhm.get(key) instanceof List );
					List ll = (List)nhm.get(key);
					assertTrue(ll.contains("1"));
				} else {
					fail();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

}
