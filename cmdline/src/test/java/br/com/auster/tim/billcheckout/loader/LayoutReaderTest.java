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
 * Created on 05/03/2010
 */
package br.com.auster.tim.billcheckout.loader;

import java.io.IOException;

import junit.framework.TestCase;

/**
 * TODO What this class is responsible for
 *
 * @author Frederico Ramos
 * @version $Id$
 * @since JDK1.4
 */
public class LayoutReaderTest extends TestCase {


	
	public void testCannotFindPropertyFile() {
		try {
			new LayoutReader("src/test/resources/loader/nofile.properties");
			fail();
		} catch (LayoutConfigurationException lce) {
			assertNotNull(lce);
			assertTrue(lce.getCause() instanceof IOException);
		}
	}
	
	public void testAllConfiguredOk() {
		try {
			new LayoutReader("src/test/resources/loader/allOk.properties");
		} catch (Exception e) {
			fail();
		}
	}
	
	public void testMissingInsertSQL() {
		try {
			new LayoutReader("src/test/resources/loader/missingInsertSQL.properties");
			fail();
		} catch (LayoutConfigurationException lce) {
			assertNotNull(lce);
			assertTrue(lce.getMessage().indexOf(LayoutReader.CFG_LAYOUT_INSERT_QUERY) > 0);
		}			
	}

	public void testMissingTotalColumns() {
		try {
			new LayoutReader("src/test/resources/loader/missingTotalColumns.properties");
			fail();
		} catch (LayoutConfigurationException lce) {
			assertNotNull(lce);
			assertTrue(lce.getMessage().indexOf(LayoutReader.CFG_LAYOUT_TOTAL_COLUMNS) > 0);
		}
	}

	public void testMissingUsedColumns() {
		try {
			new LayoutReader("src/test/resources/loader/missingUsedColumns.properties");
			fail();
		} catch (LayoutConfigurationException lce) {
			assertNotNull(lce);
			assertTrue(lce.getMessage().indexOf(LayoutReader.CFG_LAYOUT_USED_COLUMNS) > 0);
		}
	}

	public void testNoColumnsConfigured() {
		try {
			new LayoutReader("src/test/resources/loader/noColumnsConfigured.properties");
			fail();
		} catch (LayoutConfigurationException lce) {
			assertNotNull(lce);
			assertTrue(lce.getMessage().indexOf(".position") > 0);
		}
	}
	
	public void testUsedColumnsDifferFromConfiguredColumns() {
		try {
			new LayoutReader("src/test/resources/loader/differentUsedAndConfiguredColumns.properties");
		} catch (Exception e) {
			fail();
		}
	}

	public void testMissingSomeColumnPosition() {
		try {
			new LayoutReader("src/test/resources/loader/missingSomePosition.properties");
			fail();
		} catch (LayoutConfigurationException lce) {
			assertNotNull(lce);
			assertTrue(lce.getMessage().indexOf(".position") > 0);
		}
	}

	public void testMissingSomeColumnType() {
		try {
			new LayoutReader("src/test/resources/loader/missingSomeType.properties");
			fail();
		} catch (LayoutConfigurationException lce) {
			assertNotNull(lce);
			assertTrue(lce.getMessage().indexOf(".type") > 0);
		}
	}
	
}
