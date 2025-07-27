package com.rpgsheets.characterservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document(collection = "characters") // Mapeia para a coleção 'characters' no MongoDB do seu serviço Java
public class Character {

    @Id
    private String id; // ID do personagem no seu MongoDB

    private String name; // Nome do personagem (ex: "Sir Reginald")

    private String playerUserId; // ID do usuário que 'possui' este personagem

    private List<String> sheetIds; // Lista de IDs das fichas de RPG associadas a este personagem (IDs do serviço Python)

    public Character() {
        this.sheetIds = new ArrayList<>();
    }

    public Character(String id, String name, String playerUserId, List<String> sheetIds) {
        this.id = id;
        this.name = name;
        this.playerUserId = playerUserId;
        this.sheetIds = sheetIds != null ? sheetIds : new ArrayList<>();
    }

    // --- Getters e Setters ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPlayerUserId() { return playerUserId; }
    public void setPlayerUserId(String playerUserId) { this.playerUserId = playerUserId; }
    public List<String> getSheetIds() { return sheetIds; }
    public void setSheetIds(List<String> sheetIds) { this.sheetIds = sheetIds; }

    // --- Métodos equals, hashCode e toString (gerados automaticamente pela sua IDE) ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Character character = (Character) o;
        return Objects.equals(id, character.id) && Objects.equals(name, character.name) && Objects.equals(playerUserId, character.playerUserId) && Objects.equals(sheetIds, character.sheetIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, playerUserId, sheetIds);
    }

    @Override
    public String toString() {
        return "Character{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", playerUserId='" + playerUserId + '\'' +
                ", sheetIds=" + sheetIds +
                '}';
    }
}