---
title: 利用python进行数据分析4（pandas数据处理进阶）
toc: true
date: 2020-02-17 18:19:09
tags: [python,pandas,数据分析]
categories:
- Python
- 第三方库
---

# 七、数据聚合和分组操作
<!--more-->
## 1.GroupBy机制

groupby的结果会自动过滤掉非数值列

![image-20190707151329772](_attachments/f7c9e6abf1180c0d8eff99d0d5c41e60.png)

```
####以自身的列作为键
grouped = df['data1'].groupby(df[key1])
grouped 是一个GroupBy对象
grouped.mean() 返回对应键的均值

grouped = df['data1'].groupby([df['key1'],df['key2']]).mean()  #多层

# 还可以直接使用列名进行分组
df['data1'].groupby('key1').mean()
df['data1'].groupby(['key1','key2']).mean()
```

![image-20190707153301322](_attachments/d9b92a309abcbdd7604127ec77344bb9.png)

分组的键还可以是正确长度的任何数组：

![image-20190707153502518](_attachments/92ef1a06ce69ebb0eda74a5b13cecc50.png)

`grouped.size()`返回一个包含组大小信息的Series。

### （1）遍历各分组

1.

![image-20190707154602162](_attachments/7f1106f1e17013ea221fd407b752cb02.png)

2.

![image-20190707154624660](_attachments/4ce9b7193633bb3687eaeff273e8903b.png)

3.

![image-20190707154809186](_attachments/c564571432f6d8fcaf1c2d20c5e17d26.png)

4.

​	axis=1 表示将每一列分给不同的组

​	默认axis=1表示将每一行分给不同的组

### （2）使用字典/Series分组

1.使用字典作为索引

![image-20190707155643473](_attachments/d92ecf52168c45bad3391739836b9b1d.png)

![image-20190707155600334](_attachments/462c6b0825a12d94072723c13d41c108.png)

![image-20190707155616480](_attachments/2f38a8c9010a1c3f5d9e15775560adfc.png)

2.使用Series作为索引

![image-20190707155729260](_attachments/51859bf0a3a1acc9a3ca7f91f7294480.png)

![image-20190707155745929](_attachments/77c263cebc715f825bd76a3996156fb6.png)

### （3）使用函数分组

将行索引作为调用函数的参数，以返回值作为分组依据

`df.groupby(len).sum()`

### （4）以索引层级进行分组

![image-20190707160358983](_attachments/a00f33c87eaf15ebfd44e64dcecbb029.png)

![image-20190707160412210](_attachments/47ba9d502f1d33b0c285ffe01a2df41d.png)

## 2.数据聚合

apply方法传入的是整个dataframe，而agg方法传入的是每一列数据

常用的聚合方法

| 函数名      | 描述                          |
| ----------- | ----------------------------- |
| count       | 非NA值的数量                  |
| sum         | 非NA值的和                    |
| mean        | 非NA值的均值                  |
| median      | 非NA值的中位数                |
| std，var    | 无偏的（分母n-1）标准差和方差 |
| min，max    | 非NA值的最值                  |
| prod        | 非NA值的乘积                  |
| first，last | 非NA值的第一个，最后一个      |

Series的方法生来就可以用于聚合

自定义聚合方法：

![image-20190707161508864](_attachments/8adafdaeae608364a28871a2673929e7.png)

同时使用多个聚合函数：传入函数名列表

​		`grouped.agg(['sum','std',peak_to_peak])`

使用多个聚合函数时的列名难以辨认：可以通过传入元组列表（name，function）

​		`grouped.agg([('mean','foo')])`	

对不同的列使用不同的聚合函数：传入字典列表{列名：函数名}

​	`grouped.agg({'tip':np.max})`

​	`grouped.agg({'tip':[np.max,'sum']})`

返回不含行索引的聚合数据：

​		`df.groupby('key1',as_index=False)`

也可以将结果进行reset_index()获得，但as_index方法可以避免不必要的计算

## 3.应用（split-apply-combine）

对分组后的df采用apply（）方法，函数必须返回标量值或pandas对象

![image-20190707164001487](_attachments/a3d8cd528bcc7c5f605f5126c37aeb3f.png)

![image-20190707164047780](_attachments/666ec555ad230fa1437c10177f1524ea.png)

apply方法可以传入关键字参数：

![image-20190707164145159](_attachments/270739c16b6de769a5ba74ef5b033eed.png)

### （1）压缩分组键

不显示分组的键：

`df.groupby('key1',group_keys=False)`

### （2）分位数与桶分析

cut后的对象可直接传入groupby方法。

![image-20190707165346518](_attachments/475eeccb57c04f40e0fffa455ddd1d60.png)

![image-20190707165403700](_attachments/16d671d16df1df0c500fe2c3f8bd1a01.png)

![image-20190707165456115](_attachments/86f31f75e0886e5527ef8c3f1eac5a9a.png)

## 4.数据透视表与交叉表

