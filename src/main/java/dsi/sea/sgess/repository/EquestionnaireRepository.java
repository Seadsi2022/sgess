package dsi.sea.sgess.repository;

import dsi.sea.sgess.domain.Equestionnaire;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Equestionnaire entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EquestionnaireRepository extends JpaRepository<Equestionnaire, Long> {}
