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
 * Created on 11/12/2006
 */
package br.com.auster.tim.billcheckout.util;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.map.LRUMap;
import org.apache.log4j.Logger;

/**
 * @author framos
 * @version $Id$
 *
 */
public abstract class NFNumberHelper {


	private static final Logger log = Logger.getLogger(NFNumberHelper.class);


	protected static final ThreadLocal<Pattern> numberPattern = new ThreadLocal<Pattern>() {
		protected synchronized Pattern initialValue() {
			return Pattern.compile("^\\d{3}+\\.\\d{3}+\\.\\d{3}+$");
		};
	};

	protected static final String splitterChar = "-";


	private static final Lock nfNumberLock = new ReentrantLock();
	private static final LRUMap nfNumberCache = new LRUMap(15);
//	private static final Map<String, String> nfNumber = new TreeMap<String, String>();

	private static String transactionId;


	//
	// This portion of the code is to handle duplicated NF numbers for the same cycle/cutdate
	//

	public static boolean isNFNumberDuplicated(String _nfNumber, String _custcode, String _uf, String _transactionId) {
		try {
			nfNumberLock.lockInterruptibly();
			if (_nfNumber == null) {
				return false;
			}
			// checking if another set of accounts was put to process
			if (!nfNumberCache.containsKey(_transactionId)) {
				nfNumberCache.put(_transactionId,  new TreeMap<String, String>());
			}
			Map<String, String> nfNumber = (Map<String, String>) nfNumberCache.get(_transactionId);
			String key = buildKey(_nfNumber, _uf);
			if (!nfNumber.containsKey(key)) {
				// this nf number was not found before
				nfNumber.put(key, _custcode);
				log.debug("NFNumber " + _nfNumber + "at UF " + _uf + " was never found before in transactionId " + _transactionId + ". First custCode is " + _custcode);
			} else {
				// this NF number was already found
				log.debug("NFNumber " + _nfNumber + "at UF " + _uf + " previously found, now repeated on custCode " + _custcode + " in transactionId " + _transactionId);
				return true;
			}
		} catch (Exception e) {
			// dispite the error, should
			log.error("Error handling custcode comparison", e);
		} finally {
			nfNumberLock.unlock();
		}
		return false;
	}

	public static final String getFistCustcode(String _nfNumber, String _uf, String _transactionId) {
		try {
			nfNumberLock.lockInterruptibly();
			Map<String, String> nfNumber = (Map<String, String>) nfNumberCache.get(_transactionId);
			if (nfNumber != null) {
				return nfNumber.get(buildKey(_nfNumber, _uf));
			}
		} catch (Exception e) {
			log.error("Error initializing the due dates map", e);
		} finally {
			nfNumberLock.unlock();
		}
		return null;
	}

	public static final void clearNumbers() {
		clearNumbers(null);
	}
	public static final void clearNumbers(String _transactionId) {
		try {
			nfNumberLock.lockInterruptibly();
			if (_transactionId == null) {
				nfNumberCache.clear();
			} else {
				nfNumberCache.remove(_transactionId);
			}
		} catch (Exception e) {
			log.error("Error initializing the due dates map", e);
		} finally {
			nfNumberLock.unlock();
		}
	}

	private static final String buildKey(String _nfNumber, String _uf) {
		return (_nfNumber + "|" + _uf);
	}


	//
	// This portion of helps validating NF numbers and series
	//
	public static String findNFSeries(String _number) {
		return findNFSeries(_number, "", "");
	}
	public static String findNFSeries(String _number, String _carrierCode, String _carrierState) {
		try {
			nfNumberLock.lockInterruptibly();
			if (_number == null) {
				return null;
			}
			try {
				String number = new String(_number.substring(_number.indexOf(splitterChar)+1));
				if ((number.indexOf(splitterChar) >= 0) &&
					(!carrierAndStateHasSplitterOnSeries(_carrierCode, _carrierState))) {
					number = new String(number.substring(0, number.indexOf(splitterChar)));
				}
				log.debug("Found NF series:" + number);
				return number;
			} catch (IndexOutOfBoundsException iobe) {
				log.error("Exception while handling NF series", iobe);
			}
		} catch (Exception e) {
			log.error("Error initializing the due dates map", e);
		} finally {
			nfNumberLock.unlock();
		}
		return null;
	}

	/**
	 * This method is needed since carrier 12, for UFs MG,GO,MS and SP, do not have subSeries and have
	 *    the splitter char in its series
	 */
	private static boolean carrierAndStateHasSplitterOnSeries(String _carrierCode, String _carrierState) {
		return ("12".equals(_carrierCode) && ("|MG|GO|MS|SP|".indexOf(_carrierState) > 0));
	}


	public static String findNFSubSeries(String _number) {
		return findNFSubSeries(_number, "", "");
	}

	public static String findNFSubSeries(String _number, String _carrierCode, String _carrierState) {
		try {
			nfNumberLock.lockInterruptibly();
			if (_number == null) {
				return null;
			}
			try {
				String number = new String(_number.substring(_number.indexOf(splitterChar)+1));
				if ((number.indexOf(splitterChar) >= 0) && (!carrierAndStateHasSplitterOnSeries(_carrierCode, _carrierState))) {
					number = new String(number.substring(number.indexOf(splitterChar)+1));
				} else {
					number = "";
				}
				log.debug("Found NF series:" + number);
				return number;
			} catch (IndexOutOfBoundsException iobe) {
				log.error("Exception while handling NF series", iobe);
			}
		} catch (Exception e) {
			log.error("Error initializing the due dates map", e);
		} finally {
			nfNumberLock.unlock();
		}
		return null;
	}

	public static boolean validateNFNumber(String _number) {
		try {
			nfNumberLock.lockInterruptibly();
			if ((_number == null) || (_number.indexOf(splitterChar) <= 0)) {
				return false;
			}
			String number = _number.substring(0, _number.indexOf(splitterChar));
			log.debug("Found NF number:" + number);
			Matcher m = numberPattern.get().matcher(number);
			return m.matches();
		} catch (IndexOutOfBoundsException iobe) {
			log.error("Exception while handling NF number", iobe);
		} catch (Exception e) {
			log.error("Error initializing the due dates map", e);
		} finally {
			nfNumberLock.unlock();
		}
		return false;
	}

}
