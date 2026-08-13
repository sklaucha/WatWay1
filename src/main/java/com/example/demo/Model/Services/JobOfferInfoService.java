package com.example.demo.Model.Services;

import com.example.demo.Model.DTO.JobOfferInfoDTO;
import com.example.demo.Model.Entities.JobOfferInfo;
import com.example.demo.Model.Repository.JobOfferInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JobOfferInfoService {
    private final JobOfferInfoRepository jobOfferInfoRepository;

    public JobOfferInfoDTO crear(JobOfferInfoDTO dto) {
            JobOfferInfo entity = JobOfferInfo.builder()
                .empleador(dto.getEmpleador())
                .fecha_incio(dto.getFecha_incio())
                .fecha_finalizacion(dto.getFecha_finalizacion())
                .build();

        JobOfferInfo guardado = jobOfferInfoRepository.save(entity);

        return JobOfferInfoDTO.builder()
                .id(guardado.getId())
                .empleador(guardado.getEmpleador())
                .fecha_incio(guardado.getFecha_incio())
                .fecha_finalizacion(guardado.getFecha_finalizacion())
                .build();
    }
}
