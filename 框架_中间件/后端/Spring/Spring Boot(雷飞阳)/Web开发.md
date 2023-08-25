# Web开发

# SpringMVC自动配置

- `ContentNegotiatingViewResolver`和`BeanNameViewResolver`
- 静态资源支持（包括warjars）
- 自动注册`Converter，GenericConverter，Formatter`
- 支持`HttpMessageConverters`
- 自动注册 `MessageCodesResolver`
- 静态index.html支持
- 自定义Favicon
- 自动使用`ConfigurableWebBindingInitializer`

# 静态资源

## 静态资源目录

**静态资源放在类路径下**： called `/static` (or `/public` or `/resources` or `/META-INF/resources`

**访问** ： 当前项目根路径/ + 静态资源名

**原理：**静态映射`/**`。 请求进来先在Controller里面找，找不到在`/**`找，再找不到404

## 静态资源访问前缀：

默认无前缀

```yaml
spring:
  mvc:
    static-path-pattern: /res/**

  resources:
    static-locations: [classpath:/haha/]
```

## WebJars

自动映射`/webjars/**`

https://www.webjars.org/

访问地址：[http://localhost:8080/webjars/**jquery/3.5.1/jquery.js**](http://localhost:8080/webjars/jquery/3.5.1/jquery.js)   后面地址要按照依赖里面的包路径

## 欢迎页支持

- 静态资源路径下的`index.html`
    - 这种情况不能配置访问前缀（2.5.3bug已经修复？）
- controller能处理`/index`

## 自定义Favicon

- 放在资源目录下的`favicon.ico`文件即可

## 静态资源配置原理

- SpringBoot默认加载自动配置类xxxAutoConfiguration
- `WebMvcAutoConfiguration` 内部类`WebMvcAutoConfigurationAdapter` 的参数`ResourceHandlerRegistrationCustomizer`
- 资源文件 `ResourceProperties`
- 欢迎页面的处理`WelcomePageHandlerMapping`

> 如果配置类只有一个有参构造器，则有参构造器中的所有参数都会从容器中确定。
> 

# 请求参数处理

## 请求映射

### rest使用与原理

- @xxxMapping；
- Rest风格支持（*使用HTTP请求方式动词来表示对资源的操作*）
    - *以前：/getUser 获取用户 /deleteUser 删除用户 /editUser 修改用户 /saveUser 保存用户*
    - *现在： /user GET-获取用户 DELETE-删除用户 PUT-修改用户 POST-保存用户*
    - 核心Filter；HiddenHttpMethodFilter
        - 用法： 表单method=post，隐藏域 _method=put
        - SpringBoot中手动开启

Rest原理（表单提交要使用REST的时候）

- 表单提交会带上**_method=PUT**
- **请求过来被**HiddenHttpMethodFilter拦截
- 请求是否正常，并且是POST
- 获取到**_method**的值。
- 兼容以下请求；**PUT**.**DELETE**.**PATCH**
- **原生request（post），包装模式requesWrapper重写了getMethod方法，返回的是传入的值。**
- **过滤器链放行的时候用wrapper。以后的方法调用getMethod是调用requesWrapper的。**

> `Ctrl+H` 打开继承树
`Ctrl+F12` 打开类方法弹窗
> 

# 普通参数与基本注解

## 注解

@PathVariable、@RequestHeader、@ModelAttribute、@RequestParam、@MatrixVariable、@CookieValue、@RequestBody

```java
@RestController
public class ParameterTestController {

    //  car/2/owner/zhangsan
    @GetMapping("/car/{id}/owner/{username}")
    public Map<String,Object> getCar(@PathVariable("id") Integer id,
                                     @PathVariable("username") String name,
                                     @PathVariable Map<String,String> pv,
                                     @RequestHeader("User-Agent") String userAgent,
                                     @RequestHeader Map<String,String> header,
                                     @RequestParam("age") Integer age,
                                     @RequestParam("inters") List<String> inters,
                                     @RequestParam Map<String,String> params,
                                     @CookieValue("_ga") String _ga,
                                     @CookieValue("_ga") Cookie cookie){

        Map<String,Object> map = new HashMap<>();

//        map.put("id",id);
//        map.put("name",name);
//        map.put("pv",pv);
//        map.put("userAgent",userAgent);
//        map.put("headers",header);
        map.put("age",age);
        map.put("inters",inters);
        map.put("params",params);
        map.put("_ga",_ga);
        System.out.println(cookie.getName()+"===>"+cookie.getValue());
        return map;
    }

		
    @PostMapping("/save")
    public Map postMethod(@RequestBody String content){
        Map<String,Object> map = new HashMap<>();
        map.put("content",content);
        return map;
    }

    //1、语法： 请求路径：/cars/sell;low=34;brand=byd,audi,yd
    //2、SpringBoot默认是禁用了矩阵变量的功能
    //      手动开启：原理。对于路径的处理。UrlPathHelper进行解析。
    //              removeSemicolonContent（移除分号内容）支持矩阵变量的
    //3、矩阵变量必须有url路径变量才能被解析 pathVar
    @GetMapping("/cars/{path}")
    public Map carsSell(@MatrixVariable("low") Integer low,
                        @MatrixVariable("brand") List<String> brand,
                        @PathVariable("path") String path){
        Map<String,Object> map = new HashMap<>();

        map.put("low",low);
        map.put("brand",brand);
        map.put("path",path);
        return map;
    }

    // /boss/1;age=20/2;age=10

    @GetMapping("/boss/{bossId}/{empId}")
    public Map boss(@MatrixVariable(value = "age",pathVar = "bossId") Integer bossAge,
                    @MatrixVariable(value = "age",pathVar = "empId") Integer empAge){
        Map<String,Object> map = new HashMap<>();

        map.put("bossAge",bossAge);
        map.put("empAge",empAge);
        return map;

    }

}
```

> `@RequestAttribute` 注解用于获取请求域中的参数
如：HttpRequestServlet→setAttribute
> 

> 页面开发，cookie禁用了，session怎么用
session.set(a,b) → jsessionid → cookie → 每次发请求携带
url重写：`/abc;jsessionid=xxxx` 把cookie的值使用矩阵变量发送
> 

## 参数映射原理

DispatcherServlet

- HandlerMapping找到Handler
- 为Handler找到HandlerAdapter

Map，Model类型的参数相当于给requestAttribute(httpsevletrequst.setattribute)赋值。

# 内容协商

### 自定义Converter

自定义WebMvcConfigure

```java
//1、WebMvcConfigurer定制化SpringMVC的功能
    @Bean
    public WebMvcConfigurer webMvcConfigurer(){
        return new WebMvcConfigurer() {
            @Override
            public void configurePathMatch(PathMatchConfigurer configurer) {
                UrlPathHelper urlPathHelper = new UrlPathHelper();
                // 不移除；后面的内容。矩阵变量功能就可以生效
                urlPathHelper.setRemoveSemicolonContent(false);
                configurer.setUrlPathHelper(urlPathHelper);
            }

            @Override
            public void addFormatters(FormatterRegistry registry) {
                registry.addConverter(new Converter<String, Pet>() {

                    @Override
                    public Pet convert(String source) {
                        // 啊猫,3
                        if(!StringUtils.isEmpty(source)){
                            Pet pet = new Pet();
                            String[] split = source.split(",");
                            pet.setName(split[0]);
                            pet.setAge(Integer.parseInt(split[1]));
                            return pet;
                        }
                        return null;
                    }
                });
            }
        };
    }
```

# 拦截器

## **HandlerInterceptor 接口**

```java
/**
 * 登录检查
 * 1、配置好拦截器要拦截哪些请求
 * 2、把这些配置放在容器中
 */
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    /**
     * 目标方法执行之前
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();
        log.info("preHandle拦截的请求路径是{}",requestURI);

        //登录检查逻辑
        HttpSession session = request.getSession();

        Object loginUser = session.getAttribute("loginUser");

        if(loginUser != null){
            //放行
            return true;
        }

        //拦截住。未登录。跳转到登录页
        request.setAttribute("msg","请先登录");
//        re.sendRedirect("/");
        request.getRequestDispatcher("/").forward(request,response);
        return false;
    }

    /**
     * 目标方法执行完成以后
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle执行{}",modelAndView);
    }

    /**
     * 页面渲染以后
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("afterCompletion执行异常{}",ex);
    }
}
```

配置

```java
/**
 * 1、编写一个拦截器实现HandlerInterceptor接口
 * 2、拦截器注册到容器中（实现WebMvcConfigurer的addInterceptors）
 * 3、指定拦截规则【如果是拦截所有，静态资源也会被拦截】
 */
@Configuration
public class AdminWebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")  //所有请求都被拦截包括静态资源
                .excludePathPatterns("/","/login","/css/**","/fonts/**","/images/**","/js/**"); //放行的请求
    }
}
```

# 文件上传

页面表单

```java
<form method="post" action="/upload" enctype="multipart/form-data">
    <input type="file" name="file"><br>
    <input type="submit" value="提交">
</form>
```

后台处理文件代码

```java
@PostMapping("/upload")
  public String upload(@RequestParam("email") String email,
                       @RequestParam("username") String username,
                       @RequestPart("headerImg") MultipartFile headerImg,
                       @RequestPart("photos") MultipartFile[] photos) throws IOException {

      log.info("上传的信息：email={}，username={}，headerImg={}，photos={}",
              email,username,headerImg.getSize(),photos.length);

      if(!headerImg.isEmpty()){
          //保存到文件服务器，OSS服务器
          String originalFilename = headerImg.getOriginalFilename();
          headerImg.transferTo(new File("H:\\cache\\"+originalFilename));
      }

      if(photos.length > 0){
          for (MultipartFile photo : photos) {
              if(!photo.isEmpty()){
                  String originalFilename = photo.getOriginalFilename();
                  photo.transferTo(new File("H:\\cache\\"+originalFilename));
              }
          }
      }

      return "main";
  }
```

## 自动配置原理

**文件上传自动配置类-MultipartAutoConfiguration-MultipartProperties**

- 自动配置好了 **StandardServletMultipartResolver 【文件上传解析器】**
- **原理步骤**
- **1、请求进来使用文件上传解析器判断（**isMultipart**）并封装（**resolveMultipart，**返回**MultipartHttpServletRequest**）文件上传请求**
- **2、参数解析器来解析请求中的文件内容封装成MultipartFile**
- **3、将request中文件信息封装为一个Map；**MultiValueMap<String, MultipartFile>

**FileCopyUtils**。实现文件流的拷贝

# 异常处理

## 错误处理

### 默认规则

- 默认情况下，Spring Boot提供`/error`处理所有错误的映射
- 对于机器客户端，它将生成JSON响应，其中包含错误，HTTP状态和异常消息的详细信息。对于浏览器客户端，响应一个“ whitelabel”错误视图，以HTML格式呈现相同的数据
- **要对其进行自定义，添加`View`解析为`error`**
- 要完全替换默认行为，可以实现 `ErrorController` 并注册该类型的Bean定义，或添加`ErrorAttributes类型的组件`以使用现有机制但替换其内容。
- error/下的4xx，5xx页面会被自动解析；

# Web原生组件注入（Servlet、Filter、Listener）

## 方法1：使用Servlet Api

可以使用``@WebServlet` ,`@WebFilter` 以及`@WebListener` 来注册组件，然后通过使用`@ServletComponentScan`来使这些组件生效

Servlet实现servlet接口，Filter实现Filter接口，Listener实现Listener（如ServletContenxtListener）接口

DispatchServlet 如何注册进来

- 容器中自动配置了 DispatcherServlet 属性绑定到 WebMvcProperties；对应的配置文件配置项是 **spring.mvc。**
- **通过 ServletRegistrationBean**<DispatcherServlet> 把 DispatcherServlet 配置进来。
- 默认映射的是 / 路径。

![https://cdn.nlark.com/yuque/0/2020/png/1354552/1606284869220-8b63d54b-39c4-40f6-b226-f5f095ef9304.png?x-oss-process=image%2Fwatermark%2Ctype_d3F5LW1pY3JvaGVp%2Csize_32%2Ctext_YXRndWlndS5jb20g5bCa56GF6LC3%2Ccolor_FFFFFF%2Cshadow_50%2Ct_80%2Cg_se%2Cx_10%2Cy_10](https://cdn.nlark.com/yuque/0/2020/png/1354552/1606284869220-8b63d54b-39c4-40f6-b226-f5f095ef9304.png?x-oss-process=image%2Fwatermark%2Ctype_d3F5LW1pY3JvaGVp%2Csize_32%2Ctext_YXRndWlndS5jb20g5bCa56GF6LC3%2Ccolor_FFFFFF%2Cshadow_50%2Ct_80%2Cg_se%2Cx_10%2Cy_10)

Tomcat-Servlet；

多个Servlet都能处理到同一层路径，精确优选原则(最长前缀优先原则)

A： /my/

B： /my/1

## 方法2：使用RegistrationBean

`ServletRegistrationBean`, `FilterRegistrationBean`, and `ServletListenerRegistrationBean`

```java
@Configuration
public class MyRegistConfig {

    @Bean
    public ServletRegistrationBean myServlet(){
        MyServlet myServlet = new MyServlet();

        return new ServletRegistrationBean(myServlet,"/my","/my02");
    }

    @Bean
    public FilterRegistrationBean myFilter(){

        MyFilter myFilter = new MyFilter();
//        return new FilterRegistrationBean(myFilter,myServlet());
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(myFilter);
        filterRegistrationBean.setUrlPatterns(Arrays.asList("/my","/css/*"));
        return filterRegistrationBean;
    }

    @Bean
    public ServletListenerRegistrationBean myListener(){
        MySwervletContextListener mySwervletContextListener = new MySwervletContextListener();
        return new ServletListenerRegistrationBean(mySwervletContextListener);
    }
}
```

# 嵌入式Servlet容器

## 切换嵌入式Servlet容器

- 默认支持的webServer
- `Tomcat`, `Jetty`, or `Undertow`
- `ServletWebServerApplicationContext 容器启动寻找ServletWebServerFactory 并引导创建服务器`
- 切换服务器

![https://cdn.nlark.com/yuque/0/2020/png/1354552/1606280937533-504d0889-b893-4a01-af68-2fc31ffce9fc.png?x-oss-process=image%2Fwatermark%2Ctype_d3F5LW1pY3JvaGVp%2Csize_26%2Ctext_YXRndWlndS5jb20g5bCa56GF6LC3%2Ccolor_FFFFFF%2Cshadow_50%2Ct_80%2Cg_se%2Cx_10%2Cy_10](https://cdn.nlark.com/yuque/0/2020/png/1354552/1606280937533-504d0889-b893-4a01-af68-2fc31ffce9fc.png?x-oss-process=image%2Fwatermark%2Ctype_d3F5LW1pY3JvaGVp%2Csize_26%2Ctext_YXRndWlndS5jb20g5bCa56GF6LC3%2Ccolor_FFFFFF%2Cshadow_50%2Ct_80%2Cg_se%2Cx_10%2Cy_10)

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <exclusions>
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-tomcat</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

- 原理
    - SpringBoot应用启动发现当前是Web应用。web场景包-导入tomcat
    - web应用会创建一个web版的ioc容器 `ServletWebServerApplicationContext`
    - `ServletWebServerApplicationContext` 启动的时候寻找 **`ServletWebServerFactory**（Servlet 的web服务器工厂---> Servlet 的web服务器）`
    - SpringBoot底层默认有很多的WebServer工厂；`TomcatServletWebServerFactory`, `JettyServletWebServerFactory`, or `UndertowServletWebServerFactory`
    - 底层直接会有一个自动配置类。`ServletWebServerFactoryAutoConfiguration`
    - `ServletWebServerFactoryAutoConfiguration`导入了`ServletWebServerFactoryConfiguration`（配置类）
    - `ServletWebServerFactoryConfiguration` 配置类 根据动态判断系统中到底导入了那个Web服务器的包。（默认是web-starter导入tomcat包），容器中就有`TomcatServletWebServerFactory`
    - `TomcatServletWebServerFactory` 创建出Tomcat服务器并启动；`TomcatWebServer` 的构造器拥有初始化方法`initialize---this.tomcat.start();`
    - 内嵌服务器，就是手动把启动服务器的代码调用（tomcat核心jar包存在）

## 定制Servlet容器

- 实现 `**WebServerFactoryCu**stomizer<ConfigurableServletWebServerFactory>`
- 把配置文件的值和**`ServletWebServerFactory 进行绑定`**
- 修改配置文件 **server.xxx**
- 直接自定义 **ConfigurableServletWebServerFactory**

**xxxxxCustomizer：定制化器，可以改变xxxx的默认规则**

```java
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.stereotype.Component;

@Component
public class CustomizationBean implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {

    @Override
    public void customize(ConfigurableServletWebServerFactory server) {
        server.setPort(9000);
    }

}
```

# 定制化原理

## 定制化的常见方式

- 修改配置文件；
- **xxxxxCustomizer；**
- **编写自定义的配置类 xxxConfiguration；+ @Bean替换、增加容器中默认组件；视图解析器**
- **Web应用 编写一个配置类实现 WebMvcConfigurer 即可定制化web功能；+ @Bean给容器中再扩展一些组件**

```java
@Configuration
public class AdminWebConfig implements WebMvcConfigurer
```

- `@EnableWebMvc + WebMvcConfigurer` —— `@Bean` 可以全面接管SpringMVC，所有规则全部自己重新配置； 实现定制和扩展功能
- 原理
    - 1、`WebMvcAutoConfiguration` 默认的SpringMVC的自动配置功能类。静态资源、欢迎页.....
    - 2、一旦使用 `@EnableWebMvc` 会 @Import(DelegatingWebMvcConfiguration.**class**)
    - 3、**DelegatingWebMvcConfiguration** 的 作用，只保证SpringMVC最基本的使用
        - 把所有系统中的 `WebMvcConfigurer` 拿过来。所有功能的定制都是这些 `WebMvcConfigurer` 合起来一起生效
        - 自动配置了一些非常底层的组件。**`RequestMappingHandlerMapping`**、这些组件依赖的组件都是从容器中获取
        - **`public class** DelegatingWebMvcConfiguration **extends WebMvcConfigurationSupport**`
    - 4、**`WebMvcAutoConfiguration`** 里面的配置要能生效 必须 `@ConditionalOnMissingBean(**WebMvcConfigurationSupport**.**class**)`
    - 5、`@EnableWebMvc` 导致了 **`WebMvcAutoConfiguration` 没有生效。**
    - ... ...

## 原理分析套路

**场景starter - xxxxAutoConfiguration - 导入xxx组件 - 绑定xxxProperties -- 绑定配置文件项**