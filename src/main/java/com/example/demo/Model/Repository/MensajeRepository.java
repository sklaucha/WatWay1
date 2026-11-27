package com.example.demo.Model.Repository;

import com.example.demo.Model.DTO.NoLeidosDTO;
import com.example.demo.Model.Entities.MensajeEntity;
import com.example.demo.Model.Entities.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MensajeRepository extends JpaRepository<MensajeEntity, Long> {

    @Query("SELECT m FROM MensajeEntity m WHERE " +
            "(m.emisor = :usuario1 AND m.receptor = :usuario2) OR " +
            "(m.emisor = :usuario2 AND m.receptor = :usuario1) " +
            "ORDER BY m.fechaEnvio ASC")
    List<MensajeEntity> findConversacion(
            @Param("usuario1") UsuarioEntity usuario1,
            @Param("usuario2") UsuarioEntity usuario2);

    // Mensajes que me mandó otro y todavía no leí (para marcarlos al abrir el chat)
    List<MensajeEntity> findByEmisorAndReceptorAndLeidoFalse(UsuarioEntity emisor, UsuarioEntity receptor);

    // Cantidad de no leídos agrupados por quién los mandó (los eliminados no cuentan)
    @Query("SELECT new com.example.demo.Model.DTO.NoLeidosDTO(m.emisor.id, COUNT(m)) " +
            "FROM MensajeEntity m " +
            "WHERE m.receptor = :receptor AND m.leido = false AND m.eliminado = false " +
            "GROUP BY m.emisor.id")
    List<NoLeidosDTO> contarNoLeidosPorEmisor(@Param("receptor") UsuarioEntity receptor);
}
