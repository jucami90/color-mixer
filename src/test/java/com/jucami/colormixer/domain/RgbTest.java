package com.jucami.colormixer.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class RgbTest {

    @Test
    @DisplayName("Convierte de hexadecimal a RGB, con o sin #")
    void fromHex() {
        assertThat(Rgb.fromHex("#FF8000")).isEqualTo(new Rgb(255, 128, 0));
        assertThat(Rgb.fromHex("00ff00")).isEqualTo(new Rgb(0, 255, 0));
        assertThat(Rgb.fromHex("  #0000FF ")).isEqualTo(new Rgb(0, 0, 255));
    }

    @Test
    void toHexEnMayusculasConCeros() {
        assertThat(new Rgb(10, 0, 255).toHex()).isEqualTo("#0A00FF");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "#FFF", "#GGGGGG", "12345", "#1234567", "rojo"})
    void rechazaHexInvalido(String hex) {
        assertThatThrownBy(() -> Rgb.fromHex(hex))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("hexadecimal");
    }

    @Test
    void rechazaHexNulo() {
        assertThat(Rgb.isHex(null)).isFalse();
        assertThatThrownBy(() -> Rgb.fromHex(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rechazaCanalesFueraDeRango() {
        assertThatThrownBy(() -> new Rgb(256, 0, 0)).hasMessageContaining("canal r");
        assertThatThrownBy(() -> new Rgb(0, -1, 0)).hasMessageContaining("canal g");
        assertThatThrownBy(() -> new Rgb(0, 0, 300)).hasMessageContaining("canal b");
    }

    @Test
    void aceptaLimites() {
        assertThat(new Rgb(0, 0, 0).toHex()).isEqualTo("#000000");
        assertThat(new Rgb(255, 255, 255).toHex()).isEqualTo("#FFFFFF");
    }

    @Test
    void distanciaAlCuadrado() {
        assertThat(new Rgb(0, 0, 0).distanceSquared(new Rgb(1, 2, 3))).isEqualTo(14);
        assertThat(new Rgb(5, 5, 5).distanceSquared(new Rgb(5, 5, 5))).isZero();
    }
}
