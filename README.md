
download:
=========================

https://drone.io/github.com/jarod/qqwry-java/files


build:
=========================
```
# OPTIONAL To embed qqwry.dat in the jar file, copy qqwry.dat to src/main/resources/qqwry.dat
# jar file will out put as ./build/lib/qqwry-java-X.X.X.jar
./gradlew jar
```

usage:
=========================
```java
QQWry qqwry = new QQWry(); // load qqwry.dat from classpath
QQWry qqwry = new QQWry(Paths.get("path/to/qqwry.dat")); // load qqwry.dat from java.nio.file.Path

byte[] data = Files.readAllBytes(Paths.get("path/to/qqwry.dat"));
QQWry qqwry = new QQWry(data); // create QQWry with provided data

String myIP = "127.0.0.1";
IPZone ipzone = qqwry.findIP(myIP);
System.out.printf("%s, %s", ipzone.getCountry(), ipzone.getCity());
// IANA, 保留地址用于本地回送
```

see tests https://github.com/jarod/qqwry-java/tree/master/src/test/java/com/github/jarod/qqwry

* copy qqwry.dat to src/main/resources/qqwry.dat before running the tests.

[![Build Status](https://drone.io/github.com/jarod/qqwry-java/status.png)](https://drone.io/github.com/jarod/qqwry-java/latest)
