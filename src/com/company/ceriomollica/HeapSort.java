
/*
 * Classe ausiliaria definita per l'algoritmo di ordinamento:
 *  HEAP SORT
 */

package com.company.ceriomollica;

public class HeapSort implements Sorty {

    @Override
    public void sort(String type, CustomComparable[] comp){
        CustomComparable[] c = new CustomComparable[comp.length+1];
        int i = 0;
        while(i < comp.length){
            c[i+1] = comp[i];
            i++;
        }
        heapSort(c, type);
        
        i = 1;
        while (i < c.length){
            comp[i-1] = c[i];
            i++;
        }
    }

    private static void heapify(CustomComparable[] comp, int n, int i, String type){
        if(i > n) return;
        heapify(comp, n, 2*i, type);
        heapify(comp, n, 2*i+1, type);
        fixheap(comp, n, i, type);
    }

    private static void fixheap(CustomComparable[] comp, int a, int i, String type){
        int max = 2*i;
        if(2*i  > a) return;
        if(2*i +1 <= a && comp[2*i].customCompare(type,comp[2*i +1]) < 0) max = 2*i+1;
        if(comp[i].customCompare(type,comp[max]) < 0){
            CustomComparable temp = comp[max];
            comp[max] = comp[i];
            comp[i] = temp;
            fixheap(comp, a, max,type);
        }
    }

    private static void deleteMax(CustomComparable[] comp, int a, String type) {
        if (a <= 0) return;
        comp[1] = comp[a];
        a--;
        fixheap(comp, a, 1, type);
    }
    public static void heapSort(CustomComparable[] comp, String type) {
        heapify(comp, comp.length - 1, 1, type);
        int c = (comp.length - 1);
        while(c > 0){
            CustomComparable k = comp[1];
            deleteMax(comp, c, type);
            comp[c] = k;
            c--;
        }
    }

}
