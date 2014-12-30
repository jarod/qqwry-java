package com.github.jarod.qqwry;

public class QIndex {
	public final long minIP;
	public final long maxIP;
	public final int recordOffset;

	public QIndex(final long minIP, final long maxIP, final int recordOffset) {
		this.minIP = minIP;
		this.maxIP = maxIP;
		this.recordOffset = recordOffset;
	}
}
