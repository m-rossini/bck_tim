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
import br.com.auster.tim.billcheckout.util.XStreamLoader;



/**
 * @author framos
 * @version $Id$
 *
 */
public class Rule04_3_Collection2Test extends BaseRuleTest {


	private String[] RULES = { "src/main/conf/rules/credcorp/INIT-collection.drl",
							   "src/main/conf/rules/credcorp/R04.3-credcorp-collection.drl"};

	// overriding
	protected List<NamedDouble> results;

    protected void createGlobals() {
        results = new ArrayList<NamedDouble>();
        workingMemory.setGlobal( "results", results );
    }

	/**
	 * Ticket #177
	 *
	 * 2x Cobrança 50$
	 */
	public void testAccount10() {
		try {
			System.setProperty("cglib.disabled", "true");
			System.setProperty(XStreamLoader.ROOT_DIR_SYSPROP, "src/test/resources/bgh/r04_3/credcorp");
			System.setProperty(XStreamLoader.FILENAME_SYSPROP, "credcorp.xml.gz");
			// faking request
			FileRequest req = new FileRequest(new File(RULES[0]));
			req.setTransactionId(String.valueOf(0));
			BillcheckoutRequestWrapper wrapper = new BillcheckoutRequestWrapper(req);
			req.getAttributes().put("custCode", "7.312058.10");
			// firing rules
			this.startupRuleEngine(RULES);
			Account acc = this.loadBGHTestFile("bgh/r04_3/ACCOUNT10.BGH");
			this.assertAccount(acc, wrapper);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(2, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Ticket #177
	 *
	 * 1x Cobrança 40$
	 * 1x Cobrança 20$
	 */
	public void testAccount11() {
		try {
			System.setProperty("cglib.disabled", "true");
			System.setProperty(XStreamLoader.ROOT_DIR_SYSPROP, "src/test/resources/bgh/r04_3/credcorp");
			System.setProperty(XStreamLoader.FILENAME_SYSPROP, "credcorp.xml.gz");
			// faking request
			FileRequest req = new FileRequest(new File(RULES[0]));
			req.setTransactionId(String.valueOf(0));
			BillcheckoutRequestWrapper wrapper = new BillcheckoutRequestWrapper(req);
			req.getAttributes().put("custCode", "7.312058.10.10");
			// firing rules
			this.startupRuleEngine(RULES);
			Account acc = this.loadBGHTestFile("bgh/r04_3/ACCOUNT11.BGH");
			this.assertAccount(acc, wrapper);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(2, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Ticket #177
	 *
	 * Nothing
	 */
	public void testAccount12() {
		try {
			System.setProperty("cglib.disabled", "true");
			System.setProperty(XStreamLoader.ROOT_DIR_SYSPROP, "src/test/resources/bgh/r04_3/credcorp");
			System.setProperty(XStreamLoader.FILENAME_SYSPROP, "credcorp.xml.gz");
			// faking request
			FileRequest req = new FileRequest(new File(RULES[0]));
			req.setTransactionId(String.valueOf(0));
			BillcheckoutRequestWrapper wrapper = new BillcheckoutRequestWrapper(req);
			req.getAttributes().put("custCode", "7.312058");
			// firing rules
			this.startupRuleEngine(RULES);
			Account acc = this.loadBGHTestFile("bgh/r04_3/ACCOUNT12.BGH");
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
	 * Ticket #177
	 *
	 * 1x Desconto 50$
	 */
	public void testAccount13() {
		try {
			System.setProperty("cglib.disabled", "true");
			System.setProperty(XStreamLoader.ROOT_DIR_SYSPROP, "src/test/resources/bgh/r04_3/credcorp");
			System.setProperty(XStreamLoader.FILENAME_SYSPROP, "credcorp.xml.gz");
			// faking request
			FileRequest req = new FileRequest(new File(RULES[0]));
			req.setTransactionId(String.valueOf(0));
			BillcheckoutRequestWrapper wrapper = new BillcheckoutRequestWrapper(req);
			req.getAttributes().put("custCode", "7.312058.10.10.100000");
			// firing rules
			this.startupRuleEngine(RULES);
			Account acc = this.loadBGHTestFile("bgh/r04_3/ACCOUNT13.BGH");
			this.assertAccount(acc, wrapper);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(2, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Ticket #177
	 *
	 * Nothing
	 */
	public void testAccount14() {
		try {
			System.setProperty("cglib.disabled", "true");
			System.setProperty(XStreamLoader.ROOT_DIR_SYSPROP, "src/test/resources/bgh/r04_3/credcorp");
			System.setProperty(XStreamLoader.FILENAME_SYSPROP, "credcorp.xml.gz");
			// faking request
			FileRequest req = new FileRequest(new File(RULES[0]));
			req.setTransactionId(String.valueOf(0));
			BillcheckoutRequestWrapper wrapper = new BillcheckoutRequestWrapper(req);
			req.getAttributes().put("custCode", "7.312058.10.10.100001");
			// firing rules
			this.startupRuleEngine(RULES);
			Account acc = this.loadBGHTestFile("bgh/r04_3/ACCOUNT14.BGH");
			this.assertAccount(acc, wrapper);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
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
