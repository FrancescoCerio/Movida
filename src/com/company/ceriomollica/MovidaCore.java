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


public class MovidaCore implements IMovidaSearch, IMovidaConfig, IMovidaDB, IMovidaCollaborations {
    private DBUtils db_utils;
    private MyDictionary<String, Movie> movies;
    private Graph<Object> collaboration;
    //TODO: implementare dizionari per movie e persone da usare come storage interno

    MovidaCore(){
        this.db_utils = new DBUtils();
        this.movies = new HashConcatenamento<String, Movie>();
        //this.t = new BTree<String, Movie>();
        this.collaboration = new Graph<>();
    }

    /** GESTIONE DEL DATABASE **/

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
                    bw.write("Title: " + "|" + movie.getTitle() + "|");
                    bw.newLine();
                    bw.write("Year: " + "|" + movie.getYear().toString() + "|");
                    bw.newLine();
                    bw.write("Director: " + "|" + movie.getDirector().getName() + "|");
                    bw.newLine();
                    /* TODO: La funzione getPersonCast() ritorna un unica stringa con i nomi degli attori
                    quando dovrebbe ritornare il nome di ogni singolo Attore e dopo concatenarli con la virgola (credo)
                    */
                    bw.write("Cast: " + movie.getPersonCast());
                    bw.newLine();
                    bw.write("Votes: " + "|" + movie.getVotes().toString() + "|");
                    bw.newLine();
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
        //return this.lhm.values().toArray().length;
    }

    /**
     * Restituisce il numero di persone
     *
     * @return numero di persone totali
     */
    public int countPeople(){
        return 0;
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
        return null;
    }


    /**
     * Restituisce il vettore di tutti i film
     *
     * @return array di film
     */
    public Movie[] getAllMovies(){
        return null;
    }

    /**
     * Restituisce il vettore di tutte le persone
     *
     * @return array di persone
     */
    public Person[] getAllPeople(){
        return null;
    }

    /**
     * Per questa ricerca bisogna trovare i diretti collaboratori di un attore passato come parametro.
     * Effettuo una ricerca in ampiezza fermandomi al primo grado
     * di attraversamento del grafo avente come radice l'attore.
     *
     * @param actor attore di cui cercare i collaboratori diretti
     * @return
     */
    @Override
    public Person[] getDirectCollaboratorsOf(Person actor) {
        return new Person[0];
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
        return new Person[0];
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
        if(m == MapImplementation.BTree){
            value = true;
            this.movies = new BTree<>();
            //TODO: impostare algoritmo di dizionario BTree
        }
        if(m == MapImplementation.HashConcatenamento){
            value = true;
            this.movies = new HashConcatenamento<>();
            //TODO: impostare algoritmo di dizionario HashConcatenamento
        }
        return value;
    }

    @Override
    public Movie[] searchMoviesByTitle(String title) {
        return new Movie[0];
    }

    @Override
    public Movie[] searchMoviesInYear(Integer year) {
        return new Movie[0];
    }

    @Override
    public Movie[] searchMoviesDirectedBy(String name) {
        return new Movie[0];
    }

    @Override
    public Movie[] searchMoviesStarredBy(String name) {
        return new Movie[0];
    }

    @Override
    public Movie[] searchMostVotedMovies(Integer N) {
        return new Movie[0];
    }

    @Override
    public Movie[] searchMostRecentMovies(Integer N) {
        return new Movie[0];
    }

    @Override
    public Person[] searchMostActiveActors(Integer N) {
        return new Person[0];
    }

    /** Funzione main per il test
     * Al momento loadFromFile() e saveToFile() funzionano :D
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        MovidaCore m = new MovidaCore();
        m.loadFromFile(new File("/Users/francesco/IdeaProjects/Movida/src/com/company/commons/esempio-formato-dati.txt"));
        File file = new File("test.txt");
        m.saveToFile(file);
    }
}
