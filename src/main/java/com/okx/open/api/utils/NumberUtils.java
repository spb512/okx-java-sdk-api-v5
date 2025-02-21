package com.okx.open.api.utils;

import org.apache.commons.lang3.StringUtils;

import com.okx.open.api.constant.ApiConstants;

import java.text.NumberFormat;

/**
 * 
 * @author spb512
 * @date 2022年6月5日 下午5:02:59
 *
 */
public class NumberUtils {

	/**
	 * scientific notation: double to string <br/>
	 * eg: <br/>
	 * -4.62E-6 -> -0.00000462 <br/>
	 * 8.9E9 -> 8900000000 <br/>
	 * 5.50 -> 5.5 <br/>
	 */
	public static String doubleToString(Double arg) {
		if (arg == null) {
			return ApiConstants.DOUBLE_ZERO_STRING;
		}
		String argStr = arg.toString();
		int scale;
		if (argStr.contains(ApiConstants.CAP_E) || argStr.contains(ApiConstants.LOW_E)) {
			String[] argArray = argStr.toUpperCase().split(ApiConstants.CAP_E);
			int scale1 = Integer.parseInt(argArray[1]);
			scale = scale1 < 0 ? Math.abs(scale1) : scale1;
			String scale2 = argArray[0];
			if (scale2.contains(ApiConstants.DOT1)) {
				String[] argDecimalsArray = scale2.split(ApiConstants.DOT2);
				String decimalsAfter = argDecimalsArray[1];
				int decimalsAfterI = Integer.parseInt(decimalsAfter);
				if (decimalsAfterI > 0) {
					scale += decimalsAfter.length();
				}
			}
		} else {
			scale = ApiConstants.DEFAULT_SCALE;
		}
		NumberFormat format = getNumberFormat(scale);
		String result = format.format(arg);
		if (StringUtils.isNotEmpty(result) && result.contains(ApiConstants.DOT1)) {
			result = result.replaceAll(ApiConstants.DOUBLE_END1, ApiConstants.EMPTY);
			result = result.replaceAll(ApiConstants.DOUBLE_END2, ApiConstants.EMPTY);
		}
		return result;
	}

	private static NumberFormat getNumberFormat(int scale) {
		NumberFormat format = NumberFormat.getInstance();
		// No comma
		format.setGroupingUsed(false);
		// Set the number of decimal places
		format.setMinimumFractionDigits(scale);
		return format;
	}

	public static int toInteger(String string) {
		return toInteger(string, 0);
	}

	public static int toInteger(String string, int defaultValue) {
		if (StringUtils.isNotEmpty(string)) {
			string = string.trim();
			if (StringUtils.isNumeric(string)) {
				return Integer.parseInt(string);
			}
		}
		return defaultValue;
	}

	public static long toLong(String string) {
		return toLong(string, 0L);
	}

	public static long toLong(String string, long defaultValue) {
		if (StringUtils.isNotEmpty(string)) {
			string = string.trim();
			if (StringUtils.isNumeric(string)) {
				return Long.parseLong(string);
			}
		}
		return defaultValue;
	}
}
