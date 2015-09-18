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

import junit.framework.TestCase;

/**
 * @author framos
 * @version $Id$
 *
 */
public class NFNumberHelperTest extends TestCase {

	
	public static final String VALID_NFNUMBER = "001.010.101-0493";
	public static final String INVALID_NFNUMBER = "001.010.1X1-04X3";
	public static final String INVALID_ONLY_NUMBER = "001.010.1X1-0423";
	public static final String INVALID_ONLY_SERIES = "001.010.101-0X23";
	public static final String INVALID_SIZE = "1.010.1X1-0433";

	public static final String NO_SPLIT_CHAR = "001.010.101A0X23";
	
	
	public void testValidNFNumber() {
		assertTrue(NFNumberHelper.validateNFNumber(VALID_NFNUMBER));
	}

	public void testInvalidNFNumber() {
		// INVALID_NFNUMBER
		assertFalse(NFNumberHelper.validateNFNumber(INVALID_NFNUMBER));
		// INVALID_ONLY_NUMBER
		assertFalse(NFNumberHelper.validateNFNumber(INVALID_ONLY_NUMBER));
		// INVALID_ONLY_SERIES
		assertTrue(NFNumberHelper.validateNFNumber(INVALID_ONLY_SERIES));
		// NO_SPLIT_CHAR
		assertFalse(NFNumberHelper.validateNFNumber(NO_SPLIT_CHAR));
		// NULL
		assertFalse(NFNumberHelper.validateNFNumber(null));
		// INVALID_SIZE
		assertFalse(NFNumberHelper.validateNFNumber(INVALID_SIZE));
	}
	
}
