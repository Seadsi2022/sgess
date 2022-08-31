package dsi.sea.sgess.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import dsi.sea.sgess.domain.Eeventualitevariable;
import dsi.sea.sgess.repository.EeventualitevariableRepository;
import dsi.sea.sgess.repository.search.EeventualitevariableSearchRepository;
import dsi.sea.sgess.service.EeventualitevariableService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Eeventualitevariable}.
 */
@Service
@Transactional
public class EeventualitevariableServiceImpl implements EeventualitevariableService {

    private final Logger log = LoggerFactory.getLogger(EeventualitevariableServiceImpl.class);

    private final EeventualitevariableRepository eeventualitevariableRepository;

    private final EeventualitevariableSearchRepository eeventualitevariableSearchRepository;

    public EeventualitevariableServiceImpl(
        EeventualitevariableRepository eeventualitevariableRepository,
        EeventualitevariableSearchRepository eeventualitevariableSearchRepository
    ) {
        this.eeventualitevariableRepository = eeventualitevariableRepository;
        this.eeventualitevariableSearchRepository = eeventualitevariableSearchRepository;
    }

    @Override
    public Eeventualitevariable save(Eeventualitevariable eeventualitevariable) {
        log.debug("Request to save Eeventualitevariable : {}", eeventualitevariable);
        Eeventualitevariable result = eeventualitevariableRepository.save(eeventualitevariable);
        eeventualitevariableSearchRepository.index(result);
        return result;
    }

    @Override
    public Eeventualitevariable update(Eeventualitevariable eeventualitevariable) {
        log.debug("Request to save Eeventualitevariable : {}", eeventualitevariable);
        Eeventualitevariable result = eeventualitevariableRepository.save(eeventualitevariable);
        eeventualitevariableSearchRepository.index(result);
        return result;
    }

    @Override
    public Optional<Eeventualitevariable> partialUpdate(Eeventualitevariable eeventualitevariable) {
        log.debug("Request to partially update Eeventualitevariable : {}", eeventualitevariable);

        return eeventualitevariableRepository
            .findById(eeventualitevariable.getId())
            .map(existingEeventualitevariable -> {
                return existingEeventualitevariable;
            })
            .map(eeventualitevariableRepository::save)
            .map(savedEeventualitevariable -> {
                eeventualitevariableSearchRepository.save(savedEeventualitevariable);

                return savedEeventualitevariable;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Eeventualitevariable> findAll(Pageable pageable) {
        log.debug("Request to get all Eeventualitevariables");
        return eeventualitevariableRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Eeventualitevariable> findOne(Long id) {
        log.debug("Request to get Eeventualitevariable : {}", id);
        return eeventualitevariableRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Eeventualitevariable : {}", id);
        eeventualitevariableRepository.deleteById(id);
        eeventualitevariableSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Eeventualitevariable> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Eeventualitevariables for query {}", query);
        return eeventualitevariableSearchRepository.search(query, pageable);
    }
}
