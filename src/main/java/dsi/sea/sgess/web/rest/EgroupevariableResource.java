package dsi.sea.sgess.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import dsi.sea.sgess.domain.Egroupevariable;
import dsi.sea.sgess.repository.EgroupevariableRepository;
import dsi.sea.sgess.service.EgroupevariableService;
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
 * REST controller for managing {@link dsi.sea.sgess.domain.Egroupevariable}.
 */
@RestController
@RequestMapping("/api")
public class EgroupevariableResource {

    private final Logger log = LoggerFactory.getLogger(EgroupevariableResource.class);

    private static final String ENTITY_NAME = "egroupevariable";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EgroupevariableService egroupevariableService;

    private final EgroupevariableRepository egroupevariableRepository;

    public EgroupevariableResource(EgroupevariableService egroupevariableService, EgroupevariableRepository egroupevariableRepository) {
        this.egroupevariableService = egroupevariableService;
        this.egroupevariableRepository = egroupevariableRepository;
    }

    /**
     * {@code POST  /egroupevariables} : Create a new egroupevariable.
     *
     * @param egroupevariable the egroupevariable to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new egroupevariable, or with status {@code 400 (Bad Request)} if the egroupevariable has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/egroupevariables")
    public ResponseEntity<Egroupevariable> createEgroupevariable(@RequestBody Egroupevariable egroupevariable) throws URISyntaxException {
        log.debug("REST request to save Egroupevariable : {}", egroupevariable);
        if (egroupevariable.getId() != null) {
            throw new BadRequestAlertException("A new egroupevariable cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Egroupevariable result = egroupevariableService.save(egroupevariable);
        return ResponseEntity
            .created(new URI("/api/egroupevariables/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /egroupevariables/:id} : Updates an existing egroupevariable.
     *
     * @param id the id of the egroupevariable to save.
     * @param egroupevariable the egroupevariable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated egroupevariable,
     * or with status {@code 400 (Bad Request)} if the egroupevariable is not valid,
     * or with status {@code 500 (Internal Server Error)} if the egroupevariable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/egroupevariables/{id}")
    public ResponseEntity<Egroupevariable> updateEgroupevariable(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Egroupevariable egroupevariable
    ) throws URISyntaxException {
        log.debug("REST request to update Egroupevariable : {}, {}", id, egroupevariable);
        if (egroupevariable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, egroupevariable.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!egroupevariableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Egroupevariable result = egroupevariableService.update(egroupevariable);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, egroupevariable.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /egroupevariables/:id} : Partial updates given fields of an existing egroupevariable, field will ignore if it is null
     *
     * @param id the id of the egroupevariable to save.
     * @param egroupevariable the egroupevariable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated egroupevariable,
     * or with status {@code 400 (Bad Request)} if the egroupevariable is not valid,
     * or with status {@code 404 (Not Found)} if the egroupevariable is not found,
     * or with status {@code 500 (Internal Server Error)} if the egroupevariable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/egroupevariables/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Egroupevariable> partialUpdateEgroupevariable(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Egroupevariable egroupevariable
    ) throws URISyntaxException {
        log.debug("REST request to partial update Egroupevariable partially : {}, {}", id, egroupevariable);
        if (egroupevariable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, egroupevariable.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!egroupevariableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Egroupevariable> result = egroupevariableService.partialUpdate(egroupevariable);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, egroupevariable.getId().toString())
        );
    }

    /**
     * {@code GET  /egroupevariables} : get all the egroupevariables.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of egroupevariables in body.
     */
    @GetMapping("/egroupevariables")
    public ResponseEntity<List<Egroupevariable>> getAllEgroupevariables(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Egroupevariables");
        Page<Egroupevariable> page = egroupevariableService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /egroupevariables/:id} : get the "id" egroupevariable.
     *
     * @param id the id of the egroupevariable to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the egroupevariable, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/egroupevariables/{id}")
    public ResponseEntity<Egroupevariable> getEgroupevariable(@PathVariable Long id) {
        log.debug("REST request to get Egroupevariable : {}", id);
        Optional<Egroupevariable> egroupevariable = egroupevariableService.findOne(id);
        return ResponseUtil.wrapOrNotFound(egroupevariable);
    }

    /**
     * {@code DELETE  /egroupevariables/:id} : delete the "id" egroupevariable.
     *
     * @param id the id of the egroupevariable to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/egroupevariables/{id}")
    public ResponseEntity<Void> deleteEgroupevariable(@PathVariable Long id) {
        log.debug("REST request to delete Egroupevariable : {}", id);
        egroupevariableService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/egroupevariables?query=:query} : search for the egroupevariable corresponding
     * to the query.
     *
     * @param query the query of the egroupevariable search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/egroupevariables")
    public ResponseEntity<List<Egroupevariable>> searchEgroupevariables(
        @RequestParam String query,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Egroupevariables for query {}", query);
        Page<Egroupevariable> page = egroupevariableService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
