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

import br.com.auster.billcheckout.consequence.Consequence;
import br.com.auster.dware.request.file.FileRequest;
import br.com.auster.om.filter.request.BillcheckoutRequestWrapper;
import br.com.auster.om.invoice.Account;
import br.com.auster.om.invoice.rules.InvoiceAssertionWorker;
import br.com.auster.tim.billcheckout.util.CustcodeHelper;



/**
 * @author framos
 * @version $Id$
 *
 */
public class Rule28_8_Test extends BaseRuleTest {


	private String[] RULES = { "src/main/conf/rules/R28.8-corporate-diff-duedates.drl" };
	private static int index = 0;
	private static BillcheckoutRequestWrapper wrapper;
	
	protected void setUp() throws Exception {
		CustcodeHelper.clearDates();
	}
	
	public void testAccounts1And2() {
		try {
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
			
			// asserting first cut cycle 
			resetAndRun("bgh/r28_8/ACCOUNT1A.BGH", 0, true, true);
			resetAndRun("bgh/r28_8/ACCOUNT1B.BGH", 0, true, false);
			resetAndRun("bgh/r28_8/ACCOUNT1C.BGH", 0, true, false);
			resetAndRun("bgh/r28_8/ACCOUNT1D.BGH", 1, true, false);
			Consequence c = this.results.get(0);
			assertEquals("6.310367", c.getAttributes().getAttributeValue1() );
			assertEquals("22/11/2006", c.getAttributes().getAttributeValue3() );
			// up to here, we simulated diff. threads; now, we will simulate running dif. accounts over the 
			//    same thread
			resetAndRun("bgh/r28_8/ACCOUNT1E.BGH", 0, true, false);
			resetAndRun("bgh/r28_8/ACCOUNT1F.BGH", 0, false, false);
			resetAndRun("bgh/r28_8/ACCOUNT1G.BGH", 0, false, false);

			// running over results list to check if consequences where ok
			assertNotNull(CustcodeHelper.getCorporateDueDate("6.310367"));
			assertNotNull(CustcodeHelper.getCorporateDueDate("6.310367.100101"));
			assertEquals("25/11/2006", sdf.format(CustcodeHelper.getCorporateDueDate("6.310367.100101")));
			assertNotNull(CustcodeHelper.getCorporateDueDate("6.110367"));
			assertNotNull(CustcodeHelper.getCorporateDueDate("6.110367.100101"));
			assertEquals("22/11/2006", sdf.format(CustcodeHelper.getCorporateDueDate("6.110367.100101")));
			assertNull(CustcodeHelper.getCorporateDueDate("1.10623954"));
			assertNull(CustcodeHelper.getCorporateDueDate("1.122"));
			
			// second cut cycle
			resetAndRun("bgh/r28_8/ACCOUNT2A.BGH", 0, true, true);
			resetAndRun("bgh/r28_8/ACCOUNT2B.BGH", 0, true, false);
			resetAndRun("bgh/r28_8/ACCOUNT2C.BGH", 0, true, false);
			resetAndRun("bgh/r28_8/ACCOUNT2D.BGH", 1, true, false);
			c = this.results.get(0);
			assertEquals("6.310367", c.getAttributes().getAttributeValue1() );
			assertEquals("21/12/2006", c.getAttributes().getAttributeValue3() );
			// up to here, we simulated diff. threads; now, we will simulate running dif. accounts over the 
			//    same thread
			resetAndRun("bgh/r28_8/ACCOUNT2E.BGH", 1, true, false);
			c = this.results.get(0);
			assertEquals("6.310367", c.getAttributes().getAttributeValue1() );
			assertEquals("25/11/2006", c.getAttributes().getAttributeValue3() );
			resetAndRun("bgh/r28_8/ACCOUNT2F.BGH", 0, false, false);
			resetAndRun("bgh/r28_8/ACCOUNT2G.BGH", 0, false, false);

			assertNotNull(CustcodeHelper.getCorporateDueDate("6.310367"));
			assertNotNull(CustcodeHelper.getCorporateDueDate("6.310367.1001"));
			assertEquals("25/12/2006", sdf.format(CustcodeHelper.getCorporateDueDate("6.310367.1001")));
			assertNotNull(CustcodeHelper.getCorporateDueDate("6.110367"));
			assertNotNull(CustcodeHelper.getCorporateDueDate("6.110367.100101"));
			assertEquals("23/12/2006", sdf.format(CustcodeHelper.getCorporateDueDate("6.110367.100101")));
			assertNull(CustcodeHelper.getCorporateDueDate("1.10623954"));
			assertNull(CustcodeHelper.getCorporateDueDate("1.122"));
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testAccounts3() {
		try {
			// asserting first cut cycle 
			resetAndRun("bgh/r28_8/ACCOUNT3A.BGH", 0, true, true);			
			resetAndRun("bgh/r28_8/ACCOUNT3B.BGH", 1, true, false);
			resetAndRun("bgh/r28_8/ACCOUNT3C.BGH", 1, true, false);
			resetAndRun("bgh/r28_8/ACCOUNT3D.BGH", 1, true, false);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	public void testAccounts4() {
		try {
			// asserting first cut cycle 
			resetAndRun("bgh/r28_8/ACCOUNT4A.BGH", 0, true, true);			
			resetAndRun("bgh/r28_8/ACCOUNT4B.BGH", 1, true, false);
			resetAndRun("bgh/r28_8/ACCOUNT4C.BGH", 1, true, false);
			resetAndRun("bgh/r28_8/ACCOUNT4D.BGH", 1, true, false);
			resetAndRun("bgh/r28_8/ACCOUNT4E.BGH", 1, true, false);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	
	private void resetAndRun(String _filename, int _errorCount, boolean _resetWorkingMem, boolean _newRequest) throws Exception {
		if (_resetWorkingMem) {
			this.startupRuleEngine(RULES);
		} else {
			// but working mem. should always be reset 
			this.workingMemory = this.ruleBase.newWorkingMemory();
			this.results.clear();
		}
		if (_newRequest) {
			FileRequest req = new FileRequest(new File(RULES[0]));
			req.setTransactionId(String.valueOf(index++));
			wrapper = new BillcheckoutRequestWrapper(req);
		}
		Account act = this.loadBGHTestFile(_filename);
        this.assertAccount(act, wrapper);
		this.workingMemory.fireAllRules();
		assertEquals(_errorCount, this.results.size());
	}
        
    protected void assertAccount(Account _account, Object _request) throws Exception {
    	TestRulesEngine engine = new TestRulesEngine(this.workingMemory);
    	InvoiceAssertionWorker.assertObjectsByReflection(engine, _account);
    	engine.assertFact(_request);
    }
	
}
