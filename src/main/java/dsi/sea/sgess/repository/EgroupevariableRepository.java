package dsi.sea.sgess.repository;

import dsi.sea.sgess.domain.Egroupevariable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Egroupevariable entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EgroupevariableRepository extends JpaRepository<Egroupevariable, Long> {}
