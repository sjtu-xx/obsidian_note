---
title: mabatis批量更新数据库数据
date: 2021-07-04 18:01:27
tags:
- Mybatis
categories:
---

Mybatis中实现批量更新的三种方式
<!--more-->
## 批量更新
### 方法1：在mapper中加入foreach方法
```xml
<update id="updateBatch"  parameterType="java.util.List">  
    <foreach collection="list" item="item" index="index" open="" close="" separator=";">
        update tableName
        <set>
            name=${item.name},
            name2=${item.name2}
        </set>
        where id = ${item.id}
    </foreach>      
</update>
```

> 默认情况下mybatis不支持多条以`;`结尾的语句，需要在链接数据库的URL配置中添加`& allowmultiqueries = true`

### 方法2：在mapper中加入(效率低，不推荐)

```xml
<update id="updateBatch" parameterType="java.util.List">
        update tableName
        <trim prefix="set" suffixOverrides=",">
            <trim prefix="c_name =case" suffix="end,">
                <foreach collection="list" item="cus">
                    <if test="cus.name!=null">
                        when id=#{cus.id} then #{cus.name}
                    </if>
                </foreach>
            </trim>
            <trim prefix="c_age =case" suffix="end,">
                <foreach collection="list" item="cus">
                    <if test="cus.age!=null">
                        when id=#{cus.id} then #{cus.age}
                    </if>
                </foreach>
            </trim>
        </trim>
        <where>
            <foreach collection="list" separator="or" item="cus">
                id = #{cus.id}
            </foreach>
        </where>
</update>
```

这种方法不需要修改url

### 方法3：使用sqlsessionfactory临时实现batch提交（推荐）

https://stackoverflow.com/questions/23486547/mybatis-batch-insert-update-for-oracle

```java
public int updateBatch(List<Object> list){
        if(list ==null || list.size() <= 0){
            return -1;
        }
        SqlSessionFactory sqlSessionFactory = SpringContextUtil.getBean("sqlSessionFactory");
        SqlSession sqlSession = null;
        try {
            sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH,false); // disable autocommit
            Mapper mapper = sqlSession.getMapper(Mapper.class);
            Int batchcount = 1000; // submit the quantity when it reaches this quantity
            for (int index = 0; index < list.size(); index++) {
                Object obj = list.get(index);
                mapper.updateInfo(obj);
                if(index != 0 && index%batchCount == 0){
                    sqlSession.commit();
                }                    
            }
            sqlSession.commit();
            return 0;
        }catch (Exception e){
            sqlSession.rollback();
            return -2;
        }finally {
            if(sqlSession != null){
                sqlSession.close();
            }
        }
        
}
```

> 方法1：需要修改URL，不安全（就是无数个update语句，只是mybatis拼接出来，效率低下）
>
> 方法2：当数据量大时，效率低（一个sql怼到DB，数据量大易锁表）
>
> 方法3：需要自己控制和处理。难以发现隐藏问题。
>
> 除了方法2不推荐外，其他方法都有人使用
>
> https://segmentfault.com/a/1190000021290975这个地方对比了几种不同的插入方法的执行效率，可供参考。

建议使用java batchinsert，因为他调用的数据库驱动的batchinsert接口，选择合适的batch大小以及添加`rewriteBatchedStatements=true`都可能提高执行效率。(https://stackoverflow.com/questions/56034380/performance-comparison-between-mybaits-batch-executortype-and-for-each-xml/56040127#56040127)







