/*
 * Copyright (c) 2004-2005 Auster Solutions do Brasil. All Rights Reserved.
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
 * Created on 12/09/2006
 */
/***
 * This class filters out objects to next filter in the processObjects(Object obj) method call.
 * This filtering is configuration based, and expects two parameters:
 * 1.- input-list-tag (Optional)
 * 2.- xpath-file (Mandatory)
 * 
 * During this filter processElement(Object _objects) method execution, it expects
 * _objects to a Map. If it is not a Map OR if the input-list-tag was not informed
 * , the processing goes like this filter did not exist.
 * 
 * If it is map, we try to get the Key indicatd in the input-list tag parameter.
 * If not found, the processing continues like this filter did not exist.
 * If found, the value of that key is expected to be a List with objects (Any type).
 * If it is not a List, the processing goes like this filter did not exist.
 * If it is a List, for each object a protected method {@see filterOut(JXPathContext context)}
 * is called returning a number	representing the number of matches(Exlained later) found.
 * 
 * The context is in the parameter is created from each object in the List.
 * 
 * Here comes the second parameter of the configuration.
 * This file is expected to have a root tag <xpaths>.
 * Inside the root, an endless number of tags <xpath source="" target""/>
 * Both attributes are xpaths.
 * source attribute is mandatory an is a xpath that must evaluates to:
 * 	true or false or null or an object.
 * target is optional and represents also a xpath.
 * 
 * IF the result of applying source xpath to the object is false or null, nothing happens.
 * Otherwise (True or an objects is returned), we do:
 * 	Increments the matcher count.
 * 	Verify if target xpath was informed, and if not skip processing to next source xpath.
 *  If target xpath exists, we use it to set the target object to null.
 *  
 *  And finally if the returning matches number is greater than zero, the processObjects to the
 *  next filter is not done.
 *  
 * 
 */
package br.com.auster.tim.billcheckout.filter;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import br.com.auster.common.io.IOUtils;
import br.com.auster.common.xml.DOMUtils;
import br.com.auster.dware.graph.ConnectException;
import br.com.auster.dware.graph.DefaultFilter;
import br.com.auster.dware.graph.FilterException;
import br.com.auster.dware.graph.ObjectProcessor;

/**
 * @author mtengelm
 * @version $Id: ObjectDropperFilter.java 79 2006-09-15 20:07:58Z mtengelm $
 */
public class ObjectDropperFilter extends DefaultFilter implements ObjectProcessor {

	private Logger log = Logger.getLogger(this.getClass());
	
	private static final String	XPATH_FILE_ATTR				= "xpath-file";
	public static final String	INPUTMAP_LISTKEY_ATTR	= "input-list-tag";
	private static final String	ENC_ATTR							= "encrypted";
	private static final String	XPATH_ELEMENTS				= "xpath";
	private static final String	SOURCE_ATTR						= "source";
	private static final String	TARGET_ATTR						= "target";

	private ObjectProcessor			objProcessor;
	private String							listKey;
	private Map									xpaths								= new HashMap();

	/**
	 * @param name
	 */
	public ObjectDropperFilter(String name) {
		super(name);
	}

	/**
	 * @see br.com.auster.dware.graph.DefaultFilter#configure(org.w3c.dom.Element)
	 */
	public void configure(Element _configuration) throws FilterException {
		String xpathFile = DOMUtils.getAttribute(_configuration, XPATH_FILE_ATTR, true);
		boolean enc = DOMUtils.getBooleanAttribute(_configuration, ENC_ATTR);
		try {
			Element element = DOMUtils.openDocument(IOUtils.openFileForRead(xpathFile, enc));
			NodeList elements = DOMUtils.getElements(element, XPATH_ELEMENTS);
			int qty = elements.getLength();
			for (int i = 0; i < qty; i++) {
				Element xpath = (Element) elements.item(i);
				this.xpaths.put(DOMUtils.getAttribute(xpath, SOURCE_ATTR, true), DOMUtils
						.getAttribute(xpath, TARGET_ATTR, false));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}

		this.listKey = DOMUtils.getAttribute(_configuration, INPUTMAP_LISTKEY_ATTR, false);
	}

	/**
	 * @see br.com.auster.dware.graph.ObjectProcessor#processElement(java.lang.Object)
	 */
	public void processElement(Object _objects) throws FilterException {
		Object obj = _objects;
		int founds = 0;
		if (_objects instanceof Map) {
			Map map = (Map) _objects;
			// List omlist = (List) map.get(this.listKey);
			Object value = map.get(this.listKey);
			List omlist;
			if (value instanceof List) {
				omlist = (List) value;
				for (Iterator itr = omlist.iterator(); itr.hasNext();) {
					Object om = itr.next();
					JXPathContext context = JXPathContext.newContext(om);
					context.setLenient(true);
					int ret = filterOut(context);
					if (ret != 0) {
						log.warn("Dropping an object due to match in dropper filter=>" + om);
					}
					founds += ret;
				}
			}
		}
		if (founds == 0) {
			this.objProcessor.processElement(obj);
		} 			
	}

	/**
	 * @param omlist
	 * @return
	 */
	protected int filterOut(JXPathContext context) {
		int retval = 0;

		for (Iterator itrPath = this.xpaths.entrySet().iterator(); itrPath.hasNext();) {
			Map.Entry entry = (Entry) itrPath.next();
			String source = (String) entry.getKey();
			String target = (String) entry.getValue();
			Object retSource = context.getValue(source);
			boolean burn = false;
			if (retSource instanceof Boolean) {
				burn = ((Boolean) retSource).booleanValue();
			} else if (retSource != null) {
				burn = true;
			}
			if (burn) {
				retval++;
				if ((!target.equals(""))) {
					context.setValue(target, null);
				}
			}
		}
		return retval;
	}

	/**
	 * @see br.com.auster.dware.filter.DefaultFilter#getInput(java.lang.String)
	 */
	public Object getInput(String filterName) throws ConnectException,
			UnsupportedOperationException {
		return this;
	}

	/**
	 * Sets the Output for this filter.
	 * 
	 */
	public void setOutput(String sourceName, Object objProcessor) {
		this.objProcessor = (ObjectProcessor) objProcessor;
	}

}
