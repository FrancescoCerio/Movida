/*
 * Copyright (C) 2020 - Angelo Di Iorio
 *
 * Progetto Movida.
 * Corso di Algoritmi e Strutture Dati
 * Laurea in Informatica, UniBO, a.a. 2019/2020
 *
 */
package com.company.ceriomollica;
import com.company.commons.*;

import java.io.*;
import java.util.*;


public class MovidaCore implements IMovidaSearch, IMovidaConfig, IMovidaDB, IMovidaCollaborations {
    private DBUtils db_utils;
    private MyDictionary<String, Movie> movies;
    private Graph collaboration;
    private MyDictionary<String, Character> character;
    private Sorty sorts;

    MovidaCore(){
        this.sorts = new HeapSort();
        this.db_utils = new DBUtils();
        this.movies = new BTree<>();
        this.character = new BTree<>();
        this.collaboration = new Graph();
    }

    /**
     * Carica i dati da un file, organizzato secondo il formato MOVIDA (vedi esempio-formato-dati.txt)
     *
     * Un film è identificato in modo univoco dal titolo (case-insensitive), una persona dal nome (case-insensitive).
     * Semplificazione: non sono gestiti omonimi e film con lo stesso titolo.
     *
     * I nuovi dati sono aggiunti a quelli già caricati.
     *
     * Se esiste un film con lo stesso titolo il record viene sovrascritto.
     * Se esiste una persona con lo stesso nome non ne viene creata un'altra.
     *
     * Se il file non rispetta il formato, i dati non sono caricati e
     * viene sollevata un'eccezione.
     *
     * @param f il file da cui caricare i dati
     *
     * @throws MovidaFileException in caso di errore di caricamento
     */
    //Testata fin ora, sembra funzionare tutto
     public void loadFromFile(File f) {
         // Chiamo loadFilm() che controla se il file rispetta il formato e carica i dati in un array Movie
         Movie[] movies = this.db_utils.loadFilm(f);
         for(Movie film: movies){
             // Controllo se esiste un film nel DB già caricato con il seguente titolo
             String title = film.getTitle().toLowerCase().trim().replaceAll("\\s", "");
             if (this.movies.search(title) != null) {
                 this.movies.delete(title);
             }
             this.movies.insert(title, film);

             // Popolo il dizionario dei Character per ogni persona presente all'interno di un film

             // Director
             String director = film.getDirector().getName().trim();
             // Cerco se il Director è già presente nel Dizionario dei Character
             // Se non dovesse essere presente lo inserisco creando un nuovo oggetto inizializzando a zero il numero di film
             Character character_director = this.character.search(director);
             if(character_director == null){
                 this.character.insert(director, new Character(director, 0, false));
             }

             // Cast
             for(Person p : film.getCast()){
                 String current_actor = p.getName();
                 Character actor = this.character.search(current_actor);

                 // Vedo se l'attore è presente nel dizionario e incremento il numero di film
                 // altrimenti lo inserisco
                 if(actor == null)
                     this.character.insert(current_actor, new Character(current_actor, 0, true));
                 else
                     actor.incMovie();
             }

         }

         Movie[] collab_movies = this.movies.values().toArray(new Movie[0]);
         for(Movie m : collab_movies){
             this.collaboration.populateCollaboration(m);
         }
     }


    /**
     * Salva tutti i dati su un file.
     *
     * Il file è sovrascritto.
     * Se non è possibile salvare, ad esempio per un problema di permessi o percorsi,
     * viene sollevata un'eccezione.
     *
     * @param f il file su cui salvare i dati
     *
     * @throws MovidaFileException in caso di errore di salvataggio
     */
    // Testata
    public void saveToFile(File f){
        try {
            // Controllo i permessi di scrittura
            if(f.canWrite()){
                // Uso un BufferedWriter passando un FileWriter con append settato a false
                // in modo da sovrascrivere dall'inizio il nuovo file
                BufferedWriter bw = new BufferedWriter(new FileWriter(f.getName(), false));
                Movie[] m = this.movies.values().toArray(new Movie[0]);
                for (Movie movie : m) {
                    bw.write("Title: " + movie.getTitle());
                    bw.newLine();
                    bw.write("Year: " + movie.getYear().toString());
                    bw.newLine();
                    bw.write("Director: " + movie.getDirector().getName());
                    bw.newLine();
                    bw.write("Cast: " + movie.getPersonCast());
                    bw.newLine();
                    bw.write("Votes: " + movie.getVotes().toString());
                    bw.newLine();
                    bw.newLine(); // Aggiungo una linea per separare i campi
                }
                bw.close();
            }

        }
        catch(MovidaFileException | IOException e){
            e.getMessage();
            e.printStackTrace();
        }
    }



