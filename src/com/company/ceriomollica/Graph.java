package com.company.ceriomollica;

import com.company.commons.Collaboration;
import com.company.commons.IMovidaCollaborations;
import com.company.commons.Movie;
import com.company.commons.Person;

import java.util.*;

public class Graph implements IMovidaCollaborations {

    HashMap<Person, ArrayList<Collaboration>> graph = new HashMap<>();

    // Funzione per caricare creare l'insieme delle collaborazioni nel grafo
    public void populateCollaboration(Movie movie){
        for(Person first_actor : movie.getCast()){
            this.graph.computeIfAbsent(first_actor, k -> new ArrayList<>());

            // Per ogni persona del cast controllo creo le collaborazioni necessarie
            for(Person other_actor : movie.getCast()){
                // Controllo se l'attore preso in considerazione ora sia già presente
                if(!other_actor.equals(first_actor)){
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
        System.out.println(this.graph.size());
        System.out.println(collabs.isEmpty());
        System.out.println(collabs.size());
        Person[] direct_collaborators = new Person[collabs.size()];

        int i = 0;
        for(Collaboration c : collabs){
            direct_collaborators[i] = c.getActorB();
            i++;
        }
        return direct_collaborators;
    }

    @Override
    public Person[] getTeamOf(Person actor) {
        return new Person[0];
    }

    @Override
    public Collaboration[] maximizeCollaborationsInTheTeamOf(Person actor) {
        return new Collaboration[0];
    }
}

