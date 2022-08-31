package dsi.sea.sgess.repository;

import dsi.sea.sgess.domain.Eeventualite;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Eeventualite entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EeventualiteRepository extends JpaRepository<Eeventualite, Long> {}
