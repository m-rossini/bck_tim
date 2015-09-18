/*
 * Copyright (c) 2004-2010 Auster Solutions. All Rights Reserved.
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
 * Created on 17/05/2010
 */
package br.com.auster.tim.billcheckout.portal.tariff;

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
import org.hibernate.Session;

import br.com.auster.dware.console.commons.RequestScopeConstants;
import br.com.auster.dware.console.error.PortalRuntimeException;
import br.com.auster.dware.console.hibernate.HibernateEnabledAction;
import br.com.auster.tim.billcheckout.tariff.dao.DataUsageTariffDAO;
import br.com.auster.tim.billcheckout.tariff.dao.MeuSonhoTariffDAO;
import br.com.auster.tim.billcheckout.tariff.dao.MicrocellTariffDAO;
import br.com.auster.tim.billcheckout.tariff.dao.NPackTariffDAO;
import br.com.auster.tim.billcheckout.tariff.dao.PackageUsageTariffDAO;
import br.com.auster.tim.billcheckout.tariff.dao.ServiceUsageTariffDAO;
import br.com.auster.tim.billcheckout.tariff.dao.VoiceUsageTariffDAO;
import br.com.auster.tim.billcheckout.tariff.model.MeuSonhoTariff;
import br.com.auster.web.utils.WebUtils;

/**
 * @author anardo
 */
public class TariffsPopupAction extends HibernateEnabledAction {

	private static final Logger log = Logger.getLogger(TariffsPopupAction.class);

	private static final String FORWARD_HISTORY_DATA = "historyData";
	private static final String FORWARD_HISTORY_VOICE = "historyVoice";
	private static final String FORWARD_HISTORY_SERVICE = "historyService";
	private static final String FORWARD_HISTORY_PACKAGE = "historyPackage";
	private static final String FORWARD_HISTORY_MC = "historyMicrocell";
	private static final String FORWARD_HISTORY_NPACK = "historyNPack";
	private static final String FORWARD_HISTORY_MEUSONHO = "meuSonhoList";
	
	private static final int VOICE_ACTION = 1;
	private static final int DATA_ACTION = 2;
	private static final int SERVICE_ACTION = 3;
	private static final int PACKAGE_ACTION = 4;
	private static final int MC_ACTION = 5;
	private static final int NPACK_ACTION = 6;
	private static final int MEUSONHO_ACTION = 7;
	
	
	
	
	public ActionForward listHistMC(ActionMapping _mapping, ActionForm _form, HttpServletRequest _request, HttpServletResponse _response) throws Exception {
		try {
			String selectedPlanUID = (String) _request.getSession().getAttribute(TariffPagesConstants.SELECTED_PLAN_UID);
			String selectedRow	= (String) WebUtils.findRequestAttribute(_request, TariffPagesConstants.SELECTED_ROW);
			_request.setAttribute(RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY, loadListHistory(selectedPlanUID, selectedRow, MC_ACTION));
			return chooseActionToReturnTo(MC_ACTION, _mapping);
		} catch (Exception e) {
			ActionMessages messages = new ActionMessages();
			messages.add(Globals.MESSAGE_KEY, new ActionMessage("error.genericError", e));
			saveMessages(_request, messages);
			log.error("Error executing method listHistMC", e);
			throw new PortalRuntimeException(e);
		}
	}
	
	public ActionForward listHistVoice(ActionMapping _mapping, ActionForm _form, HttpServletRequest _request, HttpServletResponse _response) throws Exception {
		try {
			String selectedPlanUID = (String) _request.getSession().getAttribute(TariffPagesConstants.SELECTED_PLAN_UID);
			String selectedRow     = (String) WebUtils.findRequestAttribute(_request, TariffPagesConstants.SELECTED_ROW);
			_request.setAttribute(RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY, loadListHistory(selectedPlanUID, selectedRow, VOICE_ACTION));
			return chooseActionToReturnTo(VOICE_ACTION, _mapping);
		} catch (Exception e) {
			ActionMessages messages = new ActionMessages();
			messages.add(Globals.MESSAGE_KEY, new ActionMessage("error.genericError", e));
			saveMessages(_request, messages);
			log.error("Error executing method listHistVoice", e);
			throw new PortalRuntimeException(e);
		}
	}
	
