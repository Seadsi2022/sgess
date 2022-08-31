package dsi.sea.sgess.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import dsi.sea.sgess.domain.Eattributvariable;
import dsi.sea.sgess.repository.EattributvariableRepository;
import dsi.sea.sgess.service.EattributvariableService;
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
 * REST controller for managing {@link dsi.sea.sgess.domain.Eattributvariable}.
 */
@RestController
@RequestMapping("/api")
public class EattributvariableResource {

    private final Logger log = LoggerFactory.getLogger(EattributvariableResource.class);

    private static final String ENTITY_NAME = "eattributvariable";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EattributvariableService eattributvariableService;

    private final EattributvariableRepository eattributvariableRepository;

    public EattributvariableResource(
        EattributvariableService eattributvariableService,
        EattributvariableRepository eattributvariableRepository
    ) {
        this.eattributvariableService = eattributvariableService;
        this.eattributvariableRepository = eattributvariableRepository;
    }

    /**
     * {@code POST  /eattributvariables} : Create a new eattributvariable.
     *
     * @param eattributvariable the eattributvariable to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eattributvariable, or with status {@code 400 (Bad Request)} if the eattributvariable has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/eattributvariables")
    public ResponseEntity<Eattributvariable> createEattributvariable(@RequestBody Eattributvariable eattributvariable)
        throws URISyntaxException {
        log.debug("REST request to save Eattributvariable : {}", eattributvariable);
        if (eattributvariable.getId() != null) {
            throw new BadRequestAlertException("A new eattributvariable cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Eattributvariable result = eattributvariableService.save(eattributvariable);
        return ResponseEntity
            .created(new URI("/api/eattributvariables/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /eattributvariables/:id} : Updates an existing eattributvariable.
     *
     * @param id the id of the eattributvariable to save.
     * @param eattributvariable the eattributvariable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eattributvariable,
     * or with status {@code 400 (Bad Request)} if the eattributvariable is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eattributvariable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/eattributvariables/{id}")
    public ResponseEntity<Eattributvariable> updateEattributvariable(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Eattributvariable eattributvariable
    ) throws URISyntaxException {
        log.debug("REST request to update Eattributvariable : {}, {}", id, eattributvariable);
        if (eattributvariable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eattributvariable.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eattributvariableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Eattributvariable result = eattributvariableService.update(eattributvariable);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eattributvariable.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /eattributvariables/:id} : Partial updates given fields of an existing eattributvariable, field will ignore if it is null
     *
     * @param id the id of the eattributvariable to save.
     * @param eattributvariable the eattributvariable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eattributvariable,
     * or with status {@code 400 (Bad Request)} if the eattributvariable is not valid,
     * or with status {@code 404 (Not Found)} if the eattributvariable is not found,
     * or with status {@code 500 (Internal Server Error)} if the eattributvariable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/eattributvariables/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Eattributvariable> partialUpdateEattributvariable(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Eattributvariable eattributvariable
    ) throws URISyntaxException {
        log.debug("REST request to partial update Eattributvariable partially : {}, {}", id, eattributvariable);
        if (eattributvariable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eattributvariable.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eattributvariableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Eattributvariable> result = eattributvariableService.partialUpdate(eattributvariable);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eattributvariable.getId().toString())
        );
    }

    /**
     * {@code GET  /eattributvariables} : get all the eattributvariables.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eattributvariables in body.
     */
    @GetMapping("/eattributvariables")
    public ResponseEntity<List<Eattributvariable>> getAllEattributvariables(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of Eattributvariables");
        Page<Eattributvariable> page = eattributvariableService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /eattributvariables/:id} : get the "id" eattributvariable.
     *
     * @param id the id of the eattributvariable to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eattributvariable, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/eattributvariables/{id}")
    public ResponseEntity<Eattributvariable> getEattributvariable(@PathVariable Long id) {
        log.debug("REST request to get Eattributvariable : {}", id);
        Optional<Eattributvariable> eattributvariable = eattributvariableService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eattributvariable);
    }

    /**
     * {@code DELETE  /eattributvariables/:id} : delete the "id" eattributvariable.
     *
     * @param id the id of the eattributvariable to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/eattributvariables/{id}")
    public ResponseEntity<Void> deleteEattributvariable(@PathVariable Long id) {
        log.debug("REST request to delete Eattributvariable : {}", id);
        eattributvariableService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/eattributvariables?query=:query} : search for the eattributvariable corresponding
     * to the query.
     *
     * @param query the query of the eattributvariable search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/eattributvariables")
    public ResponseEntity<List<Eattributvariable>> searchEattributvariables(
        @RequestParam String query,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Eattributvariables for query {}", query);
        Page<Eattributvariable> page = eattributvariableService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
