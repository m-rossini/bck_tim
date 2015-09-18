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
package br.com.auster.tim.billcheckout.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.naming.NamingException;

import org.apache.commons.collections.map.LRUMap;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import br.com.auster.common.sql.SQLConnectionManager;
import br.com.auster.common.xml.DOMUtils;
import br.com.auster.om.reference.facade.ConfigurationException;
import br.com.auster.tim.om.invoice.TIMUsageDetail;



/**
 * @author framos
 * @version $Id$
 *
 */
public class UsageGroupingCache  {


	private static final Logger log = Logger.getLogger(UsageGroupingCache.class);

	protected static final int BGH_GUIDING_LEN = 7;
	protected static final int DEFAULT_CACHE_SIZE = 1000;
	protected static final String STATEMENT_SQL = "UsageGroupingCache.loadCache";

	protected static final String DB_ELEMENT = "database";
	protected static final String POOL_NAME_ATTR = "pool-name";
	protected static final String CACHE_SIZE_ATTR = "cache-size";
	protected static final String ALTERNATE_POOL_NAME_ELEMENT = "alternate-pool";
	protected static final String ALTERNATE_POOL_NAME_ATTR = "name";
	protected static final String ALTERNATE_POOL_VALUE_ATTR = "value";

	// instance attributes
	protected static Map cache = new HashMap();
//	protected static final Map cache = new HashMap();
	protected static LRUMap foundKeys = null;
	protected static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	protected String poolName;
	protected String defaultPoolName;
	protected Map<String, String> alternatePoolNames = new HashMap<String, String>();
	protected Object changeEnvToken;


