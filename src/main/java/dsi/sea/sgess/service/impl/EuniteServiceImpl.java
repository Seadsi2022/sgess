package dsi.sea.sgess.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import dsi.sea.sgess.domain.Eunite;
import dsi.sea.sgess.repository.EuniteRepository;
import dsi.sea.sgess.repository.search.EuniteSearchRepository;
import dsi.sea.sgess.service.EuniteService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Eunite}.
 */
@Service
@Transactional
public class EuniteServiceImpl implements EuniteService {

    private final Logger log = LoggerFactory.getLogger(EuniteServiceImpl.class);

    private final EuniteRepository euniteRepository;

    private final EuniteSearchRepository euniteSearchRepository;

    public EuniteServiceImpl(EuniteRepository euniteRepository, EuniteSearchRepository euniteSearchRepository) {
        this.euniteRepository = euniteRepository;
        this.euniteSearchRepository = euniteSearchRepository;
    }

    @Override
    public Eunite save(Eunite eunite) {
        log.debug("Request to save Eunite : {}", eunite);
        Eunite result = euniteRepository.save(eunite);
        euniteSearchRepository.index(result);
        return result;
    }

    @Override
    public Eunite update(Eunite eunite) {
        log.debug("Request to save Eunite : {}", eunite);
        Eunite result = euniteRepository.save(eunite);
        euniteSearchRepository.index(result);
        return result;
    }

    @Override
    public Optional<Eunite> partialUpdate(Eunite eunite) {
        log.debug("Request to partially update Eunite : {}", eunite);

        return euniteRepository
            .findById(eunite.getId())
            .map(existingEunite -> {
                if (eunite.getNomunite() != null) {
                    existingEunite.setNomunite(eunite.getNomunite());
                }
                if (eunite.getSymboleunite() != null) {
                    existingEunite.setSymboleunite(eunite.getSymboleunite());
                }
                if (eunite.getFacteur() != null) {
                    existingEunite.setFacteur(eunite.getFacteur());
                }

                return existingEunite;
            })
            .map(euniteRepository::save)
            .map(savedEunite -> {
                euniteSearchRepository.save(savedEunite);

                return savedEunite;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Eunite> findAll(Pageable pageable) {
        log.debug("Request to get all Eunites");
        return euniteRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Eunite> findOne(Long id) {
        log.debug("Request to get Eunite : {}", id);
        return euniteRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Eunite : {}", id);
        euniteRepository.deleteById(id);
        euniteSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Eunite> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Eunites for query {}", query);
        return euniteSearchRepository.search(query, pageable);
    }
}
