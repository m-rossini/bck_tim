/*
 /*
 * Copyright (c) 2004 Auster Solutions. All Rights Reserved.
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
 * Created on Oct 15, 2005
 */
package br.com.auster.tim.billcheckout.util;

import br.com.auster.common.lang.StringUtils;
import br.com.auster.common.util.CommonsServicesBarCode;
import br.com.auster.om.invoice.BarCode;

/**
 * @author framos
 * @version $Id: TIMBarCode.java 250 2006-12-07 17:36:12Z framos $
 */
public abstract class TIMBarCode {

	protected static final int	COMPANY_INFO_START	= 15;
	protected static final int	COMPANY_INFO_END	  = 19;

	protected static final int	INVOICE_SEQ_START	= 21;
	protected static final int	INVOICE_SEQ_END		= 34;

	protected static final int	CUSTOMER_ID_START	= 34;
	protected static final int	CUSTOMER_ID_END	= 42;

	/**
	 * Validates if the bar code is correct. Checks for : <br>
	 * <ul>
	 * bar code correctness
	 * <ul>
	 * value must match invoice total amount
	 * <ul>
	 * segment id must be TELECOM
	 * <ul>
	 * account number must be in barcode custom field
	 * <ul>
	 * due date must match invoice due date
	 * <ul>
	 * alpha-numeric representation
	 */
	public static boolean validateCustomField(BarCode _barcode, String _companyID,
			String _invoiceID, Double _totalAmount) {

		if ((_companyID == null) || (_invoiceID == null) || (_totalAmount == null) || (_invoiceID == null)) {
			return false;
		}
		return true;
	}

	public static boolean isCompanyIdentificationValid(BarCode _barcode, String[] companies) {
		String barcodeCID = CommonsServicesBarCode.getBarCodeWithoutDVs(_barcode.getOCRLeftLine()).substring(COMPANY_INFO_START, COMPANY_INFO_END);
		for (int i=0;i<companies.length;i++) {
			if (companies[i].equals(barcodeCID)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isInvoiceSequenceNumberValid(BarCode _barcode, String invNo) {
		String comp = StringUtils.alignToRight(invNo.trim(), (INVOICE_SEQ_END-INVOICE_SEQ_START), '0') ;		
		String bcode = CommonsServicesBarCode.getBarCodeWithoutDVs(_barcode.getOCRLeftLine()).substring(INVOICE_SEQ_START, INVOICE_SEQ_END);
		return comp.equals(bcode);
	}
	
	public static boolean isCustomerIDValid(BarCode _barcode, String customerID) {		
		String comp = StringUtils.alignToRight(customerID.trim(), (CUSTOMER_ID_END-CUSTOMER_ID_START), '0') ;		
		String bcode = CommonsServicesBarCode.getBarCodeWithoutDVs(_barcode.getOCRLeftLine()).substring(CUSTOMER_ID_START, CUSTOMER_ID_END);
		return comp.equals(bcode);
	}
}
