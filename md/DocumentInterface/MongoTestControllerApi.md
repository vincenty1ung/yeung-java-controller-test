
# 
## Mongo-Test-增量man数据
**URL:** `http://172.16.19.194/mongo/save/man`

**Type:** `POST`

**Content-Type:** `application/json; charset=utf-8`

**Description:** Mongo-Test-增量man数据


**Request-parameters:**

Parameter|Type|Description|Required|Since
---|---|---|---|---
id|string|id|false|-
name|string|姓名|false|-
age|string|年龄|false|-
penisLength|int32|阴茎长度|false|-

**Request-example:**
```
{
	"id":"72",
	"name":"立辉.周",
	"age":"b7mdnq",
	"penisLength":919
}
```

**Response-example:**
```
This api return nothing.
```

## Mongo-Test-根据name查询man数据
**URL:** `http://172.16.19.194/mongo/select/man/name`

**Type:** `GET`

**Content-Type:** `application/x-www-form-urlencoded;charset=utf-8`

**Description:** Mongo-Test-根据name查询man数据


**Request-parameters:**

Parameter|Type|Description|Required|Since
---|---|---|---|---
name|string|名字|true|-

**Request-example:**
```
http://172.16.19.194/mongo/select/man/name?name=立辉.周
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
id|string|id|-
name|string|姓名|-
age|string|年龄|-
penisLength|int32|阴茎长度|-

**Response-example:**
```
{
	"id":"72",
	"name":"立辉.周",
	"age":"ebuy1h",
	"penisLength":890
}
```

## Mongo-Test-根据id查询man数据
**URL:** `http://172.16.19.194/mongo/select/man/id`

**Type:** `GET`

**Content-Type:** `application/x-www-form-urlencoded;charset=utf-8`

**Description:** Mongo-Test-根据id查询man数据


**Request-parameters:**

Parameter|Type|Description|Required|Since
---|---|---|---|---
id|string|名字|true|-

**Request-example:**
```
http://172.16.19.194/mongo/select/man/id?id=72
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
id|string|id|-
name|string|姓名|-
age|string|年龄|-
penisLength|int32|阴茎长度|-

**Response-example:**
```
{
	"id":"72",
	"name":"立辉.周",
	"age":"esvhzg",
	"penisLength":273
}
```

## Mongo-Test-获取man数据列表
**URL:** `http://172.16.19.194/mongo/list/man`

**Type:** `GET`

**Content-Type:** `application/x-www-form-urlencoded;charset=utf-8`

**Description:** Mongo-Test-获取man数据列表



**Request-example:**
```
http://172.16.19.194/mongo/list/man
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
id|string|id|-
name|string|姓名|-
age|string|年龄|-
penisLength|int32|阴茎长度|-

**Response-example:**
```
[
	{
		"id":"72",
		"name":"立辉.周",
		"age":"42ll7k",
		"penisLength":727
	}
]
```

