package dsi.sea.sgess.service;

import dsi.sea.sgess.domain.Eattribut;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Eattribut}.
 */
public interface EattributService {
    /**
     * Save a eattribut.
     *
     * @param eattribut the entity to save.
     * @return the persisted entity.
     */
    Eattribut save(Eattribut eattribut);

    /**
     * Updates a eattribut.
     *
     * @param eattribut the entity to update.
     * @return the persisted entity.
     */
    Eattribut update(Eattribut eattribut);

    /**
     * Partially updates a eattribut.
     *
     * @param eattribut the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Eattribut> partialUpdate(Eattribut eattribut);

    /**
     * Get all the eattributs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Eattribut> findAll(Pageable pageable);

    /**
     * Get the "id" eattribut.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Eattribut> findOne(Long id);

    /**
     * Delete the "id" eattribut.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the eattribut corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Eattribut> search(String query, Pageable pageable);
}
