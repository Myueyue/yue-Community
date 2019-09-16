package com.yue.community.controller;

import org.junit.Test;

import java.util.Arrays;

public class arrs {

    @Test
    public void test(){
        int[] arr = {5,20,1,30,6,80,100,60,102,55};

        for (int j = 0; j < arr.length-1; j++) {
            for (int i = 0; i < arr.length -1-j; i++) {
                if(arr[i]>arr[i+1]){
                    int t=arr[i];
                    arr[i]=arr[i+1];
                    arr[i+1]=t;
                }
            }
        }
          System.out.println(Arrays.toString(arr));
    }
}
