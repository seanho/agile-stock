package hk.reality.utils;

import java.text.DecimalFormat;

public class PriceFormatter {
	private static final DecimalFormat priceFormat = new DecimalFormat("#,##0.###");
	private static final DecimalFormat percentFormat = new DecimalFormat("#,##0.00");
	
	public static String forPrice(double value) {
		return priceFormat.format(value);
	}
	
	public static String forPercent(double value) {
		return percentFormat.format(value);
	}
}
