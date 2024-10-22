package com.wsapoa.controllers;

import com.wsapoa.services.BaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
//TODO : Have end-points to return ReqDTO as its response
public abstract class BaseController<ResDTO, ID, ReqDTO> {

    protected abstract BaseService<ResDTO, ID, ReqDTO> getService();

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody ReqDTO reqDto) {
        getService().create(reqDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable ID id, @Valid @RequestBody ReqDTO reqDto) {
        getService().update(id, reqDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable ID id) {
        getService().delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResDTO> read(@PathVariable ID id) {
        ResDTO entity = getService().read(id);
        return ResponseEntity.ok(entity);
    }

    @GetMapping
    public ResponseEntity<List<ResDTO>> getAll() {
        List<ResDTO> entities = getService().getAll();
        return ResponseEntity.ok(entities);
    }
}