package controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs361.battleships.models.Game;

public class MoveFleetGameAction {

    @JsonProperty private Game game;
    @JsonProperty private String direction;

    public Game getGame() { return game; }

    public String getDirection() { return direction; }

}
