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
 * Created on 21/11/2007
 */
package br.com.auster.tim.billcheckout.portal.bscs;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import br.com.auster.dware.console.commons.RequestScopeConstants;
import br.com.auster.dware.console.error.PortalRuntimeException;
import br.com.auster.dware.console.hibernate.HibernateEnabledAction;
import br.com.auster.facelift.services.ServiceLocator;
import br.com.auster.tim.billcheckout.bscs.Account;
import br.com.auster.tim.billcheckout.bscs.Subscriber;
import br.com.auster.tim.billcheckout.bscs.vo.AccountVO;
import br.com.auster.tim.billcheckout.bscs.vo.ContractVO;
import br.com.auster.tim.billcheckout.bscs.vo.ServiceVO;


/**
 * TODO What this class is responsible for
 *
 * @author William Soares
 * @version $Id$
 * @since JDK1.4
 */
public class ShowResultAction extends HibernateEnabledAction {
	
	private static final Logger log = Logger.getLogger(ShowResultAction.class);

	private static final String FORWARD_DETAIL_CONTRACT = "showContract";
	
	private static final String FORWARD_DETAIL_ACCOUNT = "showAccount";
	
	private static final String SERVICE_HIST_SESSION_KEY = "service.hist";
	
	private static final String SERVICES_HIST_SESSION_KEY = "services.hist";
	
	private static final String CONTRACT_NUMBER_SESSION_KEY = "contract.number";
	
	private static final String ACCOUNT_STRUCTURE_SESSION_KEY = "account.structure";
	
	
	protected ActionForward unspecified(ActionMapping _mapping, ActionForm _form, HttpServletRequest _request, HttpServletResponse _response) throws Exception {
		return this.getResultData(_mapping, _form, _request, _response);
	}

	/**
	 * Lists all plans in the database.
	 */
	public ActionForward getResultData(ActionMapping _mapping, ActionForm _form, HttpServletRequest _request, HttpServletResponse _response) throws Exception {		

		Subscriber subscriberImpl = (Subscriber) ServiceLocator.getInstance().getService("subscriberImpl");		
		Account accountImpl = (Account) ServiceLocator.getInstance().getService("accountImpl");
		
		log.debug("Implementações utilizadas: " + subscriberImpl.getClass());
		log.debug("Implementações utilizadas: " + accountImpl.getClass());
		
		DynaActionForm form = (DynaActionForm) _form;												
		HttpSession session = _request.getSession();
		
		log.debug("Parameter Name: " + (String)form.get("parameterName"));
		log.debug("Parameter Value: " + (String)form.get("parameterValue"));
		
		try {
			if (((String)form.get("parameterName")).equalsIgnoreCase("contractId")) {
				ContractVO contractVO = subscriberImpl.getContractVO((String)form.get("parameterName"), ((String)form.get("parameterValue")).trim());			
				_request.setAttribute(RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY, contractVO);
				Map servicesHistMap = new HashMap();
				for (Iterator it = contractVO.getServicesHistory().iterator(); it.hasNext();) {
					ServiceVO serviceVO = (ServiceVO) it.next();
					servicesHistMap.put(serviceVO.getSnCode().trim(), serviceVO.getServiceHistory());
				} 
				session.setAttribute(SERVICE_HIST_SESSION_KEY, servicesHistMap);
				session.setAttribute(SERVICES_HIST_SESSION_KEY, contractVO.getServicesHistory());
				session.setAttribute(CONTRACT_NUMBER_SESSION_KEY, contractVO.getContractNumber());
				session.setAttribute(ACCOUNT_STRUCTURE_SESSION_KEY, contractVO.getHierarchyStructure());
				
				return _mapping.findForward(FORWARD_DETAIL_CONTRACT);
			} else {
				AccountVO accountVO = accountImpl.getAccountVO((String)form.get("parameterName"), (String)form.get("parameterValue"));			
				_request.setAttribute(RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY, accountVO);		
				return _mapping.findForward(FORWARD_DETAIL_ACCOUNT);
			}
		} catch (Exception e) {
			log.error(e);
			throw new PortalRuntimeException(e.getMessage(), e);
		}
				
	}
		
}