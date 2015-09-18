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
 * Created on 24/03/2008
 */
package br.com.auster.tim.billcheckout.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import junit.framework.TestCase;

import org.w3c.dom.Element;

import br.com.auster.common.sql.SQLConnectionManager;
import br.com.auster.common.xml.DOMUtils;
import br.com.auster.tim.om.invoice.TIMUsageDetail;
import br.com.auster.tim.om.invoice.UsageFUDetail;

/**
 * @author framos
 * @version $Id$
 *
 */
public class UsageGroupingCacheTest extends TestCase {




	public void testCycleCalculation01() {
    	try {
    		Class.forName("org.apache.commons.dbcp.PoolingDriver");
    		Class.forName("oracle.jdbc.driver.OracleDriver");
    		SQLConnectionManager.init(DOMUtils.openDocument("bgh/guiding/pool.xml", false));

	    	Element arg0 = DOMUtils.openDocument("bgh/guiding/caches.xml", false);

	    	UsageGroupingCache usCache = new UsageGroupingCache();
	    	usCache.configure(arg0);

	    	assertEquals("TIM Torpedo", usCache.getFromCache(buildDetail("0", null, "TS22", "TORM", "VIP16", null, "H")));
	    	assertEquals("Chamadas Longa Distância: TIM LD 41", usCache.getFromCache(buildDetail("41", null, "TS11", "V3T", null, null, "H")));
	    	assertEquals("Serviços de SMS", usCache.getFromCache(buildDetail("132", null, "TS21", "TASYN", null, null, "H")));

    	} catch (Exception e) {
    		e.printStackTrace();
    	}
	}

	public void testInitEnvironmentConnection(){
    	UsageGroupingCache cache = new UsageGroupingCache();

    	cache.cache.clear();

		Connection conn = null;
		DatabaseMetaData dbMetaData;
		try {
			Class.forName("org.apache.commons.dbcp.PoolingDriver");
			Class.forName("oracle.jdbc.driver.OracleDriver");

			SQLConnectionManager.init(DOMUtils.openDocument("bgh/guiding/pool.xml", false));
			Element conf = DOMUtils.openDocument("bgh/guiding/caches3.xml", false);
			cache.configure(conf);

			conn = SQLConnectionManager.getInstance(cache.getPoolName()).getConnection();
			dbMetaData = conn.getMetaData();

			assertEquals("jdbc:oracle:thin:@MCCOY:1521:TEST01", dbMetaData.getURL());
			assertEquals("TIM_BCK", dbMetaData.getUserName());
			conn.close();

			cache.initEnvironment("S2");
			conn = SQLConnectionManager.getInstance(cache.getPoolName()).getConnection();
			dbMetaData = conn.getMetaData();
			assertEquals("jdbc:oracle:thin:@MCCOY:1521:DEVEL01", dbMetaData.getURL());
			assertEquals("TIM_BCK", dbMetaData.getUserName());

			cache.initEnvironment("S3");

		} catch (Exception e) {
			assertTrue(e.getMessage().trim().startsWith("java.sql.SQLException: ORA-00942:"));
		} finally {
			try {
				if (conn != null) {	conn.close(); }
			} catch (SQLException e) {
				e.printStackTrace();
				fail(e.getLocalizedMessage());
			}
		}
	}

	public void testInitEnvironmentCache(){
    	UsageGroupingCache cache = new UsageGroupingCache();

    	cache.cache.clear();

		try {
			Class.forName("org.apache.commons.dbcp.PoolingDriver");
			Class.forName("oracle.jdbc.driver.OracleDriver");

			SQLConnectionManager.init(DOMUtils.openDocument("bgh/guiding/pool.xml", false));
			Element conf = DOMUtils.openDocument("bgh/guiding/caches3.xml", false);
			cache.configure(conf);

			//checking the number of elements from TEST01 db
			assertFalse( "Chamadas Locais".equals(cache.getFromCache(buildDetail("0", null, "TT11", "VCTAC", null, null, "H"))) );

			cache.initEnvironment("S2");

			//checking if the environment was switched to step2(DEVEL01 db), the element below
			//was inserted only on DEVEL01 db
			assertEquals("Chamadas Locais", cache.getFromCache(buildDetail("0", null, "TT11", "VCTAC", null, null, "H")));

		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
	}

	private TIMUsageDetail buildDetail(String _csp, String _mc, String _svc, String _tz, String _fu, String _typeInd, String _net) {
		TIMUsageDetail ud = new TIMUsageDetail();
		ud.setCarrierCode(_csp);
		ud.setMicroCellDesc(_mc);
		ud.setSvcId(_svc);
		ud.setTariff(_tz);
		// TODO
		// ud.setTypeIndicator(_typeInd);
		ud.setArea(_net);
		if (_fu != null) {
			UsageFUDetail fu = new UsageFUDetail(ud);
			fu.setPackageShortDescription(_fu);
			ud.getFreeUnitDetails().add(fu);
		}
		return ud;
	}



}
