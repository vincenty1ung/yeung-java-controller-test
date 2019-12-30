package com.uncle.controller.mongo.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author 杨戬
 * @className Man
 * @email uncle.yeung.bo@gmail.com
 * @date 19-10-17 11:17
 */
@Getter
@Setter
@ToString
public class Man {

    /**
     * _id : {"$oid":"5da7dc144292181ae9a12923"}
     * id : 3
     * name : chenlomh
     * age : 18
     */

    /**
     * id
     */
    private String id;
    /**
     * 姓名
     */
    private String name;
    /**
     * 年龄
     */
    private String age;
    /**
     * 阴茎长度
     */
    private Integer penisLength;
}
