## 窗口函数

```sql
#写法一:
SELECT RANK() OVER (PARTITION BY category_id ORDER BY price DESC) AS r, 
PERCENT_RANK() OVER (PARTITION BY category_id ORDER BY price DESC) AS pr, 
id, category_id, category, NAME, price, stock
FROM goods
WHERE category_id = 1;

#写法二:
SELECT RANK() OVER w AS r,
PERCENT_RANK() OVER w AS pr,
id, category_id, category, NAME, price, stock
FROM goods
WHERE category_id = 1 WINDOW w AS (PARTITION BY category_id ORDER BY price
DESC);
```

## 公用表达式

```sql
WITH CTE名称
AS (子查询) 
SELECT|DELETE|UPDATE 语句;
```

递归公用表表达式也是一种公用表表达式，只不过，除了普通公用表表达式的特点以外，它还有自己的

特点，就是 **可以调用自己** 。它的语法结构是

```sql
WITH RECURSIVE
CTE名称 AS (子查询) 
SELECT|DELETE|UPDATE 语句;
```