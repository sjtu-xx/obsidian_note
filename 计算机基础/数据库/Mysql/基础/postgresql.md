## json
jsonb  indexing
[jsontype](https://www.postgresql.org/docs/current/datatype-json.html#JSON-INDEXING)
[PostgreSQL: Documentation: 15: 9.16. JSON Functions and Operators](https://www.postgresql.org/docs/current/functions-json.html#FUNCTIONS-JSONB-OP-TABLE)
`jsonb_path_ops` does not support the key-exists operators, but it does support `@>`, `@?` and `@@`.
```sql
`'{"a":1, "b":2}'::jsonb @> '{"b":2}'::jsonb` → `t`

```
