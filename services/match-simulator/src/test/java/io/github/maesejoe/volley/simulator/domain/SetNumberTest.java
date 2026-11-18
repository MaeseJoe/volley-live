package io.github.maesejoe.volley.simulator.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SetNumberTest {

    @ParameterizedTest
    @ValueSource(ints = {-3, 0, 6})
    void setNumbersOutsideOneToFiveAreRejected(int set) {
        assertThatThrownBy(() -> new SetNumber(set)).isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @CsvSource({
            "1, 25",
            "2, 25",
            "3, 25",
            "4, 25",
            "5, 15"
    })
    void setsReturnExpectedTargetPoints(int input, int targetPoints) {
        var set = new SetNumber(input);

        var target = set.target();

        assertThat(target).isEqualTo(targetPoints);
    }
}
