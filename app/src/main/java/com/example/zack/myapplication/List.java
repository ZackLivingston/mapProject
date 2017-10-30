package com.example.zack.myapplication;

/**
 * Created by Zack on 2017-07-22.
 */

public class List {
    public Art[] array;
    private int number;

    public List(Art[] pieces) {
        array = pieces;
    }

    public void quicksortDistance(int low, int high, double[] userLoc) {
        int i = low;
        int j = high;
        double pivot = array[low + ((high-low)/2)].distanceTo(userLoc);
        while (i <= j) {
            while (array[i].distanceTo(userLoc) < pivot) {
                i++;
            } // while array < pivot
            while (array[j].distanceTo(userLoc) > pivot) {
                j--;
            } // while array > pivot
            if (i <= j) {
                Art hold = array[i];
                array[i] = array[j];
                array[j] = hold;
                i++;
                j--;
            } // if i <= j
        } // while i <= j
        if (low < j) {
            quicksortDistance(low, j, userLoc);
        }
        if (i < high) {
            quicksortDistance(i, high, userLoc);
        }
    } // quicksort


    private void quicksortName(int low, int high) {
        int i = low;
        int j = high;
        String pivot = array[low + ((high-low)/2)].title;
        while (i <= j) {
            while (array[i].title.compareTo(pivot) < 0) {
                i++;
            } // while array < pivot
            while (array[j].title.compareTo(pivot) > 0) {
                j--;
            } // while array > pivot
            if (i <= j) {
                Art hold = array[i];
                array[i] = array[j];
                array[j] = hold;
                i++;
                j--;
            } // if i <= j
        } // while i <= j
        if (low < j) {
            quicksortName(low, j);
        }
        if (i < high) {
            quicksortName(i, high);
        }
    } // quicksort

    public void insertionSort(double[] userLoc) {
        Art temp;
        for (int i = 1; i < this.array.length; i++) {
            for(int j = i ; j > 0 ; j--){
                if(this.array[j].distanceTo(userLoc) < this.array[j-1].distanceTo(userLoc)){
                    temp = this.array[j];
                    this.array[j] = this.array[j-1];
                    this.array[j-1] = temp;
                } // if
            } // for
        } // for
    } // insertionSort



}
