package dsi.sea.sgess.service;

import dsi.sea.sgess.domain.Etypevariable;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Etypevariable}.
 */
public interface EtypevariableService {
    /**
     * Save a etypevariable.
     *
     * @param etypevariable the entity to save.
     * @return the persisted entity.
     */
    Etypevariable save(Etypevariable etypevariable);

    /**
     * Updates a etypevariable.
     *
     * @param etypevariable the entity to update.
     * @return the persisted entity.
     */
    Etypevariable update(Etypevariable etypevariable);

    /**
     * Partially updates a etypevariable.
     *
     * @param etypevariable the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Etypevariable> partialUpdate(Etypevariable etypevariable);

    /**
     * Get all the etypevariables.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Etypevariable> findAll(Pageable pageable);

    /**
     * Get the "id" etypevariable.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Etypevariable> findOne(Long id);

    /**
     * Delete the "id" etypevariable.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the etypevariable corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Etypevariable> search(String query, Pageable pageable);
}
