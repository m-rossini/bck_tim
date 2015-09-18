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
 * Created on Dec 21, 2006
 */
package br.com.auster.tim.billcheckout.util;

import java.security.MessageDigest;

import org.apache.log4j.Logger;


/**
 * This helper class generates hashcodes to validate NF information
 * 
 * @author mruao
 * @version $$Id$$
 */
public class ICMSHashGenerator {

	
	private static final Logger log = Logger.getLogger(ICMSHashGenerator.class);
	
	// Algorithm used to generate hashcode key
	private static final String DIGEST_OPTION = "MD5";

	public static String generateFiscalHashcode( String CPF_CNPJ, String invoiceNumber, 
												 String invoiceAmount, String baseAmount,
												 String taxAmount ) {

		try {
			log.info("Using values : '" + CPF_CNPJ + "', '" + invoiceNumber + "','" +
					 invoiceAmount + "','" + baseAmount + "','" + taxAmount + "'");
			
			MessageDigest msgD5 = MessageDigest.getInstance( DIGEST_OPTION );
			
			// handing CNPJ/CPF
			String tempId = "";
			if (CPF_CNPJ != null) {
				tempId = paddleWithZeros(removeNonNumericChars(CPF_CNPJ), 14);
			}
			msgD5.update( tempId.getBytes() );
			// handling invoice number
			tempId = "";
			if (invoiceNumber != null) {
				tempId = paddleWithZeros(removeNonNumericChars(invoiceNumber, true), 9);
			}
			msgD5.update( tempId.getBytes() );
			// handling invoice amount
			tempId = "";
			if (invoiceAmount != null) {
				tempId = paddleWithZeros(removeNonNumericChars(
							guaranteeTwoDecimalDigits(invoiceAmount)), 12);
			}
			msgD5.update( tempId.getBytes() );
			// handling base amount
			tempId = "";
			if (baseAmount != null) {
				tempId = paddleWithZeros(removeNonNumericChars(
							guaranteeTwoDecimalDigits(baseAmount)), 12);
			}
			msgD5.update( tempId.getBytes() );
			// handling tax amount
			tempId = "";
			if (taxAmount != null) {
				tempId = paddleWithZeros(removeNonNumericChars(
							guaranteeTwoDecimalDigits(taxAmount)), 12);
			}
			msgD5.update( tempId.getBytes() );
			// getting hash code in bytes
        	byte[] result = msgD5.digest();
        	String hashcodeStr = "";
	        for ( int i=0; i< result.length ; i++) {
	        	// gets LSB value
	        	int hashNumber = Byte.valueOf(result[i]).intValue();
	        	hashNumber &= 0xff;
	        	// verify if number will have two digits in hexa display
	        	if ( hashNumber < 16 ) {
	        		hashcodeStr += "0";
	        	}
	        	hashcodeStr += Integer.toHexString( hashNumber );

	        	// adds period mark each two numbers
	        	if( (i < (result.length-1)) && ((i%2)!=0) )
	        		hashcodeStr += ".";
	        }
	        // only uses capital letters in final hashcode
			return hashcodeStr.toUpperCase();
		} catch (Exception e) {
			log.error("Error generating hashcode for NF", e);
		}
		return null;
	}
	
	
	public static String paddleWithZeros( String original, int finalLength ) {
		char[] workBuffer = new char[finalLength];
		int j = finalLength - 1;
		
		if( original == null )
			return null;

		// copies characters from input string
		for (int i= (original.length()-1); j>=0  &&  i >=0; i-- ) {
			if( original.charAt(i) >= '0'  &&  original.charAt(i) <= '9') {
				workBuffer[j] = original.charAt(i);
				j--;
			}
		}
		//paddle final string with zeros to the left
		for(;j>=0;j--){
			workBuffer[j] = '0';
		}
		return new String(workBuffer);
	}

	public static String removeNonNumericChars( String original ) {
		return removeNonNumericChars( original, false );
	}

	public static String removeNonNumericChars( String original, boolean discardSerialPart ) {
		StringBuffer helperStr = new StringBuffer();
		
		if( original == null )
			return null;

		// copies characters from input string
		for (int i=0; i < original.length(); i++ ) {
			// does not consider after serial number part of invoice number
			if( discardSerialPart  &&  (original.charAt(i) == '-') )
				break;
			if( original.charAt(i) >= '0'  &&  original.charAt(i) <= '9') {
				helperStr.append( original.charAt(i) );
			}
		}
		return helperStr.toString();
	}
	
	public static String guaranteeTwoDecimalDigits( String number ) {
		StringBuffer adjustedNumber = new StringBuffer( number );
		
		// looks after decimal separator
		int decimalPos = number.lastIndexOf( '.' );
		if( decimalPos == -1 )
			decimalPos = number.lastIndexOf( ',' );

		// appends zero if necessary
		if( decimalPos > (number.length() - 3 ) )
			adjustedNumber.append( '0' );

		return adjustedNumber.toString();
	}
	
	
}
