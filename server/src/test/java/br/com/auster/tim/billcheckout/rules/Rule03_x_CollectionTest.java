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
import java.util.List;
import java.util.Map;

import br.com.auster.common.lang.NamedHashMap;
import br.com.auster.dware.request.file.FileRequest;
import br.com.auster.om.filter.request.BillcheckoutRequestWrapper;
import br.com.auster.om.invoice.Account;
import br.com.auster.om.invoice.rules.InvoiceAssertionWorker;
import br.com.auster.om.util.UnitCounter;
import br.com.auster.tim.billcheckout.util.FreeUnitAccumulator;



/**
 * @author framos
 * @version $Id$
 *
 */
public class Rule03_x_CollectionTest extends BaseRuleTest {


	private String[] RULES = { "src/main/conf/rules/credcorp/INIT-collection.drl",
							   "src/main/conf/rules/credcorp/R03.x-fu-collection.drl"};

	// overriding
	protected List<FreeUnitAccumulator> results;

    protected void createGlobals() {
        results = new ArrayList<FreeUnitAccumulator>();
        workingMemory.setGlobal( "results", results );
    }

	/**
	 * This account has only one FU AccountID in use
	 */
	public void testAccount1() {
		try {
			// faking request
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
			// preparing engine
			FileRequest req = new FileRequest(new File(RULES[0]));
			req.setTransactionId(String.valueOf(0));
			BillcheckoutRequestWrapper wrapper = new BillcheckoutRequestWrapper(req);
			req.getAttributes().put("custCode", "1.12727509");
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r03_x/ACCOUNT1.BGH");
			this.assertAccount(acc, wrapper);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			// rule list

			// ********************** ATTENTION **********************
			// First rule has counter -4 since it will MARK all FU accounts for matching, but as the first is TRIGGERED
			//    all others will be REMOVED from the agenda, resulting in the negative value!!!!
			// **********************           **********************

			String[] RULE_LIST = {
					"Regra R03.x - Building FU Accumulators",
					"Regra R03.x - Accumulating reduction volumes",
					"Regra R03.x - Saving FU accumulators",
					"Add custCodeWithSlashes attribute into Request" };
			int[] AGENDA_COUNT = { 1, 6, 1, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);

			// running over results list to check if consequences where ok
			assertEquals(1, this.results.size());
			FreeUnitAccumulator fuInfo = this.results.get(0);
			// checking FU information
			assertEquals("9891858", fuInfo.getAccountId());
			assertEquals(formatter.parse("25/06/06 00:00:00"), fuInfo.getPeriodStart());
			checkFreeUnitAccumulator(fuInfo,
									 formatter.parse("24/07/06 00:00:00"),	//periodEnd
									 600,									//totalVolume
									 UnitCounter.TIME_COUNTER,				//typeOfUsage
									 "F600",								//shortDescription
									 formatter.parse("27/06/06 16:03:53"),	//minDatetime
									 formatter.parse("28/06/06 13:51:53")	//maxDatetime
									);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}


	/**
	 * This account has three FU Account IDs, but two are from the same Package. They are devided into messages and voice
	 * 	usage
	 */
	public void testAccount2() {
		try {
			// faking request
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
			// preparing engine
			FileRequest req = new FileRequest(new File(RULES[0]));
			req.setTransactionId(String.valueOf(0));
			BillcheckoutRequestWrapper wrapper = new BillcheckoutRequestWrapper(req);
			req.getAttributes().put("custCode", "5.24760");
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r03_x/ACCOUNT2.BGH");
			this.assertAccount(acc, wrapper);
			this.workingMemory.fireAllRules();
			// checking assertion and match expectations
			// rule list

			// ********************** ATTENTION **********************
			// First rule has counter -4 since it will MARK all FU accounts for matching, but as the first is TRIGGERED
			//    all others will be REMOVED from the agenda, resulting in the negative value!!!!
			// **********************           **********************

			String[] RULE_LIST = {
					"Regra R03.x - Building FU Accumulators",
					"Regra R03.x - Accumulating reduction volumes",
					"Regra R03.x - Saving FU accumulators",
					"Add custCodeWithSlashes attribute into Request" };
			int[] AGENDA_COUNT = { 5, 840, 5, 1 };
			checkAssertionCounters(RULE_LIST, AGENDA_COUNT);

			// running over results list to check if consequences where ok
			assertEquals(5, this.results.size());

			for (FreeUnitAccumulator fuInfo : this.results) {

				if (fuInfo.getAccountId().equals("15573205") &&
					fuInfo.getPeriodStart().equals(formatter.parse("25/04/06 00:00:00"))) {

					checkFreeUnitAccumulator(fuInfo,
							 formatter.parse("24/05/06 00:00:00"),	//periodEnd
							 517,									//totalVolume
							 UnitCounter.TIME_COUNTER,				//typeOfUsage
							 "PPTB3",								//shortDescription
							 formatter.parse("29/04/06 23:07:33"),	//minDatetime
							 formatter.parse("30/04/06 14:22:08")	//maxDatetime
							);

				} else if (fuInfo.getAccountId().equals("15573205") &&
						   fuInfo.getPeriodStart().equals(formatter.parse("25/05/06 00:00:00"))) {

					checkFreeUnitAccumulator(fuInfo,
							 formatter.parse("24/06/06 00:00:00"),	//periodEnd
							 309,									//totalVolume
							 UnitCounter.TIME_COUNTER,				//typeOfUsage
							 "PPTB3",								//shortDescription
							 formatter.parse("15/06/06 13:12:03"),	//minDatetime
							 formatter.parse("16/06/06 10:52:23")	//maxDatetime
							);

				} else if (fuInfo.getAccountId().equals("15573204") &&
						   fuInfo.getPeriodStart().equals(formatter.parse("25/04/06 00:00:00"))) {

					checkFreeUnitAccumulator(fuInfo,
							 formatter.parse("24/05/06 00:00:00"),	//periodEnd
							 99,									//totalVolume
							 UnitCounter.TIME_COUNTER,				//typeOfUsage
							 "PPTB3",								//shortDescription
							 formatter.parse("30/04/06 11:46:38"),	//minDatetime
							 formatter.parse("30/04/06 11:46:38")	//maxDatetime
							);

				} else if (fuInfo.getAccountId().equals("5329568") ||
						   fuInfo.getAccountId().equals("5329569")) {

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	
	/**
	 * ticket #275
	 */
	public void testAccount13() {
		try {
			// faking request
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
			// preparing engine
			FileRequest req = new FileRequest(new File(RULES[0]));
			req.setTransactionId(String.valueOf(0));
			BillcheckoutRequestWrapper wrapper = new BillcheckoutRequestWrapper(req);
			req.getAttributes().put("custCode", "1.10002909");
			// firing rules
			this.startupRuleEngine(RULES, true);
			Account acc = this.loadBGHTestFile("bgh/r03_x/ACCOUNT13.BGH");
			this.assertAccount(acc, wrapper);
			this.workingMemory.fireAllRules();
			
			// running over results list to check if consequences where ok
			assertEquals(1, this.results.size());

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

	protected void checkFreeUnitAccumulator(FreeUnitAccumulator _fuInfo, Date _endDate, long _volume, String _type, String _shdes, Date _min, Date _max) {
		assertEquals(_endDate, _fuInfo.getPeriodEnd());
		assertEquals(_type, _fuInfo.getTypeOfUsage());
		assertEquals(_shdes, _fuInfo.getShortDescription());
		assertEquals(_volume, _fuInfo.getTotalVolume().getUnits());
		assertEquals(_min, _fuInfo.getMinDatetime());
		assertEquals(_max, _fuInfo.getMaxDatetime());
	}

}
