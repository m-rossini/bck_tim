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
 * Created on 16/10/2007
 */
package br.com.auster.tim.billcheckout.model;

import br.com.auster.om.reference.CustomizableEntity;

/**
 * @author framos
 * @version $Id$
 *
 */
public class Plan extends CustomizableEntity {



		// ---------------------------
		// atributes
		// ---------------------------
		private long tmcode;
		private String	description;
		private String	shortDesc;
		private String	planName;
		private String state;
		private boolean mandatory;


		public Plan() { }


		public final long getTMCode() { return tmcode; }
		public final void setTMCode(long _tmcode) { this.tmcode = _tmcode; }

		public final String getDescription() { return description; }
		public final void setDescription(String _description) { this.description = _description; }

		public final String getShortDescription() { return shortDesc; }
		public final void setShortDescription(String shortDesc) { this.shortDesc = shortDesc; }

		public final String getPlanName() { return planName; }
		public final void setPlanName(String _planName) { this.planName = _planName; }

		public final String getState() { return state; }
		public final void setState(String _state) { this.state = _state; }

		public final boolean isMandatory() { return this.mandatory; }
		public final void setMandatory(boolean _mandatory) { this.mandatory = _mandatory; }

}
