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
 * Created on 23/05/2008
 */
package br.com.auster.tim.billcheckout.util;

import java.math.BigDecimal;

/**
 * Named attribute using BigDecimal object.
 *
 * @author William Soares
 * @version $Id$
 * @since JDK1.4
 */
public class NamedBigDecimal {

	private String	name;
	private BigDecimal	bd;

	/**
	 * Init bigDecimal with '0' value.
	 */
	public NamedBigDecimal(String name) {
		this(name, new BigDecimal("0"));
	}

	/**
	 * Init bigDecimal with custom value.
	 */
	public NamedBigDecimal(String name, BigDecimal bd) {
		this.name = name;
		this.bd = bd;
	}

	/**
	 * Returns the bigDecimal - TRUNCATES TO 2 DECIMAL PLACES -
	 * 
	 * @return bd
	 */
	public BigDecimal getValue() {
		return bd.setScale(2,BigDecimal.ROUND_DOWN);
	}

	/**
	 * Sets the bigDecimal
	 * 
	 * @param bd
	 */
	public void setValue(BigDecimal bd) {
		this.bd = bd;
	}

	/**
	 * Returns the name of the BigDecimal object.
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the BigDecimal.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Only Returns the double value of the subtraction (current bigDecimal value - double parameter) result.
	 * 
	 * NOTE THAT ONLY THIS METHOD DOES NOT SET THE BIGDECIMAL RESULT SO THAT DOES NOT AFFECT THE 
	 * VALUE OF THE CURRENT BIGDECIMAL. IF IT IS NECESSARY A DIFFERENT BEHAVIOUR, IT MUST BE IMPLEMENTED
	 * OTHER SUBTRACT METHOD. DOES NOT CHANGE THIS, OTHERWISE THE RULES THAT USE IT WILL STOP WORKING. 
	 * 
	 * @param dbl
	 * @return double value of the subtraction result
	 */
	public double subtractFrom(double dbl) {
		return this.getValue().subtract(new BigDecimal(String.valueOf(dbl)).setScale(2,BigDecimal.ROUND_DOWN)).doubleValue();
	}	

	/**
	 * Sets the bigDecimal result of the addTo (current bigDecimal value + double paramenter) 
	 * to the current bigDecimal and returns the value of the current bigDecimal after the changes.
	 * 
	 * @param dbl
	 * @return double value of the current bigDecimal after adding.
	 */
	public double addTo(double dbl) {
		this.setValue(this.getValue().add(new BigDecimal(String.valueOf(dbl)).setScale(2,BigDecimal.ROUND_DOWN)));
		return this.getValue().doubleValue();
	}
	
	/**
	 * Sets the bigDecimal result of the multiplication (current bigDecimal value * double paramenter) 
	 * to the current bigDecimal and returns the value of the current bigDecimal after the changes.
	 * 
	 * @param dbl
	 * @return double value of the current bigDecimal after multiplying.
	 */	
	public double multiply(double dbl) {
		this.setValue(this.getValue().multiply(new BigDecimal(String.valueOf(dbl)).setScale(2,BigDecimal.ROUND_DOWN)));
		return this.getValue().doubleValue();
	}
	
	/**
	 * Sets the bigDecimal result of the division (current bigDecimal value / double paramenter) 
	 * to the current bigDecimal and returns the value of the current bigDecimal after the changes.
	 * 
	 * @param dbl
	 * @return double value of the current bigDecimal after dividing.
	 */		
	public double divide(double dbl) {
		this.setValue(this.getValue().divide(new BigDecimal(String.valueOf(dbl)).setScale(2,BigDecimal.ROUND_DOWN)));
		return this.getValue().doubleValue();
	}
	
	public boolean equals(Object obj) {
		if (obj instanceof NamedBigDecimal) {
			NamedBigDecimal input = (NamedBigDecimal) obj;
			if (input.getName().equals(this.getName())) {
				if (input.getValue() == this.getValue()) {
					return true;
				}
			}
		}		
		return false;
	}

	public int hashCode() {
		int retval = (this.getName().hashCode()) * 37;
		retval += (this.getValue().hashCode()) * 37;
		return retval;
	}

	public String toString() {
		return "Name=[" + this.getName() + "].BigDecimal=[" + this.getValue() + "]";
	}	
	
}
