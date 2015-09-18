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
 * Created on 24/10/2007
 */
package br.com.auster.tim.billcheckout.param;

import java.util.ArrayList;
import java.util.List;

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
public class MpufftabListVO extends CustomizableEntity implements CacheableVO {

	/**
	 * Used to store the values of  <code>serialVersionUID</code>.
	 */
	private static final long serialVersionUID = 1L;

	private String contractNumber;
	private List<MpufftabVO> mpufftabVOList= new ArrayList<MpufftabVO>();

	// generated keys
	private CacheableKey  naturalKey;

	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	public List<MpufftabVO> getMpufftabVOList() {
		return mpufftabVOList;
	}

	public void setMpufftabVOList(List<MpufftabVO> mpufftabVOList) {
		this.mpufftabVOList = mpufftabVOList;
	}

	/**
	 * @see br.com.auster.billcheckout.caches.CacheableVO#getAlternateKey()
	 */
	public CacheableKey getAlternateKey() {
		if (this.naturalKey == null) {
			this.naturalKey = createAlternateKey( this.contractNumber );
		}
		return this.naturalKey;
	}

	/**
	 * @see br.com.auster.billcheckout.caches.CacheableVO#getKey()
	 */
	public CacheableKey getKey() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Inner class to handle the alternate key for ServicePlan instances
	 */
	protected static final class InnerAlternateKey implements CacheableKey {

		private String contractNumber;

		public InnerAlternateKey(String contractNumber) {
			this.contractNumber = contractNumber;
		}

		public boolean equals(Object _other) {
			if ( _other instanceof InnerAlternateKey ) {
				InnerAlternateKey other = (InnerAlternateKey) _other;
				if (this.contractNumber == null) {
					return other.contractNumber == null;
				}
				return (this.contractNumber.equals(other.contractNumber));
			}
			else
				return false;
		}

		public int hashCode() {
			if (this.contractNumber != null) {
				return this.contractNumber.hashCode();
			} else return 0;
		}

		public String getContractNumber() {
			return this.contractNumber;
		}

	}

	// ---------------------------
	// Helper methods
	// ---------------------------

	public static final CacheableKey createAlternateKey( String contractNumber ) {
		return new InnerAlternateKey(contractNumber);
	}

	public void add(MpufftabVO o) {
		mpufftabVOList.add(o);
	}

}