
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
username|string|姓名|false|-
age|string|年龄|false|-
password|string|密码|false|-
penisLength|int32|阴茎长度|false|-

**Request-example:**
```
{
	"id":"168",
	"name":"浩然.袁",
	"username":"浩然.袁",
	"age":"g65iuo",
	"password":"felmr1",
	"penisLength":572
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
http://172.16.19.194/mongo/select/man/name?name=浩然.袁
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
id|string|id|-
name|string|姓名|-
username|string|姓名|-
age|string|年龄|-
password|string|密码|-
penisLength|int32|阴茎长度|-

**Response-example:**
```
{
	"id":"168",
	"name":"浩然.袁",
	"username":"浩然.袁",
	"age":"qk76sb",
	"password":"u96kzd",
	"penisLength":561
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
http://172.16.19.194/mongo/select/man/id?id=168
```
**Response-fields:**

Field | Type|Description|Since
---|---|---|---
id|string|id|-
name|string|姓名|-
username|string|姓名|-
age|string|年龄|-
password|string|密码|-
penisLength|int32|阴茎长度|-

**Response-example:**
```
{
	"id":"168",
	"name":"浩然.袁",
	"username":"浩然.袁",
	"age":"k6bfmp",
	"password":"r1q6db",
	"penisLength":193
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
username|string|姓名|-
age|string|年龄|-
password|string|密码|-
penisLength|int32|阴茎长度|-

**Response-example:**
```
[
	{
		"id":"168",
		"name":"浩然.袁",
		"username":"浩然.袁",
		"age":"4n027v",
		"password":"ugwbbw",
		"penisLength":775
	}
]
```

