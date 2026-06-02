package Model.Entities;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class VuelosEntity {
    private String numVuelo ;
    private LocalDate fechaIda;
    private LocalDate fechaRegreso;

}
