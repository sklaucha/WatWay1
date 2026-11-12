package com.example.demo.Model.Controller;

import com.example.demo.Model.DTO.JobOfferInfoDTO;
import com.example.demo.Model.Services.JobOfferInfoService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/joboffer")
@AllArgsConstructor
public class JobOfferInfoController {

    private final JobOfferInfoService jobOfferInfoService;

    @PostMapping
    public ResponseEntity<JobOfferInfoDTO> crear(@RequestBody JobOfferInfoDTO dto) {
        return ResponseEntity.ok(jobOfferInfoService.crear(dto));
    }
    }
