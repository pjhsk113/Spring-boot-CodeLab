# spring-boot-codelab

## Spring Boot API Server
- Embeded Tomcat 이용한 단독 실행
- Spring Actuator: 모니터링과 관리

## SET UP
- IntelliJ Ultimate, MySQL, Spring Boot
- JPA 사용(ORM : Object-relation-Mapping)

## CREATE DB
```java
create database codelab_db default character set utf8;

grant all privileges on codelab_db.* to codelab@localhost identified by 'codelab';

flush privileges;
```
## IntelliJ Project start
- create Project - Spring initializr
- New Project
- Dependencies 
  - Dev Tools - `lombok` 
  - WEB - `Spring Web` 
  - SQL - `Spring Data JPA`, `MySQL Driver`

## DB SET UP
1. `src` - `main` - `resources`
    1. application.properties 삭제
    1. application.yml 만들기
```java
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/codelab_db?serverTimezone=Asia/Seoul&useSSL=false&characterEncoding=utf-8
    username: codelab
    password: codelab
    driver-class-name: com.mysql.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update # 없으면 만들고 있으면 그대로 사용 (create : 무조건 새로 만든다.)
    show-sql: true # mysql debug mode, 실제 쿼리가 로그로 보임
    database-platform: org.hibernate.dialect.MySQL5InnoDBDialect
    properties:
      hibernate: # custom options
        format_sql: true # sql 로그를 포매팅해서 보여줌
logging:
  level:
    org.hibernate.type: trace # bind 쿼리의 내용까지 출력. ? 부분이 실제 값으로 표현됨
server:
  port: 8080
```

## CREATE CONTROLLER
1. `src` - `main` - `java` - `kr.codelabs.member`
    1. package: controller
    1. class: MyController
        1. @RestController
        1. @GetMapping
실행

## MVC
1. `src` - `main` - `java` - `kr.codelabs.member`
1. Department
    1. package: entity(VO)
    1. class: Department
        1. @Entity  ()
        1. @Table
        1. @Getter  (Lombok)
    1. DepartmentRepository
        1. package: repository
        1. interface: DepartmentRepository
        1. Anotation: @Repository
    1. DepartmentService
        1. package: service
        1. interface: DepartmentService
        1. Anotation: @Service
    1. DepartmentController
        1. package: controller
        1. class: DepartmentController
        1. Anotation: @Controller
