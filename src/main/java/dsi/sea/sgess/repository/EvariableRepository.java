package dsi.sea.sgess.repository;

import dsi.sea.sgess.domain.Evariable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Evariable entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EvariableRepository extends JpaRepository<Evariable, Long> {}
