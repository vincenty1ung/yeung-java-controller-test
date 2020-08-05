package com.uncle.controller.algorithm;

import java.util.Arrays;

/**
 * @author 杨戬
 * @className BinarySearch
 * @email yangb@chaosource.com
 * @date 2020/8/5 17:47
 */
public class BinarySearch {
    public static void main(String[] args) {
        // 二分法 算法
        int[] a = new int[20000000];
        for (int i = 0; i < 20000000; i++) {
            a[i] = i;
        }
        System.out.println("Arrays.toString(a) = " + Arrays.toString(a));
        int result = binarySearch(a, a.length, 5659);
        System.out.println("result = " + result);
    }

    private static int binarySearch(int[] a, int length, int i) {
        int low = 0;
        int high = length - 1;
        int count = 0;
        while (low <= high) {
            int mid = (low + high) / 2;
            count++;
            System.out.println("找了" + count + "次" + "中位:" + mid);
            if (a[mid] == i) {
                return mid;
            }
            if (i > a[mid]) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -1;

    }
}
