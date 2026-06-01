package Model.Entities;

import java.util.List;
import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class JobsInfo {

    private List<String> positions;
    private String WageXhour;
    private Integer minimum_hours;
}
