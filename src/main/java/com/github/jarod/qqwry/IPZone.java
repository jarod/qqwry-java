package com.github.jarod.qqwry;

public class IPZone {
	private final String ip;
	private String country = "";
	private String city = "";

	public IPZone(final String ip) {
		this.ip = ip;
	}

	public String getCity() {
		return city;
	}

	public String getCountry() {
		return country;
	}

	public String getIp() {
		return ip;
	}

	public void setCity(final String city) {
		this.city = city;
	}

	public void setCountry(final String country) {
		this.country = country;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append(country).append(" ").append(city);
		return builder.toString();
	}

}
