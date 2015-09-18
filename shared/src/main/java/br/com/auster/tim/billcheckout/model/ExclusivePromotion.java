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
 * Created on 24/04/2008
 */
package br.com.auster.tim.billcheckout.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import br.com.auster.om.reference.CustomizableEntity;

/**
 * TODO What this class is responsible for
 *
 * @author William Soares
 * @version $Id$
 * @since JDK1.4
 */
public class ExclusivePromotion extends CustomizableEntity {


	/**
	 * Used to store the values of  <code>serialVersionUID</code>.
	 */
	private static final long serialVersionUID = 1L;


	// ---------------------------
	// atributes
	// ---------------------------
	private String	description;
	private String	shortDesc;
	private Map assocPromotions;


	public ExclusivePromotion() {
		this.assocPromotions = new HashMap();
	}


	public final String getDescription() {
		return description;
	}

	public final void setDescription(String description) {
		this.description = description;
	}

	public final String getShortDesc() {
		return shortDesc;
	}

	public final void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}

	public void addPromotions(Map _map) {
		if (_map != null) {
			this.assocPromotions = _map;
		}
	}

	public void addPromotion(long _uid, String _promotionShortDesc) {
		Long key = buildKey(_uid);
		this.assocPromotions.put(key, _promotionShortDesc);
	}

	public void clearPromotions() {
		this.assocPromotions.clear();
	}

	public Iterator getPromotions() {
		return this.assocPromotions.values().iterator();
	}

	public Iterator getPromotionsKeys() {
		return this.assocPromotions.keySet().iterator();
	}

	public boolean containsKey(long _key) {
		return this.containsKey(new Long(_key));
	}

	public boolean containsKey(Long _key) {
		return this.assocPromotions.containsKey(_key);
	}

	public boolean removeKey(Long _key) {
		return (this.assocPromotions.remove(_key) != null);
	}

	public static final Long buildKey(long _uid) {
		return new Long(_uid);
	}

}
