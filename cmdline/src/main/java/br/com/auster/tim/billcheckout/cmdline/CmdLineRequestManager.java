/*
 * Copyright (c) 2004-2007 Auster Solutions. All Rights Reserved.
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
 * Created on Jun 8, 2005
 */
package br.com.auster.tim.billcheckout.cmdline;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.jms.JMSException;
import javax.naming.NamingException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;

import br.com.auster.common.log.LogFactory;
import br.com.auster.common.util.I18n;

/**
 * @author etirelli
 * @version $Id: CmdLineRequestManager.java 278 2005-11-14 12:06:11Z framos $
 */
public class CmdLineRequestManager {
	
	
	private static Logger log = null;
	private static I18n i18n = I18n.getInstance(CmdLineRequestManager.class);
	
	
	/**
	 * Public constructor
	 */
	public CmdLineRequestManager() {
	}
	
	/**
	 * Create CLI Option objects for command line parsing
	 * @return Options list of Option Objects
	 */
	protected static Options createOptions() {
		OptionBuilder.withArgName("filemask");
		OptionBuilder.hasArg();
		OptionBuilder.withDescription("Especifica os arquivos a serem processados. Aceita os curingas * e ?.");
		Option mask = OptionBuilder.create("f");
		
		OptionBuilder.withArgName("userId");
		OptionBuilder.hasArg();
		OptionBuilder.withDescription("Usuario dono da requisicao que sera criada.");
		Option user = OptionBuilder.create("u");
		
		OptionBuilder.withArgName("e-mail");
		OptionBuilder.hasArg();
		OptionBuilder.withDescription("Lista separada por virgula, com mails para os quais enviar a notificacao de fim de processamento.");
		Option mail = OptionBuilder.create("m");
		
		OptionBuilder.withArgName("config");
		OptionBuilder.hasArg();
		OptionBuilder.withDescription("Arquivo de configuração contendo informações de acesso ao servidor de billcheckout");
		Option config = OptionBuilder.create("c");

		OptionBuilder.withArgName("layout");
		OptionBuilder.hasArg();
		OptionBuilder.withDescription("Arquivo de descrição do formato BGH");
		Option layout = OptionBuilder.create("l");

		OptionBuilder.withArgName("step");
		OptionBuilder.hasArg();
		OptionBuilder.withDescription("Step de processamento do ciclo");
		Option step = OptionBuilder.create("s");
		
		OptionBuilder.withDescription("Mostra o help de uso.");
		Option help = OptionBuilder.create("h");
		
		Options options = new Options();
		options.addOption(mask);
		options.addOption(user);
		options.addOption(mail);
		options.addOption(config);
		options.addOption(layout);
		options.addOption(step);
		options.addOption(help);
		return options;
	}
	
	/**
	 * Prints command line help
	 * @param options list of available options
	 */
	protected static void printHelp(Options options) {
		// automatically generate the help statement
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp( CmdLineRequestManager.class.getName(), options );    
	}

	/**
	 * Outputs command line options to the log file
	 * 
	 * @param line
	 */
	public static void logCommandLine(CommandLine line) {
		log.info(i18n.getString("cmdline.reqmanager.args"));
		Option[] options = line.getOptions();
		for(int i=0; i<options.length; i++) {
			log.info("PARAMETER: ["+options[i].getArgName()+"] = ["+options[i].getValue()+"]");
		}
	}
	
	/**
	 * Command line entry point
	 * @param args
	 */
	public static void main(String[] args) {
		
		int exitcode = 0;
		Options options = CmdLineRequestManager.createOptions();
		
		// create the parser
		CommandLineParser parser = new PosixParser();
		try {
			// parse the command line arguments
			CommandLine line = parser.parse( options, args );
			if((line.hasOption("h")) || 
			   (!(line.hasOption("f") && line.hasOption("u") && line.hasOption("c") && line.hasOption("l") && line.hasOption("s")))) {
				CmdLineRequestManager.printHelp(options);
			} else {
				LogFactory.configureLogSystem((String) line.getOptionValue("c"));
				log = LogFactory.getLogger(CmdLineRequestManager.class);
				
				CmdLineRequestManager.logCommandLine(line);
				CmdlineRequestBuilder builder = new CmdlineRequestBuilder();
				builder.createRequest(line);
			}
		} catch( ParseException e ) {
			// oops, something went wrong
			System.out.println(i18n.getString("cmdline.reqmanager.parseError"));
			System.out.println(e.getMessage());
			CmdLineRequestManager.printHelp(options);
			exitcode = 1;
		} catch (JMSException e) {
			log.error( i18n.getString("cmdline.reqmanager.persistenceError"), e );
			exitcode = 1;
		} catch (NamingException e) {
			log.error( i18n.getString("cmdline.reqmanager.userManagerError"), e );
			exitcode = 1;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			exitcode = 1;
		} catch (IOException e) {
			e.printStackTrace();
			exitcode = 1;
		}
		System.exit(exitcode);
	}
}
