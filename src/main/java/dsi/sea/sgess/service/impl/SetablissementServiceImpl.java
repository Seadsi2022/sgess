package dsi.sea.sgess.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import dsi.sea.sgess.domain.Setablissement;
import dsi.sea.sgess.repository.SetablissementRepository;
import dsi.sea.sgess.repository.search.SetablissementSearchRepository;
import dsi.sea.sgess.service.SetablissementService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Setablissement}.
 */
@Service
@Transactional
public class SetablissementServiceImpl implements SetablissementService {

    private final Logger log = LoggerFactory.getLogger(SetablissementServiceImpl.class);

    private final SetablissementRepository setablissementRepository;

    private final SetablissementSearchRepository setablissementSearchRepository;

    public SetablissementServiceImpl(
        SetablissementRepository setablissementRepository,
        SetablissementSearchRepository setablissementSearchRepository
    ) {
        this.setablissementRepository = setablissementRepository;
        this.setablissementSearchRepository = setablissementSearchRepository;
    }

    @Override
    public Setablissement save(Setablissement setablissement) {
        log.debug("Request to save Setablissement : {}", setablissement);
        Setablissement result = setablissementRepository.save(setablissement);
        setablissementSearchRepository.index(result);
        return result;
    }

    @Override
    public Setablissement update(Setablissement setablissement) {
        log.debug("Request to save Setablissement : {}", setablissement);
        Setablissement result = setablissementRepository.save(setablissement);
        setablissementSearchRepository.index(result);
        return result;
    }

    @Override
    public Optional<Setablissement> partialUpdate(Setablissement setablissement) {
        log.debug("Request to partially update Setablissement : {}", setablissement);

        return setablissementRepository
            .findById(setablissement.getId())
            .map(existingSetablissement -> {
                if (setablissement.getCodeadministratif() != null) {
                    existingSetablissement.setCodeadministratif(setablissement.getCodeadministratif());
                }

                return existingSetablissement;
            })
            .map(setablissementRepository::save)
            .map(savedSetablissement -> {
                setablissementSearchRepository.save(savedSetablissement);

                return savedSetablissement;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Setablissement> findAll(Pageable pageable) {
        log.debug("Request to get all Setablissements");
        return setablissementRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Setablissement> findOne(Long id) {
        log.debug("Request to get Setablissement : {}", id);
        return setablissementRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Setablissement : {}", id);
        setablissementRepository.deleteById(id);
        setablissementSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Setablissement> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Setablissements for query {}", query);
        return setablissementSearchRepository.search(query, pageable);
    }
}
