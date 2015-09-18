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
 * Created on Jun 25, 2009
 */
package br.com.auster.tim.billcheckout.param;

import br.com.auster.billcheckout.caches.CacheableKey;

/**
 * @author Nardo
 * @version $Id$
 * @since JDK1.4
 */
public class ContractExistsServicesVO extends ContractServicesPeriodVO {
	
	private static final long serialVersionUID = -7874878980783978089L;
	
	private Long prmValueId;
	private String shortDesc;
	private String svcDescr;
	
//	private long coId; --Existe em ContractServicesPeriodVO
//	private long snCode; --Existe em ContractServicesPeriodVO
//	private String csStatChng; --Existe em ContractServicesPeriodVO
	
	public ContractExistsServicesVO() {}

	@Override
	public CacheableKey getAlternateKey() {
		if (this.naturalKey == null) {
			this.naturalKey = createAlternateKey( this.getContractNumber(), this.shortDesc);
		}
		return this.naturalKey;
	}
	
	@Override
	public CacheableKey getKey() {
		throw new IllegalArgumentException("Cannot use this cache as use-alternate false; only as true");
	}

	public static final CacheableKey createKey() {
		throw new IllegalArgumentException("Cannot use this cache as createKey; only as createAlternateKey");
	}
	
	public static final CacheableKey createAlternateKey(String coID, String shortDesc) {
		return new InnerAlternateKey(coID, shortDesc);
	}
	
	protected static final class InnerAlternateKey implements CacheableKey {

		private String coId;
		private String svcShortDesc;

		public InnerAlternateKey( String contrId, String svcShort) {
			this.coId = contrId;
			this.svcShortDesc = svcShort;
		}

		public boolean equals(Object _other) {
			if ( _other instanceof InnerAlternateKey ) {
				InnerAlternateKey other = (InnerAlternateKey) _other;
				
				return ( this.coId.equals(other.coId) && this.svcShortDesc.equals(other.svcShortDesc) );
			}
			else { return false; }
		}

		public int hashCode() {
			int hash = 5;
			hash += 3 * (this.coId == null ? 1 : this.coId.hashCode());
			hash += 3 * (this.svcShortDesc == null ? 1 : this.svcShortDesc.hashCode());
			return hash;
		}

		public String getCoId() 		 				 { return this.coId;	}
		public void setCoId(String coId) 			     { this.coId = coId;    }
		public String getSvcShortDesc()  				 { return svcShortDesc; }
		public void setSvcShortDesc(String svcShortDesc) { this.svcShortDesc = svcShortDesc; }
	}

	
	public Long getPrmValueId() { return prmValueId; }
	public void setPrmValueId(Long prmValueId) { this.prmValueId = prmValueId; }
	public String getShortDesc() { return shortDesc; }
	public void setShortDesc(String shortDesc) { this.shortDesc = shortDesc; }
	public String getSvcDescr() { return svcDescr; }
	public void setSvcDescr(String svcDescr) { this.svcDescr = svcDescr; }

}
