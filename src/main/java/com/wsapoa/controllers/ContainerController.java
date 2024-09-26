//package com.wsapoa.controllers;
//
//import com.wsapoa.dto.ContainerRequestDTO;
//import com.wsapoa.entity.Container;
//import com.wsapoa.services.ContainerService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import javax.validation.Valid;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/containers")
//@RequiredArgsConstructor
//public class ContainerController {
//
//    private final ContainerService containerService;
//
//    @PostMapping
//    public ResponseEntity<Void> createContainer(@Valid @RequestBody ContainerRequestDTO containerRequestDTO) {
//        containerService.createContainer(containerRequestDTO);
//        return ResponseEntity.ok().build();
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Void> updateContainer(@PathVariable Long id, @Valid @RequestBody ContainerRequestDTO containerRequestDTO) {
//        containerService.updateContainer(id, containerRequestDTO);
//        return ResponseEntity.ok().build();
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteContainer(@PathVariable Long id) {
//        containerService.deleteContainer(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Container> readContainer(@PathVariable Long id) {
//        Container container = containerService.readContainer(id);
//        return ResponseEntity.ok(container);
//    }
//
//    @GetMapping
//    public ResponseEntity<List<Container>> getAllContainers() {
//        List<Container> containers = containerService.getAllContainers();
//        return ResponseEntity.ok(containers);
//    }
//}
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
