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
 * Created on Jun 25, 2009
 */
package br.com.auster.tim.billcheckout.param;

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
 * TODO Classe que faz Cache para consulta de existência do Serviço no Contrato
 *
 * @author Nardo
 * @version $Id$
 * @since JDK1.4
 */
public class CustomerSituationCache extends ReferenceDataCache {
	
	private static final Logger log = Logger.getLogger(CustomerSituationCache.class);
	
	protected static final String SELECT_ALTERNATE_LAZY_STATEMENT = "CustomerSituation.select";

	@Override
	protected CacheableVO createVO(ResultSet _rset) throws SQLException {
		if (_rset == null){	return null; }
		
		CustomerSituationVO vo = new CustomerSituationVO();
		vo.setContractId(_rset.getString(1));
		vo.setInsertDate(_rset.getDate(2));
		return vo;
	}

	@Override
	protected String getAlternateLazySQL() {
		log.error("Cannot use this cache as use-alternate false; only as true");
		throw new IllegalArgumentException("Cannot use this cache as use-alternate false; only as true");
	}

	@Override
	protected String getLazySQL() {
		try {
			SQLConnectionManager sqlConn = SQLConnectionManager.getInstance(this.poolName);
			return sqlConn.getStatement(SELECT_ALTERNATE_LAZY_STATEMENT).getStatementText();
		} catch (NamingException ne) {
			log.error("Cannot load statement:" + SELECT_ALTERNATE_LAZY_STATEMENT, ne);
		}
		return null;
	}

	@Override
	protected String getNonLazySQL() {
		log.error("Cannot use this cache as lazy-cache false");
		throw new IllegalArgumentException("Cannot use this cache as lazy-cache false; only as true");
	}

	@Override
	protected void setAlternateLazySQLParameters(PreparedStatement _stmt, CacheableKey _key) throws SQLException { 
		log.error("Cannot use this cache as use-alternate false; only as true");
		throw new IllegalArgumentException("Cannot use this cache as use-alternate false; only as true");
	}

	@Override
	protected void setLazySQLParameters(PreparedStatement _stmt, CacheableKey _key) throws SQLException {
		CustomerSituationVO.InnerKey key = (CustomerSituationVO.InnerKey) _key;
		_stmt.setString(1, key.getContractId()); 
	}

}
