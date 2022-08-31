package dsi.sea.sgess.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import dsi.sea.sgess.domain.Evariable;
import dsi.sea.sgess.repository.EvariableRepository;
import dsi.sea.sgess.repository.search.EvariableSearchRepository;
import dsi.sea.sgess.service.EvariableService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Evariable}.
 */
@Service
@Transactional
public class EvariableServiceImpl implements EvariableService {

    private final Logger log = LoggerFactory.getLogger(EvariableServiceImpl.class);

    private final EvariableRepository evariableRepository;

    private final EvariableSearchRepository evariableSearchRepository;

    public EvariableServiceImpl(EvariableRepository evariableRepository, EvariableSearchRepository evariableSearchRepository) {
        this.evariableRepository = evariableRepository;
        this.evariableSearchRepository = evariableSearchRepository;
    }

    @Override
    public Evariable save(Evariable evariable) {
        log.debug("Request to save Evariable : {}", evariable);
        Evariable result = evariableRepository.save(evariable);
        evariableSearchRepository.index(result);
        return result;
    }

    @Override
    public Evariable update(Evariable evariable) {
        log.debug("Request to save Evariable : {}", evariable);
        Evariable result = evariableRepository.save(evariable);
        evariableSearchRepository.index(result);
        return result;
    }

    @Override
    public Optional<Evariable> partialUpdate(Evariable evariable) {
        log.debug("Request to partially update Evariable : {}", evariable);

        return evariableRepository
            .findById(evariable.getId())
            .map(existingEvariable -> {
                if (evariable.getNomvariable() != null) {
                    existingEvariable.setNomvariable(evariable.getNomvariable());
                }
                if (evariable.getDescvariable() != null) {
                    existingEvariable.setDescvariable(evariable.getDescvariable());
                }
                if (evariable.getChamp() != null) {
                    existingEvariable.setChamp(evariable.getChamp());
                }
                if (evariable.getIsActive() != null) {
                    existingEvariable.setIsActive(evariable.getIsActive());
                }

                return existingEvariable;
            })
            .map(evariableRepository::save)
            .map(savedEvariable -> {
                evariableSearchRepository.save(savedEvariable);

                return savedEvariable;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Evariable> findAll(Pageable pageable) {
        log.debug("Request to get all Evariables");
        return evariableRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Evariable> findOne(Long id) {
        log.debug("Request to get Evariable : {}", id);
        return evariableRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Evariable : {}", id);
        evariableRepository.deleteById(id);
        evariableSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Evariable> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Evariables for query {}", query);
        return evariableSearchRepository.search(query, pageable);
    }
}
