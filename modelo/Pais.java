package modelo;

import java.util.*;
import java.io.*;
import javafx.scene.image.*;

public class Pais implements Comparable<Pais> {
    private String sigla;
    private String nome;
    private int ouro;
    private int prata;
    private int bronze;
    private int ranking;
    private byte[] bandeira;

    public String getSigla() {
        return sigla;
    }

    public Pais setSigla(String sigla) {
        this.sigla = sigla;
        return this;
    }

    public String getNome() {
        return nome;
    }

    public Pais setNome(String nome) {
        this.nome = nome;
        return this;
    }

    public int getOuro() {
        return ouro;
    }

    public Pais setOuro(int ouro) {
        this.ouro = ouro;
        return this;
    }

    public int getPrata() {
        return prata;
    }

    public Pais setPrata(int prata) {
        this.prata = prata;
        return this;
    }

    public int getBronze() {
        return bronze;
    }

    public Pais setBronze(int bronze) {
        this.bronze = bronze;
        return this;
    }

    public int getRanking() {
        return ranking;
    }

    public String getPosicao()
    {
        return ranking + "º";
    }

    public Pais setRanking(int ranking) {
        this.ranking = ranking;
        return this;
    }

    public int compareTo(Pais pais) {
        return pais.ouro == this.ouro
            ? (pais.prata == this.prata
                ? (pais.bronze == this.bronze
                    ? 0 : pais.bronze - this.bronze)
                : pais.prata - this.prata)
            : pais.ouro - this.ouro;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ranking).append("º - ").append(nome).append("(").append(sigla).append(")").append(" ").append(ouro)
            .append(" ").append(prata).append(" ").append(bronze);

        return sb.toString();
    }

    public byte[] getBandeira()
    {
        return this.bandeira;
    }

    public Pais setBandeira(byte[] bandeira)
    {
        this.bandeira = bandeira;
        return this;
    }

    public ImageView getImagemBandeira()
    {
        ImageView imgView = new ImageView(new Image(new ByteArrayInputStream(bandeira)));
        imgView.setFitHeight(30);
        imgView.setPreserveRatio(true);

        return imgView;
    }
}