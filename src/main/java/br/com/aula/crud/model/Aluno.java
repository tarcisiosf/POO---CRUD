package br.com.aula.crud.model;

public class Aluno {
    private String nome;
    private Long matricula;
    boolean maioridade;
    Cursos curso;
    String sexo;

    public Aluno(Long matricula ,String nome, boolean maioridade, Cursos curso, String sexo) {
        this.nome = nome;
        this.matricula = matricula;
        this.maioridade = maioridade;
        this.curso = curso;
        this.sexo = sexo;
    }

    public Aluno() {

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getMatricula() {
        return matricula;
    }

    public void setMatricula(Long matricula) {
        this.matricula = matricula;
    }

    public boolean isMaioridade() {
        return maioridade;
    }

    public void setMaioridade(boolean maioridade) {
        this.maioridade = maioridade;
    }

    public Cursos getCurso() {
        return curso;
    }

    public void setCurso(Cursos curso) {
        this.curso = curso;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }
}

