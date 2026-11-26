package com.jucami.colormixer.domain;

import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Color en el espacio RGB (cada canal de 0 a 255).
 */
public record Rgb(int r, int g, int b) {

    private static final Pattern HEX = Pattern.compile("^#?[0-9a-fA-F]{6}$");

    public Rgb {
        validar("r", r);
        validar("g", g);
        validar("b", b);
    }

    private static void validar(String canal, int valor) {
        if (valor < 0 || valor > 255) {
            throw new IllegalArgumentException(
                    "El canal " + canal + " debe estar entre 0 y 255, se recibió " + valor);
        }
    }

    /** Crea un color a partir de un texto "#RRGGBB" o "RRGGBB". */
    public static Rgb fromHex(String hex) {
        if (hex == null || !HEX.matcher(hex.trim()).matches()) {
            throw new IllegalArgumentException("Formato hexadecimal inválido: " + hex);
        }
        String limpio = hex.trim().replace("#", "");
        return new Rgb(
                Integer.parseInt(limpio.substring(0, 2), 16),
                Integer.parseInt(limpio.substring(2, 4), 16),
                Integer.parseInt(limpio.substring(4, 6), 16));
    }

    public static boolean isHex(String texto) {
        return texto != null && HEX.matcher(texto.trim()).matches();
    }

    public String toHex() {
        return String.format(Locale.ROOT, "#%02X%02X%02X", r, g, b);
    }

    /** Distancia euclídea al cuadrado entre dos colores (para buscar el más cercano). */
    public int distanceSquared(Rgb otro) {
        int dr = r - otro.r;
        int dg = g - otro.g;
        int db = b - otro.b;
        return dr * dr + dg * dg + db * db;
    }
}
