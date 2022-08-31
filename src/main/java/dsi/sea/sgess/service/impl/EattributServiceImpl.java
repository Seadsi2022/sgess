package dsi.sea.sgess.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import dsi.sea.sgess.domain.Eattribut;
import dsi.sea.sgess.repository.EattributRepository;
import dsi.sea.sgess.repository.search.EattributSearchRepository;
import dsi.sea.sgess.service.EattributService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Eattribut}.
 */
@Service
@Transactional
public class EattributServiceImpl implements EattributService {

    private final Logger log = LoggerFactory.getLogger(EattributServiceImpl.class);

    private final EattributRepository eattributRepository;

    private final EattributSearchRepository eattributSearchRepository;

    public EattributServiceImpl(EattributRepository eattributRepository, EattributSearchRepository eattributSearchRepository) {
        this.eattributRepository = eattributRepository;
        this.eattributSearchRepository = eattributSearchRepository;
    }

    @Override
    public Eattribut save(Eattribut eattribut) {
        log.debug("Request to save Eattribut : {}", eattribut);
        Eattribut result = eattributRepository.save(eattribut);
        eattributSearchRepository.index(result);
        return result;
    }

    @Override
    public Eattribut update(Eattribut eattribut) {
        log.debug("Request to save Eattribut : {}", eattribut);
        Eattribut result = eattributRepository.save(eattribut);
        eattributSearchRepository.index(result);
        return result;
    }

    @Override
    public Optional<Eattribut> partialUpdate(Eattribut eattribut) {
        log.debug("Request to partially update Eattribut : {}", eattribut);

        return eattributRepository
            .findById(eattribut.getId())
            .map(existingEattribut -> {
                if (eattribut.getAttrname() != null) {
                    existingEattribut.setAttrname(eattribut.getAttrname());
                }
                if (eattribut.getAttrdisplayname() != null) {
                    existingEattribut.setAttrdisplayname(eattribut.getAttrdisplayname());
                }

                return existingEattribut;
            })
            .map(eattributRepository::save)
            .map(savedEattribut -> {
                eattributSearchRepository.save(savedEattribut);

                return savedEattribut;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Eattribut> findAll(Pageable pageable) {
        log.debug("Request to get all Eattributs");
        return eattributRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Eattribut> findOne(Long id) {
        log.debug("Request to get Eattribut : {}", id);
        return eattributRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Eattribut : {}", id);
        eattributRepository.deleteById(id);
        eattributSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Eattribut> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Eattributs for query {}", query);
        return eattributSearchRepository.search(query, pageable);
    }
}
