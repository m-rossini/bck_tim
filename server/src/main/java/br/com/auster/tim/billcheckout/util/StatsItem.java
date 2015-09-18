package br.com.auster.tim.billcheckout.util;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class StatsItem {

	private int level;

	private String description;

	private int amount;

	private double average;

	private double total;

	private StatsItem parent;

//	private Collection<StatsItem> children = new ArrayList<StatsItem>();
	private Map<String, StatsItem> children = new TreeMap<String, StatsItem>();

	public StatsItem(int level, String description, int amount, double average, double total) {
		this.level = level;
		this.description = description;
		this.amount = amount;
		this.average = average;
		this.total = total;
	}

	public void addItem(StatsItem subItem) {
//		this.children.add(subItem);
		this.children.put(subItem.getDescription(), subItem);
		subItem.setParent(this);
	}

	public double getSubTotal() {
		double subTotal = getTotal();
		for (StatsItem child : getChildren()) {
			subTotal -= child.getTotal();
		}
		return subTotal;
	}

	public int getLevel() {
		return this.level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getDescription() {
		return this.description; // + (getChildren().size() > 0 ? " *" : "");
	}

	public void setDescription(String item) {
		this.description = item;
	}

	public int getAmount() {
		return this.amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public double getAverage() {
		return this.average;
	}

	public void setAverage(double average) {
		this.average = average;
	}

	public double getTotal() {
		return this.total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public StatsItem getParent() {
		return this.parent;
	}

	public void setParent(StatsItem parent) {
		this.parent = parent;
	}

	public StatsItem getChild(String description) {
		return this.children.get(description);
	}

	public Collection<StatsItem> getChildren() {
//		return this.children;
		return this.children.values();
	}
}
