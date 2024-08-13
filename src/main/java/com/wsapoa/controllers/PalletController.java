package com.wsapoa.controllers;

import com.wsapoa.dto.PalletRequestDTO;
import com.wsapoa.entity.Pallets;
import com.wsapoa.services.PalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/pallets")
@RequiredArgsConstructor
public class PalletController {

    private final PalletService palletService;

    @PostMapping
    public ResponseEntity<Void> createPallet(@Valid @RequestBody PalletRequestDTO palletRequestDTO) {
        palletService.createPallet(palletRequestDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePallet(@PathVariable Long id, @Valid @RequestBody PalletRequestDTO palletRequestDTO) {
        palletService.updatePallet(id, palletRequestDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePallet(@PathVariable Long id) {
        palletService.deletePallet(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pallets> readPallet(@PathVariable Long id) {
        Pallets pallet = palletService.readPallet(id);
        return ResponseEntity.ok(pallet);
    }

    @GetMapping
    public ResponseEntity<List<Pallets>> getAllPallets() {
        List<Pallets> pallets = palletService.getAllPallets();
        return ResponseEntity.ok(pallets);
    }
}