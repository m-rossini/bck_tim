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
import br.com.auster.tim.billcheckout.model.ProgressiveDiscount;
import br.com.auster.tim.billcheckout.model.ProgressiveDiscountRates;
import br.com.auster.dware.console.hibernate.HibernateEnabledAction;
import br.com.auster.web.utils.WebUtils;

/**
 * @author framos
 * @version $Id$
 *
 */
public class DiscountRangePopupAction extends HibernateEnabledAction {


	private static final Logger log = Logger.getLogger(DiscountRangePopupAction.class);

	private static final String FORWARD_DONE = "done";
	private static final String FORWARD_DETAIL = "detail";
	private static final String REQUEST_DISCOUNT_RATE_UID = "uid";
	private static final String UID_DISCOUNT_REQUEST_ATT = "discountUid";
	private static final String UID_DISCOUNT_DESC_REQUEST_ATT = "discountDescUid";


	protected ActionForward unspecified(ActionMapping _mapping, ActionForm _form, HttpServletRequest _request, HttpServletResponse _response) throws Exception {
		return this.detail(_mapping, _form, _request, _response);
	}

	/**
	 * Prepares the packageAction JSP. If an insert action was selected, then it will send to that page the INSERT operation token and no
	 * 	package information. When we are handling update actions, then the UPDATE token is sent, and the details for the selected package
	 *  are loaded and posted to the page too.
	 */
	public ActionForward detail(ActionMapping _mapping, ActionForm _form, HttpServletRequest _request, HttpServletResponse _response) {
		ActionMessages messages = new ActionMessages();
		Session session = null;
		Criteria criteria;
		try {
			session = this.openConnection();
			String discDescUid = (String) WebUtils.findRequestAttribute(_request, UID_DISCOUNT_DESC_REQUEST_ATT);
			String discountTableUid = (String) WebUtils.findRequestAttribute(_request, UID_DISCOUNT_REQUEST_ATT);
			String discountRateUid = (String) WebUtils.findRequestAttribute(_request, REQUEST_DISCOUNT_RATE_UID);

			if ((discountRateUid == null) || ("".equals(discountRateUid))) {
				// if INSERT action or parameter is NULL, treat as INSERT
				_request.setAttribute(RequestScopeConstants.REQUEST_OPERATION_DATA, RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT);
				_request.setAttribute(UID_DISCOUNT_REQUEST_ATT, discountTableUid);
				_request.setAttribute(RequestScopeConstants.REQUEST_REQID_KEY, new ProgressiveDiscountRates());
				_request.setAttribute(UID_DISCOUNT_DESC_REQUEST_ATT, discDescUid);
			} else {
				// or handling UPDATE action
				criteria = session.createCriteria(ProgressiveDiscountRates.class);
				configureCriteria(criteria, null, new Long(discountRateUid));
				ProgressiveDiscountRates discountRateInfo = (ProgressiveDiscountRates) criteria.uniqueResult();
				if (discountRateInfo == null) {
					// could not read package information from request
					throw new IllegalArgumentException("Could not read discount information for uid " + discountRateUid);
				}
				log.debug("loaded an element for ProgressiveDiscountRates.class");
				_request.setAttribute(RequestScopeConstants.REQUEST_REQID_KEY, discountRateInfo);
				_request.setAttribute(UID_DISCOUNT_DESC_REQUEST_ATT, discDescUid);
				_request.setAttribute(UID_DISCOUNT_REQUEST_ATT, discountTableUid);
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
		Criteria criteria;
		try {
			ProgressiveDiscountRates progDiscountRateInfo = getProgDiscountRate((DynaActionForm) _form);
			ProgressiveDiscountRates loadedProgDiscountRate = null;
			String discDescUid = (String) WebUtils.findRequestAttribute(_request, UID_DISCOUNT_DESC_REQUEST_ATT);
			String discountUid = (String) WebUtils.findRequestAttribute(_request, UID_DISCOUNT_REQUEST_ATT);

			session = this.openConnection();

			criteria = session.createCriteria(ProgressiveDiscount.class);
			configureCriteria(criteria, null, new Long(discountUid));
			ProgressiveDiscount progressiveDiscount = (ProgressiveDiscount)criteria.uniqueResult();

			if (progDiscountRateInfo.getUid() > 0) {
				criteria = session.createCriteria(ProgressiveDiscountRates.class);
				configureCriteria(criteria, null, new Long(progDiscountRateInfo.getUid()));
				loadedProgDiscountRate = (ProgressiveDiscountRates)criteria.uniqueResult();
				// moving updated info to loaded object
				loadedProgDiscountRate.setLowerLimit(progDiscountRateInfo.getLowerLimit());
				loadedProgDiscountRate.setUpperLimit(progDiscountRateInfo.getUpperLimit());
				loadedProgDiscountRate.setDiscountRate(progDiscountRateInfo.getDiscountRate());
			} else {
				// else, just point loaded to new package info
				loadedProgDiscountRate = progDiscountRateInfo;
			}

			progressiveDiscount.addProgDiscountRates(loadedProgDiscountRate);

			session.saveOrUpdate(progressiveDiscount);
			session.flush();
			_request.setAttribute(UID_DISCOUNT_REQUEST_ATT, discountUid);
			_request.setAttribute(UID_DISCOUNT_DESC_REQUEST_ATT, discDescUid);

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
	 * Builds a PromotionPackage instance out of the posted HTML dynamic form
	 */
	private ProgressiveDiscountRates getProgDiscountRate(DynaActionForm _dynaForm) {
		if (_dynaForm == null) { return null; }
		try {
			ProgressiveDiscountRates progDiscountRateInfo = new ProgressiveDiscountRates();
			progDiscountRateInfo.setUid( Long.parseLong((String)_dynaForm.get("uid")) );
			progDiscountRateInfo.setLowerLimit(Double.parseDouble((String)_dynaForm.get("lowerLimit")) );
			progDiscountRateInfo.setUpperLimit(Double.parseDouble((String)_dynaForm.get("upperLimit")) );
			progDiscountRateInfo.setDiscountRate(Double.parseDouble((String)_dynaForm.get("discountRate")) );
			return progDiscountRateInfo;
		} catch (Exception e) {
			IllegalArgumentException iae = new IllegalArgumentException("Could not read package information from form");
			iae.initCause(e);
			throw iae;
		}
	}
}
