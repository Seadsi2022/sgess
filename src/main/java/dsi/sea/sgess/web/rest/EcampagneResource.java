package dsi.sea.sgess.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import dsi.sea.sgess.domain.Ecampagne;
import dsi.sea.sgess.repository.EcampagneRepository;
import dsi.sea.sgess.service.EcampagneService;
import dsi.sea.sgess.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link dsi.sea.sgess.domain.Ecampagne}.
 */
@RestController
@RequestMapping("/api")
public class EcampagneResource {

    private final Logger log = LoggerFactory.getLogger(EcampagneResource.class);

    private static final String ENTITY_NAME = "ecampagne";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EcampagneService ecampagneService;

    private final EcampagneRepository ecampagneRepository;

    public EcampagneResource(EcampagneService ecampagneService, EcampagneRepository ecampagneRepository) {
        this.ecampagneService = ecampagneService;
        this.ecampagneRepository = ecampagneRepository;
    }

    /**
     * {@code POST  /ecampagnes} : Create a new ecampagne.
     *
     * @param ecampagne the ecampagne to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ecampagne, or with status {@code 400 (Bad Request)} if the ecampagne has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ecampagnes")
    public ResponseEntity<Ecampagne> createEcampagne(@RequestBody Ecampagne ecampagne) throws URISyntaxException {
        log.debug("REST request to save Ecampagne : {}", ecampagne);
        if (ecampagne.getId() != null) {
            throw new BadRequestAlertException("A new ecampagne cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Ecampagne result = ecampagneService.save(ecampagne);
        return ResponseEntity
            .created(new URI("/api/ecampagnes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ecampagnes/:id} : Updates an existing ecampagne.
     *
     * @param id the id of the ecampagne to save.
     * @param ecampagne the ecampagne to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ecampagne,
     * or with status {@code 400 (Bad Request)} if the ecampagne is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ecampagne couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ecampagnes/{id}")
    public ResponseEntity<Ecampagne> updateEcampagne(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Ecampagne ecampagne
    ) throws URISyntaxException {
        log.debug("REST request to update Ecampagne : {}, {}", id, ecampagne);
        if (ecampagne.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ecampagne.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ecampagneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Ecampagne result = ecampagneService.update(ecampagne);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ecampagne.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ecampagnes/:id} : Partial updates given fields of an existing ecampagne, field will ignore if it is null
     *
     * @param id the id of the ecampagne to save.
     * @param ecampagne the ecampagne to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ecampagne,
     * or with status {@code 400 (Bad Request)} if the ecampagne is not valid,
     * or with status {@code 404 (Not Found)} if the ecampagne is not found,
     * or with status {@code 500 (Internal Server Error)} if the ecampagne couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ecampagnes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Ecampagne> partialUpdateEcampagne(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Ecampagne ecampagne
    ) throws URISyntaxException {
        log.debug("REST request to partial update Ecampagne partially : {}, {}", id, ecampagne);
        if (ecampagne.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ecampagne.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ecampagneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Ecampagne> result = ecampagneService.partialUpdate(ecampagne);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ecampagne.getId().toString())
        );
    }

    /**
     * {@code GET  /ecampagnes} : get all the ecampagnes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ecampagnes in body.
     */
    @GetMapping("/ecampagnes")
    public ResponseEntity<List<Ecampagne>> getAllEcampagnes(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Ecampagnes");
        Page<Ecampagne> page = ecampagneService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ecampagnes/:id} : get the "id" ecampagne.
     *
     * @param id the id of the ecampagne to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ecampagne, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ecampagnes/{id}")
    public ResponseEntity<Ecampagne> getEcampagne(@PathVariable Long id) {
        log.debug("REST request to get Ecampagne : {}", id);
        Optional<Ecampagne> ecampagne = ecampagneService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ecampagne);
    }

    /**
     * {@code DELETE  /ecampagnes/:id} : delete the "id" ecampagne.
     *
     * @param id the id of the ecampagne to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ecampagnes/{id}")
    public ResponseEntity<Void> deleteEcampagne(@PathVariable Long id) {
        log.debug("REST request to delete Ecampagne : {}", id);
        ecampagneService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/ecampagnes?query=:query} : search for the ecampagne corresponding
     * to the query.
     *
     * @param query the query of the ecampagne search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/ecampagnes")
    public ResponseEntity<List<Ecampagne>> searchEcampagnes(
        @RequestParam String query,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Ecampagnes for query {}", query);
        Page<Ecampagne> page = ecampagneService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
