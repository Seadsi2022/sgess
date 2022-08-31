package dsi.sea.sgess.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import dsi.sea.sgess.domain.Sstructure;
import dsi.sea.sgess.repository.SstructureRepository;
import dsi.sea.sgess.repository.search.SstructureSearchRepository;
import dsi.sea.sgess.service.SstructureService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Sstructure}.
 */
@Service
@Transactional
public class SstructureServiceImpl implements SstructureService {

    private final Logger log = LoggerFactory.getLogger(SstructureServiceImpl.class);

    private final SstructureRepository sstructureRepository;

    private final SstructureSearchRepository sstructureSearchRepository;

    public SstructureServiceImpl(SstructureRepository sstructureRepository, SstructureSearchRepository sstructureSearchRepository) {
        this.sstructureRepository = sstructureRepository;
        this.sstructureSearchRepository = sstructureSearchRepository;
    }

    @Override
    public Sstructure save(Sstructure sstructure) {
        log.debug("Request to save Sstructure : {}", sstructure);
        Sstructure result = sstructureRepository.save(sstructure);
        sstructureSearchRepository.index(result);
        return result;
    }

    @Override
    public Sstructure update(Sstructure sstructure) {
        log.debug("Request to save Sstructure : {}", sstructure);
        Sstructure result = sstructureRepository.save(sstructure);
        sstructureSearchRepository.index(result);
        return result;
    }

    @Override
    public Optional<Sstructure> partialUpdate(Sstructure sstructure) {
        log.debug("Request to partially update Sstructure : {}", sstructure);

        return sstructureRepository
            .findById(sstructure.getId())
            .map(existingSstructure -> {
                if (sstructure.getNomstructure() != null) {
                    existingSstructure.setNomstructure(sstructure.getNomstructure());
                }
                if (sstructure.getSiglestructure() != null) {
                    existingSstructure.setSiglestructure(sstructure.getSiglestructure());
                }
                if (sstructure.getTelstructure() != null) {
                    existingSstructure.setTelstructure(sstructure.getTelstructure());
                }
                if (sstructure.getBpstructure() != null) {
                    existingSstructure.setBpstructure(sstructure.getBpstructure());
                }
                if (sstructure.getEmailstructure() != null) {
                    existingSstructure.setEmailstructure(sstructure.getEmailstructure());
                }
                if (sstructure.getProfondeur() != null) {
                    existingSstructure.setProfondeur(sstructure.getProfondeur());
                }

                return existingSstructure;
            })
            .map(sstructureRepository::save)
            .map(savedSstructure -> {
                sstructureSearchRepository.save(savedSstructure);

                return savedSstructure;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Sstructure> findAll(Pageable pageable) {
        log.debug("Request to get all Sstructures");
        return sstructureRepository.findAll(pageable);
    }

    public Page<Sstructure> findAllWithEagerRelationships(Pageable pageable) {
        return sstructureRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Sstructure> findOne(Long id) {
        log.debug("Request to get Sstructure : {}", id);
        return sstructureRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Sstructure : {}", id);
        sstructureRepository.deleteById(id);
        sstructureSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Sstructure> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Sstructures for query {}", query);
        return sstructureSearchRepository.search(query, pageable);
    }
}
