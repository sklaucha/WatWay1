package com.example.demo.Model.Repository;

import com.example.demo.Model.Entities.JobOfferInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobOfferInfoRepository extends JpaRepository<JobOfferInfo,Long> {
}
