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

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

import br.com.auster.tim.billcheckout.loader.DataFileReader;
import br.com.auster.tim.billcheckout.loader.RowRead;

/**
 * TODO What this class is responsible for
 *
 * @author Frederico Ramos
 * @version $Id$
 * @since JDK1.4
 */
public class ReaderTask extends RunnableTasks {

	
	private static final Logger logger = Logger.getLogger(ReaderTask.class);
	
	
	private BlockingQueue<RowRead> outQueue;
	private DataFileReader reader;
	
	
	
	public ReaderTask(DataFileReader _reader, BlockingQueue<RowRead> _outQueue) {
		this.reader = _reader;
		this.outQueue = _outQueue;
	}
	
	public Integer call() throws Exception {
		try {
			this.reader.setOutputQueue(this.outQueue);
			this.reader.readFile();
		} catch (IOException ioe) {
			logger.error("Error reading file", ioe);
			return EXEC_NOK;
		} catch (InterruptedException ie) {
			logger.error("Error reading file", ie);
			return EXEC_NOK;
		}
		return EXEC_OK;
	}
}

