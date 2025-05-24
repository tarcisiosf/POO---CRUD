package br.com.aula.crud.model;

import java.time.LocalDate;


public class Aluno {

    private Long    matricula;
    private String  nome;
    private boolean maioridade;
    private Curso   curso;
    private String  sexo;
    private LocalDate dataNascimento;

    public Aluno() { }

    public Aluno(Long matricula, String nome, boolean maioridade, Curso curso, String sexo) {
        this.matricula   = matricula;
        this.nome        = nome;
        this.maioridade  = maioridade;
        this.curso       = curso;
        this.sexo        = sexo;
    }


    public Long getMatricula() {
        return matricula;
    }

    public void setMatricula(Long matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isMaioridade() {
        return maioridade;
    }

    public void setMaioridade(boolean maioridade) {
        this.maioridade = maioridade;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    @Override
    public String toString() {
        return String.format("%d: %s – %s (%s)",
                matricula, nome, curso.getSigla(), sexo);
    }
}
