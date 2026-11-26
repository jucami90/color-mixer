package com.jucami.colormixer.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class NamedColorsTest {

    @Test
    void buscaPorNombreSinImportarMayusculasNiTildes() {
        assertThat(NamedColors.byName("Rojo")).contains(new Rgb(255, 0, 0));
        assertThat(NamedColors.byName("MARRÓN")).contains(new Rgb(139, 69, 19));
        assertThat(NamedColors.byName("  azul ")).contains(new Rgb(0, 0, 255));
    }

    @Test
    void nombreDesconocidoONulo() {
        assertThat(NamedColors.byName("turquesa")).isEmpty();
        assertThat(NamedColors.byName(null)).isEmpty();
    }

    @Test
    void nombreExactoParaColoresDeLaPaleta() {
        NamedColors.all().forEach((nombre, rgb) ->
                assertThat(NamedColors.nearestName(rgb)).isEqualTo(nombre));
    }

    @Test
    void nombreAproximadoParaColoresIntermedios() {
        assertThat(NamedColors.nearestName(new Rgb(250, 10, 5))).isEqualTo("rojo");
        assertThat(NamedColors.nearestName(new Rgb(128, 128, 255))).isEqualTo("celeste");
        assertThat(NamedColors.nearestName(new Rgb(20, 20, 20))).isEqualTo("negro");
    }
}
