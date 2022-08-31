package dsi.sea.sgess.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import dsi.sea.sgess.domain.Eattributvariable;
import dsi.sea.sgess.repository.EattributvariableRepository;
import dsi.sea.sgess.repository.search.EattributvariableSearchRepository;
import dsi.sea.sgess.service.EattributvariableService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Eattributvariable}.
 */
@Service
@Transactional
public class EattributvariableServiceImpl implements EattributvariableService {

    private final Logger log = LoggerFactory.getLogger(EattributvariableServiceImpl.class);

    private final EattributvariableRepository eattributvariableRepository;

    private final EattributvariableSearchRepository eattributvariableSearchRepository;

    public EattributvariableServiceImpl(
        EattributvariableRepository eattributvariableRepository,
        EattributvariableSearchRepository eattributvariableSearchRepository
    ) {
        this.eattributvariableRepository = eattributvariableRepository;
        this.eattributvariableSearchRepository = eattributvariableSearchRepository;
    }

    @Override
    public Eattributvariable save(Eattributvariable eattributvariable) {
        log.debug("Request to save Eattributvariable : {}", eattributvariable);
        Eattributvariable result = eattributvariableRepository.save(eattributvariable);
        eattributvariableSearchRepository.index(result);
        return result;
    }

    @Override
    public Eattributvariable update(Eattributvariable eattributvariable) {
        log.debug("Request to save Eattributvariable : {}", eattributvariable);
        Eattributvariable result = eattributvariableRepository.save(eattributvariable);
        eattributvariableSearchRepository.index(result);
        return result;
    }

    @Override
    public Optional<Eattributvariable> partialUpdate(Eattributvariable eattributvariable) {
        log.debug("Request to partially update Eattributvariable : {}", eattributvariable);

        return eattributvariableRepository
            .findById(eattributvariable.getId())
            .map(existingEattributvariable -> {
                if (eattributvariable.getAttrname() != null) {
                    existingEattributvariable.setAttrname(eattributvariable.getAttrname());
                }
                if (eattributvariable.getAttrvalue() != null) {
                    existingEattributvariable.setAttrvalue(eattributvariable.getAttrvalue());
                }
                if (eattributvariable.getIsActive() != null) {
                    existingEattributvariable.setIsActive(eattributvariable.getIsActive());
                }

                return existingEattributvariable;
            })
            .map(eattributvariableRepository::save)
            .map(savedEattributvariable -> {
                eattributvariableSearchRepository.save(savedEattributvariable);

                return savedEattributvariable;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Eattributvariable> findAll(Pageable pageable) {
        log.debug("Request to get all Eattributvariables");
        return eattributvariableRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Eattributvariable> findOne(Long id) {
        log.debug("Request to get Eattributvariable : {}", id);
        return eattributvariableRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Eattributvariable : {}", id);
        eattributvariableRepository.deleteById(id);
        eattributvariableSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Eattributvariable> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Eattributvariables for query {}", query);
        return eattributvariableSearchRepository.search(query, pageable);
    }
}
