package dsi.sea.sgess.repository;

import dsi.sea.sgess.domain.Etypechamp;
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
public class EtypechampRepositoryWithBagRelationshipsImpl implements EtypechampRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Etypechamp> fetchBagRelationships(Optional<Etypechamp> etypechamp) {
        return etypechamp.map(this::fetchEattributs);
    }

    @Override
    public Page<Etypechamp> fetchBagRelationships(Page<Etypechamp> etypechamps) {
        return new PageImpl<>(fetchBagRelationships(etypechamps.getContent()), etypechamps.getPageable(), etypechamps.getTotalElements());
    }

    @Override
    public List<Etypechamp> fetchBagRelationships(List<Etypechamp> etypechamps) {
        return Optional.of(etypechamps).map(this::fetchEattributs).orElse(Collections.emptyList());
    }

    Etypechamp fetchEattributs(Etypechamp result) {
        return entityManager
            .createQuery(
                "select etypechamp from Etypechamp etypechamp left join fetch etypechamp.eattributs where etypechamp is :etypechamp",
                Etypechamp.class
            )
            .setParameter("etypechamp", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Etypechamp> fetchEattributs(List<Etypechamp> etypechamps) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, etypechamps.size()).forEach(index -> order.put(etypechamps.get(index).getId(), index));
        List<Etypechamp> result = entityManager
            .createQuery(
                "select distinct etypechamp from Etypechamp etypechamp left join fetch etypechamp.eattributs where etypechamp in :etypechamps",
                Etypechamp.class
            )
            .setParameter("etypechamps", etypechamps)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
