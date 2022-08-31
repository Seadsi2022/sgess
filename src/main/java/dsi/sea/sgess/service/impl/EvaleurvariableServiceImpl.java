package dsi.sea.sgess.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import dsi.sea.sgess.domain.Evaleurvariable;
import dsi.sea.sgess.repository.EvaleurvariableRepository;
import dsi.sea.sgess.repository.search.EvaleurvariableSearchRepository;
import dsi.sea.sgess.service.EvaleurvariableService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Evaleurvariable}.
 */
@Service
@Transactional
public class EvaleurvariableServiceImpl implements EvaleurvariableService {

    private final Logger log = LoggerFactory.getLogger(EvaleurvariableServiceImpl.class);

    private final EvaleurvariableRepository evaleurvariableRepository;

    private final EvaleurvariableSearchRepository evaleurvariableSearchRepository;

    public EvaleurvariableServiceImpl(
        EvaleurvariableRepository evaleurvariableRepository,
        EvaleurvariableSearchRepository evaleurvariableSearchRepository
    ) {
        this.evaleurvariableRepository = evaleurvariableRepository;
        this.evaleurvariableSearchRepository = evaleurvariableSearchRepository;
    }

    @Override
    public Evaleurvariable save(Evaleurvariable evaleurvariable) {
        log.debug("Request to save Evaleurvariable : {}", evaleurvariable);
        Evaleurvariable result = evaleurvariableRepository.save(evaleurvariable);
        evaleurvariableSearchRepository.index(result);
        return result;
    }

    @Override
    public Evaleurvariable update(Evaleurvariable evaleurvariable) {
        log.debug("Request to save Evaleurvariable : {}", evaleurvariable);
        Evaleurvariable result = evaleurvariableRepository.save(evaleurvariable);
        evaleurvariableSearchRepository.index(result);
        return result;
    }

    @Override
    public Optional<Evaleurvariable> partialUpdate(Evaleurvariable evaleurvariable) {
        log.debug("Request to partially update Evaleurvariable : {}", evaleurvariable);

        return evaleurvariableRepository
            .findById(evaleurvariable.getId())
            .map(existingEvaleurvariable -> {
                if (evaleurvariable.getValeur() != null) {
                    existingEvaleurvariable.setValeur(evaleurvariable.getValeur());
                }
                if (evaleurvariable.getLigne() != null) {
                    existingEvaleurvariable.setLigne(evaleurvariable.getLigne());
                }
                if (evaleurvariable.getColonne() != null) {
                    existingEvaleurvariable.setColonne(evaleurvariable.getColonne());
                }
                if (evaleurvariable.getIsActive() != null) {
                    existingEvaleurvariable.setIsActive(evaleurvariable.getIsActive());
                }

                return existingEvaleurvariable;
            })
            .map(evaleurvariableRepository::save)
            .map(savedEvaleurvariable -> {
                evaleurvariableSearchRepository.save(savedEvaleurvariable);

                return savedEvaleurvariable;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Evaleurvariable> findAll(Pageable pageable) {
        log.debug("Request to get all Evaleurvariables");
        return evaleurvariableRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Evaleurvariable> findOne(Long id) {
        log.debug("Request to get Evaleurvariable : {}", id);
        return evaleurvariableRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Evaleurvariable : {}", id);
        evaleurvariableRepository.deleteById(id);
        evaleurvariableSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Evaleurvariable> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Evaleurvariables for query {}", query);
        return evaleurvariableSearchRepository.search(query, pageable);
    }
}
