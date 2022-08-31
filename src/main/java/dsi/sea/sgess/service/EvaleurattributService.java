package dsi.sea.sgess.service;

import dsi.sea.sgess.domain.Evaleurattribut;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Evaleurattribut}.
 */
public interface EvaleurattributService {
    /**
     * Save a evaleurattribut.
     *
     * @param evaleurattribut the entity to save.
     * @return the persisted entity.
     */
    Evaleurattribut save(Evaleurattribut evaleurattribut);

    /**
     * Updates a evaleurattribut.
     *
     * @param evaleurattribut the entity to update.
     * @return the persisted entity.
     */
    Evaleurattribut update(Evaleurattribut evaleurattribut);

    /**
     * Partially updates a evaleurattribut.
     *
     * @param evaleurattribut the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Evaleurattribut> partialUpdate(Evaleurattribut evaleurattribut);

    /**
     * Get all the evaleurattributs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Evaleurattribut> findAll(Pageable pageable);

    /**
     * Get the "id" evaleurattribut.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Evaleurattribut> findOne(Long id);

    /**
     * Delete the "id" evaleurattribut.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the evaleurattribut corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Evaleurattribut> search(String query, Pageable pageable);
}
