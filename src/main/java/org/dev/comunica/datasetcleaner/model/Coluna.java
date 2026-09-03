package org.dev.comunica.datasetcleaner.model;

public record Coluna(String nome, int valor) {
    
    @Override
    public final String toString() {
        return nome;
    }
}
