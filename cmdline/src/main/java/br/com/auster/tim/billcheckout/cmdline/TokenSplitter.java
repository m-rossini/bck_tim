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
 * Created on 03/07/2007
 */
package br.com.auster.tim.billcheckout.cmdline;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author framos
 * @version $Id$
 *
 */
public class TokenSplitter {

	
	private static final Logger log = Logger.getLogger(TokenSplitter.class);
	
	
	public static final String CFG_SEPARATOR = ".";
	public static final String CFG_ROOT_TOKEN = "files.token";
	public static final String CFG_ROOT_LEN = CFG_ROOT_TOKEN  + CFG_SEPARATOR + "len";

	public static final String CFG_TOKEN_START = "start";
	public static final String CFG_TOKEN_LENGTH = "len";
	public static final String CFG_TOKEN_NAME = "name";
	public static final String CFG_TOKEN_APPEND_BEFORE = "append.before";
	public static final String CFG_TOKEN_APPEND_AFTER = "append.after";
	
	
	private TokenInfo[] tokens;
	
	
	public TokenSplitter(Properties _properties) {
		if (!_properties.containsKey(CFG_ROOT_LEN)) {
			this.tokens = null;
			return;
		}
		try {
			int len = Integer.parseInt(_properties.getProperty(CFG_ROOT_LEN));
			this.tokens = new TokenInfo[len];
			for (int i=0; i < len; i++) {
				this.tokens[i] = buildTokenInfo(_properties, i); 
			}
		} catch (Exception e) {
			log.error("Error while reading splitter configuration", e);
			this.tokens = null;
			return;
		}
	}
	
	private TokenInfo buildTokenInfo(Properties _properties, int _pos) throws Exception {
		TokenInfo info = new TokenInfo();
		String propNameRoot = CFG_ROOT_TOKEN + CFG_SEPARATOR + String.valueOf(_pos) + CFG_SEPARATOR;
		info.start = Integer.parseInt(_properties.getProperty(propNameRoot + CFG_TOKEN_START));
		info.len = Integer.parseInt(_properties.getProperty(propNameRoot + CFG_TOKEN_LENGTH));
		info.name = _properties.getProperty(propNameRoot + CFG_TOKEN_NAME);
		info.appendBefore = _properties.getProperty(propNameRoot + CFG_TOKEN_APPEND_BEFORE);
		info.appendAfter = _properties.getProperty(propNameRoot + CFG_TOKEN_APPEND_AFTER);
		return info;
	}
	
	
	
	public int getTokenLen() {
		return (this.tokens == null ? 0 : this.tokens.length);
	}

	public String getTokenName(int _level) {
		if ((_level < 0) || (_level >= this.tokens.length) || (this.tokens[_level] == null)) {
			throw new IllegalStateException("Request level not properly configured: " + _level);
		}
		TokenInfo t = this.tokens[_level];
		return t.name;
	}

	public String getTokenValue(int _level, String _source) {
		return getTokenValue(_level, new File(_source));
	}	
	
	public String getTokenValue(int _level, File _source) {
		if ((_level < 0) || (_level >= this.tokens.length) || (this.tokens[_level] == null)) {
			throw new IllegalStateException("Request level not properly configured: " + _level);
		}
		TokenInfo t = this.tokens[_level];
		try {
			String source = _source.getName();
			return  (t.appendBefore == null ? "" : t.appendBefore ) +
					source.substring(t.start, t.start + t.len) +
					(t.appendAfter == null ? "" : t.appendAfter );
		} catch (ArrayIndexOutOfBoundsException aiobe) {
			throw new IllegalStateException("Request level not properly configured", aiobe);
		}
	}
	
	public Map<String, String> getAllTokenValues(String _source) {
		Map<String, String> tokenValues = new HashMap<String, String>();
		for (int i=0; i < this.tokens.length; i++) {
			tokenValues.put( this.getTokenName(i), getTokenValue(i, _source) );
		}
		return tokenValues;
	}
	
	
	
	
	private static class TokenInfo {
		public int start;
		public int len;
		public String name;
		public String appendBefore;
		public String appendAfter;
		
		TokenInfo() { }
	}
}

