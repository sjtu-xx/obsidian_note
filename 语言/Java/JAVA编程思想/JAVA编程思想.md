# JAVA编程思想.md

```yaml

title: JAVA编程思想
date: 2021-06-15 21:44:57
tags:
categories:
- JAVA
- 提升

```

《on Java 8》是Bruce Eckel继《Java编程思想(第四版)》（Thinking in Java）后的续作，主要是在《Java编程思想》的基础上，增加了对Java8新特性的一些理解。

由于这本书面向所有读者，本笔记不再对Java基础部分进行记录，只记录一些在Java编程过程中经常会遇到的一些问题。

<!--more-->

# 对象

> Java中的参数传递：Java 对象标识符实际上是“对象引用”（object references），并且一切都是值传递。所以你不是通过引用传递，而是“通过值传递对象引用。
> 

## Java中基本数据类型

### 基本数据类型所占的空间

| 包装类型      | 基本类型    | 大小      | 最大值            | 最小值       |
|-----------|---------|---------|----------------|-----------|
| Boolean   | boolean | —       | —              | —         |
| Character | char    | 16 bits | Unicode 216 -1 | Unicode 0 |
| Byte      | byte    | 8 bits  | 127            | -128      |
| Short     | short   | 16 bits | 215 -1         | -215      |
| Integer   | int     | 32 bits | 231 -1         | -231      |
| Long      | long    | 64 bits | 263 -1         | -263      |
| Float     | float   | 32 bits | IEEE754        | IEEE754   |
| Double    | double  | 64 bits | IEEE754        | IEEE754   |
| Void      | void    | —       | —              | —         |

所有的数值类型都是有正/负符号的。布尔（boolean）类型的大小没有明确的规定，通常定义为取字面值 “true” 或 “false” 。基本类型有自己对应的包装类型，如果你希望在堆内存里表示基本类型的数据，就需要用到它们的包装类。

### 基本数据类型的初始化
| 初始值           | 基本类型    |  |
|---------------|---------|--|
| FALSE         | boolean |  |
| \u0000 (null) | char    |  |
| (byte) 0      | byte    |  |
| (short) 0     | short   |  |
| 0             | int     |  |
| 0L            | long    |  |
| 0.0f          | float   |  |
| 0.0d          | double  |

> 为了安全，我们最好始终显式地初始化变量（类中的变量）。

# 操作符

## 移位操作符

左移位操作符（`<<`）会将操作符左侧的操作数向左移动，移动的位数在操作符右侧指定（低位补0）。

有符号的右移位操作符（`>>`）会将操作符左侧的操作数向右移动，移动的位数在操作符右侧指定（如果符号为正，高位插入0，否则插入1）。

无符号的右移位操作符（`>>>`），使用零扩展：无论符号为正还是负，都在高位补0。

<aside>
💡 如果对byte或short值进行移位运算，得到的可能是不正确的结果。他们会首先被提升为int类型，然后在被赋给原来的变量时被截断。

</aside>

```java
public class URShift {
    public static void main(String[] args) {
        byte b = -1;
        System.out.println(Integer.toBinaryString(b)); // 11111111111111111111111111111111
        b >>>= 10;
        System.out.println(Integer.toBinaryString(b)); // 11111111111111111111111111111111
        b = -1;
        System.out.println(Integer.toBinaryString(b)); // 11111111111111111111111111111111
        System.out.println(Integer.toBinaryString(b >>> 10)); // 1111111111111111111111
    }
}
```

## 类型转换操作符

Java可以把任何基本类型转换为别的基本类型，但boolean除外，他不允许进行任何类型的转换处理。

“类”类型也不允许进行类型转换。

## 截尾和舍入

float或double转型为整型值时，总是对该数值进行截尾。如果想要舍入，使用Math.round()

# 初始化和清理

## 无参构造器

如果已经定义了一个构造器，无论是否有参数，编译器都不会再自动创建一个无参构造器。

## this关键字

### 在构造器中调用构造器

使用this关键字进行此类调用。

## 清理

> 垃圾收集器的原理：任何没有被废弃的对象最终都能追溯到存在于栈或静态存储中的引用。·100
> 

当垃圾收集器准备释放资源的时候，他首先会调用`finalize()`方法。如果对象没有被回收，`finalize()`就永远不会执行。

