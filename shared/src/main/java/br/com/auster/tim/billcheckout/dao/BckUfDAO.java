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
 * Created on 15/08/2008
 */
package br.com.auster.tim.billcheckout.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author wsoares
 * @version $Id$
 *
 */
public class BckUfDAO {


	private static final Logger log = Logger.getLogger(BckUfDAO.class);

	public static final String SELECT_ALL_UF = "SELECT DISTINCT GEO_STATE FROM BCK_GEO_DM ORDER BY GEO_STATE ASC";


	public List getUfs(Connection _conn) throws SQLException {
		PreparedStatement stmt = null;
		try {
			// removing initial assignments
			stmt = _conn.prepareStatement(SELECT_ALL_UF);
			ResultSet rs = stmt.executeQuery();
			
			List ufs = new ArrayList(); 
			
			while(rs.next()) {
				ufs.add(rs.getString("GEO_STATE"));
			}

			log.debug("Loaded " + ufs.size() + " rows from BCK_GEO_DM.");
			
			return ufs;
		} finally {
			if (stmt != null) { stmt.close(); }
		}
	}

}
