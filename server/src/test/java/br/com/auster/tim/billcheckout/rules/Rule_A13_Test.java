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

import org.w3c.dom.Element;

import br.com.auster.billcheckout.model.ModelLoader;
import br.com.auster.common.sql.SQLConnectionManager;
import br.com.auster.common.xml.DOMUtils;
import br.com.auster.om.invoice.Account;



/**
 * @author framos
 * @version $Id$
 *
 */
public class Rule_A13_Test extends BaseRuleTest {


	private static String[] RULES_FILES = { "/conf/rules/INIT-create-cycledates-objects.drl",
											"/conf/rules/A13-cycle-dates.drl" };



    protected void createGlobals() {
    	super.createGlobals();
    	try {
    		Class.forName("org.apache.commons.dbcp.PoolingDriver");
    		Class.forName("oracle.jdbc.driver.OracleDriver");
    		SQLConnectionManager.init(DOMUtils.openDocument("bgh/guiding/pool.xml", false));

    		ModelLoader modelLoader =  new ModelLoader();
    		Element configuration = DOMUtils.openDocument("bgh/thresholds/model-caches.xml", false);
    		modelLoader.configure(configuration);
    		workingMemory.setGlobal( "modelLoader", modelLoader );
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }


	/**
	 * This is the correct file
	 */
	public void testAccount1() {
		try {
			System.getProperties().put("user.timezone", "GMT-3");
			// firing rules
			this.startupRuleEngine(RULES_FILES);
			Account acc = this.loadBGHTestFile("bgh/A13/ACCOUNT1.BGH");
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
