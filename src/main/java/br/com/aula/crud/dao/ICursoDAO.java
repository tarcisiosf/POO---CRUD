package br.com.aula.crud.dao;

import br.com.aula.crud.model.Curso;
import br.com.aula.crud.model.Area;

import java.util.List;
import java.util.Optional;

public interface ICursoDAO {

    Curso create(Curso curso);

    void update(Curso curso);

    void delete(Long codigo);

    List<Curso> findAll();

    Optional<Curso> findById(Long codigo);

    List<Curso> findByArea(Area area);

    Optional<Curso> findBySigla(String sigla);
}
