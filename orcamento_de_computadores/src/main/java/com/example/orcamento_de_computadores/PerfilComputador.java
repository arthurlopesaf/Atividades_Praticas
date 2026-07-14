package com.example.orcamento_de_computadores;

public class PerfilComputador {

    private String nome;
    private String processador;
    private String placaMae;
    private String memoria;
    private String ssd;
    private String placaVideo;
    private String fonte;
    private String gabinete;

    public PerfilComputador(String nome, String processador, String placaMae,
                            String memoria, String ssd, String placaVideo,
                            String fonte, String gabinete) {
        this.nome = nome;
        this.processador = processador;
        this.placaMae = placaMae;
        this.memoria = memoria;
        this.ssd = ssd;
        this.placaVideo = placaVideo;
        this.fonte = fonte;
        this.gabinete = gabinete;
    }

    public String getNome() {
        return nome;
    }

    public String getProcessador() {
        return processador;
    }

    public String getPlacaMae() {
        return placaMae;
    }

    public String getMemoria() {
        return memoria;
    }

    public String getSsd() {
        return ssd;
    }

    public String getPlacaVideo() {
        return placaVideo;
    }

    public String getFonte() {
        return fonte;
    }

    public String getGabinete() {
        return gabinete;
    }
}
