package com.clout.imobiliaria;

import com.clout.imobiliaria.dao.ClienteDAO;
import com.clout.imobiliaria.dao.ContratoDAO;
import com.clout.imobiliaria.dao.ImovelDAO;
import com.clout.imobiliaria.model.Cliente;
import com.clout.imobiliaria.model.Contrato;
import com.clout.imobiliaria.model.Imovel;

import java.time.LocalDate;
import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final ClienteDAO clienteDAO = new ClienteDAO();
    private static final ImovelDAO imovelDAO = new ImovelDAO();
    private static final ContratoDAO contratoDAO = new ContratoDAO();

    public static void main(String[] args) {
        System.out.println("==== IMOBILIARIA CLI ====");
        while (true) {
            menu();
            int op = lerInt("Escolha: ");
            switch (op) {
                case 1 -> cadCliente();
                case 2 -> cadImovel();
                case 3 -> cadContrato();
                case 4 -> relatorios();
                case 5 -> listarClientes();
                case 6 -> listarImoveis();
                case 7 -> encerrar();
                case 0 -> {
                    System.out.println("Até logo!");
                    return;
                }
                default -> System.out.println("Opção inválida!");
            }
            System.out.println();
        }
    }

    private static void menu() {
        System.out.println("1) Cadastrar Cliente");
        System.out.println("2) Cadastrar Imóvel");
        System.out.println("3) Cadastrar Contrato");
        System.out.println("4) Relatórios");
        System.out.println("5) Listar Clientes");
        System.out.println("6) Listar Imóveis");
        System.out.println("7) Encerrar Contrato");
        System.out.println("0) Sair");
    }

    private static void relatorios() {
        System.out.println("1) Imóveis disponíveis");
        System.out.println("2) Contratos ativos");
        System.out.println("3) Clientes com mais contratos");
        System.out.println("4) Contratos expirando em 30 dias");
        int op = lerInt("Escolha: ");
        switch (op) {
            case 1 -> {
                var list = imovelDAO.listarDisponiveis();
                if (list.isEmpty()) System.out.println("Nenhum.");
                else list.forEach(System.out::println);
            }
            case 2 -> {
                var list = contratoDAO.listarAtivos();
                if (list.isEmpty()) System.out.println("Nenhum.");
                else list.forEach(System.out::println);
            }
            case 3 -> {
                int n = lerInt("Top N (ex: 5): ");
                var rows = contratoDAO.clientesComMaisContratos(n <= 0 ? 5 : n);
                if (rows.isEmpty()) System.out.println("Sem dados.");
                else rows.forEach(System.out::println);
            }
            case 4 -> {
                var list = contratoDAO.listarExpirandoEm(30);
                if (list.isEmpty()) System.out.println("Nenhum expirando nos próximos 30 dias.");
                else list.forEach(System.out::println);
            }
            default -> System.out.println("Opção inválida.");
        }
    }

    private static void cadCliente() {
        System.out.println("-- Cliente --");
        System.out.print("Nome: ");
        String nome = sc.nextLine();
        System.out.print("CPF: ");
        String cpf = sc.nextLine();
        System.out.print("Telefone: ");
        String tel = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        int id = clienteDAO.inserir(new Cliente(nome, cpf, tel, email));
        System.out.println("ID #" + id + " cadastrado.");
    }

    private static void cadImovel() {
        System.out.println("-- Imóvel --");
        System.out.print("Tipo: ");
        String tipo = sc.nextLine();
        System.out.print("Endereço: ");
        String end = sc.nextLine();
        System.out.print("Cidade: ");
        String cid = sc.nextLine();
        System.out.print("Estado (UF): ");
        String uf = sc.nextLine();
        System.out.print("CEP: ");
        String cep = sc.nextLine();
        double m = lerDouble("Metragem (m²): ");
        int q = lerInt("Quartos: ");
        int b = lerInt("Banheiros: ");
        int v = lerInt("Vagas: ");
        boolean mob = lerBool("Mobiliado (s/n): ");
        int id = imovelDAO.inserir(new Imovel(tipo, end, cid, uf, cep, m, q, b, v, mob, true, true));
        System.out.println("Imóvel #" + id + " cadastrado.");
    }

    private static void cadContrato() {
        System.out.println("-- Contrato --");
        listarImoveis();
        int imovelId = lerInt("ID do Imóvel (disponível): ");
        listarClientes();
        int clienteId = lerInt("ID do Cliente: ");
        double valor = lerDouble("Valor mensal (R$): ");
        LocalDate ini = lerData("Início (YYYY-MM-DD): ");
        LocalDate fim = lerData("Fim (YYYY-MM-DD): ");
        int id = contratoDAO.inserir(new Contrato(imovelId, clienteId, valor, ini, fim, true));
        System.out.println("Contrato #" + id + " criado.");
    }

    private static void listarClientes() {
        System.out.println("-- Clientes --");
        var l = clienteDAO.listarTodos();
        if (l.isEmpty()) System.out.println("Nenhum.");
        else l.forEach(System.out::println);
    }

    private static void listarImoveis() {
        System.out.println("-- Imóveis --");
        var l = imovelDAO.listarTodos();
        if (l.isEmpty()) System.out.println("Nenhum.");
        else l.forEach(System.out::println);
    }

    private static void encerrar() {
        System.out.println("-- Encerrar Contrato --");
        var l = contratoDAO.listarAtivos();
        if (l.isEmpty()) {
            System.out.println("Nenhum ativo.");
            return;
        }
        l.forEach(System.out::println);
        int id = lerInt("ID do contrato: ");
        contratoDAO.encerrarContrato(id); // ✅ existe no DAO
        System.out.println("Encerrado.");
    }

    // ==== Utils de input ====
    private static int lerInt(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                return Integer.parseInt(sc.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Número inválido.");
            }
        }
    }

    private static double lerDouble(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                return Double.parseDouble(sc.nextLine().trim().replace(",", "."));
            } catch (Exception e) {
                System.out.println("Valor inválido.");
            }
        }
    }

    private static boolean lerBool(String msg) {
        System.out.print(msg);
        String s = sc.nextLine().trim().toLowerCase();
        return s.startsWith("s") || s.equals("1") || s.equals("true");
    }

    private static LocalDate lerData(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                return LocalDate.parse(sc.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Use YYYY-MM-DD.");
            }
        }
    }
}
