package jpabook.jpashop.repository;

import java.util.List;

public interface IRepository<T, ID> {
    void save(T t);

    T findById(ID id);

    List<T> findAll();
}
