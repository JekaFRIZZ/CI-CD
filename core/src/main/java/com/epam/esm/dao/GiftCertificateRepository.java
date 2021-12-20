package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.OrderSort;
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
public class GiftCertificateRepository {

    private final EntityManager entityManager;

    @Autowired
    public GiftCertificateRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<GiftCertificate> getAll(int limit, int offset) {
        CriteriaBuilder criteria = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteria.createQuery(GiftCertificate.class);
        criteriaQuery.from(GiftCertificate.class);

        List<GiftCertificate> giftCertificates =
                entityManager.createQuery(criteriaQuery).
                        setFirstResult(offset).
                        setMaxResults(limit).
                        getResultList();
        return giftCertificates;
    }

    public Optional<GiftCertificate> getById(Integer id) {
        GiftCertificate giftCertificate = entityManager.find(GiftCertificate.class, id);
        return Optional.ofNullable(giftCertificate);
    }

    public Optional<GiftCertificate> getByName(String name) {
        CriteriaBuilder criteria = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteria.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = criteriaQuery.from(GiftCertificate.class);

        criteriaQuery.where(criteria.equal(root.get("name"), name));
        List<GiftCertificate> result = entityManager.
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

    public Integer create(GiftCertificate giftCertificate) {
        entityManager.persist(giftCertificate);
        return giftCertificate.getId();
    }

    public void update(GiftCertificate giftCertificate) {
        entityManager.merge(giftCertificate);
    }

    public void deleteById(GiftCertificate giftCertificate) {
        entityManager.remove(giftCertificate);
    }

    public List<GiftCertificate> sortByOrder(String fieldName, OrderSort isASC, int limit, int offset) {
        CriteriaBuilder criteria = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteria.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = criteriaQuery.from(GiftCertificate.class);
        if(isASC.isValue()) {
            criteriaQuery.orderBy(criteria.asc(root.get(fieldName)));
        }
        else {
            criteriaQuery.orderBy(criteria.desc(root.get(fieldName)));
        }

        return entityManager.createQuery(criteriaQuery).
                        setFirstResult(offset).
                        setMaxResults(limit).
                        getResultList();
    }
}

