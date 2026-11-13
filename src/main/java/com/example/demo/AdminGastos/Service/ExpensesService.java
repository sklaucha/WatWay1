package com.example.demo.AdminGastos.Service;

import com.example.demo.AdminGastos.DTO.MovimientoDTO;
import com.example.demo.AdminGastos.Entitys.ExpensesEntity;
import com.example.demo.AdminGastos.Repository.ExpensesRepository;
import com.example.demo.Model.Entities.UsuarioEntity;
import com.example.demo.Model.Enums.ExpenseGroup;
import com.example.demo.Model.Excepciones.UsuarioNotFoundException;
import com.example.demo.Model.Repository.UsuarioRepository;
import com.example.demo.Seguridad.Entities.CredentialsEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpensesService {

    private final ExpensesRepository expensesRepository;
    private final UsuarioRepository usuarioRepository;

    // Usuario logueado, sacado del token JWT
    private UsuarioEntity getUsuarioActual() {
        CredentialsEntity credentials = (CredentialsEntity)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return usuarioRepository.findByEmail(credentials.getEmail())
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado"));
    }

    // Si el movimiento es de otro usuario respondemos "no encontrado" para no dar pistas
    private ExpensesEntity buscarMovimientoPropio(Long id) {
        ExpensesEntity movimiento = expensesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Movimiento no encontrado"));
        if (movimiento.getUsuario() == null
                || !movimiento.getUsuario().getId().equals(getUsuarioActual().getId()))
            throw new EntityNotFoundException("Movimiento no encontrado");
        return movimiento;
    }

    // READ
    public List<MovimientoDTO> listar() {
        return expensesRepository.findByUsuario_IdOrderByDateDescIdDesc(getUsuarioActual().getId())
                .stream().map(this::toDto).toList();
    }

    // CREATE
    public MovimientoDTO crear(MovimientoDTO dto) {
        ExpensesEntity movimiento = new ExpensesEntity();
        copiarDatos(dto, movimiento);
        movimiento.setUsuario(getUsuarioActual());
        return toDto(expensesRepository.save(movimiento));
    }

    // UPDATE
    public MovimientoDTO actualizar(Long id, MovimientoDTO dto) {
        ExpensesEntity movimiento = buscarMovimientoPropio(id);
        copiarDatos(dto, movimiento);
        return toDto(expensesRepository.save(movimiento));
    }

    // DELETE
    public void eliminar(Long id) {
        expensesRepository.delete(buscarMovimientoPropio(id));
    }

    private void copiarDatos(MovimientoDTO dto, ExpensesEntity movimiento) {
        movimiento.setDescription(dto.getDescription().trim());
        movimiento.setCost(dto.getCost());
        movimiento.setDate(dto.getDate());
        movimiento.setCategory(dto.getCategory());
        movimiento.setType(dto.getType());
        movimiento.setExpenseGroup(grupoDe(dto.getType(), dto.getCategory()));
    }

    // Los ingresos no tienen grupo; un gasto con categoría desconocida va a OTROS
    private ExpenseGroup grupoDe(String type, String category) {
        if (!"GASTO".equals(type)) return null;
        try {
            return ExpenseGroup.valueOf(category);
        } catch (IllegalArgumentException ex) {
            return ExpenseGroup.OTROS;
        }
    }

    private MovimientoDTO toDto(ExpensesEntity e) {
        MovimientoDTO d = new MovimientoDTO();
        d.setId(e.getId());
        d.setDescription(e.getDescription());
        d.setCost(e.getCost());
        d.setDate(e.getDate());
        d.setCategory(e.getCategory());
        d.setType(e.getType());
        return d;
    }
}
