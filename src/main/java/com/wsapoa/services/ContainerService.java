package com.wsapoa.services;

import com.wsapoa.dto.ContainerRequestDTO;
import com.wsapoa.entity.Containers;
import com.wsapoa.repository.ContainersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContainerService {

    private final ContainersRepository containersRepository;

    public void createContainer(ContainerRequestDTO containerRequestDTO) {
        Containers container = new Containers(containerRequestDTO);
        containersRepository.save(container);
    }

    public void updateContainer(Long id, ContainerRequestDTO containerRequestDTO) {
        Containers container = containersRepository.findById(id).orElseThrow(() -> new RuntimeException("Container not found"));
        container.setName(containerRequestDTO.getName());
        container.setWidth(containerRequestDTO.getWidth());
        container.setHeight(containerRequestDTO.getHeight());
        container.setLength(containerRequestDTO.getLength());
        container.setUsed(containerRequestDTO.isUsed());
        container.setContainerVolume(containerRequestDTO.getContainerVolume());
        containersRepository.save(container);
    }

    public void deleteContainer(Long id) {
        containersRepository.deleteById(id);
    }

    public Containers readContainer(Long id) {
        return containersRepository.findById(id).orElseThrow(() -> new RuntimeException("Container not found"));
    }

    public List<Containers> getAllContainers() {
        return containersRepository.findAll();
    }
}