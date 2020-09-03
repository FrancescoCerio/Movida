/*
 * Copyright (C) 2020 - Francesco Cerio, Francesco Mollica
 *
 * Progetto Movida.
 * Corso di Algoritmi e Strutture Dati
 * Laurea in Informatica, UniBO, a.a. 2019/2020
 *
 */
package com.company.ceriomollica;

import com.company.commons.Person;


/** Questa classe estende la classe Person e serve ad identificare le persone
 * che hanno partecipato ad un film (Attori, Direttori, Cast).
 *
 * E' stato aggiunto il campo num_movies per tener conto del numero di film
 * a cui un personaggio (Character) ha preso parte.
 */

public class Character extends Person implements CustomComparable{

    private Integer num_movies;
    private Boolean isActor; // valore booleano per identificare se un Character è un attore

    public Character(String name, Integer num_movies, Boolean isActor) {
        super(name);
        this.num_movies = num_movies;
        this.isActor = isActor;
    }

    public Integer getNum_movies() {
        return num_movies;
    }

    public void incMovie(){
        this.num_movies++;
    }

    public void decMovie(){
        this.num_movies--;
    }

    public boolean isActor(){return this.isActor;}

    @Override
    public int customCompare(String param, CustomComparable c) {
        if(param.equals("numFilm"))
            return this.getNum_movies().compareTo(((Character) c).getNum_movies());
        if(param.equals("name"))
            return this.getName().compareTo(((Character) c).getName());
        return 0;
    }

    @Override
    public int compareTo(Object o) {
        return super.compareTo(o);
    }
}
