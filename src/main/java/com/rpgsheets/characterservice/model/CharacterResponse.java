package com.rpgsheets.characterservice.model;

import java.util.List;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL) // Não inclui campos nulos no JSON de resposta
public class CharacterResponse {
    private String id;
    private String name;
    private String playerUserId;
    private List<String> sheetIds;

    public CharacterResponse() {}
    public CharacterResponse(String id, String name, String playerUserId, List<String> sheetIds) {
        this.id = id;
        this.name = name;
        this.playerUserId = playerUserId;
        this.sheetIds = sheetIds;
    }
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPlayerUserId() { return playerUserId; }
    public void setPlayerUserId(String playerUserId) { this.playerUserId = playerUserId; }
    public List<String> getSheetIds() { return sheetIds; }
    public void setSheetIds(List<String> sheetIds) { this.sheetIds = sheetIds; }

    @Override public boolean equals(Object o) { if (this == o) return true; if (o == null || getClass() != o.getClass()) return false; CharacterResponse that = (CharacterResponse) o; return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(playerUserId, that.playerUserId) && Objects.equals(sheetIds, that.sheetIds); }
    @Override public int hashCode() { return Objects.hash(id, name, playerUserId, sheetIds); }
    @Override public String toString() { return "CharacterResponse{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", playerUserId='" + playerUserId + '\'' + ", sheetIds=" + sheetIds + '}'; }
}