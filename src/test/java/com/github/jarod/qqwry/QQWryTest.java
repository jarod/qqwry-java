package com.github.jarod.qqwry;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class QQWryTest {
	private QQWryFactory factory;

	@Before
	public void setUp() throws Exception {
		factory = new DefaultQQWryFactory();
	}

	@Test
	public void testDirectMode() throws Exception {
		final QQWry qqwry = factory.getInstance();
		final IPZone zone = qqwry.findIP("42.86.131.35");
		assertEquals("辽宁省铁岭市", zone.getCountry());
		assertEquals("联通", zone.getCity());
	}

	@Test
	public void testRedirectMode1() {
		final QQWry qqwry = factory.getInstance();
		final IPZone zone = qqwry.findIP("115.199.185.246");
		assertEquals("浙江省杭州市", zone.getCountry());
		assertEquals("电信", zone.getCity());
	}

	@Test
	public void testRedirectMode2() {
		final QQWry qqwry = factory.getInstance();
		final IPZone zone = qqwry.findIP("111.5.126.139");
		assertEquals("河南省郑州市", zone.getCountry());
		assertEquals("移动", zone.getCity());
	}

	@Test
	public void testRedirectMode1Mode2() {
		final QQWry qqwry = factory.getInstance();
		final IPZone zone = qqwry.findIP("113.64.230.49");
		assertEquals("广东省广州市", zone.getCountry());
		assertEquals("电信", zone.getCity());
	}
}