	public ActionForward listHistData(ActionMapping _mapping, ActionForm _form, HttpServletRequest _request, HttpServletResponse _response) throws Exception {
		try {
			String selectedPlanUID = (String) _request.getSession().getAttribute(TariffPagesConstants.SELECTED_PLAN_UID);
			String selectedRow     = (String) WebUtils.findRequestAttribute(_request, TariffPagesConstants.SELECTED_ROW);
			_request.setAttribute(RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY, loadListHistory(selectedPlanUID, selectedRow, DATA_ACTION));
			return chooseActionToReturnTo(DATA_ACTION, _mapping);
		} catch (Exception e) {
			ActionMessages messages = new ActionMessages();
			messages.add(Globals.MESSAGE_KEY, new ActionMessage("error.genericError", e));
			saveMessages(_request, messages);
			log.error("Error executing method listHistData", e);
			throw new PortalRuntimeException(e);
		}
	}
	
	public ActionForward listHistService(ActionMapping _mapping, ActionForm _form, HttpServletRequest _request, HttpServletResponse _response) throws Exception {
		try {
			String selectedPlanUID = (String) _request.getSession().getAttribute(TariffPagesConstants.SELECTED_PLAN_UID);
			String selectedRow     = (String) WebUtils.findRequestAttribute(_request, TariffPagesConstants.SELECTED_ROW);
			_request.setAttribute(RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY, loadListHistory(selectedPlanUID, selectedRow, SERVICE_ACTION));
			return chooseActionToReturnTo(SERVICE_ACTION, _mapping);
		} catch (Exception e) {
			ActionMessages messages = new ActionMessages();
			messages.add(Globals.MESSAGE_KEY, new ActionMessage("error.genericError", e));
			saveMessages(_request, messages);
			log.error("Error executing method listHistService", e);
			throw new PortalRuntimeException(e);
		}
	}
	
	public ActionForward listHistPackage(ActionMapping _mapping, ActionForm _form, HttpServletRequest _request, HttpServletResponse _response) throws Exception {
		try {
			String selectedPlanUID = (String) _request.getSession().getAttribute(TariffPagesConstants.SELECTED_PLAN_UID);
			String selectedRow     = (String) WebUtils.findRequestAttribute(_request, TariffPagesConstants.SELECTED_ROW);
			_request.setAttribute(RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY, loadListHistory(selectedPlanUID, selectedRow, PACKAGE_ACTION));
			return chooseActionToReturnTo(PACKAGE_ACTION, _mapping);
		} catch (Exception e) {
			ActionMessages messages = new ActionMessages();
			messages.add(Globals.MESSAGE_KEY, new ActionMessage("error.genericError", e));
			saveMessages(_request, messages);
			log.error("Error executing method listHistPackage", e);
			throw new PortalRuntimeException(e);
		}
	}
	
	public ActionForward listHistNPack(ActionMapping _mapping, ActionForm _form, HttpServletRequest _request, HttpServletResponse _response) throws Exception {
		try {
			String selectedPlanUID = (String) _request.getSession().getAttribute(TariffPagesConstants.SELECTED_PLAN_UID);
			String selectedRow     = (String) WebUtils.findRequestAttribute(_request, TariffPagesConstants.SELECTED_ROW);
			_request.setAttribute(RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY, loadListHistory(selectedPlanUID, selectedRow, NPACK_ACTION));
			return chooseActionToReturnTo(NPACK_ACTION, _mapping);
		} catch (Exception e) {
			ActionMessages messages = new ActionMessages();
			messages.add(Globals.MESSAGE_KEY, new ActionMessage("error.genericError", e));
			saveMessages(_request, messages);
			log.error("Error executing method listHistNPack", e);
			throw new PortalRuntimeException(e);
		}
	}

