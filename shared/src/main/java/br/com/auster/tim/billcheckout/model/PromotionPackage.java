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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import br.com.auster.om.reference.CustomizableEntity;

/**
 * @author framos
 * @version $Id$
 *
 */
public class PromotionPackage extends CustomizableEntity {



		// ---------------------------
		// atributes
		// ---------------------------
		private String	description;
		private String	shortDesc;
		private Map assocPlans;


		public PromotionPackage() {
			this.assocPlans = new HashMap();
		}


		public final String getDescription() { return description; }
		public final void setDescription(String description) { this.description = description; }

		public final String getShortDescription() { return shortDesc; }
		public final void setShortDescription(String shortDesc) { this.shortDesc = shortDesc; }


		public void addPlans(Map _map) {
			if (_map != null) {
				this.assocPlans = _map;
			}
		}

		public void addPlan(long _uid, String _planName) {
			Long key = buildKey(_uid);
			this.assocPlans.put(key, _planName);
		}

		public void clearPlans() {
			this.assocPlans.clear();
		}

		public Iterator getPlans() {
			return this.assocPlans.values().iterator();
		}

		public Iterator getPlansKeys() {
			return this.assocPlans.keySet().iterator();
		}

		public boolean containsKey(long _key) {
			return this.containsKey(new Long(_key));
		}

		public boolean containsKey(Long _key) {
			return this.assocPlans.containsKey(_key);
		}

		public static final Long buildKey(long _uid) {
			return new Long(_uid);
		}
}
