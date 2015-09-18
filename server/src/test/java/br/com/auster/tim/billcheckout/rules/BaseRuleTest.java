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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.log4j.xml.DOMConfigurator;
import org.drools.RuleBase;
import org.drools.RuleBaseFactory;
import org.drools.WorkingMemory;
import org.drools.compiler.DroolsParserException;
import org.drools.compiler.PackageBuilder;
import org.drools.event.AgendaEventListener;
import org.drools.event.WorkingMemoryEventListener;
import org.drools.rule.Package;
import org.w3c.dom.Element;

import br.com.auster.billcheckout.consequence.Consequence;
import br.com.auster.billcheckout.consequence.telco.TelcoConsequenceBuilder;
import br.com.auster.common.io.IOUtils;
import br.com.auster.common.rules.RulesEngineProcessor;
import br.com.auster.common.xml.DOMUtils;
import br.com.auster.dware.graph.Request;
import br.com.auster.om.filter.request.BillcheckoutRequestWrapper;
import br.com.auster.om.invoice.Account;
import br.com.auster.om.invoice.rules.InvoiceAssertionWorker;
import br.com.auster.tim.bgh.BGH2SAX;
import br.com.auster.tim.billcheckout.util.TIMDimensionCache;

/**
 * @author framos
 * @version $Id$
 *
 */
public abstract class BaseRuleTest extends TestCase {



	public static final String LOGGER_FILE = "log4j.xml";


	protected void setUp() throws Exception {
		DOMConfigurator.configure(DOMUtils.openDocument(LOGGER_FILE, false));
	}


    protected WorkingMemory workingMemory;
    protected TestcaseEventListener eventListener;
    protected List<Consequence> results;
    protected RuleBase ruleBase;
    protected boolean eventListenerEnabled;

    protected void startupRuleEngine(String[] _filename) throws Exception {
    	startupRuleEngine(_filename, false);
    }

    /**
     * If <code>_enableListener</code> is <code>false</code> then the instance level attribute will be created but
     * 	not set as listener for events in the working memory.
     */
    protected void startupRuleEngine(String[] _filename, boolean _enableListener) throws Exception {
    	long time1 = System.currentTimeMillis();
        ruleBase = createRuleBase(_filename);
        long time2 = System.currentTimeMillis();
        System.out.println("Build rulebase took " + (time2-time1) + "ms");
        eventListener = new TestcaseEventListener();
        workingMemory = ruleBase.newWorkingMemory();
        eventListenerEnabled = _enableListener;
        if (eventListenerEnabled) {
	        workingMemory.addEventListener((WorkingMemoryEventListener)eventListener);
	        workingMemory.addEventListener((AgendaEventListener)eventListener);
        }
        createGlobals();
    }

    protected void createGlobals() {
        results = new ArrayList<Consequence>();
        workingMemory.setGlobal( "results", results );

        TIMDimensionCache cache = new TIMDimensionCache();
        workingMemory.setGlobal( "dimensionCache", cache );

        TelcoConsequenceBuilder builder = new TelcoConsequenceBuilder();
        builder.setLenient( true );
        workingMemory.setGlobal( "consequenceBuilder", builder );
    }

    protected RuleBase createRuleBase(String[] ruleFileName) throws DroolsParserException, IOException, Exception {
		// pre build the package
		final PackageBuilder builder = new PackageBuilder();
		final RuleBase ruleBase = RuleBaseFactory.newRuleBase(RuleBase.RETEOO);
		for (int i =0; i < ruleFileName.length ; i++) {
			InputStream in = IOUtils.openFileForRead(ruleFileName[i]);
			builder.addPackageFromDrl(new InputStreamReader(in));
			final Package pkg = builder.getPackage();
			// add the package to a rulebase
			ruleBase.addPackage(pkg);
    	}
		return ruleBase;
	}


    protected Account loadBGHTestFile(String _sourcename) throws Exception {
		BGH2SAX loader = new BGH2SAX();
		return loader.run(_sourcename);
    }

    protected String bghIntoXML(String _sourcename) throws Exception {
		BGH2SAX loader = new BGH2SAX();
		return loader.intoXML(_sourcename);
    }

    protected void assertAccount(Account _account, Request _request) throws Exception {
    	TestRulesEngine re = new TestRulesEngine(this.workingMemory);
    	InvoiceAssertionWorker.assertObjectsByReflection(re, _account);
    	re.assertFact(new BillcheckoutRequestWrapper(_request));
    	System.out.println("Asserted " + re.getAssertedCount() + " objects");
    }

    protected void assertAccount(Account _account) throws Exception {
    	this.assertAccount(_account, null);
    }

    /**
     * With an array of rules, and the corresponding number of expected firing,
     *   this method allows for checking if all rules are being triggered the
     *   number of times as they should.
     * </p>
     * So now we can validate if the tuples matched are only the ones we expect to happen.
     */
	protected void checkAssertionCounters(String[] _rules, int[] _correctCount) {
		if (!this.eventListenerEnabled) { return; }
		Map<String, Integer> agendaCount = this.eventListener.getAgendaCount();
		assertEquals("Incorrect number of matches.", _rules.length, agendaCount.size());
		for (int i=0; i < _rules.length; i++) {
			Integer counter = agendaCount.get(_rules[i]);
			assertNotNull("Counter for rule " + _rules[i] + " not found.", counter);
			assertEquals("Counter for rule " + _rules[i] + " does not match count.", counter.intValue(), _correctCount[i]);
		}
	}


    public static class TestRulesEngine implements RulesEngineProcessor {

    	private WorkingMemory mem;
    	private int assertedCount;

    	public int getAssertCount() {return assertedCount;}

    	public TestRulesEngine(WorkingMemory _mem) {
    		this.mem = _mem;
    		this.assertedCount=0;
    	}

		public void assertFact(Object arg0) throws Exception {
			this.mem.assertObject(arg0);
			this.assertedCount++;
		}

		public void clear() { }
		public void configure(Element arg0) throws Exception {}
		public void fireRules() throws Exception {}
		public List getResults() { return null; }
		public void init(Map arg0) throws Exception {}
		public void prepare(Map arg0) throws Exception {}
		public String getName() { return "name"; }

		public long getAssertedCount() { return this.assertedCount; }

    }
}