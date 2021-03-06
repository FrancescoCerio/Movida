/* 
 * Copyright (C) 2020 - Angelo Di Iorio
 * 
 * Progetto Movida.
 * Corso di Algoritmi e Strutture Dati
 * Laurea in Informatica, UniBO, a.a. 2019/2020
 * 
*/
package com.company.commons;

import com.company.ceriomollica.CustomComparable;

/**
 * Classe usata per rappresentare un film
 * nell'applicazione Movida.
 * 
 * Un film � identificato in modo univoco dal titolo 
 * case-insensitive, senza spazi iniziali e finali, senza spazi doppi. 
 * 
 * La classe pu� essere modicata o estesa ma deve implementare tutti i metodi getter
 * per recupare le informazioni caratterizzanti di un film.
 * 
 */
public class Movie implements CustomComparable {
	
	private String title;
	private Integer year;
	private Integer votes;
	private Person[] cast;
	private Person director;
	
	public Movie(String title, Integer year, Integer votes,
			Person[] cast, Person director) {
		this.title = title;
		this.year = year;
		this.votes = votes;
		this.cast = cast;
		this.director = director;
	}

	public String getTitle() {
		return this.title;
	}

	public Integer getYear() {
		return this.year;
	}

	public Integer getVotes() {
		return this.votes;
	}

	public Person[] getCast() {
		return this.cast;
	}

	public Person getDirector() {
		return this.director;
	}

	// Ritorno il cast di un film come stringa formattata con le virgole
	public String getPersonCast(){
		StringBuilder actors = new StringBuilder();
		for (Person person : this.cast) {
			actors.append(person.getName()).append(", ");
		}
		return actors.toString().substring(0, actors.toString().length() - 2);
	}

	// Funzione ausiliaria che utilizzo per il confronto sui parametri di "year" o "votes"
	// Utile per le funzioni dichiarate nell'interfaccia IMovidaSearch
	@Override
	public int customCompare(String param, CustomComparable c) {
		if(param.equals("votes"))
			return this.getVotes().compareTo(((Movie) c).getVotes());
		else
			return this.getYear().compareTo(((Movie) c).getYear());
	}

	@Override
	public int compareTo(Object o) {
		return this.getTitle().trim().toLowerCase().replaceAll("\\s", "").compareTo(((Movie) o).getTitle().trim().toLowerCase().replaceAll("\\s", ""));
	}
}
