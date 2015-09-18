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
 * Created on 24/04/2008
 */
package br.com.auster.tim.billcheckout.portal.excpromotions;

import java.util.Iterator;
import java.util.List;

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
import br.com.auster.tim.billcheckout.dao.ExclusivePromotionDAO;
import br.com.auster.tim.billcheckout.model.ExclusivePromotion;
import br.com.auster.dware.console.hibernate.HibernateEnabledAction;
import br.com.auster.web.indexing.utils.IndexingUtils;
import br.com.auster.web.utils.WebUtils;

/**
 * @author wsoares
 * @version $Id$
 *
 */
public class PromotionsPopupAction extends HibernateEnabledAction {



	private static final Logger log = Logger.getLogger(PromotionsPopupAction.class);


	private static final String FORWARD_DISPLAYLIST = "list";
	private static final String FORWARD_RELOAD = "reload";




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
			// Filter
			String filterType = (String) WebUtils.findRequestAttribute(_request, RequestScopeConstants.REQUEST_FILTERBY_KEY);
			String filterCondition = (String) WebUtils.findRequestAttribute(_request, RequestScopeConstants.REQUEST_FILTERCONDITION_KEY);
			// debugging these parameters
			log.debug("pageId is " + pageId);
			log.debug("moveTo is " + moveTo);
			log.debug("filterType is" + filterType);
			log.debug("filterCondition is" + filterCondition);
			// preparing to load information
			int displayLength = 50;
			int pageNbr = IndexingUtils.getDisplayPageId(Integer.parseInt(pageId), Integer.parseInt(moveTo));
			int offset = IndexingUtils.getStartingElement(pageNbr, displayLength);
			fetch.clearOrder();
			fetch.setOffset(offset);
			fetch.setSize(displayLength);
			fetch.addOrder("description", true);
			log.debug("offset is " + offset);
			log.debug("fetchSize is " + displayLength);
			// loading data
			session = this.openConnection();
			Criteria criteria = session.createCriteria(ExclusivePromotion.class);
			if ((filterType != null && filterType.trim().length() > 0) && (filterCondition != null && filterCondition.trim().length() > 0)) {
				criteria.add(Restrictions.ilike(filterType, "%"+filterCondition+"%"));
			}
			int totalPages = criteria.list().size();
			configureCriteria(criteria, fetch);
			List results = criteria.list();
			// forwarding attributes
			ExclusivePromotion promotionInfo = new ExclusivePromotion();
			String uid = (String) WebUtils.findRequestAttribute(_request, "uid");
			if (uid != null) {
				promotionInfo.setUid(Long.parseLong(uid));
				// loading current plans selected for this package
				ExclusivePromotionDAO dao = new ExclusivePromotionDAO();
				dao.loadPromotion2ForPromotion1(session.connection(), promotionInfo);
			}
			_request.setAttribute(RequestScopeConstants.REQUEST_REQID_KEY, promotionInfo);

			for (int i=0; i< results.size(); i++) {
				ExclusivePromotion excPromo = (ExclusivePromotion) results.get(i);
				if(excPromo.getUid() == promotionInfo.getUid()) {
					results.remove(i);
				}
			}

			log.debug("loaded " + results.size() + " out of " + totalPages + " elements for ExclusivePromotion.class");
			// setting info into request
			totalPages = IndexingUtils.getNumberOfPages(totalPages, displayLength);
			_request.setAttribute(RequestScopeConstants.REQUEST_PAGEID_KEY, String.valueOf(pageNbr));
			_request.setAttribute(RequestScopeConstants.REQUEST_TOTALPAGES_KEY, String.valueOf(totalPages));
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
	 * Reloads the current packageAction JSP with the selected information
	 */
	public ActionForward confirm(ActionMapping _mapping, ActionForm _form, HttpServletRequest _request, HttpServletResponse _response) throws Exception {
		Session session = null;
		try {
			session = openConnection();
			String uid = (String) WebUtils.findRequestAttribute(_request, "uid");
			ExclusivePromotion promotionInfo = new ExclusivePromotion();
			promotionInfo.setUid(Long.parseLong(uid));
			// updating package info with new plans assigned to it
			String[] promotionsUids = _request.getParameterValues("promotionsUid");
			if (promotionsUids != null && promotionsUids.length > 0) {
				for (int i=0; i < promotionsUids.length; i++) {
					promotionInfo.addPromotion(Long.parseLong(promotionsUids[i]), promotionsUids[i]);
				}
			}
			// saving modifications to database
			ExclusivePromotionDAO dao = new ExclusivePromotionDAO();
			dao.reassignPromotions(session.connection(), promotionInfo, _request.getParameterValues("displayedPromotionsUid"));
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

		//clean the filter condition field, because it was not performed a query using any kind of filter
		_request.setAttribute(RequestScopeConstants.REQUEST_FILTERCONDITION_KEY, "");

		return this.list(_mapping, _form, _request, _response);
	}
}
