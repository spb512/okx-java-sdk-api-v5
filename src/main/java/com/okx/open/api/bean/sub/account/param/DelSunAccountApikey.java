package com.okx.open.api.bean.sub.account.param;

/**
 * 
 * @author spb512
 * @date 2022年6月5日 下午5:02:59
 *
 */
public class DelSunAccountApikey {
	private String pwd;
	private String subAcct;
	private String apiKey;

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getSubAcct() {
		return subAcct;
	}

	public void setSubAcct(String subAcct) {
		this.subAcct = subAcct;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	@Override
	public String toString() {
		return "DelSunAccountApikey{" + "pwd='" + pwd + '\'' + ", subAcct='" + subAcct + '\'' + ", apiKey='" + apiKey
				+ '\'' + '}';
	}
}
