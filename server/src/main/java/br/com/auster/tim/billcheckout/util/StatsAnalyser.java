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
 * Created on 29/07/2008
 */
package br.com.auster.tim.billcheckout.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author framos
 * @version $Id$
 *
 */
public class StatsAnalyser {


//	private static final Pattern regularLogPattern = Pattern.compile("\\d\\d:\\d\\d:\\d\\d,\\d\\d\\d\\s+\\w+\\s+\\[.+?\\] .*");

	private static final Pattern rollbackPattern = Pattern.compile("\\d\\d:\\d\\d:\\d\\d,\\d\\d\\d\\s+\\w+\\s+\\[.+?\\] Rolling back processing for request \\[(.+?): .*");
	private static final Pattern processedThreadPattern = Pattern.compile("\\d\\d:\\d\\d:\\d\\d,\\d\\d\\d\\s+\\w+\\s+\\[.+?\\] Dumping stats for account (.*)");
	private static final Pattern processedRequestPattern = Pattern.compile("\\d\\d:\\d\\d:\\d\\d,\\d\\d\\d\\s+\\w+\\s+\\[.+?\\] Dumping stats for request ID .+?_(.*)");
	private static final Pattern periodicStatsDumpPattern = Pattern.compile("\\d\\d:\\d\\d:\\d\\d,\\d\\d\\d\\s+\\w+\\s+\\[.+?\\] Dumping stats:");
	private static final Pattern statsDumpPattern = Pattern.compile(".*\t(.*)\t\\s*(.*)\t(.*) x (.*) = (.*) ms");

	private static final String PERIOD_STATS_DUMP_FLAG = "Periodic";


