package dsi.sea.sgess.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import dsi.sea.sgess.domain.Eeventualitevariable;
import dsi.sea.sgess.repository.EeventualitevariableRepository;
import dsi.sea.sgess.service.EeventualitevariableService;
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
 * REST controller for managing {@link dsi.sea.sgess.domain.Eeventualitevariable}.
 */
@RestController
@RequestMapping("/api")
public class EeventualitevariableResource {

    private final Logger log = LoggerFactory.getLogger(EeventualitevariableResource.class);

    private static final String ENTITY_NAME = "eeventualitevariable";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EeventualitevariableService eeventualitevariableService;

    private final EeventualitevariableRepository eeventualitevariableRepository;

    public EeventualitevariableResource(
        EeventualitevariableService eeventualitevariableService,
        EeventualitevariableRepository eeventualitevariableRepository
    ) {
        this.eeventualitevariableService = eeventualitevariableService;
        this.eeventualitevariableRepository = eeventualitevariableRepository;
    }

    /**
     * {@code POST  /eeventualitevariables} : Create a new eeventualitevariable.
     *
     * @param eeventualitevariable the eeventualitevariable to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eeventualitevariable, or with status {@code 400 (Bad Request)} if the eeventualitevariable has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/eeventualitevariables")
    public ResponseEntity<Eeventualitevariable> createEeventualitevariable(@RequestBody Eeventualitevariable eeventualitevariable)
        throws URISyntaxException {
        log.debug("REST request to save Eeventualitevariable : {}", eeventualitevariable);
        if (eeventualitevariable.getId() != null) {
            throw new BadRequestAlertException("A new eeventualitevariable cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Eeventualitevariable result = eeventualitevariableService.save(eeventualitevariable);
        return ResponseEntity
            .created(new URI("/api/eeventualitevariables/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /eeventualitevariables/:id} : Updates an existing eeventualitevariable.
     *
     * @param id the id of the eeventualitevariable to save.
     * @param eeventualitevariable the eeventualitevariable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eeventualitevariable,
     * or with status {@code 400 (Bad Request)} if the eeventualitevariable is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eeventualitevariable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/eeventualitevariables/{id}")
    public ResponseEntity<Eeventualitevariable> updateEeventualitevariable(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Eeventualitevariable eeventualitevariable
    ) throws URISyntaxException {
        log.debug("REST request to update Eeventualitevariable : {}, {}", id, eeventualitevariable);
        if (eeventualitevariable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eeventualitevariable.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eeventualitevariableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Eeventualitevariable result = eeventualitevariableService.update(eeventualitevariable);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eeventualitevariable.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /eeventualitevariables/:id} : Partial updates given fields of an existing eeventualitevariable, field will ignore if it is null
     *
     * @param id the id of the eeventualitevariable to save.
     * @param eeventualitevariable the eeventualitevariable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eeventualitevariable,
     * or with status {@code 400 (Bad Request)} if the eeventualitevariable is not valid,
     * or with status {@code 404 (Not Found)} if the eeventualitevariable is not found,
     * or with status {@code 500 (Internal Server Error)} if the eeventualitevariable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/eeventualitevariables/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Eeventualitevariable> partialUpdateEeventualitevariable(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Eeventualitevariable eeventualitevariable
    ) throws URISyntaxException {
        log.debug("REST request to partial update Eeventualitevariable partially : {}, {}", id, eeventualitevariable);
        if (eeventualitevariable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eeventualitevariable.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eeventualitevariableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Eeventualitevariable> result = eeventualitevariableService.partialUpdate(eeventualitevariable);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eeventualitevariable.getId().toString())
        );
    }

    /**
     * {@code GET  /eeventualitevariables} : get all the eeventualitevariables.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eeventualitevariables in body.
     */
    @GetMapping("/eeventualitevariables")
    public ResponseEntity<List<Eeventualitevariable>> getAllEeventualitevariables(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of Eeventualitevariables");
        Page<Eeventualitevariable> page = eeventualitevariableService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /eeventualitevariables/:id} : get the "id" eeventualitevariable.
     *
     * @param id the id of the eeventualitevariable to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eeventualitevariable, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/eeventualitevariables/{id}")
    public ResponseEntity<Eeventualitevariable> getEeventualitevariable(@PathVariable Long id) {
        log.debug("REST request to get Eeventualitevariable : {}", id);
        Optional<Eeventualitevariable> eeventualitevariable = eeventualitevariableService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eeventualitevariable);
    }

    /**
     * {@code DELETE  /eeventualitevariables/:id} : delete the "id" eeventualitevariable.
     *
     * @param id the id of the eeventualitevariable to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/eeventualitevariables/{id}")
    public ResponseEntity<Void> deleteEeventualitevariable(@PathVariable Long id) {
        log.debug("REST request to delete Eeventualitevariable : {}", id);
        eeventualitevariableService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/eeventualitevariables?query=:query} : search for the eeventualitevariable corresponding
     * to the query.
     *
     * @param query the query of the eeventualitevariable search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/eeventualitevariables")
    public ResponseEntity<List<Eeventualitevariable>> searchEeventualitevariables(
        @RequestParam String query,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Eeventualitevariables for query {}", query);
        Page<Eeventualitevariable> page = eeventualitevariableService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
