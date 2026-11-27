package com.example.demo.Model.Entities;

import jakarta.persistence.*;
import lombok.*;

// Un estado de EE.UU. Lo carga solo EstadoInitializer al arrancar el back.
// La clave es el código de 2 letras ("FL", "NV"), que es fijo y nunca cambia.
@Entity
@Table(name = "estado_usa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadoUsaEntity {

    @Id
    @Column(length = 2)
    private String codigo;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false, length = 30)
    private String region;

    // Lo marca el admin desde su panel
    @Column(name = "tiene_empleadores", nullable = false)
    private boolean tieneEmpleadores;
}
