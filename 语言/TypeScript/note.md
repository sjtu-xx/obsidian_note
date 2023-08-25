## 简介

- 以`JS`为基础构建的语言
- `JS`的超集
- 可以在任何支持`JS`的平台执行
- TS不能直接被JS解析器执行
- 可以编译为JS

## 安装

1. 安装nodejs
    - [Download | Node.js (nodejs.org)](https://nodejs.org/en/download)
    - 命令行`node -v`
2. 安装TS
    - `npm --proxy http://localhost:7890 install -g typescript`
3. 命令行输入`tsc`

> [!note]
> - `tsc --init`初始化
> - `tsconfig.json`配置文件
> - `tsc -w`监视模式，自动编辑成JS

## 配置文件

```json5
{
  // include 哪些文件需要被编译
  // ["src/**/*"] 两个星表示所有目录，一个星表示所有文件
  "include": [
    "./**/*",
    "./*"
  ],
  // exclude 哪些文件不需要编译
  // exclude默认排除["node_modules", "bower_components", "jspm_packages"]
  "exclude": [
    "node_modules"
  ],
  // extends 继承config目录下base.json中的配置信息
  // "extends": "./config/base.json",
  "extends": "",
  // files 指定需要编译的ts文件
  "files": [
    "test.ts"
  ],
  // compilerOptions：编译器选项
  "compilerOptions": {
    /* 访问 https://aka.ms/tsconfig 了解更多关于该文件的信息 */

    /* 项目配置 */
    // "incremental": true,                              /* 保存 .tsbuildinfo 文件以实现增量编译项目。 */
    // "composite": true,                                /* 启用允许 TypeScript 项目与项目引用一起使用的约束。 */
    // "tsBuildInfoFile": "./.tsbuildinfo",              /* 指定 .tsbuildinfo 增量编译文件的路径。 */
    // "disableSourceOfProjectReferenceRedirect": true,  /* 禁用在引用复合项目时，优先使用源文件而不是声明文件。 */
    // "disableSolutionSearching": true,                 /* 编辑时，将项目排除在多项目引用检查之外。 */
    // "disableReferencedProjectLoad": true,             /* 减少 TypeScript 自动加载的项目数量。 */

    /* 语言和环境 */
    "target": "es2016",
    /* 设置生成的 JavaScript 的 JavaScript 语言版本，并包含兼容的库声明。 */
    // "lib": [],                                        /* 指定一组捆绑的库声明文件，描述目标运行时环境。 */
    // "jsx": "preserve",                                /* 指定生成的 JSX 代码。 */
    // "experimentalDecorators": true,                   /* 启用对传统实验性装饰器的实验支持。 */
    // "emitDecoratorMetadata": true,                    /* 在源文件中为装饰声明生成设计类型元数据。 */
    // "jsxFactory": "",                                 /* 指定目标为 React JSX 的 JSX 工厂函数，例如 'React.createElement' 或 'h'。 */
    // "jsxFragmentFactory": "",                         /* 指定目标为 React JSX 的片段时使用的 JSX Fragment 引用，例如 'React.Fragment' 或 'Fragment'。 */
    // "jsxImportSource": "",                            /* 指定在使用 'jsx: react-jsx*' 时导入 JSX 工厂函数的模块标识符。 */
    // "reactNamespace": "",                             /* 指定在 'createElement' 上调用的对象。仅适用于目标为 'react' 的 JSX 输出。 */
    // "noLib": true,                                    /* 禁用包括任何库文件，包括默认的 lib.d.ts 文件。 */
    // "useDefineForClassFields": true,                  /* 生成符合 ECMAScript 标准的类字段。 */
    // "moduleDetection": "auto",                        /* 控制使用哪种方法检测模块格式的 JS 文件。 */

    /* 模块配置 */
    "module": "commonjs",
    /* 指定生成的模块代码。 */
    // "rootDir": "./",                                  /* 指定源文件的根文件夹。 */
    // "moduleResolution": "node10",                     /* 指定 TypeScript 如何从给定的模块标识符查找文件。 */
    // "baseUrl": "./",                                  /* 指定解析非相对模块名称时的基本目录。 */
    // "paths": {},                                      /* 指定重新映射导入到其他查找位置的条目集合。 */
    // "rootDirs": [],                                   /* 允许将多个文件夹视为一个文件夹来解析模块。 */
    // "typeRoots": [],                                  /* 指定多个目录，其作为 './node_modules/@types' 一样。 */
    // "types": [],                                      /* 指定要包含但不在源文件中引用的类型包名称。 */
    // "allowUmdGlobalAccess": true,                     /* 允许从模块访问 UMD 全局变量。 */
    // "moduleSuffixes": [],                             /* 解析模块时要搜索的文件名后缀列表。 */
    // "allowImportingTsExtensions": true,               /* 允许导入包含 TypeScript 文件扩展名的文件。需要设置 '--moduleResolution bundler' 并且要么设置 '--noEmit' 或 '--emitDeclarationOnly'。 */
    // "resolvePackageJsonExports": true,                /* 解析导入时使用 package.json 中的 'exports' 字段。 */
    // "resolvePackageJsonImports": true,                /* 解析导入时使用 package.json 中的 'imports' 字段。 */
    // "customConditions": [],                           /* 在解析导入时，设置额外的条件，除了解析器特定的默认值。 */
    // "resolveJsonModule": true,                        /* 启用导入 .json 文件。 */
    // "allowArbitraryExtensions": true,                 /* 允许导入任何扩展名的文件，前提是存在声明文件。 */
    // "noResolve": true,                                /* 禁止 'import's、'require's 或 '<reference>'s 扩展 TypeScript 应添加到项目中的文件数量。 */

    /* JavaScript 支持 */
    // "allowJs": true,                                  /* 允许 JavaScript 文件作为程序的一部分。使用 'checkJS' 选项获取这些文件的错误报告。 */
    // "checkJs": true,                                  /* 在类型检查的 JavaScript 文件中启用错误报告。 */
    // "maxNodeModuleJsDepth": 1,                        /* 指定用于检查来自 'node_modules' 的 JavaScript 文件的最大文件夹深度。仅适用于 'allowJs'。 */

    /* 输出配置 */
    // "declaration": true,                              /* 从 TypeScript 和 JavaScript 文件生成 .d.ts 文件。 */
    // "declarationMap": true,                           /* 为 .d.ts 文件创建源映射。 */
    // "emitDeclarationOnly": true,                      /* 仅输出 .d.ts 文件，而不输出 JavaScript 文件。 */
    // "sourceMap": true,                                /* 为生成的 JavaScript 文件创建源映射文件。 */
    // "inlineSourceMap": true,                          /* 将源映射文件包含在生成的 JavaScript 内部。 */
    // "outFile": "./",                                  /* 指定将所有输出捆绑成一个 JavaScript 文件。如果 'declaration' 为 true，则也指定将所有 .d.ts 输出捆绑成一个文件。 */
    "outDir": "./dist/",
    /* 指定所有输出文件的输出文件夹。 */
    // "removeComments": true,                           /* 禁用输出注释。 */
    // "noEmit": true,                                   /* 禁用编译输出文件。 */
    // "importHelpers": true,                            /* 允许在整个项目中从 tslib 导入帮助函数，而不是每个文件都包含它们。 */
    // "importsNotUsedAsValues": "remove",               /* 指定导入仅用于类型的输出/检查行为。 */
    // "downlevelIteration": true,                       /* 为迭代生成更符合标准但冗长的 JavaScript 代码。 */
    // "sourceRoot": "",                                 /* 指定调试器用于查找引用源代码的根路径。 */
    // "mapRoot": "",                                    /* 指定调试器应该定位 map 文件的位置，而不是生成的位置。 */
    // "inlineSources": true,                            /* 在生成的 JavaScript 内部包含源代码的内容。 */
    // "emitBOM": true,                                  /* 在输出文件的开头生成一个 UTF-8 字节顺序标记 (BOM)。 */
    // "newLine": "crlf",                                /* 设置输出文件的换行符。 */
    // "stripInternal": true,                            /* 禁用生成包含 '@internal' 的 JSDoc 注释的声明。 */
    // "noEmitHelpers": true,                            /* 禁用在编译输出中生成自定义帮助函数，例如 '__extends'。 */
    // "noEmitOnError": true,                            /* 如果报告了任何类型检查错误，则禁用生成文件的输出。 */
    // "preserveConstEnums": true,                       /* 禁用在生成的代码中擦除 'const enum' 声明。 */
    // "declarationDir": "./",                           /* 指定生成声明文件的输出目录。 */
    // "preserveValueImports": true,                     /* 保留 JavaScript 输出中未使用的导入值，否则将会被删除。 */

    /* 互操作约束 */
    // "isolatedModules": true,                          /* 确保每个文件都可以安全地转译，而不依赖于其他导入。 */
    // "verbatimModuleSyntax": true,                     /* 不转换或省略任何未标记为仅类型的导入或导出，确保它们按照 'module' 设置在输出文件中的格式编写。 */
    // "allowSyntheticDefaultImports": true,             /* 允许在模块没有默认导出时使用 'import x from y'。 */
    "esModuleInterop": true,
    /* 生成额外的 JavaScript 代码以便支持导入 CommonJS 模块。这将启用 'allowSyntheticDefaultImports' 以实现类型兼容性。 */
    // "preserveSymlinks": true,                         /* 禁用解析符号链接到它们的真实路径。与 node 中的相同标志相关。 */
    "forceConsistentCasingInFileNames": true,
    /* 确保导入时的命名大小写正确。 */

    /* 类型检查 */
    "strict": true,
    /* 启用所有严格的类型检查选项。 */
    // "noImplicitAny": true,                            /* 启用对表达式和声明中暗含的 'any' 类型的错误报告。 */
    // "strictNullChecks": true,                         /* 在类型检查时考虑 'null' 和 'undefined'。 */
    // "strictFunctionTypes": true,                      /* 分配函数时，检查参数和返回值是否是子类型兼容的。 */
    // "strictBindCallApply": true,                      /* 检查 'bind'、'call' 和 'apply' 方法的参数是否与原始函数匹配。 */
    // "strictPropertyInitialization": true,             /* 检查在构造函数中声明但未设置的类属性。 */
    // "noImplicitThis": true,                           /* 当 'this' 的类型为 'any' 时，启用错误报告。 */
    // "useUnknownInCatchVariables": true,               /* 在 catch 子句中将默认的 catch 变量类型设为 'unknown' 而不是 'any'。 */
    // "alwaysStrict": true,                             /* 确保始终输出 'use strict'。 */
    // "noUnusedLocals": true,                           /* 启用错误报告，指示局部变量未被使用。 */
    // "noUnusedParameters": true,                       /* 当函数参数未使用时引发错误。 */
    // "exactOptionalPropertyTypes": true,               /* 将可选属性的类型解释为编写的类型，而不是添加 'undefined'。 */
    // "noImplicitReturns": true,                        /* 对于函数中没有显式返回的代码路径，启用错误报告。 */
    // "noFallthroughCasesInSwitch": true,               /* 启用针对 switch 语句中的 fallthrough 案例的错误报告。 */
    // "noUncheckedIndexedAccess": true,                 /* 当使用索引访问时，将 'undefined' 添加到类型。 */
    // "noImplicitOverride": true,                       /* 确保在派生类中重写的成员标有 override 修饰符。 */
    // "noPropertyAccessFromIndexSignature": true,       /* 强制使用索引访问器来访问使用索引类型声明的键。 */
    // "allowUnusedLabels": true,                        /* 禁用对未使用标签的错误报告。 */
    // "allowUnreachableCode": true,                     /* 禁用对无法访问的代码的错误报告。 */

    /* 完整性 */
    // "skipDefaultLibCheck": true,                      /* 跳过对 TypeScript 包含的 .d.ts 文件的类型检查。 */
    "skipLibCheck": true
    /* 跳过对所有 .d.ts 文件的类型检查。 */
  }
}
```

## 类型

### 类型基础

```ts
let a: number;
a = 12;

let b: string = "z";
// 数组
let b1: string[] = ["a", "b"];
let b2: Array<string> = ["a", "b"];
// 元组
let b3: [string, number] = ["a", 1];

// 自动确定类型
let c = true;
c = true;

let d: "male" | "female";
d = "male";

let e: boolean | string;
e = true;
e = "e";

let x: any;
x = "asdf";
x = true;
x = 123;

// any类型赋值给任何类型都不会报错
e = x;

let n: unknown;
n = 'a';
// unknown赋值给别的值会compile error
// e = n;
// 类型断言
// 方法1：类型断言
b = n as string;
b = <string>n;
// 方法2：判断类型
if (typeof n === "string") {
    b = n;
}

// 枚举
enum Gender {
    MALE = 0,
    FEMALE = 1
}

let gender: Gender = Gender.MALE;

// &表示同时
let j: { name: string } & { age: number }
j = {name: "hh", age: 123}

// 类型别名
type myType = string;
let m: myType;
m = "asdf"

type num = 1 | 2 | 3 | 4 | 5;
let k: num;
k = 1;

// void 空值(或undefined) 在 JavaScript 中，函数默认返回 undefined，这在 TypeScript 中的类型是 void
function sum(a: number, b: number): number | void {
    if (a === 1) {
        return a + b;
    }
}

// never 表示没有返回值：抛出一个异常或永远不返回
function neverReturn(): never {
    throw new Error();
}

// {} 指定对象中包含那些属性, 属性名？表示属性可选
let obj: { name: string, age?: number };
obj = {name: "xx"}

// [propName: string]: any 任意类型的属性可选
let obj2: { name: string, [propName: string]: any };
obj2 = {name: "123", s: 1}

// 声明函数
let func: (a: number, b: number) => number;
func = function (n1, n2) {
    return n1 + n2;
}
```

## webpack打包

### 打包

1. `npm init -y`项目初始化
2. `npm i --proxy http://localhost:7890 -D webpack webpack-cli typescript ts-loader`
3. 增加webpack配置文件`webpack.config.js`

```js
const path = require("path");

module.exports = {
    entry: "./src/index.js",
    output: {
        path: path.resolve(__dirname, "dist"),
        filename: "bundle.js"
    },
    module: {
        rules: [
            {
                // 指定的是规则生效的文件
                test: /\.ts$/,
                use: 'ts-loader',
                exclude: /node_modules/
            }
        ]
    }
}
```

4. 增加`tsconfig.json`

```json
{
  "compilerOptions": {
    "module": "ES2015",
    "target": "ES2015",
    "strict": true
  },
  "exclude": [
    "node_modules"
  ]
}
```

5. `package.json`中增加build

```json
{
  "scripts": {
    "build": "webpack"
  }
}
```

### 自动生成html文件

1. 安装`html-webpack-plugin`

- `npm install -D --proxy="http://localhost:7890" html-webpack-plugin`

2. 修改`webpack.config.js`

```js
const HTMLWebpackPlugin = require("html-webpack-plugin");
module.exports = {
    // 配置webpack插件
    plugins: [
        new HTMLWebpackPlugin({
            // title: "Webpack Plugin自定义的title"
            template: "./src/index.html"
        })
    ]
}
```

3. `npm run build`

### 热构建

1. 安装`webpack-dev-server`

- `npm install --proxy http://localhost:7890 -D webpack-dev-server`

2. 修改`package.json`

```json
{
  "scripts": {
    "start": "webpack serve --open"
  }
}
```

3. 修改webpack的mode
   在`weback.config.js`增加mode

```js
module.exports = {
    mode: "development"
}
```

### 自动清空dist

1. 在webpack5以上的版本中支持

```js
module.exports = {
    // ...
    output: {
        path: path.resolve(__dirname, 'dist'),
        clean: true,  // 清理输出目录
    },
};

```

### babel

解决兼容性支持

1. 安装
    - `npm install --proxy http://localhost:7890 -D @babel/core @babel/preset-env babel-loader core-js`

2. 修改`webpack.config.js`
```js
    {
    module: {
        rules: [
            {
                // 指定的是规则生效的文件
                test: /\.ts$/,
                use: [{
                    loader: 'babel-loader',
                    options: {
                        presets:
                            [['@babel/preset-env',
                                {
                                    // 要兼容的版本
                                    targets: {
                                        "chrome": "58",
                                        "ie": "11"
                                    },
                                    // corejs版本
                                    "corejs": "3",
                                    // 使用corejs的方式
                                    "useBuiltIns": "usage" /*按需加载*/
                                }
                            ]]
                    },
                }, 'ts-loader'], /*先使用后面的加载器，然后用前面的*/
                exclude: /node_modules/
            }
        ]
    }
}
```

## 类
```ts
class Person {
   // 实例属性
   readonly name: string = "xx";
   private _age: number = 10;
   // 静态属性
   static readonly country: string = "china";

   constructor(name: string, age: number) {
      this.name = name;
      this._age = age;
   }

   // 存取器
   get age(): number {
      return this._age;
   }

   set age(age: number) {
      this._age = age;
   }

}

class Student extends Person {
   constructor(name: string, age: number) {
      super(name, age);
   }
}

console.log(Person.country);
let person = new Person("xx", 20);
console.log(person.age);
// 静态字段可以被继承
console.log(Student.country)

class C {
   constructor(public x: number, public y: number) {
   }
}

// 和上面的C等价
class C1 {
   x: number;
   y: number;

   constructor(x: number, y: number) {
   }
}

abstract class Animal {
   name: string

   constructor(name: string) {
      this.name = name;
   }

   abstract eat(): void;
}

class Dog extends Animal {
   eat() {
      console.log("dog eat");
   }
}

// 下面两种定义对象类型的方式等效
interface PersonInterface {
   name: string;
   age: number;
}

// 可以重复声明，等效于两个加在一起
interface PersonInterface {
   // 属性不能有实际值
   ex: string;

   // 所有方法默认是抽象方法
   sayHello(): void;
}

type personType = {
   name: string;
   age: number;
}

const person0: PersonInterface = {
   name: "xx",
   age: 20,
   ex: "xx",
   sayHello() {
      console.log("hello");
   }
}

class PersonImpl implements PersonInterface {
   name: string;
   age: number;
   ex: string;

   sayHello() {
      console.log("hello");
   }
}

const person1: personType = {
   name: "xx",
   age: 20
}

// 函数类型
let func: (person: PersonInterface) => void;
func = function (person: PersonInterface) {
   console.log(person.name);
}

// 泛型
function fn<T>(arg: T): T {
   return arg;
}

function fn2<T extends Person, U>(arg1: T, arg2: U): T {
   return arg1;
}
```

## 补充
### `?`的用法
在 TypeScript (TS) 中，`?` 有多种用法，下面列出了它的主要应用：

1. **可选属性**：在接口、类或类型别名中标记某个属性为可选。

   `interface Person { name: string;   age?: number; }`

2. **可选参数**：在函数的参数列表中标记某个参数为可选。

   `function greet(name: string, greeting?: string) {   // ... }`

3. **可选链** (Optional Chaining)：允许在查询链的任何部分上进行可选访问。如果链中的某个部分为 `null` 或 `undefined`，则整个表达式的结果也会是 `null` 或 `undefined`。

   `let x = obj?.property?.subProperty;`

4. **空值合并运算符**：当你想给可能为 `null` 或 `undefined` 的值设定一个默认值时，可以使用 `??` 运算符。

   `let y = value ?? defaultValue;`

   注意：这虽然包含了 `?`，但它是一个双字符的运算符，和前面的用法不同。

5. **条件（三元）运算符**：这是 JavaScript (和多数 C 风格的编程语言) 中常见的，不特定于 TypeScript，但值得提及。

   `let z = condition ? valueIfTrue : valueIfFalse;`

6. **函数类型的可选参数**：在定义函数类型时也可以使用 `?` 来标记某个参数为可选。

   `type Callback = (data?: any) => void;`

总结，`?` 在 TypeScript 中主要用于标记可选属性和参数，而可选链是 TypeScript 3.7 新引入的特性，用于更安全地访问对象的属性。


### `!`的用法
#### 一元运算符

```ts
// ! 就是将之后的结果取反，比如：
// 当 isNumber(input) 为 True 时返回 False； isNumber(input) 为 False 时返回True
const a = !isNumber(input);
```

#### 成员

```ts
// 因为接口B里面name被定义为可空的值，但是实际情况是不为空的，那么我们就可以
// 通过在class里面使用！，重新强调了name这个不为空值
class A implemented B {
  name!: string
}

interface B {
  name?: string
}
```

#### 强制链式调用

```ts
// 这里 Error对象定义的stack是可选参数，如果这样写的话编译器会提示
// 出错 TS2532: Object is possibly 'undefined'.
new Error().stack.split('\n');

// 我们确信这个字段100%出现，那么就可以添加！，强调这个字段一定存在
new Error().stack!.split('\n');
```
