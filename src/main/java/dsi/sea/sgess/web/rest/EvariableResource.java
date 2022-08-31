package dsi.sea.sgess.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import dsi.sea.sgess.domain.Evariable;
import dsi.sea.sgess.repository.EvariableRepository;
import dsi.sea.sgess.service.EvariableService;
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
 * REST controller for managing {@link dsi.sea.sgess.domain.Evariable}.
 */
@RestController
@RequestMapping("/api")
public class EvariableResource {

    private final Logger log = LoggerFactory.getLogger(EvariableResource.class);

    private static final String ENTITY_NAME = "evariable";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EvariableService evariableService;

    private final EvariableRepository evariableRepository;

    public EvariableResource(EvariableService evariableService, EvariableRepository evariableRepository) {
        this.evariableService = evariableService;
        this.evariableRepository = evariableRepository;
    }

    /**
     * {@code POST  /evariables} : Create a new evariable.
     *
     * @param evariable the evariable to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new evariable, or with status {@code 400 (Bad Request)} if the evariable has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/evariables")
    public ResponseEntity<Evariable> createEvariable(@RequestBody Evariable evariable) throws URISyntaxException {
        log.debug("REST request to save Evariable : {}", evariable);
        if (evariable.getId() != null) {
            throw new BadRequestAlertException("A new evariable cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Evariable result = evariableService.save(evariable);
        return ResponseEntity
            .created(new URI("/api/evariables/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /evariables/:id} : Updates an existing evariable.
     *
     * @param id the id of the evariable to save.
     * @param evariable the evariable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated evariable,
     * or with status {@code 400 (Bad Request)} if the evariable is not valid,
     * or with status {@code 500 (Internal Server Error)} if the evariable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/evariables/{id}")
    public ResponseEntity<Evariable> updateEvariable(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Evariable evariable
    ) throws URISyntaxException {
        log.debug("REST request to update Evariable : {}, {}", id, evariable);
        if (evariable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, evariable.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!evariableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Evariable result = evariableService.update(evariable);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, evariable.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /evariables/:id} : Partial updates given fields of an existing evariable, field will ignore if it is null
     *
     * @param id the id of the evariable to save.
     * @param evariable the evariable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated evariable,
     * or with status {@code 400 (Bad Request)} if the evariable is not valid,
     * or with status {@code 404 (Not Found)} if the evariable is not found,
     * or with status {@code 500 (Internal Server Error)} if the evariable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/evariables/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Evariable> partialUpdateEvariable(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Evariable evariable
    ) throws URISyntaxException {
        log.debug("REST request to partial update Evariable partially : {}, {}", id, evariable);
        if (evariable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, evariable.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!evariableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Evariable> result = evariableService.partialUpdate(evariable);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, evariable.getId().toString())
        );
    }

    /**
     * {@code GET  /evariables} : get all the evariables.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of evariables in body.
     */
    @GetMapping("/evariables")
    public ResponseEntity<List<Evariable>> getAllEvariables(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Evariables");
        Page<Evariable> page = evariableService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /evariables/:id} : get the "id" evariable.
     *
     * @param id the id of the evariable to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the evariable, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/evariables/{id}")
    public ResponseEntity<Evariable> getEvariable(@PathVariable Long id) {
        log.debug("REST request to get Evariable : {}", id);
        Optional<Evariable> evariable = evariableService.findOne(id);
        return ResponseUtil.wrapOrNotFound(evariable);
    }

    /**
     * {@code DELETE  /evariables/:id} : delete the "id" evariable.
     *
     * @param id the id of the evariable to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/evariables/{id}")
    public ResponseEntity<Void> deleteEvariable(@PathVariable Long id) {
        log.debug("REST request to delete Evariable : {}", id);
        evariableService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/evariables?query=:query} : search for the evariable corresponding
     * to the query.
     *
     * @param query the query of the evariable search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/evariables")
    public ResponseEntity<List<Evariable>> searchEvariables(
        @RequestParam String query,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Evariables for query {}", query);
        Page<Evariable> page = evariableService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
