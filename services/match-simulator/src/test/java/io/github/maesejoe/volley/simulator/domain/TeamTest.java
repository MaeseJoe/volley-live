package io.github.maesejoe.volley.simulator.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TeamTest {

    @Test
    void opponentOfHomeIsAway() {
        assertThat(Team.HOME.opponent()).isEqualTo(Team.AWAY);
    }

    @Test
    void opponentOfAwayIsHome() {
        assertThat(Team.AWAY.opponent()).isEqualTo(Team.HOME);
    }
}