    /**
     * Cancella tutti i dati.
     *
     * Sar� quindi necessario caricarne altri per proseguire.
     */
    // Cancello i dati delle strutture inizializzando di nuovo le strutture presenti
    public void clear(){
        this.db_utils = new DBUtils();
        this.movies = new HashConcatenamento<>();
        this.character = new HashConcatenamento<>();
        this.collaboration = new Graph();
        this.sorts = new SelectionSort();
    }

    /**
     * Restituisce il numero di film
     *
     * @return numero di film totali
     */
    // TESTATA
    public int countMovies(){
        return this.movies.values().toArray().length;
    }

    /**
     * Restituisce il numero di persone
     *
     * @return numero di persone totali
     */
    // TESTATA
    public int countPeople(){
        return this.character.values().size();
    }

    /**
     * Cancella il film con un dato titolo, se esiste.
     *
     * @param title titolo del film
     * @return <code>true</code> se il film � stato trovato e cancellato,
     * 		   <code>false</code> in caso contrario
     */
    // TESTATA
    public boolean deleteMovieByTitle(String title){
        Movie temp = this.movies.search(title.trim().toLowerCase().replaceAll("\\s", ""));

        if(temp != null){
            System.out.println(temp.getTitle());
            // Decremento di 1 il numero di film a cui ogni attore del cast ha preso parte
            for(Person p : temp.getCast()){
                Character actor = this.character.search(p.getName());
                actor.decMovie();
            }
            Movie m = this.movies.delete(temp.getTitle().trim().toLowerCase().replaceAll("\\s", ""));
            return true;
        } else
            return false;
    }

    /**
     * Restituisce il record associato ad un film
     *
     * @param title il titolo del film
     * @return record associato ad un film
     */
    // TESTATA
    public Movie getMovieByTitle(String title){
        return this.movies.search(title);
    }

    /**
     * Restituisce il record associato ad una persona, attore o regista
     *
     * @param name il nome della persona
     * @return record associato ad una persona
     */
    // TESTATA
    public Person getPersonByName(String name){
        return this.character.search(name);
    }


    /**
     * Restituisce il vettore di tutti i film
     *
     * @return array di film
     */
    public Movie[] getAllMovies(){
        return this.movies.values().toArray(new Movie[0]);
    }

    /**
     * Restituisce il vettore di tutte le persone
     *
     * @return array di persone
     */
    // TESTATA
    public Person[] getAllPeople(){
        return this.character.values().toArray(new Person[0]);
    }

    /**
     * Per questa ricerca bisogna trovare i diretti collaboratori di un attore passato come parametro.
     * Effettuo una ricerca dell'attore passato come parametro e scorro la lista dei suoi collaboratori
     *
     * @param actor attore di cui cercare i collaboratori diretti
     * @return record associato all'array di Person
     */
    // TESTATA
    @Override
    public Person[] getDirectCollaboratorsOf(Person actor) {
        return this.collaboration.getDirectCollaboratorsOf(actor);
    }

    /**
     * Restituisco il team di attori (diretti e indiretti) che hanno collaborato con l'attore passato come parametro.
     * Effettuo una ricerca in ampiezza totale.
     *
     * @param actor attore di cui individuare il team
     * @return
     */
    // TESTATA
    @Override
    public Person[] getTeamOf(Person actor) {
        return this.collaboration.getTeamOf(actor);
    }

    /**
     * TODO: implementare
     * @param actor attore di cui individuare il team
     * @return
     */
    @Override
    public Collaboration[] maximizeCollaborationsInTheTeamOf(Person actor) {
        return this.collaboration.maximizeCollaborationsInTheTeamOf(actor);
    }

    @Override
    public boolean setSort(SortingAlgorithm a) {
        boolean value = false;
        if(a == SortingAlgorithm.HeapSort){
            value = true;
            this.sorts = new HeapSort();
        }
        if(a == SortingAlgorithm.SelectionSort){
            value = true;
            this.sorts = new SelectionSort();
        }
        return value;
    }

    @Override
    public boolean setMap(MapImplementation m) {
        boolean value = false;
        if(m == MapImplementation.BTree && this.movies instanceof BTree){
            value = true;
            this.movies = new BTree<>();
        }
        if(m == MapImplementation.HashConcatenamento  && this.movies instanceof HashConcatenamento){
            value = true;
            this.movies = new HashConcatenamento<>();
        }
        return value;
    }

    // TESTATO
    @Override
    public Movie[] searchMoviesByTitle(String title) {
        ArrayList<Movie> m = new ArrayList<>();
        Movie[] listMov = this.movies.values().toArray(new Movie[0]);

        for (Movie mov : listMov){
            if (mov.getTitle().toLowerCase().replaceAll("\\s", "").contains(title.toLowerCase().trim().replaceAll("\\s", ""))){
                m.add(getMovieByTitle(mov.getTitle().trim().toLowerCase().replaceAll("\\s", "")));
            }
        }
        return m.toArray(new Movie[0]);
    }

