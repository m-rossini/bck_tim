/*
 * Copyright (c) 2004-2008 Auster Solutions. All Rights Reserved.
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
 * Created on 14/04/2008
 */
package br.com.auster.tim.billcheckout.model;

import junit.framework.TestCase;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Expression;

import br.com.auster.common.sql.SQLConnectionManager;
import br.com.auster.common.xml.DOMUtils;

/**
 * @author framos
 * @version $Id$
 *
 */
public class PromotionPackageTest extends TestCase {


	public void testCRUDWithSingleSession() {
		try {

			Class.forName("org.apache.commons.dbcp.PoolingDriver");
			Class.forName("oracle.jdbc.driver.OracleDriver");
			SQLConnectionManager.init(DOMUtils.openDocument("pool/pool.xml", false));

			SessionFactory sf = buildSessionFactory();
			Session ss = sf.openSession();

			insert(ss);
			select(ss);
			update(ss);
			select2(ss);
			delete(ss);
			select3(ss);
		} catch (Exception e) {
    		e.printStackTrace();
    		fail(e.getLocalizedMessage());
		}
	}


	public void testCRUDWithMultipleSessions() {
		try {

			Class.forName("org.apache.commons.dbcp.PoolingDriver");
			Class.forName("oracle.jdbc.driver.OracleDriver");
			SQLConnectionManager.init(DOMUtils.openDocument("pool/pool.xml", false));

			SessionFactory sf = buildSessionFactory();
			Session ss = sf.openSession();
			insert(ss);
			ss.close();
			ss = sf.openSession();
			select(ss);
			ss.close();
			ss = sf.openSession();
			update(ss);
			ss.close();
			ss = sf.openSession();
			select2(ss);
			ss.close();
			ss = sf.openSession();
			delete(ss);
			ss.close();
			ss = sf.openSession();
			select3(ss);
		} catch (Exception e) {
    		e.printStackTrace();
    		fail(e.getLocalizedMessage());
		}
	}


	public void testCRUDWithMultipleSessionFactories() {
		try {

			Class.forName("org.apache.commons.dbcp.PoolingDriver");
			Class.forName("oracle.jdbc.driver.OracleDriver");
			SQLConnectionManager.init(DOMUtils.openDocument("pool/pool.xml", false));

			insert(null);
			select(null);
			update(null);
			select2(null);
			delete(null);
			select3(null);
		} catch (Exception e) {
    		e.printStackTrace();
    		fail(e.getLocalizedMessage());
		}
	}


	private void insert(Session _ss) throws Exception {

		Session ss = null;
		if (_ss == null) {
			ss = buildSessionFactory().openSession();
		} else {
			ss = _ss;
		}

		ProgressiveDiscount discount = buildInsertDiscount();
		ss.save(discount);
		ProgressiveDiscountDesc desc1 = buildInsertDiscountDesc(discount, "Discount Description 1");
		ss.save(desc1);
		ProgressiveDiscountDesc desc2 = buildInsertDiscountDesc(discount, "Discount Description 2");
		ss.save(desc2);

		ss.flush();
		ss.connection().commit();

		if (_ss == null) {
			ss.close();
		}
	}

	private void select(Session _ss) throws Exception {

		Session ss = null;
		if (_ss == null) {
			ss = buildSessionFactory().openSession();
		} else {
			ss = _ss;
		}

		Criteria crit = ss.createCriteria(ProgressiveDiscountDesc.class);
		crit.add(Expression.eq("discountDesc", "Discount Description 3"));
		assertEquals(0, crit.list().size());

		crit = ss.createCriteria(ProgressiveDiscountDesc.class);
		crit.add(Expression.eq("discountDesc", "Discount Description 1"));
		assertEquals(1, crit.list().size());
		ProgressiveDiscountDesc found = (ProgressiveDiscountDesc) crit.list().get(0);
		assertNotNull(found.getProgDiscount());
		ProgressiveDiscount discount = found.getProgDiscount();
		assertEquals("Some Discount", discount.getRangeName());
		assertNotNull(discount.getProgDiscountRates());
		assertEquals(3, discount.getProgDiscountRates().size());

		if (_ss == null) {
			ss.close();
		}
	}

	private void delete(Session _ss) throws Exception {

		Session ss = null;
		if (_ss == null) {
			ss = buildSessionFactory().openSession();
		} else {
			ss = _ss;
		}

		Criteria crit = ss.createCriteria(ProgressiveDiscountDesc.class);
		crit.add(Expression.eq("discountDesc", "Discount Description 1 - New"));
		assertEquals(1, crit.list().size());
		ProgressiveDiscountDesc found = (ProgressiveDiscountDesc) crit.uniqueResult();
		ss.delete(found);
		ss.flush();

		crit = null;
		crit = ss.createCriteria(ProgressiveDiscountDesc.class);
		crit.add(Expression.eq("discountDesc", "Discount Description 2"));
		assertEquals(1, crit.list().size());
		found = (ProgressiveDiscountDesc) crit.uniqueResult();
		ss.delete(found);
		ss.flush();

		crit = null;
		crit = ss.createCriteria(ProgressiveDiscount.class);
		crit.add(Expression.eq("rangeName", "Some New Discount"));
		assertEquals(1, crit.list().size());
		ProgressiveDiscount found2 = (ProgressiveDiscount) crit.list().get(0);
		ss.delete(found2);
		ss.flush();

		ss.connection().commit();
		if (_ss == null) {
			ss.close();
		}
	}

