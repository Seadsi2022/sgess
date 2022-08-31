package dsi.sea.sgess.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import dsi.sea.sgess.domain.Egroupevariable;
import dsi.sea.sgess.repository.EgroupevariableRepository;
import dsi.sea.sgess.repository.search.EgroupevariableSearchRepository;
import dsi.sea.sgess.service.EgroupevariableService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Egroupevariable}.
 */
@Service
@Transactional
public class EgroupevariableServiceImpl implements EgroupevariableService {

    private final Logger log = LoggerFactory.getLogger(EgroupevariableServiceImpl.class);

    private final EgroupevariableRepository egroupevariableRepository;

    private final EgroupevariableSearchRepository egroupevariableSearchRepository;

    public EgroupevariableServiceImpl(
        EgroupevariableRepository egroupevariableRepository,
        EgroupevariableSearchRepository egroupevariableSearchRepository
    ) {
        this.egroupevariableRepository = egroupevariableRepository;
        this.egroupevariableSearchRepository = egroupevariableSearchRepository;
    }

    @Override
    public Egroupevariable save(Egroupevariable egroupevariable) {
        log.debug("Request to save Egroupevariable : {}", egroupevariable);
        Egroupevariable result = egroupevariableRepository.save(egroupevariable);
        egroupevariableSearchRepository.index(result);
        return result;
    }

    @Override
    public Egroupevariable update(Egroupevariable egroupevariable) {
        log.debug("Request to save Egroupevariable : {}", egroupevariable);
        Egroupevariable result = egroupevariableRepository.save(egroupevariable);
        egroupevariableSearchRepository.index(result);
        return result;
    }

    @Override
    public Optional<Egroupevariable> partialUpdate(Egroupevariable egroupevariable) {
        log.debug("Request to partially update Egroupevariable : {}", egroupevariable);

        return egroupevariableRepository
            .findById(egroupevariable.getId())
            .map(existingEgroupevariable -> {
                if (egroupevariable.getOrdrevariable() != null) {
                    existingEgroupevariable.setOrdrevariable(egroupevariable.getOrdrevariable());
                }
                if (egroupevariable.getIsActive() != null) {
                    existingEgroupevariable.setIsActive(egroupevariable.getIsActive());
                }

                return existingEgroupevariable;
            })
            .map(egroupevariableRepository::save)
            .map(savedEgroupevariable -> {
                egroupevariableSearchRepository.save(savedEgroupevariable);

                return savedEgroupevariable;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Egroupevariable> findAll(Pageable pageable) {
        log.debug("Request to get all Egroupevariables");
        return egroupevariableRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Egroupevariable> findOne(Long id) {
        log.debug("Request to get Egroupevariable : {}", id);
        return egroupevariableRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Egroupevariable : {}", id);
        egroupevariableRepository.deleteById(id);
        egroupevariableSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Egroupevariable> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Egroupevariables for query {}", query);
        return egroupevariableSearchRepository.search(query, pageable);
    }
}
