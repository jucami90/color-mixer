package com.jucami.colormixer.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ColorParserTest {

    @Test
    void aceptaNombres() {
        assertThat(ColorParser.parse("amarillo")).isEqualTo(new Rgb(255, 255, 0));
    }

    @Test
    void aceptaHexadecimales() {
        assertThat(ColorParser.parse("#123456")).isEqualTo(new Rgb(0x12, 0x34, 0x56));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void rechazaVacio(String texto) {
        assertThatThrownBy(() -> ColorParser.parse(texto)).hasMessageContaining("vacío");
    }

    @Test
    void rechazaColorDesconocido() {
        assertThatThrownBy(() -> ColorParser.parse("turquesa"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Color desconocido: 'turquesa'");
    }
}
