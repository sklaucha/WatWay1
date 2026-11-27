package com.example.demo.Model.Services;

import com.example.demo.Model.DTO.CrearDenunciaDTO;
import com.example.demo.Model.DTO.DenunciaDTO;
import com.example.demo.Model.DTO.ResolverDenunciaDTO;
import com.example.demo.Model.Entities.DenunciaEntity;
import com.example.demo.Model.Entities.UsuarioEntity;
import com.example.demo.Model.Enums.EstadoDenuncia;
import com.example.demo.Model.Enums.TipoDenuncia;
import com.example.demo.Model.Repository.DenunciaRepository;
import com.example.demo.Model.Repository.UsuarioRepository;
import com.example.demo.Model.Services.denuncias.ContenidoDenunciado;
import com.example.demo.Model.Services.denuncias.ManejadorDenuncia;
import com.example.demo.Seguridad.Repositories.CredentialsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

// Denuncias de usuarios y moderación del admin.
//  - El ciclo de vida lo decide EstadoDenuncia (STATE).
//  - Qué se busca y qué se borra según el tipo lo decide cada ManejadorDenuncia (STRATEGY).
//  - Este service solo coordina.
@Service
public class DenunciaService {

    private static final List<EstadoDenuncia> ABIERTAS = List.of(EstadoDenuncia.PENDIENTE, EstadoDenuncia.EN_REVISION);

    private final DenunciaRepository denunciaRepository;
    private final UsuarioRepository usuarioRepository;
    private final CredentialsRepository credentialsRepository;
    private final UsuarioActualProvider usuarioActual;
    // Un manejador por tipo. Spring inyecta todas las clases que implementan ManejadorDenuncia.
    private final Map<TipoDenuncia, ManejadorDenuncia> manejadores = new EnumMap<>(TipoDenuncia.class);

    public DenunciaService(DenunciaRepository denunciaRepository,
                           UsuarioRepository usuarioRepository,
                           CredentialsRepository credentialsRepository,
                           UsuarioActualProvider usuarioActual,
                           List<ManejadorDenuncia> listaManejadores) {
        this.denunciaRepository = denunciaRepository;
        this.usuarioRepository = usuarioRepository;
        this.credentialsRepository = credentialsRepository;
        this.usuarioActual = usuarioActual;
        listaManejadores.forEach(m -> manejadores.put(m.tipo(), m));
    }

    // ================= USUARIO =================

    @Transactional
    public DenunciaDTO crear(CrearDenunciaDTO dto) {
        UsuarioEntity actual = usuarioActual.getUsuario();
        ContenidoDenunciado contenido = manejador(dto.getTipo()).buscar(dto.getReferenciaId(), actual);

        if (contenido.autor().getId().equals(actual.getId()))
            throw new IllegalArgumentException("No podés denunciarte a vos mismo");

        if (denunciaRepository.existsByDenuncianteAndTipoAndReferenciaIdAndEstadoIn(
                actual, dto.getTipo(), dto.getReferenciaId(), ABIERTAS))
            throw new IllegalArgumentException("Ya denunciaste esto y el equipo lo está revisando");

        String descripcion = dto.getDescripcion() == null || dto.getDescripcion().isBlank()
                ? null : dto.getDescripcion().trim();

        DenunciaEntity denuncia = denunciaRepository.save(DenunciaEntity.builder()
                .denunciante(actual)
                .denunciado(contenido.autor())
                .tipo(dto.getTipo())
                .referenciaId(dto.getReferenciaId())
                .motivo(dto.getMotivo())
                .descripcion(descripcion)
                .contenidoDenunciado(recortar(contenido.texto()))
                .build());
        return toDTO(denuncia);
    }

    // Las denuncias que hice yo (para ver en qué estado están)
    public List<DenunciaDTO> misDenuncias() {
        return denunciaRepository.findByDenuncianteOrderByFechaCreacionDesc(usuarioActual.getUsuario())
                .stream().map(this::toDTO).toList();
    }

    // ================= ADMIN =================

    // estado null = todas
    public List<DenunciaDTO> listar(EstadoDenuncia estado) {
        List<DenunciaEntity> lista = estado == null
                ? denunciaRepository.findAllByOrderByFechaCreacionDesc()
                : denunciaRepository.findByEstadoOrderByFechaCreacionDesc(estado);
        return lista.stream().map(this::toDTO).toList();
    }

