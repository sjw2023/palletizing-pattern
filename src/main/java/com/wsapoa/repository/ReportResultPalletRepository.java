package com.wsapoa.repository;

import com.wsapoa.entity.ReportResultPallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportResultPalletRepository extends JpaRepository<ReportResultPallet, Long> {
}
