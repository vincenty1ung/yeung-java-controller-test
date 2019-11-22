package com.uncle.controller.bank.test;


import com.uncle.controller.bank.enums.PayTypeEnum;

import java.util.EnumSet;

/**
 * @author 杨戬
 * @className demo
 * @email yangb@chaosource.com
 * @date 19-11-21 11:42
 */
public class PaytypeEnumTest {
    public static void main(String[] args) {

        String[] array = {"622254564846541654",
                "54546846123514",
                "352848845465654",
                "601148845465654",
                "629548845465654",
                "51158845465654",
                "4564842135888"
        };
        int index = 1;
        for (String s : array) {
            System.out.print(index + ".卡号：" + s);
            PayTypeEnum.IINEnum iinEnumByBankCardPrSix = PayTypeEnum.getIINEnumByBankCardPrSix(s);
            System.out.println(" 归属卡组织：" + iinEnumByBankCardPrSix.getMsg());
            index++;
        }
        System.out.println("=========================================");
        EnumSet<?> universe = PayTypeEnum.CARD_PAY.getUniverse();
        for (Enum<?> anEnum : universe) {
            System.out.println("anEnum.name() = " + anEnum.name());
        }
    }
}
