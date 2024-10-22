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