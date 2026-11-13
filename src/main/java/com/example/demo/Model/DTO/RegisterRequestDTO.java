package com.example.demo.Model.DTO;

import com.example.demo.Model.Enums.Sexo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Setter
@Getter
@NoArgsConstructor // Jackson lo necesita para armar el objeto desde el JSON
@AllArgsConstructor
@Builder
public class RegisterRequestDTO {

    // Letras (con tildes y ñ), separadas por un espacio, ' o -. Ej: "María José", "O'Connor", "Pérez-Gómez".
    // Bloquea números, símbolos, emojis, caracteres invisibles y saltos de línea.
    public static final String SOLO_LETRAS = "^ *[\\p{L}\\p{M}]+(?:[ '\\-][\\p{L}\\p{M}]+)* *$";

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 60, message = "El nombre no puede superar los 60 caracteres")
    @Pattern(regexp = SOLO_LETRAS, message = "El nombre solo puede tener letras, espacios, ' o -")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 60, message = "El apellido no puede superar los 60 caracteres")
    @Pattern(regexp = SOLO_LETRAS, message = "El apellido solo puede tener letras, espacios, ' o -")
    private String apellido;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no es válido")
    @Size(max = 100, message = "El email no puede superar los 100 caracteres")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 72, message = "La contraseña debe tener entre 6 y 72 caracteres")
    private String password;

    @NotBlank(message = "La posición laboral es obligatoria")
    @Size(max = 100, message = "La posición laboral no puede superar los 100 caracteres")
    private String posicionLaboral;

    @NotBlank(message = "La ciudad destino es obligatoria")
    @Size(max = 100, message = "La ciudad destino no puede superar los 100 caracteres")
    private String ciudadDestino;

    @NotNull(message = "El sexo es obligatorio")
    private Sexo sexo;

    @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
    private String descripcion;
}