package com.jucami.colormixer.api;

import java.util.List;

/** Respuesta JSON de una mezcla. */
public record MixResponse(
        List<String> entrada,
        String modo,
        String hex,
        int r,
        int g,
        int b,
        String nombreAproximado) {
}
