package org.dev.comunica.datasetcleaner.model;

public class Filtro {
    
    public static boolean filtro(Mensagem m) {
        return m.getConteudo().length() > 10;
    }
}
