package Model.DTO;

import Model.Enums.Sexo;
import lombok.Data;

@Data
public class RegisterRequestDTO {

    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private String posicionLaboral;
    private String ciudadDestino;
    private Sexo sexo;
    private String descripcion;
}