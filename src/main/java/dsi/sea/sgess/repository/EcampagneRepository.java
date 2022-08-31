package dsi.sea.sgess.repository;

import dsi.sea.sgess.domain.Ecampagne;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Ecampagne entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EcampagneRepository extends JpaRepository<Ecampagne, Long> {}
