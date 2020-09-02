/*
 * Classe ausiliaria definita per l'algoritmo di ordinamento:
 *  SELECTION SORT
 */

package com.company.ceriomollica;

import java.lang.reflect.Array;

public class SelectionSort implements Sorty{

    public void selectionSort(String param, CustomComparable[] array) {
        for(int i = 0; i < array.length-1; i++) {
            int minimo = i; //Partiamo dall' i-esimo elemento
            for(int j = i+1; j < array.length; j++) {
                //Qui avviene la selezione, ogni volta
                //che nell' iterazione troviamo un elemento piú piccolo di minimo
                //facciamo puntare minimo all' elemento trovato
                if(array[minimo].customCompare(param, array[j]) > 0) {
                    minimo = j;
                }
            }
            // Se minimo è diverso dall' elemento di partenza
            // allora avviene lo scambio
            if(minimo!=i) {
                CustomComparable k = array[minimo];
                array[minimo]= array[i];
                array[i] = k;
            }
        }
    }

    @Override
    public void sort(String param, CustomComparable[] c) {
        this.selectionSort(param, c);
    }
}
