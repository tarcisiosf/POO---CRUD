package br.com.aula.crud.dao;

import br.com.aula.crud.config.ConnectionFactory;
import br.com.aula.crud.model.Aluno;
import br.com.aula.crud.model.Cursos;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AlunoDAO implements IAlunoDAO {
    @Override
    public Aluno create(Aluno aluno) {
        try (Connection connection = ConnectionFactory.getConnection();) {
            String query = "INSERT INTO alunos (nome, maioridade, curso, sexo) VALUES (?, ?, ?, ? )";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, aluno.getNome());
            statement.setBoolean(2, aluno.isMaioridade());
            statement.setString(3, aluno.getCurso().toString());
            statement.setString(4, aluno.getSexo());
            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            Long matricula = resultSet.getLong(1);
            aluno.setMatricula(matricula);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return aluno;
    }

    @Override
    public Aluno update(Aluno aluno) {
        return null;
    }

    @Override
    public void delete(Long matricula) {

    }

    @Override
    public List<Aluno> findAll() {
        String query = "SELECT * FROM alunos";
        List<Aluno> lista = new ArrayList<>();
        try (Connection connection = ConnectionFactory.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeQuery();
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Aluno aluno = new Aluno();
                aluno.setMatricula(rs.getLong("matricula"));
                aluno.setNome(rs.getString("nome"));
                aluno.setMaioridade(rs.getBoolean("maioridade"));
                aluno.setCurso(Cursos.valueOf(rs.getString("curso")));
                aluno.setSexo(rs.getString("sexo"));
                lista.add(aluno);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lista;
    }

    @Override
    public Optional<Aluno> findById(Long matricula) {
        String query = "SELECT * FROM alunos WHERE matricula = ?";
        Aluno aluno;
        try (Connection connection = ConnectionFactory.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, matricula);
            statement.executeQuery();
            ResultSet rs = statement.executeQuery();
            rs.next();
            aluno = new Aluno(
                    rs.getLong("matricula"),
                    rs.getString("nome"),
                    rs.getBoolean("maioridade"),
                    Cursos.valueOf(rs.getString("curso")),
                    rs.getString("sexo")
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(aluno);
    }

    @Override
    public List<Aluno> findByCurso(Cursos curso) {
        String query = "SELECT * FROM alunos WHERE curso = ?";
        List<Aluno> lista = new ArrayList<>();
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Define o parâmetro do filtro
            statement.setString(1, curso.toString());

            // Executa a consulta
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Aluno aluno = new Aluno();
                    aluno.setMatricula(rs.getLong("matricula"));
                    aluno.setNome(rs.getString("nome"));
                    aluno.setMaioridade(rs.getBoolean("maioridade"));
                    aluno.setCurso(Cursos.valueOf(rs.getString("curso")));
                    aluno.setSexo(rs.getString("sexo"));
                    lista.add(aluno);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar alunos por curso: " + curso, e);
        }
        return lista;
    }
}