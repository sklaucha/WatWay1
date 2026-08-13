package com.example.demo.Model.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobOfferInfoDTO {
    private Long id;
    private String empleador;
    private Date fecha_incio;
    private Date fecha_finalizacion;
}
