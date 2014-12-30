package com.github.jarod.qqwry;

public class WryString {
	public final String string;
	/** length including the \0 end byte */
	public final int length;

	public WryString(final String string, final int length) {
		this.string = string;
		this.length = length;
	}
}
