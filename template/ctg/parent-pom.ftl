<#include "var.ftl"/><#t/>
<#-- ================================ -->
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.5.2</version>
    </parent>

    <groupId>cn.ctg</groupId>
    <artifactId>exec-cockpit</artifactId>
    <packaging>pom</packaging>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
        <cockpit.version>1.0-SNAPSHOT</cockpit.version>
        <java.version>1.8</java.version>
        <spring-cloud.version>2020.0.3</spring-cloud.version>
        <mysql.version>8.0.17</mysql.version>
        <druid.version>1.1.13</druid.version>
        <mybatisplus.version>3.4.3.4</mybatisplus.version>
        <mybatis-spring.version>2.2.0</mybatis-spring.version>
        <jackson.version>2.13.1</jackson.version>
        <apollo.version>1.8.0</apollo.version>
        <dameng.version>7.6.0.142</dameng.version>
        <fastjson.version>1.2.76</fastjson.version>
        <swagger2.version>2.9.2</swagger2.version>
        <hutool.version>5.7.7</hutool.version>
        <poi.version>3.17</poi.version>
        <commons-lang3.version>3.8.1</commons-lang3.version>
        <xstream.version>1.4.18</xstream.version>
        <!-- 2.14.1存在漏洞，升级到最新版 -->
        <log4j-to-slf4j.version>2.15.0</log4j-to-slf4j.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-to-slf4j</artifactId>
            <version>${'$'}{log4j-to-slf4j.version}</version>
            <scope>compile</scope>
        </dependency>
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-api</artifactId>
            <version>${'$'}{log4j-to-slf4j.version}</version>
            <scope>compile</scope>
        </dependency>
    </dependencies>

	<modules>
		<module>${custom.service}</module>
	</modules>
</project>