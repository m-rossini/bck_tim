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
 * Created on Jul 23, 2009
 */
package br.com.auster.tim.billcheckout.bscs;

import br.com.auster.billcheckout.caches.CacheableKey;
import br.com.auster.billcheckout.caches.CacheableVO;

/**
 * @author Nardo
 * @version $Id$
 * @since JDK1.4
 */
public class ScanStructureLevelVO implements CacheableVO {
	
	private static String custCode;
	private static String codPlanoLA;
	private static String codServLA;
	private static String codPcteLA;
	
	private CacheableKey key;

	public CacheableKey getAlternateKey() {
		throw new IllegalArgumentException("Cannot use this cache as use-alternate true; only as false");
	}

	public CacheableKey getKey() {
		if (this.key == null) {
			this.key = createKey( ScanStructureLevelVO.custCode,  ScanStructureLevelVO.codPlanoLA, 
								  ScanStructureLevelVO.codServLA, ScanStructureLevelVO.codPcteLA);
		}
		return this.key;
	}
	
	public static final CacheableKey createAlternateKey() {
		throw new IllegalArgumentException("Cannot use this cache as createAlternateKey; only as createKey");
	}
	
	public static final CacheableKey createKey(String codCust, String codPlan, String codServ, String codPack) {
		ScanStructureLevelVO.custCode   = codCust;  
		ScanStructureLevelVO.codPlanoLA = codPlan;
		ScanStructureLevelVO.codServLA  = codServ;
		ScanStructureLevelVO.codPcteLA  = codPack;
		return new InnerKey(codCust, codPlan, codServ, codPack);
	}
	
	protected static final class InnerKey implements CacheableKey {

		private String custCod;
		private String planCod;
		private String servCod;
		private String packCod;
		
		public InnerKey(String custCod, String planCod, String servCod,	String packCod) {
			this.custCod = custCod;
			this.planCod = planCod;
			this.servCod = servCod;
			this.packCod = packCod;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((custCod == null) ? 0 : custCod.hashCode());
			result = prime * result
					+ ((packCod == null) ? 0 : packCod.hashCode());
			result = prime * result
					+ ((planCod == null) ? 0 : planCod.hashCode());
			result = prime * result
					+ ((servCod == null) ? 0 : servCod.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final InnerKey other = (InnerKey) obj;
			if (custCod == null) {
				if (other.custCod != null)
					return false;
			} else if (!custCod.equals(other.custCod))
				return false;
			if (packCod == null) {
				if (other.packCod != null)
					return false;
			} else if (!packCod.equals(other.packCod))
				return false;
			if (planCod == null) {
				if (other.planCod != null)
					return false;
			} else if (!planCod.equals(other.planCod))
				return false;
			if (servCod == null) {
				if (other.servCod != null)
					return false;
			} else if (!servCod.equals(other.servCod))
				return false;
			return true;
		}
		public String getCustCod() { return custCod; }
		public void setCustCod(String custCod) { this.custCod = custCod; }
		public String getPlanCod() { return planCod; }
		public void setPlanCod(String planCod) { this.planCod = planCod; }
		public String getServCod() { return servCod; }
		public void setServCod(String servCod) { this.servCod = servCod; }
		public String getPackCod() { return packCod; }
		public void setPackCod(String packCod) { this.packCod = packCod; }
	}
}
