package com.jucami.colormixer.api;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Prueba de integración: levanta el contexto completo de Spring y prueba la API por HTTP.
 */
@SpringBootTest
@AutoConfigureMockMvc
class ColorControllerIT {

    @Autowired
    private MockMvc mvc;

    @Test
    void mezclaPinturaPorDefecto() throws Exception {
        mvc.perform(get("/api/colores/mezclar").param("colores", "amarillo,azul"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.modo").value("PINTURA"))
                .andExpect(jsonPath("$.hex").value("#00FF00"))
                .andExpect(jsonPath("$.nombreAproximado").value("verde"))
                .andExpect(jsonPath("$.entrada.length()").value(2));
    }

    @Test
    void mezclaLuzConHexadecimales() throws Exception {
        mvc.perform(get("/api/colores/mezclar")
                        .param("colores", "#FF0000, #00FF00, #0000FF")
                        .param("modo", "luz"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hex").value("#FFFFFF"))
                .andExpect(jsonPath("$.r").value(255));
    }

    @Test
    void colorDesconocidoDevuelve400() throws Exception {
        mvc.perform(get("/api/colores/mezclar").param("colores", "rojo,turquesa"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value(containsString("turquesa")));
    }

    @Test
    void demasiadosColoresDevuelve400() throws Exception {
        mvc.perform(get("/api/colores/mezclar").param("colores", "rojo,azul,verde,negro"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value(containsString("entre 2 y 3")));
    }

    @Test
    void modoInvalidoDevuelve400() throws Exception {
        mvc.perform(get("/api/colores/mezclar").param("colores", "rojo,azul").param("modo", "acuarela"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value(containsString("PINTURA o LUZ")));
    }

    @Test
    void parametroFaltanteDevuelve400() throws Exception {
        mvc.perform(get("/api/colores/mezclar"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value(containsString("colores")));
    }

    @Test
    void listaLaPaleta() throws Exception {
        mvc.perform(get("/api/colores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rojo").value("#FF0000"));
    }

    @Test
    void healthCheckDelActuator() throws Exception {
        mvc.perform(get("/actuator/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void sirveLaPaginaWeb() throws Exception {
        mvc.perform(get("/index.html"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Mezclador de colores")));
    }
}
