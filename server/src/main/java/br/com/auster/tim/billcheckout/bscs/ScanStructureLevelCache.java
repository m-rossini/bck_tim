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
 * Created on Jul 23, 2009
 */
package br.com.auster.tim.billcheckout.bscs;

import java.sql.Connection;
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
public class ScanStructureLevelCache extends ReferenceDataCache{
	
	private static final Logger log = Logger.getLogger(ScanStructureLevelCache.class);
	
	protected static final String SELECT_LAZY_STATEMENT_QAB = "ScanLevelLA_QAB.loadCache";
	protected static final String SELECT_LAZY_STATEMENT_QC  = "ScanLevelLA_QC.loadCache";
	protected static final String QA = "queryCustPlan";
	protected static final String QB = "queryCustPlanServ";
	protected static final String QC = "queryCustPlanServPcte";
	private   static int index = 1;

	@Override
	/**
	 * Não criado obj setando atributos pois não os guardarei em cache, só um indicador q existe registro retornado
	 * Ainda cria instancia no retorno deste método para usar em ReferenceDataCache, do método readAndToCache desta classe.
	 */
	protected CacheableVO createVO(ResultSet _rset) throws SQLException {
		return new ScanStructureLevelVO();
	}

	/**
	 * Retorna a query com os parâmetros necessários, de forma dinâmica
	 * 
	 * @param _key
	 * @return String representando a query com todos os parametros de consulta
	 */
	protected String getLazySQLs(CacheableKey _key) {
		/*criado variavel stmtSelect, apenas para colocar em log o stmt selecionado*/
		String query = null;
		String stmtSelect = null;
		try {
			ScanStructureLevelVO.InnerKey key = (ScanStructureLevelVO.InnerKey) _key;
			SQLConnectionManager sqlConn = SQLConnectionManager.getInstance(this.poolName);
			
			if (null != key.getCustCod() && null != key.getPlanCod()){
				if (null == key.getServCod() && null == key.getPackCod()){
					/* Q6-A - Apenas custCode e COD_PLANO_LA não nulos */
					query = sqlConn.getStatement(SELECT_LAZY_STATEMENT_QAB).getStatementText();
					stmtSelect =  addStmt(QA, key.getCustCod(), key.getPlanCod(), key.getServCod(),
							 			  key.getPackCod(), query);
					return stmtSelect;
				}
				if (null != key.getServCod()){
					if(null == key.getPackCod()){
						/* Q6-B - Apenas custCode, COD_PLANO_LA e COD_SERV_LA não nulos */
						query = sqlConn.getStatement(SELECT_LAZY_STATEMENT_QAB).getStatementText();
						stmtSelect = addStmt(QB, key.getCustCod(), key.getPlanCod(), key.getServCod(),
											 key.getPackCod(), query);
						return stmtSelect;
					}
					else{/* Q6-C - custCode, COD_PLANO_LA, COD_SERV_LA e COD_PCTE_LA não nulos */
						query = sqlConn.getStatement(SELECT_LAZY_STATEMENT_QC).getStatementText();
						stmtSelect = addStmt(QC, key.getCustCod(), key.getPlanCod(), key.getServCod(),
								 			 key.getPackCod(), query);
						return stmtSelect;
					}
				}
			}
		} catch (NamingException ne) {
			log.error("Cannot load statement:" + stmtSelect, ne);
		}
		return null;
	}
	/**
	 * Metodo que adiciona o complemento da query, de acordo com os parametros de consulta passados
	 * 
	 * @param typeQuery - Query A, B ou C
	 * @param cust      - custCode do cliente
	 * @param plan 		- coluna COD_PLANO_LA
	 * @param query     - query configurada no bscs-sql.xml, com o seu CORE, para ser complementada
	 * @return query    - query final ja montada dinamicamente com todos os seus parametros necessarios
	 */
	private String addStmt(String typeQuery, String cust, String plan, String serv, String pack, String query){
		/*Query com parametros de custCode e COD_PLANO_LA - default para todos*/
		query = addVariaveis(preparedCust(cust), query);
		query += " and tm.shdes in ";
		query = addVariaveis(plan.split(","), query);
		
		if (typeQuery.equals(QB) || typeQuery.equals(QC)){
			query += " and sn.shdes = ? ";
			 if (typeQuery.equals(QC)){
					query += " and fu.short_name = ?";
			 }
		}
		return query;
	}
	/**
	 * Este método utiliza de um array de String para montar os parametros (?) em uma query passada
	 * 
	 * @param array  -> array de parametros
	 * @param querys -> query a ser adicionada os parametros
	 * @return
	 */
	private String addVariaveis (String[] array, String querys){
		querys += "(";
		for (int j=0; j< array.length; j++){
			if (j < array.length-1){
				querys += "?,";
			}else{
				querys += "?) ";
			}
		}
		return querys;
	}
	
	/**
	 * Este método concatena o custCode, separando os niveis com virgulas, e depois divide os niveis em um array 
	 * Example:
	 * tmpCust = 6.586432.00.00.100000
	 * retorno = Array contendo array[0]6.586432.00.00 array[1]6.586432.00 array[3]6.586432 - niveis (30, 20, 10).
	 * @param tmpCust -> custCode completo do BGH
	 * @return arrayCust[] -> divide em um array o custCode em niveis 30,20,10,se existi-los, qdo tmpCust for de nivel > 10.
	 */
	private String[] preparedCust(String tmpCust){
		String concatCust = "";
		while (-1 != tmpCust.indexOf(".", 2) ){
			tmpCust = tmpCust.substring(0, tmpCust.lastIndexOf("."));
			if (-1 == tmpCust.indexOf(".", 2) ){
				concatCust += tmpCust;
			}else {
				concatCust += tmpCust + ",";
			}
		}
		String[] arrayCust = concatCust.split(",");
		return arrayCust;
	}

