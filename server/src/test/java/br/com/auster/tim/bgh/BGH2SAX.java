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
 * Created on 01/08/2006
 */
package br.com.auster.tim.bgh;

import org.apache.log4j.xml.DOMConfigurator;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import br.com.auster.common.io.IOUtils;
import br.com.auster.common.io.NIOUtils;
import br.com.auster.common.xml.DOMUtils;
import br.com.auster.common.xml.sax.NIOInputSource;
import br.com.auster.om.invoice.Account;
import br.com.auster.tim.bgh.sax.TIMOMLoader;
import br.com.auster.udd.reader.TaggedFileReader;

/**
 * @author framos
 * @version $Id$
 *
 */
public class BGH2SAX {

	
	public static final String UDD_FILE = "bgh/ifd-udd.xml";
	public static final String HANDLER_FILE = "bgh/omloader.xml";
	public static final String LOGGER_FILE = "log4j.xml";
	
	public static final double MAX_VALUES_DIFFERENCE = 0.009;
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		if (args.length != 1) {
			System.out.println("Usage: BGH2SAX <bgh-file>");
			System.exit(1);
		}
		BGH2SAX main = new BGH2SAX();
		try {
			main.run(args[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Account run(String _file) throws Exception {
		DOMConfigurator.configure(DOMUtils.openDocument(LOGGER_FILE, false));
		// udd reader
		TaggedFileReader reader = new TaggedFileReader(DOMUtils.openDocument(IOUtils.openFileForRead(UDD_FILE)));
		// om loader
		TIMOMLoader loader = new TIMOMLoader();
		loader.configure(DOMUtils.openDocument(IOUtils.openFileForRead(HANDLER_FILE)));
		
		reader.setContentHandler(loader);
		
		// parsing document
		NIOInputSource in = new NIOInputSource(NIOUtils.openFileForRead(_file));
		reader.parse(in);

		return (Account)loader.getObjects().get(0);
	}	
	
	public String intoXML(String _file) throws Exception {
		TaggedFileReader reader = new TaggedFileReader(DOMUtils.openDocument(IOUtils.openFileForRead(UDD_FILE)));
		
		// xml dumper
		XMLDumper dumper = new XMLDumper();
		reader.setContentHandler(dumper);
		
		// parsing document
		NIOInputSource in = new NIOInputSource(NIOUtils.openFileForRead(_file));
		reader.parse(in);
		
		return dumper.getContent();
	}	
}


class XMLDumper implements ContentHandler {

	private StringBuilder content;
	private String lineBreak;
	
	public XMLDumper() {
		this.content = new StringBuilder();
		this.lineBreak = System.getProperty("line.separator");
	}
	
	public final String getContent() { return content.toString(); }
	

	public void startDocument() throws SAXException {}
	public void endDocument() throws SAXException {}

	public void startElement(String arg0, String arg1, String arg2, Attributes arg3) throws SAXException {
		this.content.append("<");
		this.content.append(arg1);
		for (int i=0; i < arg3.getLength(); i++) {
			this.content.append(" ");
			this.content.append(arg3.getLocalName(i));
			this.content.append("=\"");
			this.content.append(arg3.getValue(3));
			this.content.append("\"");
		}
		this.content.append(">");
		this.content.append(this.lineBreak);
	}

	public void endElement(String arg0, String arg1, String arg2) throws SAXException {
		this.content.append("</");
		this.content.append(arg1);
		this.content.append(">");
		this.content.append(this.lineBreak);
	}

	public void characters(char[] arg0, int arg1, int arg2) throws SAXException {}
	public void startPrefixMapping(String arg0, String arg1) throws SAXException {}
	public void endPrefixMapping(String arg0) throws SAXException {}
	public void ignorableWhitespace(char[] arg0, int arg1, int arg2) throws SAXException {}
	public void processingInstruction(String arg0, String arg1) throws SAXException {}
	public void setDocumentLocator(Locator arg0) {}
	public void skippedEntity(String arg0) throws SAXException {}

}