#springboot #mysql

[Mysql中时区问题排查过程](https://juejin.cn/post/7035046535725334565)

几个需要配置的地方：
When working on JPA + Spring Boot for a backend application, make sure to use same TimeZone through out server configuration. For example, to use UTC do as below:

-   On server OS level: `sudo timedatectl set-timezone UTC` (for example on Cent OS)
-   On MySQL / Database server in `my.cnf` file (optional)
-   Spring config: `spring.jpa.properties.hibernate.jdbc.time_zone=UTC`
-   MySQL URL query parameters `spring.datasource.url=jdbc\:mysql\://localhost\:3306/my_db_name?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC&useLegacyDatetimeCode=false`
-   And if you are using Jackson JSON framework you can automatically parse `DateTime` value to String format using `@JsonFormat(shape= JsonFormat.Shape.STRING, pattern="dd MMM yyyy HH:mm:ss")` on the entity property / field.
-   Application entry point 
```java
@SpringBootApplication
@PostConstruct 
public void init() { 
	TimeZone.setDefault(TimeZone.getTimeZone("UTC")); 
}
```



### 设置JVM启动参数来设置时区
[stackoverflow](https://stackoverflow.com/questions/54316667/how-do-i-force-a-spring-boot-jvm-into-utc-time-zone)
if you want to pass JVM options from Maven Spring Boot Plugin to forked Spring Boot application:
```java
<properties>
  <spring-boot.run.jvmArguments>-Duser.timezone=UTC</spring-boot.run.jvmArguments>
</properties>
```

This is be equivalent to command line syntax:
```java
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Duser.timezone=UTC"
```

or when running a fully packaged Spring Boot application:
```java
java -Duser.timezone=UTC -jar app.jar
```