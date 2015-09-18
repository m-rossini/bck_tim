package br.com.auster.tim.billcheckout.bscs;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

import junit.framework.TestCase;

import org.w3c.dom.Element;

import br.com.auster.common.sql.SQLConnectionManager;
import br.com.auster.common.xml.DOMUtils;

public class RateplanHistTest extends TestCase {

	public void testInitEnvironmentConnection(){
		RateplanHistCache cache = new RateplanHistCache();

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		Connection conn = null;
		DatabaseMetaData dbMetaData;
		try {
			Class.forName("org.apache.commons.dbcp.PoolingDriver");
			Class.forName("oracle.jdbc.driver.OracleDriver");

			SQLConnectionManager.init(DOMUtils.openDocument("bgh/guiding/pool.xml", false));
			Element conf = DOMUtils.openDocument("bgh/guiding/caches.xml", false);
			cache.configure(conf);

			RateplanHistVO vo = (RateplanHistVO) cache.getFromCache(RateplanHistVO.createKey("6262940"));
			assertNotNull(vo);
			assertEquals("6262940", vo.getContractNumber());
			List<Integer> tmcodes = vo.getTMCodes(sdf.parse("01/04/2008"), sdf.parse("30/04/2008"));
			assertNotNull(tmcodes);
			assertEquals(1, tmcodes.size());
			assertEquals(354, tmcodes.get(0).intValue());
			tmcodes = vo.getTMCodes(sdf.parse("01/05/2008"), sdf.parse("30/05/2008"));
			assertNotNull(tmcodes);
			assertEquals(2, tmcodes.size());

		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		} finally {
			try {
				if (conn != null) {	conn.close(); }
			} catch (SQLException e) {
				e.printStackTrace();
				fail(e.getLocalizedMessage());
			}
		}
	}

}
