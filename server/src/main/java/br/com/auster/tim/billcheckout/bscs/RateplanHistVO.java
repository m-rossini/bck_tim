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
 * Created on 01/07/2008
 */
package br.com.auster.tim.billcheckout.bscs;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import br.com.auster.billcheckout.caches.CacheableKey;
import br.com.auster.billcheckout.caches.CacheableVO;
import br.com.auster.common.datastruct.RangeMap;

/**
 * @author framos
 * @version $Id$
 *
 */
public class RateplanHistVO implements CacheableVO {



	private CacheableKey key;

	private String contractNumber;
	private RangeMap changeLog = new RangeMap();
	private Map<Integer, Integer> seqno = new HashMap<Integer, Integer>();



	public final String getContractNumber() { return this.contractNumber;}
	public final void setContractNumber(String _cn) { this.contractNumber = _cn;}


	public final void addRange(Date _start, Date _end, int _seqno, Integer _tmcode) {
		Integer seqnoInt = new Integer(_seqno);
		long endTime = Long.MAX_VALUE;
		if (_end != null) {
			endTime = _end.getTime()-1;
		}
		changeLog.add(_start.getTime(), endTime, seqnoInt);
		seqno.put(seqnoInt, _tmcode);
	}

	public final List<Integer> getTMCodes(Date _start, Date _end) {
		// getting initial seqno
		List tmp = (List)this.changeLog.get(_start.getTime());
		if (tmp == null) { return null; }
		int startSeqno = ((Integer)tmp.iterator().next()).intValue();
		// getting last seqno
		tmp = (List)this.changeLog.get(_end.getTime());
		int endSeqno = this.seqno.size();
		if (tmp != null) { endSeqno = ((Integer)tmp.iterator().next()).intValue(); }
		// getting all tmcodes
		List<Integer> result = new LinkedList<Integer>();
		for (int i=startSeqno; i <= endSeqno ; i++) {
			if (this.seqno.containsKey(new Integer(i))) {
				result.add( this.seqno.get(new Integer(i)) );
			}
		}
		return result;
	}



	/**
	 * @see br.com.auster.billcheckout.caches.CacheableVO#getAlternateKey()
	 */
	public CacheableKey getAlternateKey() {
		return this.getKey();
	}

	/**
	 * @see br.com.auster.billcheckout.caches.CacheableVO#getKey()
	 */
	public CacheableKey getKey() {
		if (this.key == null) {
			this.key = createKey( this.contractNumber );
		}
		return this.key;
	}

	/**
	 * Inner class to handle the alternate key for PlansVO instances
	 */
	protected static final class InnerKey implements CacheableKey {

		private String contractNumber;

		public InnerKey( String _contractNumber) {
			this.contractNumber = _contractNumber;
		}

		public boolean equals(Object _other) {
			if ( _other instanceof InnerKey ) {
				InnerKey other = (InnerKey) _other;
				return this.contractNumber.equals(other.contractNumber);
			}
			else { return false; }
		}

		public int hashCode() {
			return this.contractNumber.hashCode();
		}

		public String getContractNumber() { return this.contractNumber; }
	}


	// ---------------------------
	// Helper methods
	// ---------------------------

	public static final CacheableKey createKey( String _contractNumber ) {
		return new InnerKey(_contractNumber);
	}

}
