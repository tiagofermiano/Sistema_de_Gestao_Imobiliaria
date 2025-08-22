PRAGMA foreign_keys = ON;

-- CLIENTES
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

-- IMOVEIS
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

-- CONTRATOS
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

-- Índices úteis
CREATE INDEX IF NOT EXISTS idx_contratos_cliente  ON contratos(cliente_id);
CREATE INDEX IF NOT EXISTS idx_contratos_imovel   ON contratos(imovel_id);
CREATE INDEX IF NOT EXISTS idx_imoveis_disponivel ON imoveis(disponivel);
CREATE INDEX IF NOT EXISTS idx_imoveis_ativo_disp ON imoveis(ativo, disponivel);

-- Dados de exemplo (compatíveis com as validações)
INSERT INTO clientes (nome, cpf, telefone, email) VALUES
  ('Ana Souza',    '123.456.789-00', '(47) 99999-0001', 'ana@example.com'),
  ('Bruno Lima',   '987.654.321-00', '(47) 99999-0002', 'bruno@example.com'),
  ('Carla Mendes', '555.444.333-22', '(47) 99999-0003', 'carla@example.com');

INSERT INTO imoveis (
  tipo, endereco, cidade, estado, cep,
  metragem, quartos, banheiros, vagas,
  mobiliado, ativo, disponivel
) VALUES
  ('Apartamento',    'Rua A, 100',       'Joinville', 'SC', '89200-000',  65.00, 2, 1, 1, 1, 1, 1),
  ('Casa',           'Rua B, 200',       'Joinville', 'SC', '89200-001', 120.00, 3, 2, 2, 0, 1, 1),
  ('Sala Comercial', 'Av. Central, 300', 'Joinville', 'SC', '89200-002',  45.00, 0, 1, 0, 0, 1, 1);

INSERT INTO contratos (imovel_id, cliente_id, valor_mensal, data_inicio, data_fim, ativo)
VALUES (1, 1, 1800.00, date('now','-10 day'), date('now','+350 day'), 1);

UPDATE imoveis SET disponivel = 0 WHERE id = 1;
