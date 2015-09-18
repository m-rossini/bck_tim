package br.com.auster.tim.billcheckout.param;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import junit.framework.TestCase;

import org.w3c.dom.Element;

import br.com.auster.common.sql.SQLConnectionManager;
import br.com.auster.common.xml.DOMUtils;

public class CBCFContractDAOTest extends TestCase {

	public void testInitEnvironmentConnection(){
		CBCFContractDAO dao = new CBCFContractDAO();

		Connection conn = null;
		DatabaseMetaData dbMetaData;
		try {
			Class.forName("org.apache.commons.dbcp.PoolingDriver");
			Class.forName("oracle.jdbc.driver.OracleDriver");

			SQLConnectionManager.init(DOMUtils.openDocument("bgh/guiding/pool.xml", false));
			Element conf = DOMUtils.openDocument("bgh/guiding/caches3.xml", false);
			dao.configure(conf);

			conn = SQLConnectionManager.getInstance(dao.getPoolName()).getConnection();
			dbMetaData = conn.getMetaData();

			assertEquals("jdbc:oracle:thin:@MCCOY:1521:TEST01", dbMetaData.getURL());
			assertEquals("TIM_BCK", dbMetaData.getUserName());
			conn.close();

			dao.initEnvironment("S2");
			conn = SQLConnectionManager.getInstance(dao.getPoolName()).getConnection();
			dbMetaData = conn.getMetaData();
			assertEquals("jdbc:oracle:thin:@MCCOY:1521:DEVEL01", dbMetaData.getURL());
			assertEquals("TIM_BCK", dbMetaData.getUserName());
			conn.close();

			dao.initEnvironment("S3");
			conn = SQLConnectionManager.getInstance(dao.getPoolName()).getConnection();
			dbMetaData = conn.getMetaData();
			assertEquals("jdbc:oracle:thin:@MCCOY:1521:DEVEL01", dbMetaData.getURL());
			assertEquals("INTERDW", dbMetaData.getUserName());

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
