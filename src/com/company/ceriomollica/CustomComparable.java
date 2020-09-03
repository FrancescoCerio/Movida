/*
 * Copyright (C) 2020 - Francesco Cerio, Francesco Mollica
 *
 * Progetto Movida.
 * Corso di Algoritmi e Strutture Dati
 * Laurea in Informatica, UniBO, a.a. 2019/2020
 *
 */
package com.company.ceriomollica;

public interface CustomComparable extends Comparable {
    int customCompare(String param, CustomComparable c);
}
