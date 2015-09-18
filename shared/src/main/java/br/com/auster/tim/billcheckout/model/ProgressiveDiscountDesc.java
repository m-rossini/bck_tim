/*
 * Copyright (c) 2004-2008 Auster Solutions. All Rights Reserved.
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
 * Created on 03/04/2008
 */
package br.com.auster.tim.billcheckout.model;

import br.com.auster.om.reference.CustomizableEntity;

/**
 * Model class for ProgressiveDiscountDesc.
 *
 * @author William Soares
 * @version $Id$
 * @since JDK1.4
 */
public class ProgressiveDiscountDesc extends CustomizableEntity {


	/**
	 * Used to store the values of  <code>serialVersionUID</code>.
	 */
	private static final long serialVersionUID = 1L;

	// ---------------------------
	// atributes
	// ---------------------------
	private String discountDesc;
	private String serviceShortDesc;
	private String state;
	private ProgressiveDiscount progDiscount = new ProgressiveDiscount();


	// ---------------------------
	// getters and setters
	// ---------------------------

	public String getDiscountDesc() {
		return discountDesc;
	}

	public void setDiscountDesc(String discountDesc) {
		this.discountDesc = discountDesc;
	}

	public String getServiceShortDesc() {
		return serviceShortDesc;
	}

	public void setServiceShortDesc(String serviceShortDesc) {
		this.serviceShortDesc = serviceShortDesc;
	}

	public ProgressiveDiscount getProgDiscount() {
		return progDiscount;
	}

	public void setProgDiscount(ProgressiveDiscount progDiscount) {
		this.progDiscount = progDiscount;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
}
