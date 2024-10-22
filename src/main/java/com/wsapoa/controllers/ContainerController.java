package com.wsapoa.controllers;

import com.wsapoa.dto.ContainerRequestDTO;
import com.wsapoa.entity.Container;
import com.wsapoa.services.BaseService;
import com.wsapoa.services.ContainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/containers")
@RequiredArgsConstructor
public class ContainerController extends BaseController<Container, Long, ContainerRequestDTO> {

    private final ContainerService containerService;

    @Override
    protected BaseService<Container, Long, ContainerRequestDTO> getService() {
        return containerService;
    }
}
