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
 * Created on 03/04/2008
 */
package br.com.auster.tim.billcheckout.model;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import br.com.auster.om.reference.CustomizableEntity;

/**
 * Model class for ProgressiveDiscount.
 *
 * @author William Soares
 * @version $Id$
 * @since JDK1.4
 */
public class ProgressiveDiscount extends CustomizableEntity {


	/**
	 * Used to store the values of  <code>serialVersionUID</code>.
	 */
	private static final long serialVersionUID = 1L;

	// ---------------------------
	// atributes
	// ---------------------------
	private String	rangeName;
	private Set progDiscountRates = new LinkedHashSet();


	public ProgressiveDiscount() {
		super();
	}

	// ---------------------------
	// getters and setters
	// ---------------------------
	public String getRangeName() {
		return rangeName;
	}

	public void setRangeName(String rangeName) {
		this.rangeName = rangeName;
	}

	public Set getProgDiscountRates() {
		return progDiscountRates;
	}

	public List getSortedDiscountRates() {
		List temp = new LinkedList(progDiscountRates);
		Collections.sort(temp);
		return temp;
	}

	public void setProgDiscountRates(Set progDiscountRates) {
		this.progDiscountRates = progDiscountRates;
	}

	public void addProgDiscountRates(ProgressiveDiscountRates _discountRate) {
		if (this.progDiscountRates == null) {
			this.progDiscountRates = new LinkedHashSet();
		}
		this.progDiscountRates.add(_discountRate);
		_discountRate.setParent(this);
	}

}