	private void update(Session _ss) throws Exception {

		Session ss = null;
		if (_ss == null) {
			ss = buildSessionFactory().openSession();
		} else {
			ss = _ss;
		}

		Criteria crit = ss.createCriteria(ProgressiveDiscountDesc.class);
		crit.add(Expression.eq("discountDesc", "Discount Description 1"));
		ProgressiveDiscountDesc found = (ProgressiveDiscountDesc) crit.uniqueResult();
		assertNotNull(found);
		assertNotNull(found.getProgDiscount());
		ProgressiveDiscount discount = found.getProgDiscount();
		assertEquals("Some Discount", discount.getRangeName());
		assertNotNull(discount.getSortedDiscountRates());
		assertEquals(3, discount.getSortedDiscountRates().size());

		ProgressiveDiscountRates rate = (ProgressiveDiscountRates)discount.getSortedDiscountRates().iterator().next();
		rate.setLowerLimit(-10);
		rate.setDiscountRate(2.5);
		discount.setRangeName("Some New Discount");

		rate = new ProgressiveDiscountRates();
		rate.setLowerLimit(51);
		rate.setUpperLimit(100);
		rate.setDiscountRate(10);
		discount.addProgDiscountRates(rate);
		ss.saveOrUpdate(discount);

		found.setDiscountDesc("Discount Description 1 - New");
		ss.saveOrUpdate(found);

		ss.flush();
		ss.connection().commit();

		if (_ss == null) {
			ss.close();
		}
	}

	private void select2(Session _ss) throws Exception {

		Session ss = null;
		if (_ss == null) {
			ss = buildSessionFactory().openSession();
		} else {
			ss = _ss;
		}

		Criteria crit = ss.createCriteria(ProgressiveDiscountDesc.class);
		crit.add(Expression.eq("discountDesc", "Discount Description 1"));
		assertEquals(0, crit.list().size());

		crit = ss.createCriteria(ProgressiveDiscountDesc.class);
		crit.add(Expression.eq("discountDesc", "Discount Description 1 - New"));
		assertEquals(1, crit.list().size());
		ProgressiveDiscountDesc found = (ProgressiveDiscountDesc) crit.list().get(0);
		assertNotNull(found.getProgDiscount());
		ProgressiveDiscount discount = found.getProgDiscount();
		assertEquals("Some New Discount", discount.getRangeName());
		assertNotNull(discount.getSortedDiscountRates());
		assertEquals(4, discount.getSortedDiscountRates().size());

		ProgressiveDiscountRates rate = (ProgressiveDiscountRates)discount.getSortedDiscountRates().iterator().next();
		assertEquals(-10, rate.getLowerLimit(), 0.01);
		assertEquals(2.5, rate.getDiscountRate(), 0.01);

		if (_ss == null) {
			ss.close();
		}
	}

	private void select3(Session _ss) throws Exception {

		Session ss = null;
		if (_ss == null) {
			ss = buildSessionFactory().openSession();
		} else {
			ss = _ss;
		}

		Criteria crit = ss.createCriteria(ProgressiveDiscountDesc.class);
		crit.add(Expression.eq("discountDesc", "Discount Description 1"));
		assertEquals(0, crit.list().size());

		crit = ss.createCriteria(ProgressiveDiscountDesc.class);
		crit.add(Expression.eq("discountDesc", "Discount Description 1 - New"));
		assertEquals(0, crit.list().size());

		crit = ss.createCriteria(ProgressiveDiscountDesc.class);
		crit.add(Expression.eq("discountDesc", "Discount Description 2"));
		assertEquals(0, crit.list().size());

		crit = ss.createCriteria(ProgressiveDiscount.class);
		crit.add(Expression.eq("rangeName", "Some New Discount"));
		assertEquals(0, crit.list().size());

		if (_ss == null) {
			ss.close();
		}
	}


	private SessionFactory buildSessionFactory() throws Exception {
		Configuration cfg = new Configuration();
		cfg.configure("/hibernate-configuration.xml");
		return cfg.buildSessionFactory();

	}


	private ProgressiveDiscountDesc buildInsertDiscountDesc(ProgressiveDiscount _discount, String _desc) {
		ProgressiveDiscountDesc desc = new ProgressiveDiscountDesc();
		desc.setDiscountDesc(_desc);
		desc.setProgDiscount(_discount);
		return desc;
	}

	private ProgressiveDiscount buildInsertDiscount() {
		ProgressiveDiscount disc = new ProgressiveDiscount();
		disc.setRangeName("Some Discount");

		ProgressiveDiscountRates rate = new ProgressiveDiscountRates();
		rate.setDiscountRate(10);
		rate.setLowerLimit(1);
		rate.setUpperLimit(10);
//		rate.setSeqNum(0);
		disc.addProgDiscountRates(rate);

		rate = new ProgressiveDiscountRates();
		rate.setDiscountRate(20);
		rate.setLowerLimit(11);
		rate.setUpperLimit(50);
//		rate.setSeqNum(1);
		disc.addProgDiscountRates(rate);

		rate = new ProgressiveDiscountRates();
		rate.setDiscountRate(30);
		rate.setLowerLimit(51);
		//rate.setUpperLimit();
//		rate.setSeqNum(2);
		disc.addProgDiscountRates(rate);

		return disc;
	}
}
