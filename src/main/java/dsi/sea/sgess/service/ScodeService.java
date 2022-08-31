package dsi.sea.sgess.service;

import dsi.sea.sgess.domain.Scode;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Scode}.
 */
public interface ScodeService {
    /**
     * Save a scode.
     *
     * @param scode the entity to save.
     * @return the persisted entity.
     */
    Scode save(Scode scode);

    /**
     * Updates a scode.
     *
     * @param scode the entity to update.
     * @return the persisted entity.
     */
    Scode update(Scode scode);

    /**
     * Partially updates a scode.
     *
     * @param scode the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Scode> partialUpdate(Scode scode);

    /**
     * Get all the scodes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Scode> findAll(Pageable pageable);

    /**
     * Get the "id" scode.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Scode> findOne(Long id);

    /**
     * Delete the "id" scode.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the scode corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Scode> search(String query, Pageable pageable);
}
