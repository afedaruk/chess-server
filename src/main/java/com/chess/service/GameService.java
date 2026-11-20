package com.chess.service;

import Chess.Board;
import Chess.Color;
import Chess.Coordinate;
import Chess.Pieces.Piece;
import com.chess.dto.AvailableMovesDto;
import com.chess.dto.GameStateDto;
import com.chess.dto.PieceDto;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameService {

    private final Map<String, Board> games = new ConcurrentHashMap<>();

    public String createGame() {
        String gameId = UUID.randomUUID().toString().substring(0, 8);
        games.put(gameId, new Board());
        return gameId;
    }

    public Board getGameBoard(String gameId) {
        Board board = games.get(gameId);
        if (board == null) {
            throw new NoSuchElementException("Game not found with ID: " + gameId);
        }
        return board;
    }

    public GameStateDto getGameState(String gameId) {
        Board board = getGameBoard(gameId);
        return mapToDto(gameId, board);
    }

    public AvailableMovesDto getAvailableMoves(String gameId, String square) {
        Board board = getGameBoard(gameId);
        try {
            Coordinate coord = new Coordinate(square.toLowerCase());
            List<Coordinate> moves = board.getPieceMoves(coord);
            List<String> moveStrings = new ArrayList<>();
            for (Coordinate move : moves) {
                moveStrings.add(move.toString());
            }
            return new AvailableMovesDto(square, moveStrings);
        } catch (IllegalArgumentException e) {
            return new AvailableMovesDto(square, Collections.emptyList());
        }
    }

    public synchronized GameStateDto makeMove(String gameId, String fromStr, String toStr) {
        Board board = getGameBoard(gameId);

        Coordinate from;
        Coordinate to;
        try {
            from = new Coordinate(fromStr.toLowerCase());
            to = new Coordinate(toStr.toLowerCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid coordinate format: from=" + fromStr + ", to=" + toStr);
        }

        Piece piece = board.get(from);
        if (piece == null) {
            throw new IllegalArgumentException("No piece at square " + fromStr);
        }

        if (piece.getColor() != board.getTurn()) {
            throw new IllegalStateException("Not " + piece.getColor() + "'s turn. Current turn is " + board.getTurn());
        }

        List<Coordinate> validMoves = board.getPieceMoves(from);
        if (!validMoves.contains(to)) {
            throw new IllegalArgumentException("Move from " + fromStr + " to " + toStr + " is not legal.");
        }

        board.move(from, to);
        return mapToDto(gameId, board);
    }

    public synchronized GameStateDto resign(String gameId, String colorStr) {
        Board board = getGameBoard(gameId);
        Color color = colorStr.equalsIgnoreCase("black") ? Color.Black : Color.White;
        board.resign(color);
        return mapToDto(gameId, board);
    }

    public Map<String, Object> listActiveGames() {
        Map<String, Object> result = new HashMap<>();
        result.put("count", games.size());
        result.put("games", new ArrayList<>(games.keySet()));
        return result;
    }

    private GameStateDto mapToDto(String gameId, Board board) {
        GameStateDto dto = new GameStateDto();
        dto.setGameId(gameId);
        dto.setTurn(board.getTurn().name().toUpperCase());
        dto.setState(board.getState().name());
        dto.setCheck(board.isUnderCheck(board.getTurn()));

        if (board.getWinner() != null) {
            dto.setWinner(board.getWinner().name().toUpperCase());
        }

        if (board.getLastMovedPieceFrom() != null) {
            dto.setLastMovedFrom(board.getLastMovedPieceFrom().toString());
        }
        if (board.getLastMovedPieceTo() != null) {
            dto.setLastMovedTo(board.getLastMovedPieceTo().toString());
        }

        dto.setPgn(board.PGNBuilder.getRawHistory().trim());

        PieceDto[][] boardDto = new PieceDto[8][8];
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Piece piece = board.get(x, y);
                if (piece != null) {
                    String letter = piece.toLetter();
                    if (letter.isEmpty()) {
                        letter = "P";
                    }
                    boardDto[x][y] = new PieceDto(
                            piece.getClass().getSimpleName().toUpperCase(),
                            piece.getColor().name().toUpperCase(),
                            String.valueOf(piece.toChar()),
                            letter
                    );
                }
            }
        }
        dto.setBoard(boardDto);
        return dto;
    }
}
