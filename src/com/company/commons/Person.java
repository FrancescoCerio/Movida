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
 * Classe usata per rappresentare una persona, attore o regista,
 * nell'applicazione Movida.
 * 
 * Una persona � identificata in modo univoco dal nome 
 * case-insensitive, senza spazi iniziali e finali, senza spazi doppi. 
 * 
 * Semplificazione: <code>name</code> � usato per memorizzare il nome completo (nome e cognome)
 * 
 * La classe pu� essere modicata o estesa ma deve implementare il metodo getName().
 * 
 */
public class Person implements CustomComparable {

	private String name;
	
	public Person(String name) {
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}

	public String nameNormalize(){
		return this.name.toLowerCase().trim().replaceAll("\\s","");
	}

	@Override
	public boolean equals(Object obj) {
		return this.nameNormalize().equals(((Person) obj).nameNormalize());
	}


	@Override
	public int hashCode() {
		return this.nameNormalize().hashCode();
	}

	@Override
	public int customCompare(String param, CustomComparable c) {
		if(param.equals("name"))
			return this.getName().compareTo(((Person) c).getName());
		else return 0;
	}

	@Override
	public int compareTo(Object o) {
		return this.nameNormalize().compareTo(((Person) o).nameNormalize());
	}
}
