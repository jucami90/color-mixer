package com.jucami.colormixer;

import com.jucami.colormixer.domain.ColorMixer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ColorMixerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ColorMixerApplication.class, args);
    }

    @Bean
    ColorMixer colorMixer() {
        return new ColorMixer();
    }
}
