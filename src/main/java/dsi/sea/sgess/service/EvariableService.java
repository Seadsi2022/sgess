package dsi.sea.sgess.service;

import dsi.sea.sgess.domain.Evariable;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Evariable}.
 */
public interface EvariableService {
    /**
     * Save a evariable.
     *
     * @param evariable the entity to save.
     * @return the persisted entity.
     */
    Evariable save(Evariable evariable);

    /**
     * Updates a evariable.
     *
     * @param evariable the entity to update.
     * @return the persisted entity.
     */
    Evariable update(Evariable evariable);

    /**
     * Partially updates a evariable.
     *
     * @param evariable the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Evariable> partialUpdate(Evariable evariable);

    /**
     * Get all the evariables.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Evariable> findAll(Pageable pageable);

    /**
     * Get the "id" evariable.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Evariable> findOne(Long id);

    /**
     * Delete the "id" evariable.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the evariable corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Evariable> search(String query, Pageable pageable);
}
