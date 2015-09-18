/**
 * Copyright (c) 2004-2009 Auster Solutions. All Rights Reserved.
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
 * Created on Jul 7, 2009
 */
package br.com.auster.tim.billcheckout.bscs;

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
 * @author Nardo
 * @version $Id$
 * @since JDK1.4
 */
public class ContractExistsPackCache extends ReferenceDataCache {
	
	private static final Logger log = Logger.getLogger(ContractExistsPackCache.class);
	protected static final String SELECT_ALTERNATE_LAZY_STATEMENT = "VerifyPcteIndiv.loadCache";

	@Override
	protected CacheableVO createVO(ResultSet rs) throws SQLException {
		if (rs == null){	return null; }
		
		ContractExistsPackVO vo = new ContractExistsPackVO();
		vo.setShortNamePack(rs.getString(1));
		vo.setLongName(rs.getString(2));
				
		return vo;
	}

	@Override
	protected String getAlternateLazySQL() {
		try {
			SQLConnectionManager sqlConn = SQLConnectionManager.getInstance(this.poolName);
			return sqlConn.getStatement(SELECT_ALTERNATE_LAZY_STATEMENT).getStatementText();
		} catch (NamingException ne) {
			log.error("Cannot load statement:" + SELECT_ALTERNATE_LAZY_STATEMENT, ne);
		}
		return null;
	}

	@Override
	protected String getLazySQL() {
		log.error("Cannot use this cache as use-alternate false; only as true");
		throw new IllegalArgumentException("Cannot use this cache as use-alternate false; only as true");
	}

	@Override
	protected String getNonLazySQL() {
		log.error("Cannot use this cache as lazy-cache false");
		throw new IllegalArgumentException("Cannot use this cache as lazy-cache false; only as true");
	}

	@Override
	protected void setAlternateLazySQLParameters(PreparedStatement _stmt, CacheableKey _key) throws SQLException {
		ContractExistsPackVO.InnerAlternateKey key = (ContractExistsPackVO.InnerAlternateKey) _key;
		_stmt.setLong(1, key.getPrmValue() );
		_stmt.setString(2, key.getShortName());
		_stmt.setDate(3, new java.sql.Date(key.getDateCut().getTime()));
	}

	@Override
	protected void setLazySQLParameters(PreparedStatement stmt,	CacheableKey key) throws SQLException {
		log.error("Cannot use this cache as use-alternate false; only as true");
		throw new IllegalArgumentException("Cannot use this cache as use-alternate false; only as true");
	}

}
