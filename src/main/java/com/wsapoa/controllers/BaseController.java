package com.wsapoa.controllers;

import com.wsapoa.services.BaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
//TODO : Have end-points to return DTO as its response
public abstract class BaseController<T, ID, DTO> {

    protected abstract BaseService<T, ID, DTO> getService();

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody DTO dto) {
        getService().create(dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable ID id, @Valid @RequestBody DTO dto) {
        getService().update(id, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable ID id) {
        getService().delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<T> read(@PathVariable ID id) {
        T entity = getService().read(id);
        return ResponseEntity.ok(entity);
    }

    @GetMapping
    public ResponseEntity<List<T>> getAll() {
        List<T> entities = getService().getAll();
        return ResponseEntity.ok(entities);
    }
}