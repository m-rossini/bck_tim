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

import java.sql.Connection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import br.com.auster.common.io.NIOUtils;
import br.com.auster.common.sql.SQLConnectionManager;
import br.com.auster.common.xml.DOMUtils;
import br.com.auster.tim.billcheckout.loader.tasks.ReaderTask;
import br.com.auster.tim.billcheckout.loader.tasks.SaverTask;
import br.com.auster.tim.billcheckout.loader.tasks.ValidatorTask;

/**
 * TODO What this class is responsible for
 *
 * @author Frederico Ramos
 * @version $Id$
 * @since JDK1.4
 */
public class LoaderMain {

	
	public static final String SYSPROPERTY_LAYOUT = "loader.layout";
	public static final String SYSPROPERTY_DATABASE = "loader.db";
	public static final String SYSPROPERTY_DRYRUN = "loader.dryrun";
	public static final String SYSPROPERTY_COMMIT_LIMIT = "loader.db.commit";

	public static boolean DRYRUN = Boolean.parseBoolean(System.getProperty(SYSPROPERTY_DRYRUN));	
	
	private static final Logger logger = Logger.getLogger(LoaderMain.class);
	
	
	
	/**
	 * TODO what this method is responsible for
	 * <p>
	 * Example:
	 * <pre>
	 *    Create a use example.
	 * </pre>
	 * </p>
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		if ((args.length < 1) || (args[0] == null)) {
			logger.fatal("Must specify input file");
			System.exit(-1);
		}
		String inputFile = args[0];
		
		String databasePool = System.getProperty(SYSPROPERTY_DATABASE);
		String layoutFile = System.getProperty(SYSPROPERTY_LAYOUT);
		if ((databasePool == null) || (layoutFile == null)) {
			logger.fatal("Cannot start without database or layout configuration");
			System.exit(1);
		}
		
		// setting up environment
		LayoutReader lr = null;
		try {
			// setting up layout
			lr = new LayoutReader(System.getProperty(SYSPROPERTY_LAYOUT));
			// setting up database 
			if (!DRYRUN) {
				Element dbConfiguration = DOMUtils.openDocument(System.getProperty(SYSPROPERTY_DATABASE), false);
				SQLConnectionManager.init(dbConfiguration);
			}
		} catch (Exception e) {
			logger.fatal("Exception while handing database init", e);
			System.exit(3);
		}

		int commitLimit = 1;
		try {
			commitLimit = Integer.parseInt(System.getProperty(SYSPROPERTY_COMMIT_LIMIT)); 
		} catch (NumberFormatException nfe) {
			logger.warn("Cannot work with commit limit of " + System.getProperty(SYSPROPERTY_COMMIT_LIMIT) + ". Defaulting to 1");
		}
		
		try {
			// all components up!
			SQLConnectionManager sqlManager = null;
			Connection conn = null;
			String sql = null;
			if (!DRYRUN) {
				sqlManager = SQLConnectionManager.getInstance(lr.getProperty(LayoutReader.CFG_LAYOUT_DB_POOL));
				conn = sqlManager.getConnection();
				sql = sqlManager.getStatement(lr.getProperty(LayoutReader.CFG_LAYOUT_INSERT_QUERY)).getStatementText();
			}
			
			logger.info("Dry-run enabled? " + LoaderMain.DRYRUN);
			
			DataFileReader reader = new DataFileReader(NIOUtils.openFileForRead(inputFile));
			DataValidator validator = new DataValidator(conn, sqlManager, lr, false);
			DataSaver saver = new DataSaver(lr, conn, sql, commitLimit);

			// running pre-statements
			if (lr.getProperty(LayoutReader.CFG_LAYOUT_PRE_INSERT_QUERY) != null) {
				String preSql = sqlManager.getStatement(lr.getProperty(LayoutReader.CFG_LAYOUT_PRE_INSERT_QUERY)).getStatementText();
				saver.runStatements( preSql );
			}			
			
			BlockingQueue<RowRead> validatorQueue = new LinkedBlockingQueue<RowRead>(1000);
			BlockingQueue<String[]> saverQueue = new LinkedBlockingQueue<String[]>(1000);
			
			// building threads
			ReaderTask readerTask = new ReaderTask(reader, validatorQueue);
			ValidatorTask validatorTask = new ValidatorTask(validator, validatorQueue, saverQueue);
			SaverTask saverTask = new SaverTask(saver, saverQueue);
			
			// starting threads
			Future<Integer> readerFuture = Executors.newSingleThreadExecutor().submit( readerTask );
			Future<Integer> validatorFuture = Executors.newSingleThreadExecutor().submit( validatorTask );
			Future<Integer> saverFuture = Executors.newSingleThreadExecutor().submit( saverTask );
			
			// waiting for termination
			logger.debug("Reader terminated ok? " + (readerFuture.get()==0));
			validatorTask.markDone();
			logger.debug("Validator terminated ok? " + (validatorFuture.get()==0));
			saverTask.markDone();
			logger.debug("Saver terminated ok? " + (saverFuture.get()==0));
			
			// running pre-statements
			if (lr.getProperty(LayoutReader.CFG_LAYOUT_POS_INSERT_QUERY) != null) {
				String posSql = sqlManager.getStatement(lr.getProperty(LayoutReader.CFG_LAYOUT_POS_INSERT_QUERY)).getStatementText();
				saver.runStatements( posSql );
			}			
			
		} catch (Exception e) {
			logger.fatal("Exception while handing loader", e);
			System.exit(4);
		}
		System.exit(0);
	}

}
