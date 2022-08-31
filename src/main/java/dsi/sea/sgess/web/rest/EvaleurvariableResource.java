package dsi.sea.sgess.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import dsi.sea.sgess.domain.Evaleurvariable;
import dsi.sea.sgess.repository.EvaleurvariableRepository;
import dsi.sea.sgess.service.EvaleurvariableService;
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
 * REST controller for managing {@link dsi.sea.sgess.domain.Evaleurvariable}.
 */
@RestController
@RequestMapping("/api")
public class EvaleurvariableResource {

    private final Logger log = LoggerFactory.getLogger(EvaleurvariableResource.class);

    private static final String ENTITY_NAME = "evaleurvariable";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EvaleurvariableService evaleurvariableService;

    private final EvaleurvariableRepository evaleurvariableRepository;

    public EvaleurvariableResource(EvaleurvariableService evaleurvariableService, EvaleurvariableRepository evaleurvariableRepository) {
        this.evaleurvariableService = evaleurvariableService;
        this.evaleurvariableRepository = evaleurvariableRepository;
    }

    /**
     * {@code POST  /evaleurvariables} : Create a new evaleurvariable.
     *
     * @param evaleurvariable the evaleurvariable to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new evaleurvariable, or with status {@code 400 (Bad Request)} if the evaleurvariable has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/evaleurvariables")
    public ResponseEntity<Evaleurvariable> createEvaleurvariable(@RequestBody Evaleurvariable evaleurvariable) throws URISyntaxException {
        log.debug("REST request to save Evaleurvariable : {}", evaleurvariable);
        if (evaleurvariable.getId() != null) {
            throw new BadRequestAlertException("A new evaleurvariable cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Evaleurvariable result = evaleurvariableService.save(evaleurvariable);
        return ResponseEntity
            .created(new URI("/api/evaleurvariables/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /evaleurvariables/:id} : Updates an existing evaleurvariable.
     *
     * @param id the id of the evaleurvariable to save.
     * @param evaleurvariable the evaleurvariable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated evaleurvariable,
     * or with status {@code 400 (Bad Request)} if the evaleurvariable is not valid,
     * or with status {@code 500 (Internal Server Error)} if the evaleurvariable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/evaleurvariables/{id}")
    public ResponseEntity<Evaleurvariable> updateEvaleurvariable(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Evaleurvariable evaleurvariable
    ) throws URISyntaxException {
        log.debug("REST request to update Evaleurvariable : {}, {}", id, evaleurvariable);
        if (evaleurvariable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, evaleurvariable.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!evaleurvariableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Evaleurvariable result = evaleurvariableService.update(evaleurvariable);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, evaleurvariable.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /evaleurvariables/:id} : Partial updates given fields of an existing evaleurvariable, field will ignore if it is null
     *
     * @param id the id of the evaleurvariable to save.
     * @param evaleurvariable the evaleurvariable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated evaleurvariable,
     * or with status {@code 400 (Bad Request)} if the evaleurvariable is not valid,
     * or with status {@code 404 (Not Found)} if the evaleurvariable is not found,
     * or with status {@code 500 (Internal Server Error)} if the evaleurvariable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/evaleurvariables/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Evaleurvariable> partialUpdateEvaleurvariable(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Evaleurvariable evaleurvariable
    ) throws URISyntaxException {
        log.debug("REST request to partial update Evaleurvariable partially : {}, {}", id, evaleurvariable);
        if (evaleurvariable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, evaleurvariable.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!evaleurvariableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Evaleurvariable> result = evaleurvariableService.partialUpdate(evaleurvariable);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, evaleurvariable.getId().toString())
        );
    }

    /**
     * {@code GET  /evaleurvariables} : get all the evaleurvariables.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of evaleurvariables in body.
     */
    @GetMapping("/evaleurvariables")
    public ResponseEntity<List<Evaleurvariable>> getAllEvaleurvariables(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Evaleurvariables");
        Page<Evaleurvariable> page = evaleurvariableService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /evaleurvariables/:id} : get the "id" evaleurvariable.
     *
     * @param id the id of the evaleurvariable to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the evaleurvariable, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/evaleurvariables/{id}")
    public ResponseEntity<Evaleurvariable> getEvaleurvariable(@PathVariable Long id) {
        log.debug("REST request to get Evaleurvariable : {}", id);
        Optional<Evaleurvariable> evaleurvariable = evaleurvariableService.findOne(id);
        return ResponseUtil.wrapOrNotFound(evaleurvariable);
    }

    /**
     * {@code DELETE  /evaleurvariables/:id} : delete the "id" evaleurvariable.
     *
     * @param id the id of the evaleurvariable to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/evaleurvariables/{id}")
    public ResponseEntity<Void> deleteEvaleurvariable(@PathVariable Long id) {
        log.debug("REST request to delete Evaleurvariable : {}", id);
        evaleurvariableService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/evaleurvariables?query=:query} : search for the evaleurvariable corresponding
     * to the query.
     *
     * @param query the query of the evaleurvariable search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/evaleurvariables")
    public ResponseEntity<List<Evaleurvariable>> searchEvaleurvariables(
        @RequestParam String query,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Evaleurvariables for query {}", query);
        Page<Evaleurvariable> page = evaleurvariableService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
