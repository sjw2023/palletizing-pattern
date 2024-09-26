package com.wsapoa.services;

import java.util.List;

public interface BaseService<T, ID, DTO> {
    void create(DTO dto);
    void update(ID id, DTO dto);
    void delete(ID id);
    T read(ID id);
    List<T> getAll();
}