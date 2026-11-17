package io.github.maesejoe.volley.simulator.domain;

import java.util.ArrayList;
import java.util.List;

public record Lineup(List<Integer> players) {

    public Lineup(List<Integer> players) {
        this.players = List.copyOf(players);
        if(this.players.size() != 6) {
            throw new IllegalArgumentException("All lineups have to contain exactly 6 players.");
        }
    }

    public Lineup rotated() {
        List<Integer> newLineup = new ArrayList<>(players.subList(1, players.size()));
        Integer first = players.getFirst();
        newLineup.add(first);
        return new Lineup(newLineup);
    }

    public Integer server() {
        return players.getFirst();
    }
}
