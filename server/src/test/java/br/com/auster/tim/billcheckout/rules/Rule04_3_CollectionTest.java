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
import java.util.ArrayList;
import java.util.List;

import br.com.auster.common.lang.NamedDouble;
import br.com.auster.dware.request.file.FileRequest;
import br.com.auster.om.filter.request.BillcheckoutRequestWrapper;
import br.com.auster.om.invoice.Account;
import br.com.auster.om.invoice.rules.InvoiceAssertionWorker;



/**
 * @author framos
 * @version $Id$
 *
 */
public class Rule04_3_CollectionTest extends BaseRuleTest {


	private String[] RULES = { "src/main/conf/rules/credcorp/INIT-collection.drl",
	    					   "src/main/conf/rules/credcorp/R04.3-credcorp-collection.drl"};

	// overriding
	protected List<NamedDouble> results;

    protected void createGlobals() {
        results = new ArrayList<NamedDouble>();
        workingMemory.setGlobal( "results", results );
    }

	/**
	 * This file contains 250$ in debits
	 */
	public void testAccount1() {
		try {
			System.setProperty("cglib.disabled", "true");
			// faking request
			FileRequest req = new FileRequest(new File(RULES[0]));
			req.setTransactionId(String.valueOf(0));
			BillcheckoutRequestWrapper wrapper = new BillcheckoutRequestWrapper(req);
			req.getAttributes().put("custCode", "6.237749.13");
			// firing rules
			this.startupRuleEngine(RULES);
			Account acc = this.loadBGHTestFile("bgh/r04_3/ACCOUNT1.BGH");
			this.assertAccount(acc, wrapper);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * This file contains 200 in credits
	 */
	public void testAccount2() {
		try {
			System.setProperty("cglib.disabled", "true");
			// faking request
			FileRequest req = new FileRequest(new File(RULES[0]));
			req.setTransactionId(String.valueOf(0));
			BillcheckoutRequestWrapper wrapper = new BillcheckoutRequestWrapper(req);
			req.getAttributes().put("custCode", "6.238377.28.11.100015");
			// firing rules
			this.startupRuleEngine(RULES);
			Account acc = this.loadBGHTestFile("bgh/r04_3/ACCOUNT2.BGH");
			this.assertAccount(acc, wrapper);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(2, this.results.size());
			for (NamedDouble db : this.results) {
				if (db.getName().endsWith("debit")) {
					assertEquals(0.0, db.getValue(), 0.01);
				} else if (db.getName().endsWith("credit")) {
					assertEquals(-200.0, db.getValue(), 0.01);
				} else {
					fail();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * This file contains a Flat account with credcorp OCCs
	 */
	public void testAccount3() {
		try {
			System.setProperty("cglib.disabled", "true");
			// faking request
			FileRequest req = new FileRequest(new File(RULES[0]));
			req.setTransactionId(String.valueOf(0));
			BillcheckoutRequestWrapper wrapper = new BillcheckoutRequestWrapper(req);
			req.getAttributes().put("custCode", "1.14000593");
			// firing rules
			this.startupRuleEngine(RULES);
			Account acc = this.loadBGHTestFile("bgh/r04_3/ACCOUNT3.BGH");
			this.assertAccount(acc, wrapper);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * This file contains a large account without credcorp OCCs
	 */
	public void testAccount4() {
		try {
			System.setProperty("cglib.disabled", "true");
			// faking request
			FileRequest req = new FileRequest(new File(RULES[0]));
			req.setTransactionId(String.valueOf(0));
			BillcheckoutRequestWrapper wrapper = new BillcheckoutRequestWrapper(req);
			req.getAttributes().put("custCode", "6.238377.28.11.100015");
			// firing rules
			this.startupRuleEngine(RULES);
			Account acc = this.loadBGHTestFile("bgh/r04_3/ACCOUNT4.BGH");
			this.assertAccount(acc, wrapper);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * This file contains 250$ in debits
	 */
	public void testAccount5() {
		try {
			System.setProperty("cglib.disabled", "true");
			// faking request
			FileRequest req = new FileRequest(new File(RULES[0]));
			req.setTransactionId(String.valueOf(0));
			BillcheckoutRequestWrapper wrapper = new BillcheckoutRequestWrapper(req);
			req.getAttributes().put("custCode", "6.237749.19");
			// firing rules
			this.startupRuleEngine(RULES);
			Account acc = this.loadBGHTestFile("bgh/r04_3/ACCOUNT5.BGH");
			this.assertAccount(acc, wrapper);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(2, this.results.size());
			for (NamedDouble db : this.results) {
				if (db.getName().endsWith("debit")) {
					assertEquals(250.0, db.getValue(), 0.01);
				} else if (db.getName().endsWith("credit")) {
					assertEquals(0.0, db.getValue(), 0.01);
				} else {
					fail();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	protected void assertAccount(Account _account, Object _request) throws Exception {
    	TestRulesEngine engine = new TestRulesEngine(this.workingMemory);
    	InvoiceAssertionWorker.assertObjectsByReflection(engine, _account);
    	engine.assertFact(_request);
    }


}
