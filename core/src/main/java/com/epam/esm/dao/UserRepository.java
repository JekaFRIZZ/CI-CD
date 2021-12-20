package com.epam.esm.dao;

import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.exception.DaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private final EntityManager entityManager;

    @Autowired
    public UserRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Integer create(User user) {
        entityManager.persist(user);
        return user.getId();
    }

    public List<User> getAll(int limit, int offset) {
        CriteriaBuilder criteria = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteria.createQuery(User.class);
        criteriaQuery.from(User.class);
        List<User> users =
                entityManager.createQuery(criteriaQuery).
                        setFirstResult(offset).
                        setMaxResults(limit).
                        getResultList();
        return users;
    }

    public Optional<User> getById(Integer id) {
        User user = entityManager.find(User.class, id);
        return Optional.ofNullable(user);
    }

    public Optional<User> getByUsername(String username) {
        CriteriaBuilder criteria = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteria.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);

        criteriaQuery.where(criteria.equal(root.get("username"), username));
        List<User> result = entityManager.
                createQuery(criteriaQuery).
                getResultList();

        if (result.size() > 1) {
            throw new DaoException("More entities than expected");
        } else if (result.size() == 1) {
            return Optional.of(result.get(0));
        } else {
            return Optional.empty();
        }
    }

    public void deleteById(User user) {
        entityManager.remove(user);
    }
}
