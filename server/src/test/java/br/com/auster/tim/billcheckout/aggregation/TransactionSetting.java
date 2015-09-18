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
 * Created on 19/08/2007
 */
package br.com.auster.tim.billcheckout.aggregation;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import br.com.auster.common.data.groovy.GroovyDataRetriever;
import br.com.auster.common.xml.DOMUtils;
import br.com.auster.dware.request.file.FileRequest;

/**
 * @author framos
 * @version $Id$
 *
 */
public class TransactionSetting {

	
	public static final String CONFIGURATION_FILE = "src/test/resources/bgh/transaction/set-transaction-id.xml";
	
	
	
	public static void main(String args[]) {
		try {
			GroovyDataRetriever engine = new GroovyDataRetriever();
			engine.configure(DOMUtils.openDocument(CONFIGURATION_FILE, false));
			FileRequest request = new FileRequest(new File("src/test/FILENAME.BGH"));
			Map info = new HashMap();
			info.put("request", request);
			engine.retrieve(info);
			System.out.println(request.getAttributes().containsKey("format"));
			System.out.println(request.getAttributes().get("format"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
