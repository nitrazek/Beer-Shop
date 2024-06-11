package pl.wipb.beershop.dao.interfaces;

import pl.wipb.beershop.models.utils.BaseModel;

import java.util.List;
import java.util.Optional;

public interface GenericDao<T extends BaseModel<ID>, ID> {
    T saveOrUpdate(T t);
    void delete (T t);
    Optional<T> findById(ID id);
    List<T> findAll();
}
