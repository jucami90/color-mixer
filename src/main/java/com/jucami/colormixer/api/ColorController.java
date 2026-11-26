package com.jucami.colormixer.api;

import com.jucami.colormixer.domain.ColorMixer;
import com.jucami.colormixer.domain.ColorParser;
import com.jucami.colormixer.domain.MixMode;
import com.jucami.colormixer.domain.NamedColors;
import com.jucami.colormixer.domain.Rgb;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/colores")
public class ColorController {

    private final ColorMixer mixer;

    public ColorController(ColorMixer mixer) {
        this.mixer = mixer;
    }

    /**
     * Ejemplo: GET /api/colores/mezclar?colores=rojo,azul&amp;modo=pintura
     */
    @GetMapping("/mezclar")
    public MixResponse mezclar(
            @RequestParam("colores") String colores,
            @RequestParam(value = "modo", defaultValue = "PINTURA") String modo) {

        List<String> entrada = Arrays.stream(colores.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
        List<Rgb> rgb = entrada.stream().map(ColorParser::parse).toList();
        MixMode mixMode = parseModo(modo);

        Rgb resultado = mixer.mix(rgb, mixMode);
        return new MixResponse(entrada, mixMode.name(), resultado.toHex(),
                resultado.r(), resultado.g(), resultado.b(),
                NamedColors.nearestName(resultado));
    }

    /** Lista la paleta de colores disponibles por nombre. */
    @GetMapping
    public Map<String, String> paleta() {
        Map<String, String> paleta = new LinkedHashMap<>();
        NamedColors.all().forEach((nombre, rgb) -> paleta.put(nombre, rgb.toHex()));
        return paleta;
    }

    private static MixMode parseModo(String modo) {
        try {
            return MixMode.valueOf(modo.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Modo inválido: '" + modo + "'. Usa PINTURA o LUZ");
        }
    }
}