`df.pivot_table(index=['day','smoker'])`

如果只想在tip_pct和size上聚合，并根据time分组。把day放入行，smoker放入列。

`df.pivot_table(['tip_pct','size'],index=['time','day'],columns='smoker')`

添加margins=True会添加All行和列表标签，这会返回单层中所有数据的分组统计值。All为均值

可以添加aggfunc参数：

![image-20190707172634853](_attachments/d061ce1db8356cfaef47d67f679db684.png)

参数：

![image-20190707172657002](_attachments/9cb6056bd2622fb5fb52021f2c501ba1.png)

### **（1）交叉表**

计算的是分组中的数据出现的次数。

`pd.crosstab([df.time,df.dat],tips.smoker,margins=True)`

参数依次为：index，columms，





# 八、时间序列

`pd.read_csv(filename,parse_dates=True,index_col=0)`

### 1.日期和时间数据的类型及工具

常用的python库：`time,datetime,calendar`

![image-20190707190118479](_attachments/b55f07d58bb44a556907128dc00fda13.png)

### (1)字符串与datetime的转化

str方法和strftime方法

![image-20190707190313148](_attachments/d8649f5e2dd9172825b0b95102e8d194.png)

datetime格式

![image-20190707190438413](_attachments/cfa2b77d147cccce9ea3746ff57e0069.png)

![image-20190707190453233](_attachments/dca4d45d2f925ab9b40c3888ae89bce9.png)

使用相同的方法可以将str转换为datetime格式

![image-20190707190558121](_attachments/40df312ea6c0ff571681f8de279f1c77.png)

dateutil库能够自动解析日期并转换为datetime格式（日期在月份前可以使用dayfirst=True参数）：

![image-20190707190808858](_attachments/3140215da5e4e72668e76d04c5b97381.png)

pandas中的日期：

![image-20190707191141642](_attachments/d7501496da4fffbd2c11e7577081532c.png)

## 2.时间序列基础

不同索引的时间序列之间的算术运算在日期上自动对齐

![image-20190707191649370](_attachments/33ec576a57e263d2e6bad48882858a57.png)

### （1）索引、选择、子集

索引：

1）基于标签索引

![image-20190707191748664](_attachments/e1647f77f19af718b7d91a31ad96a6c3.png)

2）使用能解释为日期的字符串

![image-20190707191838407](_attachments/e8a82f199a55c6a940f27dfd922c0bbd.png)

3）使用切片：通过传递年份或年份加月份（可以使用不在索引中的时间戳进行切片）

![image-20190707191932392](_attachments/c1417c421d567e0d096a502668c1a3fb.png)

![image-20190707191957091](_attachments/8d851ea9e76cf2eb77c3ef7e191e45e6.png)

​	使用truncate切片：

![image-20190707192308408](_attachments/1095f13e0ac86ec848cf02235c14757d.png)

## 3.日期范围、频率和移位

### （1）生成日期范围

```
pd.date_range('2012-04-01','2012-06-01')  #默认以天为间隔
pd.date_range(start='2012-04-01',periods=20)  #生成20个时间
pd.date_range(end='2012-04-01',periods=20)  #生成20个时间
pd.date_range('2000-01-01','2000-12-01',freq='BM') #在区间内按照频率生成
pd.date_range('2012-05-06 12:56:31',periods=5,normalize=True) #生成的列表中不包含时刻，只有日期
```

![image-20190707193307614](_attachments/755ffe7713782aef05fca84f1ee460cb.png)

### （2）频率和日期offset

`pandas.tseries.offsets`中有Hour，Minute等对象，大多数情况下不需要主动创建

可以传递的freq参数：‘4h‘，’1h30min‘

![image-20190707193811689](_attachments/797a83c3cbd901ba4522b95cb733aa07.png)

offset可以通过加法结合：

![image-20190707193847245](_attachments/9168e47eab077fd273622fcc44e24b19.png)

### (3)移位

将时间戳对应的数据前移或后移。

![image-20190707194308970](_attachments/35f832de11347e74961ef3a3e708fada.png)

指定freq后，将会改变时间戳，而不会改变数据

![image-20190707194657856](_attachments/c11d775330413e8a69fb5347de8a6cbf.png)

使用offset：	![image-20190707195000813](_attachments/1f7dce2ffff5e1d588f179ad55a8729e.png)

![image-20190707195032012](_attachments/ebb21910f7779d9cbfced028aedf86cc.png)

rowback和rollforward：

![image-20190707195118522](_attachments/d0672eff00073b1d4d219216255aa7b8.png)

offset与groupby结合

![image-20190707195140170](_attachments/87e6607ade5e9e6ef332d68752c04400.png)

## 4.时区处理

### （1）时区的本地化和转换

