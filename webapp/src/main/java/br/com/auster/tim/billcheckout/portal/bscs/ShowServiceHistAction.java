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
 * Created on 22/11/2007
 */
package br.com.auster.tim.billcheckout.portal.bscs;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.auster.dware.console.commons.RequestScopeConstants;
import br.com.auster.dware.console.hibernate.HibernateEnabledAction;
import br.com.auster.web.utils.WebUtils;

/**
 * TODO What this class is responsible for
 *
 * @author William Soares
 * @version $Id$
 * @since JDK1.4
 */
public class ShowServiceHistAction extends HibernateEnabledAction {

	private static final Logger log = Logger.getLogger(ShowServiceHistAction.class);

	private static final String FORWARD_DETAIL = "showServiceHist";
	
	private static final String SERVICE_HIST_SESSION_KEY = "service.hist";
	
	
	protected ActionForward unspecified(ActionMapping _mapping, ActionForm _form, HttpServletRequest _request, HttpServletResponse _response) throws Exception {
		return this.detail(_mapping, _form, _request, _response);
	}

	/**
	 * Lists all plans in the database.
	 */
	public ActionForward detail(ActionMapping _mapping, ActionForm _form, HttpServletRequest _request, HttpServletResponse _response) throws Exception {			

		String snCode = (String) WebUtils.findRequestAttribute(_request, RequestScopeConstants.REQUEST_REQID_KEY);
		String description = (String) WebUtils.findRequestAttribute(_request, RequestScopeConstants.REQUEST_REQINFO_KEY);	
		
		HttpSession session = _request.getSession();
		
		Map servicesHistMap = (Map) session.getAttribute(SERVICE_HIST_SESSION_KEY);
		
		Map serviceHist = (Map) servicesHistMap.get(snCode);
		
		_request.setAttribute(RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY, serviceHist);
		_request.setAttribute(RequestScopeConstants.REQUEST_REQID_KEY, snCode);
		_request.setAttribute(RequestScopeConstants.REQUEST_REQINFO_KEY, description);
						
		return _mapping.findForward(FORWARD_DETAIL);
				
	}	
	
}