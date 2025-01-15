package com.danhcaonguyen.web.generic;

import com.danhcaonguyen.web.entity.Cv;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Optional;

@Component
public interface IService<T, ID> {
    void save(T t);

    void delete(ID id);

    Iterator<T> findAll();

    T findOne(ID id);

//    Optional<Cv> findById(Integer id);
}
