package dsi.sea.sgess.service;

import dsi.sea.sgess.domain.Eeventualitevariable;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Eeventualitevariable}.
 */
public interface EeventualitevariableService {
    /**
     * Save a eeventualitevariable.
     *
     * @param eeventualitevariable the entity to save.
     * @return the persisted entity.
     */
    Eeventualitevariable save(Eeventualitevariable eeventualitevariable);

    /**
     * Updates a eeventualitevariable.
     *
     * @param eeventualitevariable the entity to update.
     * @return the persisted entity.
     */
    Eeventualitevariable update(Eeventualitevariable eeventualitevariable);

    /**
     * Partially updates a eeventualitevariable.
     *
     * @param eeventualitevariable the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Eeventualitevariable> partialUpdate(Eeventualitevariable eeventualitevariable);

    /**
     * Get all the eeventualitevariables.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Eeventualitevariable> findAll(Pageable pageable);

    /**
     * Get the "id" eeventualitevariable.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Eeventualitevariable> findOne(Long id);

    /**
     * Delete the "id" eeventualitevariable.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the eeventualitevariable corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Eeventualitevariable> search(String query, Pageable pageable);
}
