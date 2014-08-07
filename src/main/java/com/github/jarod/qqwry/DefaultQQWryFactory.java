package com.github.jarod.qqwry;

import java.io.IOException;

public class DefaultQQWryFactory extends QQWryFactory {

	public DefaultQQWryFactory(final String filename) throws IOException {
		super(filename);
	}

	@Override
	public QQWry getInstance() {
		return new QQWry(getData());
	}

}
