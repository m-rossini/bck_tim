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
import br.com.auster.billcheckout.caches.CacheableKey;
import br.com.auster.common.sql.SQLConnectionManager;
import br.com.auster.common.xml.DOMUtils;
import br.com.auster.tim.billcheckout.param.UsageGroupVO.InnerAlternateKey;


/**
 * @author mruao
 * @version $Id$
 *
 */
public class UsageGroupLDVoAlternateTest extends TestCase {

	private UsageGroupLDCache cache;
	private boolean configured = false;
	
	protected void setUp() throws Exception {
		Class.forName("org.apache.commons.dbcp.PoolingDriver");
		Class.forName("oracle.jdbc.driver.OracleDriver");
		System.getProperties().put("log4j.configuration", "./log4j.xml");
		try {
			cache = new UsageGroupLDCache();
			if (!configured) {
				SQLConnectionManager.init(DOMUtils.openDocument("test.cfg", false));
				cache.configure(DOMUtils.openDocument("/alternateTest.xml", false));
				configured = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//-----------------------------
	// Alternate Key tests
	//-----------------------------
	public void testAlternateKey_found() {
		try {
			CacheableKey key = UsageGroupLDVO.createAlternateKey("Chamadas Longa Dist�ncia: Telemar", "31"); 
			UsageGroupLDVO voItem = (UsageGroupLDVO) cache.getFromCache(key);
			assertNotNull(voItem);
			assertEquals( "Chamadas Longa Dist�ncia: Telemar", voItem.getName());
			assertEquals( "31", voItem.getCspId());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testAlternateKey_notFound() {
		try {
			CacheableKey key = UsageGroupLDVO.createAlternateKey("Chamadas Longa Dist�ncia: Telemar", "41"); 
			UsageGroupLDVO voItem = (UsageGroupLDVO) cache.getFromCache(key);
			assertNull(voItem);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}