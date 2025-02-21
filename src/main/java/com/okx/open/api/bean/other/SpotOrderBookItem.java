package com.okx.open.api.bean.other;

/**
 * 
 * @author spb512
 * @date 2022年6月5日 下午5:02:42
 *
 */
public class SpotOrderBookItem implements OrderBookItem<String> {
	private final String price;
	private final String size;
	private final String numLiquid;
	private final String numOrder;

	public SpotOrderBookItem(String price, String size, String numLiquid, String numOrder) {
		this.price = price;
		this.size = size;
		this.numLiquid = numLiquid;
		this.numOrder = numOrder;
	}

	@Override
	public String toString() {
		return "[\"" + price.toString() + "\",\"" + size + "\",\"" + numLiquid + "\",\"" + numOrder + "\"]";
	}

	@Override
	public String getPrice() {
		return price;
	}

	@Override
	public String getSize() {
		return size;
	}

	public String getNumOrder() {
		return numOrder;
	}

	public String getNumLiquid() {
		return numLiquid;
	}
}
