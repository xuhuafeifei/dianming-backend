package com.xhf.utils.common;

public class RandomUtils {

    /**
     * 范围内随机数，>=num1 ,<num2
     * @param num1 下界
     * @param num2 上界
     * @return
     */
    public static int createInt(int num1,int num2) {
        int n=num1+(int)(Math.random()*(num2-num1));
        return n;
    }

    /**
     * 范围内随机数，>=num1 ,<num2, 多线程中使用
     * @param num1 下界
     * @param num2 上界
     * @return
     */
    public static int createMilliSecond(int num1,int num2)
    {
        return (num1+(int)(Math.random()*(num2-num1)))*1000;
    }
}
