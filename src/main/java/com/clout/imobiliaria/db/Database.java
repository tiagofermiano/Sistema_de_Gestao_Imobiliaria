package com.clout.imobiliaria.db;
import java.sql.*;
public class Database {
    private static final String URL = "jdbc:sqlite:imobiliaria.db";
    static {
        try (Connection conn = getConnection(); Statement st = conn.createStatement()) {
            st.executeUpdate("CREATE TABLE IF NOT EXISTS clientes (id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT NOT NULL, cpf TEXT NOT NULL UNIQUE, telefone TEXT, email TEXT);");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS imoveis (id INTEGER PRIMARY KEY AUTOINCREMENT, tipo TEXT NOT NULL, endereco TEXT NOT NULL, cidade TEXT NOT NULL, estado TEXT NOT NULL, cep TEXT NOT NULL, metragem REAL NOT NULL, quartos INTEGER NOT NULL, banheiros INTEGER NOT NULL, vagas INTEGER NOT NULL, mobiliado INTEGER NOT NULL DEFAULT 0, ativo INTEGER NOT NULL DEFAULT 1, disponivel INTEGER NOT NULL DEFAULT 1);");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS contratos (id INTEGER PRIMARY KEY AUTOINCREMENT, imovel_id INTEGER NOT NULL, cliente_id INTEGER NOT NULL, valor_mensal REAL NOT NULL, data_inicio TEXT NOT NULL, data_fim TEXT NOT NULL, ativo INTEGER NOT NULL DEFAULT 1, FOREIGN KEY (imovel_id) REFERENCES imoveis(id), FOREIGN KEY (cliente_id) REFERENCES clientes(id));");
        } catch (SQLException e) { throw new RuntimeException("Erro ao inicializar o banco: " + e.getMessage(), e); }
    }
    public static Connection getConnection() throws SQLException { return DriverManager.getConnection(URL); }
}