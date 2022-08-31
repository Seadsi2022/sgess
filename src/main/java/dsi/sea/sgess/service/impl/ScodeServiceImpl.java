package dsi.sea.sgess.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import dsi.sea.sgess.domain.Scode;
import dsi.sea.sgess.repository.ScodeRepository;
import dsi.sea.sgess.repository.search.ScodeSearchRepository;
import dsi.sea.sgess.service.ScodeService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Scode}.
 */
@Service
@Transactional
public class ScodeServiceImpl implements ScodeService {

    private final Logger log = LoggerFactory.getLogger(ScodeServiceImpl.class);

    private final ScodeRepository scodeRepository;

    private final ScodeSearchRepository scodeSearchRepository;

    public ScodeServiceImpl(ScodeRepository scodeRepository, ScodeSearchRepository scodeSearchRepository) {
        this.scodeRepository = scodeRepository;
        this.scodeSearchRepository = scodeSearchRepository;
    }

    @Override
    public Scode save(Scode scode) {
        log.debug("Request to save Scode : {}", scode);
        Scode result = scodeRepository.save(scode);
        scodeSearchRepository.index(result);
        return result;
    }

    @Override
    public Scode update(Scode scode) {
        log.debug("Request to save Scode : {}", scode);
        Scode result = scodeRepository.save(scode);
        scodeSearchRepository.index(result);
        return result;
    }

    @Override
    public Optional<Scode> partialUpdate(Scode scode) {
        log.debug("Request to partially update Scode : {}", scode);

        return scodeRepository
            .findById(scode.getId())
            .map(existingScode -> {
                if (scode.getCodeLib() != null) {
                    existingScode.setCodeLib(scode.getCodeLib());
                }

                return existingScode;
            })
            .map(scodeRepository::save)
            .map(savedScode -> {
                scodeSearchRepository.save(savedScode);

                return savedScode;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Scode> findAll(Pageable pageable) {
        log.debug("Request to get all Scodes");
        return scodeRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Scode> findOne(Long id) {
        log.debug("Request to get Scode : {}", id);
        return scodeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Scode : {}", id);
        scodeRepository.deleteById(id);
        scodeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Scode> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Scodes for query {}", query);
        return scodeSearchRepository.search(query, pageable);
    }
}