垃圾收集仅与内存有关！

使用finalize()的唯一原因是回收程序中不再使用的内存（通过**本地方法**分配的内存）。

[本地方法](本地方法.md)

## 初始化

### 成员初始化

如果类的字段时基本类型，类的每个基本类型字段都会获得一个初始值。

### 构造器初始化

static初始化仅在必要时发生。

初始化顺序是从静态字段开始，然后是非静态字段

> 虽然没有显式使用static关键字，但构造器实际也是static方法
静态初始化只在Class对象首次加载时发生一次。
> 

Java提供了一种称为**实例初始化**的语法用于初始化每个对象的非静态变量，实例初始化子句在构造器之前执行。

```java
class A {
	private int a;
	{
		a = 1;
	}
}
```

### 数组初始化

数组复制实际复制的是引用

```java
public class ArraysOfPrimitives {
    public static void main(String[] args) {
        int[] a1 = {1, 2, 3, 4, 5};
        int[] a2;
        a2 = a1;
        for (int i = 0; i < a2.length; i++) {
            a2[i] += 1;
        }
        for (int i = 0; i < a1.length; i++) {
            System.out.println("a1[" + i + "]" + a1[i]);
        }
        //a1[0]2
        //a1[1]3
        //a1[2]4
        //a1[3]5
        //a1[4]6
    }
}
```

#### 动态数组的创建

`int[] a = new int[rand.nextInt(20)];`

如果数组元素为基本类型，则会自动初始化为空值（数值类型和char是0，boolean是false）

如果数组元素为类，不会进行自动初始化，需要for对每个元素进行赋值，否则值为空

```java
public class ArraysOfPrimitives {
    public static void main(String[] args) {
        Integer[] a = new Integer[new Random().nextInt(20)];
        System.out.println(Arrays.toString(a)); // [null, null]
    }
}
```

# 实现隐藏

## 模块

模块的作用：

- 引入包时，引入需要的模块即可
- 隐藏不对外暴露的模块

对于除了大型第三方库之外的任何项目，在不使用模块的情况下进行构建就可以了

# 多态

## 字段与静态方法

只有普通方法的调用是可以多态的。如果直接访问一个字段，则该字段会在编译时解析。

```java
class Super {
    public int field = 0;

    public int getField() {
        return field;
    }
}

class Sub extends Super {
    public int field = 1;

    @Override
    public int getField() {
        return field;
    }

    public int getSuperField() {
        return super.field;
    }
}

public class FieldAccess {
    public static void main(String[] args) {
        Super sup = new Sub();
        System.out.println(sup.field); // 0
        System.out.println(sup.getField()); // 1
    }
}
```

如果一个方法是静态方法，他的行为就不会是多态的。

## 协变返回类型

Java5增加了协变返回类型，这表示子类中重写方法的返回值可以是基类方法返回值的子类型。

# 接口

```java
interface AnInterface{
	// 可以添加默认方法
	default void newMethod(){
		System.out.println("123")
	}
	// 可以有静态方法
	static void show() {}
	void firstMethod;
}
```

## 抽象类和接口

| 特性 | 接口 | 抽象类 |
| --- | --- | --- |
| 组合 | 可以在新类中组合多个接口 | 只能继承一个抽象类 |
| 状态 | 不能包含非静态字段 | 可以包含字段，非抽象方法可以引用这些字段 |
| 默认方法与抽象方法 | 默认方法不需要在子类中实现，他只能引用接口中的方法 | 抽象方法必须在子类中实现 |
| 构造器 | 不能有构造器 | 可以有构造器 |
| 访问权限控制 | 隐式public | 可以为protected或包访问权限 |

## 接口中的字段

接口中的任何字段都自动是static和final的

## 嵌套接口

接口可以嵌套在类和其他接口中。

private接口只能在定义接口的类中实现

## Java9新特性：private方法

Java9中可以定义private方法，这种方法默认都是default的。

## Java17新特性：密封类和密封接口

JDK17引入了密封类和密封接口，因此基类或接口可以限制自己可以派生出哪些类。

```java
sealed class Base permits D1,D2 {}
```

如果所有的子类都定义在同一个文件中，则不需要permits子句。

seaded类的子类只能通过下面的某个修饰符定义：

