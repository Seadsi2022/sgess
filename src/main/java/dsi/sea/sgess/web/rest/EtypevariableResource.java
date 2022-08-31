package dsi.sea.sgess.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import dsi.sea.sgess.domain.Etypevariable;
import dsi.sea.sgess.repository.EtypevariableRepository;
import dsi.sea.sgess.service.EtypevariableService;
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
 * REST controller for managing {@link dsi.sea.sgess.domain.Etypevariable}.
 */
@RestController
@RequestMapping("/api")
public class EtypevariableResource {

    private final Logger log = LoggerFactory.getLogger(EtypevariableResource.class);

    private static final String ENTITY_NAME = "etypevariable";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EtypevariableService etypevariableService;

    private final EtypevariableRepository etypevariableRepository;

    public EtypevariableResource(EtypevariableService etypevariableService, EtypevariableRepository etypevariableRepository) {
        this.etypevariableService = etypevariableService;
        this.etypevariableRepository = etypevariableRepository;
    }

    /**
     * {@code POST  /etypevariables} : Create a new etypevariable.
     *
     * @param etypevariable the etypevariable to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new etypevariable, or with status {@code 400 (Bad Request)} if the etypevariable has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/etypevariables")
    public ResponseEntity<Etypevariable> createEtypevariable(@RequestBody Etypevariable etypevariable) throws URISyntaxException {
        log.debug("REST request to save Etypevariable : {}", etypevariable);
        if (etypevariable.getId() != null) {
            throw new BadRequestAlertException("A new etypevariable cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Etypevariable result = etypevariableService.save(etypevariable);
        return ResponseEntity
            .created(new URI("/api/etypevariables/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /etypevariables/:id} : Updates an existing etypevariable.
     *
     * @param id the id of the etypevariable to save.
     * @param etypevariable the etypevariable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated etypevariable,
     * or with status {@code 400 (Bad Request)} if the etypevariable is not valid,
     * or with status {@code 500 (Internal Server Error)} if the etypevariable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/etypevariables/{id}")
    public ResponseEntity<Etypevariable> updateEtypevariable(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Etypevariable etypevariable
    ) throws URISyntaxException {
        log.debug("REST request to update Etypevariable : {}, {}", id, etypevariable);
        if (etypevariable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, etypevariable.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!etypevariableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Etypevariable result = etypevariableService.update(etypevariable);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, etypevariable.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /etypevariables/:id} : Partial updates given fields of an existing etypevariable, field will ignore if it is null
     *
     * @param id the id of the etypevariable to save.
     * @param etypevariable the etypevariable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated etypevariable,
     * or with status {@code 400 (Bad Request)} if the etypevariable is not valid,
     * or with status {@code 404 (Not Found)} if the etypevariable is not found,
     * or with status {@code 500 (Internal Server Error)} if the etypevariable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/etypevariables/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Etypevariable> partialUpdateEtypevariable(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Etypevariable etypevariable
    ) throws URISyntaxException {
        log.debug("REST request to partial update Etypevariable partially : {}, {}", id, etypevariable);
        if (etypevariable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, etypevariable.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!etypevariableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Etypevariable> result = etypevariableService.partialUpdate(etypevariable);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, etypevariable.getId().toString())
        );
    }

    /**
     * {@code GET  /etypevariables} : get all the etypevariables.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of etypevariables in body.
     */
    @GetMapping("/etypevariables")
    public ResponseEntity<List<Etypevariable>> getAllEtypevariables(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Etypevariables");
        Page<Etypevariable> page = etypevariableService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /etypevariables/:id} : get the "id" etypevariable.
     *
     * @param id the id of the etypevariable to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the etypevariable, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/etypevariables/{id}")
    public ResponseEntity<Etypevariable> getEtypevariable(@PathVariable Long id) {
        log.debug("REST request to get Etypevariable : {}", id);
        Optional<Etypevariable> etypevariable = etypevariableService.findOne(id);
        return ResponseUtil.wrapOrNotFound(etypevariable);
    }

    /**
     * {@code DELETE  /etypevariables/:id} : delete the "id" etypevariable.
     *
     * @param id the id of the etypevariable to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/etypevariables/{id}")
    public ResponseEntity<Void> deleteEtypevariable(@PathVariable Long id) {
        log.debug("REST request to delete Etypevariable : {}", id);
        etypevariableService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/etypevariables?query=:query} : search for the etypevariable corresponding
     * to the query.
     *
     * @param query the query of the etypevariable search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/etypevariables")
    public ResponseEntity<List<Etypevariable>> searchEtypevariables(
        @RequestParam String query,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Etypevariables for query {}", query);
        Page<Etypevariable> page = etypevariableService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