```
df.index.tz      查看当前的index的时区属性
生成时直接设定
rng = pd.data_range('2012-01-01 12:01',period=10,tz='UTC')
ts = pd.Series(np.random.randn(len(rng)),index=rng)

将时区转化为本地化时区
ts.tz_localize('UTC')
ts_shanghai = ts.tz_localize('Asia/Shanghai')
ts_shanghai.tz_convert('UTC')
```

### (2)时区感知时间戳对象的操作

时间戳对象也可直接进行时区操作

![image-20190707200326507](_attachments/c0ae12db1436fc0b26c84b61b1f79648.png)

![image-20190707200340558](_attachments/53472267444c7f968c91a5904ee96cf7.png)

### （3）不同时区间的操作

两个时区不同的时间序列联合时，结果为UTC时间

## 5.时间区间和区间算数

```
pandas区间：
p = pd.Period(2007,freq='A-DEC')
p+2
p-5
p - pd.Period(2005,freq='A-DEC')  # 2

pandas区间序列：
pd.period_range('2000-01-01','2000-06-30',freq='M')
使用数组生成区间序列：
pd.PeriodIndex(['200103','200104'],freq='Q-DEC')
pd.PeriodIndex(year=yearlist,quarter=quaterlist,freq='Q-DEC')

区间频率的转换
p.asfreq('M',how='start')  # 低频率向高频率转换
# 高频率向低频率转换时，直接根据自区间所属决定父区间

# 时间戳转化为区间
pts = ts.to_period('M')  #转换的频率可以不指定
# 区间转换为时间戳
ts = pts.to_timestamp(how='end')

# 从数组生成PeriodIndex
```

## 6.重采样

从低频率转换到高频率：向上采样

从高频率转换到低频率：向下采样

resample方法的参数：

![image-20190707203721264](_attachments/f0f868fb90d024136f8c9ca232ef33c7.png)

### （1）下采样

![image-20190707204059651](_attachments/2f25e4537fb659c04fb8672f9bd6efe2.png)

### （2）开高低收

`ts.resample('5min').ohlc()`

### (3)向上采样与差值

使用asfreq()在不聚合的情况下转到高频率

`df.resample('D').asfreq()`

差值或向前填充

`df.resample('D').ffill()`

### (4)使用时间区间进行重采样

由于区间涉及时间范围：

- 在向下采样中，目标频率必须是原频率的子区间
- 在向上采样中，目标频率必须是原频率的父区间

## 7.移动窗口函数

```
df.COL1.rolling(250,min_periods=10).mean()
df.COL1.expanding().mean()
df.COL1.ewm(span=30).mean() #指数加权函数，给近期的观测值更高的权重,span为跨度

二元移动窗口
pd.COL2.rolling(125,min_period=10).corr(pd.COL1)
pd.rolling(125,min_period=10).corr(pd.COL1)

自定义窗口函数只要输入为一个序列返回一个单值即可
```





# 九、高阶Pandas

## 1.分类数据

### （1）Categorical对象

![image-20190707210848754](_attachments/102d0464ff5e0298fa54a7b26d7ab346.png)

Series转换为category类型的数据

![image-20190707210900725](_attachments/d05bbaeb56f19f5baa1dc112a6604835.png)

category.value对象有两个属性：codes和categories。

![image-20190707211003754](_attachments/8b35f04384cc1dc03d2075db4e6f8683.png)

![image-20190707211015462](_attachments/318044ffafbf28b45c37af55c73c6c15.png)

可以通过python序列直接创建pandas.Categorical对象

![image-20190707211320833](_attachments/e5f32b4f523aa4efb9c4f75241e24bf9.png)

通过codes和categories构建

![image-20190707211422094](_attachments/ebb291a20c51bc75bf6fa875faf38e62.png)

访问category对象的分类方法(通过cat)：

```
df1.cat.codes
df1.cat.categories
df2.cat.set_categories(['a','b','c'])
```

Categories方法

![image-20190707212549393](_attachments/f94d75de3f94dfbbf9c68e77f5ab610a.png)

```python
pd.Categorical(df['thal']).cat.codes  #Categorial对应的index值
```



## 2.GroupBy高阶应用

### （1）分组转换和展开Groupby

```
df = pd.DataFrame({'key':['a','b','c']*4,'value':np.arange(12)})
gdf = df.groupby('key').value  #返回的是Series对象
gdf.mean()  #	每一组的平均值
gdf.transform('mean')  #将每一组的平均值赋值给该组每一个元素，然后返回
gdf.transform(lambda x:x.mean())
```

### （2）方法链技术

方法链可以减少中间变量

两种等价的赋值方式

![image-20190707214825897](_attachments/1177e207626edea1057f75a291fee553.png)

### （3）pipe方法

两种等价形式

![image-20190707215347390](_attachments/8c4acbd97641037a405d6ae0ab074cf6.png)

![image-20190707215409912](_attachments/4166a6c19e68f62c6188242dfc8d51ef.png)



## 十、Python建模库

将pandas的DataFrame对象转换为numpy数组：df1.value



Patsy库

Statsmodels库

scikit-learn库
