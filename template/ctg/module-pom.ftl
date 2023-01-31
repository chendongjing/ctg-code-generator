<#include "var.ftl"/><#t/>
<#-- ================================ -->
<project xmlns="http://maven.apache.org/POM/4.0.0"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
	    <artifactId>exec-cockpit</artifactId>
        <groupId>cn.ctg</groupId>
        <version>1.0-SNAPSHOT</version>
	</parent>
	<artifactId>${custom.service}</artifactId>
	<packaging>jar</packaging>
	<dependencies>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-webflux</artifactId>
		</dependency>

		<dependency>
			<groupId>cn.ctg</groupId>
			<artifactId>cockpit-common</artifactId>
			<version>1.0-SNAPSHOT</version>
		</dependency>

		<dependency>
			<groupId>org.postgresql</groupId>
			<artifactId>postgresql</artifactId>
		</dependency>

		<!-- apollo -->
		<dependency>
			<groupId>com.ctrip.framework.apollo</groupId>
			<artifactId>apollo-client</artifactId>
			<version>${'$'}{apollo.version}</version>
		</dependency>

		<dependency>
			<groupId>com.ctrip.framework.apollo</groupId>
			<artifactId>apollo-core</artifactId>
			<version>${'$'}{apollo.version}</version>
		</dependency>

		<dependency>
			<groupId>io.springfox</groupId>
			<artifactId>springfox-swagger2</artifactId>
			<version>${'$'}{swagger2.version}</version>
		</dependency>

		<dependency>
			<groupId>io.springfox</groupId>
			<artifactId>springfox-swagger-ui</artifactId>
			<version>${'$'}{swagger2.version}</version>
		</dependency>

		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-bootstrap</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
			<exclusions>
				<exclusion>
					<groupId>com.thoughtworks.xstream</groupId>
					<artifactId>xstream</artifactId>
				</exclusion>
			</exclusions>
		</dependency>
		<dependency>
			<groupId>com.thoughtworks.xstream</groupId>
			<artifactId>xstream</artifactId>
			<version>${'$'}{xstream.version}</version>
		</dependency>

		<dependency>
			<groupId>com.baomidou</groupId>
			<artifactId>mybatis-plus-boot-starter</artifactId>
			<version>${'$'}{mybatisplus.version}</version>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-openfeign</artifactId>
		</dependency>

	</dependencies>

	<dependencyManagement>
		<dependencies>
			<dependency>
				<groupId>org.springframework.cloud</groupId>
				<artifactId>spring-cloud-dependencies</artifactId>
				<version>${'$'}{spring-cloud.version}</version>
				<type>pom</type>
				<scope>import</scope>
			</dependency>
		</dependencies>
	</dependencyManagement>

	<build>
		<finalName>${'$'}{project.artifactId}</finalName>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-surefire-plugin</artifactId>
				<configuration>
					<skipTests>true</skipTests>
				</configuration>
			</plugin>

			<plugin>
				<artifactId>maven-antrun-plugin</artifactId>
				<executions>
					<execution>
						<id>copy</id>
						<phase>package</phase>
						<configuration>
							<tasks>
								<copy todir="../deploy" overwrite="true">
									<fileset dir="${'$'}{project.build.directory}">
										<include name="${'$'}{project.artifactId}.jar" />
									</fileset>
								</copy>
							</tasks>
						</configuration>
						<goals>
							<goal>run</goal>
						</goals>
					</execution>
				</executions>
			</plugin>
		</plugins>
	</build>
</project>