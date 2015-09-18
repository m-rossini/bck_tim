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
 * Created on 06/03/2008
 */
package br.com.auster.tim.billcheckout.npack;

import java.util.HashMap;
import java.util.Map;

import br.com.auster.common.text.DurationFormat;
import br.com.auster.om.util.UnitCounter;

/**
 * Named attribute like NamedDouble, but using long type.
 *
 * @author William Soares
 * @version $Id$
 * @since JDK1.4
 */
public class NPackOCCDescription {

	
	
	public static final String PATTERN_USED_TIME = "tempo_utilizado";
	public static final String PATTERN_TOTAL_TIME = "tempo_pacote";
	public static final String PATTERN_PHONE_NUMBER = "num_fone";
	
	public static final String PATTERN_INIT_VAR = "<";
	public static final String PATTERN_END_VAR = ">";
	
	
	
	private Map<String, String> occDescriptions;
	

	/**
	 * 
	 */
	public NPackOCCDescription() {
		this.occDescriptions = new HashMap<String, String>();
	}

	public void buildOCCDescriptions(String _occPattern, Map<String, UnitCounter> _accumulatedUsage, long _totalMinutes) {
		for (String accessNumber : _accumulatedUsage.keySet()) {
			this.occDescriptions.put(accessNumber,  buildFinalString( _occPattern, _accumulatedUsage.get(accessNumber), accessNumber, _totalMinutes ));
		}
	}
	

	
	public boolean isContractAccumulated(String _accessNumber) {
		return this.occDescriptions.containsKey(_accessNumber);
	}
	
	public String getContractOCCDescription(String _accessNumber) {
		return this.occDescriptions.get(_accessNumber);
	}

	public boolean isContractOCCDescription(String _accessNumber, String _occ) {
		String localOcc = this.getContractOCCDescription(_accessNumber);
		return _occ.equals(localOcc);
	}
	
	
	private String buildFinalString(String _pattern, UnitCounter _usedVolume, String _phoneNumber,  double _totalVolume) {
		StringBuilder descBuilder = new StringBuilder();
				
		// adding init of the text
		int start = 0;
		int initIndex = 0;
		do {
			initIndex = _pattern.indexOf(PATTERN_INIT_VAR, start);
			if (initIndex > 0) {
				if (initIndex > start) {
					descBuilder.append(_pattern.substring(start, initIndex));
				} 
				int endIndex = _pattern.indexOf(PATTERN_END_VAR, initIndex);
				descBuilder.append( formatValue(_usedVolume.getUnits(), _totalVolume, _phoneNumber, _pattern.substring(initIndex+1, endIndex)) );
				start = endIndex+1;
			} else {
				descBuilder.append(_pattern.substring(start));
			}
		} while (initIndex > 0);
		
		return descBuilder.toString();
	}
	
	private String formatValue(double _usedVolume, double _totalVolume, String _phoneNumber, String _token) {		
		if (PATTERN_PHONE_NUMBER.equals(_token)) {
			return _phoneNumber;
		} else if (PATTERN_TOTAL_TIME.equals(_token)) {
			int[] x = DurationFormat.getHMS(_totalVolume);
			return (x[0]*60 + x[1]) + "m";
		} else if (PATTERN_USED_TIME.equals(_token)) {
			return DurationFormat.formatFromSeconds(_usedVolume, "m", "s", true);
		}
		return "";
	}
}
