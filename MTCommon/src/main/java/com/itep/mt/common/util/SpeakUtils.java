package com.itep.mt.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpeakUtils {

    private static String[] to_19 = {"zero", "one", "two", "three", "four",
            "five", "six", "seven", "eight", "nine", "ten", "eleven", "twelve",
            "thirteen", "fourteen", "fifteen", "sixteen", "seventeen",
            "eighteen", "nineteen"};

    private static String[] tens = {"twenty", "thirty", "forty", "fifty",
            "sixty", "seventy", "eighty", "ninety"};

    private static String[] denom = {"", "thousand", "million", "billion",
            "trillion", "quadrillion", "quintillion", "sextillion",
            "septillion", "octillion", "nonillion", "decillion", "undecillion",
            "duodecillion", "tredecillion", "quattuordecillion",
            "sexdecillion", "septendecillion", "octodecillion",
            "novemdecillion", "vigintillion"};

    public static String[] CN_NUM = {"零", "一", "二", "三", "四", "五", "六", "七",
            "八", "九"};


    public static String parse(String param) {
        //return parseNumber(removeNumberComma(param));
        String spStr = param.toLowerCase();
        StringBuffer numberSb = new StringBuffer();
        String[] str = spStr.toLowerCase().split("number");
        if (str.length > 1) {
            numberSb.append(str[0]);
            for (int i = 1; i < str.length; i++) {
                char[] c = str[i].trim().toCharArray();
                numberSb.append("number ");
                for (int j = 0; j < c.length; j++) {
                    String num = String.valueOf(c[j]);
                    if (StringUtil.isNumeric(num)) {
                        numberSb.append(getNumenglish(num));
                    } else if (".".equals(num)) {
                        numberSb.append(" point ");
                    } else {
                        String otherStr = str[i].trim();
                        numberSb.append(otherStr.substring(j, otherStr.length()));
                        break;
                    }
                }
            }
        }
        if (StringUtil.isBlank(numberSb.toString())) {
            numberSb.append(spStr);
        }


        StringBuffer moneySb = new StringBuffer();
        String[] moneyStrs = numberSb.toString().split("money");
        if (moneyStrs.length > 1) {
            moneySb.append(moneyStrs[0]);
            for (int i = 1; i < moneyStrs.length; i++) {
                boolean isRmbSymbols = false;
                boolean isDols = false;
                moneySb.append("money ");
                String moneyStr = moneyStrs[i];
                moneyStr = moneyStr.replace(",", "").replaceAll("，", "");
                char[] c = moneyStr.trim().toCharArray();
                String numStr = "";
                String noNumStr = "";
                for (int j = 0; j < c.length; j++) {
                    int toCount = 0;
                    String num = String.valueOf(c[j]);
                    if (StringUtil.isNumeric(num) || num.equals(".")) {
                        numStr = numStr + num;
                    } else if ("￥".equals(num)) {
                        isRmbSymbols = true;
                    } else if ("$".equals(num)) {
                        isDols = true;
                    } else if ("-".equals(num) && toCount == 0) {
                        numStr = numStr + ",";
                        toCount++;
                    } else {
                        String otherStr = moneyStrs[i].trim();
                        noNumStr = otherStr.substring(j, otherStr.length());
                        break;
                    }
                }
                String[] numStrs = numStr.split(",");
                if (numStrs.length == 2) {
                    moneySb.append(english_number(numStrs[0]));
                    moneySb.append(" to ");
                    moneySb.append(english_number(numStrs[1]));
                } else {
                    moneySb.append(english_number(numStrs[0]));
                }

                if (isRmbSymbols) {
                    moneySb.append(" yuan ");
                }
                if (isDols) {
                    moneySb.append(" dollars ");
                }
                moneySb.append(" ").append(noNumStr);
            }
        }
        if (StringUtil.isBlank(moneySb.toString())) {
            moneySb.append(numberSb.toString());
        }
        //      StringBuffer filterZero=new StringBuffer();
        //      Pattern p=Pattern.compile("[￥|$]([0-9]|[0-9]{1,3}(,[0-9]{3})*)(.[0-9]{1,3})");
        String moneyStr = moneySb.toString();
        moneyStr = moneyStr.replaceAll(",", "").replaceAll("，", "");
        StringBuffer filterOne = new StringBuffer();
        Pattern p = Pattern.compile("([￥|$][0-9]+(\\.[0-9]{1,2})?(\\-[0-9]+(\\.[0-9]{1,2})?)?)|([0-9]+笔)");
        Matcher m = p.matcher(moneyStr);
        while (m.find()) {
            String matchStr = m.group();
            String flag = matchStr.substring(0, 1);
            String mStr = m.group().substring(1);
            if ("￥".equals(flag)) {
                m.appendReplacement(filterOne, getNum2NumToCnStr(mStr));
            } else if ("$".equals(flag)) {
                m.appendReplacement(filterOne, getNum2NumToUSStr(mStr));
            } else {
                String converStr = getNum2NumToCnStr(matchStr.substring(0, matchStr.length() - 1));
                m.appendReplacement(filterOne, converStr.substring(0, converStr.length() - 1) + "笔");
            }
        }
        m.appendTail(filterOne);

        //p=Pattern.compile("[0-9]+笔");


        StringBuffer content = new StringBuffer();
        //        String str1="011.1.1,现金￥10.6,现金$105.5，现金￥10004G1.0.06KHHHH11155hs1";
        p = Pattern.compile("^([0-9]+(\\.?[0-9]+)*)|(([^(￥|$|\\.|0-9)])[0-9]+(\\.?[0-9]+)*)");
        m = p.matcher(filterOne.toString());
        while (m.find()) {
            m.appendReplacement(content, getNumToCnStr(m.group()));
        }
        m.appendTail(content);
        return content.toString();
    }


    private static String getNum2NumToUSStr(String str) {
        String result = "";
        String[] nums = str.split("-");
        for (int i = 0; i < nums.length; i++) {
            String[] numStrs = nums[i].split("\\.");
            String decStr = "";
            if (numStrs.length == 2) {
                decStr = "点" + getNumToCnStr(numStrs[1]);
            }
            if (i == 1) {
                result = result.substring(0, result.length() - 2) + "至";
            }
            String converStr = NumberToCnUtils.number2CNMontrayUnit(numStrs[0]);
            result = result + converStr.substring(0, converStr.length() - 1) + decStr + "美元";
        }
        return result;
    }


    private static String getNum2NumToCnStr(String str) {
        String result = "";
        String[] nums = str.split("-");
        for (int i = 0; i < nums.length; i++) {
            if (i == 1) {
                String lastStr = result.substring(result.length() - 1, result.length());
                if (lastStr.equals("元")) {
                    result = result.substring(0, result.length() - 1) + "至";
                } else {
                    result = result + "至";
                }
            }
            result = result + NumberToCnUtils.number2CNMontrayUnit(nums[i]);
        }
        return result;
    }


    private static String getNumToCnStr(String str) {
        String result = "";
        char[] c = str.toCharArray();
        for (int i = 0; i < c.length; i++) {
            String curStr = String.valueOf(c[i]);
            if (".".equals(curStr)) {
                curStr = "点";
            } else if (StringUtil.isNumeric(curStr)) {
                curStr = CN_NUM[Integer.parseInt(curStr)];
            }
            result = result + curStr;
        }
        return result;
    }

    private static String getNumenglish(String str) {
        String result = "";
        char[] c = str.toCharArray();
        for (int i = 0; i < c.length; i++) {
            result = result + to_19[Integer.parseInt(String.valueOf(c[i]))]
                    + " ";
        }
        return result;
    }

    private static String convert_nn(int val) throws Exception {
        if (val < 20)
            return to_19[val];

        int flag = val / 10 - 2;
        if (val % 10 != 0)
            return tens[flag] + "-" + to_19[val % 10];
        else
            return tens[flag];
    }

    private static String convert_nnn(int val) throws Exception {
        String word = "";
        int rem = val / 100;
        int mod = val % 100;
        if (rem > 0) {
            word = to_19[rem] + " hundred";
            if (mod > 0) {
                word = word + " ";
            }
        }
        if (mod > 0) {
            word = word + convert_nn(mod);
        }
        return word;
    }

    //英文转数字，按int
    public static String english_number(String str) {
        String result = str;
        try {
            Double.parseDouble(str);
            String[] numbers = str.split("\\.");
            int val = Integer.parseInt(numbers[0]);
            if (val < 100) {
                result = convert_nn(val);
            } else if (val < 1000) {
                result = convert_nnn(val);
            } else {
                for (int v = 0; v < denom.length; v++) {
                    int didx = v - 1;
                    int dval = new Double(Math.pow(1000, v)).intValue();
                    if (dval > val) {
                        int mod = new Double(Math.pow(1000, didx)).intValue();
                        int l = val / mod;
                        int r = val - (l * mod);
                        String ret = convert_nnn(l) + " " + denom[didx];
                        if (r > 0) {
                            ret = ret + ", "
                                    + english_number(String.valueOf(r));
                        }
                        result = ret;
                        break;
                    }
                }
            }
            if (numbers.length == 2) {
                String decimalsStr = numbers[1];
                result = result + " point";
                char[] decimals = decimalsStr.toCharArray();
                for (int i = 0; i < decimals.length; i++) {
                    char c = decimals[i];
                    result = result + " "
                            + to_19[Integer.parseInt(String.valueOf(c))];
                }

            }
        } catch (Exception e) {
        }
        return result;
    }

    //纯粹数字转换英文 带to
    public static String english2numberInt(String numberOfMoneyStr, boolean isCN) {
        int j = 0, n = 0;
        String[] sa = numberOfMoneyStr.split("-");
        String result = "";
        n = (sa.length >= 2) ? 2 : 1;
        for (j = 0; j < n; j++) {
            String buffer = sa[j];
            result += english_number(buffer);
            if (n >= 2 && j == 0) {
                if (isCN)
                    result += " 至 ";
                else
                    result += " to ";
            }
        }
        return result;
    }

    //纯粹数字转换英文
    public static String english_number2(String numberOfMoneyStr, boolean isCN) {
        int i = 0, j = 0, n = 0;
        String[] sa = numberOfMoneyStr.split("-");
        String result = "";
        n = (sa.length >= 2) ? 2 : 1;
        for (j = 0; j < n; j++) {
            String buffer = sa[j];
            for (i = 0; i < 10; i++)
                buffer = buffer.replace(String.valueOf(i), " " + to_19[i] + " ");
            buffer = buffer.replace(".", " point ");
            result += buffer;
            if (n >= 2 && j == 0) {
                if (isCN)
                    result += " 至 ";
                else
                    result += " to ";
            }
        }

        return result;
    }

    // 数字转换英文
    public static String ConvEnNumber(String numberStr) {
        int i = 0;
        String buffer = numberStr;
        for (i = 0; i < 10; i++)
            buffer = buffer.replace(String.valueOf(i), " " + to_19[i] + " ");
        buffer = buffer.replace(".", " point ");
        return buffer;
    }

    // 数字转换英文
    public static String findPrev(String str, int startpos) {
        int i = 0;
        String buffer = "";
        for (i = startpos - 1; i >= 0; i--) {
            char ch = str.charAt(i);
            if (ch == ' ' || ch == '\n' || ch == '\t' || ch == '\r' || ch == '\f' || ch == '.' || ch == ',' || ch == '-')
                continue;
            buffer = String.valueOf(ch);
            break;
        }
        return buffer;
    }

    // 查找下个非空字符
    public static String findLast(String str, int startpos, int length) {
        int i = 0;
        String buffer = "";
        for (i = startpos + length; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (ch == ' ' || ch == '\n' || ch == '\t' || ch == '\r' || ch == '\f' || ch == '.' || ch == ',' || ch == '-')
                continue;
            buffer = String.valueOf(ch);
            break;
        }
        return buffer;
    }

    // 判断字符是否为ABC
    public static boolean isABC(char ch) {
        return ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z'));
    }

    public static String removeNumberComma(String source) {
        String test = source;// "dsfsd$1,000.00";
        Pattern p = Pattern.compile("(([￥|$]{1}[0-9]{1,3}(,[0-9]{3})+))(\\-([0-9]{1,3}(,[0-9]{3})+))?");
        //Pattern p = Pattern.compile("[￥|$]([0-9]|[0-9]{1,3}(,[0-9]{3})*)(\\.[0-9]{1,3})((\\-[0-9]|[0-9]{1,3}(,[0-9]{3})*)(\\.[0-9]{1,3}))?");
        // Pattern p=Pattern.compile("(([0-9]{1,3}(,[0-9]{3})*)(\\.[0-9]+)?)");
        StringBuffer sb = new StringBuffer();
        Matcher m = p.matcher(test);
        while (m.find()) {
            String temp = m.group();
            String newStr = temp.replace(",", "");
            //System.out.println("mf:"+temp+","+newStr);
            m.appendReplacement(sb, newStr.replace("$", "\\$"));
        }
        m.appendTail(sb);
        return sb.toString();
    }

    public static String parseNumber(String s) {
        Pattern p = Pattern.compile("([0-9]+((\\.)[0-9]+)*)(\\-[0-9]+((\\.)[0-9]+)*)?");
        Matcher m = p.matcher(s);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String str_r = m.group();

            boolean isCn = true;
            boolean isCounts = false;
            int isDollar = 0;//0无 1rmb 2dollar
            String appendMoney = "";
            String prev = findPrev(s, m.start());
            String last = findLast(s, m.start(), str_r.length());
            if (last.equals("笔")) {
                isCn = true;
                isCounts = true;
            } else if (prev.equals("$") || prev.equals("￥")) {
                isDollar = (prev.equals("$")) ? 2 : 1;
                prev = findPrev(s, m.start() - 1);
                System.out.println("prev:" + prev);
                if (prev.length() > 0 && isABC(prev.charAt(0))) {
                    if (isDollar == 2)
                        appendMoney = " dollars ";
                    else
                        appendMoney = " yuan ";
                    isCn = false;
                } else {
                    if (isDollar == 2)
                        appendMoney = " 美元  ";
                    else
                        appendMoney = " 元  ";
                    isCn = true;
                }
            } else if (prev.length() > 0 && isABC(prev.charAt(0))) {
                isCn = false;
            }

            if (isCn) {
                if (isCounts)
                    str_r = NumberToCnUtils.number2CNInt(str_r, isCn);//pen
                    //else if(isDollar == 2) str_r = NumberToCnUtils.number2CNMontrayUnit(str_r,isCn).replace("元", "") + appendMoney;//dollar
                else if (isDollar == 2)
                    str_r = getNum2NumToUSStr(str_r);//dollar
                else if (isDollar == 1)
                    str_r = NumberToCnUtils.number2CNMontrayUnit(str_r, isCn);//rmb
                else
                    str_r = NumberToCnUtils.number2CN(str_r, isCn);//normal
            } else {
                if (isDollar != 0)
                    str_r = english2numberInt(str_r, isCn) + appendMoney;//dollar & rmb
                else
                    str_r = english_number2(str_r, isCn);//normal
                if (str_r.trim().equals("one dollars"))
                    str_r = str_r.replace("one dollars", "one dollar");
            }


            m.appendReplacement(sb, str_r);
            //else if(findPrev(s,m.start()) == "$")appendMoney = "";
            //else if(findPrev(s,m.start()) == "￥")appendMoney = "";
            //else if(StringUtils.isAlpha(findPrev(s,m.start())))

            //System.out.println( m.group()+"-->"+str_r+"!->"+m.start()+"->"+prev+"->"+last);
        }
        m.appendTail(sb);
        //System.out.println(sb);
        return sb.toString().replace('$', ' ').replace('￥', ' ');
    }

    //	public static void main(String[] args) {
    //		System.out.println(getNum2NumToUSStr("100.12-123.22"));
    //		String s="现金￥1,100.02-100.01 adf 1000-1233现金input$100 1-10笔 a $1,100 Money $1,100-1,200 Number1234.5 yuan 105笔";
    //		s = " 请1,100.12-200.01";
    //		System.out.println(parse(s));
    //		System.out.println(s);
    //		s = removeNumberComma(s);
    //		System.out.println(s);
    //		s = parseNumber(s);
    //		System.out.println(s);
    //	}


}
