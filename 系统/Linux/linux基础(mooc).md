# linux基础(mooc)

# 常用指令

## grep

`grep <mode> <filename_list>`

> egrep：支持扩展正则表达式
fgrep：快速搜索指定字符串
标准正则表达式的元字符：`.*[\^$`
> 

参数：

| -n | 显示行号 |
| --- | --- |
| -v | 反向选择，输出不包含模式的行 |
| -i | 忽略大小写 |

## sed

流操作：常用于正则替换

`sed <command> <filenamelist>`

`sed -e <command1> -e <command2> <filenamelist>`

`sed -f <command file> <filename_list>`

```bash
tail -f asdf.log | sed 's/11235\.3\.23\.26/桥西/g'
tail -f asdf.log | sed -f sed.cmd

# 也可以使用正则表达式中的group，\0表示整个表达式匹配结果，\1表示第一个group
# 将日期格式进行更改03-25-1997替换为1997.03.25
cat asf.log | sed 's/\([0-9][0-9]\)-\([0-9][0-9]\)-\([0-9][0-9]*\)/\3.\1.\2/g'
```

<aside>
💡 sed中的正则表达式表示分组的括号需要用转义字符进行转换！！！

</aside>

## awk

遍历每一行，满足条件执行操作

`awk <command> filenamelist`

`awk -f <commandfile> <filename_list>`

### **command的格式**

 `condition {action}`

**condition：**

- 关系算符（<=.>=等）和条件算符（&&，||等）
- 正则表达式匹配（使用```符号包围表达式）
- 特殊的条件
    
    - 不指定任何条件，对所有文本行执行操作
    
    - BEGIN 开始处理所有文本之前执行操作
    
    - END 结束处理所有文本之后执行操作
    

**action**

- 自定义变量
- 加减乘除运算
- 正则表达式匹配运算符（`~`匹配， `!~`不匹配） `$2 ~ "[1-9][0-9]*"`表示第二个域与后面的模式匹配
- 流程控制与C语言类似if，for
- 打印
    
    - print 变量1，变量2
    
    - printf("格式串",变量1,变量2)
    
- 允许多段程序：多段程序用空格或分号分开
- 变量`NR`表示行号
- 每行用空格分开的部分叫做记录的“域”
- `$1`是第一个域的内容`$0`是整行内容

```bash
# 打印第二列的字符
ps -ef | awk '/guest/{ printf("%s ",$2); }'

# 打印行和行号
awk‘'{printf("%d: %s\n", NR, $0);}'

# awk file
/播报/ {date=$1;}
/查看/ && ~/暂无/ {
for (i=0;i<9;i++)
printf("%s,%s %d:00,%s\n", $1, date, 9+i, $(2+i*2));
}

统计书的总册数
cat 1.txt | iconv -f GBK -t UTF-8 | sed -e 's/.*[^0-9]\([0-9][0-9]*\)多万.*/\1/g' | awk '/^[0-9][0-9]*$/{sum+=$1}; END {print sum}'

统计词频最高的200个单词
head -n 2000 zte-rd-aegis-envdc_app.log | tr '[A-Z]' '[a-z]'|tr ',[]\047"()-.=|:\<>{}\011$? ' ' '| tr ' \011' '\012' | grep -v '^ *$' | sort | uniq -c | sort -n | tail -n 80

查看单引号的八进制标识
echo \' | od -t o1
```

## 文件比较

- cmp
    
    `cmp file1 file2`逐个字节比较
    
    显示不同的第一个字节，相同时无提示
    
- md5
    
    `md5sum/sha1sm/sha512sum`计算hash值（md5：16个字节，sha1：20个字节），判断文件内容是否相同
    
- diff
    
    `diff file1 file2`
    

<aside>
💡 `--`标识命令行参数的结束
# 筛选 -1          grep -- -1

# 删除-1文件.    rm -- -1

</aside>

- cp
    
    `cp -u` 增量拷贝（只拷贝时间戳更新的文件）
    
    `rsync远程同步`（将文件分块，只是将文件之间的差异进行同步）
    

### 目录遍历

```bash
# 将两个文件夹中的c文件找到，打印出来
# 必须使用单引号，否则会将文件通配符自动解析
# -name 文件名与正则匹配
# -regex 整个文件路径与正则匹配
find ver1.d ver2.d -name '*.c' -print

# 在当前目录查找  不比.vimrc新，且名称符合正则表达式的文件，并且打印出文件中1所在的文件和行号
# grep指定多个文件时，会显示匹配的路径
# /dev/null 为文件黑洞
# 分号需要转义
find . ! -newer .vimrc \( -name '*1.txt' \) -exec grep -n -- 1 {} \dev\null \; -print \; -exec ls -l {} \;
```

- xargs
    
    xargs将前一个命令返回的结果作为参数传给第二个参数，可以使用-n指定每次传入多少个参数
    
    find . ! -newer .vimrc \( -name '*1.txt' \) -print | xargs grep -n -- 1
    
    # 相比上面的命令，因为要启动多次grep，导致速度差异很大
    
    find . ! -newer .vimrc \( -name '*1.txt' \) -exec grep -n -- 1 {};
    
- tar
    - c 创建
    - t 列出文件名
    - x 提取
    - v 回显处理的文件
    - z 使用gzip压缩
    - j 使用bzip2压缩
    
    ```bash
    # 将当前目录下的文件压缩到rct0
    tar cvf /dev/rct0 .
    
    # 查看rct0下的文件
    tar tvf /dev/rct0
    
    # 解压
    tar xvf /dev/rct0
    
    # 压缩时间短，压缩率稍低
    gzip
    gunzip
    
    # 压缩时间长，但压缩率高
    bzip2
    bunzip2
    ```
    

# bash

### 环境变量

```bash
# 临时环境变量
A=123

