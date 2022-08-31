package dsi.sea.sgess.repository;

import dsi.sea.sgess.domain.Scode;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Scode entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ScodeRepository extends JpaRepository<Scode, Long> {}
