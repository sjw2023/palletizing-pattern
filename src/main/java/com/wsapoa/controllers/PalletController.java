//package com.wsapoa.controllers;
//
//import com.wsapoa.dto.PalletRequestDTO;
//import com.wsapoa.entity.Pallet;
//import com.wsapoa.services.PalletService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import javax.validation.Valid;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/pallets")
//@RequiredArgsConstructor
//public class PalletController {
//
//    private final PalletService palletService;
//
//    @PostMapping
//    public ResponseEntity<Void> createPallet(@Valid @RequestBody PalletRequestDTO palletRequestDTO) {
//        palletService.createPallet(palletRequestDTO);
//        return ResponseEntity.ok().build();
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Void> updatePallet(@PathVariable Long id, @Valid @RequestBody PalletRequestDTO palletRequestDTO) {
//        palletService.updatePallet(id, palletRequestDTO);
//        return ResponseEntity.ok().build();
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deletePallet(@PathVariable Long id) {
//        palletService.deletePallet(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Pallet> readPallet(@PathVariable Long id) {
//        Pallet pallet = palletService.readPallet(id);
//        return ResponseEntity.ok(pallet);
//    }
//
//    @GetMapping
//    public ResponseEntity<List<Pallet>> getAllPallets() {
//        List<Pallet> pallets = palletService.getAllPallets();
//        return ResponseEntity.ok(pallets);
//    }
//}
package com.wsapoa.controllers;

import com.wsapoa.dto.PalletRequestDTO;
import com.wsapoa.entity.Pallet;
import com.wsapoa.services.BaseService;
import com.wsapoa.services.PalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pallets")
@RequiredArgsConstructor
public class PalletController extends BaseController<Pallet, Long, PalletRequestDTO> {

    private final PalletService palletService;

    @Override
    protected BaseService<Pallet, Long, PalletRequestDTO> getService() {
        return palletService;
    }
}