- final：不允许有进一步的子类。
- sealed：允许有一组密封子类
- non-sealed：允许未知的子类继承他。

JDK16中的record也可以用作接口的密封实现，record是隐式final的。

# 内部类

内部类可以自动访问外围类的所有成员

## 使用.this和.new

要生成外部类对象的引用，可以使用外部类的名字，后面加上`.this`

创建内部类，`p.new InnerClass()`

## 匿名内部类

如果你正在定义一个匿名内部类，而且一定要用到一个在该匿名类之外定义的对象，编译器要求参数引用用final修饰。

实例初始化部分就是匿名内部类的构造器。他也有局限性：无法重载实例初始化部分，所以只能有一个这样的构造器。

## 嵌套类

如果不需要内部类对象和外部类对象之间的连接，可以将内部类设置为staic的，通常称之为**嵌套类**

嵌套类：创建不依赖外部类对象；无法从嵌套类内部访问非static的外部类对象。

普通内部类中不能有static数据、static字段，也不能包含嵌套类。（类创建时，初始化过程要初始化内部类，普通内部类可以看作外部类的非静态成员）

## 为什么需要内部类

每个内部类都可以独立的继承自一个实现。因此，外部类是否已经继承了某个实现，对内部类没有限制。

如果没有内部类提供的这种事实上能继承多个具体类或抽象类的能力，有些设计或编程问题会非常棘手。从某种角度讲，内部类完善了多重继承问题的解决方案。

### 闭包与回调

闭包是一个可调用对象，塔博阿留了来自被他创建时所在作用域的信息。

在Java8之前，要生成类似闭包的行为，只能通过内部类。自Java8之后也可以通过lambda表达式。

## 继承内部类

因为内部类的构造器必须附着到一个指向其包围类的对象的引用上，所以当你要继承内部类时，事情就稍微有点复杂了。

```jsx
class WithInner {
    class Inner {
    }
}

public class InheritInner extends WithInner.Inner {
    InheritInner(WithInner wi) {
        wi.super();
    }
```

## 内部类可以被重写吗

不可以，但是继承外部类后，新的外部类中的子类可以显式继承旧外部类中的子类。

当继承外部类时，内部类没有额外的特殊之处，这两个内部类是完全独立的实体，分别在自己的命名空间中。

# 集合

## 基本概念

Java集合类从设计上分为两类：

- Collection：都实现了Iterable接口
- Map

## Iterator

- 使用iterator()方法返回Iterator
- next()方法获得序列中的下一个元素
- hasNext()检查是否还有对象
- remove()删除迭代器最近返回的元素

```java
public static void main(String[] args) {
    List<Integer> ll = new LinkedList<>(Arrays.asList(1, 2, 3, 4));
    Iterator<Integer> iterator = ll.iterator();
    while (iterator.hasNext()) {
        iterator.next();
        iterator.remove();
    }
}
```

### ListIterator

ListIterator只有List类可以生成，可以访问前一个（previous）或后一个

## LinkedList

- getFirst()和element()是完全相同的，返回头部第一个元素，如果列表为空，抛出NoSuchElementException
- peek()返回第一个元素，如果为空，返回null
- removeFirst和remove也是完全相同，删除并返回表头的第一个元素。如果列表为空，抛出NoSuchElementException
- poll()删除表头第一个元素，如果列表为空，返回null
- addFirst，在表头插入一个元素
- offer，add和addLast相同，都是在表尾插入一个元素
- removeLast一出并返回列表中的最后一个元素

## Stack

Java1.0中的Stack设计的非常糟糕。Java6加入了ArrayDeque，提供了直接实现栈功能的方法。

```java
public class StackTest {
    public static void main(String[] args) {
        Deque<String> stack = new ArrayDeque<>();
        stack.push("123");
        String pop = stack.pop();
    }
}
```

## Set

TreeSet储存时会进行排序。

```java
// treeset中会按照字母排序，并且不区分大小写
Set<String> word = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
```

## Java16新特性：record类型

record定义的希望是成为数据传输对象。

使用record关键字时，会自动生成：

- 不可变的字段
- 规范的构造器
- 每个元素都有的访问器方法
- equals(),hashCode(),toString()

```java
record Employee(String name, int id) { }

public class BasicRecord {
    public static void main(String[] args) {
        var bob = new Employee("Bob", 11);
        System.out.println(bob.name());
        System.out.println(bob);
    }
}
```

