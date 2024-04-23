package com.xhf.test;

import java.io.IOException;

public class Test {
    private static int cnt = 0;
    private static boolean flag = true;
    public static void main(String[] args) throws IOException, InterruptedException {

        // 读线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    System.out.println(cnt);
                }
            }
        }).start();

        Thread.sleep(1000);

        /*
        // 写线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while(true) {
                    cnt = i;
                    ++i;
                }
            }
        }).start();
         */
        cnt = 1;
        flag = false;
        System.in.read();
    }
}
