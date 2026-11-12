package com.example.demo.Seguridad.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Para dar de baja la cuenta hay que confirmar con la contraseña
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BajaCuentaRequest {
    private String refreshToken;
    private String password;
}
