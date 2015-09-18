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

import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

import br.com.auster.tim.billcheckout.loader.DataSaver;

/**
 * TODO What this class is responsible for
 *
 * @author Frederico Ramos
 * @version $Id$
 * @since JDK1.4
 */
public class SaverTask extends RunnableTasks {

	
	private static final Logger logger = Logger.getLogger(SaverTask.class);
	
	
	private DataSaver saver;
	private BlockingQueue<String[]> inQueue;
	
	
	
	public SaverTask(DataSaver _saver, BlockingQueue<String[]> _inQueue) {
		this.saver = _saver;
		this.inQueue = _inQueue;
	}
	
	public Integer call() throws Exception {
		String[] columns = null;
		int rows=0, errors=0;
		do {
			columns = this.inQueue.poll(POOL_WAIT, POOL_UNIT);
			if (columns != null) {
				rows++;
				try {
					this.saver.saveLine(columns);
				} catch (SQLException sqle) {
					logger.error(" >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Error saving line: ", sqle);
					// TODO add column to exception queue
					errors++;
				}
			}
		} while ((columns != null) || (!this.isDone()));
		// last commit
		this.saver.handleCommit();
		logger.info("Could not save " + errors + " rows out of " + rows);		
		return EXEC_OK;
	}
}