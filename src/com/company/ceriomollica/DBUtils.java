package com.company.ceriomollica;

import com.company.commons.MovidaFileException;
import com.company.commons.Movie;
import com.company.commons.Person;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class DBUtils {

    public Movie[] loadFilm(File f) {
        String[] rcFilm = new String[5];
        ArrayList<Movie> Film = new ArrayList<>();

        try {
            Scanner input = new Scanner(f);
            while (input.hasNextLine()) {//scannerizzo il file di testo fino all'esistenza di nuove linee

                for (int j = 0; j < 5; j++) {
                    String line = input.nextLine();
                    if (!line.matches("(.*):(.*)")){
                        throw new MovidaFileException();
                    }
                    rcFilm[j] = line; //salvo in un array temporaneo il contenuto della nuova linea del file
                }

                String[] filmF = this.splitRecord(rcFilm); //funzione che splitta il contenuto della linea e restituisce i valori
                //a destra che sono i valori da salvare come contenuto nei record
                Movie filmToAdd = this.createRecord(filmF); //creo un tipo Movie, e vi associo tramite la funzione createRecord,
                //i campi del record associati al film estrato dal file.
                Film.add(filmToAdd); //aggiungo alla lista di Movie il contenuto del film (di tipo Movie) estratto

                if (input.hasNextLine()) {
                    if (!input.nextLine().trim().isEmpty()) {
                        System.out.println("ATTENZIONE! Separare il contenuto dei film con una linea vuota!");
                        throw new MovidaFileException(); //richiamo un eccezione se non trovo nella riga successiva una corrispondenza *:*
                    }
                }
            }
        } catch (Exception m){
            new MovidaFileException().getMessage();
        }
        Movie[] mov = new Movie[Film.size()]; //creo una lista di Movie della dimensione del numero di film "matchati"
        return Film.toArray(mov); //restituisco la lista convertita ad array
    }

    public String[] splitRecord(String[] Film){
        String[] filmFields = new String[5];
        for(int i=0;i<5;i++) {
            String[] splittedStrFilm = Film[i].split(":");
            filmFields[i] = splittedStrFilm[1];
        }
        return filmFields;
    }


    public Movie createRecord(String[] Film){
        String title = Film[0];
        Integer year = Integer.parseInt(Film[1].trim());
        Person director = new Person(Film[2]);
        String[] castMov = Film[3].split(",");
        Person[] cast = this.getCastNames(castMov);
        Integer votes = Integer.parseInt(Film[4].trim());
        return new Movie(title,year,votes,cast,director);
    }

    public Person[] getCastNames(String[] names){
        Person[] cast = new Person[names.length];
        for(int i = 0; i < names.length; i++){
            cast[i] = new Person(names[i]);
        }
        return cast;
    }
}
