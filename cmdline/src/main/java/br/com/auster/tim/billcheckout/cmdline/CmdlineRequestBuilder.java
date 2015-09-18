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
 * Created on Ago 16, 2007
 */
package br.com.auster.tim.billcheckout.cmdline;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.jms.JMSException;
import javax.naming.NamingException;

import org.apache.commons.cli.CommandLine;
import org.apache.log4j.Logger;

import br.com.auster.common.io.FileSet;
import br.com.auster.common.util.I18n;
import br.com.auster.dware.console.listener.RequestCreation;
import br.com.auster.facelift.requests.interfaces.RequestBuilder;
import br.com.auster.facelift.requests.model.Request;
import br.com.auster.facelift.requests.web.interfaces.WebRequestBuilder;
import br.com.auster.facelift.requests.web.model.NotificationEmail;
import br.com.auster.facelift.requests.web.model.WebRequest;


/**
 * 
 * @author framos
 * @version $Id$
 */
public class CmdlineRequestBuilder {

	
	public static final String SPLIT_REQUEST_TOKEN = "files.token.split";
	public static final String CMDLINE_DRYRUN_TOKEN =  "cmdline.request.dryrun";
	
	public static final String CONFIG_JNDI_QUEUE_NAME = "jndi.queue";
	public static final String CONFIG_JNDI_FACTORY_NAME = "jndi.factory";
	
	
	private static final Logger log = Logger.getLogger(CmdlineRequestBuilder.class);
	private static final I18n i18n = I18n.getInstance(CmdlineRequestBuilder.class);

	
	
	public void createRequest(CommandLine line) 
				throws JMSException, NamingException, FileNotFoundException, IOException { 
		
		log.info(i18n.getString("cmdline.reqmanager.newrequest"));
		
		// loading properties
		Properties p = new Properties();
		p.load( new FileInputStream( (String) line.getOptionValue("c")) );
		
		boolean shouldSplit = false;
		if (p.containsKey(SPLIT_REQUEST_TOKEN)) {
			shouldSplit = Boolean.parseBoolean(p.getProperty(SPLIT_REQUEST_TOKEN));
		}
		// if should do all unless send requests to server
		boolean dryRun = false;
		if (p.containsKey(CMDLINE_DRYRUN_TOKEN)) {
			dryRun = Boolean.parseBoolean(p.getProperty(CMDLINE_DRYRUN_TOKEN));
		}
		
		
		TokenSplitter splitter = new TokenSplitter(p);
		
		// files
		String filemask = line.getOptionValue("f");
		Map inputFiles = this.createFileList(filemask.trim(), splitter, shouldSplit);
		if ((inputFiles == null) || (inputFiles.size() <= 0)) {
			System.err.println(i18n.getString("cmdline.reqmanager.noFilesFound", filemask));
			System.exit(1);
		}
		
		List requests2Send = this.buildAllRequests(0, inputFiles, splitter, line.getOptionValue("l"));
		
		log.debug(i18n.getString("cmdline.reqmanager.requestObjectCreated"));

		String step = line.getOptionValue("s");
		for (Iterator it = requests2Send.iterator(); it.hasNext();) {			
			WebRequest request = (WebRequest) it.next();
			// notifications 
			if(line.hasOption("m")) {
				this.addNotifications(line.getOptionValue("m"), request);
			}
			// adding step to cycle information
			if (step != null) {
				String currentValue = (String) request.getAdditionalInformation().get(splitter.getTokenName(0));
				if (currentValue != null) {
					request.getAdditionalInformation().put(splitter.getTokenName(0), currentValue + "-" + step);
				} else {
					request.getAdditionalInformation().put(splitter.getTokenName(0), step);
				}
				request.getAdditionalInformation().put("mode.id", step);
			}
			if (!dryRun) {
				log.debug(i18n.getString("cmdline.reqmanager.callingBusinessObjects"));			
				JMSMessageSender sender = new JMSMessageSender(p.getProperty(CONFIG_JNDI_FACTORY_NAME));
				sender.connect(p, p.getProperty(CONFIG_JNDI_QUEUE_NAME));
				RequestCreation message = new RequestCreation(line.getOptionValue("u"), request);
				sender.sendMessage(message);
			} else {
				log.debug(i18n.getString("cmdline.reqmanager.dryrun"));
			}
			log.info(i18n.getString("cmdline.reqmanager.requestCreated"));
		}
		
	}
	
	

