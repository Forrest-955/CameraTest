package com.itep.mt.common.util;

import android.text.TextUtils;

/**
 * Created by cy on 2019/09/18.
 */

public class PokerTest {

    private static final double MACHEP = 1.11022302462515654042E-16;
    private static final double MAXLOG = 7.09782712893383996732E2;
    private static final double MINLOG = -7.451332191019412076235E2;
    private static final double MAXGAM = 171.624376956302725;
    private static final double SQTPI = 2.50662827463100050242E0;
    private static final double SQRTH = 7.07106781186547524401E-1;
    private static final double LOGPI = 1.14472988584940017414;
    private static float a = 0.01f;

    /**
     * 随机数检测
     * 参数要求 |n/m|>=5*2^m
     *
     * @param randomHexStr 十六进制随机数字符串
     * @param m            子序列的比特长度
     * @return success
     */
    public static boolean randomTest(String randomHexStr, int m) {
        if (TextUtils.isEmpty(randomHexStr)) {
            return false;
        }
        try {
            double v = statisticValue(randomHexStr, m);
            double pValue = ComplementaryErrorValue(m, v);
            Logger.e("V:" + v + "P_value:" + pValue);
            return 1 - pValue > a;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 设置显著性水平，目前只允许为0.01或0.05
     *
     * @param a Significance Level
     */
    public void setSignificanceLevel(float a) {
        if (a == 0.05 || a == 0.01) {
            PokerTest.a = a;
        }
    }

    /**
     * 计算统计值
     *
     * @param m 子序列长度
     * @return 统计值V
     */
    private static double statisticValue(String randomHexStr, int m) {
        int n = randomHexStr.length() * 4;//二进制序列长度，hexString两位一个字节
        int N = n / m;//分多少块

        int cCount = (int) Math.pow(2, m);//子序列类型数
        Logger.e("计算统计值n:" + n + "N:" + N + "cCount:" + cCount);
        int[] tab = new int[cCount];//统计表....数值,数量
        for (int i = 0; i < cCount; i++) {
            tab[i] = 0;
        }

        String binStr = strToBinStr(randomHexStr);//二进制序列
        Logger.e("计算统计值binStr:" + binStr);
        //for (int i = 0, j = 0; j < N; j++, i += m)//统计各子序列情况数量
        //{
        //    String t = binStr.substring(i, m);
        //    //String t=binStr.substring(m*i,(m+1)*i);
        //    int v = Integer.valueOf(t, 2);
        //    tab[v] = tab[v] + 1;
        //}
        for (int i = 0; i < N; i++) {//统计各子序列情况数量
            String t = binStr.substring(m * i, m * (i + 1));
            int v = Integer.valueOf(t, 2);
            tab[v] = tab[v] + 1;
        }
        //var c = (int)(N / Math.Pow(2, m));
        int c = 0;

        double sum_ni = 0d;
        for (int item : tab) {
            sum_ni += Math.pow(item, 2);
            //sum_ni += Math.Pow(c, 2);
            c += item;
        }
        //计算V
        double mp = Math.pow(2, m) / N;
        double V = mp * sum_ni - N;
        return V;
    }

    /**
     * 余误差函数 complementary error function
     *
     * @param m 子序列长度
     * @param v 统计值V
     * @return P_value
     */
    private static double ComplementaryErrorValue(int m, double v) {
        double a = (Math.pow(2, m) - 1) / 2;
        double x = v / 2;
        return igam(a, x);
    }

    /**
     * Returns the complemented incomplete gamma function.返回补充的不完整伽玛函数
     */
    public static double igamc(double a, double x) {
        double big = 4.503599627370496e15;
        double biginv = 2.22044604925031308085e-16;
        double ans, ax, c, yc, r, t, y, z;
        double pk, pkm1, pkm2, qk, qkm1, qkm2;

        if (x <= 0 || a <= 0)
            return 1.0;

        if (x < 1.0 || x < a)
            return 1.0 - igam(a, x);

        ax = a * Math.log(x) - x - lgamma(a);
        //Logger.e("pokerTest" + logGamma(a) + "|" + lgamma(a));
        if (ax < -MAXLOG)
            return 0.0;

        ax = Math.exp(ax);

            /* continued fraction */
        y = 1.0 - a;
        z = x + y + 1.0;
        c = 0.0;
        pkm2 = 1.0;
        qkm2 = x;
        pkm1 = x + 1.0;
        qkm1 = z * x;
        ans = pkm1 / qkm1;

        do {
            c += 1.0;
            y += 1.0;
            z += 2.0;
            yc = y * c;
            pk = pkm1 * z - pkm2 * yc;
            qk = qkm1 * z - qkm2 * yc;
            if (qk != 0) {
                r = pk / qk;
                t = Math.abs((ans - r) / r);
                ans = r;
            } else
                t = 1.0;

            pkm2 = pkm1;
            pkm1 = pk;
            qkm2 = qkm1;
            qkm1 = qk;
            if (Math.abs(pk) > big) {
                pkm2 *= biginv;
                pkm1 *= biginv;
                qkm2 *= biginv;
                qkm1 *= biginv;
            }
        } while (t > MACHEP);

        return ans * ax;
    }

    /**
     * Returns the incomplete gamma function.返回不完整的伽玛函数。
     */
    public static double igam(double a, double x) {
        double ans, ax, c, r;

        if (x <= 0 || a <= 0)
            return 0.0;

        if (x > 1.0 && x > a)
            return 1.0 - igamc(a, x);

            /* Compute  x**a * exp(-x) / gamma(a)  */
        ax = a * Math.log(x) - x - lgamma(a);
        if (ax < -MAXLOG)
            return (0.0);

        ax = Math.exp(ax);

            /* power series */
        r = a;
        c = 1.0;
        ans = 1.0;

        do {
            r += 1.0;
            c *= x / r;
            ans += c;
        } while (c / ans > MACHEP);

        return (ans * ax / a);

    }

    /**
     * Returns the natural logarithm of gamma function.返回伽马函数的自然对数
     */
    public static double lgamma(double x) {
        double p, q, w, z;

        double[] A = {
                8.11614167470508450300E-4,
                -5.95061904284301438324E-4,
                7.93650340457716943945E-4,
                -2.77777777730099687205E-3,
                8.33333333333331927722E-2
        };
        double[] B = {
                -1.37825152569120859100E3,
                -3.88016315134637840924E4,
                -3.31612992738871184744E5,
                -1.16237097492762307383E6,
                -1.72173700820839662146E6,
                -8.53555664245765465627E5
        };
        double[] C = {// 1.00000000000000000000E0,
                -3.51815701436523470549E2,
                -1.70642106651881159223E4,
                -2.20528590553854454839E5,
                -1.13933444367982507207E6,
                -2.53252307177582951285E6,
                -2.01889141433532773231E6
        };

        if (x < -34.0) {
            q = -x;
            w = lgamma(q);
            p = Math.floor(q);
            if (p == q)
                throw new ArithmeticException("lgam: Overflow");
            z = q - p;
            if (z > 0.5) {
                p += 1.0;
                z = p - q;
            }
            z = q * Math.sin(Math.PI * z);
            if (z == 0.0)
                throw new
                        ArithmeticException("lgamma: Overflow");
            z = LOGPI - Math.log(z) - w;
            return z;
        }

        if (x < 13.0) {
            z = 1.0;
            while (x >= 3.0) {
                x -= 1.0;
                z *= x;
            }
            while (x < 2.0) {
                if (x == 0.0)
                    throw new
                            ArithmeticException("lgamma: Overflow");
                z /= x;
                x += 1.0;
            }
            if (z < 0.0)
                z = -z;
            if (x == 2.0)
                return Math.log(z);
            x -= 2.0;
            p = x * polevl(x, B, 5) / p1evl(x, C, 6);
            return (Math.log(z) + p);
        }

        if (x > 2.556348e305)
            throw new
                    ArithmeticException("lgamma: Overflow");

        q = (x - 0.5) * Math.log(x) - x + 0.91893853320467274178;
        if (x > 1.0e8)
            return (q);

        p = 1.0 / (x * x);
        if (x >= 1000.0)
            q += ((7.9365079365079365079365e-4 * p
                    - 2.7777777777777777777778e-3) * p
                    + 0.0833333333333333333333) / x;
        else
            q += polevl(p, A, 4) / x;
        return q;
    }

    private static double incompleteGamma(double a, double x) {
        int n;
        double p, q, d, s, s1;
        double p0, q0, p1, q1, qq;
        if ((a <= 0.0) || (x <= 0.0)) {
            if (a <= 0.0) {
                Logger.e("err ** a<=0!\n");
            }
            if (x < 0.0) {
                Logger.e("err ** x<=0!\n");
            }
            return (-1.0);
        }
        if (x + 1.0 == 1.0)
            return (0.0);
        if (x > 1.0e+35)
            return (1.0);
        q = Math.log(x);
        q = a * q;
        qq = Math.exp(q);
        if (x < 1.0 + a) {
            p = a;
            d = 1.0 / a;
            s = d;
            for (n = 1; n <= 100; n++) {
                p = 1.0 + p;
                d = d * x / p;
                s = s + d;
                if (Math.abs(d) < Math.abs(s) * 1.0e-07) {
                    s = s * Math.exp(-x) * qq / logGamma(a);
                    return (s);
                }
            }
        } else {
            s = 1.0 / x;
            p0 = 0.0;
            p1 = 1.0;
            q0 = 1.0;
            q1 = x;
            for (n = 1; n <= 100; n++) {
                p0 = p1 + (n - a) * p0;
                q0 = q1 + (n - a) * q0;
                p = x * p0 + n * p1;
                q = x * q0 + n * q1;
                if (Math.abs(q) + 1.0 != 1.0) {
                    s1 = p / q;p1 = p;q1 = q;
                    if (Math.abs((s1 - s) / s1) < 1.0e-07) {
                        s = s1 * Math.exp(-x) * qq / logGamma(a);
                        return (1.0 - s);
                    }
                    s = s1;
                }
                p1 = p;
                q1 = q;
            }
        }
        //printf("a too large !\n");
        s = 1.0 - s * Math.exp(-x) * qq / logGamma(a);
        return (s);
    }

    /**
     * Gamma(x) = integral( t^(x-1) e^(-t), t = 0 .. infinity)
     * Uses Lanczos approximation formula. See Numerical Recipes 6.1.
     */
    private static double logGamma(double x) {
        double tmp = (x - 0.5) * Math.log(x + 4.5) - (x + 4.5);
        double ser = 1.0 + 76.18009173 / (x + 0) - 86.50532033 / (x + 1)
                + 24.01409822 / (x + 2) - 1.231739516 / (x + 3)
                + 0.00120858003 / (x + 4) - 0.00000536382 / (x + 5);
        return tmp + Math.log(ser * Math.sqrt(2 * Math.PI));
    }

    /**
     * Evaluates polynomial of degree N.求N次多项式的值
     *
     * @return
     */
    private static double polevl(double x, double[] coef, int N) {
        double ans;

        ans = coef[0];

        for (int i = 1; i <= N; i++) {
            ans = ans * x + coef[i];
        }

        return ans;
    }

    /**
     * Evaluates polynomial of degree N with assumtion that coef[N] = 1.0
     *
     * @return
     */
    private static double p1evl(double x, double[] coef, int N) {
        double ans;

        ans = x + coef[0];

        for (int i = 1; i < N; i++) {
            ans = ans * x + coef[i];
        }

        return ans;
    }

    /**
     * 十六进制字符串转二进制字符串
     *
     * @param str hex
     * @return binStr
     */
    public static String strToBinStr(String str) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            String t = str.substring(i, i + 1);
            String bin = Integer.toBinaryString(Integer.valueOf(t, 16));
            //转化的binaryString可能不足4位，在前面补0
            String cover = "0000";
            if (bin.length() < 4) {
                bin = cover.substring(bin.length()) + bin;
            }
            result.append(bin);
        }
        return result.toString();
    }
}
