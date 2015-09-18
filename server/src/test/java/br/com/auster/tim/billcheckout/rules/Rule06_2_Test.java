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
import java.util.HashSet;

import br.com.auster.billcheckout.consequence.Consequence;
import br.com.auster.dware.request.file.FileRequest;
import br.com.auster.om.filter.request.BillcheckoutRequestWrapper;
import br.com.auster.om.invoice.Account;
import br.com.auster.om.invoice.rules.InvoiceAssertionWorker;



/**
 * @author framos
 * @version $Id$
 *
 */
public class Rule06_2_Test extends BaseRuleTest {


	private String[] RULES1 = {"src/main/conf/rules/guiding/load-carrier-code.drl"};
	private String[] RULES2 = {"src/main/conf/rules/R06.2-validate-nf-series.drl" };


	/**
	 * This is the original file, its step is override to S2 so that
	 * it matches the constraint for step number.
	 */
	public void testAccount1() {
		try {
			// firing rules
			resetAndRun("bgh/r06_2/ACCOUNT1.BGH", 0, 0, 2);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * This is ACCOUNT1.BGH where the correct LOCAL NF series was modified to AA, its step is override to S2 so that
	 * it matches the constraint for step number.
	 */
	public void testAccount2() {
		try {
			// firing rules
			resetAndRun("bgh/r06_2/ACCOUNT2.BGH", 0, 0, 2);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * This is ACCOUNT1.BGH where the correct LOCAL NF series was modified to U, its step is override to S2 so that
	 * it matches the constraint for step number.
	 */
	public void testAccount3() {
		try {
			// firing rules
			resetAndRun("bgh/r06_2/ACCOUNT3.BGH", 1, 0, 2);
			// running over results list to check if consequences where ok
			HashSet<String> hashset = new HashSet<String>();
			for (Consequence c : this.results) {
				hashset.add(c.getAttributes().getAttributeValue1());
			}
			assertEquals(1, hashset.size());
			assertTrue(hashset.contains("000.000.088-U"));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * This is ACCOUNT1.BGH where the correct LOCAL NF series was modified lower case, its step is override to S2 so that
	 * it matches the constraint for step number.
	 */
	public void testAccount4() {
		try {
			// firing rules
			resetAndRun("bgh/r06_2/ACCOUNT4.BGH", 1, 0, 2);
			// running over results list to check if consequences where ok
			HashSet<String> hashset = new HashSet<String>();
			for (Consequence c : this.results) {
				hashset.add(c.getAttributes().getAttributeValue1());
			}
			assertEquals(1, hashset.size());
//			assertTrue(hashset.contains("000.000.044-C"));
			assertTrue(hashset.contains("000.000.088-ab"));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}


	/**
	 * This is ACCOUNT1.BGH where the UF was changed to MG and LOCAL NF series is U, its step is override to S2 so that
	 * it matches the constraint for step number.
	 */
	public void testAccount5() {
		try {
			// firing rules
			resetAndRun("bgh/r06_2/ACCOUNT5.BGH", 0, 0, 2);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * This is ACCOUNT1.BGH where the UF was changed to MG and LOCAL NF series is AB, its step is override to S2 so that
	 * it matches the constraint for step number.
	 */
	public void testAccount6() {
		try {
			// firing rules
			resetAndRun("bgh/r06_2/ACCOUNT6.BGH", 1, 0, 2);
			// running over results list to check if consequences where ok
			HashSet<String> hashset = new HashSet<String>();
			for (Consequence c : this.results) {
				hashset.add(c.getAttributes().getAttributeValue1());
			}
			assertEquals(1, hashset.size());
//			assertTrue(hashset.contains("000.000.044-C"));
			assertTrue(hashset.contains("000.000.088-AB"));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * This is ACCOUNT1.BGH where the UF was changed to SE and LOCAL NF series is U, its step is override to S2 so that
	 * it matches the constraint for step number.
	 */
	public void testAccount7() {
		try {
			// firing rules
			resetAndRun("bgh/r06_2/ACCOUNT7.BGH", 1, 0, 2);
			// running over results list to check if consequences where ok
			HashSet<String> hashset = new HashSet<String>();
			for (Consequence c : this.results) {
				hashset.add(c.getAttributes().getAttributeValue1());
			}
			assertEquals(1, hashset.size());
//			assertTrue(hashset.contains("000.000.044-C"));
			assertTrue(hashset.contains("000.000.088-U"));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * This is ACCOUNT1.BGH where the UF was changed to SE and LOCAL NF series is AB, its step is override to S2 so that
	 * it matches the constraint for step number.
	 */
	public void testAccount8() {
		try {
			// firing rules
			resetAndRun("bgh/r06_2/ACCOUNT8.BGH", 1, 0, 2);
			// running over results list to check if consequences where ok
			HashSet<String> hashset = new HashSet<String>();
			for (Consequence c : this.results) {
				hashset.add(c.getAttributes().getAttributeValue1());
			}
			assertEquals(1, hashset.size());
//			assertTrue(hashset.contains("000.000.044-C"));
			assertTrue(hashset.contains("000.000.088-AB"));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * This is ACCOUNT1.BGH where the UF was changed to SE and LOCAL NF series is B5, its step is override to S2 so that
	 * it matches the constraint for step number.
	 */
	public void testAccount9() {
		try {
			// firing rules
			resetAndRun("bgh/r06_2/ACCOUNT1.BGH", 0, 0, 2);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * This is ACCOUNT1.BGH where the carrier was changed to Intelig, UF was changed to AM and LD NF series is 4,
	 * its step is override to S2 so that it matches the constraint for step number.
	 */
	public void testAccount10() {
		try {
			// firing rules
			resetAndRun("bgh/r06_2/ACCOUNT10.BGH", 1, 0, 2);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * This is ACCOUNT1.BGH where the carrier was changed to Telemar, UF was changed to RS and LD NF series is 5,
	 * its step is override to S2 so that it matches the constraint for step number.
	 */
	public void testAccount11() {
		try {
			// firing rules
			resetAndRun("bgh/r06_2/ACCOUNT11.BGH", 1, 0, 2);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * This is ACCOUNT1.BGH where the carrier was changed to Claro, UF was changed to RR and LD NF series is B6,
	 * its step is override to S2 so that it matches the constraint for step number.
	 */
	public void testAccount12() {
		try {
			// firing rules
			resetAndRun("bgh/r06_2/ACCOUNT12.BGH", 1, 0, 2);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Copy of the ACCOUNT8.BGH where the UF was changed to SE and LOCAL NF series is AB but it is on step 3 (S3),
	 * so the results list will be empty
	 */
	public void testAccount13() {
		try {
			// firing rules
			resetAndRun("bgh/r06_2/ACCOUNT13.BGH", 0, 0, 3);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * BGH to test the sub-series validation regarding ticket #235.
	 * There is no error.
	 * There is one NF number with sub-serie: 000.000.375-A-5a (EMBRATEL-RJ)
	 */
	public void testAccount14() {
		try {
			// firing rules
			resetAndRun("bgh/r06_2/ACCOUNT14.BGH", 0, 0, 2);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * BGH to test the sub-series validation regarding ticket #235.
	 * There is an error with the NF number: 000.000.375-A-5b (EMBRATEL-RJ), the right
	 * sub-serie is 5a.
	 */
	public void testAccount15() {
		try {
			// firing rules
			resetAndRun("bgh/r06_2/ACCOUNT15.BGH", 1, 0, 2);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * BGH to test the sub-series validation regarding ticket #235.
	 * There is no error.
	 * There is two NF number with sub-serie: 000.000.375-A-5a (EMBRATEL-RJ)
	 * and 000.000.220-U-5 (TELEMAR-MG)
	 */
	public void testAccount16() {
		try {
			// firing rules
			resetAndRun("bgh/r06_2/ACCOUNT16.BGH", 0, 0, 2);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 */
	public void testAccount17() {
		try {
			// firing rules
			resetAndRun("bgh/r06_2/ACCOUNT17.BGH", 0, 0, 2);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}


	/**
	 * Ticket #235 - Not handling CTBC carrier
	 */
	public void testAccount18() {
		try {
			// firing rules
			resetAndRun("bgh/r06_2/ACCOUNT18.BGH", 0, 0, 2);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	private void resetAndRun(String _filename, int _errorCount, int _counter, int _mode) throws Exception {
		// firing rules
		this.startupRuleEngine(RULES1);
		Account act = this.loadBGHTestFile(_filename);
        this.assertAccount(act);
		this.workingMemory.fireAllRules();
		this.startupRuleEngine(RULES2);
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
		FileRequest req = new FileRequest(new File(RULES2[0]));
		req.setTransactionId(String.valueOf(_index));
		req.getAttributes().put("mode.id", "S" + String.valueOf(_mode));
		BillcheckoutRequestWrapper wrapper = new BillcheckoutRequestWrapper(req);
		return wrapper;
    }

}
