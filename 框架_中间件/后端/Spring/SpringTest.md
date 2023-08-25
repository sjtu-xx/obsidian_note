# SpringTest

## SpringTest中mock对象

1. 使用`@MockBean`注解标注的对象，会自动替换所有autowired中的上下文。

```java
@RunWith(SpringRunner.class)
@SpringTest(classes = Application.class)
public class MockBeanAnnotationIntegrationTest {
    
    @MockBean
    UserRepository mockRepository;
    
    @Autowired
    ApplicationContext context;
    
    @Test
    public void givenCountMethodMocked_WhenCountInvoked_ThenMockValueReturned() {
        Mockito.when(mockRepository.count()).thenReturn(123L);

        UserRepository userRepoFromContext = context.getBean(UserRepository.class);
        long userCount = userRepoFromContext.count();

        Assert.assertEquals(123L, userCount);
        Mockito.verify(mockRepository).count();
    }
}
```

1. 使用`@Mock`注解标注的对象，会自动注入到`@InjectMocks`注解的Bean中的上下文。

## `@MockBean`和`@SpyBean`的区别

使用MockBean进行mock的对象，如果没有mock方法的返回值，就会返回null，

SpyBean则会同时mock对象的默认方法，只有对返回值进行mock时，才会调用mock方法