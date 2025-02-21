package com.okx.open.api.enums;

/**
 * @author wenchao.jia@okcoin.com
 * @date 2019/5/7 11:29 AM
 */
public enum FuturesDirectionEnum {
	/**
	 * long
	 */
	LONG("long"),
	/**
	 * short
	 */
	SHORT("short");

	private String direction;

	FuturesDirectionEnum(String direction) {
		this.direction = direction;
	}

	public String getDirection() {
		return direction;
	}
}
