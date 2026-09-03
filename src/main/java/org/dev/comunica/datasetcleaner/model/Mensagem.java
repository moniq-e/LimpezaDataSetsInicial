package org.dev.comunica.datasetcleaner.model;

public class Mensagem {
    private String md5 = "";
    private int tipo;
    private String conteudo;
    private boolean verificado;
    private boolean fake;
    private String justificativa;

    public Mensagem(int tipo, String conteudo, boolean verificado, boolean fake, String justificativa) {
        this.tipo = tipo;
        this.conteudo = conteudo;
        this.verificado = verificado;
        this.fake = fake;
        this.justificativa = justificativa;
    }

    @Override
    public final String toString() {
        return String.format("%s,%d,\"%s\",%d,%d,%s", md5, tipo, conteudo, verificado ? 1 : 0, fake ? 1 : 0, justificativa);
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public boolean isVerificado() {
        return verificado;
    }

    public void setVerificado(boolean verificado) {
        this.verificado = verificado;
    }

    public boolean isFake() {
        return fake;
    }

    public void setFake(boolean fake) {
        this.fake = fake;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }
}
