package com.example.cadastro_clientes;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexao {

    public static Connection conectar() {

        Connection con = null;

        try {
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:8080/ds_cadastro_clientes",
                    "root",
                    ""
            );


        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }

        return con;
    }
}
