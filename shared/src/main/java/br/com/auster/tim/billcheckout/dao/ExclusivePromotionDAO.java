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
 * Created on 16/10/2007
 */
package br.com.auster.tim.billcheckout.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;

import br.com.auster.tim.billcheckout.model.ExclusivePromotion;

/**
 * @author framos
 * @version $Id$
 *
 */
public class ExclusivePromotionDAO {


	private static final Logger log = Logger.getLogger(ExclusivePromotionDAO.class);


	public static final String DELETE_CURRENT_PROMOTIONS = "delete from promotion_exclusivity_rel where objid_1 = ? and objid_2 = ?";
	public static final String INSERT_NEW_PROMOTION = "insert into promotion_exclusivity_rel ( objid_1, objid_2 ) values ( ? , ? )";

	public static final String SELECT_PROMOTION = "select description from promotion_exclusivity_rel where objid_1 = ?";
	public static final String SELECT_PROMOTION2_FOR_PROMOTION1 = "select objid, short_desc, description from promotion_exclusivity where objid in ( select objid_2 from promotion_exclusivity_rel where objid_1 = ?)";



	public void reassignPromotions(Connection _conn, ExclusivePromotion promotion, String[] displayed) throws SQLException {
		PreparedStatement stmt = null;
		try {
			// removing all rows for displayed promotions
			if (displayed != null) {
				stmt = _conn.prepareStatement(DELETE_CURRENT_PROMOTIONS);
				for (int i=0; i < displayed.length; i++) {
					stmt.setLong(1, promotion.getUid());
					stmt.setLong(2, Long.parseLong(displayed[i]));
					stmt.addBatch();
					stmt.clearParameters();
				}
				int[] results = stmt.executeBatch();
				stmt.close();
				// setting null so that if an exception runs the execution into "finally" before
				//   reaching the assignment step, it wont try to close an already closed statement
				stmt = null;
				int deletedRows = 0;
				for (int i=0; i < results.length; i++) {
					deletedRows += results[i];
				}
				log.debug("Removed " + deletedRows + " rows from PROMOTION_EXCLUSIVITY_REL for promotion " + promotion.getUid());
			}

			// now we assign those checked by the user
			stmt = _conn.prepareStatement(INSERT_NEW_PROMOTION);
			for (Iterator it = promotion.getPromotionsKeys(); it.hasNext();) {
				stmt.setLong(1, promotion.getUid());
				stmt.setLong(2, ((Long) it.next()).longValue());
				stmt.addBatch();
				stmt.clearParameters();
			}
			int[] ids = stmt.executeBatch();
			log.debug("Created " + ids.length + " rows on PROMOTION_EXCLUSIVITY_REL for promotion " + promotion.getUid());
		} finally {
			if (stmt != null) { stmt.close(); }
		}
	}

	public void loadPromotionsDesc(Connection _conn, ExclusivePromotion promotion) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rset = null;
		HashMap map = new HashMap();
		try {
			stmt = _conn.prepareStatement(SELECT_PROMOTION);
			for (Iterator it = promotion.getPromotionsKeys(); it.hasNext();) {
				Long promotionUid = (Long) it.next();
				stmt.setLong(1, promotionUid.longValue());
				rset = stmt.executeQuery();
				if (rset.next()) {
					map.put(promotionUid, rset.getString(1));
					log.debug("Added promotion_2 " + map.get(promotionUid) + " for promotion_1 " + promotion.getUid());
				} else {
					log.debug("Could not load information for promotion " + promotionUid);
				}
				stmt.clearParameters();
			}
			promotion.addPromotions(map);
		} finally {
			if (stmt != null) { stmt.close(); }
		}
	}


	public void loadPromotion2ForPromotion1(Connection _conn, ExclusivePromotion promotion) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rset = null;

		try {
			stmt = _conn.prepareStatement(SELECT_PROMOTION2_FOR_PROMOTION1);
			stmt.setLong(1, promotion.getUid());
			rset = stmt.executeQuery();
			while (rset.next()) {
				promotion.addPromotion(rset.getLong(1), rset.getString(2) + "(" + rset.getString(3) + ")");
			}
		} finally {
			if (stmt != null) { stmt.close(); }
		}
	}

}
