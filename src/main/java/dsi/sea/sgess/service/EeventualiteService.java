package dsi.sea.sgess.service;

import dsi.sea.sgess.domain.Eeventualite;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Eeventualite}.
 */
public interface EeventualiteService {
    /**
     * Save a eeventualite.
     *
     * @param eeventualite the entity to save.
     * @return the persisted entity.
     */
    Eeventualite save(Eeventualite eeventualite);

    /**
     * Updates a eeventualite.
     *
     * @param eeventualite the entity to update.
     * @return the persisted entity.
     */
    Eeventualite update(Eeventualite eeventualite);

    /**
     * Partially updates a eeventualite.
     *
     * @param eeventualite the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Eeventualite> partialUpdate(Eeventualite eeventualite);

    /**
     * Get all the eeventualites.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Eeventualite> findAll(Pageable pageable);

    /**
     * Get the "id" eeventualite.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Eeventualite> findOne(Long id);

    /**
     * Delete the "id" eeventualite.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the eeventualite corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Eeventualite> search(String query, Pageable pageable);
}
