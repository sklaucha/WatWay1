package com.example.demo.Model.DTO;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Lo que decide el admin al resolver o descartar una denuncia
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResolverDenunciaDTO {

    @Size(max = 500, message = "La nota no puede superar los 500 caracteres")
    private String nota;

    // Solo se usan al resolver (al descartar se ignoran)
    private boolean borrarContenido;
    private boolean desactivarUsuario;
}
