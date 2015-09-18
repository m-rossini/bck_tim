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
 * Created on 04/10/2007
 */
package br.com.auster.tim.billcheckout.param;

import java.util.Date;

import br.com.auster.billcheckout.caches.CacheableKey;
import br.com.auster.billcheckout.caches.CacheableVO;

/**
 * TODO What this class is responsible for
 *
 * @author William Soares
 * @version $Id$
 * @since JDK1.4
 */
public class CBCFContractVO implements CacheableVO {



	// ---------------------------
	// atributes
	// ---------------------------

	private long codFidelizacao;
	private String customerId;
	private Date startDate;
	private Date endDate;
	private double totalAmount;


	// generated keys
	private CacheableKey  logicKey;
	private CacheableKey  naturalKey;



	// ---------------------------
	// Constructors
	// ---------------------------

	public CBCFContractVO() {}



	// ---------------------------
	// Accessors
	// ---------------------------

	public final String getCustomerId() { return customerId; }
	public final void setCustomerId(String _customerId) { this.customerId = _customerId; }

	public final long getCodFidelizacao() { return codFidelizacao; }
	public final void setCodFidelizacao(long _codFidelizacao) { this.codFidelizacao = _codFidelizacao; }

	public final Date getStartDate() { return startDate; }
	public final void setStartDate(Date _startDate) { this.startDate = _startDate; }
	public final Date getEndDate() { return endDate; }
	public final void setEndDate(Date _endDate) { this.endDate = _endDate; }

	public final double getTotalAmount() { return totalAmount; }
	public final void setTotalAmount(double _totalAmount) { this.totalAmount = _totalAmount; }


	// ---------------------------
	// CacheableVO interface methods
	// ---------------------------

	/**
	 * @see br.com.auster.billcheckout.caches.CacheableVO#getKey()
	 */
	public CacheableKey getKey() {
		if (this.naturalKey == null) {
			// actually creates alternate key using Plan description
			// instead of short description
			this.naturalKey = createKey( this.codFidelizacao );
		}
		return this.naturalKey;
	}

	/**
	 * Inner class to handle the alternate key for PlansVO instances
	 */
	protected static final class InnerKey implements CacheableKey {

		private long codFidelizacao;

		public InnerKey(long _codFidelizacao) {
			this.codFidelizacao = _codFidelizacao;
		}

		public boolean equals(Object _other) {
			if ( _other instanceof InnerKey ) {
				InnerKey other = (InnerKey) _other;
				return  this.codFidelizacao == other.codFidelizacao;
			}
			else
				return false;
		}

		public int hashCode() {
			return (int) this.codFidelizacao;
		}

		public long getCodFidelizacao() { return this.codFidelizacao; }
	}

	/**
	 * @see br.com.auster.billcheckout.caches.CacheableVO#getAlternateKey()
	 */
	public CacheableKey getAlternateKey() {
		if (this.logicKey == null) {
			// actually creates alternate key using Plan description
			// instead of short description
			this.logicKey = createAlternateKey( this.customerId, this.startDate, this.endDate );
		}
		return this.logicKey;
	}

	protected static final class InnerAlternateKey implements CacheableKey {

		private String customerId;
		private Date startDate;
		private Date endDate;

		public InnerAlternateKey(String _customerId, Date _startDate, Date _endDate) {
			this.startDate = _startDate;
			this.endDate = _endDate;
			this.customerId = _customerId;
		}

		public boolean equals(Object _other) {
			if ( _other instanceof InnerAlternateKey ) {
				InnerAlternateKey other = (InnerAlternateKey) _other;
				return  ( this.customerId.equals(other.customerId) &&
						  this.startDate.equals(other.startDate) &&
						  this.endDate.equals(other.endDate) );
			}
			else
				return false;
		}

		public int hashCode() {
			int hash = this.customerId.hashCode();
			hash += 17* this.startDate.hashCode();
			hash += 17* this.endDate.hashCode();
			return hash;
		}

		public String getCustomerId() { return this.customerId; }
		public Date getStartDate() { return this.startDate; }
		public Date getEndDate() { return this.endDate; }
	}




	// ---------------------------
	// Helper methods
	// ---------------------------

	public static final CacheableKey createKey( long _codFidelizacao ) {
		return new InnerKey(_codFidelizacao);
	}

	public static final CacheableKey createAlternateKey( String _customerId, Date _startDate, Date _endDate ) {
		return new InnerAlternateKey( _customerId, _startDate, _endDate );
	}
}
