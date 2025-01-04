package com.whizservices.hris.services;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface BaseService<T> {
    @Transactional
    void saveOrUpdate(T object);

    @Transactional
    T getById(UUID id);

    @Transactional
    void delete(T object);

    @Transactional
    List<T> getAll(int page, int pageSize);

    @Transactional
    List<T> findByParameter(String param);
}
