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
import java.util.Set;

import org.apache.log4j.Logger;

import br.com.auster.tim.billcheckout.model.PromotionPackage;

/**
 * @author framos
 * @version $Id$
 *
 */
public class PromotionPackageDAO {


	private static final Logger log = Logger.getLogger(PromotionPackageDAO.class);


	public static final String DELETE_CURRENT_PLANS = "delete from qlf_plans_packages where package_uid = ? and plan_uid = ?";
	public static final String INSERT_NEW_PLAN = "insert into qlf_plans_packages ( plan_uid, package_uid ) values ( ? , ? )";
	public static final String DELETE_CURRENT_PLANS_DEVIATION = "delete from qlf_plans_packages_deviation where package_uid = ? and plan_uid = ?";
	public static final String INSERT_NEW_PLAN_DEVIATION = "insert into qlf_plans_packages_deviation ( plan_uid, package_uid ) values ( ? , ? )";

	public static final String SELECT_PLAN = "select description from qlf_plans where objid = ?";
	public static final String SELECT_PLAN_FOR_PACKAGE = "select objid, plan_name, state from qlf_plans where objid in ( select plan_uid from qlf_plans_packages where package_uid = ?)";
	public static final String SELECT_PLAN_DEVIATION_FOR_PACKAGE = "select objid, plan_name, state from qlf_plans where objid in ( select plan_uid from qlf_plans_packages_deviation where package_uid = ?)";
	
	public void reassignPlans(Connection _conn, PromotionPackage _package, Set _displayed) throws SQLException {
		PreparedStatement stmt = null;
		try {
			// removing initial assignments
			stmt = _conn.prepareStatement(DELETE_CURRENT_PLANS);
			if (_displayed != null) {
				for (Iterator it = _displayed.iterator(); it.hasNext(); ) {
					stmt.setLong(1, _package.getUid());
					stmt.setLong(2, Integer.parseInt((String)it.next()));
					stmt.addBatch();
					stmt.clearParameters();
				}
			}
			int[] ids = stmt.executeBatch();
			log.debug("Removed " + ids.length + " rows from QLF_PLANS_PACKAGES for package " + _package.getUid());
			stmt.close();
			// assiging new plans to this package
			stmt = _conn.prepareStatement(INSERT_NEW_PLAN);
			for (Iterator it = _package.getPlansKeys(); it.hasNext();) {
				Long planUid = (Long) it.next();
				stmt.setLong(1, planUid.longValue());
				stmt.setLong(2, _package.getUid());
				stmt.addBatch();
				log.debug("Assigning plan " + planUid + " for package " + _package.getUid());
				stmt.clearParameters();
			}
			ids = stmt.executeBatch();
			log.debug("Created " + ids.length + " rows on QLF_PLANS_PACKAGES for package " + _package.getUid());
		} finally {
			if (stmt != null) { stmt.close(); }
		}
	}
	
	public void reassignPlansDiscount(Connection _conn, PromotionPackage _package, Set _displayed) throws SQLException {
		PreparedStatement stmt = null;
		try {
			// removing initial assignments
			stmt = _conn.prepareStatement(DELETE_CURRENT_PLANS_DEVIATION);
			if (_displayed != null) {
				for (Iterator it = _displayed.iterator(); it.hasNext(); ) {
					stmt.setLong(1, _package.getUid());
					stmt.setLong(2, Integer.parseInt((String)it.next()));
					stmt.addBatch();
					stmt.clearParameters();
				}
			}
			int[] ids = stmt.executeBatch();
			log.debug("Removed " + ids.length + " rows from QLF_PLANS_PACKAGES_DEVIATION for package " + _package.getUid());
			stmt.close();
			// assiging new plans to this package
			stmt = _conn.prepareStatement(INSERT_NEW_PLAN_DEVIATION);
			for (Iterator it = _package.getPlansKeys(); it.hasNext();) {
				Long planUid = (Long) it.next();
				stmt.setLong(1, planUid.longValue());
				stmt.setLong(2, _package.getUid());
				stmt.addBatch();
				log.debug("Assigning plan " + planUid + " for package " + _package.getUid());
				stmt.clearParameters();
			}
			ids = stmt.executeBatch();
			log.debug("Created " + ids.length + " rows on QLF_PLANS_PACKAGES_DEVIATION for package " + _package.getUid());
		} finally {
			if (stmt != null) { stmt.close(); }
		}
	}


	public void loadPlanNames(Connection _conn, PromotionPackage _package) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rset = null;
		HashMap map = new HashMap();
		try {
			stmt = _conn.prepareStatement(SELECT_PLAN);
			for (Iterator it = _package.getPlansKeys(); it.hasNext();) {
				Long planUid = (Long) it.next();
				stmt.setLong(1, planUid.longValue());
				rset = stmt.executeQuery();
				if (rset.next()) {
					map.put(planUid, rset.getString(1));
					log.debug("Added plan " + map.get(planUid) + " for package " + _package.getUid());
				} else {
					log.debug("Could not load information for plan " + planUid);
				}
				stmt.clearParameters();
			}
			_package.addPlans(map);
		} finally {
			if (stmt != null) { stmt.close(); }
		}
	}


	public void loadPlanForPackage(Connection _conn, PromotionPackage _package) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rset = null;
		HashMap map = new HashMap();
		try {
			stmt = _conn.prepareStatement(SELECT_PLAN_FOR_PACKAGE);
			stmt.setLong(1, _package.getUid());
			rset = stmt.executeQuery();
			while (rset.next()) {
				_package.addPlan(rset.getLong(1), rset.getString(2) + "(" + rset.getString(3) + ")");
			}
		} finally {
			if (stmt != null) { stmt.close(); }
		}
	}
	
	public void loadPlanDiscountForPackage(Connection _conn, PromotionPackage _package) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rset = null;
		HashMap map = new HashMap();
		try {
			stmt = _conn.prepareStatement(SELECT_PLAN_DEVIATION_FOR_PACKAGE);
			stmt.setLong(1, _package.getUid());
			rset = stmt.executeQuery();
			while (rset.next()) {
				_package.addPlan(rset.getLong(1), rset.getString(2) + "(" + rset.getString(3) + ")");
			}
		} finally {
			if (stmt != null) { stmt.close(); }
		}
	}


}
