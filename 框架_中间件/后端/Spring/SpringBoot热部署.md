个人觉得没啥用，随便记录下
### pom
```xml
<dependency>  
	<groupId>org.springframework.boot</groupId>  
	<artifactId>spring-boot-devtools</artifactId>  
	<scope>runtime</scope>  
	<optional>true</optional>  
</dependency>
```
### maven打包插件
```xml
<build>  
	<plugins>  
		<plugin>  
			<groupId>org.springframework.boot</groupId>  
			<artifactId>spring-boot-maven-plugin</artifactId>  
			<configuration>  
				<fork>true</fork>  
				<addResources>true</addResources>  
			</configuration>  
		</plugin>  
	</plugins>  
</build>
```
### 自动编译
idea->build->comiler
![](_attachments/Pasted%20image%2020230606231503.png)
选中build project automatically、compile independent modules in parallel

![](_attachments/Pasted%20image%2020230606233451.png)