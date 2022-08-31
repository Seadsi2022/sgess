package dsi.sea.sgess.repository;

import dsi.sea.sgess.domain.Sstructure;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class SstructureRepositoryWithBagRelationshipsImpl implements SstructureRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Sstructure> fetchBagRelationships(Optional<Sstructure> sstructure) {
        return sstructure.map(this::fetchScodes);
    }

    @Override
    public Page<Sstructure> fetchBagRelationships(Page<Sstructure> sstructures) {
        return new PageImpl<>(fetchBagRelationships(sstructures.getContent()), sstructures.getPageable(), sstructures.getTotalElements());
    }

    @Override
    public List<Sstructure> fetchBagRelationships(List<Sstructure> sstructures) {
        return Optional.of(sstructures).map(this::fetchScodes).orElse(Collections.emptyList());
    }

    Sstructure fetchScodes(Sstructure result) {
        return entityManager
            .createQuery(
                "select sstructure from Sstructure sstructure left join fetch sstructure.scodes where sstructure is :sstructure",
                Sstructure.class
            )
            .setParameter("sstructure", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Sstructure> fetchScodes(List<Sstructure> sstructures) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, sstructures.size()).forEach(index -> order.put(sstructures.get(index).getId(), index));
        List<Sstructure> result = entityManager
            .createQuery(
                "select distinct sstructure from Sstructure sstructure left join fetch sstructure.scodes where sstructure in :sstructures",
                Sstructure.class
            )
            .setParameter("sstructures", sstructures)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
