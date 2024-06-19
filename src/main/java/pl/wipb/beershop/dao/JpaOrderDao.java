package pl.wipb.beershop.dao;

import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import pl.wipb.beershop.dao.interfaces.OrderDao;
import pl.wipb.beershop.models.Account;
import pl.wipb.beershop.models.Order;

import java.util.List;
import java.util.Optional;

@Stateless
public class JpaOrderDao extends JpaGenericDao<Order, Long> implements OrderDao {
    public List<Order> findByAccount(Account account) {
        return em.createNamedQuery("Order.findByAccount", Order.class)
                .setParameter("accountId", account.getId())
                .getResultList();
    }
}
