package Model.Entities;
import lombok.*;
import Model.Enums.Mes;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString

public class IngresosEntity {
    private String ingreso_mensual;
    private Enum<Mes> mes_Trabajado;
}
