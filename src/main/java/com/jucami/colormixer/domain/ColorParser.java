package com.jucami.colormixer.domain;

/**
 * Convierte el texto que envía el usuario ("rojo", "#FF0000", "ff0000") en un {@link Rgb}.
 */
public final class ColorParser {

    private ColorParser() {
    }

    public static Rgb parse(String texto) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException("El color no puede estar vacío");
        }
        if (Rgb.isHex(texto)) {
            return Rgb.fromHex(texto);
        }
        return NamedColors.byName(texto)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Color desconocido: '" + texto.trim()
                                + "'. Usa un nombre de la paleta o un hexadecimal #RRGGBB"));
    }
}
