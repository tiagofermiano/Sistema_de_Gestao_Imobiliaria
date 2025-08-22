package com.clout.imobiliaria.dao;

import com.clout.imobiliaria.db.Database;
import com.clout.imobiliaria.model.Imovel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ImovelDAO {

    public int inserir(Imovel i) {
        String sql = "INSERT INTO imoveis (tipo,endereco,cidade,estado,cep,metragem,quartos,banheiros,vagas,mobiliado,ativo,disponivel) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, i.getTipo());
            ps.setString(2, i.getEndereco());
            ps.setString(3, i.getCidade());
            ps.setString(4, i.getEstado());
            ps.setString(5, i.getCep());
            ps.setDouble(6, i.getMetragem());
            ps.setInt(7, i.getQuartos());
            ps.setInt(8, i.getBanheiros());
            ps.setInt(9, i.getVagas());
            ps.setInt(10, i.isMobiliado() ? 1 : 0);
            ps.setInt(11, i.isAtivo() ? 1 : 0);
            ps.setInt(12, i.isDisponivel() ? 1 : 0);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir imóvel: " + e.getMessage(), e);
        }
        return -1;
    }

    public List<Imovel> listarTodos() {
        List<Imovel> list = new ArrayList<>();
        String sql = "SELECT * FROM imoveis ORDER BY id";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Imovel i = map(rs);
                list.add(i);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar imóveis: " + e.getMessage(), e);
        }
        return list;
    }

    public List<Imovel> listarDisponiveis() {
        List<Imovel> list = new ArrayList<>();
        String sql = "SELECT * FROM imoveis WHERE disponivel=1 AND ativo=1 ORDER BY id";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar imóveis: " + e.getMessage(), e);
        }
        return list;
    }

    public Imovel buscarPorId(int id) {
        String sql = "SELECT * FROM imoveis WHERE id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar imóvel: " + e.getMessage(), e);
        }
        return null;
    }

    public void marcarDisponibilidade(int imovelId, boolean disponivel) {
        String sql = "UPDATE imoveis SET disponivel=? WHERE id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, disponivel ? 1 : 0);
            ps.setInt(2, imovelId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar disponibilidade do imóvel: " + e.getMessage(), e);
        }
    }

    private Imovel map(ResultSet rs) throws SQLException {
        return new Imovel(
                rs.getInt("id"),
                rs.getString("tipo"),
                rs.getString("endereco"),
                rs.getString("cidade"),
                rs.getString("estado"),
                rs.getString("cep"),
                rs.getDouble("metragem"),
                rs.getInt("quartos"),
                rs.getInt("banheiros"),
                rs.getInt("vagas"),
                rs.getInt("mobiliado") == 1,
                rs.getInt("ativo") == 1,
                rs.getInt("disponivel") == 1
        );
    }
}
