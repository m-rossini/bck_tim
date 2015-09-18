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
 * Created on 30/03/2010
 */
package br.com.auster.tim.billcheckout.loader.validator;

import org.apache.log4j.Logger;

import br.com.auster.common.util.I18n;

/**
 * TODO What this class is responsible for
 *
 * @author Frederico Ramos
 * @version $Id$
 * @since JDK1.4
 */
public class DiscardedRowsLogger {

	
	
	private static final Logger logger = Logger.getLogger(DiscardedRowsLogger.class);
	private static final I18n i18n = I18n.getInstance(DiscardedRowsLogger.class);
	
	
	
	public static void discardRow(String _message, Object[] _params) {
		logger.info(i18n.getString(_message, _params));
	}
}
