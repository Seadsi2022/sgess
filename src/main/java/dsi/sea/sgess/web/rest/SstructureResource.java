package dsi.sea.sgess.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import dsi.sea.sgess.domain.Sstructure;
import dsi.sea.sgess.repository.SstructureRepository;
import dsi.sea.sgess.service.SstructureService;
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
 * REST controller for managing {@link dsi.sea.sgess.domain.Sstructure}.
 */
@RestController
@RequestMapping("/api")
public class SstructureResource {

    private final Logger log = LoggerFactory.getLogger(SstructureResource.class);

    private static final String ENTITY_NAME = "sstructure";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SstructureService sstructureService;

    private final SstructureRepository sstructureRepository;

    public SstructureResource(SstructureService sstructureService, SstructureRepository sstructureRepository) {
        this.sstructureService = sstructureService;
        this.sstructureRepository = sstructureRepository;
    }

    /**
     * {@code POST  /sstructures} : Create a new sstructure.
     *
     * @param sstructure the sstructure to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sstructure, or with status {@code 400 (Bad Request)} if the sstructure has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sstructures")
    public ResponseEntity<Sstructure> createSstructure(@RequestBody Sstructure sstructure) throws URISyntaxException {
        log.debug("REST request to save Sstructure : {}", sstructure);
        if (sstructure.getId() != null) {
            throw new BadRequestAlertException("A new sstructure cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Sstructure result = sstructureService.save(sstructure);
        return ResponseEntity
            .created(new URI("/api/sstructures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sstructures/:id} : Updates an existing sstructure.
     *
     * @param id the id of the sstructure to save.
     * @param sstructure the sstructure to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sstructure,
     * or with status {@code 400 (Bad Request)} if the sstructure is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sstructure couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sstructures/{id}")
    public ResponseEntity<Sstructure> updateSstructure(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Sstructure sstructure
    ) throws URISyntaxException {
        log.debug("REST request to update Sstructure : {}, {}", id, sstructure);
        if (sstructure.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sstructure.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sstructureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Sstructure result = sstructureService.update(sstructure);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sstructure.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sstructures/:id} : Partial updates given fields of an existing sstructure, field will ignore if it is null
     *
     * @param id the id of the sstructure to save.
     * @param sstructure the sstructure to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sstructure,
     * or with status {@code 400 (Bad Request)} if the sstructure is not valid,
     * or with status {@code 404 (Not Found)} if the sstructure is not found,
     * or with status {@code 500 (Internal Server Error)} if the sstructure couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sstructures/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Sstructure> partialUpdateSstructure(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Sstructure sstructure
    ) throws URISyntaxException {
        log.debug("REST request to partial update Sstructure partially : {}, {}", id, sstructure);
        if (sstructure.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sstructure.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sstructureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Sstructure> result = sstructureService.partialUpdate(sstructure);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sstructure.getId().toString())
        );
    }

    /**
     * {@code GET  /sstructures} : get all the sstructures.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sstructures in body.
     */
    @GetMapping("/sstructures")
    public ResponseEntity<List<Sstructure>> getAllSstructures(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of Sstructures");
        Page<Sstructure> page;
        if (eagerload) {
            page = sstructureService.findAllWithEagerRelationships(pageable);
        } else {
            page = sstructureService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sstructures/:id} : get the "id" sstructure.
     *
     * @param id the id of the sstructure to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sstructure, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sstructures/{id}")
    public ResponseEntity<Sstructure> getSstructure(@PathVariable Long id) {
        log.debug("REST request to get Sstructure : {}", id);
        Optional<Sstructure> sstructure = sstructureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sstructure);
    }

    /**
     * {@code DELETE  /sstructures/:id} : delete the "id" sstructure.
     *
     * @param id the id of the sstructure to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sstructures/{id}")
    public ResponseEntity<Void> deleteSstructure(@PathVariable Long id) {
        log.debug("REST request to delete Sstructure : {}", id);
        sstructureService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/sstructures?query=:query} : search for the sstructure corresponding
     * to the query.
     *
     * @param query the query of the sstructure search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/sstructures")
    public ResponseEntity<List<Sstructure>> searchSstructures(
        @RequestParam String query,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Sstructures for query {}", query);
        Page<Sstructure> page = sstructureService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
