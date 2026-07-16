package com.example.controle_estacionamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Veiculo {

    private String placa;
    private String modelo;
    private String tipo;
    private LocalDateTime entrada;
    private LocalDateTime saida;
    private int horas;
    private BigDecimal valor;
    private String situacao;

    public Veiculo() {
    }

    public Veiculo(String placa, String modelo, String tipo,
                   LocalDateTime entrada, LocalDateTime saida,
                   int horas, BigDecimal valor, String situacao) {

        this.placa = placa;
        this.modelo = modelo;
        this.tipo = tipo;
        this.entrada = entrada;
        this.saida = saida;
        this.horas = horas;
        this.valor = valor;
        this.situacao = situacao;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getEntrada() {
        return entrada;
    }

    public void setEntrada(LocalDateTime entrada) {
        this.entrada = entrada;
    }

    public LocalDateTime getSaida() {
        return saida;
    }

    public void setSaida(LocalDateTime saida) {
        this.saida = saida;
    }

    public int getHoras() {
        return horas;
    }

    public void setHoras(int horas) {
        this.horas = horas;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }
}