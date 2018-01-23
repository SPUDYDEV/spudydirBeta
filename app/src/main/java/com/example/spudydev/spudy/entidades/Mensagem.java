package com.example.spudydev.spudy.entidades;

/**
 * Created by Matheus on 22/01/2018.
 */

public class Mensagem {

    private String texto;
    private String autor;
    private int idMensagem;

    public Mensagem(String texto, String autor, int idMensagem){
        this.texto = texto;
        this.autor = autor;
        this.idMensagem = idMensagem;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getIdMensagem() {
        return idMensagem;
    }

    public void setIdMensagem(int idMensagem) {
        this.idMensagem = idMensagem;
    }
}
