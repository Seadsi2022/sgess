package dsi.sea.sgess.repository;

import dsi.sea.sgess.domain.Scodevaleur;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Scodevaleur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ScodevaleurRepository extends JpaRepository<Scodevaleur, Long> {}
