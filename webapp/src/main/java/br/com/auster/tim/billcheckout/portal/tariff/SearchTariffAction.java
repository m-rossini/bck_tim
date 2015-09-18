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
package br.com.auster.tim.billcheckout.portal.tariff;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;

import br.com.auster.dware.console.commons.RequestScopeConstants;
import br.com.auster.dware.console.error.PortalRuntimeException;
import br.com.auster.dware.console.hibernate.HibernateEnabledAction;
import br.com.auster.tim.billcheckout.dao.BckUfDAO;
import br.com.auster.tim.billcheckout.model.Plan;
import br.com.auster.tim.billcheckout.tariff.dao.DataUsageTariffDAO;
import br.com.auster.tim.billcheckout.tariff.dao.MeuSonhoTariffDAO;
import br.com.auster.tim.billcheckout.tariff.dao.MicrocellTariffDAO;
import br.com.auster.tim.billcheckout.tariff.dao.NPackTariffDAO;
import br.com.auster.tim.billcheckout.tariff.dao.PackageUsageTariffDAO;
import br.com.auster.tim.billcheckout.tariff.dao.ServiceUsageTariffDAO;
import br.com.auster.tim.billcheckout.tariff.dao.VoiceUsageTariffDAO;
import br.com.auster.web.utils.WebUtils;

/**
 * @author framos
 * @version $Id$
 *
 */
public class SearchTariffAction extends HibernateEnabledAction {

	private static final Logger log = Logger.getLogger(SearchTariffAction.class);

	private static final String FORWARD_DISPLAYLIST_DATA = "listData";
	private static final String FORWARD_DISPLAYLIST_VOICE = "listVoice";
	private static final String FORWARD_DISPLAYLIST_SERVICE = "listService";
	private static final String FORWARD_DISPLAYLIST_PACKAGE = "listPackage";
	private static final String FORWARD_DISPLAYLIST_MC = "listMicrocell";
	private static final String FORWARD_DISPLAYLIST_NPACK = "listNPack";
	private static final String FORWARD_DISPLAYLIST_MEUSONHO = "listMeuSonho";
	private static final String FORWARD_DISPLAYSEARCH = "search";

	
	private static final int VOICE_ACTION = 1;
	private static final int DATA_ACTION = 2;
	private static final int SERVICE_ACTION = 3;
	private static final int PACKAGE_ACTION = 4;
	private static final int MC_ACTION = 5;
	private static final int NPACK_ACTION = 6;
	private static final int MEUSONHO_ACTION = 7;
	
