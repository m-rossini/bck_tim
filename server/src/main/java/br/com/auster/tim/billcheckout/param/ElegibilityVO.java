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
 * Created on Jun 17, 2009
 */
package br.com.auster.tim.billcheckout.param;

import java.util.Date;

import br.com.auster.billcheckout.caches.CacheableKey;
import br.com.auster.billcheckout.caches.CacheableVO;

/**
 * @author Nardo
 * @version $Id$
 * @since JDK1.4
 */
public class ElegibilityVO implements CacheableVO {
	
	private static final long serialVersionUID = -1583000868535465359L;
	
	/* atributo apenas para armazenar o short_desc do plano usado em getAlternateKey() e getKey() */
	private static String shortPlan;
	
	private long   codPromo;
	private String indEstrutura;
	private String codPlanoIndiv;
	private String codServIndiv;
	private String codPcteIndiv;
	private String codPlanoLA;
	private String codServLA;
	private String codPcteLA;
	private String codServDesvio;
	private double vlrDesvio;
	private Date   dataInicioVig;
	private Date   dataFimVig;

	// generated keys
	private CacheableKey logicKey;
	private CacheableKey  naturalKey;

	// ---------------------------
	// Constructors
	// ---------------------------
	
	public ElegibilityVO (){
		
	}
	
	public ElegibilityVO(long codPromo, String indEstrutura, String codPlanoIndiv, String codServIndiv, String codPcteIndiv,
			String codPlanoLA, String codServLA, String codPcteLA, String codServDesvio, long vlrDesvio, Date dataInicioVig,
			Date dataFimVig) {
		super();
		this.codPromo = codPromo;
		this.indEstrutura = indEstrutura;
		this.codPlanoIndiv = codPlanoIndiv;
		this.codServIndiv = codServIndiv;
		this.codPcteIndiv = codPcteIndiv;
		this.codPlanoLA = codPlanoLA;
		this.codServLA = codServLA;
		this.codPcteLA = codPcteLA;
		this.codServDesvio = codServDesvio;
		this.vlrDesvio = vlrDesvio;
		this.dataInicioVig = dataInicioVig;
		this.dataFimVig = dataFimVig;
	}

	// ---------------------------
	// CacheableVO interface methods
	// ---------------------------

	/**
	 * @see br.com.auster.billcheckout.caches.CacheableVO#getAlternateKey()
	 */
	public CacheableKey getAlternateKey() {
		if (this.naturalKey == null) {
			this.naturalKey = createAlternateKey( ElegibilityVO.shortPlan, this.getIndEstrutura() );
		}
		return this.naturalKey;
	}
	
	/**
	 * Método idêntico ao getAlternateKey(), apenas se diferencia o retorno de naturalKey para logicKey;
	 * 
	 * @see br.com.auster.billcheckout.caches.CacheableVO#getKey()
	 */
	
	public CacheableKey getKey() {
		if (this.logicKey == null) {
			this.logicKey = createKey( ElegibilityVO.shortPlan, this.getIndEstrutura() );
		}
		return this.logicKey;
	}

	protected static final class InnerAlternateKey implements CacheableKey {

		private String planShort;
		private String typeClient;

		public InnerAlternateKey(String plan, String type) { 
			this.planShort = plan;
			this.typeClient = type;
		}

		public boolean equals(Object _other) {
			if (_other instanceof InnerAlternateKey) {
				InnerAlternateKey other = (InnerAlternateKey) _other;
				return ( this.planShort.equals(other.planShort) && this.typeClient.equals(other.typeClient) );
			} else{
				return false;
			}
		}

		public int hashCode() {
			int hashcode = (int) (this.planShort == null ? 0 : this.planShort.hashCode());
			hashcode += (this.typeClient == null ? 0 : this.typeClient.hashCode());
			return hashcode;
		}

		public String getPlanShort() {
			return planShort;
		}

		public void setPlanShort(String planShort) {
			this.planShort = planShort;
		}

		public String getTypeClient() {
			return typeClient;
		}

		public void setTypeClient(String typeClient) {
			this.typeClient = typeClient;
		}
	}

	// ---------------------------
	// Helper methods
	// ---------------------------

	/**
	 * Método identico ao createAlternateKey, assim, nem criado InnerKey, usando InnerAlternateKey 
	 */
	public static final CacheableKey createKey(String plan, String type) {
		return ElegibilityVO.createAlternateKey(plan, type);
	}

	public static final CacheableKey createAlternateKey(String plan, String type) {
		ElegibilityVO.shortPlan=plan;
		return new InnerAlternateKey(plan, type);
	}

	public long getCodPromo() {
		return codPromo;
	}

	public void setCodPromo(long codPromo) {
		this.codPromo = codPromo;
	}

	public String getIndEstrutura() {
		return indEstrutura;
	}

	public void setIndEstrutura(String indEstrutura) {
		this.indEstrutura = indEstrutura;
	}

	public String getCodPlanoIndiv() {
		return codPlanoIndiv;
	}

	public void setCodPlanoIndiv(String codPlanoIndiv) {
		this.codPlanoIndiv = codPlanoIndiv;
	}

	public String getCodServIndiv() {
		return codServIndiv;
	}

	public void setCodServIndiv(String codServIndiv) {
		this.codServIndiv = codServIndiv;
	}

	public String getCodPcteIndiv() {
		return codPcteIndiv;
	}

	public void setCodPcteIndiv(String codPcteIndiv) {
		this.codPcteIndiv = codPcteIndiv;
	}

	public String getCodPlanoLA() {
		return codPlanoLA;
	}

	public void setCodPlanoLA(String codPlanoLA) {
		this.codPlanoLA = codPlanoLA;
	}

	public String getCodServLA() {
		return codServLA;
	}

	public void setCodServLA(String codServLA) {
		this.codServLA = codServLA;
	}

	public String getCodPcteLA() {
		return codPcteLA;
	}

	public void setCodPcteLA(String codPcteLA) {
		this.codPcteLA = codPcteLA;
	}

	public String getCodServDesvio() {
		return codServDesvio;
	}

	public void setCodServDesvio(String codServDesvio) {
		this.codServDesvio = codServDesvio;
	}

	public double getVlrDesvio() {
		return vlrDesvio;
	}

	public void setVlrDesvio(long vlrDesvio) {
		this.vlrDesvio = vlrDesvio;
	}

	public Date getDataInicioVig() {
		return dataInicioVig;
	}

	public void setDataInicioVig(Date dataInicioVig) {
		this.dataInicioVig = dataInicioVig;
	}

	public Date getDataFimVig() {
		return dataFimVig;
	}

	public void setDataFimVig(Date dataFimVig) {
		this.dataFimVig = dataFimVig;
	}
}
