
### 常用语句
```http
# 索引-查所有
GET /_cat/indices?v

# 索引-查一个
GET /test

# 索引-删除
DELETE /test

# 索引-创建
PUT /test2

# 文档-插入
POST /test/_doc/1
{"name":1}

# 文档-查询
GET /test/_doc/1

# 文档-全量查询
GET /test/_search

# 文档-更新
PUT /test/_doc/1
{"name":2}

POST /test/_update/1
{
  "doc":{
    "name": 3
  }
}

# 文档-删除
DELETE /test/_doc/1

# 查询
GET /test/_search?q=name:1

# 匹配值中的任意一个单词（全文检索）
GET /bank/_search
{
  "query": { "match": { "address": "mill lane" } }
}

# 返回所有
GET /bank/_search
{
  "query": {
    "match_all" : {}
  }
}

# 匹配值中的一个完整的字符串（完全匹配）
GET /bank/_search
{
  "query": { "match_phrase": { "address": "mill lane" } }
}

# 分页查询
GET /bank/_search
{
  "query": {
    "match_all" : {}
  },
  "from": 0,
  "size": 5
}

# 查询指定字段
GET /bank/_search
{
  "query": {
    "match_all" : {}
  },
  "_source": ["city"]
}

# 排序
GET /bank/_search
{
  "query": {
    "match_all" : {}
  },
  "_source": ["city"],
  "sort":{
    "age": {
      "order": "desc"
    }
  }
}

# 多条件查询
# and
GET /bank/_search
{
  "query": {
    "bool": {
      "must": [
        {"match":
          {"age":20}
        },
        {"match":
          {"gender": "F"}
        }
      ]
    }
  }
}

# or(假的或，联合时不满足也可以)
GET /bank/_search
{
  "query": {
    "bool": {
      "should": [
        {"match":
          {"age":20}
        },
        {"match":
          {"gender": "F"}
        }
      ]
    }
  }
}

# 范围查询
GET /bank/_search
{
  "query": {
    "bool": {
      "must": [
        {"match":
          {"age":20}
        },
        {"match":
          {"gender": "F"}
        }
      ],
      "filter": {
        "range":{
          "age":{
            "gt":25
          }
        }
      }
    }
  }
}

# 聚合操作
GET /bank/_search
{
  "aggs": {
    "group_age": {
      "terms": { //分组
        "field": "age"
      }
    }
  },
  "size": 0 //不显示原始数据
}

GET /bank/_search
{
  "aggs": {
    "group_age": {
      "avg": {
        "field": "age"
      }
    }
  }
}


# mappings
PUT /users/_mapping
{
  "properties": {
    "name": {
      "type": "text", // 可以分词
      "index": true
    },
    "sex": {
      "type": "keyword",
      "index": true
    },
    "tel": {
      "type": "keyword",
      "index": false
    }
  }
}
```
