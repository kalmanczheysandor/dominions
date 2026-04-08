server:
  port: 15020
  servlet:
    session:
      timeout: 30m  # Set session timeout to 30 minutes


logging:
  level:
    #    org.springframework.security: DEBUG
    org.springframework.cloud.stream.binding: DEBUG






#  cookie:
#    same-site: none # Enables cookies between different sites.






spring:
  datasource:
    # Case-1: No container in use
    url: jdbc:mysql://127.0.0.1:3306/dominions?createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&autoReconnect=true

    # Case-2: Only database is in container
    #url: jdbc:mysql://127.0.0.1:3310/webshop?createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&autoReconnect=true

    # Case-3: containers are in use
    #   url: jdbc:mysql://webshop_db_container/webshop?createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&autoReconnect=true
    username: root
    password: CHANGE_ME
  jpa:
    hibernate:
      ddl-auto: validate
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQLDialect
    show-sql: on

  servlet:
    multipart:
      max-file-size: -1
      max-request-size: -1


  cloud:
    stream:
      bindings:
        test-out-0:
          destination: my-test-destination
        myTest-in-0:
          destination: my-test-destination
          group: my-test-queue
          consumer:
            concurrency: 2
      rabbit:
        bindings:

          test-out-0:
            producer:
              routingKeyExpression: '''my-test-key'''
          myTest-in-0:
            consumer:
              bindingRoutingKey: '''my-test-key'''
              queue-name-group-only: true
      binders:
        rabbit:
          type: rabbit
          environment:
            spring:
              rabbitmq:
                host: localhost
                port: 5672
                username: guest
                password: guest

#  mail:
#    host: CHANGE_ME
#    port: 587
#    username: "61e63cc99e6aed4fd5a250944d208d3b"
#    password: "71636c53537d3f5d8d46f02df58ef5cc"
#    properties:
#      mail:
#        smtp:
#          auth: true
#          starttls:
#            enable: true




  mail:
    host: CHANGE_ME
    port: 25
    username: CHANGE_ME
    password: jwYur520f0he
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: false