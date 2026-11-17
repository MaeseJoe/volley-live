package io.github.maesejoe.volley.simulator.domain;

public enum Team {
    HOME,
    AWAY;

    public Team opponent() {
        return this == HOME ? AWAY : HOME;
    }
}
