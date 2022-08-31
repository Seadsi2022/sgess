package dsi.sea.sgess.service;

import dsi.sea.sgess.domain.Egroupe;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Egroupe}.
 */
public interface EgroupeService {
    /**
     * Save a egroupe.
     *
     * @param egroupe the entity to save.
     * @return the persisted entity.
     */
    Egroupe save(Egroupe egroupe);

    /**
     * Updates a egroupe.
     *
     * @param egroupe the entity to update.
     * @return the persisted entity.
     */
    Egroupe update(Egroupe egroupe);

    /**
     * Partially updates a egroupe.
     *
     * @param egroupe the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Egroupe> partialUpdate(Egroupe egroupe);

    /**
     * Get all the egroupes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Egroupe> findAll(Pageable pageable);

    /**
     * Get the "id" egroupe.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Egroupe> findOne(Long id);

    /**
     * Delete the "id" egroupe.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the egroupe corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Egroupe> search(String query, Pageable pageable);
}
