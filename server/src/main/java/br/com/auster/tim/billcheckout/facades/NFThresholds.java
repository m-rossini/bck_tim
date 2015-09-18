/*
* Copyright (c) 2004-2005 Auster Solutions do Brasil. All Rights Reserved.
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
* Created on 18/08/2006
*/
//TODO Comment this Class
package br.com.auster.tim.billcheckout.facades;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;

import org.w3c.dom.Element;

import br.com.auster.common.io.IOUtils;
import br.com.auster.om.reference.facade.ConfigurationException;
import br.com.auster.om.reference.facade.ReferenceFacades;


/**
 * @author mtengelm
 * @version $Id: NFThresholds.java 30 2006-08-26 01:10:11Z mtengelm $
 */
public class NFThresholds implements ReferenceFacades {

	public NFThresholds() {
		
	}
	/* (non-Javadoc)
	 * @see br.com.auster.om.reference.facade.ReferenceFacades#configure(org.w3c.dom.Element)
	 */
	public void configure(Element config) throws ConfigurationException {
		System.out.println("I am here.Config. Element");

	}

	/* (non-Javadoc)
	 * @see br.com.auster.om.reference.facade.ReferenceFacades#configure(java.lang.String)
	 */
	public void configure(String fileName) throws ConfigurationException {
		this.configure(fileName, false);

	}

	/* (non-Javadoc)
	 * @see br.com.auster.om.reference.facade.ReferenceFacades#configure(java.lang.String, boolean)
	 */
	public void configure(String fileName, boolean enc) throws ConfigurationException {
		try {
			InputStream stream = IOUtils.openFileForRead(fileName, enc);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
	}
	public int doBlah1() {
		return 1;
	}
	public void doBlah() {
		System.err.println("Blah");
	}
}
