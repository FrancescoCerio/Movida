package com.company.ceriomollica;

import com.company.commons.Collaboration;
import com.company.commons.IMovidaCollaborations;
import com.company.commons.Movie;
import com.company.commons.Person;

import java.util.*;

public class Graph implements IMovidaCollaborations {

    private HashMap<Person, ArrayList<Collaboration>> graph;

    Graph(){
        this.graph = new HashMap<>();
    }

    // TESTATA
    // Funzione per creare l'insieme delle collaborazioni nel grafo
    public void populateCollaboration(Movie movie){
        for(Person first_actor : movie.getCast()){
            //this.graph.computeIfAbsent(first_actor, k -> new ArrayList<>());
            if (!this.graph.containsKey(first_actor)){//se l'attore non è ancora stato aggiunto
                this.graph.put(first_actor,new ArrayList<>());//aggiungilo
            }
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

    // TESTATA
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
    //TODO: testare
    @Override
    public Person[] getTeamOf(Person actor) {

        // Ricerco la lista degli attori diretti che collaborano con l'attore passato come parametro
        Person[] direct_collabs = getDirectCollaboratorsOf(actor);
        ArrayList<Person> actor_list = new ArrayList<>();

        for(Person p : direct_collabs){
            Person[] indirect_collabs = getDirectCollaboratorsOf(p);
            actor_list.add(p);
            for(Person q : indirect_collabs){
                if(!actor_list.contains(q)){
                    System.out.println(q);
                    actor_list.add(q);
                }
            }
        }
        Person[] total_collabs = new Person[actor_list.size()];
        int i = 0;
        for(Person a : actor_list){
            total_collabs[i] = a;
            i++;
        }

        return total_collabs;

        /*
        ArrayList<Collaboration> direct_collabs = this.graph.get(actor);
        ArrayList<ArrayList<Collaboration>> all_collabs = new ArrayList<>();
        all_collabs.add(direct_collabs);

        for(Collaboration co : direct_collabs){
            ArrayList<Collaboration> indirect_collabs = this.graph.get(co.getActorB());
            all_collabs.add(indirect_collabs);
        }
        Person[] team = new Person[all_collabs.size()];
        team[0] = actor;
        int i = 1;
        for(ArrayList<Collaboration> c : all_collabs){
            for(Collaboration tmp : c){
                team[i] = tmp.getActorB();
                i++;
            }
        }

        return team;
         */
    }

    @Override
    public Collaboration[] maximizeCollaborationsInTheTeamOf(Person actor) {
        return new Collaboration[0];
    }
}

