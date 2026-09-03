package org.dev.comunica.datasetcleaner.model;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CSV {

    public static Coluna[] lerColunas(File path) throws Exception {
        var reader = new BufferedReader(new FileReader(path));

        var res = new ArrayList<Coluna>();
        var linha = reader.readLine();
        var colunas = linha.split(",");

        for (int i = 0; i < colunas.length; i++) {
            res.add(new Coluna(colunas[i], i));
        }
        reader.close();

        return res.toArray(new Coluna[res.size()]);
    }

    public static Mensagem[] lerCSV(File path, int[] colunas) throws Exception {
        var reader = new BufferedReader(new FileReader(path));

        var res = new ArrayList<Mensagem>();
        var linha = reader.readLine();
        while ((linha = reader.readLine()) != null) {
            var dados = linha.split(",");
            var d = converter(dados,  colunas);
            res.add(d);
        }
        reader.close();

        return res.toArray(new Mensagem[res.size()]);
    }

    public static void gerarCSV(Mensagem[] mensagens, String fileName) {
        try (var writer = new FileWriter(fileName + "-filtrado.csv")) {
            writer.write("md5,tipo,conteudo,verificado,fake,justificativa\n");
            
            for (int i = 0; i < mensagens.length; i++) {
                writer.write(mensagens[i].toString() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Mensagem converter(String[] dados, int[] colunas) {
        return new Mensagem(Integer.parseInt(dados[colunas[0]]),
            dados[colunas[1]],
            dados[colunas[2]].equals("1") ? true : false,
            dados[colunas[3]].equals("1") ? true : false,
            dados[colunas[4]]);
    }
}