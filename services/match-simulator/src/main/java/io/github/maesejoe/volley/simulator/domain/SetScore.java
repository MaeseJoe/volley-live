package io.github.maesejoe.volley.simulator.domain;

public record SetScore(int home, int away) {

    public SetScore {
        if (home < 0 || away < 0) {
            throw new IllegalArgumentException("Score can not be a negative number.");
        }
    }

    public boolean isOver(int target) {
        if(home >= target || away >= target) {
            return Math.abs(home - away) >= 2;
        }
        return false;
    }

    public SetScore pointFor(Team team) {
        return switch (team) {
            case HOME -> new SetScore(home + 1, away);
            case AWAY -> new SetScore(home, away + 1);
        };
    }
}
