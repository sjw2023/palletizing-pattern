package com.wsapoa.services;

import com.wsapoa.dto.ContainerRequestDTO;
import com.wsapoa.entity.Container;
import com.wsapoa.repository.ContainerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContainerService implements BaseService<Container, Long, ContainerRequestDTO> {

    @Override
    public void create(ContainerRequestDTO containerRequestDTO) {
        Container container = new Container(containerRequestDTO);
        containerRepository.save(container);
    }

    @Override
    public void update(Long id, ContainerRequestDTO containerRequestDTO) {
        Container container = containerRepository.findById(id).orElseThrow(() -> new RuntimeException("Container not found"));
        container.setName(containerRequestDTO.getName());
        container.setWidth(containerRequestDTO.getWidth());
        container.setHeight(containerRequestDTO.getHeight());
        container.setLength(containerRequestDTO.getLength());
        container.setUsed(containerRequestDTO.isUsed());
        container.setContainerVolume(containerRequestDTO.getContainerVolume());
        containerRepository.save(container);
    }

    @Override
    public void delete(Long id) {
        containerRepository.deleteById(id);
    }

    @Override
    public Container read(Long id) {
        return containerRepository.findById(id).orElseThrow(() -> new RuntimeException("Container not found"));
    }

    @Override
    public List<Container> getAll() {
        return containerRepository.findAll();
    }

    private final ContainerRepository containerRepository;

    public void createContainer(ContainerRequestDTO containerRequestDTO) {
        Container container = new Container(containerRequestDTO);
        containerRepository.save(container);
    }

    public void updateContainer(Long id, ContainerRequestDTO containerRequestDTO) {
        Container container = containerRepository.findById(id).orElseThrow(() -> new RuntimeException("Container not found"));
        container.setName(containerRequestDTO.getName());
        container.setWidth(containerRequestDTO.getWidth());
        container.setHeight(containerRequestDTO.getHeight());
        container.setLength(containerRequestDTO.getLength());
        container.setUsed(containerRequestDTO.isUsed());
        container.setContainerVolume(containerRequestDTO.getContainerVolume());
        containerRepository.save(container);
    }

    public void deleteContainer(Long id) {
        containerRepository.deleteById(id);
    }

    public Container readContainer(Long id) {
        return containerRepository.findById(id).orElseThrow(() -> new RuntimeException("Container not found"));
    }

    public List<Container> getAllContainers() {
        return containerRepository.findAll();
    }
}