package com.controlefinanceiro.modelo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Modelo que representa um gasto financeiro
 */
public class Gasto {
    private String id;
    private String descricao;
    private double valor;
    private String categoria;
    private LocalDate data;
    private int mes;
    private int ano;

    public Gasto() {
        this.data = LocalDate.now();
        this.mes = this.data.getMonthValue();
        this.ano = this.data.getYear();
    }

    public Gasto(String descricao, double valor, String categoria, LocalDate data) {
        this.descricao = descricao;
        this.valor = valor;
        this.categoria = categoria;
        this.data = data;
        this.mes = data.getMonthValue();
        this.ano = data.getYear();
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
        this.mes = data.getMonthValue();
        this.ano = data.getYear();
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    @Override
    public String toString() {
        return String.format("Gasto{id='%s', descricao='%s', valor=%.2f, categoria='%s', data=%s}",
                id, descricao, valor, categoria, data.format(DateTimeFormatter.ISO_LOCAL_DATE));
    }
}

