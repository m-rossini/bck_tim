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
import java.util.HashMap;

import br.com.auster.billcheckout.consequence.Consequence;
import br.com.auster.billcheckout.consequence.telco.TelcoConsequence;
import br.com.auster.dware.graph.Request;
import br.com.auster.dware.request.file.FileRequest;
import br.com.auster.om.filter.request.BillcheckoutRequestWrapper;
import br.com.auster.om.invoice.Account;
import br.com.auster.om.invoice.rules.InvoiceAssertionWorker;
import br.com.auster.tim.billcheckout.util.NFNumberHelper;


/**
 * @author framos
 * @version $Id$
 *
 */
public class Rule06_1_Test extends BaseRuleTest {


	private String[] RULES = { "src/main/conf/rules/R06.1-validate-nf-number.drl" };



	protected void setUp() throws Exception {
		NFNumberHelper.clearNumbers();
	}

	/**
	 * This is the correct file, its step is override to S2 so that
	 * it matches the constraint for step number.
	 */
	public void testAccount1() {
		try {
			// firing rules
			resetAndRun("bgh/r06_1/ACCOUNT1.BGH", 0, 0, 2);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * This is was manipulated to have incorrect information(invalid NF number), its step is override to S2 so that
	 * it matches the constraint for step number.
	 */
	public void testAccount2() {
		try {
			// firing rules
			resetAndRun("bgh/r06_1/ACCOUNT2.BGH", 1, 0, 2);
			// running over results list to check if consequences where ok
			assertTrue(this.results.get(0) instanceof TelcoConsequence);
			TelcoConsequence c = (TelcoConsequence)this.results.get(0);
			assertEquals("Número NF", c.getAttributes().getAttributeName1());
			assertEquals("0.000.088-AB", c.getAttributes().getAttributeValue1());
			assertEquals("A numeração desta NF não é válida.", c.getDescription());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Will test NF numbering duplication over cycles, its step is override to S2 so that
	 * it matches the constraint for step number.
	 */
	public void testDuplicatedNFNumbers() {
		try {
			// firing rules
			resetAndRun("bgh/r06_1/ACCOUNT1.BGH", 0, 0, 2);
			resetAndRun("bgh/r06_1/ACCOUNT3.BGH", 0, 0, 2);
			resetAndRun("bgh/r06_1/ACCOUNT2.BGH", 2, 0, 2);
			assertTrue(this.results.get(0) instanceof TelcoConsequence);
			TelcoConsequence c = (TelcoConsequence)this.results.get(0);
			assertEquals("A numeração desta NF já foi encontrada nesta amostragem.", c.getDescription());
			assertEquals("000.000.044-C", c.getAttributes().getAttributeValue1());
			assertEquals("1.12727509", c.getAttributes().getAttributeValue2());

			resetAndRun("bgh/r06_1/ACCOUNT4.BGH", 0, 1, 2);
			resetAndRun("bgh/r06_1/ACCOUNT2.BGH", 2, 1, 2);
			resetAndRun("bgh/r06_1/ACCOUNT1.BGH", 2, 1, 2);
			assertTrue(this.results.get(0) instanceof TelcoConsequence);
			c = (TelcoConsequence)this.results.get(0);
			assertEquals("A numeração desta NF já foi encontrada nesta amostragem.", c.getDescription());
			assertEquals("000.000.044-C", c.getAttributes().getAttributeValue1());
			assertEquals("1.12727509", c.getAttributes().getAttributeValue2());

			resetAndRun("bgh/r06_1/ACCOUNT1.BGH", 0, 2, 2);
			// it has the same NF Number, but from diff. UFs
			resetAndRun("bgh/r06_1/ACCOUNT7.BGH", 0, 2, 2);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Will test NF numbering duplication over cycles, its step is override to S2 so that
	 * it matches the constraint for step number.
	 */
	public void testDuplicatedNFNumbersOnSingleInvoice() {
		try {
			resetAndRun("bgh/r06_1/ACCOUNT5.BGH", 1, 0, 2);
			assertTrue(this.results.get(0) instanceof TelcoConsequence);
			TelcoConsequence c = (TelcoConsequence)this.results.get(0);
			assertEquals("A numeração desta NF já foi encontrada nesta amostragem.", c.getDescription());
			assertEquals("000.000.088-AB", c.getAttributes().getAttributeValue1());
			assertEquals("1.12747509", c.getAttributes().getAttributeValue2());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Copy of BGH file "Account2". The NF number ir invalid and duplicated but it is on step 3 (S3),
	 * so the results list will be empty
	 */
	public void testAccount6() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
	        this.assertAccount(this.loadBGHTestFile("bgh/r06_1/ACCOUNT6.BGH"), 0);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * BGH file sent by TIM. The NF number ir invalid but it is on step 3 (S3),
	 * so the results list will be empty
	 */
	public void testAccount7() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
	        this.assertAccount(this.loadBGHTestFile("bgh/r06_1/ACCOUNT7.BGH"), 0);
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			assertEquals(0, this.results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	private void resetAndRun(String _filename, int _errorCount, int _counter, int _mode) throws Exception {
		this.startupRuleEngine(RULES);
		Account act = this.loadBGHTestFile(_filename);
        this.assertAccount(act, createRequest(_counter, _mode));
		this.workingMemory.fireAllRules();
		assertEquals(_errorCount, this.results.size());
	}


    protected void assertAccount(Account _account, Object _request) throws Exception {
    	TestRulesEngine engine = new TestRulesEngine(this.workingMemory);
    	InvoiceAssertionWorker.assertObjectsByReflection(engine, _account);
    	engine.assertFact(_request);
    }

    protected BillcheckoutRequestWrapper createRequest(int _index, int _mode) {
		FileRequest req = new FileRequest(new File(RULES[0]));
		req.setTransactionId(String.valueOf(_index));
		req.getAttributes().put("mode.id", "S" + String.valueOf(_mode));
		BillcheckoutRequestWrapper wrapper = new BillcheckoutRequestWrapper(req);
		return wrapper;
    }

}