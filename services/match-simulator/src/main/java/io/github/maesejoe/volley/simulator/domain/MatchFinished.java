package io.github.maesejoe.volley.simulator.domain;

import java.util.List;

public record MatchFinished(List<SetScore> finishedSets) implements MatchState {

    public MatchFinished {
        finishedSets = List.copyOf(finishedSets);
    }
}
