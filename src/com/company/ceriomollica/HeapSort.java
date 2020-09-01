/*
 * Classe ausiliaria definita per l'algoritmo di ordinamento:
 *  HEAP SORT
 */

package com.company.ceriomollica;

public class HeapSort {

    private int[] heap;
    private int len;

    HeapSort (int[] heap) {
        this.heap=heap;
        this.len=heap.length;
    }

    /**
     * This is an in-place sort and will modify the original array passed in the constructor.
     */
    void sort () {
        int n=len;
        heapify();

        for (int i=0;i<n;i++) {
            int temp=extract();
            heap[len]=temp; // Since the heap shrinks by 1 each iteration, we can put each extracted element at the end of the effective heap
        }
    }

    private int extract () {
        int top = heap[0];
        heap[0]=heap[len-1];
        len--;
        siftDown(0);
        return top;
    }

    /**
     * Uses Floyd's method for heap building.
     */
    private void heapify () {
        for (int i=len-1;i>=0;i--) {
            siftDown(i);
        }
    }

    private void siftDown (int i) {
        while (!isLeaf(i) && !biggerThanChildren(i)) {
            i=swap(i, maxChild(i));
        }
    }

    private int maxChild (int i) {
        if (right(i)>=len) return left(i);
        if (heap[left(i)]>=heap[right(i)]) return left(i);
        return right(i);
    }

    private int swap (int x, int y) {
        int temp=heap[x];
        heap[x]=heap[y];
        heap[y]=temp;
        return y;
    }

    private boolean biggerThanChildren (int i) {
        if (right(i)>=len) return heap[i]>=heap[left(i)];
        return heap[i]>=heap[left(i)] && heap[i]>=heap[right(i)];
    }

    private int left (int i) {
        return 2*i+1;
    }

    private int right (int i) {
        return 2*i+2;
    }

    private boolean isLeaf (int i) {
        return left(i)>=len;
    }
}
