package dsi.sea.sgess.service;

import dsi.sea.sgess.domain.Egroupevariable;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Egroupevariable}.
 */
public interface EgroupevariableService {
    /**
     * Save a egroupevariable.
     *
     * @param egroupevariable the entity to save.
     * @return the persisted entity.
     */
    Egroupevariable save(Egroupevariable egroupevariable);

    /**
     * Updates a egroupevariable.
     *
     * @param egroupevariable the entity to update.
     * @return the persisted entity.
     */
    Egroupevariable update(Egroupevariable egroupevariable);

    /**
     * Partially updates a egroupevariable.
     *
     * @param egroupevariable the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Egroupevariable> partialUpdate(Egroupevariable egroupevariable);

    /**
     * Get all the egroupevariables.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Egroupevariable> findAll(Pageable pageable);

    /**
     * Get the "id" egroupevariable.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Egroupevariable> findOne(Long id);

    /**
     * Delete the "id" egroupevariable.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the egroupevariable corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Egroupevariable> search(String query, Pageable pageable);
}
