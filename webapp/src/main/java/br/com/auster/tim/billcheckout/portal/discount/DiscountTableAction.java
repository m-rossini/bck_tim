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
 * Created on 22/10/2007
 */
package br.com.auster.tim.billcheckout.portal.discount;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import br.com.auster.dware.console.commons.RequestScopeConstants;
import br.com.auster.dware.console.error.PortalRuntimeException;
import br.com.auster.dware.console.hibernate.HibernateEnabledAction;
import br.com.auster.tim.billcheckout.model.ProgressiveDiscount;
import br.com.auster.tim.billcheckout.model.ProgressiveDiscountRates;
import br.com.auster.web.utils.WebUtils;

/**
 * @author wsoares
 * @version $Id$
 *
 */
public class DiscountTableAction extends HibernateEnabledAction {


	private static final Logger log = Logger.getLogger(DiscountTableAction.class);

	private static final String FORWARD_DETAIL = "detail";
	private static final String FORWARD_DONE = "done";
	private static final String FORWARD_PROG_TABLE = "progTable";
	private static final String FORWARD_DELETE_RATE_DONE = "deleteRateDone";
	private static final String UID_DISCOUNT_REQUEST_ATT = "discountUid";
	private static final String UID_DISCOUNT_DESC_REQUEST_ATT = "discountDescUid";


	protected ActionForward unspecified(ActionMapping _mapping, ActionForm _form, HttpServletRequest _request, HttpServletResponse _response) throws Exception {
		return this.detail(_mapping, _form, _request, _response);
	}
		
	
	/**
	 * Lists all plans in the database.
	 */
	public ActionForward detail(ActionMapping _mapping, ActionForm _form, HttpServletRequest _request, HttpServletResponse _response) throws Exception {
		ActionMessages messages = new ActionMessages();
		Session session = null;
		try {
			String discountTableUid = (String) WebUtils.findRequestAttribute(_request, UID_DISCOUNT_REQUEST_ATT);
			String discountDescUid = (String) WebUtils.findRequestAttribute(_request, UID_DISCOUNT_DESC_REQUEST_ATT);
			if ((discountTableUid == null) || ("".equals(discountTableUid))) {
				// if INSERT action or parameter is NULL, treat as INSERT
				_request.setAttribute(RequestScopeConstants.REQUEST_OPERATION_DATA, RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT);
				_request.setAttribute(UID_DISCOUNT_DESC_REQUEST_ATT, discountDescUid);
				_request.setAttribute(RequestScopeConstants.REQUEST_REQID_KEY, new ProgressiveDiscount());
			} else {
				// or handling UPDATE action
				session = this.openConnection();
				Criteria criteria = session.createCriteria(ProgressiveDiscount.class);
				configureCriteria(criteria, null, new Long(discountTableUid));
				ProgressiveDiscount discountTableInfo = (ProgressiveDiscount) criteria.uniqueResult();
				if (discountTableInfo == null) {
					// could not read package information from request
					throw new IllegalArgumentException("Could not read discount table information for uid " + discountTableUid);
				}
				_request.setAttribute(UID_DISCOUNT_DESC_REQUEST_ATT, discountDescUid);
				_request.setAttribute(RequestScopeConstants.REQUEST_REQID_KEY, discountTableInfo);
				_request.setAttribute(RequestScopeConstants.REQUEST_OPERATION_DATA, RequestScopeConstants.REQUEST_OPERATION_TYPE_UPADTE);
			}
		} catch (Exception e) {
			messages = new ActionMessages();
			messages.add(Globals.MESSAGE_KEY, new ActionMessage("error.genericError", e));
			saveMessages(_request, messages);
			throw new PortalRuntimeException(e);
		} finally {
			if (session != null) {
				session.flush();
				session.close();
			}
		}
		return _mapping.findForward(FORWARD_DETAIL);
	}


