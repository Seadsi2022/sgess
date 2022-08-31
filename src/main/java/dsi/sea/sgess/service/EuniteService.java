package dsi.sea.sgess.service;

import dsi.sea.sgess.domain.Eunite;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Eunite}.
 */
public interface EuniteService {
    /**
     * Save a eunite.
     *
     * @param eunite the entity to save.
     * @return the persisted entity.
     */
    Eunite save(Eunite eunite);

    /**
     * Updates a eunite.
     *
     * @param eunite the entity to update.
     * @return the persisted entity.
     */
    Eunite update(Eunite eunite);

    /**
     * Partially updates a eunite.
     *
     * @param eunite the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Eunite> partialUpdate(Eunite eunite);

    /**
     * Get all the eunites.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Eunite> findAll(Pageable pageable);

    /**
     * Get the "id" eunite.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Eunite> findOne(Long id);

    /**
     * Delete the "id" eunite.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the eunite corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Eunite> search(String query, Pageable pageable);
}
