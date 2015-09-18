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
 * Created on 10/01/2007
 */
package br.com.auster.tim.billcheckout.util;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

/**
 * @author framos
 * @version $Id$
 *
 */
public abstract class CustcodeHelper {

	
	private static final Logger log = Logger.getLogger(CustcodeHelper.class);
	private static final Lock dueDateMapLock = new ReentrantLock();
	
	private static final Map<String, Date> dueDateMap = new TreeMap<String, Date>();

	private static String transactionId; 

	
	
	/**
	 * Checks if the custcode is formed as X.YY.Z
	 */
	public static boolean isCorporateCustcode(String _custcode) {
		try {
			dueDateMapLock.lockInterruptibly();
			if (_custcode != null) { 
				return (getRootCorporateCustcodeIndex(_custcode) > 0);
			}
		} catch (Exception e) {
			// dispite the error, should go on
			log.error("Error handling custcode verification", e);
		} finally {
			dueDateMapLock.unlock();
		}
		return false;
	}

	/**
	 * Will return false, when the specified due date is not the same of a previously identified due date, for the
	 * 	same custcode root.
	 * 
	 * True is returned when:
	 *   - the custcode is not corporate or is NULL
	 *   - some exception was raised during the validation of the due date
	 *   - the transaction Id 
	 *   
	 *   The cut date is used to identify when another set of accounts is put to process.
	 */
	public static boolean isCorporateCustcodeInSameDuedate(String _custcode, Date _dueDate, String _transactionId) {
		
		Date dt = null;
		try {
			dueDateMapLock.lockInterruptibly();
			// will only work for corporate custcodes
			int idx = getRootCorporateCustcodeIndex(_custcode);
			if (idx <= 0) {
				// this is not a corporate custcode, so it should not be checked
				return true;
			}
			
			// checking if another set of accounts was put to process
			if (transactionId == null) {
				transactionId = _transactionId;
			} else if (!transactionId.equals(_transactionId)) {
				clearDates(_transactionId);
			}
			
			// validating the due date for the specified custcode
			String key = _custcode.substring(0, idx);
			if (!dueDateMap.containsKey(key)) {
				// this custcode root was not found before, so it will assume as the correct due date
				dueDateMap.put(key, _dueDate);
				return true;
			}
			dt = dueDateMap.get(key);
		} catch (Exception e) {
			// dispite the error, should go on
			log.error("Error handling custcode comparison", e);
		} finally {
			dueDateMapLock.unlock();
		}
		// this custcode root was found before. Checking if the due date is the same
		return ((dt != null) && (dt.equals(_dueDate)));
	}
	
	public static Date getCorporateDueDate(String _custcode) {
		try {
			dueDateMapLock.lockInterruptibly();
			// will only work for corporate custcodes
			int idx = getRootCorporateCustcodeIndex(_custcode);
			if (idx > 0) {
				String key = _custcode.substring(0, idx);
				return dueDateMap.get(key);
			}
		} catch (Exception e) {
			// dispite the error, should go on
			log.error("Error handling custcode dueDate", e);
		} finally {
			dueDateMapLock.unlock();
		}
		return null;
	}
	
	public static int getRootCorporateCustcodeIndex(String _custcode) {
		int idx = 0;
		try {
			dueDateMapLock.lockInterruptibly();
			if (_custcode == null) { return 0; }
			idx = _custcode.indexOf(".");
			if (idx > 0) {
				idx = _custcode.indexOf(".", idx+1);
			} 
			// if no second period is found, then this may be the ROOT account
			if (idx < 0) {
				idx = _custcode.length();
			}
		} catch (Exception e) {
			// dispite the error, should go on
			log.error("Error handling custcode indexing", e);
		} finally {
			dueDateMapLock.unlock();
		}
		return idx;
	}

	public static final void clearDates() {
		clearDates(null);
	}
	public static final void clearDates(String _transactionId) {
		try {
			dueDateMapLock.lockInterruptibly();
			transactionId = _transactionId;
			dueDateMap.clear();
		} catch (Exception e) {
			log.error("Error initializing the due dates map", e);
		} finally {
			dueDateMapLock.unlock();
		}
	}	
}

