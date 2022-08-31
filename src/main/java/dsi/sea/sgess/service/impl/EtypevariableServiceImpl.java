package dsi.sea.sgess.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import dsi.sea.sgess.domain.Etypevariable;
import dsi.sea.sgess.repository.EtypevariableRepository;
import dsi.sea.sgess.repository.search.EtypevariableSearchRepository;
import dsi.sea.sgess.service.EtypevariableService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Etypevariable}.
 */
@Service
@Transactional
public class EtypevariableServiceImpl implements EtypevariableService {

    private final Logger log = LoggerFactory.getLogger(EtypevariableServiceImpl.class);

    private final EtypevariableRepository etypevariableRepository;

    private final EtypevariableSearchRepository etypevariableSearchRepository;

    public EtypevariableServiceImpl(
        EtypevariableRepository etypevariableRepository,
        EtypevariableSearchRepository etypevariableSearchRepository
    ) {
        this.etypevariableRepository = etypevariableRepository;
        this.etypevariableSearchRepository = etypevariableSearchRepository;
    }

    @Override
    public Etypevariable save(Etypevariable etypevariable) {
        log.debug("Request to save Etypevariable : {}", etypevariable);
        Etypevariable result = etypevariableRepository.save(etypevariable);
        etypevariableSearchRepository.index(result);
        return result;
    }

    @Override
    public Etypevariable update(Etypevariable etypevariable) {
        log.debug("Request to save Etypevariable : {}", etypevariable);
        Etypevariable result = etypevariableRepository.save(etypevariable);
        etypevariableSearchRepository.index(result);
        return result;
    }

    @Override
    public Optional<Etypevariable> partialUpdate(Etypevariable etypevariable) {
        log.debug("Request to partially update Etypevariable : {}", etypevariable);

        return etypevariableRepository
            .findById(etypevariable.getId())
            .map(existingEtypevariable -> {
                if (etypevariable.getNomtypevar() != null) {
                    existingEtypevariable.setNomtypevar(etypevariable.getNomtypevar());
                }
                if (etypevariable.getDesctypevariable() != null) {
                    existingEtypevariable.setDesctypevariable(etypevariable.getDesctypevariable());
                }
                if (etypevariable.getIsActive() != null) {
                    existingEtypevariable.setIsActive(etypevariable.getIsActive());
                }

                return existingEtypevariable;
            })
            .map(etypevariableRepository::save)
            .map(savedEtypevariable -> {
                etypevariableSearchRepository.save(savedEtypevariable);

                return savedEtypevariable;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Etypevariable> findAll(Pageable pageable) {
        log.debug("Request to get all Etypevariables");
        return etypevariableRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Etypevariable> findOne(Long id) {
        log.debug("Request to get Etypevariable : {}", id);
        return etypevariableRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Etypevariable : {}", id);
        etypevariableRepository.deleteById(id);
        etypevariableSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Etypevariable> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Etypevariables for query {}", query);
        return etypevariableSearchRepository.search(query, pageable);
    }
}
