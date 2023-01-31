spring:
  datasource:
    url: ${'$'}{cockpit.jdbc.cockpit-sys.url}
    driver-class-name: ${'$'}{cockpit.jdbc.cockpit-sys.driver-class-name}
    username: ${'$'}{cockpit.jdbc.cockpit-sys.username}
    password: ${'$'}{cockpit.jdbc.cockpit-sys.password}
