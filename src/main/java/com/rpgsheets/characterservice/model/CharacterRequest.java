package com.rpgsheets.characterservice.model;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Objects;

public class CharacterRequest {
    @NotBlank(message = "Nome do personagem é obrigatório")
    private String name;
    @NotBlank(message = "Player User ID é obrigatório")
    private String playerUserId;
    private List<String> sheetIds; // Opcional, para associar IDs de fichas existentes

    public CharacterRequest() {}
    public CharacterRequest(String name, String playerUserId, List<String> sheetIds) {
        this.name = name;
        this.playerUserId = playerUserId;
        this.sheetIds = sheetIds;
    }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPlayerUserId() { return playerUserId; }
    public void setPlayerUserId(String playerUserId) { this.playerUserId = playerUserId; }
    public List<String> getSheetIds() { return sheetIds; }
    public void setSheetIds(List<String> sheetIds) { this.sheetIds = sheetIds; }

    @Override public boolean equals(Object o) { if (this == o) return true; if (o == null || getClass() != o.getClass()) return false; CharacterRequest that = (CharacterRequest) o; return Objects.equals(name, that.name) && Objects.equals(playerUserId, that.playerUserId) && Objects.equals(sheetIds, that.sheetIds); }
    @Override public int hashCode() { return Objects.hash(name, playerUserId, sheetIds); }
    @Override public String toString() { return "CharacterRequest{" + "name='" + name + '\'' + ", playerUserId='" + playerUserId + '\'' + ", sheetIds=" + sheetIds + '}'; }
}