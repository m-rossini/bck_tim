package br.com.auster.tim.billcheckout.util;

public class SummedUpItem extends StatsItem {

	public SummedUpItem(int level, String description, int amount, double total) {
		super(level, description, amount, 0.0, total);
	}

	public SummedUpItem(int level, String description) {
		this(level, description, 0, 0.0);
	}

	protected SummedUpItem(StatsItem original) {
		this(original.getLevel(), original.getDescription(), original.getAmount(), original.getTotal()); // original.getSubTotal());
	}

	public void sumUp(StatsItem other) {
		this.setAmount(this.getAmount() + other.getAmount());
		this.setTotal(this.getTotal() + other.getTotal()); // other.getSubTotal());
	}

	@Override
	public double getAverage() {
		if (getAmount() == 0) {
			return 0.0;
		} else {
			return getTotal() / getAmount();
		}
	}
}
