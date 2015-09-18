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
 * Created on 10/10/2007
 */
package br.com.auster.tim.billcheckout.util;

import java.io.File;
import java.io.FilenameFilter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.auster.common.io.IOUtils;

import com.sun.xml.fastinfoset.tools.FI_SAX_XML;
import com.thoughtworks.xstream.XStream;

/**
 * @author framos
 * @version $Id$
 *
 */
public class XStreamLoader {

	private static final Logger log = Logger.getLogger(XStreamLoader.class);

	public static final String ROOT_DIR_SYSPROP = "tim.billcheckout.xstream.basedir";
	public static final String FILENAME_SYSPROP = "tim.billcheckout.xstream.filename";

	public static String ROOT_DIR;
	public static String FILENAME;

	static {
		ROOT_DIR = System.getProperty(ROOT_DIR_SYSPROP);
		if (ROOT_DIR == null) {
			ROOT_DIR = ".";
		}
		ROOT_DIR += System.getProperty("file.separator");

		FILENAME = System.getProperty(FILENAME_SYSPROP);
		if (FILENAME == null) {
			FILENAME = "xstream.xml";
		}
	}

	public List<Object> loadXStreamInfo(String _custcode) {
		return loadXStreamInfo(_custcode, false, null);
	}

	public List<Object> loadXStreamInfo(String _custcode, boolean _walkDownHierarchy) {
		return loadXStreamInfo(_custcode, _walkDownHierarchy, null);
	}

	public List<Object> loadXStreamInfo(String _custcode, boolean _walkDownHierarchy, Class _clazz) {

		if (_custcode == null) {
			return Collections.EMPTY_LIST;
		}
		String custCodeWithSlashes = _custcode.replaceAll("\\.", "/");
		File currentDir = new File(ROOT_DIR + custCodeWithSlashes);
		if ((!currentDir.exists()) || (!currentDir.isDirectory())) {
			log.debug("XStream directory " + currentDir + " for custCode " + _custcode + " doesnot exist.");
			return Collections.EMPTY_LIST;
		}
		// resulting list
		List<Object> allInfo = new ArrayList<Object>();
		// loading this account information
		try {
			allInfo.addAll(unmarshallObjects(currentDir, _clazz));
		} catch (Exception e) {
			log.warn("Could not read and convert file " + currentDir + " when loading credcorp information for custCode " + _custcode, e);
		}
		// loading child accounts
		if (_walkDownHierarchy) {
			File[] subdirs = currentDir.listFiles();
			for (int i = 0; i < subdirs.length; i++) {
				File f = subdirs[i];
				log.debug("Analyzing subdirectory" + f);
				if (!f.isDirectory()) {
					log.debug("Entry " + f + " is not a direcotry; skipping.");
					continue;
				}
				try {
					List<Object> o = unmarshallObjects(f, _clazz);
					log.debug("Loaded object [" + o + "] from file " + f);
					allInfo.addAll(o);
				} catch (Exception e) {
					log.warn("Could not read and convert file " + f + " when loading child for custCode " + _custcode);
				}
			}
		}
		return allInfo;
	}

	private List<Object> unmarshallObjects(File _dir, Class _clazz) throws Exception {
		log.debug("Checking CredCorp file for subdirectory " + _dir);
		File[] filename = _dir.listFiles(new XStreamFilenameFilter(FILENAME));
		if (filename.length > 1) {
			log.warn("More than one file was found... using the first one only.");
		} else if (filename.length == 0) {
			// no credcorp file found
			log.debug("No credicorp file found for directory " + _dir);
			return Collections.EMPTY_LIST;
		}
		// returing persisted xstream file
		XStream xs = new XStream();
		xs.setMode(XStream.NO_REFERENCES);
		xs.useAttributeFor(String.class);
		List<Object> result = (List<Object>) xs.fromXML( IOUtils.openFileForRead(filename[0]) );
		// if a filtering class was specified, then filter results
		if (_clazz != null) {
			for (int i=0; i < result.size(); i++) {
				if ( !_clazz.isAssignableFrom(result.get(i).getClass()) ) {
					result.remove(i--);
				}
			}
		}
		// returning resulting list
		return result;
	}


	/**
	 * This class is responsible for filtering only the credcorp file in the current directory
	 */
	class XStreamFilenameFilter implements FilenameFilter {
		private String filename;
		public XStreamFilenameFilter(String _filename) { filename = _filename; }
		public boolean accept(File _dir, String _filename) {
			return filename.equals(_filename);
		}
	}
}
