package dsi.sea.sgess.repository;

import dsi.sea.sgess.domain.Evaleurattribut;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Evaleurattribut entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EvaleurattributRepository extends JpaRepository<Evaleurattribut, Long> {}
