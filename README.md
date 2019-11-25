## Spring Boot API Server
- JPA를 이용한 REST API 서버 구현(CRUD)
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
        1. `@Entity` - `DB와 Mapping되는 모델`
        1. `@Table`
        1. @Getter - `Lombok`
        1. `@Coulum` - `Annotation은 데이터베이스 컬럼으로 지정, 컬럼명과 Mapping됨`
    1. DepartmentRepository
        1. package: repository - `Interface역할, JpaRepository<Entity, 기본키 타입> 을 extends하면 기본적인 CRUD가 자동으로 생성`
        1. interface: DepartmentRepository
        1. Anotation: `@Repository`
    1. DepartmentService
        1. package: service
        1. interface: DepartmentService
        1. Anotation: `@Service`
    1. DepartmentController
        1. package: controller
        1. class: DepartmentController
        1. Anotation: `@Controller`
## CRUD
1. `@RestController` - REST API를 사용하기 위한 Anotation, Json/Xml 형태로 객체 데이터를 반환
1. GET
    1. @GetMapping("/departments")
1. POST
    1. @PostMapping("/departments")
1. PUT
    1. @PutMapping("/departments/{id}")
1. DELETE
    1. @DeleteMapping("/departments/{id}")
## ENTITY
```JAVA
@Entity
@Table(name = "department")
@Getter
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "department_id")
    private Collection<Member> members;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void setCurrentTime() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void update(Department department) {
        this.name = department.getName();
        this.updatedAt = LocalDateTime.now();
    }
}
```
## REPOSITORY
```JAVA
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Optional<Department> findByName(String name);
}
```
## SERVICE
```JAVA
@Service
@AllArgsConstructor
public class DepartmentService {

    private DepartmentRepository departmentRepository;

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Department getDepartment(Long id) {
        Department department = departmentRepository.findById(id).orElse(null);

        if (department == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Department Not Found");
        } else {
            return department;
        }
    }

    public Department getDepartmentByName(String name) {
        Department department = departmentRepository.findByName(name).orElse(null);

        if (department == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Department Not Found");
        } else {
            return department;
        }
    }

    public Department createDepartment(Department department) {
        department.setCurrentTime();

        return departmentRepository.save(department);
    }

    public Department updateDepartment(Long id, Department department) {
        Department savedDepartment = departmentRepository.findById(id).orElse(null);

        if (savedDepartment == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Department Not Found");
        } else {
            savedDepartment.update(department);

            return departmentRepository.save(savedDepartment);
        }
    }

    public void deleteDepartment(Long id) {
        if (departmentRepository.findById(id).isPresent()) {
            departmentRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Department Not Found");
        }
    }
}
```
## CONTROLLER
```JAVA
@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class DepartmentController {

    private DepartmentService departmentService;

    @GetMapping("/departments")
    @ApiOperation(value = "전체 부서 조회", notes = "모든 부서를 조회하는 API")
    public ResponseEntity<?> getAllDepartments() {
        List<Department> departments = departmentService.getAllDepartments();

        return ResponseEntity.ok(departments);
    }

    @GetMapping("/departments/{id}")
    @ApiOperation(value = "부서 검색", notes = "ID로 부서를 검색하는 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "부서 ID", paramType = "path", dataType = "Long")
    })
    public ResponseEntity<?> getDepartment(@PathVariable Long id) {
        Department department = departmentService.getDepartment(id);

        return ResponseEntity.ok(department);
    }

    @GetMapping("/departments/name")
    public ResponseEntity<?> getDepartmentByName(@RequestParam String name) {
        Department department = departmentService.getDepartmentByName(name);

        return ResponseEntity.ok(department);
    }

    @PostMapping("/departments")
    public ResponseEntity<?> createDepartment(@RequestBody Department department) {
        return new ResponseEntity<>(departmentService.createDepartment(department), HttpStatus.CREATED);
    }

    @PutMapping("/departments/{id}")
    public ResponseEntity<?> updateDepartment(@PathVariable Long id, @RequestBody Department department) {
        return new ResponseEntity<>(departmentService.updateDepartment(id, department), HttpStatus.OK);
    }

    @DeleteMapping("/departments/{id}")
    public ResponseEntity<?> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }
}
```
