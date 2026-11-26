package com.jucami.colormixer.domain;

import java.util.List;

/**
 * Lógica de negocio: mezcla de 2 o 3 colores.
 */
public class ColorMixer {

    public static final int MIN_COLORES = 2;
    public static final int MAX_COLORES = 3;

    public Rgb mix(List<Rgb> colores, MixMode modo) {
        if (colores == null || colores.size() < MIN_COLORES || colores.size() > MAX_COLORES) {
            throw new IllegalArgumentException(
                    "Debes indicar entre " + MIN_COLORES + " y " + MAX_COLORES + " colores");
        }
        if (modo == null) {
            throw new IllegalArgumentException("El modo de mezcla es obligatorio");
        }
        return switch (modo) {
            case LUZ -> mezclaAditiva(colores);
            case PINTURA -> mezclaPintura(colores);
        };
    }

    /** Luz: se suman los canales RGB (con tope en 255). */
    private Rgb mezclaAditiva(List<Rgb> colores) {
        int r = 0;
        int g = 0;
        int b = 0;
        for (Rgb c : colores) {
            r += c.r();
            g += c.g();
            b += c.b();
        }
        return new Rgb(Math.min(r, 255), Math.min(g, 255), Math.min(b, 255));
    }

    /**
     * Pintura: se promedian los colores en el espacio RYB (rojo-amarillo-azul, el de los pintores)
     * y se vuelve a RGB conservando la intensidad media de los colores de entrada.
     */
    private Rgb mezclaPintura(List<Rgb> colores) {
        double r = 0;
        double y = 0;
        double b = 0;
        double intensidad = 0;
        for (Rgb c : colores) {
            double[] ryb = rgbToRyb(c.r(), c.g(), c.b());
            r += ryb[0];
            y += ryb[1];
            b += ryb[2];
            intensidad += Math.max(c.r(), Math.max(c.g(), c.b()));
        }
        int n = colores.size();
        double[] rgb = rybToRgb(r / n, y / n, b / n);
        return ajustarIntensidad(rgb, intensidad / n);
    }

    /** Escala solo la parte "cromática" (sin el blanco/gris común) hasta la intensidad objetivo. */
    private Rgb ajustarIntensidad(double[] rgb, double objetivo) {
        double blanco = Math.min(rgb[0], Math.min(rgb[1], rgb[2]));
        double maxCroma = Math.max(rgb[0], Math.max(rgb[1], rgb[2])) - blanco;
        if (maxCroma > 0) {
            double factor = (objetivo - blanco) / maxCroma;
            for (int i = 0; i < 3; i++) {
                rgb[i] = blanco + (rgb[i] - blanco) * factor;
            }
        }
        return new Rgb(clamp(rgb[0]), clamp(rgb[1]), clamp(rgb[2]));
    }

    private static int clamp(double v) {
        return (int) Math.max(0, Math.min(255, Math.round(v)));
    }

    static double[] rgbToRyb(double r, double g, double b) {
        double blanco = Math.min(r, Math.min(g, b));
        r -= blanco;
        g -= blanco;
        b -= blanco;
        double maxG = Math.max(r, Math.max(g, b));

        double y = Math.min(r, g);
        r -= y;
        g -= y;
        if (b > 0 && g > 0) {
            b /= 2;
            g /= 2;
        }
        y += g;
        b += g;

        double maxY = Math.max(r, Math.max(y, b));
        if (maxY > 0) {
            double n = maxG / maxY;
            r *= n;
            y *= n;
            b *= n;
        }
        return new double[] {r + blanco, y + blanco, b + blanco};
    }

    static double[] rybToRgb(double r, double y, double b) {
        double blanco = Math.min(r, Math.min(y, b));
        r -= blanco;
        y -= blanco;
        b -= blanco;
        double maxY = Math.max(r, Math.max(y, b));

        double g = Math.min(y, b);
        y -= g;
        b -= g;
        if (b > 0 && g > 0) {
            b *= 2;
            g *= 2;
        }
        r += y;
        g += y;

        double maxG = Math.max(r, Math.max(g, b));
        if (maxG > 0) {
            double n = maxY / maxG;
            r *= n;
            g *= n;
            b *= n;
        }
        return new double[] {r + blanco, g + blanco, b + blanco};
    }
}
