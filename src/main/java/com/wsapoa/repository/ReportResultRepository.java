package com.wsapoa.repository;

import com.wsapoa.entity.ReportResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportResultRepository extends JpaRepository<ReportResult, Long> {
}
