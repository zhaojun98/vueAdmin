package com.yl.utils;


/**
 * Created by jerry on 2018/5/26.
 */
public class NumberUtils extends org.apache.commons.lang.math.NumberUtils {

    public static Double toDouble(Object object) {
        return toDouble(object, 0);
    }

    public static double toDouble(Object object, double defaultValue) {
        return object == null ? defaultValue : toDouble(String.valueOf(object), defaultValue);
    }
    public static long toLong(Object object) {
        return toLong(object, 0);
    }

    public static long toLong(Object object, long defaultValue) {
        return object == null ? defaultValue : toLong(String.valueOf(object), defaultValue);
    }

    public static int toInt(Object object) {
        return toInt(object, 0);
    }

    public static int toInt(Object object, int defaultValue) {
        return object == null ? defaultValue : toInt(String.valueOf(object), defaultValue);
    }

    // double转long
    public static long toLong(double number){
        return  new Double(number).longValue();
    }

    // double转int
    public static int toInt(double number){
        return new Double(number).intValue();
    }


    public static void main(String[] args) {
        double random = Math.round(Math.random()*10000);
        System.out.println(random);
        long l = new Double(random).longValue();
        System.out.println(l);
    }
}