record类可以定义方法，但是这些方法只能读取字段。

不能继承record，因为record隐含为final。

record可以使用紧凑型构造器

```java
record Point(int x, int y) {
    Point {
        x += 1;
        y += 1;
    }
    //    // 可以使用普通构造器替换紧凑型构造器
    //    Point(int x, int y) {
    //        this.x = x + 1;
    //        this.y = y + 1;
    //    }
}
```

## Queue

LinkedList实现了Queue接口

### PriorityQueue

Java5中增加了PriorityQueue。默认使用自然顺序，可以通过提供Comparator来修改顺序。

```java
PriorityQueue(int initialCapacity, Comparator<? super E> comparator)
```

## for-in和迭代器

实现了Iterable接口的类，可以用在for-in语句中

```java
class IterableClass implements Iterable<String>{}

for (String s: new IterableClass())
```

### 适配器方法惯用法

如果要反向遍历列表，可以继承类，并覆盖iterator方法，但是没有选择的余地。

可以编写一个适配器，增加一个生成Iterable对象的方法。

```java
public class ReversibleArrayList<T> extends ArrayList<T> {
    ReversibleArrayList(Collection<T> c) {
        super(c);
    }

    public Iterable<T> reversed() {
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return new Iterator<T>() {
                    int current = size() - 1;

                    @Override
                    public boolean hasNext() {
                        return current > -1;
                    }

                    @Override
                    public T next() {
                        return get(current--);
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }
}
```

# 函数式编程

## lambda表达式

### 递归

递归意味着一个函数调用了自身。在Java中也可以编写递归的lambda表达式，但是，这个lambda表达式必须被赋值给一个静态变量或一个实例变量否个会出现错误。

```java
interface IntCall {
    int call(int arg);
}

public class RecursiveFactorial {
    static IntCall fact;

    public static void main(String[] args) {
        fact = n -> n == 0 ? 1 : n * fact.call(n - 1);
        for (int i = 0; i < 10; i++) {
            System.out.println(fact.call(i));
        }
    }
}
```

## 方法引用

### 未绑定方法引用

```java
class X {
    String f() {
        return "X:f()";
    }
}

interface MakeString {
    String make();
}

interface TransformX {
    String transform(X x);
}

public class UnBoundMethodReference {
    public static void main(String[] args) {
        TransformX sp = X::f;
        X x = new X();
        System.out.println(sp.transform(x));
    }
}
```

## 函数式接口

每个接口都只包含一个抽象方法，叫做**函数式方法**。

函数式方法模式可以使用`@FunctionalInterface`注解来强制实施。

使用了`@FunctionalInterface`注解的接口也叫做**单一抽象方法**。

Java8增加的小魔法：如果我们将一个方法引用或lambda表达式赋值给某个函数式接口（而且类型可以匹配），那么Java会调整这个赋值，使其匹配目标接口。

java.util.function旨在创建一套完成的目标接口。如Runnable、Supplier、Callable、Consumer、UnaryOperator、Predicate等。

### 带有更多参数的函数式接口

```java
@FunctionalInterface
public interface TriFunction<T, U, W, R> {
    R apply(T t, U u, W w);
}
```

## 高阶函数

高阶函数式一个能接收函数作为参数或能把函数作为返回值的函数。

```java
interface Funcss extends Function<String, String> {
}

public class ProduceFunction {
    static Funcss product() {
        return s -> s.toLowerCase(Locale.ROOT);
    }
}
```

使用继承，可以方便的为专门的接口创建一个别名。

```java
interface FunctionAlias<T,R> extends Function<T,R> {}

public class FunctionAliasTest {
    public static void main(String[] args) {
        FunctionAlias<String, String> function = a -> a.toLowerCase(Locale.ROOT);
        System.out.println(function.apply("ssS"));
    }
}
```

## 闭包

如果lambda表达式中引用了局部作用域中的变量，则这个变量必须是final的（可以不声明为final，但是不能对变量进行更改（ 对变量的引用进行更改），即对基本类型就是不能变，对引用类型引用不能变）。

<aside>
💡 Java8中引用的“实际上的最终变量”：我们虽然没有显式地将一个变量声明为final的，但是仍然可以用最终变量的方式来对待他，只要不修改他即可。（即，可以在变量前加final，而不用修改其他代码）

