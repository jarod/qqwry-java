package com.github.jarod.qqwry;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QQWryTest {
	private static QQWry qqwry;

	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		qqwry = new QQWry();
	}

	@Test
	public void testDirectMode() throws Exception {
		final IPZone zone = qqwry.findIP("42.86.131.35");
		assertEquals("辽宁省铁岭市", zone.getMainInfo());
		assertEquals("联通", zone.getSubInfo());
	}

	@Test
	public void testRedirectMode1() {
		final IPZone zone = qqwry.findIP("115.199.185.246");
		assertEquals("浙江省杭州市", zone.getMainInfo());
		assertEquals("电信", zone.getSubInfo());
	}

	@Test
	public void testRedirectMode2() {
		final IPZone zone = qqwry.findIP("111.5.126.139");
		assertEquals("河南省郑州市", zone.getMainInfo());
		assertEquals("移动", zone.getSubInfo());
	}

	@Test
	public void testRedirectMode1Mode2() {
		final IPZone zone = qqwry.findIP("113.64.230.49");
		assertEquals("广东省河源市", zone.getMainInfo());
		assertEquals("电信", zone.getSubInfo());
	}

	@Test
	public void testParseDatabaseVersion() {
		String dbVer = qqwry.parseDatabaseVersion();
		System.out.println("qqwry.dat version=" + dbVer);
		assertTrue(dbVer != null && !dbVer.isEmpty());
		String[] parts = dbVer.split("\\.");
		assertEquals(3, parts.length);
	}

	/**
	 * https://github.com/jarod/qqwry-java/issues/3
	 */
	@Test
	public void testIssue3() {
		final IPZone zone = qqwry.findIP("1.179.249.1");
		assertEquals("泰国", zone.getMainInfo());
		assertEquals("曼谷Google缓存服务器(泰国电信公共有限公司-泰国电讯公司TOT)", zone.getSubInfo());
	}
}
