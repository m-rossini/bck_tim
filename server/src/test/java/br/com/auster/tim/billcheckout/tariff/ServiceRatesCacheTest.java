/*
 * Copyright (c) 2004-2010 Auster Solutions. All Rights Reserved.
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
 * Created on 18/03/2010
 */
package br.com.auster.tim.billcheckout.tariff;

import junit.framework.TestCase;

import org.w3c.dom.Element;

import br.com.auster.common.sql.SQLConnectionManager;
import br.com.auster.common.xml.DOMUtils;

/**
 * TODO What this class is responsible for
 *
 * @author Frederico Ramos
 * @version $Id$
 * @since JDK1.4
 */
public class ServiceRatesCacheTest  extends TestCase {

	
	

	@Override
	protected void setUp() throws Exception {
    	try {
      		Class.forName("org.apache.commons.dbcp.PoolingDriver");
    		Class.forName("oracle.jdbc.driver.OracleDriver");
    		SQLConnectionManager.init(DOMUtils.openDocument("test.cfg", false));
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
	}
	
	
	public void testFindTariff() {
		try {
			Element arg0 = DOMUtils.openDocument("test.xml", false);
			ServiceRatesCache cache = new ServiceRatesCache();
			cache.configure(arg0);
			
			CachedServiceRatesVO vo = (CachedServiceRatesVO) cache.getFromCache(CachedServiceRatesVO.createKey("TSFE", "Plano Light 40", "AM"));
			
			assertNotNull(vo);
			assertEquals("TSFE", vo.getSnCode());
			assertEquals("Plano Light 40", vo.getPlanName());
			assertEquals("AM", vo.getState());
			// number of ranges
			assertEquals(6, vo.getNumberOfRanges());
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	public void testTariffNotFound() {
		try {
			Element arg0 = DOMUtils.openDocument("test.xml", false);
			ServiceRatesCache cache = new ServiceRatesCache();
			cache.configure(arg0);
			
			CachedServiceRatesVO vo = (CachedServiceRatesVO)
			                 cache.getFromCache(CachedServiceRatesVO.createKey("TS11", "Plano T Voc� GSM PR", "PR"));
			
			assertNull(vo);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}
