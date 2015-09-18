/**
 * Copyright (c) 2004-2009 Auster Solutions. All Rights Reserved.
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
 * Created on Jul 7, 2009
 */
package br.com.auster.tim.billcheckout.bscs;

import java.util.Date;

import br.com.auster.billcheckout.caches.CacheableKey;
import br.com.auster.billcheckout.caches.CacheableVO;

/**
 * @author Nardo
 * @version $Id$
 * @since JDK1.4
 */
public class ContractExistsPackVO implements CacheableVO {
	
	private CacheableKey key;

	private static Date cutDate;
	private static Long prmValueId;
	
	private String longName;
	private String shortNamePack;
	
	
	public CacheableKey getAlternateKey() {
		if (this.key == null) {
			this.key = createAlternateKey( ContractExistsPackVO.prmValueId, this.shortNamePack, ContractExistsPackVO.cutDate);
		}
		return this.key;
	}
	
	public CacheableKey getKey() {
		throw new IllegalArgumentException("Cannot use this cache as use-alternate false; only as true");
	}

	public static final CacheableKey createKey() {
		throw new IllegalArgumentException("Cannot use this cache as createKey; only as createAlternateKey");
	}
	
	public static final CacheableKey createAlternateKey(Long prmValue, String shortName, Date dateCut) {
		ContractExistsPackVO.prmValueId = prmValue;
		ContractExistsPackVO.cutDate = dateCut;
		return new InnerAlternateKey(prmValue, shortName, dateCut);
	}
	
	protected static final class InnerAlternateKey implements CacheableKey {

		private Long   prmValue;
		private String shortName;
		private Date   dateCut;
		
		public InnerAlternateKey( Long prmValue, String shortName, Date dateCut) {
			this.prmValue  = prmValue;
			this.shortName = shortName;
			this.dateCut   = dateCut;
		}

		public boolean equals(Object _other) {
			if ( _other instanceof InnerAlternateKey ) {
				InnerAlternateKey other = (InnerAlternateKey) _other;
				return ( this.prmValue.equals(other.prmValue) && this.shortName.equals(other.shortName) && this.dateCut.equals(other.dateCut));
			}
			else { return false; }
		}

		public int hashCode() {
			int hash = 5;
			hash += 3 * (this.prmValue == null ? 1 : this.prmValue.hashCode());
			hash += 3 * (this.shortName == null ? 1 : this.shortName.hashCode());
			hash += 3 * (this.dateCut == null ? 1 : this.dateCut.hashCode());
			return hash;
		}

		public Long getPrmValue() 			 	{ return prmValue; }
		public void setPrmValue(Long prmValue) 	{ this.prmValue = prmValue; }
		public String getShortName() 			 	{ return shortName; }
		public void setShortName(String shortName)  { this.shortName = shortName; }
		public Date getDateCut() 					{ return dateCut; }
		public void setDateCut(Date dateCut) 		{ this.dateCut = dateCut; }
	}

	public String getShortNamePack() {
		return shortNamePack;
	}

	public void setShortNamePack(String shortNamePack) {
		this.shortNamePack = shortNamePack;
	}

	public String getLongName() {
		return longName;
	}

	public void setLongName(String longName) {
		this.longName = longName;
	}
}
