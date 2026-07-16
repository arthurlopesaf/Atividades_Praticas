package com.example.cadastro_clientes;

import java.time.LocalDateTime;

public class Clientes {

    private int id;
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private String cidade;
    private boolean ativo;
    private LocalDateTime dataCadastro;


    public Clientes() {
    }


    public Clientes(int id, String nome, String cpf, String email,
                   String telefone, String cidade, boolean ativo,
                   LocalDateTime dataCadastro) {

        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
        this.cidade = cidade;
        this.ativo = ativo;
        this.dataCadastro = dataCadastro;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }


    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }


    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }


    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }


    @Override
    public String toString() {
        return nome;
    }
}