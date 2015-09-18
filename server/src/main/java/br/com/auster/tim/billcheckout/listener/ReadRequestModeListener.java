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
 * Created on 27/08/2007
 */
package br.com.auster.tim.billcheckout.listener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import org.apache.log4j.Logger;

import br.com.auster.billcheckout.listeners.BootstrapListener;
import br.com.auster.common.sql.SQLConnectionManager;
import br.com.auster.dware.Bootstrap;
import br.com.auster.dware.graph.Request;

/**
 * @author framos
 * @version $Id$
 *
 */
public class ReadRequestModeListener extends BootstrapListener {

	
	private static final Logger log = Logger.getLogger(ReadRequestModeListener.class);
	
	public static final String SELECT_MODEID_SQL = 
		"select info_value from web_request_info where info_key = 'mode.id' and  web_request_id = ? ";

	
	
	
	
	/**
	 * @see br.com.auster.dware.BootstrapListener#onProcess(br.com.auster.dware.Bootstrap, java.lang.String, java.util.Map, java.util.List, java.util.Collection)
	 */
	public void onProcess(Bootstrap _bootstrap, String _chainName, Map _args, List _desiredRequests, Collection _requests) {
		super.onProcess(_bootstrap, _chainName, _args, _desiredRequests, _requests);
		
		if (_requests == null) {
			log.warn("No requests to process.");
			return;
		}
		// finding mode in database
		Request request = (Request) _requests.iterator().next();
		String modeId = this.findMode(request.getTransactionId());
		if (modeId == null) {
			log.warn("Transaction " + request.getTransactionId() + " does not have a 'mode.id' attribute configured");
			modeId = "";
		}
		// adding mode to requests
		for (Iterator<Request> it = _requests.iterator(); it.hasNext(); ) {
			request = it.next();
			request.getAttributes().put("mode.id", modeId);
		}
	}

	
	
	private String findMode(String _transactionId) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			SQLConnectionManager connManager = SQLConnectionManager.getInstance(this.poolName);
			if (connManager == null) {
				log.error("Database pool " + this.poolName + " not configured.");
				return null;
			}
			conn = connManager.getConnection();
			stmt = conn.prepareStatement(SELECT_MODEID_SQL);
			stmt.setLong(1, Long.parseLong(_transactionId));
			rset = stmt.executeQuery();
			if (rset.next()) {
				return rset.getString(1);
			}
		} catch (NamingException ne) {
			log.error("Error running query to find mode.id", ne);
		} catch (SQLException sqle) {
			log.error("Error running query to find mode.id", sqle);
		} finally {
			try {
				if (rset != null) { rset.close(); }
				if (stmt != null) { stmt.close(); }
				if (conn != null) { conn.close(); }
			} catch (SQLException sqle) {
				log.error("Could not release database resources", sqle);
			}
		}
		// if no mode.id found, then return null;
		return null;
	}
}
