package dsi.sea.sgess.repository;

import dsi.sea.sgess.domain.Etypevariable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Etypevariable entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EtypevariableRepository extends JpaRepository<Etypevariable, Long> {}