    // TESTATO
    @Override
    public Movie[] searchMoviesInYear(Integer year) {
        ArrayList<Movie> m = new ArrayList<>();
        Movie[] listMov = this.movies.values().toArray(new Movie[0]);
        for(Movie mov:listMov){
            if(mov.getYear().equals(year)) {
                m.add(mov);
            }
        }
	    return m.toArray(new Movie[0]);
    }

    // TESTATA
    @Override
    public Movie[] searchMoviesDirectedBy(String name) {
        ArrayList<Movie> m = new ArrayList<>();
        Movie[] listMov = this.movies.values().toArray(new Movie[0]);
        for(Movie mov:listMov){
            if(mov.getDirector().getName().toLowerCase().replaceAll("\\s", "").equals(name.toLowerCase().trim().replaceAll("\\s", ""))) {
                m.add(mov);
            }
        }
        return m.toArray(new Movie[0]);
    }

    // TESTATA
    @Override
    public Movie[] searchMoviesStarredBy(String name) {
        // Creo un array di Movie in cui salvare la lista di film a cui l'attore ha preso parte
        ArrayList<Movie> m = new ArrayList<>();
        // Ottengo tutti i film presenti nel DB
        ArrayList<Movie> am = this.movies.values();

        // Controllo per ogni film presente in DB se il nome dell'attore è presente nel cast e se presente
        // aggiungo il film al vettore dei film da ritornare
        int i = 0;
        for(Movie mo : am){
            Person[] tmp_cast = mo.getCast();
            for(Person tmp_act : tmp_cast){
                if (tmp_act.compareTo(new Person(name)) == 0){
                    m.add(mo);
                }
                i++;
            }
        }

        return m.toArray(new Movie[0]);
    }

    // TESTATA
    @Override
    public Movie[] searchMostVotedMovies(Integer N) {
        Movie[] m = new Movie[N];
        Movie[] listMov = this.movies.values().toArray(new Movie[0]);
        this.sorts.sort("votes", listMov);

        // Definisco la lista dei valori e chiamo reverse() in modo da ordinare in ordine decrescente i valori
        List<Movie> temp = Arrays.asList(listMov);
        Collections.reverse(temp);
        return getMovies(N, m, listMov, temp);
    }

    // TESTATA
    @Override
    public Movie[] searchMostRecentMovies(Integer N) {
        Movie[] m = new Movie[N];
        Movie[] listMov = this.movies.values().toArray(new Movie[0]);

        this.sorts.sort("year", listMov);
        List<Movie> temp = Arrays.asList(listMov);
        Collections.reverse(temp);
        return getMovies(N, m, listMov, temp);
    }

    // Funzione ausiliaria per le funzioni di ricerca searchMostVotedMovies e searchMostRecentMovies
    // riduce la ridondanza di codice
    private Movie[] getMovies(Integer N, Movie[] m, Movie[] listMov, List<Movie> temp) {

        if (listMov.length <= N){
            return listMov;
        }else{
            for(int i = 0; i < N; i++){
                m[i] = temp.get(i);
            }
            return m;
        }
    }

    // TESTATA
    @Override
    public Person[] searchMostActiveActors(Integer N) {
        Person[] c = new Person[N];
        Character[] listPeop = this.character.values().toArray(new Character[0]);
        List<Character> temp = new ArrayList<>();

        int j;
        for(j = 0; j < listPeop.length; j++){
            if(listPeop[j].isActor()) {
                temp.add(listPeop[j]);
                j++;
            }
        }

        this.sorts.sort("numFilm", listPeop);

        Collections.reverse(temp);

        if (listPeop.length <= N){
            return listPeop;
        }else{
            for(int i = 0; i < N; i++){
                c[i] = temp.get(i);
            }
            return c;
        }
    }

    // Funzione main utilizzata per il test delle funzioni
    public static void main(String[] args) {
        MovidaCore m = new MovidaCore();
        m.loadFromFile(new File("/Users/francesco/IdeaProjects/Movida/src/com/company/commons/esempio-formato-dati.txt"));

        Collaboration[] co = m.maximizeCollaborationsInTheTeamOf(new Person("robertdeniro"));
        /*
        for(Collaboration c : co){
            System.out.println(c.getActorA().getName() + " -> " + c.getActorB().getName() + ": " + c.getScore());
        }

         */

        Movie[] d = m.getAllMovies();
        for(Movie h : d){
            System.out.println(h.getDirector().getName());
        }

        Movie moo = m.getMovieByTitle("taxidriver");
        System.out.println(moo.getYear());

        File file = new File("test.txt");
        m.saveToFile(file);
    }

}
