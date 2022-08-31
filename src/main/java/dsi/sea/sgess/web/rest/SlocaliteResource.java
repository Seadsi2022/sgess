package dsi.sea.sgess.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import dsi.sea.sgess.domain.Slocalite;
import dsi.sea.sgess.repository.SlocaliteRepository;
import dsi.sea.sgess.service.SlocaliteService;
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
 * REST controller for managing {@link dsi.sea.sgess.domain.Slocalite}.
 */
@RestController
@RequestMapping("/api")
public class SlocaliteResource {

    private final Logger log = LoggerFactory.getLogger(SlocaliteResource.class);

    private static final String ENTITY_NAME = "slocalite";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SlocaliteService slocaliteService;

    private final SlocaliteRepository slocaliteRepository;

    public SlocaliteResource(SlocaliteService slocaliteService, SlocaliteRepository slocaliteRepository) {
        this.slocaliteService = slocaliteService;
        this.slocaliteRepository = slocaliteRepository;
    }

    /**
     * {@code POST  /slocalites} : Create a new slocalite.
     *
     * @param slocalite the slocalite to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new slocalite, or with status {@code 400 (Bad Request)} if the slocalite has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/slocalites")
    public ResponseEntity<Slocalite> createSlocalite(@RequestBody Slocalite slocalite) throws URISyntaxException {
        log.debug("REST request to save Slocalite : {}", slocalite);
        if (slocalite.getId() != null) {
            throw new BadRequestAlertException("A new slocalite cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Slocalite result = slocaliteService.save(slocalite);
        return ResponseEntity
            .created(new URI("/api/slocalites/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /slocalites/:id} : Updates an existing slocalite.
     *
     * @param id the id of the slocalite to save.
     * @param slocalite the slocalite to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated slocalite,
     * or with status {@code 400 (Bad Request)} if the slocalite is not valid,
     * or with status {@code 500 (Internal Server Error)} if the slocalite couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/slocalites/{id}")
    public ResponseEntity<Slocalite> updateSlocalite(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Slocalite slocalite
    ) throws URISyntaxException {
        log.debug("REST request to update Slocalite : {}, {}", id, slocalite);
        if (slocalite.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, slocalite.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!slocaliteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Slocalite result = slocaliteService.update(slocalite);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, slocalite.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /slocalites/:id} : Partial updates given fields of an existing slocalite, field will ignore if it is null
     *
     * @param id the id of the slocalite to save.
     * @param slocalite the slocalite to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated slocalite,
     * or with status {@code 400 (Bad Request)} if the slocalite is not valid,
     * or with status {@code 404 (Not Found)} if the slocalite is not found,
     * or with status {@code 500 (Internal Server Error)} if the slocalite couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/slocalites/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Slocalite> partialUpdateSlocalite(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Slocalite slocalite
    ) throws URISyntaxException {
        log.debug("REST request to partial update Slocalite partially : {}, {}", id, slocalite);
        if (slocalite.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, slocalite.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!slocaliteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Slocalite> result = slocaliteService.partialUpdate(slocalite);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, slocalite.getId().toString())
        );
    }

    /**
     * {@code GET  /slocalites} : get all the slocalites.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of slocalites in body.
     */
    @GetMapping("/slocalites")
    public ResponseEntity<List<Slocalite>> getAllSlocalites(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Slocalites");
        Page<Slocalite> page = slocaliteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /slocalites/:id} : get the "id" slocalite.
     *
     * @param id the id of the slocalite to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the slocalite, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/slocalites/{id}")
    public ResponseEntity<Slocalite> getSlocalite(@PathVariable Long id) {
        log.debug("REST request to get Slocalite : {}", id);
        Optional<Slocalite> slocalite = slocaliteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(slocalite);
    }

    /**
     * {@code DELETE  /slocalites/:id} : delete the "id" slocalite.
     *
     * @param id the id of the slocalite to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/slocalites/{id}")
    public ResponseEntity<Void> deleteSlocalite(@PathVariable Long id) {
        log.debug("REST request to delete Slocalite : {}", id);
        slocaliteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/slocalites?query=:query} : search for the slocalite corresponding
     * to the query.
     *
     * @param query the query of the slocalite search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/slocalites")
    public ResponseEntity<List<Slocalite>> searchSlocalites(
        @RequestParam String query,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Slocalites for query {}", query);
        Page<Slocalite> page = slocaliteService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
