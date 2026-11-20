package com.chess.controller;

import com.chess.dto.GameStateDto;
import com.chess.dto.MoveDto;
import com.chess.service.GameService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GameWebSocketController {

    private final GameService gameService;

    public GameWebSocketController(GameService gameService) {
        this.gameService = gameService;
    }

    @MessageMapping("/game/{gameId}/move")
    @SendTo("/topic/game/{gameId}")
    public GameStateDto handleMove(@DestinationVariable String gameId, MoveDto move) {
        return gameService.makeMove(gameId, move.getFrom(), move.getTo());
    }

    @MessageMapping("/game/{gameId}/join")
    @SendTo("/topic/game/{gameId}")
    public GameStateDto handleJoin(@DestinationVariable String gameId) {
        return gameService.getGameState(gameId);
    }
}
