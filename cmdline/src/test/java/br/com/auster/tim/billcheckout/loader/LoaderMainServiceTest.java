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
 * Created on 09/03/2010
 */
package br.com.auster.tim.billcheckout.loader;

import java.util.Locale;

import junit.framework.TestCase;

import org.apache.log4j.xml.DOMConfigurator;

import br.com.auster.common.xml.DOMUtils;

/**
 * TODO What this class is responsible for
 *
 * @author Frederico Ramos
 * @version $Id$
 * @since JDK1.4
 */
public class LoaderMainServiceTest extends TestCase {

	
	@Override
	protected void setUp() throws Exception {
		DOMConfigurator.configure(DOMUtils.openDocument("src/test/resources/loader/examples/log4j.xml", false));
	}

	public void testInsertServiceRates() {

		// since file is in pt_BR, we should for this locale
		Locale.setDefault(new Locale("pt", "BR"));
		
		try {
			Class.forName("org.apache.commons.dbcp.PoolingDriver");
			Class.forName("oracle.jdbc.driver.OracleDriver");		
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		System.getProperties().setProperty(LoaderMain.SYSPROPERTY_DATABASE, "src/test/resources/loader/db/test.cfg");
		System.getProperties().setProperty(LoaderMain.SYSPROPERTY_LAYOUT, "src/test/resources/loader/serviceRates.properties");
		System.getProperties().setProperty(LoaderMain.SYSPROPERTY_DRYRUN, "false");
		
		String inputFile = "src/test/resources/loader/examples/inputService.txt";
		LoaderMain.main(new String[] { inputFile });
	}
	
	
	public void testInsertServiceRatesWithFK() {

		// since file is in pt_BR, we should for this locale
		Locale.setDefault(new Locale("pt", "BR"));
		
		try {
			Class.forName("org.apache.commons.dbcp.PoolingDriver");
			Class.forName("oracle.jdbc.driver.OracleDriver");		
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		System.getProperties().setProperty(LoaderMain.SYSPROPERTY_DATABASE, "src/test/resources/loader/db/test.cfg");
		System.getProperties().setProperty(LoaderMain.SYSPROPERTY_LAYOUT, "src/test/resources/loader/serviceRatesFK.properties");
		System.getProperties().setProperty(LoaderMain.SYSPROPERTY_DRYRUN, "false");
		
		String inputFile = "src/test/resources/loader/examples/inputService.txt";
		LoaderMain.main(new String[] { inputFile });
	}	
	
}
