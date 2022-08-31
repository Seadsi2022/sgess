package dsi.sea.sgess.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import dsi.sea.sgess.domain.Scodevaleur;
import dsi.sea.sgess.repository.ScodevaleurRepository;
import dsi.sea.sgess.repository.search.ScodevaleurSearchRepository;
import dsi.sea.sgess.service.ScodevaleurService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Scodevaleur}.
 */
@Service
@Transactional
public class ScodevaleurServiceImpl implements ScodevaleurService {

    private final Logger log = LoggerFactory.getLogger(ScodevaleurServiceImpl.class);

    private final ScodevaleurRepository scodevaleurRepository;

    private final ScodevaleurSearchRepository scodevaleurSearchRepository;

    public ScodevaleurServiceImpl(ScodevaleurRepository scodevaleurRepository, ScodevaleurSearchRepository scodevaleurSearchRepository) {
        this.scodevaleurRepository = scodevaleurRepository;
        this.scodevaleurSearchRepository = scodevaleurSearchRepository;
    }

    @Override
    public Scodevaleur save(Scodevaleur scodevaleur) {
        log.debug("Request to save Scodevaleur : {}", scodevaleur);
        Scodevaleur result = scodevaleurRepository.save(scodevaleur);
        scodevaleurSearchRepository.index(result);
        return result;
    }

    @Override
    public Scodevaleur update(Scodevaleur scodevaleur) {
        log.debug("Request to save Scodevaleur : {}", scodevaleur);
        Scodevaleur result = scodevaleurRepository.save(scodevaleur);
        scodevaleurSearchRepository.index(result);
        return result;
    }

    @Override
    public Optional<Scodevaleur> partialUpdate(Scodevaleur scodevaleur) {
        log.debug("Request to partially update Scodevaleur : {}", scodevaleur);

        return scodevaleurRepository
            .findById(scodevaleur.getId())
            .map(existingScodevaleur -> {
                if (scodevaleur.getCodevaleurLib() != null) {
                    existingScodevaleur.setCodevaleurLib(scodevaleur.getCodevaleurLib());
                }

                return existingScodevaleur;
            })
            .map(scodevaleurRepository::save)
            .map(savedScodevaleur -> {
                scodevaleurSearchRepository.save(savedScodevaleur);

                return savedScodevaleur;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Scodevaleur> findAll(Pageable pageable) {
        log.debug("Request to get all Scodevaleurs");
        return scodevaleurRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Scodevaleur> findOne(Long id) {
        log.debug("Request to get Scodevaleur : {}", id);
        return scodevaleurRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Scodevaleur : {}", id);
        scodevaleurRepository.deleteById(id);
        scodevaleurSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Scodevaleur> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Scodevaleurs for query {}", query);
        return scodevaleurSearchRepository.search(query, pageable);
    }
}