	public ActionForward listMeuSonho(ActionMapping _mapping, ActionForm _form, HttpServletRequest _request, HttpServletResponse _response) throws Exception {
		try {
			String selectedPlan = (String) WebUtils.findRequestAttribute(_request, TariffPagesConstants.SELECTED_RATE_PLAN);
			String[] array = selectedPlan.split("-");
			String selectedRow     = (String) WebUtils.findRequestAttribute(_request, TariffPagesConstants.SELECTED_ROW);
			List results = loadListHistory(array[2], selectedRow, MEUSONHO_ACTION);			
			_request.setAttribute(RequestScopeConstants.REQUEST_LISTOFRESULTS_KEY, ((MeuSonhoTariff) results.get(0)).getTariffZoneList() );
			return chooseActionToReturnTo(MEUSONHO_ACTION, _mapping);
		} catch (Exception e) {
			ActionMessages messages = new ActionMessages();
			messages.add(Globals.MESSAGE_KEY, new ActionMessage("error.genericError", e));
			saveMessages(_request, messages);
			log.error("Error executing method listHistNPack", e);
			throw new PortalRuntimeException(e);
		}
	}
	
	private List loadListHistory(String _selectedPlanUID, String _selectedRow, int _action) throws Exception {
		Session session = null;
		try {
				// loading list of tariffs
				if(_selectedRow != null && _selectedRow.length()!=0){
					session = this.openConnection();
					
					switch (_action) {
					case VOICE_ACTION:
						VoiceUsageTariffDAO dao1 = new VoiceUsageTariffDAO();
						return dao1.listRates(session.connection(), new Long(_selectedPlanUID), _selectedRow); 
					case DATA_ACTION:
						DataUsageTariffDAO dao2 = new DataUsageTariffDAO();
						return dao2.listRates(session.connection(), new Long(_selectedPlanUID), _selectedRow); 
					case SERVICE_ACTION :
						ServiceUsageTariffDAO dao3 = new ServiceUsageTariffDAO();
						return dao3.listRates(session.connection(), new Long(_selectedPlanUID), _selectedRow);
					case PACKAGE_ACTION :
						PackageUsageTariffDAO dao4 = new PackageUsageTariffDAO();
						return dao4.listRates(session.connection(), new Long(_selectedPlanUID), _selectedRow); 
					case MC_ACTION:
						MicrocellTariffDAO dao5 = new MicrocellTariffDAO();
						return dao5.listRates(session.connection(), new Long(_selectedPlanUID), _selectedRow);
					case NPACK_ACTION:
						NPackTariffDAO dao6 = new NPackTariffDAO();
						return dao6.listRates(session.connection(), new Long(_selectedPlanUID), _selectedRow);
					case MEUSONHO_ACTION:
						MeuSonhoTariffDAO dao7 = new MeuSonhoTariffDAO();
						return dao7.listRates(session.connection(), _selectedPlanUID, _selectedRow);
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
			return _mapping.findForward(FORWARD_HISTORY_VOICE);
		case DATA_ACTION:
			return _mapping.findForward(FORWARD_HISTORY_DATA);
		case SERVICE_ACTION :
			return _mapping.findForward(FORWARD_HISTORY_SERVICE);
		case PACKAGE_ACTION :
			return _mapping.findForward(FORWARD_HISTORY_PACKAGE);
		case MC_ACTION:
			return _mapping.findForward(FORWARD_HISTORY_MC);
		case NPACK_ACTION:
			return _mapping.findForward(FORWARD_HISTORY_NPACK);
		case MEUSONHO_ACTION:
			return _mapping.findForward(FORWARD_HISTORY_MEUSONHO);
		}
		return null;
	}

}
