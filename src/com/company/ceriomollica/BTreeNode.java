/*
 * Classe ausiliaria definita per l'algoritmo di dizionario:
 * BTREENode
 */

package com.company.ceriomollica;

public class BTreeNode<K,V>{
    K[] keys; // Array di chiavi
    V value; // valori
    int t; // grado minimo
    BTreeNode[] C; // Array di puntatori a figli
    int n; // Numero corrente di chiavi
    boolean leaf; // true quando il nodo è una foglia, falso altrimenti

    V getValue(){
        return this.value;
    }

    BTreeNode(int t, boolean leaf) {
        this.t = t;
        this.leaf = leaf;
        this.keys = (K[]) new Object[2 * t - 1];
        this.C = new BTreeNode[2 * t];
        this.n = 0;
    }

    // Funzione di attraversamento dei nodi di un sotto albero a partire da un nodo
    public void traverse() {

        // Ci sono n chiavi ed n+1 figli, attraversa i primi n figli
        int i = 0;
        for (i = 0; i < this.n; i++) {

            // Se non è una foglia, prima di stampare key[i],
            // attraversa il sottoalbero del figlio C[i].
            if (!this.leaf) {
                C[i].traverse();
            }
            System.out.print(keys[i].toString() + " ");
        }

        // Stampa il sottoalbero radicato nel figlio C[i]
        if (!leaf)
            C[i].traverse();
    }

    // Funzione di ricerca di un nodo con chiave k in un sottoalbero
    BTreeNode search(Integer k) { // ritorna NULL se k non è presente

        // Trova la prima chiave maggiore o uguale a k
        int i = 0;
        while (i <= n && k.hashCode() > keys[i].hashCode())
            i++;


        // Se la chiave trovata è k la restituisco
        if (keys[i].hashCode() == k.hashCode())
            return this;

        // Se la chiave non è stata trovata e sono in una foglia ritorno null
        if (leaf)
            return null;

        // richiamo la funzione sul figlio
        return C[i].search(k);

    }

    public void split(BTreeNode x, BTreeNode y, int i){
        BTreeNode n = new BTreeNode(t - 1, y.leaf);
        if (t - 1 >= 0) System.arraycopy(y.keys, t, n.keys, 0, t - 1);

        if(!y.leaf){
            if (t >= 0) System.arraycopy(y.C, t, n.C, 0, t);
        }
        y.n = t-1;

        if (x.n + 1 - i + 1 >= 0) System.arraycopy(x.C, i + 2, x.C, i + 2 + 1, x.n + 1 - i + 1);

        x.C[i + 1] = n;

        if (x.n - i >= 0) System.arraycopy(x.keys, i + 1, x.keys, i + 1 + 1, x.n - i);

        x.keys[i] = y.keys[t];
        x.n = x.n + 1;
    }

    public void insertNonFull(Integer k){
        int i = n-1;
        if(leaf){
            //Cerco la posizione dove inserire la nuova chiave
            //Sposto tutti i numeri maggiori in una posizione avanti
            while(i >= 0 && keys[i].hashCode() > k.hashCode()){
                keys[i+1] = keys[i];
                i--;
            }
            //Inserisco la nuova chiave nella posizione trovata
            keys[i+1] = (K) k;
            n = n+1;

        } else{
            //trovo il figlio che avrà una nuova chiave
            while(i >= 0 && keys[i].hashCode() > k.hashCode()){
                i--;
            }

            if(C[i+1].n == 2*t-1){
                split(C[i], C[i+1], i+1);

                if(keys[i+1].hashCode() < k.hashCode())
                    i++;
            }
            C[i+1].insertNonFull(k);
        }
    }
}