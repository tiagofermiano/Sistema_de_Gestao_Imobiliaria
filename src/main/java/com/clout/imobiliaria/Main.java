package com.clout.imobiliaria;
import com.clout.imobiliaria.dao.*; import com.clout.imobiliaria.model.*;
import java.time.LocalDate; import java.util.*; 
public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final ClienteDAO clienteDAO = new ClienteDAO();
    private static final ImovelDAO imovelDAO = new ImovelDAO();
    private static final ContratoDAO contratoDAO = new ContratoDAO();
    public static void main(String[] args){ System.out.println("==== IMOBILIARIA CLI ===="); while(true){ menu(); int op=lerInt("Escolha: ");
        switch(op){ case 1->cadCliente(); case 2->cadImovel(); case 3->cadContrato(); case 4->relatorios(); case 5->listarClientes(); case 6->listarImoveis(); case 7->encerrar(); case 0->{System.out.println("Até logo!"); return;} default->System.out.println("Opção inválida!"); } System.out.println(); } }
    private static void menu(){ System.out.println("1) Cadastrar Cliente\n2) Cadastrar Imóvel\n3) Cadastrar Contrato\n4) Relatórios\n5) Listar Clientes\n6) Listar Imóveis\n7) Encerrar Contrato\n0) Sair"); }
    private static void relatorios(){ System.out.println("1) Imóveis disponíveis\n2) Contratos ativos\n3) Clientes com mais contratos\n4) Contratos expirando em 30 dias"); int op=lerInt("Escolha: ");
        switch(op){ case 1->{ var l=imovelDAO.listarDisponiveis(); if(l.isEmpty()) System.out.println("Nenhum."); else l.forEach(System.out::println);} 
            case 2->{ var l=contratoDAO.listarAtivos(); if(l.isEmpty()) System.out.println("Nenhum."); else l.forEach(System.out::println);} 
            case 3->{ int n=lerInt("Top N (ex: 5): "); var rows=contratoDAO.clientesComMaisContratos(n<=0?5:n); if(rows.isEmpty()) System.out.println("Sem dados."); else rows.forEach(System.out::println);} 
            case 4->{ var l=contratoDAO.listarExpirandoEm(30); if(l.isEmpty()) System.out.println("Nenhum expirando nos próximos 30 dias."); else l.forEach(System.out::println);} 
            default->System.out.println("Opção inválida."); } }
    private static void cadCliente(){ System.out.println("-- Cliente --"); System.out.print("Nome: "); String nome=sc.nextLine(); System.out.print("CPF: "); String cpf=sc.nextLine(); System.out.print("Telefone: "); String tel=sc.nextLine(); System.out.print("Email: "); String email=sc.nextLine(); int id=clienteDAO.inserir(new Cliente(nome,cpf,tel,email)); System.out.println("ID #"+id+" cadastrado."); }
    private static void cadImovel(){ System.out.println("-- Imóvel --"); System.out.print("Tipo: "); String tipo=sc.nextLine(); System.out.print("Endereço: "); String e=sc.nextLine(); System.out.print("Cidade: "); String c=sc.nextLine(); System.out.print("Estado (UF): "); String uf=sc.nextLine(); System.out.print("CEP: "); String cep=sc.nextLine(); double m=lerDouble("Metragem (m²): "); int q=lerInt("Quartos: "); int b=lerInt("Banheiros: "); int v=lerInt("Vagas: "); boolean mob=lerBool("Mobiliado (s/n): "); int id=imovelDAO.inserir(new Imovel(tipo,e,c,uf,cep,m,q,b,v,mob,true,true)); System.out.println("Imóvel #"+id+" cadastrado."); }
    private static void cadContrato(){ System.out.println("-- Contrato --"); listarImoveis(); int imovelId=lerInt("ID do Imóvel (disponível): "); listarClientes(); int clienteId=lerInt("ID do Cliente: "); double valor=lerDouble("Valor mensal (R$): "); LocalDate ini=lerData("Início (YYYY-MM-DD): "); LocalDate fim=lerData("Fim (YYYY-MM-DD): "); int id=contratoDAO.inserir(new Contrato(imovelId,clienteId,valor,ini,fim,true)); System.out.println("Contrato #"+id+" criado."); }
    private static void listarClientes(){ System.out.println("-- Clientes --"); var l=clienteDAO.listarTodos(); if(l.isEmpty()) System.out.println("Nenhum."); else l.forEach(System.out::println); }
    private static void listarImoveis(){ System.out.println("-- Imóveis --"); var l=imovelDAO.listarTodos(); if(l.isEmpty()) System.out.println("Nenhum."); else l.forEach(System.out::println); }
    private static void encerrar(){ System.out.println("-- Encerrar Contrato --"); var l=contratoDAO.listarAtivos(); if(l.isEmpty()){ System.out.println("Nenhum ativo."); return; } l.forEach(System.out::println); int id=lerInt("ID do contrato: "); contratoDAO.encerrarContrato(id); System.out.println("Encerrado."); }
    private static int lerInt(String msg){ while(true){ try{ System.out.print(msg); return Integer.parseInt(sc.nextLine().trim()); } catch(Exception e){ System.out.println("Número inválido."); } } }
    private static double lerDouble(String msg){ while(true){ try{ System.out.print(msg); return Double.parseDouble(sc.nextLine().trim().replace(",",".")); } catch(Exception e){ System.out.println("Valor inválido."); } } }
    private static boolean lerBool(String msg){ System.out.print(msg); String s=sc.nextLine().trim().toLowerCase(); return s.startsWith("s")||s.equals("1")||s.equals("true"); }
    private static LocalDate lerData(String msg){ while(true){ try{ System.out.print(msg); return java.time.LocalDate.parse(sc.nextLine().trim()); } catch(Exception e){ System.out.println("Use YYYY-MM-DD."); } } }
}