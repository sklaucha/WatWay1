package com.example.demo.Model.Entities;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString

public class JobOfferInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @Column(name = "empleador")
    private String empleador;

    @Column(name = "fecha-inicio")
    private Date fecha_incio;

    @Column(name = "fecha-finalizacion")
    private Date fecha_finalizacion;

}
