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
 * Created on 16/11/2007
 */
package br.com.auster.tim.billcheckout.portal.bscs;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import br.com.auster.facelift.services.ServiceLocator;
import br.com.auster.tim.billcheckout.bscs.SearchAndStructure;
import br.com.auster.dware.console.hibernate.HibernateEnabledAction;


/**
 * TODO What this class is responsible for
 *
 * @author William Soares
 * @version $Id$
 * @since JDK1.4
 */
public class SearchParametersAction extends HibernateEnabledAction {

	private static final Logger log = Logger.getLogger(SearchParametersAction.class);
	
	private static final String CONFIGURED_PARAMETERS_REQUEST_KEY = "configured.parameters";

	private static final String FORWARD_REQUESTPARAM = "searchParameters";
	
	private static final String FORWARD_SHOW = "showResult";
	
	private static final String FORWARD_SHOW_SERV_HIST = "showServHistResult";
	
	
	protected ActionForward unspecified(ActionMapping _mapping, ActionForm _form, HttpServletRequest _request, HttpServletResponse _response) throws Exception {
		return this.getSearchParameters(_mapping, _form, _request, _response);
	}

	/**
	 * Lists all plans in the database.
	 */
	public ActionForward getSearchParameters(ActionMapping _mapping, ActionForm _form, HttpServletRequest _request, HttpServletResponse _response) throws Exception {		
		
		SearchAndStructure searchAndStructure = (SearchAndStructure) ServiceLocator.getInstance().getService("searchAndStructureImpl");
				
		_request.setAttribute(CONFIGURED_PARAMETERS_REQUEST_KEY, searchAndStructure.getAvailableSearchParameters());

		log.debug("Parâmetros configurados: " + searchAndStructure.getAvailableSearchParameters());
		
		DynaActionForm form = (DynaActionForm) _form;		
		
		if(form != null && ((String) form.get("searchType")).trim().equalsIgnoreCase("NAVIGATION")) {
			return _mapping.findForward(FORWARD_SHOW);
		} else if(form != null && ((String) form.get("searchType")).trim().equalsIgnoreCase("NAVIGATION_SERV_HIST")){
			return _mapping.findForward(FORWARD_SHOW_SERV_HIST);
		} else {
			return _mapping.findForward(FORWARD_REQUESTPARAM);
		}		

	}
		
}