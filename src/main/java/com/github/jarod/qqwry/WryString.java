package com.github.jarod.qqwry;

public class WryString {
	private final String string;
	private final int length;

	public WryString(final String string, final int length) {
		this.string = string;
		this.length = length;
	}

	public int getLength() {
		return length;
	}

	public String getString() {
		return string;
	}
}
