/*
 * Copyright (c) 2004-2006 Auster Solutions. All Rights Reserved.
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
 * Created on 23/08/2006
 */
package br.com.auster.tim.billcheckout.util;

import br.com.auster.billcheckout.consequence.DimensionCache;
import br.com.auster.tim.bgh.sax.TIMOMLoader;

/**
 * This class doesnot requires configuration. It will only create an internal <code>Map</code>
 *   which will help rules to store and retrieve dimension objects.
 * 
 * @author framos
 * @version $Id$
 *
 */
public class TIMDimensionCache extends DimensionCache {

	
	
	
	// ---------------------------
	// Constructors
	// ---------------------------
	
	public TIMDimensionCache() {
		super();
	}

	/**
	 * Retrieves the dimension object referenced by <code>_key</code> from the current 
	 * 	cache. 
	 */
	public Object getFromCache(String _key) {
		String searchKey = _key;
		if ( (_key == null) || ("0".equals(_key)) ) {
			searchKey = TIMOMLoader.TIM_CARRIERCODE;
		}
		return super.getFromCache(searchKey);
	}
}