	private Map createFileList(String mask, TokenSplitter _splitter, boolean _shouldSplit) {

		log.info(i18n.getString("cmdline.reqmanager.creatingFileList", mask));
		List filelist = new ArrayList();
		String basedir = ".";
		if(mask.startsWith(File.separator)) {
			basedir = File.separator;
			mask = mask.substring(1);
		} else if(mask.charAt(1)==':') {
			basedir = mask.substring(0,3);
			mask = mask.substring(3);
		}
		File[] files = FileSet.getFiles(basedir, mask);

		Map<String, Object> fileListMap = new HashMap<String, Object>();		
		if (_shouldSplit) {
			// If should split
			for(int i = 0; i < files.length; i++) {
				String filename = files[i].getAbsolutePath();
				log.debug(i18n.getString("cmdline.reqmanager.fileFound", filename));
				
				Map<String, Object> fileMap = fileListMap;
				for(int j = 0; j < _splitter.getTokenLen()-1; j++) {
					// building maps of maps, until before last level
					String value = _splitter.getTokenValue(j, filename);
					if (!fileMap.containsKey(value)) {
						fileMap.put(value, new HashMap<String, Object>());
					}
					fileMap = (Map<String, Object>)fileMap.get(value);
				}
				// building last level
				String value = _splitter.getTokenValue(_splitter.getTokenLen()-1, filename);
				if (!fileMap.containsKey(value)) {
					fileMap.put(value, new ArrayList<String>());
				}
				((List<String>)fileMap.get(value)).add(filename);
			}
		} else {
			// If should NOT split
			fileListMap.put("", Arrays.asList(files));
		}
		log.info(i18n.getString("cmdline.reqmanager.totalFilesFound", new Integer(files.length)));
		return fileListMap;
	}
	

	private void addRequestList(WebRequestBuilder builder, List inputFiles, String cfgFile) {
		// accounts
		Map controllerMap = new HashMap();
		
		log.info(i18n.getString("cmdline.reqmanager.accountlist.creating"));
		RequestBuilder reqBuilder = new RequestBuilder();
		BGHParser parser = new BGHParser(cfgFile);
		for(Iterator i=inputFiles.iterator(); i.hasNext(); ) {
			String filename = (String) i.next();
			parser.parse(new File(filename));
			Map accounts = (Map) parser.getParsedInfo(BGHParser.PARSER_ACCOUNTMAP_INFO);
			for(Iterator j=accounts.entrySet().iterator(); j.hasNext(); ) {
				Map.Entry entry = (Map.Entry) j.next();
				// check if account was already found 
				if (! controllerMap.containsKey(entry.getKey())) {
					reqBuilder.buildRequest((String) entry.getKey());
					reqBuilder.addAddInfo("account.name", (String) entry.getValue());
					reqBuilder.addInputFile(filename);
					Request req = reqBuilder.getRequest();
					builder.addProcessingRequest(req);
					log.debug(i18n.getString("cmdline.reqmanager.accountlist.added", entry.getKey(), entry.getValue(), filename));
					// saving that account was processed
					controllerMap.put(entry.getKey(), entry.getValue());
				} else {
					log.error(i18n.getString("cmdline.reqmanager.accountAlreadyFound", entry.getKey()));
				}
			}
		}    
	}
	
	private void addNotifications(String _notificationList, WebRequest _request) {
		String[] mail = _notificationList.split(",");
		for(int i = 0; i < mail.length; i++) {
			NotificationEmail email = new NotificationEmail();
			email.setEmailAddress(mail[i]);
			_request.getNotifications().add(email);
			log.info(i18n.getString("cmdline.reqmanager.notification.added", mail[i]));
		}
	}
		
	private List buildAllRequests(int _level, Map _inputFiles, TokenSplitter _splitter, String _parserFile) {
		List allRequests = new ArrayList();
		for (Iterator it = _inputFiles.keySet().iterator(); it.hasNext(); ) {
			String value = (String) it.next();		
			if (_level == _splitter.getTokenLen()-1) {
				WebRequestBuilder builder = new WebRequestBuilder();
				builder.buildWebRequest(0);		
				// using always de default token name
				builder.addAddInfo(_splitter.getTokenName(0), value);
				
				log.debug(i18n.getString("cmdline.reqbuilder.buildingReqLevel", String.valueOf(_level), value));
				this.addRequestList(builder, (List) _inputFiles.get(value), _parserFile);
				Integer nbrOfAcct = new Integer(builder.getWebRequest().getProcessingRequests().size());
				log.info(i18n.getString("cmdline.reqmanager.accountlist.success", value, nbrOfAcct));
				WebRequest request = builder.getWebRequest();
				allRequests.add( request );
			} else {
				log.debug(i18n.getString("cmdline.reqbuilder.buildingReqLevel", String.valueOf(_level), value));
				List innerRequests = buildAllRequests(_level+1, (Map) _inputFiles.get(value), _splitter, _parserFile);
				for (Iterator it2 = innerRequests.iterator(); it2.hasNext(); ) {
					WebRequest request = (WebRequest) it2.next();
					String currentValue = (String) request.getAdditionalInformation().get(_splitter.getTokenName(0));
					if (currentValue != null) {
						request.getAdditionalInformation().put(_splitter.getTokenName(0), value + "-" + currentValue);
					} else {
						request.getAdditionalInformation().put(_splitter.getTokenName(0), value);
					}
				}
				log.debug(i18n.getString("cmdline.reqbuilder.buildingReqLevelSize", String.valueOf(_level), value, String.valueOf(innerRequests.size())));
				allRequests.addAll(innerRequests);
			}
		}
		return allRequests;
	}
	
	
}
