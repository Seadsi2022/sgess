package dsi.sea.sgess.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import dsi.sea.sgess.domain.Egroupe;
import dsi.sea.sgess.repository.EgroupeRepository;
import dsi.sea.sgess.repository.search.EgroupeSearchRepository;
import dsi.sea.sgess.service.EgroupeService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Egroupe}.
 */
@Service
@Transactional
public class EgroupeServiceImpl implements EgroupeService {

    private final Logger log = LoggerFactory.getLogger(EgroupeServiceImpl.class);

    private final EgroupeRepository egroupeRepository;

    private final EgroupeSearchRepository egroupeSearchRepository;

    public EgroupeServiceImpl(EgroupeRepository egroupeRepository, EgroupeSearchRepository egroupeSearchRepository) {
        this.egroupeRepository = egroupeRepository;
        this.egroupeSearchRepository = egroupeSearchRepository;
    }

    @Override
    public Egroupe save(Egroupe egroupe) {
        log.debug("Request to save Egroupe : {}", egroupe);
        Egroupe result = egroupeRepository.save(egroupe);
        egroupeSearchRepository.index(result);
        return result;
    }

    @Override
    public Egroupe update(Egroupe egroupe) {
        log.debug("Request to save Egroupe : {}", egroupe);
        Egroupe result = egroupeRepository.save(egroupe);
        egroupeSearchRepository.index(result);
        return result;
    }

    @Override
    public Optional<Egroupe> partialUpdate(Egroupe egroupe) {
        log.debug("Request to partially update Egroupe : {}", egroupe);

        return egroupeRepository
            .findById(egroupe.getId())
            .map(existingEgroupe -> {
                if (egroupe.getTitregroupe() != null) {
                    existingEgroupe.setTitregroupe(egroupe.getTitregroupe());
                }
                if (egroupe.getOrdregroupe() != null) {
                    existingEgroupe.setOrdregroupe(egroupe.getOrdregroupe());
                }
                if (egroupe.getIsActive() != null) {
                    existingEgroupe.setIsActive(egroupe.getIsActive());
                }

                return existingEgroupe;
            })
            .map(egroupeRepository::save)
            .map(savedEgroupe -> {
                egroupeSearchRepository.save(savedEgroupe);

                return savedEgroupe;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Egroupe> findAll(Pageable pageable) {
        log.debug("Request to get all Egroupes");
        return egroupeRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Egroupe> findOne(Long id) {
        log.debug("Request to get Egroupe : {}", id);
        return egroupeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Egroupe : {}", id);
        egroupeRepository.deleteById(id);
        egroupeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Egroupe> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Egroupes for query {}", query);
        return egroupeSearchRepository.search(query, pageable);
    }
}
