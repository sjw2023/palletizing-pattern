package com.wsapoa.services;

import com.wsapoa.dto.PalletRequestDTO;
import com.wsapoa.entity.Pallet;
import com.wsapoa.repository.PalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PalletService implements BaseService<Pallet, Long, PalletRequestDTO> {
    @Override
    public void create(PalletRequestDTO palletRequestDTO) {
        Pallet pallet = new Pallet( palletRequestDTO );
        palletRepository.save(pallet);
    }

    @Override
    public void update(Long id, PalletRequestDTO palletRequestDTO) {
        Pallet pallet = palletRepository.findById(id).orElseThrow(() -> new RuntimeException("Pallet not found"));
        pallet.setName(palletRequestDTO.getName());
        pallet.setWidth(palletRequestDTO.getWidth());
        pallet.setHeight(palletRequestDTO.getHeight());
        pallet.setLength(palletRequestDTO.getLength());
        pallet.setUsed(palletRequestDTO.isUsed());
        palletRepository.save(pallet);
    }

    @Override
    public void delete(Long id) {
        palletRepository.deleteById(id);
    }

    @Override
    public Pallet read(Long id) {
        return palletRepository.findById(id).orElseThrow(() -> new RuntimeException("Pallet not found"));
    }

    @Override
    public List<Pallet> getAll() {
        return palletRepository.findAll();
    }

    private final PalletRepository palletRepository;

    public void createPallet(PalletRequestDTO palletRequestDTO) {
        Pallet pallet = new Pallet( palletRequestDTO );
        palletRepository.save(pallet);
    }

    public void updatePallet(Long id, PalletRequestDTO palletRequestDTO) {
        Pallet pallet = palletRepository.findById(id).orElseThrow(() -> new RuntimeException("Pallet not found"));
        pallet.setName(palletRequestDTO.getName());
        pallet.setWidth(palletRequestDTO.getWidth());
        pallet.setHeight(palletRequestDTO.getHeight());
        pallet.setLength(palletRequestDTO.getLength());
        pallet.setUsed(palletRequestDTO.isUsed());
        palletRepository.save(pallet);
    }

    public void deletePallet(Long id) {
        palletRepository.deleteById(id);
    }

    public Pallet readPallet(Long id) {
        return palletRepository.findById(id).orElseThrow(() -> new RuntimeException("Pallet not found"));
    }

    public List<Pallet> getAllPallets() {
        return palletRepository.findAll();
    }
}