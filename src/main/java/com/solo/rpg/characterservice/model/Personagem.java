package com.solo.rpg.characterservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document(collection = "personagens")
public class Personagem {

    @Id
    private String id;
    private String ownerId;
    private String nomePersonagem;
    private String fichaId;
    private String historia; // NOVO CAMPO
    private String imagem;   // NOVO CAMPO

    public Personagem() {
    }

    public Personagem(String id, String ownerId, String nomePersonagem, String fichaId, String historia, String imagem) {
        this.id = id;
        this.ownerId = ownerId;
        this.nomePersonagem = nomePersonagem;
        this.fichaId = fichaId;
        this.historia = historia;
        this.imagem = imagem;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getNomePersonagem() {
        return nomePersonagem;
    }

    public void setNomePersonagem(String nomePersonagem) {
        this.nomePersonagem = nomePersonagem;
    }

    public String getFichaId() {
        return fichaId;
    }

    public void setFichaId(String fichaId) {
        this.fichaId = fichaId;
    }

    public String getHistoria() {
        return historia;
    }

    public void setHistoria(String historia) {
        this.historia = historia;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Personagem that = (Personagem) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Personagem{" +
                "id='" + id + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", nomePersonagem='" + nomePersonagem + '\'' +
                ", fichaId='" + fichaId + '\'' +
                ", historia='" + historia + '\'' +
                ", imagem='" + imagem + '\'' +
                '}';
    }
}