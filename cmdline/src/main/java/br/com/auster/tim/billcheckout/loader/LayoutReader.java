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
 * Created on 05/03/2010
 */
package br.com.auster.tim.billcheckout.loader;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;

import br.com.auster.common.io.IOUtils;
import br.com.auster.common.util.I18n;

/**
 * TODO What this class is responsible for
 *
 * @author Frederico Ramos
 * @version $Id$
 * @since JDK1.4
 */
public class LayoutReader {

	
	
	// layout configuration
	public static final String CFG_LAYOUT_PRE_INSERT_QUERY = "layout.preInsertQuery";
	public static final String CFG_LAYOUT_INSERT_QUERY = "layout.insertQuery";
	public static final String CFG_LAYOUT_POS_INSERT_QUERY = "layout.posInsertQuery";
	public static final String CFG_LAYOUT_DB_POOL = "layout.dbPool";
	public static final String CFG_LAYOUT_TOTAL_COLUMNS = "layout.totalColumns";
	public static final String CFG_LAYOUT_USED_COLUMNS = "layout.usedColumns";
	// configuration for each column
	public static final String CFG_LAYOUT_COL_POSITION = "layout.column.{0}.position";	 
	public static final String CFG_LAYOUT_COL_TYPE = "layout.column.{0}.type";
	public static final String CFG_LAYOUT_COL_FORMAT = "layout.column.{0}.format";
	public static final String CFG_LAYOUT_COL_FKQUERY = "layout.column.{0}.fk.query";
	public static final String CFG_LAYOUT_COL_FKPOOL = "layout.column.{0}.fk.pool";
	public static final String CFG_LAYOUT_COL_FKTYPE = "layout.column.{0}.fk.type";
	public static final String CFG_LAYOUT_COL_FKFORMAT = "layout.column.{0}.fk.format";
	public static final String CFG_LAYOUT_COL_LOV_SEPARATOR = "layout.column.{0}.lov.separator";
	
	

	private static final I18n i18n = I18n.getInstance(LayoutReader.class);

	

	protected Properties properties;
	
	
	
	public LayoutReader(String _configurationPath) {
		this.properties = new Properties();
		try {
			InputStream is = IOUtils.openFileForRead(_configurationPath);
			this.properties.load(is);
		} catch (IOException ioe) {
			throw new LayoutConfigurationException(i18n.getString("layoutreader.ioexception", _configurationPath), ioe);
		}
		
		// validating configuration
		validateNotNullParameter(CFG_LAYOUT_INSERT_QUERY);
		validateNotNullParameter(CFG_LAYOUT_DB_POOL);
		validateNotNullParameter(CFG_LAYOUT_TOTAL_COLUMNS);
		validatePositiveIntegerParameter(CFG_LAYOUT_TOTAL_COLUMNS);
		validateNotNullParameter(CFG_LAYOUT_USED_COLUMNS);
		validatePositiveIntegerParameter(CFG_LAYOUT_USED_COLUMNS);
		
		int usedColumns = Integer.parseInt(this.properties.getProperty(CFG_LAYOUT_USED_COLUMNS));
		int i=1;
		for (; i <= usedColumns; i++) {
			validateNotNullParameter(CFG_LAYOUT_COL_POSITION, i);
			validateNotNullParameter(CFG_LAYOUT_COL_TYPE, i);
		}
		if ((usedColumns+1) != i) {
			throw new LayoutConfigurationException(i18n.getString("layoutreader.validation.diffUsedAndConfiguredColumns",
																  String.valueOf(usedColumns), String.valueOf(i) 
																 ));
		}
	}
	
	private void validateNotNullParameter(String _key) {
		if (this.properties.get(_key) == null) {
			throw new LayoutConfigurationException(i18n.getString("layoutreader.validation.noKey", _key));
		}
	}
	
	private void validateNotNullParameter(String _key, int _pos) {
		String realKey = MessageFormat.format(_key, String.valueOf(_pos));
		validateNotNullParameter(realKey);
	}
	
	private void validatePositiveIntegerParameter(String _key) {
		try {
			int value = Integer.parseInt(this.properties.getProperty(_key));
			if (value <= 0) {
				throw new LayoutConfigurationException(i18n.getString("layoutreader.validation.invalidInteger", _key));
			}
		} catch (NumberFormatException nfe) {
			throw new LayoutConfigurationException(i18n.getString("layoutreader.validation.invalidInteger", _key), nfe);
		}
	}
	
	
	public String getProperty(String _key, int _pos) {
		String realKey = MessageFormat.format(_key, String.valueOf(_pos));
		return this.getProperty(realKey);
	}
	
	public String getProperty(String _key) {
		String value = this.properties.getProperty(_key);
		return (value != null ? value.trim() : value);
	}
	
	public int getIntProperty(String _key, int _pos) {
		String realKey = MessageFormat.format(_key, String.valueOf(_pos));
		return this.getIntProperty(realKey);
	}

	public int getIntProperty(String _key) {
		return Integer.parseInt(this.properties.getProperty(_key).trim());
	}
	
}
