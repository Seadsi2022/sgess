package dsi.sea.sgess.repository;

import dsi.sea.sgess.domain.Eeventualitevariable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Eeventualitevariable entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EeventualitevariableRepository extends JpaRepository<Eeventualitevariable, Long> {}
