package com.uncle.controller.mongo.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author 杨戬
 * @className Man
 * @email yangb@chaosource.com
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

    private String id;
    private String name;
    private String age;
}
