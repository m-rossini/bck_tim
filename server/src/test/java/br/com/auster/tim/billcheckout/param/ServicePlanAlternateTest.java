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
 * Created on 04/12/2006
 */
package br.com.auster.tim.billcheckout.param;

import junit.framework.TestCase;
import br.com.auster.common.sql.SQLConnectionManager;
import br.com.auster.common.xml.DOMUtils;
import br.com.auster.tim.billcheckout.param.ServicePlanVO.ServicePlanAlternateKey;

/**
 * @author framos
 * @version $Id$
 *
 */
public class ServicePlanAlternateTest extends TestCase {

	
	

	private ServicePlanCache cache;
	private boolean configured = false;
	
	protected void setUp() throws Exception {
		Class.forName("org.apache.commons.dbcp.PoolingDriver");
		Class.forName("oracle.jdbc.driver.OracleDriver");
		System.getProperties().put("log4j.configuration", "./log4j.xml");
		try {
			cache = new ServicePlanCache();
			if (!configured) {
				SQLConnectionManager.init(DOMUtils.openDocument("test.cfg", false));
				cache.configure(DOMUtils.openDocument("/alternateTest.xml", false));
				configured = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void testFoundServicePlan() {
		try {
			ServicePlanAlternateKey key = new ServicePlanAlternateKey("SSF1", "Plano Meu Mundo", "MT"); 
			ServicePlanVO svcplan = (ServicePlanVO) cache.getFromCache(key);
			assertNotNull(svcplan);
			assertEquals("Plano Meu Mundo", svcplan.getPlanCode());
			assertEquals("MT", svcplan.getPlanState());
			assertEquals("SSF1", svcplan.getServiceCode());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testNotFoundServicePlan() {
		try {
			ServicePlanAlternateKey key = new ServicePlanAlternateKey("SSFX", "Plano XYZ Mundo", "XT"); 
			ServicePlanVO svcplan = (ServicePlanVO) cache.getFromCache(key);
			assertNull(svcplan);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

}
