package com.wsapoa.repository;

import com.wsapoa.entity.Pallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PalletRepository extends JpaRepository<Pallet, Long> {
}