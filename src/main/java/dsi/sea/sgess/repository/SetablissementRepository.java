package dsi.sea.sgess.repository;

import dsi.sea.sgess.domain.Setablissement;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Setablissement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SetablissementRepository extends JpaRepository<Setablissement, Long> {}
