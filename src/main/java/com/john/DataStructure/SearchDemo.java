package com.john.DataStructure;

public class SearchDemo {
    public static void main(String[] args) {
//        int[] arr = new int[100000];
//        for(int i = 0; i < arr.length; i++){
//            arr[i] = i + 1;
//        }
        int[] arr2 = {1,3,4,6,7};
        System.out.println(binarySearch(arr2, 7, 0, arr2.length - 1));
    }
    //二分查找
    public static int binarySearch(int[] arr, int target, int left, int right){
        if(left <= right){
            int middle = (left + right)/2;
            if(target < arr[middle]){
               return binarySearch(arr, target, left, middle - 1);
            }else if(target > arr[middle]){
               return binarySearch(arr, target, middle + 1, right);
            }else {
                return middle;
            }
        }
            return -1;
    }
}
