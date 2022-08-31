package dsi.sea.sgess.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import dsi.sea.sgess.domain.Etypechamp;
import dsi.sea.sgess.repository.EtypechampRepository;
import dsi.sea.sgess.repository.search.EtypechampSearchRepository;
import dsi.sea.sgess.service.EtypechampService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Etypechamp}.
 */
@Service
@Transactional
public class EtypechampServiceImpl implements EtypechampService {

    private final Logger log = LoggerFactory.getLogger(EtypechampServiceImpl.class);

    private final EtypechampRepository etypechampRepository;

    private final EtypechampSearchRepository etypechampSearchRepository;

    public EtypechampServiceImpl(EtypechampRepository etypechampRepository, EtypechampSearchRepository etypechampSearchRepository) {
        this.etypechampRepository = etypechampRepository;
        this.etypechampSearchRepository = etypechampSearchRepository;
    }

    @Override
    public Etypechamp save(Etypechamp etypechamp) {
        log.debug("Request to save Etypechamp : {}", etypechamp);
        Etypechamp result = etypechampRepository.save(etypechamp);
        etypechampSearchRepository.index(result);
        return result;
    }

    @Override
    public Etypechamp update(Etypechamp etypechamp) {
        log.debug("Request to save Etypechamp : {}", etypechamp);
        Etypechamp result = etypechampRepository.save(etypechamp);
        etypechampSearchRepository.index(result);
        return result;
    }

    @Override
    public Optional<Etypechamp> partialUpdate(Etypechamp etypechamp) {
        log.debug("Request to partially update Etypechamp : {}", etypechamp);

        return etypechampRepository
            .findById(etypechamp.getId())
            .map(existingEtypechamp -> {
                if (etypechamp.getName() != null) {
                    existingEtypechamp.setName(etypechamp.getName());
                }
                if (etypechamp.getDisplayname() != null) {
                    existingEtypechamp.setDisplayname(etypechamp.getDisplayname());
                }

                return existingEtypechamp;
            })
            .map(etypechampRepository::save)
            .map(savedEtypechamp -> {
                etypechampSearchRepository.save(savedEtypechamp);

                return savedEtypechamp;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Etypechamp> findAll(Pageable pageable) {
        log.debug("Request to get all Etypechamps");
        return etypechampRepository.findAll(pageable);
    }

    public Page<Etypechamp> findAllWithEagerRelationships(Pageable pageable) {
        return etypechampRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Etypechamp> findOne(Long id) {
        log.debug("Request to get Etypechamp : {}", id);
        return etypechampRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Etypechamp : {}", id);
        etypechampRepository.deleteById(id);
        etypechampSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Etypechamp> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Etypechamps for query {}", query);
        return etypechampSearchRepository.search(query, pageable);
    }
}
