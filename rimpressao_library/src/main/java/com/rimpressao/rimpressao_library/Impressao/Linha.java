package com.rimpressao.rimpressao_library.Impressao;

import android.graphics.Bitmap;

import java.util.HashMap;

public class Linha {
    private String texto;
    private Bitmap imagem;
    private HashMap<String, Integer> estilo;

    public Linha(String texto, Bitmap imagem, HashMap<String, Integer> estilo) {
        this.texto = texto;
        this.imagem = imagem;
        this.estilo = estilo;
    }

    public String getTexto() {
        return texto;
    }

    public Bitmap getImagem() {
        return imagem;
    }

    public HashMap<String, Integer> getEstilo() {
        return estilo;
    }
}
