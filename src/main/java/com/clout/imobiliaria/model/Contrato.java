package com.clout.imobiliaria.model;

import java.time.LocalDate;

public class Contrato {
    private Integer id, imovelId, clienteId;
    private double valorMensal;
    private LocalDate dataInicio, dataFim;
    private boolean ativo;

    public Contrato() {
    }

    public Contrato(Integer id, Integer imovelId, Integer clienteId, double valorMensal, LocalDate dataInicio,
            LocalDate dataFim, boolean ativo) {
        this.id = id;
        this.imovelId = imovelId;
        this.clienteId = clienteId;
        this.valorMensal = valorMensal;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.ativo = ativo;
    }

    public Contrato(Integer imovelId, Integer clienteId, double valorMensal, LocalDate dataInicio, LocalDate dataFim,
            boolean ativo) {
        this(null, imovelId, clienteId, valorMensal, dataInicio, dataFim, ativo);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getImovelId() {
        return imovelId;
    }

    public void setImovelId(Integer imovelId) {
        this.imovelId = imovelId;
    }

    public Integer getClienteId() {
        return clienteId;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }

    public double getValorMensal() {
        return valorMensal;
    }

    public void setValorMensal(double v) {
        this.valorMensal = v;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate d) {
        this.dataInicio = d;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate d) {
        this.dataFim = d;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean a) {
        this.ativo = a;
    }

    public String toString() {
        return String.format("#%d - Imóvel:%d | Cliente:%d | R$ %.2f | %s -> %s | Ativo:%s", id, imovelId, clienteId,
                valorMensal, dataInicio, dataFim, ativo ? "Sim" : "Não");
    }
}