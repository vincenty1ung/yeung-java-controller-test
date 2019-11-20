package com.uncle.controller.bank.test;

import com.uncle.controller.bank.enums.IINEnum;

/**
 * 获取银行转接清算机构
 *
 * @author 杨戬
 * @className IINEnumTest
 * @email uncle.yeung.bo@gmail.com
 * @date 19-11-20 17:20
 */
public class IINEnumTest {
    public static void main(String[] args) {
        IINEnum IINEnumByBankCard = IINEnum.getIINEnumByBankCardPrSix("456488");
        System.out.println("银行转接清算机构名称：" + IINEnumByBankCard.getMsg());
        System.out.println("银行转接清算机构代码：" + IINEnumByBankCard.name());

    }
}
