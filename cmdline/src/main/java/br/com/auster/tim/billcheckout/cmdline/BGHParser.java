/*
 * Copyright (c) 2004-2006 Auster Solutions. All Rights Reserved.
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
 * Created on 29/08/2006
 */
package br.com.auster.tim.billcheckout.cmdline;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import br.com.auster.common.io.IOUtils;
import br.com.auster.common.io.NIOUtils;
import br.com.auster.common.util.I18n;
import br.com.auster.common.xml.DOMUtils;
import br.com.auster.common.xml.sax.NIOInputSource;
import br.com.auster.dware.console.request.parsers.IncomingFileParser;
import br.com.auster.udd.reader.TaggedFileReader;

/**
 * @author framos
 * @version $Id$
 */
public class BGHParser implements IncomingFileParser, ContentHandler {
	
	
	private static final Logger log = Logger.getLogger(BGHParser.class);
	private static final I18n i18n = I18n.getInstance(BGHParser.class);
	
    public static final String PARSER_ACCOUNTMAP_INFO = "accounts";
    
    private String currentAccountId;
	private Map accounts;
    private TaggedFileReader inputHandler;
       
    
    
    public BGHParser(String _cfgFile) {
        this.accounts = new HashMap();
        try {
            if (_cfgFile != null) { 
                Element config = DOMUtils.openDocument(IOUtils.openFileForRead(_cfgFile));
                inputHandler = new TaggedFileReader(config);
            } else {
                log.error("UDD configuration file not found : " + _cfgFile);
            }
        } catch (Exception e) {
            log.error("could not initialize UDD parser", e);
        }
    }
    	
	/**
	 * @see br.com.auster.dware.console.request.parsers.IncomingFileParser#getParsedInfo(java.lang.String)
	 */
	public Object getParsedInfo(String _token) {
		if (PARSER_ACCOUNTMAP_INFO.equals(_token)) {
			return this.accounts;
		}
		return null;
	}

	/**
	 * @see br.com.auster.dware.console.request.parsers.IncomingFileParser#parse(java.io.File)
	 */
	public void parse(File _file) {
	       log.debug("starting to parse incoming file " + _file);
	        try {
	        	this.accounts.clear();
	            this.inputHandler.setContentHandler(this);
	            NIOInputSource input = new NIOInputSource(NIOUtils.openFileForRead(_file));                
	            this.inputHandler.parse(input);
	        } catch (Exception e) {
	            log.error("exception raised while parsing file", e);
	        } 
	}
	
	// SAX ContentHandler API methods
    public void endDocument() throws SAXException {}
    public void startDocument() throws SAXException {}
    public void characters(char[] ch, int start, int length) throws SAXException {}
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {}
    public void endPrefixMapping(String prefix) throws SAXException {}
    public void skippedEntity(String name) throws SAXException {}
    public void setDocumentLocator(Locator locator) {}
    public void processingInstruction(String target, String data) throws SAXException {}
    public void startPrefixMapping(String prefix, String uri) throws SAXException {}
	public void endElement(String arg0, String arg1, String arg2) throws SAXException {}
    
    public void startElement(String namespaceURI, String _localName, String qName, Attributes atts) throws SAXException {
    	// found a new account
        if ("GeneralInformation".equals(_localName)) {
            this.currentAccountId = atts.getValue("custCode");
        // found account name
        } else if (("Header".equals(_localName) || "NFHeader".equals(_localName)) && (this.currentAccountId != null)) {
        	if (!this.accounts.containsKey(this.currentAccountId)) {
        		this.accounts.put(this.currentAccountId, atts.getValue("custAddrName"));
        		this.currentAccountId = null;
        	} else {
        		log.warn(i18n.getString("cmdline.bghparser.duplicateAccount", this.currentAccountId));
        	}
        }
    }


}
