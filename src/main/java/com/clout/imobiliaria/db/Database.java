package com.clout.imobiliaria.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static final String URL = "jdbc:sqlite:imobiliaria.db";

    static {
        // Cria/atualiza o schema na inicialização da aplicação
        try (Connection conn = getConnection(); Statement st = conn.createStatement()) {

            // CLIENTES
            st.executeUpdate("""
                        CREATE TABLE IF NOT EXISTS clientes (
                          id        INTEGER      PRIMARY KEY AUTOINCREMENT,
                          nome      VARCHAR(120) NOT NULL,
                          cpf       VARCHAR(14)  NOT NULL UNIQUE
                                                 CHECK (
                                                   cpf GLOB '[0-9][0-9][0-9].[0-9][0-9][0-9].[0-9][0-9][0-9]-[0-9][0-9]'
                                                   OR cpf GLOB '[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'
                                                 ),
                          telefone  VARCHAR(20),
                          email     VARCHAR(254)
                        );
                    """);

            // IMOVEIS
            st.executeUpdate("""
                        CREATE TABLE IF NOT EXISTS imoveis (
                          id         INTEGER       PRIMARY KEY AUTOINCREMENT,
                          tipo       VARCHAR(50)   NOT NULL,
                          endereco   VARCHAR(150)  NOT NULL,
                          cidade     VARCHAR(80)   NOT NULL,
                          estado     CHAR(2)       NOT NULL CHECK (length(estado) = 2),
                          cep        VARCHAR(9)    NOT NULL
                                                   CHECK (
                                                     cep GLOB '[0-9][0-9][0-9][0-9][0-9]-[0-9][0-9][0-9]'
                                                     OR cep GLOB '[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'
                                                   ),
                          metragem   DECIMAL(10,2) NOT NULL CHECK (metragem > 0),
                          quartos    INTEGER       NOT NULL CHECK (quartos   >= 0),
                          banheiros  INTEGER       NOT NULL CHECK (banheiros >= 0),
                          vagas      INTEGER       NOT NULL CHECK (vagas     >= 0),
                          mobiliado  INTEGER       NOT NULL DEFAULT 0 CHECK (mobiliado IN (0,1)),
                          ativo      INTEGER       NOT NULL DEFAULT 1 CHECK (ativo     IN (0,1)),
                          disponivel INTEGER       NOT NULL DEFAULT 1 CHECK (disponivel IN (0,1))
                        );
                    """);

            // CONTRATOS
            st.executeUpdate("""
                        CREATE TABLE IF NOT EXISTS contratos (
                          id           INTEGER        PRIMARY KEY AUTOINCREMENT,
                          imovel_id    INTEGER        NOT NULL,
                          cliente_id   INTEGER        NOT NULL,
                          valor_mensal DECIMAL(12,2)  NOT NULL CHECK (valor_mensal >= 0),
                          data_inicio  DATE           NOT NULL,  -- armazenado como TEXT 'YYYY-MM-DD'
                          data_fim     DATE           NOT NULL CHECK (date(data_fim) >= date(data_inicio)),
                          ativo        INTEGER        NOT NULL DEFAULT 1 CHECK (ativo IN (0,1)),
                          FOREIGN KEY (imovel_id)  REFERENCES imoveis(id),
                          FOREIGN KEY (cliente_id) REFERENCES clientes(id)
                        );
                    """);

            // Índices úteis
            st.executeUpdate("CREATE INDEX IF NOT EXISTS idx_contratos_cliente  ON contratos(cliente_id);");
            st.executeUpdate("CREATE INDEX IF NOT EXISTS idx_contratos_imovel   ON contratos(imovel_id);");
            st.executeUpdate("CREATE INDEX IF NOT EXISTS idx_imoveis_disponivel ON imoveis(disponivel);");
            st.executeUpdate("CREATE INDEX IF NOT EXISTS idx_imoveis_ativo_disp ON imoveis(ativo, disponivel);");

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inicializar o banco: " + e.getMessage(), e);
        }
    }

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL);
        // IMPORTANTE: o SQLite exige ativar FKs em **toda** conexão
        try (Statement s = conn.createStatement()) {
            s.execute("PRAGMA foreign_keys = ON;");
        }
        return conn;
    }
}
