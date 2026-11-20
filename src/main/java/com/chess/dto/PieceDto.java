package com.chess.dto;

public class PieceDto {
    private String type;
    private String color;
    private String symbol;
    private String notation;

    public PieceDto() {
    }

    public PieceDto(String type, String color, String symbol, String notation) {
        this.type = type;
        this.color = color;
        this.symbol = symbol;
        this.notation = notation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getNotation() {
        return notation;
    }

    public void setNotation(String notation) {
        this.notation = notation;
    }
}
