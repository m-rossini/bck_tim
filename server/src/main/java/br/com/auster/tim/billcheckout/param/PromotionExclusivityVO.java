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
 * Created on 22/04/2008
 */
package br.com.auster.tim.billcheckout.param;

import java.util.Set;

import br.com.auster.billcheckout.caches.CacheableKey;
import br.com.auster.billcheckout.caches.CacheableVO;
import br.com.auster.om.reference.CustomizableEntity;

/**
 * TODO What this class is responsible for
 *
 * @author William Soares
 * @version $Id$
 * @since JDK1.4
 */
public class PromotionExclusivityVO extends CustomizableEntity implements CacheableVO {
	
	// ---------------------------
	// atributes
	// ---------------------------
	private static final long serialVersionUID = 1L;
	 
	private String description;
	private String shortDescription;
	private Set relatedPromotions;

	// generated keys
	private CacheableKey  naturalKey;

	// ---------------------------
	// Accessors
	// ---------------------------
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	
	public Set getRelatedPromotions() {
		return relatedPromotions;
	}

	public void setRelatedPromotions(Set relatedPromotions) {
		this.relatedPromotions = relatedPromotions;
	}
	
	// ---------------------------
	// CacheableVO interface methods
	// ---------------------------

	/**
	 * @see br.com.auster.billcheckout.caches.CacheableVO#getAlternateKey()
	 */
	public CacheableKey getAlternateKey() {
		if (this.naturalKey == null) {
			// actually creates alternate key using Plan description
			// instead of short description
			this.naturalKey = createAlternateKey( this.getShortDescription() );
		}
		return this.naturalKey;
	}	
	
	/**
	 * Inner class to handle the alternate key for PromotionExclusivityVO instances
	 */
	protected static final class InnerAlternateKey implements CacheableKey {
		
		private String shortDescription;
		
		public InnerAlternateKey( String shortDescription ) {
			this.shortDescription = shortDescription;
		}
		
		public boolean equals(Object _other) {
			if ( _other instanceof InnerAlternateKey ) {
				InnerAlternateKey other = (InnerAlternateKey) _other;
				return  (this.shortDescription==null) ? "".equals(other.shortDescription) : this.shortDescription.equals(other.shortDescription);
			}
			else 
				return false;
		}

		public int hashCode() { 
			return this.shortDescription == null ? 0 : this.shortDescription.hashCode();		
		}

		public String getShortDescription() {
			return shortDescription;
		}		

	}
	
	/**
	 * @see br.com.auster.billcheckout.caches.CacheableVO#getKey()
	 */
	public CacheableKey getKey() {
		return null;
	}
	
	// ---------------------------
	// Helper methods
	// ---------------------------
		
	public static final CacheableKey createAlternateKey( String shortDescription ) {
		return new InnerAlternateKey( shortDescription );
	}

	@Override
	public boolean equals(Object _other) {
		if ( _other instanceof PromotionExclusivityByContractVO ) {
			PromotionExclusivityByContractVO other = (PromotionExclusivityByContractVO) _other;
			return  this.shortDescription.equals(other.getShortDescription());
		}
		else 
			return false;
	}
	
}
