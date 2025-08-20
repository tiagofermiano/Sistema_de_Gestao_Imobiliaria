package com.clout.imobiliaria.dao;

import com.clout.imobiliaria.db.Database;
import com.clout.imobiliaria.model.Contrato;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class ContratoDAO {
    public int inserir(Contrato c) {
        String sql = "INSERT INTO contratos (imovel_id,cliente_id,valor_mensal,data_inicio,data_fim,ativo) VALUES (?,?,?,?,?,?)";
        try (Connection conn = Database.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, c.getImovelId());
                ps.setInt(2, c.getClienteId());
                ps.setDouble(3, c.getValorMensal());
                ps.setString(4, c.getDataInicio().toString());
                ps.setString(5, c.getDataFim().toString());
                ps.setInt(6, c.isAtivo() ? 1 : 0);
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        try (PreparedStatement ps2 = conn
                                .prepareStatement("UPDATE imoveis SET disponivel=0 WHERE id=?")) {
                            ps2.setInt(1, c.getImovelId());
                            ps2.executeUpdate();
                        }
                        conn.commit();
                        return id;
                    }
                }
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir contrato: " + e.getMessage(), e);
        }
    }

    public List<Contrato> listarAtivos() {
        List<Contrato> list = new ArrayList<>();
        String sql = "SELECT * FROM contratos WHERE ativo=1 ORDER BY id";
        try (Connection conn = Database.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                list.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar contratos ativos: " + e.getMessage(), e);
        }
        return list;
    }

    public List<Contrato> listarExpirandoEm(int dias) {
        List<Contrato> list = new ArrayList<>();
        String sql = "SELECT * FROM contratos WHERE date(data_fim) BETWEEN date('now') AND date('now', ?)";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "+" + dias + " day");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar contratos expirando: " + e.getMessage(), e);
        }
        return list;
    }

    public List<String> clientesComMaisContratos(int limit) {
        List<String> rows = new ArrayList<>();
        String sql = "SELECT c.nome AS cliente, COUNT(*) AS total FROM contratos ct JOIN clientes c ON c.id=ct.cliente_id GROUP BY ct.cliente_id ORDER BY total DESC, cliente ASC LIMIT ?";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    rows.add(rs.getString("cliente") + " — " + rs.getInt("total") + " contrato(s)");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao apurar clientes com mais contratos: " + e.getMessage(), e);
        }
        return rows;
    }

    private Contrato map(ResultSet rs) throws SQLException {
        return new Contrato(rs.getInt("id"), rs.getInt("imovel_id"), rs.getInt("cliente_id"),
                rs.getDouble("valor_mensal"), LocalDate.parse(rs.getString("data_inicio")),
                LocalDate.parse(rs.getString("data_fim")), rs.getInt("ativo") == 1);
    }
}