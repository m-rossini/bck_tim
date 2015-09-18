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
package br.com.auster.tim.billcheckout.portal.packages;

import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import br.com.auster.dware.console.commons.RequestScopeConstants;
import br.com.auster.dware.console.error.PortalRuntimeException;
import br.com.auster.dware.console.hibernate.HibernateEnabledAction;
import br.com.auster.tim.billcheckout.model.Plan;
import br.com.auster.web.indexing.utils.IndexingUtils;
import br.com.auster.web.utils.WebUtils;

/**
 * @author framos
 * @version $Id$
 *
 */
public class PlansAction extends HibernateEnabledAction {



	private static final Logger log = Logger.getLogger(PlansAction.class);


	private static final String FORWARD_DISPLAYLIST = "list";




	protected ActionForward unspecified(ActionMapping _mapping, ActionForm _form, HttpServletRequest _request, HttpServletResponse _response) throws Exception {
		return this.list(_mapping, _form, _request, _response);
	}

	/**
	 * Lists all plans in the database.
	 */
	public ActionForward list(ActionMapping _mapping, ActionForm _form, HttpServletRequest _request, HttpServletResponse _response) throws Exception {
		Session session = null;
		try {
			// defining paging information
			String pageId = (String) WebUtils.findRequestAttribute(_request, RequestScopeConstants.REQUEST_PAGEID_KEY);
			if (pageId == null) { pageId = "1"; }
			String moveTo = (String) WebUtils.findRequestAttribute(_request, RequestScopeConstants.REQUEST_MOVETO_KEY);
			if (moveTo == null) { moveTo = "0"; }
			// order field and orientation
			String orderBy = (String) WebUtils.findRequestAttribute(_request, RequestScopeConstants.REQUEST_ORDERBY_KEY);
			if ((orderBy == null) || (orderBy.trim().length() <= 0)) { orderBy = "shortDescription"; }
			String orderWay = (String) WebUtils.findRequestAttribute(_request, RequestScopeConstants.REQUEST_ORDERWAY_KEY);
			if (orderWay == null) { orderWay = RequestScopeConstants.REQUEST_ORDERFORWARD_KEY; }
			// Filter
			String filterType = (String) WebUtils.findRequestAttribute(_request, RequestScopeConstants.REQUEST_FILTERBY_KEY);
			String filterCondition = (String) WebUtils.findRequestAttribute(_request, RequestScopeConstants.REQUEST_FILTERCONDITION_KEY);
			// debugging these parameters
			log.debug("pageId is " + pageId);
			log.debug("moveTo is " + moveTo);
			log.debug("orderBy is " + orderBy);
			log.debug("orderWay is " + orderWay);
			log.debug("filterType is" + filterType);
			log.debug("filterCondition is" + filterCondition);
			// preparing to load information
			int displayLength = 20;
			int pageNbr = IndexingUtils.getDisplayPageId(Integer.parseInt(pageId), Integer.parseInt(moveTo));
			int offset = IndexingUtils.getStartingElement(pageNbr, displayLength);
			fetch.clearOrder();
			fetch.setOffset(offset);
			fetch.setSize(displayLength);
			fetch.addOrder(orderBy, orderWay.equals(RequestScopeConstants.REQUEST_ORDERFORWARD_KEY));
			log.debug("offset is " + offset);
			log.debug("fetchSize is " + displayLength);
			// loading data
			session = this.openConnection();
			Criteria criteria = session.createCriteria(Plan.class);
			if ((filterType != null && filterType.trim().length() > 0) && (filterCondition != null && filterCondition.trim().length() > 0)) {
				criteria.add(Restrictions.ilike(filterType, "%"+filterCondition+"%"));
			}
			int totalPages = criteria.list().size();
			configureCriteria(criteria, fetch);
			List results = criteria.list();
			log.debug("loaded " + results.size() + " out of " + totalPages + " elements for Plan.class");
			// setting info into request
			totalPages = IndexingUtils.getNumberOfPages(totalPages, displayLength);
			_request.setAttribute(RequestScopeConstants.REQUEST_PAGEID_KEY, String.valueOf(pageNbr));
			_request.setAttribute(RequestScopeConstants.REQUEST_TOTALPAGES_KEY, String.valueOf(totalPages));
			_request.setAttribute(RequestScopeConstants.REQUEST_ORDERBY_KEY, (orderBy == null ? "customerType" : orderBy) );
			_request.setAttribute(RequestScopeConstants.REQUEST_ORDERWAY_KEY, orderWay);
			_request.setAttribute(RequestScopeConstants.REQUEST_FILTERBY_KEY, (filterType == null ? "" : filterType) );
			_request.setAttribute(RequestScopeConstants.REQUEST_FILTERCONDITION_KEY, (filterCondition == null? "" : filterCondition) );
			_request.setAttribute(RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY, results);

		} catch (Exception e) {
			ActionMessages messages = new ActionMessages();
			messages.add(Globals.MESSAGE_KEY, new ActionMessage("error.genericError", e));
			saveMessages(_request, messages);
			throw new PortalRuntimeException(e);
		} finally {
			if (session != null) {
				session.flush();
				session.close();
			}
		}
		return _mapping.findForward(FORWARD_DISPLAYLIST);
	}



	/**
	 * Lists all plans in the database.
	 */
	public ActionForward confirm(ActionMapping _mapping, ActionForm _form, HttpServletRequest _request, HttpServletResponse _response) throws Exception {
		Session session = null;
		try {
			// saving confirmed information
			String disabledList = (String) WebUtils.findRequestAttribute(_request, "disabled");
			log.debug("Disabled list is " + disabledList);
			String enabledList = (String) WebUtils.findRequestAttribute(_request, "enabled");
			log.debug("Enabled list is " + enabledList);

			session = openConnection();
			updateMandatoryFlag(session, disabledList, false);
			updateMandatoryFlag(session, enabledList, true);

		} catch (Exception e) {
			ActionMessages messages = new ActionMessages();
			messages.add(Globals.MESSAGE_KEY, new ActionMessage("error.genericError", e));
			saveMessages(_request, messages);
			throw new PortalRuntimeException(e);
		} finally {
			if (session != null) {
				session.flush();
				session.close();
			}
		}
		return this.list(_mapping, _form, _request, _response);
	}


	/**
	 * Updates the mandatory package flag for Plans according to the incoming boolean parameter
	 */
	private void updateMandatoryFlag(Session _session, String _uids, boolean _newValue) {
		if ((_uids != null) && (_uids.trim().length() > 0)) {
			StringTokenizer tokenizer = new StringTokenizer(_uids.trim(), ",");
			while (tokenizer.hasMoreTokens()) {
				String uid = tokenizer.nextToken();
				log.debug("Setting mandatory flag for plan uid " + uid + " to " + _newValue);
				Criteria criteria = _session.createCriteria(Plan.class);
				criteria.add(Restrictions.like("uid", new Long(uid)));
				Plan plan = (Plan) criteria.uniqueResult();
				if (plan == null) {
					log.warn("Could not locate plan for uid " + uid);
					continue;
				}
				plan.setMandatory(_newValue);
				_session.update(plan);
			}
		}
	}
}
