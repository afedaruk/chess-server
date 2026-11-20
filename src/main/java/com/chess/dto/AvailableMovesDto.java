package com.chess.dto;

import java.util.List;

public class AvailableMovesDto {
    private String square;
    private List<String> moves;

    public AvailableMovesDto() {
    }

    public AvailableMovesDto(String square, List<String> moves) {
        this.square = square;
        this.moves = moves;
    }

    public String getSquare() {
        return square;
    }

    public void setSquare(String square) {
        this.square = square;
    }

    public List<String> getMoves() {
        return moves;
    }

    public void setMoves(List<String> moves) {
        this.moves = moves;
    }
}
