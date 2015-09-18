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
 * Created on 07/04/2008
 */
package br.com.auster.tim.billcheckout.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import br.com.auster.common.sql.SQLConnectionManager;
import br.com.auster.common.sql.SQLStatement;
import br.com.auster.common.xml.DOMUtils;
import br.com.auster.om.reference.facade.ConfigurationException;

/**
 * This class have some helper methods to run R03.6 rule.
 *
 * @author framos
 * @version $Id$
 *
 */
public final class FreeUnitHelper {


	private static final String PACKAGES_HAVE_IDENTICAL_SCENARIOS_SQL = "FreeUnitHelper.AnyNonMatchingScenario";
	private static final String DETERMINE_CONSUMPTION_ORDER_SQL = "FreeUnitHelper.ConsumptionOrder";

	private static final Logger log = Logger.getLogger(FreeUnitHelper.class);

	protected static final String DB_ELEMENT = "database";
	protected static final String POOL_NAME_ATTR = "pool-name";

	protected static final String ALTERNATE_POOL_NAME_ELEMENT = "alternate-pool";
	protected static final String ALTERNATE_POOL_NAME_ATTR = "name";
	protected static final String ALTERNATE_POOL_VALUE_ATTR = "value";

	protected String poolName;
	protected String defaultPoolName;
	protected Map<String, String> alternatePoolNames;

	protected Object changeEnvToken;



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

	/**
	 * Identifies if the two specified packages (their short descriptions) share the same free unit scenarios.
	 * This is done by running a SQL query at the BSCS database.
	 * <p>
	 * This method does not consider the order in which the packages are specified in the parameters.
	 *
	 * @param _shortDesc1 the short description for the first package
	 * @param _shortDesc2 the short description for the second package
	 *
	 * @return if the two packages share all the same scenarios
	 */
	public boolean packagesHaveIdenticalScenarios(String _shortDesc1, String _shortDesc2) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			SQLConnectionManager sqlManager = SQLConnectionManager.getInstance(this.poolName);
			if (sqlManager == null) {
				log.error("Cannot find configured pool " + this.poolName);
			} else {
				conn = sqlManager.getConnection();
				SQLStatement sql = sqlManager.getStatement(PACKAGES_HAVE_IDENTICAL_SCENARIOS_SQL);
				stmt = conn.prepareStatement(sql.getStatementText());
				stmt.setString(1, _shortDesc1);
				stmt.setString(2, _shortDesc2);
				rset = stmt.executeQuery();
				// returning if any row was found
				return (!rset.next());
			}
		} catch (Exception e) {
			log.error("Exception while running query ", e);
		} finally {
			closeAll(conn, stmt, rset);
		}
		return false;
	}


	/**
	 * Determines which of the two packages should be consumed first. The returning string is the short
	 * 	description of such package, but if <code>NULL</code> is returned then both packages have the
	 * 	same priority and the free units can be consumed in any order.
	 *
	 * @param _shortDesc1 the short description for the first package
	 * @param _shortDesc2 the short description for the second package
	 *
	 * @return The short description of the first package to be consumed or <code>NULL</code> if they have
	 * 	the same priority
	 */
	public String determineConsumptionOrder(String _shortDesc1, String _shortDesc2) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			String[][] orderInfo = new String[2][3];
			SQLConnectionManager sqlManager = SQLConnectionManager.getInstance(this.poolName);
			if (sqlManager == null) {
				log.error("Cannot find configured pool " + this.poolName);
			} else {
				conn = sqlManager.getConnection();
				SQLStatement sql = sqlManager.getStatement(DETERMINE_CONSUMPTION_ORDER_SQL);
				stmt = conn.prepareStatement(sql.getStatementText());
				stmt.setString(1, _shortDesc1);
				stmt.setString(2, _shortDesc2);
				rset = stmt.executeQuery();
				// returning if any row was found
				for (int i = 0; (i < 2) && rset.next(); i++) {
					orderInfo[i][0] = rset.getString(1);
					orderInfo[i][1] = rset.getString(2);
					orderInfo[i][2] = rset.getString(3);

				}
				// if first package found, then match with second
				if (orderInfo[0][0] != null) {
					// if second package not found, then return the first one
					if (orderInfo[1][0] == null) {
						return (String) orderInfo[1][0];
					// if second package found, check if one of the two attributes were different
					} else if (! (orderInfo[0][1].equals(orderInfo[1][1]) &&
							      orderInfo[0][2].equals(orderInfo[1][2])) ) {
							return (String) orderInfo[0][0];
					}
				}
			}
		} catch (Exception e) {
			log.error("Exception while running query ", e);
		} finally {
			closeAll(conn, stmt, rset);
		}
		// returns NULL if no package was found, or if both were found and they share the same values for
		//   attributes [1] and [2] in the array.
		return null;
	}


	/**
	 * Used to help closing all database resources
	 */
	private void closeAll(Connection _conn, Statement _stmt, ResultSet _rset) {
		try {
			if (_rset != null) { _rset.close(); }
		} catch (SQLException sqle) {
			log.error("Could not close result set", sqle);
		}
		try {
			if (_stmt != null) { _stmt.close(); }
		} catch (SQLException sqle) {
			log.error("Could not close statement", sqle);
		}
		try {
			if (_conn != null) { _conn.close(); }
		} catch (SQLException sqle) {
			log.error("Could not close database connection", sqle);
		}
	}

}