</aside>

## 函数组合

java.util.function中的一些接口包含了支持函数组合的方法。

- andThen(argument)先执行原始操作，再执行参数操作。
- compose(argument)先执行参数操作，在执行原始操作。
- and(argument)
- or(argument)
- negate(argument)

# 流

## Java8对流的支持

三种操作：创建流、修改流元素、消费流元素

## 创建

```java
// 创建
Stream.of()
new LinkedList<String>().stream()  // 所有Collection都可以
new Random().ints().boxed()
IntStream.range(10,20).sum()

// generate
public class RandomWords implements Supplier<String> {}
Stream.generate(new RandomWords())

// iterate
// Stream.iterate()从第一个种子开始（第一个参数），然后将其传给第二个参数所引用的方法。其结果被添加到这个流上，并且保存下来作为下一次iterate调用的第一个参数。
Stream.iterate(0, i -> {
	int result = x + i;
	x = i;
	return result;
})

// builder
Stream.Builder<String> builder = Stream.builder();
builder.add("22");
builder.build().limit(1).sum();

// arrays
Arrays.asStream();

// regex
Pattern.compile("[ ,.]+").splitAsStream("asdfa sdf");
```

## 中间操作

```java
peek()
sorted()
distinct()
filter()
map()
mapToInt() // IntStream

flatMap(Function) // Function生成的是流
flatMapToInt()

Stream.concat(stream1,stream2) //合并两个流
```

## Optional类型

Optional在对象不存在时，为Optional.empty

### Optional对象上的操作

- filter(Predicate)：将谓词操作用于Optional内容，返回结果。如果为empty，直接返回
- map(Function)：如果Optional不为empty，将Function用于Optional中包含的对象，并返回结果
- flatMap(Function)：和map相似，但是所提供的映射函数会将结果包含在Optional中，所以不会再重复包装到Optional中

```java
public class OptionalOpTest {
    public static void main(String[] args) {
        Predicate<String> predicate = s -> true;
        Optional<String> ss = Optional.of("ss").filter(predicate);
        System.out.println(ss);

        Function<String, String> function = s -> s.toUpperCase(Locale.ROOT);
        Optional<String> s2 = Optional.of("ss").map(function);
        System.out.println(s2);

        Function<String, Optional<String>> function2 = s -> Optional.of(s.toUpperCase(Locale.ROOT));
        Optional<String> s3 = Optional.of("ss").flatMap(function2);
        System.out.println(s3);
    }
}
```

## 终结操作

```java
toArray()
toArray(generator)
forEach(Consumer)  // parallel时，会以随机的方式执行
forEachOrdered() 
collect(collector) // Collectors.toCollection(TreeSet::new)
collect(supplier, accumulator, combiner)  // accumulator将元素增加到result，combiner将不同线程的结果整合
reduce(BinaryOperator) 
reduce(identity, BinaryOperator) // identity为初始值
reduce(identity, BiFunction, BinaryOperator);

allMatch()
anyMatch()
nonMatch()

findFirst() //返回包含流中第一个元素的Optional
findAny() //返回包含流中某个元素的Optional（对非并行流，选择第一个元素，对并行流，选择任意元素）

// 如果需要选择最后一个元素
reduce((n1,n2)->n2)

count()
sum()
min()
max()
summaryStatistics()
```

# 异常

`e.printStackTrace()`默认会输入到标准错误流（System.err）

## 更好的NullPointerExcetion报告机制

JDK15敲定了更好的NullPointerExcetion报告机制，会返回为null的对象

## finally

```java
try{
	return;
} catch(){
	return;
}finally{
	System.out.println("不管在trycatch中是否返回，都会执行finally")
}
```

## 异常丢失

```java
public class LostException {
    public static void f() {
        throw new RuntimeException("ff");
    }
    public static void d() {
        throw new RuntimeException("dd");
    }

    public static void main(String[] args) {
        try {
            try {
                f();
            } finally {
                d();
            }
        } catch (RuntimeException e){
            System.out.println(e); // dd
						// f抛出的异常会丢失
        }
    }
}

// 在finally中return会导致前面的异常丢失
try {
	new RuntimeException();
} catch() {
	return;
}
```

## 异常的约束