package dsi.sea.sgess.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import dsi.sea.sgess.domain.Evaleurattribut;
import dsi.sea.sgess.repository.EvaleurattributRepository;
import dsi.sea.sgess.repository.search.EvaleurattributSearchRepository;
import dsi.sea.sgess.service.EvaleurattributService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Evaleurattribut}.
 */
@Service
@Transactional
public class EvaleurattributServiceImpl implements EvaleurattributService {

    private final Logger log = LoggerFactory.getLogger(EvaleurattributServiceImpl.class);

    private final EvaleurattributRepository evaleurattributRepository;

    private final EvaleurattributSearchRepository evaleurattributSearchRepository;

    public EvaleurattributServiceImpl(
        EvaleurattributRepository evaleurattributRepository,
        EvaleurattributSearchRepository evaleurattributSearchRepository
    ) {
        this.evaleurattributRepository = evaleurattributRepository;
        this.evaleurattributSearchRepository = evaleurattributSearchRepository;
    }

    @Override
    public Evaleurattribut save(Evaleurattribut evaleurattribut) {
        log.debug("Request to save Evaleurattribut : {}", evaleurattribut);
        Evaleurattribut result = evaleurattributRepository.save(evaleurattribut);
        evaleurattributSearchRepository.index(result);
        return result;
    }

    @Override
    public Evaleurattribut update(Evaleurattribut evaleurattribut) {
        log.debug("Request to save Evaleurattribut : {}", evaleurattribut);
        Evaleurattribut result = evaleurattributRepository.save(evaleurattribut);
        evaleurattributSearchRepository.index(result);
        return result;
    }

    @Override
    public Optional<Evaleurattribut> partialUpdate(Evaleurattribut evaleurattribut) {
        log.debug("Request to partially update Evaleurattribut : {}", evaleurattribut);

        return evaleurattributRepository
            .findById(evaleurattribut.getId())
            .map(existingEvaleurattribut -> {
                if (evaleurattribut.getValeur() != null) {
                    existingEvaleurattribut.setValeur(evaleurattribut.getValeur());
                }
                if (evaleurattribut.getValeurdisplayname() != null) {
                    existingEvaleurattribut.setValeurdisplayname(evaleurattribut.getValeurdisplayname());
                }

                return existingEvaleurattribut;
            })
            .map(evaleurattributRepository::save)
            .map(savedEvaleurattribut -> {
                evaleurattributSearchRepository.save(savedEvaleurattribut);

                return savedEvaleurattribut;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Evaleurattribut> findAll(Pageable pageable) {
        log.debug("Request to get all Evaleurattributs");
        return evaleurattributRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Evaleurattribut> findOne(Long id) {
        log.debug("Request to get Evaleurattribut : {}", id);
        return evaleurattributRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Evaleurattribut : {}", id);
        evaleurattributRepository.deleteById(id);
        evaleurattributSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Evaleurattribut> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Evaleurattributs for query {}", query);
        return evaleurattributSearchRepository.search(query, pageable);
    }
}
