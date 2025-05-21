package br.com.aula.crud.model;

import java.io.Serializable;

public enum Cursos implements Serializable {
    ADS("Análise e Desenvolvimento de Sistemas"),
    ECMP("Engenharia de Computação"),
    CCMP("Ciências da Computação"),
    EQ("Engenharia Química"),
    OUTROS("Outros");

    private String nomeCurso;
    private Cursos(String nomeCurso){ this.nomeCurso = nomeCurso; }
    public String nomeCurso(){ return this.nomeCurso; }
}
