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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Stack;


public class MovidaCore implements IMovidaSearch, IMovidaConfig, IMovidaDB, IMovidaCollaborations {
    private DBUtils db_utils;
    private MyDictionary<String, Movie> movies;
    private Graph collaboration;
    private MyDictionary<String, Character> character;
    private Sorty sorts;

    MovidaCore(){
        this.sorts = new SelectionSort();
        this.db_utils = new DBUtils();
        this.movies = new HashConcatenamento<>();
        this.character = new HashConcatenamento<>();
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
    //TODO: Creare una classe Person e un relativo oggetto per salvare gli attori e il Director di ogni film
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
             String director = film.getDirector().getName().toLowerCase().replaceAll("\\s", "");
             // Cerco se il Director è già presente nel Dizionario dei Character
             // Se non dovesse essere presente lo inserisco creando un nuovo oggetto inizializzando a zero il numero di film
             Character character_director = this.character.search(director);
             if(character_director == null){
                 this.character.insert(director, new Character(director, 0));
             }

             // Cast
             for(Person p : film.getCast()){
                 String current_actor = p.getName().trim().toLowerCase().replaceAll("\\s", "");
                 Character actor = this.character.search(current_actor);

                 // Vedo se l'attore è presente nel dizionario e incremento il numero di film
                 // altrimenti lo inserisco
                 if(actor == null)
                     this.character.insert(current_actor, new Character(current_actor, 0));
                 else
                     actor.incMovie();
             }

         }

         Movie[] collab_movies = this.movies.values().toArray(new Movie[0]);
         for(Movie m : collab_movies){
             this.collaboration.populateCollaboration(m);
         }

         // test
         Person p = new Person("Katharine Towne");
         for(Person q : getTeamOf(p)){
             System.out.println(q.getName());
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
    // Testata, sembra funzionare tutto
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
        //TODO: Aggiungere altre strutture una volta implementate
    }

    /**
     * Restituisce il numero di film
     *
     * @return numero di film totali
     */
    //TODO: testare, dovrebbe essere giusta
    public int countMovies(){
        return this.movies.values().toArray().length;
    }

    /**
     * Restituisce il numero di persone
     *
     * @return numero di persone totali
     */
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
    public boolean deleteMovieByTitle(String title){
        Movie temp = this.movies.search(title);
        if(temp != null){
            // Decremento di 1 il numero di film a cui ogni attore del cast ha preso parte
            for(Person p : temp.getCast()){
                Character actor = this.character.search(p.getName());
                actor.decMovie();
            }
            this.movies.delete(temp.getTitle());
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
    public Movie getMovieByTitle(String title){
        return this.movies.search(title);
    }

    /**
     * Restituisce il record associato ad una persona, attore o regista
     *
     * @param name il nome della persona
     * @return record associato ad una persona
     */

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
    @Override
    public Person[] getTeamOf(Person actor) {
        return this.collaboration.getTeamOf(actor);
    }

    /**
     * TODO: vedere bene implementazione
     * @param actor attore di cui individuare il team
     * @return
     */
    @Override
    public Collaboration[] maximizeCollaborationsInTheTeamOf(Person actor) {
        return new Collaboration[0];
    }

    @Override
    public boolean setSort(SortingAlgorithm a) {
        boolean value = false;
        if(a == SortingAlgorithm.HeapSort){
            value = true;
            //TODO: impostare algoritmo di ordinamento heapsort
        }
        if(a == SortingAlgorithm.SelectionSort){
            value = true;
            //TODO: impostare algoritmo di ordinamento selectionsort
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

    @Override
    public Movie[] searchMoviesByTitle(String title) {
        ArrayList<Movie> m = new ArrayList<>();
        Movie[] listMov = this.movies.values().toArray(new Movie[0]);
        for (Movie mov : listMov){
            if (mov.getTitle().contains(title)){
                m.add(getMovieByTitle(mov.getTitle()));
            }
        }
	    return m.toArray(new Movie[0]);
    }

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

    @Override
    public Movie[] searchMoviesDirectedBy(String name) {
        ArrayList<Movie> m = new ArrayList<>();
        Movie[] listMov = this.movies.values().toArray(new Movie[0]);
        for(Movie mov:listMov){
            if(mov.getDirector().getName().equals(name.toLowerCase().trim().replaceAll("\\s", ""))) {
                m.add(mov);
            }
        }
        return m.toArray(new Movie[0]);
    }

    @Override
    public Movie[] searchMoviesStarredBy(String name) {
        // Creo un array di Movie con dimensione pari al numero di film a cui l'attore ha preso parte
        Movie[] m = new Movie[this.character.search(name).getNum_movies()];
        // Ottengo tutti i film presenti nel DB
        Movie[] am = this.movies.values().toArray(new Movie[0]);

        // Controllo per ogni film presente in DB se il nome dell'attore è presente nel cast e se presente
        // aggiungo il film al vettore dei film da ritornare
        int i;
        for(i = 0; i < am.length; i++){
            Person[] tmp_cast = am[i].getCast();
            for(Person tmp_act : tmp_cast){
                if (tmp_act.compareTo(new Person(name)) == 0){
                    m[i] = am[i];
                }
            }
        }
        return m;
    }

    @Override
    public Movie[] searchMostVotedMovies(Integer N) {
        Movie[] m = new Movie[N];
        Movie[] listMov = this.movies.values().toArray(new Movie[0]);
        Stack<Movie> temp = new Stack<Movie>();
        //listMov.sort("votes", listMov);

        return getMovies(N, m, listMov, temp);
    }

    @Override
    public Movie[] searchMostRecentMovies(Integer N) {
        Movie[] m = new Movie[N];
        Movie[] listMov = this.movies.values().toArray(new Movie[0]);
        Stack<Movie> temp = new Stack<Movie>();
        this.sorts.sort("year", listMov);

        return getMovies(N, m, listMov, temp);
    }

    private Movie[] getMovies(Integer N, Movie[] m, Movie[] listMov, Stack<Movie> temp) {
        for(Movie el:listMov){
            temp.push(el);
        }

        if (listMov.length <= N){
            return listMov;
        }else{
            for(int i = 0; i < N; i++){
                m[i] = (Movie) temp.get(i);
            }
            return m;
        }
    }

    @Override
    public Person[] searchMostActiveActors(Integer N) {
        Person[] c = new Person[N];
        Character[] listPeop = this.character.values().toArray(new Character[0]);
        this.sorts.sort("numFilm", listPeop);
        Stack<Character> temp = new Stack<Character>();

        for(Character el : listPeop){
            temp.push(el);
        }

        if (listPeop.length <= N){
            return listPeop;
        }else{
            for(int i = 0; i < N; i++){
                c[i] = (Person)temp.get(i);
            }
            return c;
        }
    }



    public static void main(String[] args) throws IOException {
        MovidaCore m = new MovidaCore();
        m.loadFromFile(new File("/Users/francesco/IdeaProjects/Movida/src/com/company/commons/esempio-formato-dati.txt"));
        File file = new File("test.txt");
        m.saveToFile(file);
    }

}
