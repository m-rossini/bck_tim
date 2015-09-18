/*
 * Copyright (c) 2004-2010 Auster Solutions. All Rights Reserved.
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
 * Created on 25/03/2010
 */
package br.com.auster.tim.billcheckout.crashprogram;

import java.util.LinkedList;
import java.util.List;

import br.com.auster.billcheckout.caches.CacheableKey;
import br.com.auster.billcheckout.caches.CacheableVO;
import br.com.auster.om.reference.CustomizableEntity;

/**
 * TODO What this class is responsible for
 *
 * @author Frederico Ramos
 * @version $Id$
 * @since JDK1.4
 */
public class CrashProgramPackageInfoVO extends CustomizableEntity implements CacheableVO {

	
	
	// ---------------------------
	// Atributes
	// ---------------------------
	private static final long serialVersionUID = 1L;

	private String tmcode;
	private String packageName;
	private List<String> services;
	private List<String> csgcodes;
	
	private CacheableKey  naturalKey;
	
	
	
	// ---------------------------
	// Constructors
	// ---------------------------
	public CrashProgramPackageInfoVO() {
		this(0);
	}

	public CrashProgramPackageInfoVO(long _uid) {
		super(_uid);
		
		this.csgcodes = new LinkedList<String>();
		this.services = new LinkedList<String>();
	}
	
	

	// ---------------------------
	// Accessors
	// ---------------------------

	public final String getTMCode() { return this.tmcode; }
	public final void setTMCode(String _tmcode) { this.tmcode = _tmcode; }

	public final String getPackageName() { return this.packageName; }
	public final void setPackageName(String _packageName) { this.packageName = _packageName; }

	public final List<String> getServiceList() { return this.services; }
	
	public final List<String> getCsgCodeList() { return this.csgcodes; }
	
	/**
	 * @see br.com.auster.billcheckout.caches.CacheableVO#getAlternateKey()
	 */
	public CacheableKey getKey() {
		if (this.naturalKey == null) {
			// actually creates alternate key using Plan description
			// instead of short description
			this.naturalKey = createKey( this.tmcode, this.packageName );
		}
		return this.naturalKey;
	}



	/**
	 * Inner class to handle the alternate key for PlansVO instances
	 */
	protected static final class InnerKey implements CacheableKey {

		private String tmcode;
		private String packageName;
		

		public InnerKey( String _tmcode, String _packageName ) {
			this.tmcode = _tmcode;
			this.packageName = _packageName;
		}

		public boolean equals(Object _other) {
			boolean equal = true;
			if ( _other instanceof InnerKey ) {
				InnerKey other = (InnerKey) _other;
				equal = equal && other.tmcode.equals(this.tmcode);
				equal = equal && other.packageName.equals(this.packageName);
			} 
			return equal;
		}

		public int hashCode() {
			int hash = this.tmcode.hashCode();
			hash += 17 * this.packageName.hashCode();
			return hash;
		}

		public String getTMCode() { return this.tmcode; }
		public void setTMCode(String _tmcode) { this.tmcode = _tmcode; }

		public String getPackageName() { return this.packageName; }
		public void setPackageName(String _pkgName) { this.packageName = _pkgName; }
	}

	/**
	 * @see br.com.auster.billcheckout.caches.CacheableVO#getKey()
	 */
	public CacheableKey getAlternateKey() {
		return null;
	}

	// ---------------------------
	// Helper methods
	// ---------------------------

	public static final CacheableKey createKey( String _tmcode, String _packageName) {
		return new InnerKey( _tmcode, _packageName );
	}	
}
