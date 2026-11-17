package io.github.maesejoe.volley.simulator.domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LineupTest {

    @Test
    void rotatedMovesEachPlayerOnePositionForward() {
        var lineup = new Lineup(List.of(1, 2, 3, 4, 5, 6));

        var rotated = lineup.rotated();

        assertThat(rotated.players()).containsExactly(2, 3, 4, 5, 6, 1);
    }

    @Test
    void rotatedDoesNotModifyTheOriginalLineup() {
        var lineup = new Lineup(List.of(1, 2, 3, 4, 5, 6));

        lineup.rotated();

        assertThat(lineup.players()).containsExactly(1, 2, 3, 4, 5, 6);
    }

    @Test
    void serverIsAlwaysTheFirstInLineup() {
        var lineup = new Lineup(List.of(1, 2, 3, 4, 5, 6));

        assertThat(lineup.server()).isEqualTo(1);

        var rotated = lineup.rotated();

        assertThat(rotated.server()).isEqualTo(2);
    }

    @Test
    void sixRotatedMovesEachPlayerIntoInitialLineup() {
        var lineup = new Lineup(List.of(1, 2, 3, 4, 5, 6));

        var rotated = lineup.rotated();
        for(var i = 0; i < 5; i++) {
            rotated = rotated.rotated();
        }

        assertThat(rotated.players()).containsExactly(1, 2, 3, 4, 5, 6);
    }

    @Test
    void lineupSizesDifferentFromSixFail() {
        assertThatThrownBy(() -> new Lineup(List.of(1, 2, 3, 4, 5)))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Lineup(List.of(1, 2, 3, 4, 5, 6, 7)))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Lineup(List.of()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void lineupIsNotAffectedByLaterChangesToTheOriginalList() {
        var original = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6));
        var lineup = new Lineup(original);

        original.set(0, 99);

        assertThat(lineup.players()).containsExactly(1, 2, 3, 4, 5, 6);
    }

    @Test
    void playersListIsUnmodifiable() {
        assertThatThrownBy(() -> new Lineup(List.of(1, 2, 3, 4, 5, 6)).players().add(7))
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
