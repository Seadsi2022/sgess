package dsi.sea.sgess.service;

import dsi.sea.sgess.domain.Etypechamp;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Etypechamp}.
 */
public interface EtypechampService {
    /**
     * Save a etypechamp.
     *
     * @param etypechamp the entity to save.
     * @return the persisted entity.
     */
    Etypechamp save(Etypechamp etypechamp);

    /**
     * Updates a etypechamp.
     *
     * @param etypechamp the entity to update.
     * @return the persisted entity.
     */
    Etypechamp update(Etypechamp etypechamp);

    /**
     * Partially updates a etypechamp.
     *
     * @param etypechamp the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Etypechamp> partialUpdate(Etypechamp etypechamp);

    /**
     * Get all the etypechamps.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Etypechamp> findAll(Pageable pageable);

    /**
     * Get all the etypechamps with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Etypechamp> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" etypechamp.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Etypechamp> findOne(Long id);

    /**
     * Delete the "id" etypechamp.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the etypechamp corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Etypechamp> search(String query, Pageable pageable);
}
