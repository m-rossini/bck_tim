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
 * Created on 24/03/2008
 */
package br.com.auster.tim.billcheckout.util;

import junit.framework.TestCase;

/**
 * @author framos
 * @version $Id$
 *
 */
public class XStreamLoaderTest extends TestCase {


	public void testR03_x_Info() {
		System.setProperty("cglib.disabled", "true");
		System.setProperty(XStreamLoader.ROOT_DIR_SYSPROP, "src/test/resources/bgh/r03_x/xstream");
		System.setProperty(XStreamLoader.FILENAME_SYSPROP, "credcorp.xml.gz");
		XStreamLoader loader = new XStreamLoader();
		assertNotNull(loader.loadXStreamInfo("6.316195.11"));
	}

}
