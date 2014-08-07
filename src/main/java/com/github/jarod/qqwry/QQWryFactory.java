package com.github.jarod.qqwry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * All instance created by same factory share one copy of qqwry.dat memory data.
 * @author Jarod Liu <liuyuanzhi@gmail.com>
 */
public abstract class QQWryFactory {
	private final byte[] data;

	protected QQWryFactory(final String filename) throws IOException {
		data = Files.readAllBytes(Paths.get(filename));
	}

	public abstract QQWry getInstance();

	protected byte[] getData() {
		return data;
	}
}