package dsi.sea.sgess.repository;

import dsi.sea.sgess.domain.Eattribut;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Eattribut entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EattributRepository extends JpaRepository<Eattribut, Long> {}
