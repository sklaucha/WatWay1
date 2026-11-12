package com.example.demo.Model.Entities;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
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