# 持久环境信息
export A = 123
```

### 解释程序

- 默认解释程序为`/bin/sh`
- 可以在文件的第一行自行指定解释程序，指定方法： `#! /bin/sh`
- `#!`必须是文件首先出现的两个字符

### 文件权限控制

`chmod [ugoa][+-=][rwxst] file`

- u(user)，g(group)，o(other group), a(all)
- rwx,s(SUID),t(sticky)
- SUID控制部分权限

### shell

shell在操作系统看来就是一个命令解释器

常见的shell：B-shell、C-shell、K-shell、bash

**bash的启动的三种方式**

- 注册shell：登录时自动执行`~/.bash_profile`，退出时，自动执行`.bash_logout`
- 交互式shell：` ./bash`。启动时，自动执行`.bashrc`
- 脚本解释器
- 系统级：
    - 注册shell，自动执行`/etc/profile`，交互式shell`/etc/bash.bashrc`，注册shell退出`/etc/bash.bash.logout`,先于用户配置执行

### shell脚本

```bash
# lsdir
if [ $# = 0]
then
	dir=.
else
	dir=$1
fi
find $dir -type d -print
echo '-------'
cd $dir
pwd
```

### 脚本文件执行的几种方式

- 新建子进程，在子进程中执行脚本
    - # 无法携带命令行参数
        
        `bash < lsdir`
        
        `bash lsdir`
        
    - # 给文件设置可执行属性
        
        `chmod u+x lsdir`
        
        `./lsdir /usr/lib/gcc`
        
- 在当前shell中执行
    - `. lsdir /usr/lib/gcc`
    - `source lsdir /usr/lib/gcc`

### 历史和别名

```bash
!! 引用上一条记录、
!str 以str开头的最近使用过的命令
alias 查看所有别名
alias p = 'ping 202.14.214.24'
```

### 重定向

```bash
<
# 用于变量替换
<<word 从shell脚本文件获取数据直到再次遇到定界符word, word两边加单引号，就不会替换
<<<word 将word作为命令的标准输入
> filename   # stdout重定向到file中
>> filename  # stdout追加到file中
2> filename # 重定向错误输出
2>&1

`命令替换，将date结果替换
`date`
```

### bash变量

- 所有变量都是字符串
- 第一个字符必须是字母或下划线
- 变量赋值时等号两边不能有空格

```bash
addr=20.1.1.254

# 引用变量
$addr 或 ${addr}

# 如果右侧字符串含有特殊字符，使用双引号
unit="Sh Univer"

# 引用未定义的变量，变量值为空字符串
set -u # 当引用未定义变量时，返回一个错误
set +u # 当引用未定义变量时，返回空字符串
```

```bash
echo arg1 arg2
echo（linux必须加-e 进行转义）可以用于打印不可见字符如 \b退格 \n换行 \r回车 \t水平制表 \\反斜线 \nnn \c打印完不换行
printf '\033[HConnect to $s' $proto

read name 从标准输入读入，放入name中
```

### 环境变量

- HOME: 用户主目录的路径
- PATH=/bin:/usr/bin:/etc  # 依次查找每一个目录查找
- TERM： 终端类型
- set 列出当前的所有变量的值、函数的定义
- env 列出环境变量
- proto=abc
    
    export proto  将proto变成环境变量
    

### 替换

- `ls *.c`  # 参数替换
- `ls $HOME` # 变量替换
- `now=`date`` # 命令替换
- `now=$(date)` # 与``类似
- 位置参数
    - `$0` 脚本文件本身的名字
    - `$1` 命令行的第一个参数
    - `$#` 命令行参数的个数
    - `"$*"` 等同于"$1 $2 $3 ..."  # 整个作为一个参数
    - `"$@"` 等同于"$1" "$2" ... 用于把变长的命令行操作传递给其他命令
    - shift 将位置参数左移，`$2`变成`$1`，`$#`减一

### 元字符

- 空格和制表符： 命令行参数的分隔符
- 回车： 执行键入的命令
- `><|`： 重定向与管道
- `;`： 一行内输入多个命令
- `&`： 后台运行
- `&&`：
- `$`：引用变量
- ```：变量替换
- `*[]?`：通配符
- `\`：转义
- `()`：在shell中执行一组命令
- 双引号：除了```和`$`之外的特殊字符都被取消 
- 单引号：对所有字符都不做特殊解释（单引号中不能在用单引号）


### 感叹号
`!!` 再次执行上一条命令
`!-i` 的形式可以执行倒数第 i 条命令。如 `!-6`，`!-8`等。特别地，`!-1` 就相当于 `!!` 。
`!cmd` 通过关键词来执行历史命令
`!$` 和 `!^` 也是很常用的，它们的作用是重复上一条命令的第一个或最后一个参数。
`!:-`去掉最后一个参数执行上一个命令
`![命令名]:[参数号]`:命令对应指令的第i个参数
逻辑非的作用
`rm !(*.cfg)` 删除除了cfg结尾以外的所有文件


