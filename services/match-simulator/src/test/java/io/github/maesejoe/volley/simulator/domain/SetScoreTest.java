package io.github.maesejoe.volley.simulator.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SetScoreTest {

    @Test
    void settingNegativeScoreIsForbidden() {
        assertThatThrownBy(() -> new SetScore(-1, 2)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new SetScore(2, -1)).isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @CsvSource({
            "0, 0, false",
            "25, 23, true",
            "25, 24, false",
            "24, 24, false",
            "26, 24, true",
            "25, 15, true",
            "30, 28, true",
            "23, 25, true",
            "20, 25, true",
            "15, 13, false"
    })
    void setOverDependsOn25AndLead(int home, int away, boolean expected) {
        assertThat(new SetScore(home, away).isOver(25)).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
            "0, 0, false",
            "15, 13, true",
            "15, 14, false",
            "14, 14, false",
            "15, 5, true",
            "16, 14, true",
            "20, 18, true",
            "13, 15, true",
            "10, 15, true",
    })
    void setOverDependsOn15AndLead(int home, int away, boolean expected) {
        assertThat(new SetScore(home, away).isOver(15)).isEqualTo(expected);
    }

    @Test
    void pointForReturnsNewScoreAndKeepsTheOriginal() {
        var score = new SetScore(12, 16);

        var newHomeScore = score.pointFor(Team.HOME);
        var newAwayScore = score.pointFor(Team.AWAY);

        assertThat(score).isEqualTo(new SetScore(12, 16));
        assertThat(newHomeScore).isEqualTo(new SetScore(13, 16));
        assertThat(newAwayScore).isEqualTo(new SetScore(12, 17));
    }

    @ParameterizedTest
    @CsvSource({
            "25, 25, 20, HOME",
            "25, 15, 25, AWAY",
            "25, 28, 26, HOME",
            "25, 27, 29, AWAY",
            "15, 15, 13, HOME",
            "15, 10, 15, AWAY",
            "15, 17, 15, HOME",
            "15, 14, 16, AWAY",
    })
    void winnerIsTheTeamWithMoreTotalPointsWhenSetIsOver(int target, int home, int away, Team team) {
        var setScore = new SetScore(home, away);

        var winner = setScore.winner(target);

        assertThat(winner).isEqualTo(team);
    }

    @ParameterizedTest
    @CsvSource({
            "25, 15, 20",
            "25, 15, 10",
            "25, 25, 24",
            "25, 24, 25",
            "15, 11, 13",
            "15, 10, 7",
            "15, 15, 14",
            "15, 14, 15",
    })
    void winnerIsRejectedWhenSetIsNotOver(int target, int home, int away) {
        assertThatThrownBy(() -> new SetScore(home, away).winner(target))
                .isInstanceOf(IllegalStateException.class);
    }
}
