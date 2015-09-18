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
 * Created on 25/07/2008
 */
package br.com.auster.tim.billcheckout.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author framos
 * @version $Id$
 *
 */
public class RuleStatApplier {

		public static void main(String[] args) {
			try {
				if (args.length < 2) {
					System.out.println("Usage: RuleStatApplier <input-dir> <output-dir>");
				}
				new RuleStatApplier().insertStats(args[0], args[1]);
//				new RulePreProcessor().dumpLineFromFiles(182);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}


//		public void dumpLineFromFiles(int lineNumber) throws IOException {
//			File inputFolder = new File("C:\\Documents and Settings\\lmorozow\\workspace\\xVivo-BCK\\src\\main\\conf\\drools");
//			File outputFolder = new File("H:\\drools");
//			dumpLine(lineNumber, outputFolder, new AllFilesFilter());
//
//			inputFolder = new File(inputFolder, "guiding");
//			outputFolder = new File(outputFolder, "guiding");
//			dumpLine(lineNumber, outputFolder, new OneFileFilter("rating.drl"));
//		}
//
//		private void dumpLine(int searchedLineNumber, File folder, FilenameFilter filter) throws IOException {
//			for (File file : folder.listFiles(filter)) {
//				if (!file.isDirectory()) {
//					BufferedReader reader = new BufferedReader(new FileReader(file));
//					int lineNumber = 0;
//					String line;
//					while ((line = reader.readLine()) != null) {
//						lineNumber++;
//						if (Math.abs(lineNumber - searchedLineNumber) <= 1) {
//							System.out.println(file.getName() + ": " + line);
//						}
//					}
//				}
//			}
//		}

		public void insertStats(String _inputDir, String _outputDir) throws IOException {
			File inputFolder = new File(_inputDir);
			File outputFolder = new File(_outputDir);
			insertStats(inputFolder, outputFolder, new AllFilesFilter());
//			inputFolder = new File(inputFolder, "guiding");
//			outputFolder = new File(outputFolder, "guiding");
//			insertStats(inputFolder, outputFolder, new OneFileFilter("rating.drl"));
		}

		private void insertStats(File inputFolder, File outputFolder, FilenameFilter filter) throws FileNotFoundException, IOException {
			outputFolder.mkdirs();
			for (File inputFile : inputFolder.listFiles(filter)) {
				if (!inputFile.isDirectory()) {
					System.out.println("Pre-processing " + inputFile.getName() + "...");
					boolean imported = false;
					BufferedReader reader = new BufferedReader(new FileReader(inputFile));
					File outputFile = new File(outputFolder, inputFile.getName());
					PrintWriter writer = new PrintWriter(new FileWriter(outputFile));
					String ruleName = null;
					String line;
					while ((line = reader.readLine()) != null) {
						String originalLine = line;
						line = line.trim();
						if (line.startsWith("rule ")) {
							if (line.indexOf("salience") >= 0) {
								ruleName = line.substring("rule ".length(), line.indexOf("salience")).trim();
							} else if (line.indexOf("#") >= 0) {
								ruleName = line.substring("rule ".length(), line.indexOf("#")).trim();
							} else if (line.indexOf("//") >= 0) {
								ruleName = line.substring("rule ".length(), line.indexOf("//")).trim();
							} else {
								ruleName = line.substring("rule ".length());
							}
							System.out.println(ruleName);
						}
						if (!imported && line.startsWith("import")) {
							writer.println("import br.com.auster.common.stats.StatsMapping;\nimport br.com.auster.common.stats.ProcessingStats;\n");
							imported = true;
						}
						if (line.startsWith("then")) {
							writer.println("then\n\tStatsMapping stats = ProcessingStats.starting(\"RHS\", \"" + outputFile.getName() + "\", " + ruleName + ");\n\ttry {");
						} else if (line.equals("end")) {
							writer.println("\t} finally {\n\t\tstats.finished();\n\t}\nend");
						} else {
							writer.println(originalLine);
						}
					}
					writer.close();
				}
			}
		}

		public class OneFileFilter implements FilenameFilter {

			private String expectedFilename;

			public OneFileFilter(String expectedFilename) {
				this.expectedFilename = expectedFilename;
			}

			public boolean accept(File dir, String name) {
				return name.equalsIgnoreCase(this.expectedFilename);
			}
		}

		public class AllFilesFilter implements FilenameFilter {

			public boolean accept(File dir, String name) {
				return true;
			}
		}
	}

