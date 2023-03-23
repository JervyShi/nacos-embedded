# Nacos Embedded

[![Build Status](https://travis-ci.org/JervyShi/nacos-embedded.svg?branch=master)](https://travis-ci.org/JervyShi/nacos-embedded)
[![codecov](https://codecov.io/gh/jervyshi/nacos-embedded/branch/master/graph/badge.svg)](https://codecov.io/gh/jervyshi/nacos-embedded)

Nacos-embedded provides easy way to run Nacos in integration tests. Inspired by [embedded-consul](https://github.com/pszymczyk/embedded-consul).

Compatible with jdk1.8+. 
Working on all operating systems: Mac(Intel & Apple silicon), Linux, Windows.

## How to get it?

```xml
<dependency>
    <groupId>name.jervyshi</groupId>
    <artifactId>nacos-embedded</artifactId>
    <version>0.3.0</version>
    <scope>test</scope>
</dependency>
```

## Usage

### Manual

```java
public class IntegrationTest {

    private NacosProcess nacosProcess;

    @Before
    public void setup() {
        nacosProcess = NacosStarterBuilder.nacosStarter().build().start();
    }

    @After
    public void cleanup() throws Exception {
        nacosProcess.close();
    }

    /* tests with nacos client */
}
```
