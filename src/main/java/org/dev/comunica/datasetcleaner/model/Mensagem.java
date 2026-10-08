package org.dev.comunica.datasetcleaner.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public class Mensagem {
    private String titulo;
    private String texto;
    private String hashConteudo = "";
    private boolean verificado;
    private String url;
    private String resumo;

    public Mensagem(String titulo, String texto, boolean verificado, String url, String resumo) {
        this.titulo = titulo;
        this.texto = texto;
        this.verificado = verificado;
        this.url = url;
        this.resumo = resumo;
    }

    @Override
    public final String toString() {
        try {
            return String.format("\"%s\",\"%s\",%s,%d,\"%s\",\"%s\"", 
                titulo, texto, getHashConteudo(), verificado ? 1 : 0, url, resumo);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return String.format("\"%s\",\"%s\",%s,%d,\"%s\",\"%s\"", 
                titulo, texto, hashConteudo, verificado ? 1 : 0, url, resumo);
        }
    }

    public String getHashConteudo() throws NoSuchAlgorithmException {
        if (hashConteudo == null || hashConteudo.isBlank()) {
            var md = MessageDigest.getInstance("MD5");
            // Calcula o hash baseado no texto (ou titulo + texto, se preferir)
            md.update(texto != null ? texto.getBytes() : new byte[0]);
            var digest = md.digest();

            hashConteudo = HexFormat.of().withUpperCase().formatHex(digest);
        }
        return hashConteudo;
    }

    public void setHashConteudo(String hashConteudo) {
        this.hashConteudo = hashConteudo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public boolean isVerificado() {
        return verificado;
    }

    public void setVerificado(boolean verificado) {
        this.verificado = verificado;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getResumo() {
        return resumo;
    }

    public void setResumo(String resumo) {
        this.resumo = resumo;
    }
}