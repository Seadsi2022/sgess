package dsi.sea.sgess.service;

import dsi.sea.sgess.domain.Evaleurvariable;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Evaleurvariable}.
 */
public interface EvaleurvariableService {
    /**
     * Save a evaleurvariable.
     *
     * @param evaleurvariable the entity to save.
     * @return the persisted entity.
     */
    Evaleurvariable save(Evaleurvariable evaleurvariable);

    /**
     * Updates a evaleurvariable.
     *
     * @param evaleurvariable the entity to update.
     * @return the persisted entity.
     */
    Evaleurvariable update(Evaleurvariable evaleurvariable);

    /**
     * Partially updates a evaleurvariable.
     *
     * @param evaleurvariable the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Evaleurvariable> partialUpdate(Evaleurvariable evaleurvariable);

    /**
     * Get all the evaleurvariables.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Evaleurvariable> findAll(Pageable pageable);

    /**
     * Get the "id" evaleurvariable.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Evaleurvariable> findOne(Long id);

    /**
     * Delete the "id" evaleurvariable.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the evaleurvariable corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Evaleurvariable> search(String query, Pageable pageable);
}
