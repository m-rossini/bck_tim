/*
 * Copyright (c) 2004-2007 Auster Solutions. All Rights Reserved.
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
 * Created on 04/10/2007
 */
package br.com.auster.tim.billcheckout.param;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import br.com.auster.common.sql.SQLConnectionManager;
import br.com.auster.common.sql.SQLStatement;
import br.com.auster.common.xml.DOMUtils;
import br.com.auster.om.reference.facade.ConfigurationException;

/**
 * This class loads all possible descriptions for OCC
 *
 * @author Frederico Ramos
 * @version $Id$
 * @since JDK1.4
 */
public class CBCFContractDAO  {


	private static final Logger log = Logger.getLogger(CBCFContractDAO.class);

	protected static final String DB_ELEMENT = "database";
	protected static final String POOL_NAME_ATTR = "pool-name";
	protected static final String ALTERNATE_POOL_NAME_ELEMENT = "alternate-pool";
	protected static final String ALTERNATE_POOL_NAME_ATTR = "name";
	protected static final String ALTERNATE_POOL_VALUE_ATTR = "value";

	protected String poolName;
	protected String defaultPoolName;
	protected Map<String, String> alternatePoolNames;

	protected Object changeEnvToken;

	protected static final String SELECT_STATEMENT = "CBCFContractDAO.loadCache";



	/**
	 *
	 * @param config
	 * @throws ConfigurationException
	 */
	public void configure(Element config) throws ConfigurationException {

		log.debug("Configuring cache instance for " + this.getClass().getSimpleName() + " instances.");
		Element dbElement = DOMUtils.getElement(config, DB_ELEMENT, true);
		poolName = DOMUtils.getAttribute(dbElement, POOL_NAME_ATTR, true);
		if ((poolName == null) || (poolName.trim().length() == 0)) {
			throw new ConfigurationException("pool-name was not informed.");
		}
		//sets default pool-name(it is important keeping that on a separated variable, because it can be used later)
		defaultPoolName = poolName;

		//creating the alternate pools map
		this.alternatePoolNames = new HashMap<String,String>();
		NodeList altPoolNodeList = DOMUtils.getElements(dbElement, ALTERNATE_POOL_NAME_ELEMENT);
		Element altPoolElement;
		String key;
		String value;
		for (int i = 0; i < altPoolNodeList.getLength(); i++) {
			altPoolElement = (Element) altPoolNodeList.item(i);
			key = DOMUtils.getAttribute(altPoolElement, ALTERNATE_POOL_NAME_ATTR, true);
			value = DOMUtils.getAttribute(altPoolElement, ALTERNATE_POOL_VALUE_ATTR, true);
			this.alternatePoolNames.put(key, value);
		}

	}

	/**
	 * Inits the environment changing the used pool depending on
	 * the current changeEnvironment token. In this case it is used the
	 * step number as token.
	 *
	 * @param _changeEnvToken
	 */
	public void initEnvironment(String _changeEnvToken) {
		if ((this.changeEnvToken == null) || (_changeEnvToken == null) || (!this.changeEnvToken.equals(_changeEnvToken))) {
			log.debug("New changeEnvironment token found.");
			this.changeEnvToken = _changeEnvToken;
			poolName = (String)this.alternatePoolNames.get(_changeEnvToken);
			if ((poolName == null) || (poolName.trim().length() == 0)) {
				log.warn("Alternate pool-name was not informed, it will be used the default pool-name.");
				poolName = defaultPoolName;
			}
			this.changeEnvToken = _changeEnvToken;
		}
	}

	/**
	 * Returns the configured JDBC pool name
	 */
	public String getPoolName() {
		return this.poolName;
	}

	/**
	 * Returns the configured JDBC alternate pool names
	 */
	public Map<String, String> getAlternatePoolNames() {
		return this.alternatePoolNames;
	}

	public Map<Double, Double> getCBCFCustomerList(String _customerId, Date _startDate, Date _endDate) {
		Map<Double, Double> info = new HashMap<Double, Double>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			conn = SQLConnectionManager.getInstance(this.poolName).getConnection();
			SQLStatement sql = SQLConnectionManager.getInstance(this.poolName).getStatement(SELECT_STATEMENT);
			stmt = conn.prepareStatement(sql.getStatementText());
			this.setParameters(stmt, _customerId, _startDate, _endDate);
			rset = stmt.executeQuery();
			while (rset.next()) {
				info.put( new Double(rset.getDouble(1)), new Double(rset.getDouble(2)) );
			}
		} catch (NamingException ne) {
			log.error("Could not load CBCF information ", ne);
		} catch (SQLException sqle) {
			log.error("Could not load CBCF information ", sqle);
		} finally {
			try {
				if (rset != null) { rset.close(); }
			} catch (Exception sqle) {
				log.error("Cannot close resultset ", sqle);
			}
			try {
				if (stmt != null) { stmt.close(); }
			} catch (Exception sqle) {
				log.error("Cannot close statment ", sqle);
			}
			try {
				if (conn != null) { conn.close(); }
			} catch (Exception sqle) {
				log.error("Cannot close connection ", sqle);
			}
		}
		return info;
	}

	private void setParameters(PreparedStatement _stmt, String _customerId, Date _startDate, Date _endDate) throws SQLException  {
		// before union
		_stmt.setString(1, _customerId);
		_stmt.setDate(2, new java.sql.Date(_startDate.getTime()));
		_stmt.setDate(3, new java.sql.Date(_endDate.getTime()));
		// after union
		_stmt.setString(4, _customerId);
		_stmt.setDate(5, new java.sql.Date(_startDate.getTime()));
		_stmt.setDate(6, new java.sql.Date(_endDate.getTime()));
	}

}
