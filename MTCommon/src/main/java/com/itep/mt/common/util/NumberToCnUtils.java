package com.itep.mt.common.util;

import java.math.BigDecimal;

/**
 * 数字转换为汉语中人民币的大写<br>
 *
 * @author hongten
 * @contact hongtenzone@foxmail.com
 * @create 2013-08-13
 */
public class NumberToCnUtils {
    /**
     * 汉语中数字大写
     */
    private static final String[] CN_UPPER_NUMBER = {"零", "壹", "贰", "叁", "肆",
            "伍", "六", "柒", "捌", "玖"};
    /**
     * 汉语中货币单位大写，这样的设计类似于占位符
     */
    private static final String[] CN_UPPER_MONETRAY_UNIT = {"分", "角", "元",
            "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "兆", "拾",
            "佰", "仟"};
    /**
     * 特殊字符：整
     */
    private static final String CN_FULL = "";
    /**
     * 特殊字符：负
     */
    private static final String CN_NEGATIVE = "负";
    /**
     * 金额的精度，默认值为2
     */
    private static final int MONEY_PRECISION = 2;
    /**
     * 特殊字符：零元整
     */
    private static final String CN_ZEOR_FULL = "零元" + CN_FULL;

    /**
     * 把输入的金额转换为汉语中人民币的大写
     * <p>
     * 输入的金额
     *
     * @return 对应的汉语大写
     */
    public static String number2CNMontrayUnit(String numberOfMoneyStr) {
        BigDecimal numberOfMoney = new BigDecimal(Double.parseDouble(numberOfMoneyStr));
        StringBuffer sb = new StringBuffer();
        // -1, 0, or 1 as the value of this BigDecimal is negative, zero, or
        // positive.
        int signum = numberOfMoney.signum();
        // 零元整的情况
        if (signum == 0) {
            return CN_ZEOR_FULL;
        }
        //这里会进行金额的四舍五入
        long number = numberOfMoney.movePointRight(MONEY_PRECISION)
                .setScale(0, 4).abs().longValue();
        // 得到小数点后两位值
        long scale = number % 100;
        int numUnit = 0;
        int numIndex = 0;
        boolean getZero = false;
        // 判断最后两位数，一共有四中情况：00 = 0, 01 = 1, 10, 11
        if (!(scale > 0)) {
            numIndex = 2;
            number = number / 100;
            getZero = true;
        }
        if ((scale > 0) && (!(scale % 10 > 0))) {
            numIndex = 1;
            number = number / 10;
            getZero = true;
        }
        int zeroSize = 0;
        while (true) {
            if (number <= 0) {
                break;
            }
            // 每次获取到最后一个数
            numUnit = (int) (number % 10);
            if (numUnit > 0) {
                if ((numIndex == 9) && (zeroSize >= 3)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[6]);
                }
                if ((numIndex == 13) && (zeroSize >= 3)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[10]);
                }
                sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                sb.insert(0, CN_UPPER_NUMBER[numUnit]);
                getZero = false;
                zeroSize = 0;
            } else {
                ++zeroSize;
                if (!(getZero)) {
                    sb.insert(0, CN_UPPER_NUMBER[numUnit]);
                }
                if (numIndex == 2) {
                    if (number > 0) {
                        sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                    }
                } else if (((numIndex - 2) % 4 == 0) && (number % 1000 > 0)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                }
                getZero = true;
            }
            // 让number每次都去掉最后一个数
            number = number / 10;
            ++numIndex;
        }
        // 如果signum == -1，则说明输入的数字为负数，就在最前面追加特殊字符：负
        if (signum == -1) {
            sb.insert(0, CN_NEGATIVE);
        }
        // 输入的数字小数点后两位为"00"的情况，则要在最后追加特殊字符：整
        if (!(scale > 0)) {
            sb.append(CN_FULL);
        }

        String buffer = sb.toString();
        for (int i = 0; i < CN_UPPER_NUMBER.length; i++)
            buffer = buffer.replace("元零" + CN_UPPER_NUMBER[i] + "角", "元" + CN_UPPER_NUMBER[i] + "角");
        //sb.toString().replace("元零", "元");
        //        //工行需要去除零
        //        int index=sb.indexOf("元");
        //        if(sb.indexOf("分")==-1 && index>0){
        //        	if(sb.length()>index+1 && sb.charAt(index+1)=='零'){
        //        		sb.delete(index+1, index+2);
        //        	}
        //        }
        //
        //        //工行需要去除零角
        //        index=sb.indexOf("元");
        //        if(index>0){
        //        	if(sb.length()>index+1 && sb.charAt(index+1)=='角'){
        //        		sb.delete(index+1, index+2);
        //        	}
        //        }

        sb.delete(0, sb.length());
        sb.append(buffer);

        if (sb.length() > 2 && sb.charAt(0) == '贰') {
            if (!(sb.charAt(1) == '拾')) {
                sb.replace(0, 1, "两");
            }
        }

        int indexj = sb.indexOf("角");
        int indexf = sb.indexOf("分");
        if (indexj != -1) {
            int indexNum = indexj - 1;
            if (indexNum >= 0 && sb.charAt(indexNum) == '贰') {
                sb.replace(indexNum, indexNum + 1, "两");
            }
        }
        if (indexf != -1) {
            int indexNum = indexf - 1;
            if (indexNum >= 0 && sb.charAt(indexNum) == '贰') {
                sb.replace(indexNum, indexNum + 1, "两");
            }
        }

        buffer = sb.toString();

        return buffer;//sb.toString();
    }

    //INT数字转换中文 带to
    public static String number2CNMontrayUnit(String numberOfMoneyStr, boolean isCN) {
        int j = 0, n = 0;
        String[] sa = numberOfMoneyStr.split("-");
        String result = "";
        n = (sa.length >= 2) ? 2 : 1;
        for (j = 0; j < n; j++) {
            String buffer = sa[j];
            result += number2CNMontrayUnit(buffer);
            if (n >= 2 && j == 0) {
                if (result.indexOf('角') == -1 && result.indexOf('分') == -1)
                    result = result.replace("元", "");
                if (isCN)
                    result += " 至 ";
                else
                    result += " to ";
            }
        }
        return result;
    }

    /**
     * 把输入的数字转换为汉语数字读法
     */
    public static String number2CNInt(String numberOfMoneyStr) {
        return number2CNMontrayUnit(numberOfMoneyStr).replace("元", "");
    }

    //纯粹数字转换中文 带to
    public static String number2CNInt(String numberOfMoneyStr, boolean isCN) {
        int j = 0, n = 0;
        String[] sa = numberOfMoneyStr.split("-");
        String result = "";
        n = (sa.length >= 2) ? 2 : 1;
        for (j = 0; j < n; j++) {
            String buffer = sa[j];
            result += number2CNInt(buffer);
            if (n >= 2 && j == 0) {
                if (isCN)
                    result += " 至 ";
                else
                    result += " to ";
            }
        }
        return result;
    }

    //纯粹数字转换中文 带to
    public static String number2CN(String numberOfMoneyStr, boolean isCN) {
        int j = 0, n = 0;
        String[] sa = numberOfMoneyStr.split("-");
        String result = "";
        n = (sa.length >= 2) ? 2 : 1;
        for (j = 0; j < n; j++) {
            String buffer = sa[j];
            result += number2CN(buffer);
            if (n >= 2 && j == 0) {
                if (isCN)
                    result += " 至 ";
                else
                    result += " to ";
            }
        }
        return result;
    }

    //纯粹数字转换中文
    public static String number2CN(String numberOfMoneyStr) {
        int i = 0;
        String buffer = numberOfMoneyStr;
        for (i = 0; i < CN_UPPER_NUMBER.length; i++)
            buffer = buffer.replace(String.valueOf(i), CN_UPPER_NUMBER[i]);
        buffer = buffer.replace('.', '点');
        return buffer;
    }
}