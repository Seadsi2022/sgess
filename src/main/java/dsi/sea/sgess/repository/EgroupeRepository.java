package dsi.sea.sgess.repository;

import dsi.sea.sgess.domain.Egroupe;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Egroupe entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EgroupeRepository extends JpaRepository<Egroupe, Long> {}
