package com.example.demo.Model.Repository;

import com.example.demo.Model.Entities.ForoTemaEntity;
import com.example.demo.Model.Enums.TipoTema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ForoTemaRepository extends JpaRepository<ForoTemaEntity, Long> {

    // Temas de un estado, del más nuevo al más viejo
    List<ForoTemaEntity> findByEstado_CodigoOrderByFechaCreacionDesc(String codigo);

    // Temas del foro global (estado = null) de un tipo
    List<ForoTemaEntity> findByEstadoIsNullAndTipoOrderByFechaCreacionDesc(TipoTema tipo);

    // Cantidad de temas por estado en una sola consulta: [codigo, cantidad]
    @Query("SELECT t.estado.codigo, COUNT(t) FROM ForoTemaEntity t WHERE t.estado IS NOT NULL GROUP BY t.estado.codigo")
    List<Object[]> contarTemasPorEstado();
}
