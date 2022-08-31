package dsi.sea.sgess.service;

import dsi.sea.sgess.domain.Setablissement;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Setablissement}.
 */
public interface SetablissementService {
    /**
     * Save a setablissement.
     *
     * @param setablissement the entity to save.
     * @return the persisted entity.
     */
    Setablissement save(Setablissement setablissement);

    /**
     * Updates a setablissement.
     *
     * @param setablissement the entity to update.
     * @return the persisted entity.
     */
    Setablissement update(Setablissement setablissement);

    /**
     * Partially updates a setablissement.
     *
     * @param setablissement the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Setablissement> partialUpdate(Setablissement setablissement);

    /**
     * Get all the setablissements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Setablissement> findAll(Pageable pageable);

    /**
     * Get the "id" setablissement.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Setablissement> findOne(Long id);

    /**
     * Delete the "id" setablissement.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the setablissement corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Setablissement> search(String query, Pageable pageable);
}
