package com.wsapoa.repository;

import com.wsapoa.entity.ReportResultProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportResultProductRepository extends JpaRepository<ReportResultProduct, Long> {
}
