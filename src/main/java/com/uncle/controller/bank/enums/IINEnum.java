package com.uncle.controller.bank.enums;

import com.uncle.core.SysCode;
import com.uncle.core.UncleException;
import org.apache.commons.lang3.StringUtils;

/**
 * 银行转接清算机构枚举
 *
 * @author 杨戬
 * @className IINEnum
 * @email uncle.yeung.bo@gmail.com
 * @date 19-11-19 15:57
 */
public enum IINEnum {
    /**
     * 大来卡
     */
    DINERS_CLUB_INTERNATIONAL_IIN(new String[]{"300", "301", "302", "303", "304", "305", "2014", "2149", "36", "54", "55"},
            "大来卡"),
    /**
     * JBC卡
     */
    JAPAN_CREDIT_BUREAU_IIN(new String[]{"3528", "3589"},
            "JBC卡"),
    /**
     * 发现卡
     */
    DISCOVER_CARD_IIN(new String[]{"6011", "65", "644", "645", "646", "647", "648", "649", "622126", "622925"},
            "发现卡"),
    /**
     * 镭射卡，2007年起，曾发行镭射卡的金融机构开始改发维萨或万事达借记卡。2014年2月28日，镭射卡退出市场。
     */
    LASER_IIN(new String[]{"6304", "6706", "6771", "6709"},
            "镭射卡"),
    /**
     * 万事顺卡
     */
    MAESTRO_IIN(new String[]{"5018", "5020", "5038", "6304", "6759", "6761", "6762", "6763"},
            "万事顺卡"),
    /**
     * 美国运通卡
     */
    AMERICAN_EXPRESS_IIN(new String[]{"34", "37"},
            "美国运通卡"),
    /**
     * Solo卡,卡于2011年3月31日永久退出市场。
     */
    SOLO_IIN(new String[]{"6334", "6767"},
            "Solo卡"),
    /**
     * SWITCH卡,2002年被其所有者万事达与万事顺卡（英语：Maestro）品牌合并。
     * 由于在合并前，其在英国已经建立了强势的品牌，人们在其停止发行后数年仍以Switch代指借记卡交易。
     */
    SWITCH_IIN(new String[]{"4903", "4905", "4911", "4936", "564182", "633110", "6333", "6759"},
            "SWITCH卡"),
    /**
     * VISA卡
     */
    VISA_IIN(new String[]{"4"},
            "VISA卡"),
    /**
     * 万事达卡
     */
    MASTERCARD_INCORPORATED_IIN(new String[]{"51", "55"},
            "万事达卡"),
    /**
     * 中国银联卡
     */
    CHINA_UNIONPAY_IIN(new String[]{"62"},
            "中国银联卡"),
    /**
     * 其他卡
     */
    OTHER_IIN(new String[]{""},
            "其他卡"),
    ;
    private String[] codes;
    private String msg;

    IINEnum(String[] codes, String msg) {
        this.codes = codes;
        this.msg = msg;
    }


    public String[] getCodes() {
        return codes;
    }

    public String getMsg() {
        return msg;
    }

    /**
     * 根据银行卡号前六位IIN码获取银行转接清算机构
     *
     * @param bankCardPrSix 银行卡号前六位IIN码
     * @return 银行转接清算机构
     */
    public static IINEnum getIINEnumByBankCardPrSix(String bankCardPrSix) {
        //取出空白字符校验合法性
        String nonBlank = bankCardPrSix.replaceAll("\\s*", "");
        if (StringUtils.isBlank(nonBlank) || nonBlank.length() < 6) {
            throw new UncleException(SysCode.ERRORS);
        }

        //获得切分卡号
        String pr1String = nonBlank.substring(0, 1);
        String pr2String = nonBlank.substring(0, 2);
        String pr3String = nonBlank.substring(0, 3);
        String pr4String = nonBlank.substring(0, 4);
        String pr5String = nonBlank.substring(0, 5);

        //获取IIN
        for (IINEnum value : IINEnum.values()) {
            String[] codes = value.getCodes();

            //中国银联
            if (value.equals(IINEnum.CHINA_UNIONPAY_IIN)) {
                for (String code : codes) {
                    if (code.equals(pr2String)) {
                        return value;
                    }
                }

            }
            //大来卡
            if (value.equals(IINEnum.DINERS_CLUB_INTERNATIONAL_IIN)) {
                for (String code : codes) {
                    if (code.equals(pr3String)) {
                        return value;
                    }
                    if (code.equals(pr4String)) {
                        return value;
                    }
                    if (code.equals(pr2String)) {
                        return value;
                    }
                }
            }
            //JBC
            if (value.equals(IINEnum.JAPAN_CREDIT_BUREAU_IIN)) {
                int pr4IntegerMin = Integer.parseInt(value.getCodes()[0]);
                int pr4IntegerMax = Integer.parseInt(value.getCodes()[1]);
                int pr4Integer = Integer.parseInt(pr4String);
                if (isMaxMinRange(pr4IntegerMin, pr4IntegerMax, pr4Integer)) {
                    return value;
                }
            }
            //发现卡
            if (value.equals(IINEnum.DISCOVER_CARD_IIN)) {
                for (String code : codes) {
                    if (code.equals(pr2String)) {
                        return value;
                    }
                    if (code.equals(pr3String)) {
                        return value;
                    }
                    if (code.equals(pr4String)) {
                        return value;
                    }
                }
                int pr6IntegerMin = Integer.parseInt(value.getCodes()[8]);
                int pr6IntegerMax = Integer.parseInt(value.getCodes()[9]);
                int pr6Integer = Integer.parseInt(nonBlank);
                if (isMaxMinRange(pr6IntegerMin, pr6IntegerMax, pr6Integer)) {
                    return value;
                }
            }
            //万事顺
            if (value.equals(IINEnum.MAESTRO_IIN)) {
                for (String code : codes) {
                    if (code.equals(pr4String)) {
                        return value;
                    }
                }
            }
            //美国运通卡
            if (value.equals(IINEnum.AMERICAN_EXPRESS_IIN)) {
                for (String code : codes) {
                    if (code.equals(pr2String)) {
                        return value;
                    }
                }
            }
            //VISA卡
            if (value.equals(IINEnum.VISA_IIN)) {
                for (String code : codes) {
                    if (code.equals(pr1String)) {
                        return value;
                    }
                }
            }
            //万事达卡
            if (value.equals(IINEnum.MASTERCARD_INCORPORATED_IIN)) {
                int pr2IntegerMin = Integer.parseInt(value.getCodes()[0]);
                int pr2IntegerMax = Integer.parseInt(value.getCodes()[1]);
                int pr2Integer = Integer.parseInt(pr2String);
                if (isMaxMinRange(pr2IntegerMin, pr2IntegerMax, pr2Integer)) {
                    return value;
                }

            }
        }
        return IINEnum.OTHER_IIN;
    }


    private static boolean isMaxMinRange(int intMin, int intMax, int value) {
        return value >= intMin && value <= intMax;
    }
}