    public long contarPendientes() {
        return denunciaRepository.countByEstado(EstadoDenuncia.PENDIENTE);
    }

    @Transactional
    public DenunciaDTO revisar(Long id) {
        DenunciaEntity d = buscar(id);
        d.revisar(usuarioActual.getUsuario());
        return toDTO(denunciaRepository.save(d));
    }

    // Resolver = la denuncia era válida. El admin puede además borrar el contenido
    // y/o desactivar al usuario denunciado. Todo en una transacción: o se aplica todo o nada.
    @Transactional
    public DenunciaDTO resolver(Long id, ResolverDenunciaDTO dto) {
        DenunciaEntity d = buscar(id);
        UsuarioEntity admin = usuarioActual.getUsuario();
        ManejadorDenuncia manejador = manejador(d.getTipo());

        List<String> medidas = new ArrayList<>();
        if (dto.isDesactivarUsuario()) {
            UsuarioEntity denunciado = d.getDenunciado();
            if (denunciado.getId().equals(admin.getId()))
                throw new IllegalArgumentException("No podés desactivarte a vos mismo");
            if (esAdmin(denunciado))
                throw new IllegalArgumentException("No se puede desactivar a un administrador desde una denuncia");
        }

        // Primero el estado: si la transición no corresponde, no se toca nada más
        d.resolver(admin, dto.getNota(), null);

        if (dto.isBorrarContenido() && manejador.tieneContenidoBorrable()) {
            manejador.borrar(d.getReferenciaId());
            medidas.add("Contenido borrado");
        }
        if (dto.isDesactivarUsuario()) {
            UsuarioEntity denunciado = d.getDenunciado();
            denunciado.setActivo(false);
            usuarioRepository.save(denunciado);
            medidas.add("Usuario desactivado");
        }
        d.setMedidas(medidas.isEmpty() ? "Sin medidas" : String.join(", ", medidas));
        return toDTO(denunciaRepository.save(d));
    }

    // Descartar = la denuncia no correspondía. No se toca el contenido.
    @Transactional
    public DenunciaDTO descartar(Long id, ResolverDenunciaDTO dto) {
        DenunciaEntity d = buscar(id);
        d.descartar(usuarioActual.getUsuario(), dto == null ? null : dto.getNota());
        return toDTO(denunciaRepository.save(d));
    }

    // ---------- Ayudas ----------

    private ManejadorDenuncia manejador(TipoDenuncia tipo) {
        ManejadorDenuncia m = manejadores.get(tipo);
        if (m == null) throw new IllegalArgumentException("Tipo de denuncia no soportado");
        return m;
    }

    private DenunciaEntity buscar(Long id) {
        return denunciaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Denuncia no encontrada"));
    }

    private boolean esAdmin(UsuarioEntity u) {
        return credentialsRepository.findByEmail(u.getEmail())
                .map(c -> c.getRoles().stream().anyMatch(r -> r.getRole().name().equals("ADMIN")))
                .orElse(false);
    }

    private String recortar(String texto) {
        if (texto == null) return null;
        return texto.length() > 2200 ? texto.substring(0, 2197) + "..." : texto;
    }

    private String nombre(UsuarioEntity u) {
        return u == null ? null : u.getNombre() + " " + u.getApellido();
    }

    private DenunciaDTO toDTO(DenunciaEntity d) {
        return DenunciaDTO.builder()
                .id(d.getId())
                .tipo(d.getTipo())
                .referenciaId(d.getReferenciaId())
                .motivo(d.getMotivo())
                .descripcion(d.getDescripcion())
                .contenidoDenunciado(d.getContenidoDenunciado())
                .estado(d.getEstado())
                .denuncianteNombre(nombre(d.getDenunciante()))
                .denunciadoId(d.getDenunciado().getId())
                .denunciadoNombre(nombre(d.getDenunciado()))
                .denunciadoActivo(d.getDenunciado().isActivo())
                .fechaCreacion(d.getFechaCreacion())
                .fechaCierre(d.getFechaCierre())
                .notaAdmin(d.getNotaAdmin())
                .medidas(d.getMedidas())
                .revisadaPorNombre(nombre(d.getRevisadaPor()))
                .build();
    }
}
