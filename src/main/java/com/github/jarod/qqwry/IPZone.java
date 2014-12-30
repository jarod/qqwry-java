package com.github.jarod.qqwry;

public class IPZone {
	private final String ip;
	private String mainInfo = "";
	private String subInfo = "";

	public IPZone(final String ip) {
		this.ip = ip;
	}

	public String getIp() {
		return ip;
	}

	public String getMainInfo() {
		return mainInfo;
	}

	public String getSubInfo() {
		return subInfo;
	}

	public void setMainInfo(final String info) {
		this.mainInfo = info;
	}

	public void setSubInfo(final String info) {
		this.subInfo = info;
	}

	@Override
	public String toString() {
		return new StringBuilder(mainInfo).append(subInfo).toString();
	}

}
