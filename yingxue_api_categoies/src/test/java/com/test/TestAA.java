package com.test;

import java.util.HashMap;

public class TestAA {

    public static void main(String[] args) {

        /*
            逻辑运算   &&(与) 两边同时为true 结果true   ||(或) 表达式两边只要有一个结果为true  true   !(非) 非true 就是false

            位运算     &(与)上下两个位同时为1 结果为1 否则都是0
                      |(或) 上下两个位只要有一个为1  结果为1 否则都是0
                      ^(异或)表示的两个的位，相同则结果为0，不同则结果为1
                      ~(非)  表示位为0，结果是1，如果位为1，结果是0
         */

        //int 4个字节    32位
        //int a=1       0000 0000 0000 0000 0000 0000 0000 0000
        //int b=2       0000 0000 0000 0000 0000 0000 0000 0010
        // a&b          0000 0000 0000 0000 0000 0000 0000 0000
        // a|b          0000 0000 0000 0000 0000 0000 0000 0011
        // a^b          0000 0000 0000 0000 0000 0000 0000 0011
        // ~a           1111 1111 1111 1111 1111 1111 1111 1111
        int a=1;
        int b=2;
        /*System.out.println(a&b);
        System.out.println(a|b);
        System.out.println(a^b);//10000
        System.out.println(~a);*/


        System.out.println(a>>>16);

        //
        int[] aas ={111,234};
        

    }

}
