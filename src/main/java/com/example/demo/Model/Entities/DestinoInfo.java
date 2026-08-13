package com.example.demo.Model.Entities;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "destino")
public class DestinoInfo {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
    @Column(name = "ciudad")
    private  String ciudad;
    @Column(name = "estado")
    private  String estado;

}
