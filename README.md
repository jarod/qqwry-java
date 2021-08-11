# qqwry-java

[![Build Status](https://api.travis-ci.org/jarod/qqwry-java.svg?branch=master)](https://travis-ci.org/jarod/qqwry-java)
[![Javadocs](http://www.javadoc.io/badge/com.github.jarod/qqwry-java.svg)](http://www.javadoc.io/doc/com.github.jarod/qqwry-java/0.8.0)

### usage:

Code sample:

```java
QQWry qqwry=new QQWry(); // load qqwry.dat from classpath

        QQWry qqwry=new QQWry(Paths.get("path/to/qqwry.dat")); // load qqwry.dat from java.nio.file.Path

        byte[]data=Files.readAllBytes(Paths.get("path/to/qqwry.dat"));
        QQWry qqwry=new QQWry(data); // create QQWry with provided data

        String dbVer=qqwry.getDatabaseVersion();
        System.out.printf("qqwry.dat version=%s",dbVer);
// qqwry.dat version=2020.9.10

        String myIP="127.0.0.1";
        IPZone ipzone=qqwry.findIP(myIP);
        System.out.printf("%s, %s",ipzone.getMainInfo(),ipzone.getSubInfo());
// IANA, 保留地址用于本地回送
```

Gradle:

```groovy
dependencies {
    api(
            "com.github.jarod:qqwry-java:0.8.+",
    )
}
```

or import from git repository directly (offical manual is
here: https://blog.gradle.org/introducing-source-dependencies )

first, update your settings.gradle at root of project directory

```groovy
sourceControl {
    gitRepository("ssh://git@github.com/jarod/qqwry-java.git") {
        producesModule("com.github.jarod:qqwry-java")
    }
}
```

second, add your dependencies within

```groovy
implementation('com.github.jarod:qqwry-java') {
    version {
        branch = 'master'
    }
}
```

Maven:

```xml

<dependency>
    <groupId>com.github.jarod</groupId>
    <artifactId>qqwry-java</artifactId>
    <version>0.8.0</version>
</dependency>
```

### build:

```
# OPTIONAL To embed qqwry.dat in the jar file, copy qqwry.dat to src/main/resources/qqwry.dat
# jar file will out put as ./build/lib/qqwry-java-X.X.X.jar
./gradlew jar
```
