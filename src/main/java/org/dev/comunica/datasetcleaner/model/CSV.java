package org.dev.comunica.datasetcleaner.model;

import com.opencsv.CSVReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CSV {

    public static Coluna[] lerColunas(File path) throws Exception {
        var res = new ArrayList<Coluna>();
        
        try (CSVReader reader = new CSVReader(new FileReader(path))) {
            String[] colunas = reader.readNext(); // Lê a primeira linha (cabeçalho)
            
            if (colunas != null) {
                for (int i = 0; i < colunas.length; i++) {
                    res.add(new Coluna(colunas[i], i));
                }
            }
        }
        
        return res.toArray(new Coluna[0]);
    }

    public static Mensagem[] lerCSV(File path, int[] colunas) throws Exception {
        var res = new ArrayList<Mensagem>();
        
        try (CSVReader reader = new CSVReader(new FileReader(path))) {
            reader.readNext(); // Pula a primeira linha (cabeçalho)
            
            String[] linha;
            // Lê linha por linha. O OpenCSV já lida com vírgulas dentro de aspas duplas!
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
        
        return res.toArray(new Mensagem[0]);
    }

    public static void gerarCSV(Mensagem[] mensagens, String fileName) {
        // Mantido exatamente como o original para não quebrar a estrutura da classe Mensagem
        try (var writer = new FileWriter(fileName + "-filtrado.csv")) {
            writer.write("md5,tipo,conteudo,verificado,fake,justificativa\n");
            
            for (int i = 0; i < mensagens.length; i++) {
                if (mensagens[i].getConteudo().length() > 10) {
                    writer.write(mensagens[i].toString() + "\n");    
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Mensagem converter(String[] dados, int[] colunas) {
        // Utilizamos a abordagem segura com tratamento do "long" para o ID
        String idStr = obterValorSeguro(dados, colunas[0]);
        String conteudo = obterValorSeguro(dados, colunas[1]);
        String verificadoStr = obterValorSeguro(dados, colunas[2]);
        String fakeStr = obterValorSeguro(dados, colunas[3]);
        String justificativa = obterValorSeguro(dados, colunas[4]);

        long id = idStr.isEmpty() ? 0L : Long.parseLong(idStr);
        boolean verificado = verificadoStr.equals("1");
        boolean fake = fakeStr.equals("1");

        return new Mensagem(id, conteudo, verificado, fake, justificativa);
    }

    private static String obterValorSeguro(String[] dados, int indice) {
        if (indice >= 0 && indice < dados.length) {
            return dados[indice].trim(); 
        }
        return "";
    }
}