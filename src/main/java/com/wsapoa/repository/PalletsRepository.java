package com.wsapoa.repository;

import com.wsapoa.entity.Pallets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PalletsRepository extends JpaRepository<Pallets, Long> {
}