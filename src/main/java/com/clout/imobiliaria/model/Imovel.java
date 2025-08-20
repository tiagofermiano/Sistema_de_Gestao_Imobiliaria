package com.clout.imobiliaria.model;
public class Imovel {
    private Integer id; private String tipo; private String endereco; private String cidade; private String estado; private String cep;
    private double metragem; private int quartos; private int banheiros; private int vagas;
    private boolean mobiliado; private boolean ativo; private boolean disponivel;
    public Imovel() {}
    public Imovel(Integer id,String tipo,String endereco,String cidade,String estado,String cep,double metragem,int quartos,int banheiros,int vagas,boolean mobiliado,boolean ativo,boolean disponivel){
        this.id=id;this.tipo=tipo;this.endereco=endereco;this.cidade=cidade;this.estado=estado;this.cep=cep;this.metragem=metragem;this.quartos=quartos;this.banheiros=banheiros;this.vagas=vagas;this.mobiliado=mobiliado;this.ativo=ativo;this.disponivel=disponivel;}
    public Imovel(String tipo,String endereco,String cidade,String estado,String cep,double metragem,int quartos,int banheiros,int vagas,boolean mobiliado,boolean ativo,boolean disponivel){this(null,tipo,endereco,cidade,estado,cep,metragem,quartos,banheiros,vagas,mobiliado,ativo,disponivel);}
    public Integer getId(){return id;} public void setId(Integer id){this.id=id;}
    public String getTipo(){return tipo;} public void setTipo(String tipo){this.tipo=tipo;}
    public String getEndereco(){return endereco;} public void setEndereco(String endereco){this.endereco=endereco;}
    public String getCidade(){return cidade;} public void setCidade(String cidade){this.cidade=cidade;}
    public String getEstado(){return estado;} public void setEstado(String estado){this.estado=estado;}
    public String getCep(){return cep;} public void setCep(String cep){this.cep=cep;}
    public double getMetragem(){return metragem;} public void setMetragem(double m){this.metragem=m;}
    public int getQuartos(){return quartos;} public void setQuartos(int q){this.quartos=q;}
    public int getBanheiros(){return banheiros;} public void setBanheiros(int b){this.banheiros=b;}
    public int getVagas(){return vagas;} public void setVagas(int v){this.vagas=v;}
    public boolean isMobiliado(){return mobiliado;} public void setMobiliado(boolean m){this.mobiliado=m;}
    public boolean isAtivo(){return ativo;} public void setAtivo(boolean a){this.ativo=a;}
    public boolean isDisponivel(){return disponivel;} public void setDisponivel(boolean d){this.disponivel=d;}
    public String toString(){return String.format("#%d - %s | %s, %s-%s | %.2f m² | Q:%d B:%d V:%d | Mob:%s | Ativo:%s | Disp:%s", id,tipo,endereco,cidade,estado,metragem,quartos,banheiros,vagas,mobiliado?"Sim":"Não",ativo?"Sim":"Não",disponivel?"Sim":"Não");}}