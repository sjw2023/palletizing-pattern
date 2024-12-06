package com.wsapoa.services;

import java.util.List;

public interface BaseService<ResDTO, ID, ReqDTO> {
    void create(ReqDTO reqDto);
    void update(ID id, ReqDTO reqDto);
    void delete(ID id);
    ResDTO read(ID id);
    List<ResDTO> getAll();
}