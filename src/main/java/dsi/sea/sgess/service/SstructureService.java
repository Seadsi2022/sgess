package dsi.sea.sgess.service;

import dsi.sea.sgess.domain.Sstructure;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Sstructure}.
 */
public interface SstructureService {
    /**
     * Save a sstructure.
     *
     * @param sstructure the entity to save.
     * @return the persisted entity.
     */
    Sstructure save(Sstructure sstructure);

    /**
     * Updates a sstructure.
     *
     * @param sstructure the entity to update.
     * @return the persisted entity.
     */
    Sstructure update(Sstructure sstructure);

    /**
     * Partially updates a sstructure.
     *
     * @param sstructure the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Sstructure> partialUpdate(Sstructure sstructure);

    /**
     * Get all the sstructures.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Sstructure> findAll(Pageable pageable);

    /**
     * Get all the sstructures with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Sstructure> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" sstructure.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Sstructure> findOne(Long id);

    /**
     * Delete the "id" sstructure.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the sstructure corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Sstructure> search(String query, Pageable pageable);
}
