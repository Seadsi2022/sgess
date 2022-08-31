package dsi.sea.sgess.service;

import dsi.sea.sgess.domain.Equestionnaire;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Equestionnaire}.
 */
public interface EquestionnaireService {
    /**
     * Save a equestionnaire.
     *
     * @param equestionnaire the entity to save.
     * @return the persisted entity.
     */
    Equestionnaire save(Equestionnaire equestionnaire);

    /**
     * Updates a equestionnaire.
     *
     * @param equestionnaire the entity to update.
     * @return the persisted entity.
     */
    Equestionnaire update(Equestionnaire equestionnaire);

    /**
     * Partially updates a equestionnaire.
     *
     * @param equestionnaire the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Equestionnaire> partialUpdate(Equestionnaire equestionnaire);

    /**
     * Get all the equestionnaires.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Equestionnaire> findAll(Pageable pageable);

    /**
     * Get the "id" equestionnaire.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Equestionnaire> findOne(Long id);

    /**
     * Delete the "id" equestionnaire.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the equestionnaire corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Equestionnaire> search(String query, Pageable pageable);
}
