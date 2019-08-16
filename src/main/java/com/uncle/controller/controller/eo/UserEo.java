package com.uncle.controller.controller.eo;

import com.uncle.core.BaseEntityObject;
import lombok.*;

/**
 * @author 杨戬
 * @className UserEo
 * @email uncle.yeung.bo@gmail.com
 * @date 2019/5/28 18:02
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserEo extends BaseEntityObject {
    /**
     * 账号
     */
    private String password;

    /**
     * 账号
     */
    private String username;

}
