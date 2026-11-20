package com.chess.controller;

import com.chess.dto.AvailableMovesDto;
import com.chess.dto.GameStateDto;
import com.chess.dto.MoveDto;
import com.chess.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/games")
@CrossOrigin(origins = "*")
public class GameRestController {

    private final GameService gameService;
    private final SimpMessagingTemplate messagingTemplate;

    public GameRestController(GameService gameService, SimpMessagingTemplate messagingTemplate) {
        this.gameService = gameService;
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping
    public ResponseEntity<GameStateDto> createGame() {
        String gameId = gameService.createGame();
        GameStateDto state = gameService.getGameState(gameId);
        return ResponseEntity.status(HttpStatus.CREATED).body(state);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> listGames() {
        return ResponseEntity.ok(gameService.listActiveGames());
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<GameStateDto> getGame(@PathVariable String gameId) {
        return ResponseEntity.ok(gameService.getGameState(gameId));
    }

    @GetMapping("/{gameId}/moves")
    public ResponseEntity<AvailableMovesDto> getAvailableMoves(
            @PathVariable String gameId,
            @RequestParam String square) {
        return ResponseEntity.ok(gameService.getAvailableMoves(gameId, square));
    }

    @PostMapping("/{gameId}/move")
    public ResponseEntity<GameStateDto> makeMove(
            @PathVariable String gameId,
            @RequestBody MoveDto move) {
        GameStateDto updatedState = gameService.makeMove(gameId, move.getFrom(), move.getTo());
        messagingTemplate.convertAndSend("/topic/game/" + gameId, updatedState);
        return ResponseEntity.ok(updatedState);
    }

    @PostMapping("/{gameId}/resign")
    public ResponseEntity<GameStateDto> resign(
            @PathVariable String gameId,
            @RequestParam(defaultValue = "white") String color) {
        GameStateDto updatedState = gameService.resign(gameId, color);
        messagingTemplate.convertAndSend("/topic/game/" + gameId, updatedState);
        return ResponseEntity.ok(updatedState);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<Map<String, String>> handleBadRequest(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
    }
}
