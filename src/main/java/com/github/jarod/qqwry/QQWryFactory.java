package com.github.jarod.qqwry;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * All instance created by same factory share one copy of qqwry.dat memory data.
 * @author Jarod Liu <liuyuanzhi@gmail.com>
 */
public abstract class QQWryFactory {
	private final byte[] data;

	/**
	 * Load qqwry.dat from classpath
	 */
	protected QQWryFactory() throws IOException {
		final InputStream in = DefaultQQWryFactory.class.getClassLoader().getResourceAsStream("qqwry.dat");
		final ByteArrayOutputStream out = new ByteArrayOutputStream(10 * 1024 * 1024); // 10MB
		final byte[] buffer = new byte[4096];
		while (true) {
			final int r = in.read(buffer);
			if (r == -1) {
				break;
			}
			out.write(buffer, 0, r);
		}
		data = out.toByteArray();
	}

	public QQWryFactory(final String filename) throws IOException {
		data = Files.readAllBytes(Paths.get(filename));
	}

	public abstract QQWry getInstance();

	protected byte[] getData() {
		return data;
	}
}