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
 * Created on 11/10/2007
 */
package br.com.auster.tim.billcheckout.tariff;

import java.io.Serializable;

/**
 * TODO What this class is responsible for
 *
 * @author William Soares
 * @version $Id$
 * @since JDK1.4
 */
public class CrashProgramRatesVO implements Serializable {

	

	// ---------------------------
	// Atributes
	// ---------------------------
	private static final long serialVersionUID = 1L;

	private String costId;
	private String csgCode;
	private long initVolume;
	private long endVolume;
	private double ppm;
	
	

	// ---------------------------
	// Constructors
	// ---------------------------
	public CrashProgramRatesVO() {
	}



	// ---------------------------
	// Accessors
	// ---------------------------

	public final String getCostId() { return costId; }
	public final void setCostId(String costId) { this.costId = costId; }

	public final String getCsgCode() { return csgCode; }
	public final void setCsgCode(String csgCode) { this.csgCode = csgCode; }

	public final long getInitVolume() { return initVolume; }
	public final void setInitVolume(long initVolume) { this.initVolume = initVolume; }

	public final long getEndVolume() { return endVolume; }
	public final void setEndVolume(long endVolume) { this.endVolume = endVolume; }

	public final double getPPM() { return ppm; }
	public final void setPPM(double ppm) { this.ppm = ppm; }

}
