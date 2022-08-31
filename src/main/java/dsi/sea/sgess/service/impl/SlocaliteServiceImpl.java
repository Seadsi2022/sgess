package dsi.sea.sgess.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import dsi.sea.sgess.domain.Slocalite;
import dsi.sea.sgess.repository.SlocaliteRepository;
import dsi.sea.sgess.repository.search.SlocaliteSearchRepository;
import dsi.sea.sgess.service.SlocaliteService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Slocalite}.
 */
@Service
@Transactional
public class SlocaliteServiceImpl implements SlocaliteService {

    private final Logger log = LoggerFactory.getLogger(SlocaliteServiceImpl.class);

    private final SlocaliteRepository slocaliteRepository;

    private final SlocaliteSearchRepository slocaliteSearchRepository;

    public SlocaliteServiceImpl(SlocaliteRepository slocaliteRepository, SlocaliteSearchRepository slocaliteSearchRepository) {
        this.slocaliteRepository = slocaliteRepository;
        this.slocaliteSearchRepository = slocaliteSearchRepository;
    }

    @Override
    public Slocalite save(Slocalite slocalite) {
        log.debug("Request to save Slocalite : {}", slocalite);
        Slocalite result = slocaliteRepository.save(slocalite);
        slocaliteSearchRepository.index(result);
        return result;
    }

    @Override
    public Slocalite update(Slocalite slocalite) {
        log.debug("Request to save Slocalite : {}", slocalite);
        Slocalite result = slocaliteRepository.save(slocalite);
        slocaliteSearchRepository.index(result);
        return result;
    }

    @Override
    public Optional<Slocalite> partialUpdate(Slocalite slocalite) {
        log.debug("Request to partially update Slocalite : {}", slocalite);

        return slocaliteRepository
            .findById(slocalite.getId())
            .map(existingSlocalite -> {
                if (slocalite.getCodelocalite() != null) {
                    existingSlocalite.setCodelocalite(slocalite.getCodelocalite());
                }
                if (slocalite.getNomlocalite() != null) {
                    existingSlocalite.setNomlocalite(slocalite.getNomlocalite());
                }

                return existingSlocalite;
            })
            .map(slocaliteRepository::save)
            .map(savedSlocalite -> {
                slocaliteSearchRepository.save(savedSlocalite);

                return savedSlocalite;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Slocalite> findAll(Pageable pageable) {
        log.debug("Request to get all Slocalites");
        return slocaliteRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Slocalite> findOne(Long id) {
        log.debug("Request to get Slocalite : {}", id);
        return slocaliteRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Slocalite : {}", id);
        slocaliteRepository.deleteById(id);
        slocaliteSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Slocalite> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Slocalites for query {}", query);
        return slocaliteSearchRepository.search(query, pageable);
    }
}
