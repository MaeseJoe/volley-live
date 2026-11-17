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
}
