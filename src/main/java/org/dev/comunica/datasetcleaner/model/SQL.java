package org.dev.comunica.datasetcleaner.model;

import com.opencsv.CSVReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class SQL {

    public static Coluna[] lerColunas(File path) throws Exception {
        var res = new ArrayList<Coluna>();
        
        try (var reader = new CSVReader(new FileReader(path))) {
            String[] colunas = reader.readNext(); // Lê a primeira linha (cabeçalho)
            
            if (colunas != null) {
                for (int i = 0; i < colunas.length; i++) {
                    res.add(new Coluna(colunas[i], i));
                }
            }
        }
        res.add(new Coluna("Valor Padrão: Falso", res.size()));
        res.add(new Coluna("Valor Padrão: Verdadeiro", res.size()));
        return res.toArray(new Coluna[res.size()]);
    }

    public static Mensagem[] lerCSV(File path, int[] colunas) throws Exception {
        var res = new ArrayList<Mensagem>();
        
        try (var reader = new CSVReader(new FileReader(path))) {
            reader.readNext(); // Pula a primeira linha (cabeçalho)
            
            String[] linha;
            while ((linha = reader.readNext()) != null) {
                try {
                    var d = converter(linha, colunas);
                    res.add(d);
                } catch (Exception e) {
                    // Se alguma linha específica estiver totalmente corrompida, 
                    // o erro é exibido no console, mas o arquivo continua sendo lido
                    System.err.println("Aviso: Linha ignorada devido a erro: " + e.getMessage());
                }
            }
        }
        return res.toArray(new Mensagem[res.size()]);
    }

    public static void gerarSQL(Mensagem[] mensagens, String fileName) {
        try (var writer = new FileWriter(fileName + "-filtrado.sql")) {
            writer.write("START TRANSACTION;\n\n");
            
            for (int i = 0; i < mensagens.length; i++) {
                if (!Filtro.filtro(mensagens[i])) continue;

                writer.write(String.format("INSERT INTO Noticia (titulo, texto, hash_conteudo, verificado) VALUES (%s);\n", mensagens[i].toString()));

                if (mensagens[i].getUrl() != null && !mensagens[i].getUrl().isBlank()) {
                    writer.write("SET @noticia_id = LAST_INSERT_ID();\n");
                    writer.write(String.format("INSERT INTO Fonte_Consultada (noticia_id, url, resumo) VALUES (@noticia_id,'%s', '%s');\n", mensagens[i].getUrl(), mensagens[i].getResumo()));
                }
            }
            writer.write("\nCOMMIT;\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Mensagem converter(String[] dados, int[] colunas) {
        String titulo = obterValorSeguro(dados, colunas[0]);
        String texto = obterValorSeguro(dados, colunas[1]);
        String verificadoStr = obterValorSeguro(dados, colunas[2]);
        String url = obterValorSeguro(dados, colunas[3]);
        String resumo = obterValorSeguro(dados, colunas[4]);

        boolean verificado = verificadoStr.equalsIgnoreCase("1")
                || verificadoStr.equalsIgnoreCase("sim")
                || verificadoStr.equalsIgnoreCase("true")
                || verificadoStr.equalsIgnoreCase("verdadeiro");

        return new Mensagem(titulo, texto, verificado, url, resumo);
    }

    private static String obterValorSeguro(String[] dados, int indice) {
        if (indice == dados.length) {
            return "0";
        } else if (indice == dados.length + 1) {
            return "1";
        }

        if (indice >= 0 && indice < dados.length) {
            return dados[indice].trim().replace("'", "''"); 
        }
        return "";
    }
}