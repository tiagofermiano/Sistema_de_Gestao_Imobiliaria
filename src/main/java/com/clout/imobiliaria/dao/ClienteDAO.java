package com.clout.imobiliaria.dao;
import com.clout.imobiliaria.db.Database; import com.clout.imobiliaria.model.Cliente;
import java.sql.*; import java.util.*;
public class ClienteDAO {
    public int inserir(Cliente c){ String sql="INSERT INTO clientes (nome,cpf,telefone,email) VALUES (?,?,?,?)";
        try(Connection conn=Database.getConnection(); PreparedStatement ps=conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1,c.getNome()); ps.setString(2,c.getCpf()); ps.setString(3,c.getTelefone()); ps.setString(4,c.getEmail()); ps.executeUpdate();
            try(ResultSet rs=ps.getGeneratedKeys()){ if(rs.next()) return rs.getInt(1);} } catch(SQLException e){ throw new RuntimeException("Erro ao inserir cliente: "+e.getMessage(),e);} return -1; }
    public List<Cliente> listarTodos(){ List<Cliente> list=new ArrayList<>(); String sql="SELECT * FROM clientes ORDER BY id";
        try(Connection conn=Database.getConnection(); PreparedStatement ps=conn.prepareStatement(sql); ResultSet rs=ps.executeQuery()){
            while(rs.next()){ list.add(new Cliente(rs.getInt("id"),rs.getString("nome"),rs.getString("cpf"),rs.getString("telefone"),rs.getString("email"))); } } catch(SQLException e){ throw new RuntimeException("Erro ao listar clientes: "+e.getMessage(),e);} return list; }
}