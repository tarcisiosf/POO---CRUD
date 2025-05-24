package br.com.aula.crud.dao;

import br.com.aula.crud.config.ConnectionFactory;
import br.com.aula.crud.model.Aluno;
import br.com.aula.crud.model.Curso;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AlunoDAO implements IAlunoDAO {

    private final ConnectionFactory factory = new ConnectionFactory();
    private final CursoDAO cursoDAO    = new CursoDAO();

    @Override
    public Aluno create(Aluno aluno) {
        String sql = "INSERT INTO alunos (nome, maioridade, curso_codigo, sexo) VALUES (?, ?, ?, ?)";
        try (Connection conn = factory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, aluno.getNome());
            ps.setBoolean(2, aluno.isMaioridade());
            ps.setLong(3, aluno.getCurso().getCodigo());
            ps.setString(4, aluno.getSexo());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    aluno.setMatricula(rs.getLong(1));
                }
            }
            return aluno;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar aluno", e);
        }
    }

    @Override
    public void update(Aluno aluno) {
        String sql = "UPDATE alunos SET nome = ?, maioridade = ?, curso_codigo = ?, sexo = ? WHERE matricula = ?";
        try (Connection conn = factory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, aluno.getNome());
            ps.setBoolean(2, aluno.isMaioridade());
            ps.setLong(3, aluno.getCurso().getCodigo());
            ps.setString(4, aluno.getSexo());
            ps.setLong(5, aluno.getMatricula());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar aluno", e);
        }
    }

    @Override
    public void delete(Long matricula) {
        String sql = "DELETE FROM alunos WHERE matricula = ?";
        try (Connection conn = factory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, matricula);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir aluno", e);
        }
    }

    @Override
    public List<Aluno> findAll() {
        String sql = "SELECT matricula, nome, maioridade, curso_codigo, sexo FROM alunos ORDER BY matricula";
        List<Aluno> lista = new ArrayList<>();
        try (Connection conn = factory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapRow(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar alunos", e);
        }
    }

    @Override
    public Optional<Aluno> findById(Long matricula) {
        String sql = "SELECT matricula, nome, maioridade, curso_codigo, sexo FROM alunos WHERE matricula = ?";
        try (Connection conn = factory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, matricula);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar aluno por ID", e);
        }
    }

    @Override
    public List<Aluno> findByCurso(Curso curso) {
        String sql = "SELECT matricula, nome, maioridade, curso_codigo, sexo FROM alunos WHERE curso_codigo = ? ORDER BY nome";
        List<Aluno> lista = new ArrayList<>();
        try (Connection conn = factory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, curso.getCodigo());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapRow(rs));
                }
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar alunos por curso", e);
        }
    }

    @Override
    public List<Aluno> findBySexo(String sexo) {
        String sql = "SELECT matricula, nome, maioridade, curso_codigo, sexo FROM alunos WHERE sexo = ? ORDER BY nome";
        List<Aluno> lista = new ArrayList<>();
        try (Connection conn = factory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, sexo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapRow(rs));
                }
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar alunos por sexo", e);
        }
    }

    private Aluno mapRow(ResultSet rs) throws SQLException {
        Long matricula      = rs.getLong("matricula");
        String nome         = rs.getString("nome");
        boolean maioridade  = rs.getBoolean("maioridade");
        Long cursoCodigo    = rs.getLong("curso_codigo");
        Curso curso         = cursoDAO.findById(cursoCodigo)
                .orElseThrow(() ->
                        new RuntimeException("Curso não encontrado (código): " + cursoCodigo));
        String sexo         = rs.getString("sexo");

        return new Aluno(matricula, nome, maioridade, curso, sexo);
    }
}
