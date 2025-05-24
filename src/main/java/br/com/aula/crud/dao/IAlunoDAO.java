package br.com.aula.crud.dao;

import br.com.aula.crud.model.Aluno;
import br.com.aula.crud.model.Curso;

import java.util.List;
import java.util.Optional;


public interface IAlunoDAO {

    Aluno create(Aluno aluno);

    void update(Aluno aluno);

    void delete(Long matricula);

    List<Aluno> findAll();

    Optional<Aluno> findById(Long matricula);

    List<Aluno> findByCurso(Curso curso);

    List<Aluno> findBySexo(String sexo);
}
