package Model.Entities;
import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString

public class HousingEntity {
    private DestinoInfo location;
    private String addres;
    private String costo;

}
