package dsi.sea.sgess.repository;

import dsi.sea.sgess.domain.Eattributvariable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Eattributvariable entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EattributvariableRepository extends JpaRepository<Eattributvariable, Long> {}