	/**
	 * Configures the database pool
	 */
	public void configure(Element config) throws ConfigurationException {
		try {
			lock.writeLock().lockInterruptibly();
			// configuring pool name
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

			// initializing cache
			int cacheSize = DOMUtils.getIntAttribute(config, CACHE_SIZE_ATTR, false);
			if (cacheSize < 1) {
				cacheSize = DEFAULT_CACHE_SIZE;
			}
			// if cache already init.ed, then skip loading
			if (cache.size() > 0) {
				return;
			}
			foundKeys = new LRUMap(cacheSize);
			loadCache();
		} catch (InterruptedException ie) {
			log.error(ie);
			throw new ConfigurationException(ie);
		} catch (SQLException sqle) {
			log.error(sqle);
			throw new ConfigurationException(sqle);
		} catch (NamingException ne) {
			log.error(ne);
			throw new ConfigurationException(ne);
		} finally {
			lock.writeLock().unlock();
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
		try {
			if ((this.changeEnvToken == null) || (_changeEnvToken == null) || (!this.changeEnvToken.equals(_changeEnvToken))) {
				log.debug("New changeEnvironment token found.");
				try {
					String tempPoolName = (String)this.alternatePoolNames.get(_changeEnvToken);
					lock.writeLock().lockInterruptibly();
					// specified pool-name was not found
					boolean refreshCache = true;
					if ((tempPoolName == null) || (tempPoolName.trim().length() == 0)) {
						tempPoolName = defaultPoolName;
						// if we already use the default, then skip refresh
						if (tempPoolName.equals(this.poolName)) {
							log.debug("Default pool-name already set. Continuing to use it.");
							refreshCache = false;
						} else {
							// refreshing cache to use default pool-name
							log.warn("Alternate pool-name was not informed, it will be used the default pool-name.");
							this.poolName = defaultPoolName;
						}
					} else {
						// specified pool-name was found
						this.poolName = tempPoolName;
					}
					this.changeEnvToken = _changeEnvToken;
					if (refreshCache) {
						refreshCache();
						loadCache();
					}
				} catch (InterruptedException ie) {
					log.error(ie);
				} finally {
					lock.writeLock().unlock();
				}
			}
		} catch (Exception e) {
			log.error(e);
			throw new ConfigurationException(e);
		}
	}

	/**
	 */
	private void loadCache() throws SQLException, NamingException {
   		Connection c = null;
   		Statement s = null;
   		ResultSet r = null;
   		try {
   			c = SQLConnectionManager.getInstance(this.poolName).getConnection();
	    	s = c.createStatement();
	    	r = s.executeQuery( SQLConnectionManager.getInstance(this.poolName).getStatement(STATEMENT_SQL).getStatementText() );

    		log.debug("Starting to load cache.");
    		while (r.next()) {
    			addToCache(cache, r, 1, BGH_GUIDING_LEN);
    		}
    		log.debug("Finished loading cache");
   		} finally {
			if (r != null) { r.close(); }
			if (s != null) { s.close(); }
			if (c != null) { c.close(); }
   		}
	}

	public String getFromCache(TIMUsageDetail _detail) {
		String[] keys = buildUsageKeys(_detail);
		String keyAsString = buildUsageKeyAsString(keys);
		try {
			lock.readLock().lockInterruptibly();
			if (!foundKeys.containsKey(keyAsString)) {
				String found = getFromCache(cache, keys, 0);
				foundKeys.put(keyAsString, found);
				log.debug("Key " + keyAsString +  " resulted in '" + found + "' value");
			}
		} catch (InterruptedException ie) {
			log.error(ie);
		} finally {
			lock.readLock().unlock();
		}
		return (String) foundKeys.get(keyAsString);
	}




	private void refreshCache() {
		cache.clear();
		foundKeys.clear();
	}

	private String getFromCache(Map _cache, String[] _keys, int _pos) {
		try {
			lock.readLock().lockInterruptibly();
			log.debug("Looking for key " + _keys[_pos] + " under index of " + _pos);
			// There is no such entry
			if ((_cache == null) || (_cache.size() < 0)) {
				log.debug("Cache has no such entry");
				return null;
			// If we reached the last cache level
			} else if (_pos == (_keys.length-1)) {
				if (_cache.containsKey(_keys[_pos])) {
					return (String) _cache.get(_keys[_pos]);
				} else {
					return (String) _cache.get(null);
				}
			}
			// We still have more than one entry for such key!
			// If we have the key in the map, then search using it
			if (_cache.containsKey(_keys[_pos])) {
				log.debug("Going deeper into cache using key value of " + _keys[_pos]);
				String found = getFromCache((Map)_cache.get(_keys[_pos]), _keys, _pos+1);
				if (found == null) {
					log.debug("Went down but key was not found. Trying againg with NULL key");
					found = getFromCache((Map)_cache.get(null), _keys, _pos+1);
				}
				return found;
			// Else we should search as NULL
			} else {
				log.debug("Key not found. Going deeper into cache using NULL key");
				return getFromCache((Map)_cache.get(null), _keys, _pos+1);
			}
		} catch (InterruptedException ie) {
			log.error(ie);
		} finally {
			lock.readLock().unlock();
		}
		return null;
	}

	private final String[] buildUsageKeys(TIMUsageDetail _details) {
		String[] keys = new String[BGH_GUIDING_LEN];
		// CSP_ID
		keys[0] = _details.getCarrierCode();
		// MC
		keys[1] = _details.getMicroCellDesc();
		// Service
		keys[2] = _details.getSvcId();
		// TZ
		keys[3] = _details.getTariff();
		// FU_PACK_ID
		keys[4] = ( _details.getFreeUnitDetails().size() == 0 ? "null" : _details.getFreeUnitDetails().get(0).getPackageShortDescription());
		// TYPE_IND
		// TODO add this info when BGH is modified
		keys[5] = null;
		// NETWORK_IND
		keys[6] = _details.getArea();

		log.debug("Looking for detail with keys : " + buildUsageKeyAsString(keys) );
		return keys;
	}

	private final String buildUsageKeyAsString(String[] _keys) {
	return _keys[0] + "/" + _keys[1] + "/" + _keys[2] + "/" + _keys[3] + "/" + _keys[4] + "/" + _keys[5] + "/" + _keys[6];
	}

	private final void addToCache(Map _cache, ResultSet _r, int _keyPos, int _maxPos) throws SQLException {
		String key = _r.getString(_keyPos);
		log.debug( printInfo(key, _keyPos) );
		if (!_cache.containsKey(key)) {
			if (_keyPos != _maxPos) {
				_cache.put(key, new HashMap());
			}
		}
		if (_keyPos == _maxPos) {
			String value = _r.getString(_keyPos+1);
			_cache.put(key, value);
			log.debug("                Added value " + value + " for 8th key " + key);
		} else {
			addToCache((Map)_cache.get(key), _r, _keyPos+1, _maxPos);
		}

	}

	private final String printInfo(String _key, int _pos) {
		StringBuffer bf = new StringBuffer();
		for (int i=0; i < _pos; i++) {
			bf.append("  ");
		}
		bf.append("Defining key ");
		bf.append(_key);
		return bf.toString();
	}

	public String getPoolName() {
		return poolName;
	}

	public String getDefaultPoolName() {
		return defaultPoolName;
	}

	public Map<String, String> getAlternatePoolNames() {
		return alternatePoolNames;
	}
}
