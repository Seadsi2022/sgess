package dsi.sea.sgess.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import dsi.sea.sgess.domain.Equestionnaire;
import dsi.sea.sgess.repository.EquestionnaireRepository;
import dsi.sea.sgess.repository.search.EquestionnaireSearchRepository;
import dsi.sea.sgess.service.EquestionnaireService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Equestionnaire}.
 */
@Service
@Transactional
public class EquestionnaireServiceImpl implements EquestionnaireService {

    private final Logger log = LoggerFactory.getLogger(EquestionnaireServiceImpl.class);

    private final EquestionnaireRepository equestionnaireRepository;

    private final EquestionnaireSearchRepository equestionnaireSearchRepository;

    public EquestionnaireServiceImpl(
        EquestionnaireRepository equestionnaireRepository,
        EquestionnaireSearchRepository equestionnaireSearchRepository
    ) {
        this.equestionnaireRepository = equestionnaireRepository;
        this.equestionnaireSearchRepository = equestionnaireSearchRepository;
    }

    @Override
    public Equestionnaire save(Equestionnaire equestionnaire) {
        log.debug("Request to save Equestionnaire : {}", equestionnaire);
        Equestionnaire result = equestionnaireRepository.save(equestionnaire);
        equestionnaireSearchRepository.index(result);
        return result;
    }

    @Override
    public Equestionnaire update(Equestionnaire equestionnaire) {
        log.debug("Request to save Equestionnaire : {}", equestionnaire);
        Equestionnaire result = equestionnaireRepository.save(equestionnaire);
        equestionnaireSearchRepository.index(result);
        return result;
    }

    @Override
    public Optional<Equestionnaire> partialUpdate(Equestionnaire equestionnaire) {
        log.debug("Request to partially update Equestionnaire : {}", equestionnaire);

        return equestionnaireRepository
            .findById(equestionnaire.getId())
            .map(existingEquestionnaire -> {
                if (equestionnaire.getObjetquest() != null) {
                    existingEquestionnaire.setObjetquest(equestionnaire.getObjetquest());
                }
                if (equestionnaire.getDescriptionquest() != null) {
                    existingEquestionnaire.setDescriptionquest(equestionnaire.getDescriptionquest());
                }
                if (equestionnaire.getIsActive() != null) {
                    existingEquestionnaire.setIsActive(equestionnaire.getIsActive());
                }

                return existingEquestionnaire;
            })
            .map(equestionnaireRepository::save)
            .map(savedEquestionnaire -> {
                equestionnaireSearchRepository.save(savedEquestionnaire);

                return savedEquestionnaire;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Equestionnaire> findAll(Pageable pageable) {
        log.debug("Request to get all Equestionnaires");
        return equestionnaireRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Equestionnaire> findOne(Long id) {
        log.debug("Request to get Equestionnaire : {}", id);
        return equestionnaireRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Equestionnaire : {}", id);
        equestionnaireRepository.deleteById(id);
        equestionnaireSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Equestionnaire> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Equestionnaires for query {}", query);
        return equestionnaireSearchRepository.search(query, pageable);
    }
}
