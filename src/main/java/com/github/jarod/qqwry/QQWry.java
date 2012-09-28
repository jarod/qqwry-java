package com.github.jarod.qqwry;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class QQWry {
	private byte[] data;
	private final long indexHead;
	private final long indexTail;

	private static final byte STRING_END = '\0';
	private final byte[] STRING_BUF = new byte[64];

	private static final int IP_RECORD_LENGTH = 7;

	public QQWry(final String filename) throws IOException {
		try (BufferedInputStream in = new BufferedInputStream(
				new FileInputStream(filename), 1024 * 1024 * 10)) {
			data = new byte[in.available()];
			in.read(data);
		}
		indexHead = readLong32(0);
		indexTail = readLong32(4);
	}

	public IPZone findIP(final String ip) {
		final long ipNum = toNumericIP(ip);
		final QIndex idx = searchIndex(ipNum);
		if (idx == null) {
			return null;
		}
		return readIP(ip, idx);
	}

	private long getMiddleOffset(final long begin, final long end) {
		long records = (end - begin) / IP_RECORD_LENGTH;
		records >>= 1;
		if (records == 0) {
			records = 1;
		}
		return begin + (records * IP_RECORD_LENGTH);
	}

	private String readCity(final int offset) {
		final byte b = data[offset];
		if ((b == 0x01) || (b == 0x02)) {
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
		final int mode = data[idx.getRecordOffset() + 4];
		final IPZone z = new IPZone(ip);
		System.out.print("mode=" + mode + " ");
		if (mode == 0x01) {
			final int offset = readInt24(idx.getRecordOffset() + 5);
			if (data[offset] == 0x02) {
				readMode2(z, offset);
			} else {
				final WryString country = readString(offset);
				final String city = readCity(offset + country.getLength());
				z.setCountry(country.getString());
				z.setCity(city);
			}
		} else if (mode == 0x02) {
			readMode2(z, idx.getRecordOffset() + 4);
		}
		return z;
	}

	private long readLong32(final int offset) {
		long v = data[offset] & 0xFF;
		v |= (data[offset + 1] << 8) & 0xFF00;
		v |= ((data[offset + 2] << 16) & 0xFF0000);
		v |= ((data[offset + 3] << 24) & 0xFF000000L);
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
			STRING_BUF[i] = b;
		}
		try {
			return new WryString(new String(STRING_BUF, 0, i, "GB18030"), i);
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
		long n = Long.parseLong(parts[0].trim()) << 24L;
		n += Long.parseLong(parts[1].trim()) << 16L;
		n += Long.parseLong(parts[2].trim()) << 8L;
		n += Long.parseLong(parts[3].trim());
		return n;
	}
}