	/**
	 * Reloads the current packageAction JSP with the selected information
	 */
	public ActionForward confirm(ActionMapping _mapping, ActionForm _form, HttpServletRequest _request, HttpServletResponse _response) throws Exception {
		ActionMessages messages = new ActionMessages();
		Session session = null;
		try {
			String discountDescUid = (String) WebUtils.findRequestAttribute(_request, UID_DISCOUNT_DESC_REQUEST_ATT);
			ProgressiveDiscount progDiscountInfo = getProgDiscount((DynaActionForm) _form);
			ProgressiveDiscount loadedprogDiscount = null;
			session = this.openConnection();
			if (progDiscountInfo.getUid() > 0) {
				Criteria criteria = session.createCriteria(ProgressiveDiscount.class);
				configureCriteria(criteria, null, new Long(progDiscountInfo.getUid()));
				loadedprogDiscount = (ProgressiveDiscount)criteria.uniqueResult();
				// moving updated info to loaded object
				loadedprogDiscount.setRangeName(progDiscountInfo.getRangeName());				
			} else {
				// else, just point loaded to new package info
				loadedprogDiscount = progDiscountInfo;
			}
			session.saveOrUpdate(loadedprogDiscount);
			session.flush();

			_request.setAttribute(RequestScopeConstants.REQUEST_REQID_KEY, loadedprogDiscount);
			_request.setAttribute(RequestScopeConstants.REQUEST_OPERATION_DATA, RequestScopeConstants.REQUEST_OPERATION_TYPE_UPADTE);
			_request.setAttribute(UID_DISCOUNT_DESC_REQUEST_ATT, discountDescUid);
			
		} catch (HibernateException he) {
			messages = new ActionMessages();
			messages.add(Globals.MESSAGE_KEY, new ActionMessage("error.genericError", he.getCause()));
			saveMessages(_request, messages);
			throw new PortalRuntimeException(he);
		} catch (Exception e) {
			messages = new ActionMessages();
			messages.add(Globals.MESSAGE_KEY, new ActionMessage("error.genericError", e));
			saveMessages(_request, messages);
			throw new PortalRuntimeException(e);
		} finally {
			if (session != null) {
				session.flush();
				session.close();
			}
		}
		return _mapping.findForward(FORWARD_DONE);
	}
	
	/**
	 * This action is executed everytime a delete operation (from the list page) is confirmed by some user.
	 */
	public ActionForward delete(ActionMapping _mapping, ActionForm _form, HttpServletRequest _request, HttpServletResponse _response) {
		ActionMessages messages = new ActionMessages();
		Session session = null;
		Criteria criteria;
		try {
			String discountRangeUid = (String) WebUtils.findRequestAttribute(_request, RequestScopeConstants.REQUEST_REQID_KEY);
			String discountDescUid = (String) WebUtils.findRequestAttribute(_request, UID_DISCOUNT_DESC_REQUEST_ATT);
			String discountTableUid = (String) WebUtils.findRequestAttribute(_request, UID_DISCOUNT_REQUEST_ATT);
			session = this.openConnection();
			
			//load the discount rate
			criteria = session.createCriteria(ProgressiveDiscountRates.class);
			configureCriteria(criteria, null, new Long(discountRangeUid));
			ProgressiveDiscountRates progDiscountRatesInfo = (ProgressiveDiscountRates) criteria.uniqueResult();
			if (progDiscountRatesInfo == null) {
				// could not read package information from request
				throw new IllegalArgumentException("Could not read progressive discount rates information for uid " + discountRangeUid);
			}
			
			//load the progressive discount
			criteria = session.createCriteria(ProgressiveDiscount.class);
			configureCriteria(criteria, null, new Long(discountTableUid));
			ProgressiveDiscount progDiscountInfo = (ProgressiveDiscount) criteria.uniqueResult();
			if (progDiscountInfo == null) {
				// could not read package information from request
				throw new IllegalArgumentException("Could not read progressive discount information for uid " + discountTableUid);
			}
			
			progDiscountInfo.getProgDiscountRates().remove(progDiscountRatesInfo);
			// removing package info
			session.saveOrUpdate(progDiscountInfo);
			_request.setAttribute(RequestScopeConstants.REQUEST_REQID_KEY, progDiscountRatesInfo);
			_request.setAttribute(UID_DISCOUNT_REQUEST_ATT, discountTableUid);
			_request.setAttribute(UID_DISCOUNT_DESC_REQUEST_ATT, discountDescUid);
			_request.setAttribute(RequestScopeConstants.REQUEST_OPERATION_DATA, RequestScopeConstants.REQUEST_OPERATION_TYPE_DELETE);
		} catch (Exception e) {
			messages = new ActionMessages();
			messages.add(Globals.MESSAGE_KEY, new ActionMessage("error.genericError", e));
			saveMessages(_request, messages);
			throw new PortalRuntimeException(e);
		} finally {
			try{
			if (session != null) {
				session.flush();
				session.close();
			}
			} catch(Exception e1) {
				e1.printStackTrace();
			}
		}
		return _mapping.findForward(FORWARD_DELETE_RATE_DONE);
	}
	
