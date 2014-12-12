package com.github.jarod.qqwry;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Thread safe. A QQWry instance can share amount multi threads.
 *
 * @author Jarod Liu <liuyuanzhi@gmail.com>
 */
public class QQWry {
	private static final int INDEX_RECORD_LENGTH = 7;
	private static final byte REDIRECT_MODE_1 = 0x01;
	private static final byte REDIRECT_MODE_2 = 0x02;
	private static final byte STRING_END = '\0';

	private final byte[] data;
	private final long indexHead;
	private final long indexTail;
	private final byte[] stringBuf = new byte[64];

	/**
	 * Create QQWry Load qqwry.dat from classpath.
	 * @throws IOException
	 */
	public QQWry() throws IOException {
		final InputStream in = QQWry.class.getClassLoader().getResourceAsStream("qqwry.dat");
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
		indexHead = readLong32(0);
		indexTail = readLong32(4);
	}

	/**
	 * Create QQWry with provided qqwry.dat data.
	 * @param data fully read data from a qqwry.dat file.
	 */
	public QQWry(final byte[] data) {
		this.data = data;
		indexHead = readLong32(0);
		indexTail = readLong32(4);
	}

	/**
	 * Create QQWry from a path to file qqwry.dat.
	 * @param file path to qqwry.dat
	 * @throws IOException
	 */
	public QQWry(final Path file) throws IOException {
		data = Files.readAllBytes(file);
		indexHead = readLong32(0);
		indexTail = readLong32(4);
	}

	public IPZone findIP(final String ip) {
		final long ipNum = toNumericIP(ip);
		final QIndex idx = searchIndex(ipNum);
		if (idx == null) {
			return new IPZone(ip);
		}
		return readIP(ip, idx);
	}

	private long getMiddleOffset(final long begin, final long end) {
		long records = (end - begin) / INDEX_RECORD_LENGTH;
		records >>= 1;
		if (records == 0) {
			records = 1;
		}
		return begin + (records * INDEX_RECORD_LENGTH);
	}

	private String readCity(final int offset) {
		final byte b = data[offset];
		if ((b == REDIRECT_MODE_1) || (b == REDIRECT_MODE_2)) {
			final int areaOffset = readInt24(offset + 1);
			if (areaOffset == 0) {
				return "";
			} else {
				return readString(areaOffset).getString();
			}
		} else {
			return readString(offset).getString();
		}
	}

	private QIndex readIndex(final int offset) {
		final long min = readLong32(offset);
		final int record = readInt24(offset + 4);
		final long max = readLong32(record);
		return new QIndex(min, max, record);
	}

	private int readInt24(final int offset) {
		int v = data[offset] & 0xFF;
		v |= ((data[offset + 1] << 8) & 0xFF00);
		v |= ((data[offset + 2] << 16) & 0xFF0000);
		return v;
	}

	private IPZone readIP(final String ip, final QIndex idx) {
		final int pos = idx.getRecordOffset() + 4; // skip ip
		final byte mode = data[pos];
		final IPZone z = new IPZone(ip);
		if (mode == REDIRECT_MODE_1) {
			final int offset = readInt24(pos + 1);
			if (data[offset] == REDIRECT_MODE_2) {
				readMode2(z, offset);
			} else {
				final WryString country = readString(offset);
				final String city = readCity(offset + country.getLength());
				z.setCountry(country.getString());
				z.setCity(city);
			}
		} else if (mode == REDIRECT_MODE_2) {
			readMode2(z, pos);
		} else {
			final WryString country = readString(pos);
			final String city = readCity(pos + country.getLength());
			z.setCountry(country.getString());
			z.setCity(city);
		}
		return z;
	}

	private long readLong32(final int offset) {
		long v = data[offset] & 0xFFL;
		v |= (data[offset + 1] << 8L) & 0xFF00L;
		v |= ((data[offset + 2] << 16L) & 0xFF0000L);
		v |= ((data[offset + 3] << 24L) & 0xFF000000L);
		return v;
	}

	private void readMode2(final IPZone z, final int offset) {
		final int countryOffset = readInt24(offset + 1);
		final String country = readString(countryOffset).getString();
		final String city = readCity(offset + 4);
		z.setCountry(country);
		z.setCity(city);
	}

	private WryString readString(final int offset) {
		int i = 0;
		for (;; i++) {
			final byte b = data[offset + i];
			if (STRING_END == b) {
				break;
			}
			stringBuf[i] = b;
		}
		try {
			return new WryString(new String(stringBuf, 0, i, "GB18030"), i + 1);
		} catch (final UnsupportedEncodingException e) {
			return new WryString("", 0);
		}
	}

	private QIndex searchIndex(final long ip) {
		long head = indexHead;
		long tail = indexTail;
		while (tail > head) {
			final long cur = getMiddleOffset(head, tail);
			final QIndex idx = readIndex((int) cur);
			if ((ip >= idx.getMinIP()) && (ip <= idx.getMaxIP())) {
				return idx;
			}
			if ((cur == head) || (cur == tail)) {
				return idx;
			}
			if (ip < idx.getMinIP()) {
				tail = cur;
			} else if (ip > idx.getMaxIP()) {
				head = cur;
			} else {
				return idx;
			}
		}
		return null;
	}

	private long toNumericIP(final String s) {
		final String[] parts = s.split("\\.");
		if (parts.length != 4) {
			throw new IllegalArgumentException("ip=" + s);
		}
		long n = Long.parseLong(parts[0]) << 24L;
		n += Long.parseLong(parts[1]) << 16L;
		n += Long.parseLong(parts[2]) << 8L;
		n += Long.parseLong(parts[3]);
		return n;
	}
}
