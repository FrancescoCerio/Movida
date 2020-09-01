/* 
 * Copyright (C) 2020 - Angelo Di Iorio
 * 
 * Progetto Movida.
 * Corso di Algoritmi e Strutture Dati
 * Laurea in Informatica, UniBO, a.a. 2019/2020
 * 
*/
package com.company.commons;
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
public class Person {

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
		Person p=(Person) obj;
		return this.nameNormalize().equals(p.nameNormalize());
	}


	@Override
	public int hashCode() {
		return this.nameNormalize().hashCode();
	}

}
