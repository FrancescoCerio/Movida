/*
 * Copyright (C) 2020 - Francesco Cerio, Francesco Mollica
 *
 * Progetto Movida.
 * Corso di Algoritmi e Strutture Dati
 * Laurea in Informatica, UniBO, a.a. 2019/2020
 *
 */
package com.company.ceriomollica;

import com.company.commons.Collaboration;
import com.company.commons.IMovidaCollaborations;
import com.company.commons.Movie;
import com.company.commons.Person;

import javax.lang.model.type.UnionType;
import javax.management.MBeanFeatureInfo;
import java.util.*;

public class Graph implements IMovidaCollaborations {

    private HashMap<Person, ArrayList<Collaboration>> graph;

    Graph(){
        this.graph = new HashMap<>();
    }

    // Funzione per creare l'insieme delle collaborazioni nel grafo
    public void populateCollaboration(Movie movie){
        for(Person first_actor : movie.getCast()){
            this.graph.computeIfAbsent(first_actor, k -> new ArrayList<>());

            // Per ogni persona del cast creo le collaborazioni necessarie
            for(Person other_actor : movie.getCast()){

                // Controllo se l'attore preso in considerazione ora sia già presente
                if(!first_actor.equals(other_actor)){
                    ArrayList<Collaboration> collabs = this.graph.get(first_actor);

                    Collaboration c = new Collaboration(first_actor, other_actor);
                    // Controllo se la collaborazione è già presente
                    if(collabs.contains(c)){
                        // Nel caso fosse vero prendo il suo indice e aggiungo il movie corrente alla lista di movies
                        int i = collabs.indexOf(c);
                        collabs.get(i).addMovie(movie);
                    } else {
                        // Altrimenti aggiungo il movie corrente e inserisco la collaborazione nella lista di collaborazioni dell'attore
                        c.addMovie(movie);
                        collabs.add(c);
                    }
                }
            }
        }

    }

    @Override
    public Person[] getDirectCollaboratorsOf(Person actor) {
        ArrayList<Collaboration> collabs = this.graph.get(actor);
        Person[] direct_collaborators = new Person[collabs.size()];

        int i = 0;
        for(Collaboration c : collabs){
            direct_collaborators[i] = c.getActorB();
            i++;
        }
        return direct_collaborators;
    }

    /**
     * Implemento una ricerca che, partendo dall'attore passato come parametro, prendo la lista dei collaboratori diretti
     * e per ognuno effettuo la ricerca dei propri collaboratori.
     *
     * @param actor attore di cui individuare il team
     * @return il grafo di tutte le collaborazioni (dirette e indirette) dell'attore passato come parametro
     */
    // TESTATO
    @Override
    public Person[] getTeamOf(Person actor) {
        ArrayList<Person> actor_list = new ArrayList<>();
        ArrayDeque<Person> actor_to_visit = new ArrayDeque<>();
        ArrayList<Person> visited = new ArrayList<>(); // lista di attori già visitati
        actor_to_visit.add(actor);
        // Itero nella coda finchè non è vuota e per ogni attore presente ricerco i suoi diretti collaboratori,
        // li inserisco nella coda se non sono già presenti e infine li aggiungo alla lista di attori che fanno
        // parte del team

        while(!actor_to_visit.isEmpty()){
            Person p = actor_to_visit.poll();
            Person[] indirect_collabs = getDirectCollaboratorsOf(p); // Ottengo la lista di diretti collaboratori dell'attore corrente

            // Per ogni attore presente nella lista dei diretti collaboratori effettuo delle operazioni di controllo
            // utilizzando le tre liste sopra definite:
            //  - Nella lista actor_to_visit salvo gli attori da visitare e controllo se ci sono doppioni, in caso positivo non li aggiungo
            //  - Nella lista visited salvo i nomi degli attori già visitati in modo da non controllare due volte lo stesso attore
            //  - Nella lista actor_list salvo la lista completa del team
            for(Person q: indirect_collabs) {
                if(!visited.contains(q)){
                    if (!actor_to_visit.contains(q)) {
                        actor_to_visit.add(q);
                        if (!actor_list.contains(q)) {
                            actor_list.add(q);
                        }
                    }
                    visited.add(q);
                }
            }
        }

        // Definisco l'array di attori che compongono il team
        Person[] total_collabs = new Person[actor_list.size()];
        int i = 1;
        total_collabs[0] = actor_list.get(actor_list.indexOf(actor));
        for(Person a : actor_list){
            if(!a.equals(actor)){
                total_collabs[i] = a;
                i++;
            }
        }

        return total_collabs;
    }

    @Override
    public Collaboration[] maximizeCollaborationsInTheTeamOf(Person actor) {
        Person[] team = getDirectCollaboratorsOf(actor);
        // Popolo la lista delle collaborazioni di ogni componente del team
        ArrayList<Collaboration> c = new ArrayList<>(this.graph.get(actor));

        // Ordino la lista dei collaboratori dell'attore in ordine decrescente e la inserisco in un ArrayDeque
        Comparator<Collaboration> compare = Comparator.comparing(Collaboration::getScore);
        c.sort(compare);
        Collections.reverse(c);
        ArrayDeque<Collaboration> to_maximize = new ArrayDeque<>(c);

        return maxICC(to_maximize);
    }


    // Funzione ausiliaria per la ricerca del IIC(T)
    Collaboration[] maxICC(ArrayDeque<Collaboration> collab_array){
        ArrayList<Collaboration> maximize = new ArrayList<>();
        ArrayList<Person> visited = new ArrayList<>();

        while(!collab_array.isEmpty()) {
            Collaboration p = collab_array.poll();

            if(!visited.contains(p.getActorB())){
                visited.add(p.getActorA());
                visited.add(p.getActorB());
                maximize.add(p);
                for(Collaboration c:this.graph.get(p.getActorB())){
                    if(!visited.contains(c.getActorB())){
                        collab_array.add(c);
                    }
                }
            }

        }
        return maximize.toArray(new Collaboration[0]);
    }
}

