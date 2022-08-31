package dsi.sea.sgess.service;

import dsi.sea.sgess.domain.Eattributvariable;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Eattributvariable}.
 */
public interface EattributvariableService {
    /**
     * Save a eattributvariable.
     *
     * @param eattributvariable the entity to save.
     * @return the persisted entity.
     */
    Eattributvariable save(Eattributvariable eattributvariable);

    /**
     * Updates a eattributvariable.
     *
     * @param eattributvariable the entity to update.
     * @return the persisted entity.
     */
    Eattributvariable update(Eattributvariable eattributvariable);

    /**
     * Partially updates a eattributvariable.
     *
     * @param eattributvariable the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Eattributvariable> partialUpdate(Eattributvariable eattributvariable);

    /**
     * Get all the eattributvariables.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Eattributvariable> findAll(Pageable pageable);

    /**
     * Get the "id" eattributvariable.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Eattributvariable> findOne(Long id);

    /**
     * Delete the "id" eattributvariable.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the eattributvariable corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Eattributvariable> search(String query, Pageable pageable);
}
