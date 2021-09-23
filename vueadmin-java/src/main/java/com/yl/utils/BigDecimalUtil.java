package com.yl.utils;

import java.math.BigDecimal;

/**
 * Created by hujoey on 16/4/17.
 */
public class BigDecimalUtil {


    public static double fixDoubleNum(double num, int scale) {
        return fixDoubleNum(new BigDecimal(String.valueOf(num)), scale);
    }

    public static double fixDoubleNum(BigDecimal num, int scale) {
        return num == null ? 0 : num.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static double fixDoubleNum2(double num) {
        return fixDoubleNum(num, 2);//四舍五入保留两位小数
    }

    public static double fixDoubleNum2(BigDecimal num) {
        return fixDoubleNum(num, 2);//四舍五入保留两位小数;
    }

    public static double fixDoubleNum2Down(double num) {
        return fixDoubleNum2Down(new BigDecimal(String.valueOf(num)), 2);//向负无穷方向舍入
    }

    public static double fixDoubleNum2Down(BigDecimal num, int scale) {
        return num == null ? 0 : num.setScale(scale, BigDecimal.ROUND_FLOOR).doubleValue();//向负无穷方向舍入
    }

    public static double fixDoubleNum2Up(double num) {
        return fixDoubleNumUp(num, 2);//向正无穷方向舍入
    }

    public static double fixDoubleNumUp(double num, int scale) {
        return fixDoubleNumUp(new BigDecimal(String.valueOf(num)), scale);//向正无穷方向舍入
    }

    public static double fixDoubleNumUp(BigDecimal num, int scale) {
        return num == null ? 0 : num.setScale(scale, BigDecimal.ROUND_CEILING).doubleValue();//向正无穷方向舍入
    }

    public static double add(double old, double add) {  //加法
        return new BigDecimal(String.valueOf(old)).add(new BigDecimal(String.valueOf(add))).doubleValue();//四舍五入保留两位小数
    }

    public static double subtract(double old, double add) {    //减法
        return new BigDecimal(String.valueOf(old)).subtract(new BigDecimal(String.valueOf(add))).doubleValue();//四舍五入保留两位小数
    }

    public static double multiply(double old, double add) {//乘法
        return new BigDecimal(String.valueOf(old)).multiply(new BigDecimal(String.valueOf(add))).doubleValue();//四舍五入保留两位小数
    }

    public static double divide(double old, double add) {//除法
        return new BigDecimal(String.valueOf(old)).divide(new BigDecimal(String.valueOf(add)), 6, BigDecimal.ROUND_FLOOR).doubleValue();//四舍五入保留两位小数
    }

    /**
     * 修正精度丢失
     *
     * @param num
     * @return
     */
    public static Double fixDoubleNumProfit(double num) {
        Double d1 = new BigDecimal(num).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
        String d1Str = d1.toString();
        int i = d1Str.indexOf(".");
        String newStr = d1Str;
        if (i > -1) {
            if (d1Str.length() > i + 3) {
                newStr = d1Str.substring(0, i + 3);
            }
        }
        return Double.valueOf(newStr);
    }

    public static void main(String[] args) {
        //new java.text.DecimalFormat(”#.00″).format(3.1415926)
        System.out.println("(0.123456)四舍五入保留两位小数"+fixDoubleNum2(0.123456));
        System.out.println(fixDoubleNum2(0.129999));
        System.out.println(fixDoubleNum2(0.12444));
        System.out.println(fixDoubleNum2(0.1250001));

        System.out.println("------------");

        System.out.println("(0.123456)//向负无穷方向舍入"+fixDoubleNum2Down(0.123456));
        System.out.println(fixDoubleNum2Down(0.129999));
        System.out.println(fixDoubleNum2Down(0.12444));
        System.out.println(fixDoubleNum2Down(0.1200001));

        System.out.println("------------");

        System.out.println("修正精度丢失:"+fixDoubleNumProfit(0.123456));
        System.out.println(fixDoubleNumProfit(0.129999));
        System.out.println(fixDoubleNumProfit(0.12444));
        System.out.println(fixDoubleNumProfit(0.1200001));
    }
}
