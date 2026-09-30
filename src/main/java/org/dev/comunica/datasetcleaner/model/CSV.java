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

    public static void gerarCSV(Mensagem[] mensagens, String fileName) {
        // Mantido exatamente como o original para não quebrar a estrutura da classe Mensagem
        try (var writer = new FileWriter(fileName + "-filtrado.csv")) {
            writer.write("md5,tipo,conteudo,verificado,fake,justificativa\n");
            
            for (int i = 0; i < mensagens.length; i++) {
                if (Filtro.filtro(mensagens[i])) {
                    writer.write(mensagens[i].toString() + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Mensagem converter(String[] dados, int[] colunas) {
        String idStr = obterValorSeguro(dados, colunas[0]);
        String conteudo = obterValorSeguro(dados, colunas[1]);
        String verificadoStr = obterValorSeguro(dados, colunas[2]);
        String fakeStr = obterValorSeguro(dados, colunas[3]);
        String justificativa = obterValorSeguro(dados, colunas[4]);

        long id = idStr.isEmpty() ? 0L : Long.parseLong(idStr);
        boolean verificado = "1simtrueverdadeiro".contains(verificadoStr);
        boolean fake = "1simtrueverdadeiro".contains(fakeStr);

        return new Mensagem(id, conteudo, verificado, fake, justificativa);
    }

    private static String obterValorSeguro(String[] dados, int indice) {
        if (indice == dados.length) {
            return "0";
        } else if (indice == dados.length + 1) {
            return "1";
        }

        if (indice >= 0 && indice < dados.length) {
            return dados[indice].trim(); 
        }
        return "";
    }
}