	public ActionForward insert(ActionMapping _mapping, ActionForm _form, HttpServletRequest _request, HttpServletResponse _response) {
		return insertOrupdate(_mapping, _form, _request, _response, RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT);
	}

	public ActionForward update(ActionMapping _mapping, ActionForm _form, HttpServletRequest _request, HttpServletResponse _response) {
		return insertOrupdate(_mapping, _form, _request, _response, RequestScopeConstants.REQUEST_OPERATION_TYPE_UPADTE);
	}

	/**
	 * Insert and update actions are much alike. So they can share the same code.
	 */
	private ActionForward insertOrupdate(ActionMapping _mapping, ActionForm _form, HttpServletRequest _request, HttpServletResponse _response, String _action) {
		ActionMessages messages = new ActionMessages();
		Session session = null;
		String fwToPopup;
		ProgressiveDiscount loadedProgDiscount = null;
		try {
			String discountDescUid = (String) WebUtils.findRequestAttribute(_request, UID_DISCOUNT_DESC_REQUEST_ATT);
			fwToPopup = (String) WebUtils.findRequestAttribute(_request, RequestScopeConstants.REQUEST_REQID_KEY);
			ProgressiveDiscount progDiscountInfo = getProgDiscount((DynaActionForm) _form);

			session = this.openConnection();
			if (progDiscountInfo.getUid() > 0) {
				Criteria criteria = session.createCriteria(ProgressiveDiscount.class);
				configureCriteria(criteria, null, new Long(progDiscountInfo.getUid()));
				loadedProgDiscount = (ProgressiveDiscount)criteria.uniqueResult();
				// moving updated info to loaded object
				loadedProgDiscount.setRangeName(progDiscountInfo.getRangeName());			
			} else {
				// else, just point loaded to new package info
				loadedProgDiscount = progDiscountInfo;
			}
			session.saveOrUpdate(loadedProgDiscount);
			session.flush();
			
			_request.setAttribute(UID_DISCOUNT_DESC_REQUEST_ATT, discountDescUid);
			_request.setAttribute(RequestScopeConstants.REQUEST_REQID_KEY, loadedProgDiscount);
			_request.setAttribute(RequestScopeConstants.REQUEST_OPERATION_DATA, _action);
		} catch (HibernateException he) {
			messages = new ActionMessages();
			messages.add(Globals.MESSAGE_KEY, new ActionMessage("error.genericError", he.getCause()));
			saveMessages(_request, messages);
			throw new PortalRuntimeException(he);
		} catch (Exception e) {
			messages = new ActionMessages();
			messages.add(Globals.MESSAGE_KEY, new ActionMessage("error.genericError", e));
			saveMessages(_request, messages);
			throw new PortalRuntimeException(e);
		} finally {
			if (session != null) {
				session.flush();
				session.close();
			}
		}
		if(fwToPopup != null && fwToPopup.equalsIgnoreCase("new")) {
			_request.setAttribute(UID_DISCOUNT_REQUEST_ATT, String.valueOf(loadedProgDiscount.getUid()));
			return _mapping.findForward(FORWARD_PROG_TABLE);
		} else {
			return _mapping.findForward(FORWARD_DONE);
		}
	}	
	
	/**
	 * Builds a PromotionPackage instance out of the posted HTML dynamic form
	 */
	private ProgressiveDiscount getProgDiscount(DynaActionForm _dynaForm) {
		if (_dynaForm == null) { return null; }
		try {
			ProgressiveDiscount progDiscountInfo = new ProgressiveDiscount();
			progDiscountInfo.setUid( Long.parseLong((String)_dynaForm.get("discountUid")) );
			progDiscountInfo.setRangeName((String)_dynaForm.get("rangeName"));
			return progDiscountInfo;
		} catch (Exception e) {
			throw new IllegalArgumentException("Could not read package information from form", e);
		}
	}
	
}
