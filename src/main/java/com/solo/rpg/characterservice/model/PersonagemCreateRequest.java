package com.solo.rpg.characterservice.model;

public class PersonagemCreateRequest {
    private String ownerId;
    private String nomePersonagem;
    private String fichaId;
    private String historia; // NOVO CAMPO
    private String imagem;   // NOVO CAMPO

    public PersonagemCreateRequest() {
    }

    public PersonagemCreateRequest(String ownerId, String nomePersonagem, String fichaId, String historia, String imagem) {
        this.ownerId = ownerId;
        this.nomePersonagem = nomePersonagem;
        this.fichaId = fichaId;
        this.historia = historia;
        this.imagem = imagem;
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
}