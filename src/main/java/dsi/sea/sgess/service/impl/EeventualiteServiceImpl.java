package dsi.sea.sgess.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import dsi.sea.sgess.domain.Eeventualite;
import dsi.sea.sgess.repository.EeventualiteRepository;
import dsi.sea.sgess.repository.search.EeventualiteSearchRepository;
import dsi.sea.sgess.service.EeventualiteService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Eeventualite}.
 */
@Service
@Transactional
public class EeventualiteServiceImpl implements EeventualiteService {

    private final Logger log = LoggerFactory.getLogger(EeventualiteServiceImpl.class);

    private final EeventualiteRepository eeventualiteRepository;

    private final EeventualiteSearchRepository eeventualiteSearchRepository;

    public EeventualiteServiceImpl(
        EeventualiteRepository eeventualiteRepository,
        EeventualiteSearchRepository eeventualiteSearchRepository
    ) {
        this.eeventualiteRepository = eeventualiteRepository;
        this.eeventualiteSearchRepository = eeventualiteSearchRepository;
    }

    @Override
    public Eeventualite save(Eeventualite eeventualite) {
        log.debug("Request to save Eeventualite : {}", eeventualite);
        Eeventualite result = eeventualiteRepository.save(eeventualite);
        eeventualiteSearchRepository.index(result);
        return result;
    }

    @Override
    public Eeventualite update(Eeventualite eeventualite) {
        log.debug("Request to save Eeventualite : {}", eeventualite);
        Eeventualite result = eeventualiteRepository.save(eeventualite);
        eeventualiteSearchRepository.index(result);
        return result;
    }

    @Override
    public Optional<Eeventualite> partialUpdate(Eeventualite eeventualite) {
        log.debug("Request to partially update Eeventualite : {}", eeventualite);

        return eeventualiteRepository
            .findById(eeventualite.getId())
            .map(existingEeventualite -> {
                if (eeventualite.getEventualitevalue() != null) {
                    existingEeventualite.setEventualitevalue(eeventualite.getEventualitevalue());
                }
                if (eeventualite.getIsActive() != null) {
                    existingEeventualite.setIsActive(eeventualite.getIsActive());
                }

                return existingEeventualite;
            })
            .map(eeventualiteRepository::save)
            .map(savedEeventualite -> {
                eeventualiteSearchRepository.save(savedEeventualite);

                return savedEeventualite;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Eeventualite> findAll(Pageable pageable) {
        log.debug("Request to get all Eeventualites");
        return eeventualiteRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Eeventualite> findOne(Long id) {
        log.debug("Request to get Eeventualite : {}", id);
        return eeventualiteRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Eeventualite : {}", id);
        eeventualiteRepository.deleteById(id);
        eeventualiteSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Eeventualite> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Eeventualites for query {}", query);
        return eeventualiteSearchRepository.search(query, pageable);
    }
}
