package com.example.demo.Model.Services;

import com.example.demo.Model.DTO.*;
import com.example.demo.Model.Entities.EstadoUsaEntity;
import com.example.demo.Model.Entities.ForoRespuestaEntity;
import com.example.demo.Model.Entities.ForoTemaEntity;
import com.example.demo.Model.Entities.UsuarioEntity;
import com.example.demo.Model.Enums.TipoTema;
import com.example.demo.Model.Excepciones.AccionNoPermitidaException;
import com.example.demo.Model.Repository.EstadoUsaRepository;
import com.example.demo.Model.Repository.ForoRespuestaRepository;
import com.example.demo.Model.Repository.ForoTemaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Lógica de negocio del foro. Coordina; las reglas las tienen otros:
//  - quién está logueado     -> UsuarioActualProvider (Fabricación Pura)
//  - quién puede editar/borrar -> las entidades (Experto en Información)
//  - reglas según el tipo      -> TipoTema (Polimorfismo)
// Las dependencias llegan por constructor (Inversión de Dependencias).
@Service
@RequiredArgsConstructor
public class ForoService {

    private final EstadoUsaRepository estadoUsaRepository;
    private final ForoTemaRepository foroTemaRepository;
    private final ForoRespuestaRepository foroRespuestaRepository;
    private final UsuarioActualProvider usuarioActual;

    // ---------- Búsquedas que lanzan 404 ----------

    private EstadoUsaEntity buscarEstado(String codigo) {
        return estadoUsaRepository.findById(codigo.toUpperCase())
                .orElseThrow(() -> new EntityNotFoundException("Estado no encontrado"));
    }

    private ForoTemaEntity buscarTema(Long id) {
        return foroTemaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tema no encontrado"));
    }

    private ForoRespuestaEntity buscarRespuesta(Long id) {
        return foroRespuestaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Respuesta no encontrada"));
    }

    // ================= ESTADOS (mapa) =================

    public List<EstadoForoDTO> listarEstados() {
        Map<String, Long> cantidades = new HashMap<>();
        for (Object[] fila : foroTemaRepository.contarTemasPorEstado()) {
            cantidades.put((String) fila[0], (Long) fila[1]);
        }
        return estadoUsaRepository.findAllByOrderByNombreAsc().stream()
                .map(e -> toEstadoDTO(e, cantidades.getOrDefault(e.getCodigo(), 0L)))
                .toList();
    }

    // Solo ADMIN (lo controla el controller)
    public EstadoForoDTO marcarEmpleadores(String codigo, boolean tieneEmpleadores) {
        EstadoUsaEntity estado = buscarEstado(codigo);
        estado.setTieneEmpleadores(tieneEmpleadores);
        estadoUsaRepository.save(estado);
        long cantidad = foroTemaRepository.findByEstado_CodigoOrderByFechaCreacionDesc(estado.getCodigo()).size();
        return toEstadoDTO(estado, cantidad);
    }

    // ================= TEMAS (CRUD) =================

    // READ: temas de un estado
    public List<TemaResumenDTO> temasDeEstado(String codigo) {
        EstadoUsaEntity estado = buscarEstado(codigo);
        return foroTemaRepository.findByEstado_CodigoOrderByFechaCreacionDesc(estado.getCodigo())
                .stream().map(this::toResumen).toList();
    }

    // READ: foro global -> primero las FAQ, después los temas generales
    public List<TemaResumenDTO> temasGlobales() {
        List<TemaResumenDTO> lista = new ArrayList<>();
        foroTemaRepository.findByEstadoIsNullAndTipoOrderByFechaCreacionDesc(TipoTema.FAQ)
                .forEach(t -> lista.add(toResumen(t)));
        foroTemaRepository.findByEstadoIsNullAndTipoOrderByFechaCreacionDesc(TipoTema.NORMAL)
                .forEach(t -> lista.add(toResumen(t)));
        return lista;
    }

    // READ: un tema con sus respuestas
    public TemaDetalleDTO obtenerTema(Long id) {
        ForoTemaEntity t = buscarTema(id);
        UsuarioEntity actual = usuarioActual.getUsuario();
        boolean esAdmin = usuarioActual.esAdmin();
        List<RespuestaDTO> respuestas = foroRespuestaRepository.findByTema_IdOrderByFechaCreacionAsc(id)
                .stream().map(r -> toRespuestaDTO(r, actual, esAdmin)).toList();
        return new TemaDetalleDTO(
                t.getId(), t.getTitulo(), t.getContenido(), t.getTipo(),
                t.getEstado() != null ? t.getEstado().getCodigo() : null,
                t.getEstado() != null ? t.getEstado().getNombre() : null,
                nombreCompleto(t.getAutor()), t.getFechaCreacion(), t.getFechaEdicion(),
                t.puedeEditarlo(actual, esAdmin), t.puedeBorrarlo(actual, esAdmin),
                respuestas);
    }

