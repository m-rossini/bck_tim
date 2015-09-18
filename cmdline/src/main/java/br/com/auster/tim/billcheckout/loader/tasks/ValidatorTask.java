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
package br.com.auster.tim.billcheckout.loader.tasks;

import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

import br.com.auster.tim.billcheckout.loader.DataValidator;
import br.com.auster.tim.billcheckout.loader.RowRead;

/**
 * TODO What this class is responsible for
 *
 * @author Frederico Ramos
 * @version $Id$
 * @since JDK1.4
 */
public class ValidatorTask extends RunnableTasks {

	
	
	private static final Logger logger = Logger.getLogger(ValidatorTask.class);
	
	
	private DataValidator validator;
	private BlockingQueue<RowRead> inQueue;
	private BlockingQueue<String[]> outQueue;
	
	
	
	public ValidatorTask(DataValidator _validator, BlockingQueue<RowRead> _inQueue, BlockingQueue<String[]> _outQueue) {
		this.validator = _validator;
		this.inQueue = _inQueue;
		this.outQueue = _outQueue;
	}
	
	public Integer call() throws Exception {
		RowRead columns = null;
		do {
			columns = this.inQueue.poll(POOL_WAIT, POOL_UNIT);
			if (columns != null) {
				this.validator.validateLine(columns, outQueue);
			}
		} while ( (columns != null) || (!this.isDone()) );
		logger.debug("Found " + this.validator.getErrorCount() + " errors out of " + this.validator.getRowCount() + " rows.");
		return EXEC_OK;
	}
}

