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
 * Created on 30/04/2010
 */
package br.com.auster.tim.billcheckout.tariff.model;

import java.util.Date;


/**
 * @author framos
 */
public class MicrocellTariff {
	
	private String mcShortDescription;
	private String fullDescription;
	private Date effectiveDate;
	private Date loadedDate;
	private Double priceValue;
	private Double scaleFactor;
	private int umcode;

	
	
	
	public MicrocellTariff(String mcShortDescription, String fullDescription,
			Date effectiveDate, Date loadedDate, Object _priceValue,
			Object _scaleFactor, int umcode) {
		this.mcShortDescription = mcShortDescription;
		this.fullDescription = fullDescription;
		this.effectiveDate = effectiveDate;
		this.loadedDate = loadedDate;
		if (_priceValue != null) {
			this.priceValue = new Double(((Number)_priceValue).doubleValue() );
		}
		if (_scaleFactor != null) {
			this.scaleFactor = new Double(((Number)_scaleFactor).doubleValue() );
		}
		this.umcode = umcode;
	}
	
	
	public final String getMcShortDescription() {
		return mcShortDescription;
	}
	public final void setMcShortDescription(String mcShortDescription) {
		this.mcShortDescription = mcShortDescription;
	}
	public final String getFullDescription() {
		return fullDescription;
	}
	public final void setFullDescription(String fullDescription) {
		this.fullDescription = fullDescription;
	}
	public final Date getEffectiveDate() {
		return effectiveDate;
	}
	public final void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public final Date getLoadedDate() {
		return loadedDate;
	}
	public final void setLoadedDate(Date loadedDate) {
		this.loadedDate = loadedDate;
	}
	public final Double getPriceValue() {
		return priceValue;
	}
	public final void setPriceValue(Double priceValue) {
		this.priceValue = priceValue;
	}
	public final Double getScaleFactor() {
		return scaleFactor;
	}
	public final void setScaleFactor(Double scaleFactor) {
		this.scaleFactor = scaleFactor;
	}
	public final int getUmcode() {
		return umcode;
	}
	public final void setUmcode(int umcode) {
		this.umcode = umcode;
	}
}
