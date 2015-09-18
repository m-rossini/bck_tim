/*
 * Copyright (c) 2004-2008 Auster Solutions. All Rights Reserved.
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
 * Created on 25/07/2008
 */
package br.com.auster.tim.billcheckout.filter;


import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import br.com.auster.common.util.I18n;
import br.com.auster.om.filter.InvoiceRulesEngineFilter;
import br.com.auster.om.filter.request.BillcheckoutRequestWrapper;
import br.com.auster.om.invoice.InvoiceModelObject;
import br.com.auster.om.invoice.rules.InvoiceAssertionWorker;

/**
 * @author framos
 * @version $Id$
 *
 */
public class ImprovedInvoiceRulesEngineFilter extends InvoiceRulesEngineFilter {



	private static final I18n i18n = I18n.getInstance(InvoiceRulesEngineFilter.class);
	private static final Logger log = Logger.getLogger(ImprovedInvoiceRulesEngineFilter.class);



	public ImprovedInvoiceRulesEngineFilter(String _name) {
		super(_name);
	}


	protected void processRules(Map<String, Object> inputMap) throws Exception {
		List objects = (List) inputMap.get(inputTag);
		inputMap.put(resultsTag, Collections.EMPTY_LIST);
		Map appData = (this.globals != null) ? this.globals : new HashMap();
		for (Iterator i = objects.iterator(); i.hasNext();) {
			this.engine.prepare(appData);
			InvoiceModelObject account = (InvoiceModelObject) i.next();
			long start = System.currentTimeMillis();
			log.debug("ProcessingRules for account [" + account + "]");

			if (this.assertionType.equals(ASSERTION_TYPE_SIMPLE)) {
				InvoiceAssertionWorker.assertSimpleUsage(engine, account);
			} else if (this.assertionType.equals(ASSERTION_TYPE_REFLECTION)) {
				InvoiceAssertionWorker.assertObjectsByReflection(engine, account);
			} else {
				InvoiceAssertionWorker.assertUsageObjects(engine, account);
			}

			//Asserts the DWARE request as a FACT
			this.engine.assertFact(new BillcheckoutRequestWrapper( this.req) );

			// log to save assert-only time
			long startFire = System.currentTimeMillis();

			this.engine.fireRules();
			List results = this.engine.getResults();
			log.info("Filter named " + this.filterName + "processed [" + results.size() + "] results for account [" + account+ "]");
			// log messages splitted to keep time control messages in the same format
			long time = (System.currentTimeMillis() - start);
			// now logging details for this filter
			String timeAsString = "(assert/fire/total) " + (startFire - start) + "ms/" + (System.currentTimeMillis() - startFire) + "ms/" + (System.currentTimeMillis() - start) + "ms - assert count:" + this.engine.getAssertCount();
			log.info(i18n.getString("rulesEngine.endProcessing", this.getClass().getSimpleName(), this.filterName, timeAsString));
			inputMap.put(resultsTag, results);
		}
		this.objProcessor.processElement(inputMap);
	}

}
