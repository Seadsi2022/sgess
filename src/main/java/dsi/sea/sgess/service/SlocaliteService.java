package dsi.sea.sgess.service;

import dsi.sea.sgess.domain.Slocalite;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Slocalite}.
 */
public interface SlocaliteService {
    /**
     * Save a slocalite.
     *
     * @param slocalite the entity to save.
     * @return the persisted entity.
     */
    Slocalite save(Slocalite slocalite);

    /**
     * Updates a slocalite.
     *
     * @param slocalite the entity to update.
     * @return the persisted entity.
     */
    Slocalite update(Slocalite slocalite);

    /**
     * Partially updates a slocalite.
     *
     * @param slocalite the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Slocalite> partialUpdate(Slocalite slocalite);

    /**
     * Get all the slocalites.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Slocalite> findAll(Pageable pageable);

    /**
     * Get the "id" slocalite.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Slocalite> findOne(Long id);

    /**
     * Delete the "id" slocalite.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the slocalite corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Slocalite> search(String query, Pageable pageable);
}
