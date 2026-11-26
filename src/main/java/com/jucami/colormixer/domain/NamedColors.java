package com.jucami.colormixer.domain;

import java.text.Normalizer;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Paleta de colores con nombre en español.
 */
public final class NamedColors {

    private static final Map<String, Rgb> PALETA;

    static {
        Map<String, Rgb> p = new LinkedHashMap<>();
        p.put("rojo", new Rgb(255, 0, 0));
        p.put("amarillo", new Rgb(255, 255, 0));
        p.put("azul", new Rgb(0, 0, 255));
        p.put("verde", new Rgb(0, 255, 0));
        p.put("naranja", new Rgb(255, 128, 0));
        p.put("morado", new Rgb(160, 32, 240));
        p.put("cian", new Rgb(0, 255, 255));
        p.put("celeste", new Rgb(135, 206, 250));
        p.put("rosa", new Rgb(255, 192, 203));
        p.put("marron", new Rgb(139, 69, 19));
        p.put("blanco", new Rgb(255, 255, 255));
        p.put("gris", new Rgb(128, 128, 128));
        p.put("negro", new Rgb(0, 0, 0));
        PALETA = Collections.unmodifiableMap(p);
    }

    private NamedColors() {
    }

    public static Map<String, Rgb> all() {
        return PALETA;
    }

    /** Busca un color por nombre, sin distinguir mayúsculas ni tildes ("Marrón" = "marron"). */
    public static Optional<Rgb> byName(String nombre) {
        if (nombre == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(PALETA.get(normalizar(nombre)));
    }

    /** Devuelve el nombre del color de la paleta más parecido. */
    public static String nearestName(Rgb color) {
        String mejor = null;
        int mejorDistancia = Integer.MAX_VALUE;
        for (Map.Entry<String, Rgb> e : PALETA.entrySet()) {
            int d = color.distanceSquared(e.getValue());
            if (d < mejorDistancia) {
                mejorDistancia = d;
                mejor = e.getKey();
            }
        }
        return mejor;
    }

    static String normalizar(String texto) {
        String sinTildes = Normalizer.normalize(texto.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return sinTildes.toLowerCase(Locale.ROOT);
    }
}
