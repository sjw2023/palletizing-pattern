package com.wsapoa.services;

import com.wsapoa.dto.PalletRequestDTO;
import com.wsapoa.entity.Pallets;
import com.wsapoa.repository.PalletsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PalletService {

    private final PalletsRepository palletsRepository;

    public void createPallet(PalletRequestDTO palletRequestDTO) {
        Pallets pallet = new Pallets( palletRequestDTO );
        palletsRepository.save(pallet);
    }

    public void updatePallet(Long id, PalletRequestDTO palletRequestDTO) {
        Pallets pallet = palletsRepository.findById(id).orElseThrow(() -> new RuntimeException("Pallet not found"));
        pallet.setName(palletRequestDTO.getName());
        pallet.setWidth(palletRequestDTO.getWidth());
        pallet.setHeight(palletRequestDTO.getHeight());
        pallet.setLength(palletRequestDTO.getLength());
        pallet.setUsed(palletRequestDTO.isUsed());
        palletsRepository.save(pallet);
    }

    public void deletePallet(Long id) {
        palletsRepository.deleteById(id);
    }

    public Pallets readPallet(Long id) {
        return palletsRepository.findById(id).orElseThrow(() -> new RuntimeException("Pallet not found"));
    }

    public List<Pallets> getAllPallets() {
        return palletsRepository.findAll();
    }
}