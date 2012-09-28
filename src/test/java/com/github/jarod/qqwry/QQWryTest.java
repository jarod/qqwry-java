package com.github.jarod.qqwry;

import org.junit.Before;
import org.junit.Test;

public class QQWryTest {
	private QQWry qqwry;

	@Before
	public void setUp() throws Exception {
		qqwry = new QQWry("qqwry.dat");
	}

	@Test
	public void testFindIP() throws Exception {
		for (int a = 0; a <= 255; a++) {
			for (int b = 0; b <= 255; b++) {
				for (int c = 0; c <= 255; c++) {
					for (int d = 0; d <= 255; d++) {
						final String ipstr = String.format("%d.%d.%d.%d", a, b,
								c, d);
						final IPZone zone = qqwry.findIP(ipstr);
						if (zone.getCountry() != null) {
							System.out.println(zone);
						}
						Thread.yield();
					}
				}
			}
		}
	}

}
