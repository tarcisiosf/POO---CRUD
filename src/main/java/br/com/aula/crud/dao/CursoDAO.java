package br.com.aula.crud.dao;

import br.com.aula.crud.config.ConnectionFactory;
import br.com.aula.crud.model.Curso;
import br.com.aula.crud.model.Area;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class CursoDAO implements ICursoDAO {

    private final ConnectionFactory factory = new ConnectionFactory();

    @Override
    public Curso create(Curso curso) {
        String sql = "INSERT INTO cursos (nome, sigla, area) VALUES (?, ?, ?)";
        try (Connection conn = factory.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, curso.getNome());
            ps.setString(2, curso.getSigla());
            ps.setString(3, curso.getArea().name());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    Long codigo = rs.getLong(1);
                    curso.setCodigo(codigo);
                }
            }
            return curso;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar curso", e);
        }
    }

    @Override
    public void update(Curso curso) {
        String sql = "UPDATE cursos SET nome = ?, sigla = ?, area = ? WHERE codigo = ?";
        try (Connection conn = factory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, curso.getNome());
            ps.setString(2, curso.getSigla());
            ps.setString(3, curso.getArea().name());
            ps.setLong(4, curso.getCodigo());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar curso", e);
        }
    }

    @Override
    public void delete(Long codigo) {
        String sql = "DELETE FROM cursos WHERE codigo = ?";
        try (Connection conn = factory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, codigo);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir curso", e);
        }
    }

    @Override
    public List<Curso> findAll() {
        String sql = "SELECT codigo, nome, sigla, area FROM cursos ORDER BY codigo";
        List<Curso> lista = new ArrayList<>();
        try (Connection conn = factory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapRow(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar cursos", e);
        }
    }

    @Override
    public Optional<Curso> findById(Long codigo) {
        String sql = "SELECT codigo, nome, sigla, area FROM cursos WHERE codigo = ?";
        try (Connection conn = factory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar curso por ID", e);
        }
    }

    @Override
    public List<Curso> findByArea(Area area) {
        String sql = "SELECT codigo, nome, sigla, area FROM cursos WHERE area = ? ORDER BY nome";
        List<Curso> lista = new ArrayList<>();
        try (Connection conn = factory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, area.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapRow(rs));
                }
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar cursos por área", e);
        }
    }

    @Override
    public Optional<Curso> findBySigla(String sigla) {
        String sql = "SELECT codigo, nome, sigla, area FROM cursos WHERE sigla = ?";
        try (Connection conn = factory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, sigla);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar curso por sigla", e);
        }
    }

    private Curso mapRow(ResultSet rs) throws SQLException {
        Long codigo = rs.getLong("codigo");
        String nome   = rs.getString("nome");
        String sigla  = rs.getString("sigla");
        Area area     = Area.valueOf(rs.getString("area"));
        return new Curso(codigo, nome, sigla, area);
    }
}
