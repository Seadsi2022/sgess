package dsi.sea.sgess.service;

import dsi.sea.sgess.domain.Ecampagne;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Ecampagne}.
 */
public interface EcampagneService {
    /**
     * Save a ecampagne.
     *
     * @param ecampagne the entity to save.
     * @return the persisted entity.
     */
    Ecampagne save(Ecampagne ecampagne);

    /**
     * Updates a ecampagne.
     *
     * @param ecampagne the entity to update.
     * @return the persisted entity.
     */
    Ecampagne update(Ecampagne ecampagne);

    /**
     * Partially updates a ecampagne.
     *
     * @param ecampagne the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Ecampagne> partialUpdate(Ecampagne ecampagne);

    /**
     * Get all the ecampagnes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Ecampagne> findAll(Pageable pageable);

    /**
     * Get the "id" ecampagne.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Ecampagne> findOne(Long id);

    /**
     * Delete the "id" ecampagne.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the ecampagne corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Ecampagne> search(String query, Pageable pageable);
}
