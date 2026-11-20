package com.chess.dto;

import java.util.List;

public class GameStateDto {
    private String gameId;
    private PieceDto[][] board;
    private String turn;
    private String state;
    private boolean check;
    private String winner;
    private String lastMovedFrom;
    private String lastMovedTo;
    private String pgn;
    private List<String> history;

    public GameStateDto() {
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public PieceDto[][] getBoard() {
        return board;
    }

    public void setBoard(PieceDto[][] board) {
        this.board = board;
    }

    public String getTurn() {
        return turn;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getLastMovedFrom() {
        return lastMovedFrom;
    }

    public void setLastMovedFrom(String lastMovedFrom) {
        this.lastMovedFrom = lastMovedFrom;
    }

    public String getLastMovedTo() {
        return lastMovedTo;
    }

    public void setLastMovedTo(String lastMovedTo) {
        this.lastMovedTo = lastMovedTo;
    }

    public String getPgn() {
        return pgn;
    }

    public void setPgn(String pgn) {
        this.pgn = pgn;
    }

    public List<String> getHistory() {
        return history;
    }

    public void setHistory(List<String> history) {
        this.history = history;
    }
}
