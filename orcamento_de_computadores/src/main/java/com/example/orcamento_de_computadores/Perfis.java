package com.example.orcamento_de_computadores;

public class Perfis {

        public static final PerfilComputador ADMINISTRATIVO = new PerfilComputador(
                "Administrativo",
                "Processador básico",
                "Placa-mãe básica",
                "8 GB",
                "SSD 240 GB",
                "Vídeo integrado",
                "500 W",
                "Gabinete básico"
        );

        public static final PerfilComputador PROFESSOR_TECNOLOGIA = new PerfilComputador(
                "Professor de Tecnologia",
                "Processador intermediário",
                "Placa-mãe intermediária",
                "16 GB",
                "SSD 500 GB",
                "Vídeo integrado",
                "500 W",
                "Gabinete intermediário"
        );

        public static final PerfilComputador DESENVOLVEDOR_SOFTWARE = new PerfilComputador(
                "Desenvolvedor de Software",
                "Processador avançado",
                "Placa-mãe intermediária",
                "16 GB",
                "SSD 500 GB",
                "Dedicada básica",
                "650 W",
                "Gabinete intermediário"
        );

        public static final PerfilComputador DESENVOLVEDOR_JOGOS = new PerfilComputador(
                "Desenvolvedor de Jogos",
                "Processador de alto desempenho",
                "Placa-mãe avançada",
                "32 GB",
                "SSD 1 TB",
                "Dedicada intermediária",
                "750 W",
                "Gabinete gamer"
        );
}
