package jpabook.jpashop.repository.interfaces;

import java.util.List;

public interface IRepository<T, IDType> {
    void save(T t);

    T findById(IDType id);

    List<T> findAll();
}
