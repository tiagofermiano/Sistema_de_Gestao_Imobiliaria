PRAGMA foreign_keys = ON;

-- Criação das tabelas
CREATE TABLE IF NOT EXISTS clientes (
  id        INTEGER PRIMARY KEY AUTOINCREMENT,
  nome      TEXT    NOT NULL,
  cpf       TEXT    NOT NULL UNIQUE,
  telefone  TEXT,
  email     TEXT
);

CREATE TABLE IF NOT EXISTS imoveis (
  id         INTEGER PRIMARY KEY AUTOINCREMENT,
  tipo       TEXT    NOT NULL,
  endereco   TEXT    NOT NULL,
  cidade     TEXT    NOT NULL,
  estado     TEXT    NOT NULL,
  cep        TEXT    NOT NULL,
  metragem   REAL    NOT NULL,
  quartos    INTEGER NOT NULL,
  banheiros  INTEGER NOT NULL,
  vagas      INTEGER NOT NULL,
  mobiliado  INTEGER NOT NULL DEFAULT 0,
  ativo      INTEGER NOT NULL DEFAULT 1,
  disponivel INTEGER NOT NULL DEFAULT 1
);

CREATE TABLE IF NOT EXISTS contratos (
  id           INTEGER PRIMARY KEY AUTOINCREMENT,
  imovel_id    INTEGER NOT NULL,
  cliente_id   INTEGER NOT NULL,
  valor_mensal REAL    NOT NULL,
  data_inicio  TEXT    NOT NULL, -- YYYY-MM-DD
  data_fim     TEXT    NOT NULL, -- YYYY-MM-DD
  ativo        INTEGER NOT NULL DEFAULT 1,
  FOREIGN KEY (imovel_id)  REFERENCES imoveis(id),
  FOREIGN KEY (cliente_id) REFERENCES clientes(id)
);

-- Inserts de exemplo
INSERT INTO clientes (nome, cpf, telefone, email)
VALUES
  ('Ana Souza',    '123.456.789-00', '(47) 99999-0001', 'ana@example.com'),
  ('Bruno Lima',   '987.654.321-00', '(47) 99999-0002', 'bruno@example.com'),
  ('Carla Mendes', '555.444.333-22', '(47) 99999-0003', 'carla@example.com');

INSERT INTO imoveis (
  tipo, endereco, cidade, estado, cep,
  metragem, quartos, banheiros, vagas,
  mobiliado, ativo, disponivel
)
VALUES
  ('Apartamento',    'Rua A, 100',       'Joinville', 'SC', '89200-000',  65.0, 2, 1, 1, 1, 1, 1),
  ('Casa',           'Rua B, 200',       'Joinville', 'SC', '89200-001', 120.0, 3, 2, 2, 0, 1, 1),
  ('Sala Comercial', 'Av. Central, 300', 'Joinville', 'SC', '89200-002',  45.0, 0, 1, 0, 0, 1, 1);

INSERT INTO contratos (imovel_id, cliente_id, valor_mensal, data_inicio, data_fim, ativo)
VALUES (1, 1, 1800.00, date('now','-10 day'), date('now','+350 day'), 1);

UPDATE imoveis SET disponivel = 0 WHERE id = 1;
