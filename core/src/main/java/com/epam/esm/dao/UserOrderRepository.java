package com.epam.esm.dao;

import com.epam.esm.entity.User;
import com.epam.esm.entity.UserOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
public class UserOrderRepository {

    private final EntityManager entityManager;

    @Autowired
    public UserOrderRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Integer create(UserOrder userOrder) {
        entityManager.persist(userOrder);
        return userOrder.getId();
    }

    public Optional<UserOrder> getById(Integer id) {
        UserOrder result = entityManager.find(UserOrder.class, id);
        return Optional.ofNullable(result);
    }

    public List<UserOrder> getAll(int limit, int offset) {
        CriteriaBuilder criteria = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserOrder> criteriaQuery = criteria.createQuery(UserOrder.class);
        criteriaQuery.from(UserOrder.class);
        List<UserOrder> userOrders =
                entityManager.createQuery(criteriaQuery).
                        setFirstResult(offset).
                        setMaxResults(limit).
                        getResultList();
        return userOrders;
    }

    public List<UserOrder> getAllByUserId(Integer userId, int limit, int offset) {
        System.out.println(userId);
        CriteriaBuilder criteria = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserOrder> criteriaQuery = criteria.createQuery(UserOrder.class);
        Root<UserOrder> root = criteriaQuery.from(UserOrder.class);
        criteriaQuery.select(root).where(criteria.equal(root.get("userId"), userId));
        List<UserOrder> userOrders =
                entityManager.createQuery(criteriaQuery).
                        setFirstResult(offset).
                        setMaxResults(limit).
                        getResultList();
        return userOrders;
    }

    public void deleteById(UserOrder userOrder) {
        entityManager.remove(userOrder);
    }
}
