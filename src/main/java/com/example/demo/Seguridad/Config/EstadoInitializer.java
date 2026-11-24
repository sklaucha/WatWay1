package com.example.demo.Seguridad.Config;

import com.example.demo.Model.Entities.EstadoUsaEntity;
import com.example.demo.Model.Repository.EstadoUsaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

// Carga los estados del foro la primera vez que arranca el back
// (igual que RoleInitializer con los roles). Si ya están, no hace nada.
// Alaska no está porque no aparece en el mapa.
@Component
@RequiredArgsConstructor
public class EstadoInitializer implements CommandLineRunner {

    private final EstadoUsaRepository estadoUsaRepository;

    // Estados donde la agencia arranca con empleadores (después los cambia el admin)
    private static final Set<String> CON_EMPLEADORES = Set.of(
            "NV", "AZ", "WY", "ND", "SD", "NE", "KS", "OK", "IA", "MO", "IL",
            "IN", "KY", "AR", "LA", "MS", "AL", "SC", "WV", "CT", "MD", "DE");

    // {codigo, nombre, region}
    private static final List<String[]> ESTADOS = List.of(
            new String[]{"WA", "Washington", "Costa Oeste"},
            new String[]{"OR", "Oregon", "Costa Oeste"},
            new String[]{"CA", "California", "Costa Oeste"},
            new String[]{"NV", "Nevada", "Costa Oeste"},
            new String[]{"ID", "Idaho", "Costa Oeste"},
            new String[]{"MT", "Montana", "Costa Oeste"},
            new String[]{"WY", "Wyoming", "Costa Oeste"},
            new String[]{"UT", "Utah", "Costa Oeste"},
            new String[]{"CO", "Colorado", "Costa Oeste"},
            new String[]{"AZ", "Arizona", "Costa Oeste"},
            new String[]{"HI", "Hawaii", "Costa Oeste"},
            new String[]{"ND", "North Dakota", "Centro Norte"},
            new String[]{"SD", "South Dakota", "Centro Norte"},
            new String[]{"NE", "Nebraska", "Centro Norte"},
            new String[]{"KS", "Kansas", "Centro Norte"},
            new String[]{"MN", "Minnesota", "Centro Norte"},
            new String[]{"IA", "Iowa", "Centro Norte"},
            new String[]{"MO", "Missouri", "Centro Norte"},
            new String[]{"WI", "Wisconsin", "Centro Norte"},
            new String[]{"IL", "Illinois", "Centro Norte"},
            new String[]{"MI", "Michigan", "Centro Norte"},
            new String[]{"IN", "Indiana", "Centro Norte"},
            new String[]{"OH", "Ohio", "Centro Norte"},
            new String[]{"KY", "Kentucky", "Centro Norte"},
            new String[]{"NM", "New Mexico", "Centro Sur"},
            new String[]{"TX", "Texas", "Centro Sur"},
            new String[]{"OK", "Oklahoma", "Centro Sur"},
            new String[]{"AR", "Arkansas", "Centro Sur"},
            new String[]{"LA", "Louisiana", "Centro Sur"},
            new String[]{"MS", "Mississippi", "Centro Sur"},
            new String[]{"AL", "Alabama", "Centro Sur"},
            new String[]{"ME", "Maine", "Costa Este"},
            new String[]{"NH", "New Hampshire", "Costa Este"},
            new String[]{"VT", "Vermont", "Costa Este"},
            new String[]{"MA", "Massachusetts", "Costa Este"},
            new String[]{"RI", "Rhode Island", "Costa Este"},
            new String[]{"CT", "Connecticut", "Costa Este"},
            new String[]{"NY", "New York", "Costa Este"},
            new String[]{"NJ", "New Jersey", "Costa Este"},
            new String[]{"PA", "Pennsylvania", "Costa Este"},
            new String[]{"DE", "Delaware", "Costa Este"},
            new String[]{"MD", "Maryland", "Costa Este"},
            new String[]{"DC", "District of Columbia", "Costa Este"},
            new String[]{"VA", "Virginia", "Costa Este"},
            new String[]{"WV", "West Virginia", "Costa Este"},
            new String[]{"TN", "Tennessee", "Costa Este"},
            new String[]{"NC", "North Carolina", "Costa Este"},
            new String[]{"SC", "South Carolina", "Costa Sur"},
            new String[]{"GA", "Georgia", "Costa Sur"},
            new String[]{"FL", "Florida", "Costa Sur"}
    );

    @Override
    public void run(String... args) {
        for (String[] e : ESTADOS) {
            if (!estadoUsaRepository.existsById(e[0])) {
                estadoUsaRepository.save(EstadoUsaEntity.builder()
                        .codigo(e[0])
                        .nombre(e[1])
                        .region(e[2])
                        .tieneEmpleadores(CON_EMPLEADORES.contains(e[0]))
                        .build());
            }
        }
    }
}
