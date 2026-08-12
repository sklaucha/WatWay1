package com.example.demo.Model.Entities;
import jakarta.persistence.*;
import lombok.*;
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
    @Column(name = "ciudad")
    private  String ciudad;
    @Column(name = "estado")
    private  String estado;

}
