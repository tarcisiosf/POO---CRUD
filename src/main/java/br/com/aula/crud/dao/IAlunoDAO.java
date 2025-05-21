package br.com.aula.crud.dao;

import java.util.List;
import java.util.Optional;
import br.com.aula.crud.model.Aluno;
import br.com.aula.crud.model.Cursos;
//define todos os métodos que o AlunoDAO possuirá
public interface IAlunoDAO {
    Aluno create(Aluno aluno);
    Aluno update(Aluno aluno);
    void delete(Long matricula);
    Optional<Aluno> findById(Long matricula);
    //com o optional, se a matricula não retornar nada, ñ dara erro
    List<Aluno> findByCurso(Cursos curso);
    List<Aluno> findAll();
}
