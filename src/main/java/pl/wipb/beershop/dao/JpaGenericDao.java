package pl.wipb.beershop.dao;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import pl.wipb.beershop.dao.interfaces.GenericDao;
import pl.wipb.beershop.models.utils.BaseModel;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

public class JpaGenericDao<T extends BaseModel<ID>, ID> implements GenericDao<T, ID> {
    @PersistenceContext(name = "PU")
    protected EntityManager em;

    private final Class<T> type;

    public JpaGenericDao() {
        Type t = getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) t;
        type = (Class<T>) pt.getActualTypeArguments()[0];
    }

    @Override
    public T saveOrUpdate(T t) {
        if(t.getId() == null) em.persist(t);
        else t = em.merge(t);
        return t;
    }

    @Override
    public void delete(T t) {
        t = em.merge(t);
        em.remove(t);
    }

    @Override
    public Optional<T> findById(ID id) {
        T dto = em.find(type, id);
        return Optional.ofNullable(dto);
    }

    @Override
    public List<T> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> c = cb.createQuery(type);
        Root<T> rootEntry = c.from(type);
        c.select(rootEntry);
        return em.createQuery(c)
                .getResultList();
    }
}
