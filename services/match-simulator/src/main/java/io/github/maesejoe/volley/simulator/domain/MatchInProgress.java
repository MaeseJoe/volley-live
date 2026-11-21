package io.github.maesejoe.volley.simulator.domain;

import java.util.List;
import java.util.Map;

public record MatchInProgress(List<SetScore> finishedSets,
                              SetScore currentScore,
                              Team servingTeam,
                              Map<Team, Lineup> currentLineups,
                              Map<Team, Lineup> startingLineups) implements MatchState {

    public MatchInProgress {
        finishedSets = List.copyOf(finishedSets);
        currentLineups = Map.copyOf(currentLineups);
        startingLineups = Map.copyOf(startingLineups);
        if(!currentLineups.containsKey(Team.HOME) || !currentLineups.containsKey(Team.AWAY)) {
            throw new IllegalArgumentException("Current Lineups must contain both AWAY and HOME teams.");
        }
        if(!startingLineups.containsKey(Team.HOME) || !startingLineups.containsKey(Team.AWAY)) {
            throw new IllegalArgumentException("Starting Lineups must contain both AWAY and HOME teams.");
        }
        if(finishedSets.size() > 4) {
            throw new IllegalArgumentException("Finished sets can not be more than 4, otherwise the match has ended.");
        }
        checkFinishedSetsAreCorrect(finishedSets);
        if(countWonSets(finishedSets, Team.HOME) >= 3 || countWonSets(finishedSets, Team.AWAY) >= 3) {
            throw new IllegalArgumentException("Match can not be in progress with 3 won sets.");
        }
        if(setHasEnded(finishedSets.size() + 1, currentScore)) {
            throw new IllegalArgumentException("Current score must be an unfinished set, not an ended one.");
        }
    }

    public int setsWon(Team team) {
        return countWonSets(finishedSets, team);
    }

    public static MatchInProgress kickOff(Lineup home, Lineup away) {
        var lineups = Map.of(Team.HOME, home, Team.AWAY, away);

        return new MatchInProgress(List.of(), new SetScore(0, 0), Team.HOME, lineups, lineups);
    }

    private static int countWonSets(List<SetScore> playedSets, Team team) {
        var setsWon = 0;
        for(int i = 0; i < playedSets.size(); i ++) {
            var score = playedSets.get(i);
            if(score.winner(new SetNumber(i + 1).target()) == team) {
                setsWon++;
            }
        }
        return setsWon;
    }

    private static void checkFinishedSetsAreCorrect(List<SetScore> playedSets) {
        for(var i = 0; i < playedSets.size(); i++) {
            var set = playedSets.get(i);
            if(!setHasEnded(i + 1, set)) {
                throw new IllegalArgumentException(String.format("Set %s with score: %s - %s, is not properly finished.",
                        i + 1, set.home(), set.away()));
            }
        }
    }

    private static boolean setHasEnded(int setNumber, SetScore setScore) {
        var target = new SetNumber(setNumber).target();
        return setScore.isOver(target);
    }
}
