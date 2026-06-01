package Model.Entities;
import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString

public class ExpensesEntity {
    private  String gasto_comida;
    private  String gasto_transporte;
    private  String gasto_casa;
}
