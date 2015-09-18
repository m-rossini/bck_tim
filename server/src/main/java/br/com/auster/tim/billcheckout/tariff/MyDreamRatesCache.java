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
 * Created on 20/04/2010
 */
package br.com.auster.tim.billcheckout.tariff;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.NamingException;

import org.apache.log4j.Logger;

import br.com.auster.billcheckout.caches.CacheableKey;
import br.com.auster.billcheckout.caches.CacheableVO;
import br.com.auster.billcheckout.caches.ReferenceDataCache;
import br.com.auster.common.sql.SQLConnectionManager;

/**
 * @author anardo
 * @version $Id$
 * @since JDK1.4
 */
public class MyDreamRatesCache extends ReferenceDataCache {
	
	private static final String SELECT_LAZY_STATEMENT = "MyDreamRatesCache.select";
	
	private static final Logger log = Logger.getLogger(MyDreamRatesCache.class);

	
	
	@Override
	protected CacheableVO createVO(ResultSet _rset) throws SQLException {
		if (_rset == null){ return null; }

		int col=1;
		MyDreamRatesVO cached = new MyDreamRatesVO();
		cached.setTariffZoneCode(_rset.getString(col++));
		cached.setState(_rset.getString(col++));
		cached.setPackageName(_rset.getString(col++));
		cached.setPPM(_rset.getDouble(col++));		
		return cached;
	}

	@Override
	protected String getAlternateLazySQL() {
		throw new NoSuchMethodError("Must run with lazy implementations.");
	}

	@Override
	protected String getLazySQL() {
		try {
			return SQLConnectionManager.getInstance(poolName).getStatement(SELECT_LAZY_STATEMENT).getStatementText();
		} catch (NamingException e) {
			log.error("Could not load sql statement" + e);
		}
		return null;
	}

	@Override
	protected String getNonLazySQL() {
		throw new NoSuchMethodError("Must run with lazy implementations.");
	}

	@Override
	protected void setAlternateLazySQLParameters(PreparedStatement _stmt, CacheableKey _key) throws SQLException { }

	@Override
	protected void setLazySQLParameters(PreparedStatement _stmt, CacheableKey _key) throws SQLException {
		MyDreamRatesVO.InnerKey key = (MyDreamRatesVO.InnerKey) _key;
		int col = 1;
		_stmt.setString(col++, key.getTariffZone());
		_stmt.setString(col++, key.getState());
		_stmt.setString(col++, key.getPackageName());
	}
}
