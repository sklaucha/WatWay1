package Model.Entities;
import Model.Enums.AgenciasEnum;
import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString

public class JobOfferInfo {
    private AgenciasEnum agencia;
    private  JobsInfo Trabajo;

}
