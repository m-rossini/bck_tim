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
import br.com.auster.tim.billcheckout.dao.PromotionPackageDAO;
import br.com.auster.tim.billcheckout.model.PromotionPackage;
import br.com.auster.web.indexing.utils.IndexingUtils;
import br.com.auster.web.utils.WebUtils;
import br.com.auster.dware.console.hibernate.HibernateEnabledAction;

/**
 * @author framos
 * @version $Id$
 *
 */
public class PackageAction extends HibernateEnabledAction {



	private static final Logger log = Logger.getLogger(PackageAction.class);



	private static final String FORWARD_DISPLAYLIST = "list";
	private static final String FORWARD_DONE = "done";
	private static final String FORWARD_DETAIL = "detail";
	private static final String FORWARD_ERROR = "error";




	protected ActionForward unspecified(ActionMapping _mapping, ActionForm _form, HttpServletRequest _request, HttpServletResponse _response) throws Exception {
		return this.list(_mapping, _form, _request, _response);
	}

	/**
	 * Lists all packages in the database.
	 *
	 * This listing function allows paging and sorting operations. Also, there is a hyperlink to delete a package.
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
			// debuggin these parameters
			log.debug("pageId is " + pageId);
			log.debug("moveTo is " + moveTo);
			log.debug("orderBy is " + orderBy);
			log.debug("orderWay is " + orderWay);
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
			Criteria criteria = session.createCriteria(PromotionPackage.class);
			int totalPages = criteria.list().size();
			configureCriteria(criteria, fetch);
			List results = criteria.list();
			log.debug("loaded " + results.size() + " out of " + totalPages + " elements for PromotionPackage.class");
			// setting info into request
			totalPages = IndexingUtils.getNumberOfPages(totalPages, displayLength);
			_request.setAttribute(RequestScopeConstants.REQUEST_PAGEID_KEY, String.valueOf(pageNbr));
			_request.setAttribute(RequestScopeConstants.REQUEST_TOTALPAGES_KEY, String.valueOf(totalPages));
			_request.setAttribute(RequestScopeConstants.REQUEST_ORDERBY_KEY, (orderBy == null ? "customerType" : orderBy) );
			_request.setAttribute(RequestScopeConstants.REQUEST_ORDERWAY_KEY, orderWay);
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
	 * This action is executed everytime a delete operation (from the list page) is confirmed by some user.
	 */
	public ActionForward delete(ActionMapping _mapping, ActionForm _form, HttpServletRequest _request, HttpServletResponse _response) {
		ActionMessages messages = new ActionMessages();
		Session session = null;
		try {
			String packageUid = (String) WebUtils.findRequestAttribute(_request, RequestScopeConstants.REQUEST_REQID_KEY);
			session = this.openConnection();
			Criteria criteria = session.createCriteria(PromotionPackage.class);
			configureCriteria(criteria, null, new Long(packageUid));
			PromotionPackage packageInfo = (PromotionPackage) criteria.uniqueResult();
			if (packageInfo == null) {
				// could not read package information from request
				throw new IllegalArgumentException("Could not read package information for uid " + packageUid);
			}
			// removing package info
			session.delete(packageInfo);
			_request.setAttribute(RequestScopeConstants.REQUEST_REQID_KEY, packageInfo);
			_request.setAttribute(RequestScopeConstants.REQUEST_OPERATION_DATA, RequestScopeConstants.REQUEST_OPERATION_TYPE_DELETE);
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
	 * Prepares the packageAction JSP. If an insert action was selected, then it will send to that page the INSERT operation token and no
	 * 	package information. When we are handling update actions, then the UPDATE token is sent, and the details for the selected package
	 *  are loaded and posted to the page too.
	 */
	public ActionForward detail(ActionMapping _mapping, ActionForm _form, HttpServletRequest _request, HttpServletResponse _response) {
		ActionMessages messages = new ActionMessages();
		Session session = null;
		try {
			String packageUid = (String) WebUtils.findRequestAttribute(_request, RequestScopeConstants.REQUEST_REQID_KEY);
			if ((packageUid == null) || ("".equals(packageUid))) {
				// if INSERT action or parameter is NULL, treat as INSERT
				_request.setAttribute(RequestScopeConstants.REQUEST_OPERATION_DATA, RequestScopeConstants.REQUEST_OPERATION_TYPE_INSERT);
				_request.setAttribute(RequestScopeConstants.REQUEST_REQID_KEY, new PromotionPackage());
			} else {
				// or handling UPDATE action
				session = this.openConnection();
				Criteria criteria = session.createCriteria(PromotionPackage.class);
				configureCriteria(criteria, null, new Long(packageUid));
				PromotionPackage packageInfo = (PromotionPackage) criteria.uniqueResult();
				if (packageInfo == null) {
					// could not read package information from request
					throw new IllegalArgumentException("Could not read package information for uid " + packageUid);
				}
				_request.setAttribute(RequestScopeConstants.REQUEST_REQID_KEY, packageInfo);
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
		try {
			PromotionPackage packageInfo = getPromotionPackage((DynaActionForm) _form);
			PromotionPackage loadedPackage = null;
			session = this.openConnection();
			if (packageInfo.getUid() > 0) {
				Criteria criteria = session.createCriteria(PromotionPackage.class);
				configureCriteria(criteria, null, new Long(packageInfo.getUid()));
				loadedPackage = (PromotionPackage)criteria.uniqueResult();
				// moving updated info to loaded object
				loadedPackage.setDescription(packageInfo.getDescription());
				loadedPackage.setCustom1(packageInfo.getCustom1());
				loadedPackage.setCustom2(packageInfo.getCustom2());
				loadedPackage.setCustom3(packageInfo.getCustom3());
			} else {
				// else, just point loaded to new package info
				loadedPackage = packageInfo;
			}
			session.saveOrUpdate(loadedPackage);
			session.flush();
			// updating assigned packages
//			PromotionPackageDAO dao = new PromotionPackageDAO();
//			dao.reassignPlans(session.connection(), loadedPackage);

			_request.setAttribute(RequestScopeConstants.REQUEST_REQID_KEY, loadedPackage);
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
		return _mapping.findForward(FORWARD_DONE);
	}

	/**
	 * Builds a PromotionPackage instance out of the posted HTML dynamic form
	 */
	private PromotionPackage getPromotionPackage(DynaActionForm _dynaForm) {
		if (_dynaForm == null) { return null; }
		try {
			PromotionPackage packageInfo = new PromotionPackage();
			packageInfo.setUid( Long.parseLong((String)_dynaForm.get("uid")) );
			packageInfo.setShortDescription((String)_dynaForm.get("shortDescription"));
			packageInfo.setDescription((String)_dynaForm.get("description"));
			return packageInfo;
		} catch (Exception e) {
			throw new IllegalArgumentException("Could not read package information from form", e);
		}
	}
}
