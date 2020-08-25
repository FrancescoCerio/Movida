/*
 * Classe ausiliaria definita per l'algoritmo di ordinamento:
 *  SELECTION SORT
 */

package com.company.ceriomollica;
public class SelectionSort {

    public void selectionSort(int [] array) {
        for(int i = 0; i < array.length-1; i++) {
            int minimo = i; //Partiamo dall' i-esimo elemento
            for(int j = i+1; j < array.length; j++) {
                //Qui avviene la selezione, ogni volta
                //che nell' iterazione troviamo un elemento piú piccolo di minimo
                //facciamo puntare minimo all' elemento trovato
                if(array[minimo]>array[j]) {
                    minimo = j;
                }
            }
            //Se minimo e diverso dall' elemento di partenza
            //allora avviene lo scambio
            if(minimo!=i) {
                int k = array[minimo];
                array[minimo]= array[i];
                array[i] = k;
            }
        }
    }
}
