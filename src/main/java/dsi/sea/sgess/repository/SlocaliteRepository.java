package dsi.sea.sgess.repository;

import dsi.sea.sgess.domain.Slocalite;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Slocalite entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SlocaliteRepository extends JpaRepository<Slocalite, Long> {}