	@Override
	protected String getNonLazySQL() {
		log.error("Cannot use this cache as lazy-cache false; only as true");
		throw new IllegalArgumentException("Cannot use this cache as lazy-cache false; only as true");
	}

	@Override
	protected String getAlternateLazySQL() {
		log.error("Cannot use this cache as use-alternate true; only as false");
		throw new IllegalArgumentException("Cannot use this cache as use-alternate true; only as false");
	}
	
	@Override
	protected void setAlternateLazySQLParameters(PreparedStatement _stmt, CacheableKey _key) throws SQLException {
		log.error("Cannot use this cache as use-alternate true; only as false");
		throw new IllegalArgumentException("Cannot use this cache as use-alternate true; only as false");
	}

	@Override
	protected void setLazySQLParameters(PreparedStatement _stmt, CacheableKey _key) throws SQLException {
		ScanStructureLevelVO.InnerKey key = (ScanStructureLevelVO.InnerKey) _key;
		String [] arrayCust = preparedCust(key.getCustCod());
		ScanStructureLevelCache.index = 1;
		if (null != key.getCustCod() && null != key.getPlanCod()){
			_stmt = setParamArrayCust(_stmt, arrayCust);
			_stmt = setParamArrayPlan(_stmt, key.getPlanCod().split(",")); 
			if (null != key.getServCod()){
				_stmt.setString(ScanStructureLevelCache.index, key.getServCod());
				ScanStructureLevelCache.index++;
				if ( null != key.getPackCod()){
					_stmt.setString(ScanStructureLevelCache.index, key.getPackCod());
				}
			}
		}
	}

	private PreparedStatement setParamArrayPlan(PreparedStatement _stmt, String[] arrayPlans) throws SQLException {
		for (int w = 0; w < arrayPlans.length; w++){
			_stmt.setString(ScanStructureLevelCache.index, arrayPlans[w]);
			ScanStructureLevelCache.index++;
		}
		return _stmt;
	}

	private PreparedStatement setParamArrayCust(PreparedStatement _stmt, String[] arrayCust) throws SQLException {
		for (int j=0; j < arrayCust.length; j++){
			_stmt.setString(ScanStructureLevelCache.index, arrayCust[j]);
			ScanStructureLevelCache.index++;
		}
		return _stmt;
	}

	@Override
	protected void loadFromDatabase(CacheableKey _key) {
		Connection conn=null;
		PreparedStatement stmt=null;
		String query = null;
		try {
			conn = this.getConnection();
			if (this.useAlternate) {
				//Não usados ate o momento, para este cache, onde teria useAlternate=true.
				query = this.getAlternateLazySQL();
				stmt = conn.prepareStatement(query);
				this.setAlternateLazySQLParameters(stmt, _key);
			} else {
				//passa a chave e retorna QA,QB ou QC.
				query = this.getLazySQLs(_key);
				if (query != null){
					stmt = conn.prepareStatement(query);
					this.setLazySQLParameters(stmt, _key);
				}else{
					//Se coluna COD_SERV_LA ==null e COD_PCTE_LA !=null, não pode estar assim, por definição do PDA.
					log.error("No query configured. Probable inconsistency DB, verify columns Plan, Service and Pack");
				}
			}
			if (query != null){
				loadIntoCache(stmt, true);
			}
		} catch (Exception e) {
			log.error("Could not load cache, executing method loadFromDatabase", e);
		} finally {
				try {
					if (stmt != null) {	stmt.close(); } 
					if (conn != null) {	conn.close(); }
				} catch (SQLException e) {
					log.error("Exception closing connections, executing loadFromDatabase", e);
				}
		}
	}
	@Override
	protected void loadIntoCache(PreparedStatement _stmt, boolean _overflowAllowed) throws SQLException {
		ResultSet rs = null;
		try {
			rs = _stmt.executeQuery();
			if (rs.next()) {
				if (!readAndToCache(rs, _overflowAllowed)) {
					return;
				}
			}
		} finally {
			try {
				if (rs != null) {	rs.close();	}
			} catch (SQLException e) {
				log.error("Exception populating cache, executing loadIntoCache", e);
			}
		}
	}
	
	protected final boolean readAndToCache(ResultSet _rset, boolean _overflowAllowed) throws SQLException {
		CacheableVO obj = createVO(_rset);
		if ((this.cache.isFull()) && (!_overflowAllowed)) {
			log.warn("Cache for key is full. Leaving the hungry populate.");
			return false;
		}
		this.putInCache((this.isUseAlternateKey())? obj.getAlternateKey() : obj.getKey(), obj);
		return true;
	}
	
	@Override
	public void putInCache(CacheableKey key, CacheableVO obj) {
		/* Ak o obj é a própria key por ser a unica variavel de instancia da classe VO, apenas simbolizando
		 * que para a varredura em niveis existe retorno(value==true)*/
		cache.put(key, obj);
		log.debug("Cache has size of:" + this.cache.size() + " and a max size of:"+ this.cache.maxSize() +
					   ".Obj:[" + obj + "]");
	}

	@Deprecated
	protected String getLazySQL() {
		log.error("Cannot use this method getLazySQL(); only the method getLazySQLs(key)");
		throw new IllegalArgumentException("Cannot use this method getLazySQL(); only the method getLazySQLs(key)");
	}
}
