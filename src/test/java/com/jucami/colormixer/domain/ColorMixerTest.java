package com.jucami.colormixer.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ColorMixerTest {

    private final ColorMixer mixer = new ColorMixer();

    private static List<Rgb> colores(String lista) {
        return Arrays.stream(lista.split("\\+")).map(ColorParser::parse).toList();
    }

    @ParameterizedTest(name = "PINTURA: {0} = {1} ({2})")
    @CsvSource({
        "rojo+azul,          #FF00FF, morado",
        "amarillo+azul,      #00FF00, verde",
        "rojo+amarillo,      #FF8000, naranja",
        "rojo+blanco,        #FF8080, rosa",
        "negro+blanco,       #808080, gris",
        "azul+blanco,        #8080FF, celeste",
        "rojo+amarillo+azul, #555555, gris"
    })
    @DisplayName("Mezcla de pintura (modelo RYB)")
    void mezclaPintura(String entrada, String hexEsperado, String nombreEsperado) {
        Rgb resultado = mixer.mix(colores(entrada), MixMode.PINTURA);

        assertThat(resultado.toHex()).isEqualTo(hexEsperado);
        assertThat(NamedColors.nearestName(resultado)).isEqualTo(nombreEsperado);
    }

    @ParameterizedTest(name = "LUZ: {0} = {1}")
    @CsvSource({
        "rojo+verde,      #FFFF00",
        "rojo+azul,       #FF00FF",
        "verde+azul,      #00FFFF",
        "rojo+verde+azul, #FFFFFF",
        "negro+negro,     #000000"
    })
    @DisplayName("Mezcla de luz (aditiva RGB)")
    void mezclaLuz(String entrada, String hexEsperado) {
        assertThat(mixer.mix(colores(entrada), MixMode.LUZ).toHex()).isEqualTo(hexEsperado);
    }

    @Test
    @DisplayName("Propiedad: el orden de los colores no cambia el resultado")
    void ordenNoImporta() {
        for (MixMode modo : MixMode.values()) {
            assertThat(mixer.mix(colores("rojo+amarillo+azul"), modo))
                    .isEqualTo(mixer.mix(colores("azul+rojo+amarillo"), modo));
        }
    }

    @Test
    @DisplayName("Propiedad: mezclar un color consigo mismo da el mismo color")
    void mezclaConsigoMismo() {
        for (String nombre : NamedColors.all().keySet()) {
            Rgb c = NamedColors.byName(nombre).orElseThrow();
            assertThat(mixer.mix(List.of(c, c), MixMode.PINTURA)).as(nombre).isEqualTo(c);
        }
    }

    @Test
    void rechazaMenosDeDosOMasDeTresColores() {
        Rgb rojo = new Rgb(255, 0, 0);
        assertThatThrownBy(() -> mixer.mix(List.of(rojo), MixMode.LUZ))
                .hasMessageContaining("entre 2 y 3");
        assertThatThrownBy(() -> mixer.mix(List.of(rojo, rojo, rojo, rojo), MixMode.LUZ))
                .hasMessageContaining("entre 2 y 3");
        assertThatThrownBy(() -> mixer.mix(null, MixMode.LUZ))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rechazaModoNulo() {
        assertThatThrownBy(() -> mixer.mix(colores("rojo+azul"), null))
                .hasMessageContaining("modo");
    }
}
