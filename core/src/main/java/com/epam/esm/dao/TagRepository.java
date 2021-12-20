package com.epam.esm.dao;

import com.epam.esm.entity.Tag;
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
public class TagRepository {
    private static final String GET_MOST_WIDELY_USED_TAG_WITH_HIGHEST_COST =
            "select * from tag where id = (select t.id from tag t" +
                    " join gifts_and_tags gt on t.id=gt.tag_id" +
                    " join user_order uo on gt.certificate_id = uo.certificate_id" +
                    " order by uo.price DESC LIMIT 1)";


    private final EntityManager entityManager;

    @Autowired
    public TagRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Tag> getAll(int limit, int offset) {
        CriteriaBuilder criteria = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteria.createQuery(Tag.class);
        criteriaQuery.from(Tag.class);
        List<Tag> tags =
                entityManager.createQuery(criteriaQuery).
                        setFirstResult(offset).
                        setMaxResults(limit).
                        getResultList();
        return tags;
    }

    public Optional<Tag> getById(Integer id) {
        Tag tag = entityManager.find(Tag.class, id);
        return Optional.ofNullable(tag);
    }

    public Optional<Tag> getByName(String name) {
        CriteriaBuilder criteria = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteria.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);

        criteriaQuery.where(criteria.equal(root.get("name"), name));
        List<Tag> result = entityManager.
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

    public Integer create(Tag tag) {
        entityManager.persist(tag);
        return tag.getId();
    }

    public void deleteById(Tag tag) {
        entityManager.remove(tag);
    }

    public Tag getMostWidelyUsedTagWithHighestCost() {
        return (Tag) entityManager.
                createNativeQuery(GET_MOST_WIDELY_USED_TAG_WITH_HIGHEST_COST, Tag.class).
                getSingleResult();
    }
}
