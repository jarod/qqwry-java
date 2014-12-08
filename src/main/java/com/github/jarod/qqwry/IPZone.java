package com.github.jarod.qqwry;

public class IPZone {
	private final String ip;
	private String country;
	private String city;

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
		builder.append("IPZone [ip=").append(ip).append(", country=").append(country).append(", city=").append(city)
		.append("]");
		return builder.toString();
	}

}
