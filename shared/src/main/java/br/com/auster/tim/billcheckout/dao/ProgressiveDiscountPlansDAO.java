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
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import br.com.auster.tim.billcheckout.model.Plan;
import br.com.auster.tim.billcheckout.model.ProgressiveDiscountPlans;

/**
 * @author framos
 * @version $Id$
 *
 */
public class ProgressiveDiscountPlansDAO {


	private static final Logger log = Logger.getLogger(ProgressiveDiscountPlansDAO.class);


	public static final String DELETE_CURRENT_PLANS = "delete from prog_discount_plans where discount_uid = ? and plan_uid = ?";
	public static final String INSERT_NEW_PROMOTION = "insert into prog_discount_plans ( discount_uid, plan_uid ) values ( ? , ? )";
	public static final String SELECT_PLANS = "select plan_uid from prog_discount_plans where discount_uid = ?";



	public void reassignPromotions(Connection _conn, ProgressiveDiscountPlans _discount, String[] _plans, List _displayedPlans) throws SQLException {
		PreparedStatement stmt = null;
		try {
			// building control set
			Set displayed = new TreeSet();
			Iterator it = _displayedPlans.iterator();
			while (it.hasNext()) {
				Plan plan = (Plan) it.next();
				displayed.add(String.valueOf(plan.getUid()));
				log.debug("Displayed uid saved [" + plan.getUid() + "]");
			}
			// assiging new plans to this package
			stmt = _conn.prepareStatement(INSERT_NEW_PROMOTION);
			for (int i=0; (_plans != null) && (i < _plans.length); i++) {
				if (!displayed.remove(_plans[i])) {
					// this should never happen
					throw new IllegalArgumentException("Plan Uid [" + _plans[i] + "] was not displayed.");
				}
				if (_discount.getPlans().contains(_plans[i])) {
					// checked before and still checked
					continue;
				}
				stmt.setLong(1, _discount.getDiscountUid());
				stmt.setLong(2, Long.parseLong(_plans[i]));
				stmt.addBatch();
				stmt.clearParameters();
				log.debug("Adding plan " + _plans[i] + " for discount id " + _discount.getDiscountUid());
			}
			log.debug("Added " + stmt.executeBatch().length +  " rows");
			stmt.close();
			// removing remaining plan uids in displayed since they where unmarked
			stmt = _conn.prepareStatement(DELETE_CURRENT_PLANS);
			it = displayed.iterator();
			while (it.hasNext()) {
				String planUid = (String) it.next();
				stmt.setLong(1, _discount.getDiscountUid());
				stmt.setLong(2, Long.parseLong(planUid));
				stmt.addBatch();
				stmt.clearParameters();
				log.debug("Removing plan " + planUid + " for discount id " + _discount.getDiscountUid());
			}
			log.debug("Removed " + stmt.executeBatch().length +  " rows");
		} finally {
			if (stmt != null) { stmt.close(); }
		}
	}

	public void loadCurrentPlans(Connection _conn, ProgressiveDiscountPlans _discount) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			stmt = _conn.prepareStatement( SELECT_PLANS );
			stmt.setLong(1, _discount.getDiscountUid());
			rset = stmt.executeQuery();
			while (rset.next()) {
				_discount.addPlan(rset.getString(1));
			}
		} finally {
			if (rset != null) { rset.close(); }
			if (stmt != null) { stmt.close(); }

		}
	}

}
