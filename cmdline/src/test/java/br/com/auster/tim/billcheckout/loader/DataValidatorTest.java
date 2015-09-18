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
 * Created on 08/03/2010
 */
package br.com.auster.tim.billcheckout.loader;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.xml.DOMConfigurator;

import br.com.auster.common.xml.DOMUtils;

import junit.framework.TestCase;

/**
 * TODO What this class is responsible for
 *
 * @author Frederico Ramos
 * @version $Id$
 * @since JDK1.4
 */
public class DataValidatorTest extends TestCase {

/*
	layout.insertQuery=insertQuery
	layout.totalColumns=5
	layout.usedColumns=3
	layout.column.1.position=3 
	layout.column.1.type=integer
	layout.column.2.position=1 
	layout.column.2.type=text
	layout.column.3.position=5 
	layout.column.3.type=date 
*/

	protected DataValidator validator;
	
	
	@Override
	protected void setUp() throws Exception {
		try {
			LayoutReader lr = new LayoutReader("src/test/resources/loader/allOk.properties");
			this.validator = new DataValidator(null, null, lr, false);
		} catch (Exception e) {
			e.printStackTrace();
		}

		DOMConfigurator.configure(DOMUtils.openDocument("src/test/resources/loader/examples/log4j.xml", false));
	}

	
	public void testLineIsOk() {
		RowRead row = new RowRead();
		row.columns = new String[] { "Someday", "noColumn1", "10", "noColumn2", "10/02/2010" };
		try {
			this.validator.validateLine(row, new LinkedBlockingQueue<String[]>());
			assertTrue(this.validator.getErrorCount() == 0);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testLineMissingColumn() {
		RowRead row = new RowRead();
		row.columns = new String[] { "Someday", "10", "noColumn2", "10/02/2010" };
		try {
			this.validator.validateLine(row, new LinkedBlockingQueue<String[]>());
			assertTrue(this.validator.getErrorCount() == 1);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testLineUnmappedColumn() {
		RowRead row = new RowRead();
		row.columns = new String[] { "Someday", "noColumn1", "10", "noColumn2", "10/02/2010", "noColumn3" };
		try {
			this.validator.validateLine(row, new LinkedBlockingQueue<String[]>());
			assertTrue(this.validator.getErrorCount() == 1);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testLineIncorrectFormatForDate() {
		RowRead row = new RowRead();
		row.columns = new String[] { "Someday", "noColumn1", "10", "noColumn2", "10-02-2010" };
		try {
			this.validator.validateLine(row, new LinkedBlockingQueue<String[]>());
			assertTrue(this.validator.getErrorCount() == 1);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testLineIncorrectFormatForInteger() {
		RowRead row = new RowRead();
		row.columns = new String[] { "Someday", "noColumn1", "10.2", "noColumn2", "10/02/2010" };
		try {
			this.validator.validateLine(row, new LinkedBlockingQueue<String[]>());
			assertTrue(this.validator.getErrorCount() == 1);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
}
