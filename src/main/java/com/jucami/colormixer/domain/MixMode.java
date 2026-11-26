package com.jucami.colormixer.domain;

/**
 * Modo de mezcla.
 * <ul>
 *   <li>PINTURA: mezcla sustractiva (pigmentos) usando el modelo RYB: rojo + azul = morado,
 *       amarillo + azul = verde, rojo + amarillo = naranja.</li>
 *   <li>LUZ: mezcla aditiva (pantallas) en RGB: rojo + verde = amarillo, rojo + verde + azul = blanco.</li>
 * </ul>
 */
public enum MixMode {
    PINTURA,
    LUZ
}
