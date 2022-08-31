package dsi.sea.sgess.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import dsi.sea.sgess.domain.Scode;
import dsi.sea.sgess.repository.ScodeRepository;
import dsi.sea.sgess.service.ScodeService;
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
 * REST controller for managing {@link dsi.sea.sgess.domain.Scode}.
 */
@RestController
@RequestMapping("/api")
public class ScodeResource {

    private final Logger log = LoggerFactory.getLogger(ScodeResource.class);

    private static final String ENTITY_NAME = "scode";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ScodeService scodeService;

    private final ScodeRepository scodeRepository;

    public ScodeResource(ScodeService scodeService, ScodeRepository scodeRepository) {
        this.scodeService = scodeService;
        this.scodeRepository = scodeRepository;
    }

    /**
     * {@code POST  /scodes} : Create a new scode.
     *
     * @param scode the scode to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new scode, or with status {@code 400 (Bad Request)} if the scode has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/scodes")
    public ResponseEntity<Scode> createScode(@RequestBody Scode scode) throws URISyntaxException {
        log.debug("REST request to save Scode : {}", scode);
        if (scode.getId() != null) {
            throw new BadRequestAlertException("A new scode cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Scode result = scodeService.save(scode);
        return ResponseEntity
            .created(new URI("/api/scodes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /scodes/:id} : Updates an existing scode.
     *
     * @param id the id of the scode to save.
     * @param scode the scode to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated scode,
     * or with status {@code 400 (Bad Request)} if the scode is not valid,
     * or with status {@code 500 (Internal Server Error)} if the scode couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/scodes/{id}")
    public ResponseEntity<Scode> updateScode(@PathVariable(value = "id", required = false) final Long id, @RequestBody Scode scode)
        throws URISyntaxException {
        log.debug("REST request to update Scode : {}, {}", id, scode);
        if (scode.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, scode.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!scodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Scode result = scodeService.update(scode);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, scode.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /scodes/:id} : Partial updates given fields of an existing scode, field will ignore if it is null
     *
     * @param id the id of the scode to save.
     * @param scode the scode to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated scode,
     * or with status {@code 400 (Bad Request)} if the scode is not valid,
     * or with status {@code 404 (Not Found)} if the scode is not found,
     * or with status {@code 500 (Internal Server Error)} if the scode couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/scodes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Scode> partialUpdateScode(@PathVariable(value = "id", required = false) final Long id, @RequestBody Scode scode)
        throws URISyntaxException {
        log.debug("REST request to partial update Scode partially : {}, {}", id, scode);
        if (scode.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, scode.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!scodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Scode> result = scodeService.partialUpdate(scode);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, scode.getId().toString())
        );
    }

    /**
     * {@code GET  /scodes} : get all the scodes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of scodes in body.
     */
    @GetMapping("/scodes")
    public ResponseEntity<List<Scode>> getAllScodes(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Scodes");
        Page<Scode> page = scodeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /scodes/:id} : get the "id" scode.
     *
     * @param id the id of the scode to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the scode, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/scodes/{id}")
    public ResponseEntity<Scode> getScode(@PathVariable Long id) {
        log.debug("REST request to get Scode : {}", id);
        Optional<Scode> scode = scodeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(scode);
    }

    /**
     * {@code DELETE  /scodes/:id} : delete the "id" scode.
     *
     * @param id the id of the scode to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/scodes/{id}")
    public ResponseEntity<Void> deleteScode(@PathVariable Long id) {
        log.debug("REST request to delete Scode : {}", id);
        scodeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/scodes?query=:query} : search for the scode corresponding
     * to the query.
     *
     * @param query the query of the scode search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/scodes")
    public ResponseEntity<List<Scode>> searchScodes(
        @RequestParam String query,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Scodes for query {}", query);
        Page<Scode> page = scodeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
