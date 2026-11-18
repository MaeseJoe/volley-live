package io.github.maesejoe.volley.simulator.domain;

public record SetNumber(int value) {

    public SetNumber {
        if(value < 1 || value > 5) {
            throw new IllegalArgumentException("Set numbers must be an integer number from 1 to 5.");
        }
    }

    public int target() {
        return switch (value) {
            case 1, 2, 3, 4 -> 25;
            case 5 -> 15;
            default -> throw new IllegalStateException("Set numbers must be an integer number from 1 to 5.");
        };
    }
}
