package dsi.sea.sgess.repository;

import dsi.sea.sgess.domain.Evaleurvariable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Evaleurvariable entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EvaleurvariableRepository extends JpaRepository<Evaleurvariable, Long> {}