	protected ActionForward unspecified(ActionMapping _mapping, ActionForm _form, HttpServletRequest _request, HttpServletResponse _response) throws Exception {
		Session session = null;
		try {
			String uf = (String) WebUtils.findRequestAttribute(_request, TariffPagesConstants.SELECTED_PLAN_UF);
			// defining paging information
			session = this.openConnection();
			Criteria criteria = session.createCriteria(Plan.class);
			if (uf != null) {
				criteria.add( Expression.eq("state", uf));
				criteria.addOrder( Order.asc("description"));
				List allPlans = criteria.list();
				log.debug("loaded " + allPlans.size() + " elements for Plan.class");
				// setting info into request
				_request.setAttribute(RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY, allPlans);
				_request.setAttribute(TariffPagesConstants.SELECTED_PLAN_UF, uf);
			} else {
				BckUfDAO dao = new BckUfDAO();
				_request.setAttribute(RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY, dao.getUfs(session.connection()));
				_request.removeAttribute(TariffPagesConstants.SELECTED_PLAN_UF);
			}			
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
		return _mapping.findForward(FORWARD_DISPLAYSEARCH);
	}
	
	public ActionForward listVoice(ActionMapping _mapping, ActionForm _form, HttpServletRequest _request, HttpServletResponse _response) throws Exception {
		try {
			String selectedPlan = (String) WebUtils.findRequestAttribute(_request, TariffPagesConstants.SELECTED_RATE_PLAN);
			
			if ((selectedPlan != null) && (selectedPlan.indexOf("Meu Sonho") > 0)) {
				_request.getSession(false).setAttribute(TariffPagesConstants.PLAN_IS_MEUSONHO, TariffPagesConstants.PLAN_IS_MEUSONHO);
			} else {
				_request.getSession(false).removeAttribute(TariffPagesConstants.PLAN_IS_MEUSONHO);
			}
			
			_request.setAttribute(TariffPagesConstants.SELECTED_RATE_PLAN, selectedPlan);
			_request.setAttribute(RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY, loadListByAction(selectedPlan, VOICE_ACTION, _request.getSession(false)));
			return chooseActionToReturnTo(VOICE_ACTION, _mapping);
		} catch (Exception e) {
			ActionMessages messages = new ActionMessages();
			messages.add(Globals.MESSAGE_KEY, new ActionMessage("error.genericError", e));
			saveMessages(_request, messages);
			throw new PortalRuntimeException(e);
		}
	}
	
	public ActionForward listData(ActionMapping _mapping, ActionForm _form, HttpServletRequest _request, HttpServletResponse _response) throws Exception {
		try {
			String selectedPlan = (String) WebUtils.findRequestAttribute(_request, TariffPagesConstants.SELECTED_RATE_PLAN);
			_request.setAttribute(TariffPagesConstants.SELECTED_RATE_PLAN, selectedPlan);
			_request.setAttribute(RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY, loadListByAction(selectedPlan, DATA_ACTION, _request.getSession(false)));
			return chooseActionToReturnTo(DATA_ACTION, _mapping);
		} catch (Exception e) {
			ActionMessages messages = new ActionMessages();
			messages.add(Globals.MESSAGE_KEY, new ActionMessage("error.genericError", e));
			saveMessages(_request, messages);
			throw new PortalRuntimeException(e);
		}
	}

	public ActionForward listMC(ActionMapping _mapping, ActionForm _form, HttpServletRequest _request, HttpServletResponse _response) throws Exception {
		try {
			String selectedPlan = (String) WebUtils.findRequestAttribute(_request, TariffPagesConstants.SELECTED_RATE_PLAN);
			_request.setAttribute(TariffPagesConstants.SELECTED_RATE_PLAN, selectedPlan);
			_request.setAttribute(RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY, loadListByAction(selectedPlan, MC_ACTION, _request.getSession(false)));
			return chooseActionToReturnTo(MC_ACTION, _mapping);
		} catch (Exception e) {
			ActionMessages messages = new ActionMessages();
			messages.add(Globals.MESSAGE_KEY, new ActionMessage("error.genericError", e));
			saveMessages(_request, messages);
			throw new PortalRuntimeException(e);
		}
	}

	public ActionForward listNPack(ActionMapping _mapping, ActionForm _form, HttpServletRequest _request, HttpServletResponse _response) throws Exception {
		try {
			String selectedPlan = (String) WebUtils.findRequestAttribute(_request, TariffPagesConstants.SELECTED_RATE_PLAN);
			_request.setAttribute(TariffPagesConstants.SELECTED_RATE_PLAN, selectedPlan);
			_request.setAttribute(RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY, loadListByAction(selectedPlan, NPACK_ACTION, _request.getSession(false)));
			return chooseActionToReturnTo(NPACK_ACTION, _mapping);
		} catch (Exception e) {
			ActionMessages messages = new ActionMessages();
			messages.add(Globals.MESSAGE_KEY, new ActionMessage("error.genericError", e));
			saveMessages(_request, messages);
			throw new PortalRuntimeException(e);
		}
	}
	
	public ActionForward listService(ActionMapping _mapping, ActionForm _form, HttpServletRequest _request, HttpServletResponse _response) throws Exception {
		try {
			String selectedPlan = (String) WebUtils.findRequestAttribute(_request, TariffPagesConstants.SELECTED_RATE_PLAN);
			_request.setAttribute(TariffPagesConstants.SELECTED_RATE_PLAN, selectedPlan);
			_request.setAttribute(RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY, loadListByAction(selectedPlan, SERVICE_ACTION, _request.getSession(false)));
			return chooseActionToReturnTo(SERVICE_ACTION, _mapping);
		} catch (Exception e) {
			ActionMessages messages = new ActionMessages();
			messages.add(Globals.MESSAGE_KEY, new ActionMessage("error.genericError", e));
			saveMessages(_request, messages);
			throw new PortalRuntimeException(e);
		}
	}
	
	public ActionForward listPackage(ActionMapping _mapping, ActionForm _form, HttpServletRequest _request, HttpServletResponse _response) throws Exception {
		try {
			String selectedPlan = (String) WebUtils.findRequestAttribute(_request, TariffPagesConstants.SELECTED_RATE_PLAN);
			_request.setAttribute(TariffPagesConstants.SELECTED_RATE_PLAN, selectedPlan);
			_request.setAttribute(RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY, loadListByAction(selectedPlan, PACKAGE_ACTION, _request.getSession(false)));
			return chooseActionToReturnTo(PACKAGE_ACTION, _mapping);
		} catch (Exception e) {
			ActionMessages messages = new ActionMessages();
			messages.add(Globals.MESSAGE_KEY, new ActionMessage("error.genericError", e));
			saveMessages(_request, messages);
			throw new PortalRuntimeException(e);
		}
	}
	
	public ActionForward listMeuSonho(ActionMapping _mapping, ActionForm _form, HttpServletRequest _request, HttpServletResponse _response) throws Exception {
		try {
			String selectedPlan = (String) WebUtils.findRequestAttribute(_request, TariffPagesConstants.SELECTED_RATE_PLAN);
			_request.setAttribute(TariffPagesConstants.SELECTED_RATE_PLAN, selectedPlan);
			_request.setAttribute(RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY, loadListByAction(selectedPlan, MEUSONHO_ACTION, _request.getSession(false)));
			return chooseActionToReturnTo(MEUSONHO_ACTION, _mapping);
		} catch (Exception e) {
			ActionMessages messages = new ActionMessages();
			messages.add(Globals.MESSAGE_KEY, new ActionMessage("error.genericError", e));
			saveMessages(_request, messages);
			throw new PortalRuntimeException(e);
		}
	}
	
	private List loadListByAction(String _selectedPlan, int _action, HttpSession _session) throws Exception {
		Session session = null;
		try {
			// defining paging information
			if (_selectedPlan != null && !"".equals(_selectedPlan)){
				// saving info to session
				String[] array = _selectedPlan.split("-");
				_session.setAttribute(TariffPagesConstants.SELECTED_PLAN_UID,  array[0]); //objid do plano
				_session.setAttribute(TariffPagesConstants.SELECTED_PLAN_NAME, array[1]); //nome do plano
				_session.setAttribute(TariffPagesConstants.SELECTED_PLAN_UF,   array[2]); //state do plano
				// loading list of tariffs
				if(array!=null && array.length==3){
					session = this.openConnection();
					
					switch (_action) {
					case VOICE_ACTION:
						VoiceUsageTariffDAO dao1 = new VoiceUsageTariffDAO();
						return dao1.listRates(session.connection(), new Long(array[0]), null); 
					case DATA_ACTION:
						DataUsageTariffDAO dao2 = new DataUsageTariffDAO();
						return dao2.listRates(session.connection(), new Long(array[0]), null); 
					case SERVICE_ACTION :
						ServiceUsageTariffDAO dao3 = new ServiceUsageTariffDAO();
						return dao3.listRates(session.connection(), new Long(array[0]), null);
					case PACKAGE_ACTION :
						PackageUsageTariffDAO dao4 = new PackageUsageTariffDAO();
						return dao4.listRates(session.connection(), new Long(array[0]), null); 
					case MC_ACTION:
						MicrocellTariffDAO dao5 = new MicrocellTariffDAO();
						return dao5.listRates(session.connection(), new Long(array[0]), null);
					case NPACK_ACTION:
						NPackTariffDAO dao6 = new NPackTariffDAO();
						return dao6.listRates(session.connection(), new Long(array[0]), null);
					case MEUSONHO_ACTION:
						MeuSonhoTariffDAO dao7 = new MeuSonhoTariffDAO();
						return dao7.listRates(session.connection(), array[2], null);
					}
				}
			}
		} finally {
			if (session != null) {
				session.flush();
				session.close();
			}
		}
		return null;
	}	
	
	private ActionForward chooseActionToReturnTo(int _action, ActionMapping _mapping) {
		switch (_action) {
		case VOICE_ACTION:
			return _mapping.findForward(FORWARD_DISPLAYLIST_VOICE);
		case DATA_ACTION:
			return _mapping.findForward(FORWARD_DISPLAYLIST_DATA);
		case SERVICE_ACTION :
			return _mapping.findForward(FORWARD_DISPLAYLIST_SERVICE);
		case PACKAGE_ACTION :
			return _mapping.findForward(FORWARD_DISPLAYLIST_PACKAGE);
		case MC_ACTION:
			return _mapping.findForward(FORWARD_DISPLAYLIST_MC);
		case NPACK_ACTION:
			return _mapping.findForward(FORWARD_DISPLAYLIST_NPACK);
		case MEUSONHO_ACTION:
			return _mapping.findForward(FORWARD_DISPLAYLIST_MEUSONHO);
		}
		return null;
	}

}