    // CREATE: tema en un estado
    public TemaDetalleDTO crearTemaEnEstado(String codigo, CrearTemaDTO dto) {
        return guardarTemaNuevo(dto, TipoTema.NORMAL, buscarEstado(codigo));
    }

    // CREATE: tema general del foro global
    public TemaDetalleDTO crearTemaGlobal(CrearTemaDTO dto) {
        return guardarTemaNuevo(dto, TipoTema.NORMAL, null);
    }

    // CREATE: pregunta frecuente (solo ADMIN, lo controla el controller)
    public TemaDetalleDTO crearFaq(CrearTemaDTO dto) {
        return guardarTemaNuevo(dto, TipoTema.FAQ, null);
    }

    private TemaDetalleDTO guardarTemaNuevo(CrearTemaDTO dto, TipoTema tipo, EstadoUsaEntity estado) {
        ForoTemaEntity tema = ForoTemaEntity.builder()
                .titulo(dto.getTitulo().trim())
                .contenido(dto.getContenido().trim())
                .tipo(tipo)
                .estado(estado)
                .autor(usuarioActual.getUsuario())
                .build();
        foroTemaRepository.save(tema);
        return obtenerTema(tema.getId());
    }

    // UPDATE
    public TemaDetalleDTO editarTema(Long id, CrearTemaDTO dto) {
        ForoTemaEntity t = buscarTema(id);
        if (!t.puedeEditarlo(usuarioActual.getUsuario(), usuarioActual.esAdmin()))
            throw new AccionNoPermitidaException("No tenés permiso para editar este tema");
        t.editar(dto.getTitulo(), dto.getContenido());
        foroTemaRepository.save(t);
        return obtenerTema(id);
    }

    // DELETE: borra también todas sus respuestas
    @Transactional
    public void eliminarTema(Long id) {
        ForoTemaEntity t = buscarTema(id);
        if (!t.puedeBorrarlo(usuarioActual.getUsuario(), usuarioActual.esAdmin()))
            throw new AccionNoPermitidaException("Solo podés borrar tus propios temas");
        foroRespuestaRepository.deleteByTema_Id(id);
        foroTemaRepository.delete(t);
    }

    // ================= RESPUESTAS (CRUD) =================

    // CREATE
    public RespuestaDTO responder(Long temaId, CrearRespuestaDTO dto) {
        ForoTemaEntity tema = buscarTema(temaId);
        if (!tema.admiteRespuestas())
            throw new IllegalArgumentException("Las preguntas frecuentes no admiten respuestas");
        UsuarioEntity actual = usuarioActual.getUsuario();
        ForoRespuestaEntity r = ForoRespuestaEntity.builder()
                .contenido(dto.getContenido().trim())
                .tema(tema)
                .autor(actual)
                .build();
        foroRespuestaRepository.save(r);
        return toRespuestaDTO(r, actual, usuarioActual.esAdmin());
    }

    // UPDATE: solo el autor
    public RespuestaDTO editarRespuesta(Long id, CrearRespuestaDTO dto) {
        ForoRespuestaEntity r = buscarRespuesta(id);
        UsuarioEntity actual = usuarioActual.getUsuario();
        if (!r.puedeEditarla(actual))
            throw new AccionNoPermitidaException("Solo podés editar tus propias respuestas");
        r.editar(dto.getContenido());
        foroRespuestaRepository.save(r);
        return toRespuestaDTO(r, actual, usuarioActual.esAdmin());
    }

    // DELETE: el autor o un admin
    public void eliminarRespuesta(Long id) {
        ForoRespuestaEntity r = buscarRespuesta(id);
        if (!r.puedeBorrarla(usuarioActual.getUsuario(), usuarioActual.esAdmin()))
            throw new AccionNoPermitidaException("Solo podés borrar tus propias respuestas");
        foroRespuestaRepository.delete(r);
    }

    // ---------- Conversión a DTO ----------

    private String nombreCompleto(UsuarioEntity u) {
        return u.getNombre() + " " + u.getApellido();
    }

    private EstadoForoDTO toEstadoDTO(EstadoUsaEntity e, long cantidadTemas) {
        return new EstadoForoDTO(e.getCodigo(), e.getNombre(), e.getRegion(), e.isTieneEmpleadores(), cantidadTemas);
    }

    private TemaResumenDTO toResumen(ForoTemaEntity t) {
        return new TemaResumenDTO(t.getId(), t.getTitulo(), t.getTipo(), nombreCompleto(t.getAutor()),
                t.getFechaCreacion(), foroRespuestaRepository.countByTema_Id(t.getId()));
    }

    private RespuestaDTO toRespuestaDTO(ForoRespuestaEntity r, UsuarioEntity actual, boolean esAdmin) {
        return new RespuestaDTO(r.getId(), r.getContenido(), nombreCompleto(r.getAutor()),
                r.getFechaCreacion(), r.getFechaEdicion(),
                r.puedeEditarla(actual), r.puedeBorrarla(actual, esAdmin));
    }
}
