package io.github.maesejoe.volley.simulator.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class MatchInProgressTest {

    @Test
    void aMatchInProgressCannotHaveATeamWithThreeSetsWon() {
        var threeSetsForHome = List.of(
                new SetScore(25, 20), new SetScore(25, 22), new SetScore(25, 18));
        var threeSetsForAway = List.of(
                new SetScore(20, 25), new SetScore(23, 25), new SetScore(21, 25));

        assertThatThrownBy(() -> new MatchInProgress(
                threeSetsForHome, new SetScore(0, 0), Team.HOME, lineups(), lineups()))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new MatchInProgress(
                threeSetsForAway, new SetScore(0, 0), Team.AWAY, lineups(), lineups()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void startingLineupMustHaveBothTeams() {
        var lineup = new HashMap<Team, Lineup>();

        lineup.put(Team.HOME, new Lineup(List.of(1, 2, 3, 4, 5, 6)));

        assertThatThrownBy(() -> new MatchInProgress(
                List.of(), new SetScore(0, 0), Team.HOME, lineups(), lineup))
                .isInstanceOf(IllegalArgumentException.class);

        lineup.clear();
        lineup.put(Team.AWAY, new Lineup(List.of(1, 2, 3, 4, 5, 6)));

        assertThatThrownBy(() -> new MatchInProgress(
                List.of(), new SetScore(0, 0), Team.AWAY, lineups(), lineup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void currentLineupMustHaveBothTeams() {
        var lineup = new Lineup(List.of(1, 2, 3, 4, 5, 6));

        assertThatThrownBy(() -> new MatchInProgress(
                List.of(), new SetScore(0, 0), Team.HOME, Map.of(Team.HOME, lineup), lineups()))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> new MatchInProgress(
                List.of(), new SetScore(0, 0), Team.AWAY, Map.of(Team.AWAY, lineup), lineups()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @CsvSource({
            "15, 13",
            "25, 24",
    })
    void finishedSetsMustBeCorrectlyEnded(int home, int away) {
        var sets = List.of(new SetScore(25, 18), new SetScore(home, away));

        assertThatThrownBy(() -> new MatchInProgress(sets, new SetScore(0, 0), Team.HOME,
                lineups(), lineups())).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void finishedSetsMustBeFourMaximum() {
        var sets = List.of(new SetScore(25, 18), new SetScore(21, 25), new SetScore(25, 20),
                new SetScore(25, 18), new SetScore(14, 16));

        assertThatThrownBy(() -> new MatchInProgress(sets, new SetScore(0, 0), Team.HOME,
                lineups(), lineups()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("more than 4");
    }

    @ParameterizedTest
    @CsvSource({
            "0, 25, 20",
            "4, 13, 15",
    })
    void currentScoreCannotBeOver(int playedSetsCount, int home, int away) {
        assertThatThrownBy(() -> new MatchInProgress(playedSets(playedSetsCount), new SetScore(home, away),
                Team.HOME, lineups(), lineups()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @CsvSource({
            "0, 15, 13",
            "0, 25, 24",
            "4, 15, 14",
    })
    void currentScoreIsNotOver(int playedSetsCount, int home, int away) {
        assertThatCode(() -> new MatchInProgress(playedSets(playedSetsCount), new SetScore(home, away),
                Team.HOME, lineups(), lineups()))
                .doesNotThrowAnyException();
    }

    @Test
    void aMatchKickoffMustReturnAnEmptyMatch() {
        var lineups = lineups();
        var match = MatchInProgress.kickOff(lineups.get(Team.HOME), lineups.get(Team.AWAY));

        assertThat(match.servingTeam()).isEqualTo(Team.HOME);
        assertThat(match.finishedSets()).isEmpty();
        assertThat(match.currentScore()).isEqualTo(new SetScore(0, 0));
        assertThat(match.startingLineups()).isEqualTo(lineups);
        assertThat(match.currentLineups()).isEqualTo(lineups);
    }

    @Test
    void setsWonForATeamAreCountedCorrectly() {
        var oneWonThreePlayed = List.of(
                new SetScore(25, 20), new SetScore(25, 22), new SetScore(22, 25));
        var twoWonThreePlayed = List.of(
                new SetScore(20, 25), new SetScore(21, 25), new SetScore(25, 18));
        var twoWonFourPlayed = List.of(
                new SetScore(25, 20), new SetScore(21, 25), new SetScore(25, 18),
                new SetScore(18, 25));

        var firstMatch = new MatchInProgress(oneWonThreePlayed, new SetScore(0, 0), Team.HOME, lineups(), lineups());
        var secondMatch = new MatchInProgress(twoWonThreePlayed, new SetScore(0, 0), Team.AWAY, lineups(), lineups());
        var thirdMatch = new MatchInProgress(twoWonFourPlayed, new SetScore(0, 0), Team.HOME, lineups(), lineups());

        assertThat(firstMatch.setsWon(Team.AWAY)).isEqualTo(1);
        assertThat(secondMatch.setsWon(Team.AWAY)).isEqualTo(2);
        assertThat(thirdMatch.setsWon(Team.HOME)).isEqualTo(2);
    }

    @Test
    void matchIsUnmodifiable() {
        var playedSets = new ArrayList<SetScore>();
        playedSets.add(new SetScore(25, 20));
        playedSets.add(new SetScore(25, 22));
        playedSets.add(new SetScore(22, 25));

        var match = new MatchInProgress(
                playedSets, new SetScore(0, 0), Team.AWAY, lineups(), lineups());
        assertThatThrownBy(() -> match.finishedSets().add(new SetScore(0, 0)))
                .isInstanceOf(UnsupportedOperationException.class);
        playedSets.add(new SetScore(0, 0));
        assertThat(match.finishedSets()).hasSize(3);
        assertThatThrownBy(() -> match.currentLineups().put(Team.HOME, new Lineup(List.of(2, 3, 4, 5, 6, 1))))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> match.startingLineups().put(Team.AWAY, new Lineup(List.of(1, 2, 3, 4, 5, 6))))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    private static Map<Team, Lineup> lineups() {
        var lineups = new HashMap<Team, Lineup>();
        lineups.put(Team.HOME, new Lineup(List.of(1, 2, 3, 4, 5, 6)));
        lineups.put(Team.AWAY, new Lineup(List.of(2, 3, 4, 5, 6, 1)));
        return lineups;
    }

    private static List<SetScore> playedSets(int count) {
        var playedSets = new ArrayList<SetScore>();
        for(int i = 0; i < count; i++) {
            if(i % 2 == 0) {
                playedSets.add(new SetScore(25, 20));
            } else {
                playedSets.add(new SetScore(20, 25));
            }
        }
        return playedSets;
    }
}
