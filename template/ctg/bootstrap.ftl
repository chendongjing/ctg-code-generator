server:
  port: 8104

spring:
  application:
    name: ${custom.service}

eureka:
  instance:
    prefer-ip-address: true
    instance-id: ${'$'}{spring.cloud.client.ip-address}:${'$'}{server.port}
  client:
    service-url:
      defaultZone: http://cockpit-eureka:8761/eureka
      fetch-registry: true

apollo:
  bootstrap:
    enabled: true
    namespaces: application,jdbc.yml,qyweixin.yml,redis.yml
  meta: http://cockpit-config:8080

mybatis:
  #mapper配置文件
  mapper-locations: classpath:mapper/*.xml
  #开启驼峰命名法
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  mybatis-plus:
    configuration:
      # 这个配置会将执行的sql打印出来，在开发或测试的时候可以用
      log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    db-config:
      id-type: ASSIGN_UUID

logging:
  level:
    cn.ctg.cockpit.dao: DEBUG