	public static void main(String[] args) {
		try {
			if (args.length < 2) {
				System.out.println("Usage: StatsAnalyser <log-filename> <result-filename>");
				System.exit(1);
			}
			new StatsAnalyser().analyseStats(args[0], args[1]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void analyseStats(String _logfile, String _resultfile) throws IOException {

		File logFile = new File(_logfile);
		if ((!logFile.exists()) || (!logFile.isFile())) {
			System.out.println("ERROR: Cannot find file " + _logfile);
			return;
		}
		BufferedReader reader = new BufferedReader(new FileReader(logFile));
		PrintWriter writer = new PrintWriter(new FileWriter(new File(_resultfile)));
//		Map<String, Double> itemSums = new TreeMap<String, Double>();
		Map<String, SummedUpItem> itemSums = new TreeMap<String, SummedUpItem>();
//		SummedUpItem summedUpRootItem = null;
		Map<String, SummedUpItem> summedUpRootItems = new TreeMap<String, SummedUpItem>();
		try {
			String line;
			int lineNumber = 0;
			String requestId = null;
			Collection<StatsItem> rootItems = new ArrayList<StatsItem>();
//			StatsItem rootItem = null;
			StatsItem currentItem = null;
			Set<String> rolledBackRequests = new HashSet<String>();
			while ((line = reader.readLine()) != null) {
				lineNumber++;
				line = line.trim();
				if (requestId == null) {
					Matcher processedRequest = processedRequestPattern.matcher(line);
					if (processedRequest.find()) {
						requestId = processedRequest.group(1);
						if (rolledBackRequests.contains(requestId)) {
							requestId = null; // ignore rolled back requests to avoid NullPointerException
						}
					} else {
						Matcher processedThread = processedThreadPattern.matcher(line);
						if (processedThread.find()) {
							requestId = processedThread.group(1);
						} else if (periodicStatsDumpPattern.matcher(line).matches()) {
							requestId = PERIOD_STATS_DUMP_FLAG;
						} else {
							Matcher rolledBackRequest = rollbackPattern.matcher(line);
							if (rolledBackRequest.find()) {
								String request = rolledBackRequest.group(1);
								System.out.println(request + " rolled back!");
								rolledBackRequests.add(request);
							}
						}
					}
				} else {
					if (line.length() == 0) { // there must be an empty line after every dump!
						for (StatsItem rootItem : rootItems) {
							writer.print(dump(requestId, rootItem));
	//						double sum = sumUp(itemSums, rootItem);
	//						System.out.printf("%s took %.3f seconds\n", requestId, sum);
							sumUp(itemSums, rootItem);
							String description = rootItem.getDescription();
							SummedUpItem summedUpRootItem = summedUpRootItems.get(description);
							if (summedUpRootItem == null) {
								summedUpRootItem = new SummedUpItem(rootItem.getLevel(), description);
								summedUpRootItems.put(description, summedUpRootItem);
							}
							sumUp(summedUpRootItem, rootItem);
						}
						System.out.printf("%s stats processed\n", requestId);
						requestId = null;
						currentItem = null;
//						rootItem = null;
						rootItems.clear();
					} else {
						Matcher statsDump = statsDumpPattern.matcher(line);
						if (statsDump.find()) {
							int level = Integer.parseInt(statsDump.group(1));
							String description = statsDump.group(2);
							int amount = Integer.parseInt(statsDump.group(3));
							double average = Double.parseDouble(statsDump.group(4));
							double total = Double.parseDouble(statsDump.group(5));
							StatsItem item = new StatsItem(level, description, amount, average, total);
							if (level == 1) {//currentItem == null) {
								rootItems.add(currentItem = item);
							} else {
								int levelDifference = currentItem.getLevel() - level;
								StatsItem parent = currentItem;
								if (levelDifference >= 0) {
									for (int i = 0; i <= levelDifference; i++) {
										parent = parent.getParent();
									}
								}
								parent.addItem(item);
								currentItem = item;
							}
	//						System.out.println(level + ", " + item + ", " + amount + " x " + average + " = " + total);
						} else {
							System.err.println("Ignoring line " + lineNumber + ": " + line);
						}
					}
				}
			}
		} finally {
			writer.close();
		}
//		for (Entry<String, Double> itemSum : itemSums.entrySet()) {
//			System.out.printf("%s\t%.3f\n", itemSum.getKey(), itemSum.getValue() / 1000.0);
//		}
		for (StatsItem itemSum : itemSums.values()) {
			System.out.printf("%s\t%d\t%d\t%.3f\t%.3f\n",
					itemSum.getDescription(), itemSum.getLevel(), itemSum.getAmount(),
					itemSum.getAverage() / 1000.0, itemSum.getTotal() / 1000.0);
		}

		writer = new PrintWriter(new FileWriter(new File(_resultfile+".totals")));
		try {
			for (SummedUpItem summedUpRootItem : summedUpRootItems.values()) {
				System.out.println(dumpTotal(summedUpRootItem, summedUpRootItem.getTotal()));
				writer.println(advancedDump(summedUpRootItem));
			}
		} finally {
			writer.close();
		}
	}

	private String simpleDump(StatsItem item) {
		StringBuilder buffer = new StringBuilder();
		buffer.append(item.getLevel());
		buffer.append('\t');
		buffer.append(item.getDescription());
		buffer.append('\t');
		buffer.append(item.getTotal());
		buffer.append('\n');
		for (StatsItem child : item.getChildren()) {
			buffer.append(simpleDump(child));
		}
		return buffer.toString();
	}

	private String advancedDump(StatsItem item) {
		return advancedDump(item, 1);
	}

	private String advancedDump(StatsItem item, int level) {
		StringBuilder buffer = new StringBuilder();
		buffer.append(level);
		buffer.append('\t');
		buffer.append(item.getDescription());
		buffer.append('\t');
		buffer.append(item.getTotal());
		buffer.append('\n');
		Map<String, Map<String, StatsItem>> prefixMap = mapCommonPrefixes(item.getChildren());
		for (Entry<String, Map<String, StatsItem>> node : prefixMap.entrySet()) {
			String prefix = node.getKey();
			Map<String, StatsItem> prefixedItems = node.getValue();
			if (prefixedItems.size() > 1) {
				int amount = 0;
				double total = 0.0;
				for (StatsItem child : prefixedItems.values()) {
					amount += child.getAmount();
					total += child.getTotal();
				}
				buffer.append(advancedDump(new StatsItem(level + 1, prefix, amount, total / amount, total), level + 1));
				for (StatsItem child : prefixedItems.values()) {
					buffer.append(advancedDump(child, level + 2));
				}
			} else {
				for (StatsItem child : prefixedItems.values()) {
					buffer.append(advancedDump(child, level + 1));
				}
			}
		}
		return buffer.toString();
	}

	private Map<String, Map<String, StatsItem>> mapCommonPrefixes(Collection<StatsItem> items) {
		Map<String, Map<String, StatsItem>> map = new TreeMap<String, Map<String, StatsItem>>();
		for (StatsItem item : items) {
			String description = item.getDescription();
			int dotPosition = description.lastIndexOf(".");
			if (dotPosition >= 0) {
				String prefix = description.substring(0, dotPosition);
				Map<String, StatsItem> prefixedItems = map.get(prefix);
				if (prefixedItems == null) {
					prefixedItems = new TreeMap<String, StatsItem>();
					map.put(prefix, prefixedItems);
				}
				prefixedItems.put(description, item);
			} else {
				Map<String, StatsItem> prefixedItems = new TreeMap<String, StatsItem>();
				prefixedItems.put(description, item);
				map.put(description, prefixedItems);
			}
		}
		return map;
	}

	private String findPrefix(String name1, String name2) {
		int length = Math.min(name1.length(), name2.length());
		int i;
		for (i = 0; i < length; i++) {
			if (name1.charAt(i) != name2.charAt(i)) {
				break;
			}
		}
		return name1.substring(0, i);
	}

	private void sumUp(SummedUpItem summedUpItem, StatsItem item) {
		if (summedUpItem.getDescription().equals(item.getDescription())) {
			summedUpItem.sumUp(item);
			for (StatsItem child : item.getChildren()) {
				SummedUpItem summedUpChild = (SummedUpItem) summedUpItem.getChild(child.getDescription());
				if (summedUpChild == null) {
					summedUpChild = new SummedUpItem(child.getLevel(), child.getDescription());
					summedUpItem.addItem(summedUpChild);
				}
				sumUp(summedUpChild, child);
			}
		} else {
			System.err.println("Oops: descriptions don't match: "
					+ summedUpItem.getDescription() + " and "
					+ item.getDescription());
		}
	}

	/*
	private double sumUp(Map<String, Double> itemSums, StatsItem item) {
		String itemKey = item.getLevel() + "_" + item.getDescription();
		Double currentSum = itemSums.get(itemKey);
		if (currentSum == null) {
			currentSum = 0.0;
		}
		double partialSum = item.getSubTotal() + currentSum;
		itemSums.put(itemKey, partialSum);
		for (StatsItem child : item.getChildren()) {
			partialSum += sumUp(itemSums, child);
		}
		return partialSum;
	}
*/
	private void sumUp(Map<String, SummedUpItem> summedUpItems, StatsItem item) {
		String itemKey = item.getLevel() + "_" + item.getDescription();
		SummedUpItem summedUpItem = summedUpItems.get(itemKey);
		if (summedUpItem == null) {
			summedUpItem = new SummedUpItem(item);
			summedUpItems.put(itemKey, summedUpItem);
		} else {
			summedUpItem.sumUp(item);
		}
		for (StatsItem child : item.getChildren()) {
			sumUp(summedUpItems, child);
		}
	}

	private String dumpTotal(StatsItem item, double totalTime) {
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < item.getLevel(); i++) {
			buffer.append("    ");
		}
		double subTotal = item.getSubTotal();
		double total = item.getTotal();

		buffer.append(String.format("%s\t%d\t%d\t%10.3f\t%10.3f",
						item.getDescription(),
						item.getAmount(),
						item.getLevel(),
						item.getAverage() / 1000.0,
						subTotal / 1000.0
						));
		buffer.append(System.getProperty("line.separator"));

//		buffer.append(String.format("%10.3f (%.1f%%)\t%10.3f (%.1f%%)    %d  %s\n",
//				subTotal / 1000.0,
//				subTotal / totalTime * 100.0,
//				total / 1000.0,
//				total / totalTime * 100.0,
//				item.getLevel(),
//				item.getDescription()));

		for (StatsItem child : item.getChildren()) {
			buffer.append(dumpTotal(child, totalTime));
		}
		return buffer.toString();
	}

	private String dump(String requestId, StatsItem item) {
		StringBuilder buffer = new StringBuilder();
		buffer.append(requestId);
//		buffer.append(':');
		buffer.append('\t');
//		for (int i = 0; i < item.getLevel(); i++) {
//			buffer.append("    ");
//		}
		buffer.append(String.format("%10.2f", item.getTotal()));
		buffer.append('\t');
		buffer.append(String.format("%10.2f", item.getSubTotal()));
		buffer.append('\t');
		buffer.append(item.getLevel());
		buffer.append('\t');
		buffer.append(item.getDescription());
		buffer.append(System.getProperty("line.separator"));
		for (StatsItem child : item.getChildren()) {
			buffer.append(dump(requestId, child));
		}
		return buffer.toString();
	}
}
