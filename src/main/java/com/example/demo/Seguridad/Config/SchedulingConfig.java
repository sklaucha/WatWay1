package com.example.demo.Seguridad.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

// Habilita las tareas programadas (@Scheduled), como la limpieza
// de tokens vencidos en TokenBlacklistJpaService.
@Configuration
@EnableScheduling
public class SchedulingConfig {
}
