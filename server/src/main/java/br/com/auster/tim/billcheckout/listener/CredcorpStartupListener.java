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
 * Created on 27/08/2007
 */
package br.com.auster.tim.billcheckout.listener;

import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import br.com.auster.dware.DataAware;
import br.com.auster.dware.StartupListener;
import br.com.auster.dware.graph.Request;

/**
 * @author framos
 * @version $Id$
 *
 */
public class CredcorpStartupListener implements StartupListener {


	private static final Logger log = Logger.getLogger(CredcorpStartupListener.class);


	public CredcorpStartupListener(Element _configuration) {}



	public void afterConfig(DataAware _dware, Element _config) {}
	public void beforeConfig(DataAware _dware, Element _config) {}

	public void afterEnqueue(DataAware _dware, Request _request) {}
	public void afterEnqueue(DataAware _dware, Collection<Request> _requests) {}

	public final boolean beforeEnqueue(DataAware _dware, Request _request) {
		String custCode = (String) _request.getAttributes().get("custCode");
		log.debug("Found custCode " + custCode);
		if (custCode != null) {
			log.debug("setting custCodeWithSlashes as '" + custCode.replaceAll("\\.", "/") + "'");
			_request.getAttributes().put("custCodeWithSlashes", custCode.replaceAll("\\.", "/"));
		}
		return true;
	}

	public boolean beforeEnqueue(DataAware _dware, Collection<Request> _requests) {
		for (Iterator<Request> it = _requests.iterator(); it.hasNext(); ) {
			Request request = it.next();
			beforeEnqueue(_dware, request);
		}
		return true;
	}
}
