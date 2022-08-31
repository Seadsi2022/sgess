package dsi.sea.sgess.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import dsi.sea.sgess.domain.Ecampagne;
import dsi.sea.sgess.repository.EcampagneRepository;
import dsi.sea.sgess.repository.search.EcampagneSearchRepository;
import dsi.sea.sgess.service.EcampagneService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Ecampagne}.
 */
@Service
@Transactional
public class EcampagneServiceImpl implements EcampagneService {

    private final Logger log = LoggerFactory.getLogger(EcampagneServiceImpl.class);

    private final EcampagneRepository ecampagneRepository;

    private final EcampagneSearchRepository ecampagneSearchRepository;

    public EcampagneServiceImpl(EcampagneRepository ecampagneRepository, EcampagneSearchRepository ecampagneSearchRepository) {
        this.ecampagneRepository = ecampagneRepository;
        this.ecampagneSearchRepository = ecampagneSearchRepository;
    }

    @Override
    public Ecampagne save(Ecampagne ecampagne) {
        log.debug("Request to save Ecampagne : {}", ecampagne);
        Ecampagne result = ecampagneRepository.save(ecampagne);
        ecampagneSearchRepository.index(result);
        return result;
    }

    @Override
    public Ecampagne update(Ecampagne ecampagne) {
        log.debug("Request to save Ecampagne : {}", ecampagne);
        Ecampagne result = ecampagneRepository.save(ecampagne);
        ecampagneSearchRepository.index(result);
        return result;
    }

    @Override
    public Optional<Ecampagne> partialUpdate(Ecampagne ecampagne) {
        log.debug("Request to partially update Ecampagne : {}", ecampagne);

        return ecampagneRepository
            .findById(ecampagne.getId())
            .map(existingEcampagne -> {
                if (ecampagne.getObjetcampagne() != null) {
                    existingEcampagne.setObjetcampagne(ecampagne.getObjetcampagne());
                }
                if (ecampagne.getDescription() != null) {
                    existingEcampagne.setDescription(ecampagne.getDescription());
                }
                if (ecampagne.getDebutcampagne() != null) {
                    existingEcampagne.setDebutcampagne(ecampagne.getDebutcampagne());
                }
                if (ecampagne.getFincampagne() != null) {
                    existingEcampagne.setFincampagne(ecampagne.getFincampagne());
                }
                if (ecampagne.getDebutreelcamp() != null) {
                    existingEcampagne.setDebutreelcamp(ecampagne.getDebutreelcamp());
                }
                if (ecampagne.getFinreelcamp() != null) {
                    existingEcampagne.setFinreelcamp(ecampagne.getFinreelcamp());
                }
                if (ecampagne.getIsopen() != null) {
                    existingEcampagne.setIsopen(ecampagne.getIsopen());
                }

                return existingEcampagne;
            })
            .map(ecampagneRepository::save)
            .map(savedEcampagne -> {
                ecampagneSearchRepository.save(savedEcampagne);

                return savedEcampagne;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Ecampagne> findAll(Pageable pageable) {
        log.debug("Request to get all Ecampagnes");
        return ecampagneRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Ecampagne> findOne(Long id) {
        log.debug("Request to get Ecampagne : {}", id);
        return ecampagneRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ecampagne : {}", id);
        ecampagneRepository.deleteById(id);
        ecampagneSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Ecampagne> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Ecampagnes for query {}", query);
        return ecampagneSearchRepository.search(query, pageable);
    }
}
