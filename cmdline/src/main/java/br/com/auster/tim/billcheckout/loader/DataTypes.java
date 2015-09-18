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
 * Created on 08/03/2010
 */
package br.com.auster.tim.billcheckout.loader;

import java.sql.Timestamp;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO What this class is responsible for
 *
 * @author Frederico Ramos
 * @version $Id$
 * @since JDK1.4
 */
public abstract class DataTypes {

	
	private static final ThreadLocal<SimpleDateFormat> dateFormatter = new ThreadLocal<SimpleDateFormat>() {
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat();
		};
	};
	
	private static final ThreadLocal<DecimalFormat> decimalFormatter = new ThreadLocal<DecimalFormat>() {
		protected DecimalFormat initialValue() {
			return new DecimalFormat();
		};		
	};
	
	
	public static final String DATE_FORMAT = "dd/MM/yyyy";
	
	public static final String DATE_TYPE = "data";
	public static final String INT_TYPE = "inteiro";
	public static final String DECIMAL_TYPE = "decimal";
	public static final String TEXT_TYPE = "texto"; 
	
	public static final Map<String, Integer> TYPES_TO_SQL = new HashMap<String, Integer>(); 	
	static {
		TYPES_TO_SQL.put(DATE_TYPE, Types.TIMESTAMP);
		TYPES_TO_SQL.put(INT_TYPE, Types.INTEGER);
		TYPES_TO_SQL.put(DECIMAL_TYPE, Types.DECIMAL);
		TYPES_TO_SQL.put(TEXT_TYPE, Types.VARCHAR);
	}
	
	
	
	
	
	public static final boolean canDecode(String _value, String _type, String _format) {
		if (_type == null) { return false; }
		if ((_value == null) || (_value.trim().length() <= 0)) { return true;}
		
		try {
			if (DATE_TYPE.equalsIgnoreCase(_type)) {
				decodeDate(_value, (_format == null ? DATE_FORMAT : _format) );
			} else if (INT_TYPE.equalsIgnoreCase(_type)) {
				decodeInt(_value, (_format == null ? null : _format) );
			} else if (DECIMAL_TYPE.equalsIgnoreCase(_type)) {
				decodeDecimal(_value, (_format == null ? null : _format) );
			} else if (!TEXT_TYPE.equalsIgnoreCase(_type)) {
				return false;
			}
		} catch (NumberFormatException nfe) {
			//TODO log
			return false;
		} catch (NullPointerException npe) {
			//TODO log
			return false;
		} catch (ParseException pe) {
			//TODO log
			return false;
		}
		return true;
	}
	
	
	public static final Object decode(String _value, String _type, String _format) {
		if ((_type == null) ||(_value == null) || (_value.trim().length() <= 0)) { return null; }
		
		try {
			if (DATE_TYPE.equalsIgnoreCase(_type)) {
				return decodeDate(_value, (_format == null ? DATE_FORMAT : _format) );
			} else if (INT_TYPE.equalsIgnoreCase(_type)) {
				return decodeInt(_value, (_format == null ? null : _format) );
			} else if (DECIMAL_TYPE.equalsIgnoreCase(_type)) {
				return decodeDecimal(_value, (_format == null ? null : _format) );
			} else if (TEXT_TYPE.equalsIgnoreCase(_type)) {
				return _value;
			}
		} catch (NumberFormatException nfe) {
			//TODO log
		} catch (NullPointerException npe) {
			//TODO log
		} catch (ParseException pe) {
			//TODO log
		}
		return null;
	}	
	
	public static final Date decodeDate(String _value, String _format) throws ParseException {
		dateFormatter.get().applyPattern(_format);
		return dateFormatter.get().parse(_value);
	}
	
	public static final Number decodeInt(String _value, String _format) throws ParseException {
		if (_format == null) {
			return new Integer(_value); 
		}
		decimalFormatter.get().applyPattern(_format);
		return new Integer(decimalFormatter.get().parse(_value).intValue());
	}
	
	public static final Number decodeDecimal(String _value, String _format) throws ParseException {
		if (_format == null) {
			return new Double(_value); 
		}
		decimalFormatter.get().applyPattern(_format);
		return new Double(decimalFormatter.get().parse(_value).doubleValue());
	}
	
	
	public static final Object convertIntoDBObject(String _value, String _type, String _format) {
		Object valueToSet = DataTypes.decode(_value, _type, _format);
		if ((valueToSet == null) || 
			( (valueToSet instanceof String) && (((String)valueToSet).trim().length() <= 0) ) ) {
			return null;
		}			
		
		if (DataTypes.TYPES_TO_SQL.get(_type) == Types.TIMESTAMP) {
			valueToSet = new Timestamp( ((Date)valueToSet).getTime() );
		}
		return valueToSet;
		
	}
}
