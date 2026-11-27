package com.example.demo.Model.Repository;

import com.example.demo.Model.Entities.DenunciaEntity;
import com.example.demo.Model.Entities.UsuarioEntity;
import com.example.demo.Model.Enums.EstadoDenuncia;
import com.example.demo.Model.Enums.TipoDenuncia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface DenunciaRepository extends JpaRepository<DenunciaEntity, Long> {

    List<DenunciaEntity> findAllByOrderByFechaCreacionDesc();

    List<DenunciaEntity> findByEstadoOrderByFechaCreacionDesc(EstadoDenuncia estado);

    List<DenunciaEntity> findByDenuncianteOrderByFechaCreacionDesc(UsuarioEntity denunciante);

    long countByEstado(EstadoDenuncia estado);

    // ¿Este usuario ya tiene una denuncia abierta sobre lo mismo?
    boolean existsByDenuncianteAndTipoAndReferenciaIdAndEstadoIn(
            UsuarioEntity denunciante, TipoDenuncia tipo, Long referenciaId, Collection<EstadoDenuncia> estados);
}
