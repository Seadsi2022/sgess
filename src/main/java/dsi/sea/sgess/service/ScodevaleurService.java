package dsi.sea.sgess.service;

import dsi.sea.sgess.domain.Scodevaleur;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Scodevaleur}.
 */
public interface ScodevaleurService {
    /**
     * Save a scodevaleur.
     *
     * @param scodevaleur the entity to save.
     * @return the persisted entity.
     */
    Scodevaleur save(Scodevaleur scodevaleur);

    /**
     * Updates a scodevaleur.
     *
     * @param scodevaleur the entity to update.
     * @return the persisted entity.
     */
    Scodevaleur update(Scodevaleur scodevaleur);

    /**
     * Partially updates a scodevaleur.
     *
     * @param scodevaleur the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Scodevaleur> partialUpdate(Scodevaleur scodevaleur);

    /**
     * Get all the scodevaleurs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Scodevaleur> findAll(Pageable pageable);

    /**
     * Get the "id" scodevaleur.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Scodevaleur> findOne(Long id);

    /**
     * Delete the "id" scodevaleur.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the scodevaleur corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Scodevaleur> search(String query, Pageable pageable);
}
