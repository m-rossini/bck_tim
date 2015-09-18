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
 * Created on 26/10/2007
 */
package br.com.auster.tim.billcheckout.param;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import br.com.auster.billcheckout.caches.CacheableKey;
import br.com.auster.common.sql.SQLConnectionManager;
import br.com.auster.common.xml.DOMUtils;
import br.com.auster.om.reference.facade.ConfigurationException;
/**
 * TODO What this class is responsible for
 *
 * @author William Soares
 * @version $Id$
 * @since JDK1.4
 */
public class ContractServicesDAO {

	private static final Logger log = Logger.getLogger(ContractServicesVO.class);

	protected static final String DB_ELEMENT = "database";
	protected static final String POOL_NAME_ATTR = "pool-name";
	protected static final String ALTERNATE_POOL_NAME_ELEMENT = "alternate-pool";
	protected static final String ALTERNATE_POOL_NAME_ATTR = "name";
	protected static final String ALTERNATE_POOL_VALUE_ATTR = "value";

	protected String poolName;
	protected String defaultPoolName;
	protected Map<String, String> alternatePoolNames;

	protected Object changeEnvToken;

	protected static final String SELECT_STATEMENT = "ContractServicesDAO.selectService";


	/**
	 *
	 * @param config
	 * @throws ConfigurationException
	 */
	public void configure(Element config) throws ConfigurationException {

		log.debug("Configuring DAO instance for " + this.getClass().getSimpleName() + " instances.");
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
	 *
	 * @param _key
	 * @return
	 */
	public List<ContractServicesVO> getContractServicesList(CacheableKey _key) {
		Connection conn=null;
		PreparedStatement stmt=null;
		List<ContractServicesVO> contractServicesList = new ArrayList<ContractServicesVO>();
		try {
			SQLConnectionManager sqlConn = SQLConnectionManager.getInstance(this.poolName);
			conn = sqlConn.getConnection();
			stmt = conn.prepareStatement(sqlConn.getStatement(SELECT_STATEMENT).getStatementText());
			this.setLazySQLParameters(stmt, _key);
			contractServicesList = loadIntoVO(stmt);
		} catch (Exception e) {
			log.error("Could not load non-lazy cache", e);
		} finally {
				try {
					if (stmt != null) {	stmt.close(); }
					if (conn != null) {	conn.close(); }
			} catch (SQLException e) {
				log.error("Exception populating cache", e);
			}
		}

		return contractServicesList;
	}

	/**
	 *
	 * TODO what this method is responsible for
	 *
	 * @param _stmt
	 * @param _overflowAllowed
	 * @return
	 * @throws SQLException
	 */
	private List loadIntoVO(PreparedStatement _stmt) throws SQLException {

		ResultSet rs = null;
		ContractServicesVO vo;
		List<ContractServicesVO> contractServicesVOList;

		try {
			rs = _stmt.executeQuery();
			contractServicesVOList = new ArrayList<ContractServicesVO>();
			while (rs.next()) {
				vo = new ContractServicesVO();
				vo.setContractNumber(rs.getString(1));
				vo.setSpCode(rs.getLong(2));
				vo.setSnCode(rs.getString(3));
				vo.setTmCode(rs.getLong(4));
				vo.setCsStatChng(rs.getString(5));
				vo.setPrmValueId(rs.getLong(7));
				vo.setServiceShDes(rs.getString(8));
				contractServicesVOList.add(vo);
			}
		} finally {
			try {
				if (rs != null) {	rs.close();	}
			} catch (SQLException e) {
				log.error("Exception populating cache", e);
			}
		}

		return contractServicesVOList;
	}

	/**
	 * @see br.com.auster.billcheckout.caches.ReferenceDataCache#setLazySQLParameters(PreparedStatement, CacheableKey)
	 */
	private void setLazySQLParameters(PreparedStatement _stmt, CacheableKey _key) throws SQLException {
		ContractServicesVO.InnerKey key = (ContractServicesVO.InnerKey) _key;
		_stmt.setString(1, key.getContractNumber());
	}

}