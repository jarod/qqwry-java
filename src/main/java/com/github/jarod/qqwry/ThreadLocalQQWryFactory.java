package com.github.jarod.qqwry;

import java.io.IOException;

public class ThreadLocalQQWryFactory extends QQWryFactory {
	private final ThreadLocal<QQWry> instances = new ThreadLocal<QQWry>() {
		@Override
		protected QQWry initialValue() {
			return new QQWry(getData());
		}
	};

	public ThreadLocalQQWryFactory(final String filename) throws IOException {
		super(filename);
	}

	@Override
	public QQWry getInstance() {
		return instances.get();
	}

}
