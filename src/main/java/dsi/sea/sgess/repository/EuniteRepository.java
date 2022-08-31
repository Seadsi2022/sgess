package dsi.sea.sgess.repository;

import dsi.sea.sgess.domain.Eunite;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Eunite entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EuniteRepository extends JpaRepository<Eunite, Long> {}
