package com.itep.mt.common.util;

import java.math.BigDecimal;

/**
 * 计算工具类
 */
public class MathUtils {

    /**
     * 提供精确的乘法运算。
     *
     * @param v1
     * @param v2
     * @return
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(v2);
        return b1.multiply(b2).doubleValue();
    }

}
