package dsi.sea.sgess.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import dsi.sea.sgess.domain.Eeventualite;
import dsi.sea.sgess.repository.EeventualiteRepository;
import dsi.sea.sgess.service.EeventualiteService;
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
 * REST controller for managing {@link dsi.sea.sgess.domain.Eeventualite}.
 */
@RestController
@RequestMapping("/api")
public class EeventualiteResource {

    private final Logger log = LoggerFactory.getLogger(EeventualiteResource.class);

    private static final String ENTITY_NAME = "eeventualite";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EeventualiteService eeventualiteService;

    private final EeventualiteRepository eeventualiteRepository;

    public EeventualiteResource(EeventualiteService eeventualiteService, EeventualiteRepository eeventualiteRepository) {
        this.eeventualiteService = eeventualiteService;
        this.eeventualiteRepository = eeventualiteRepository;
    }

    /**
     * {@code POST  /eeventualites} : Create a new eeventualite.
     *
     * @param eeventualite the eeventualite to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eeventualite, or with status {@code 400 (Bad Request)} if the eeventualite has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/eeventualites")
    public ResponseEntity<Eeventualite> createEeventualite(@RequestBody Eeventualite eeventualite) throws URISyntaxException {
        log.debug("REST request to save Eeventualite : {}", eeventualite);
        if (eeventualite.getId() != null) {
            throw new BadRequestAlertException("A new eeventualite cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Eeventualite result = eeventualiteService.save(eeventualite);
        return ResponseEntity
            .created(new URI("/api/eeventualites/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /eeventualites/:id} : Updates an existing eeventualite.
     *
     * @param id the id of the eeventualite to save.
     * @param eeventualite the eeventualite to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eeventualite,
     * or with status {@code 400 (Bad Request)} if the eeventualite is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eeventualite couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/eeventualites/{id}")
    public ResponseEntity<Eeventualite> updateEeventualite(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Eeventualite eeventualite
    ) throws URISyntaxException {
        log.debug("REST request to update Eeventualite : {}, {}", id, eeventualite);
        if (eeventualite.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eeventualite.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eeventualiteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Eeventualite result = eeventualiteService.update(eeventualite);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eeventualite.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /eeventualites/:id} : Partial updates given fields of an existing eeventualite, field will ignore if it is null
     *
     * @param id the id of the eeventualite to save.
     * @param eeventualite the eeventualite to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eeventualite,
     * or with status {@code 400 (Bad Request)} if the eeventualite is not valid,
     * or with status {@code 404 (Not Found)} if the eeventualite is not found,
     * or with status {@code 500 (Internal Server Error)} if the eeventualite couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/eeventualites/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Eeventualite> partialUpdateEeventualite(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Eeventualite eeventualite
    ) throws URISyntaxException {
        log.debug("REST request to partial update Eeventualite partially : {}, {}", id, eeventualite);
        if (eeventualite.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eeventualite.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eeventualiteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Eeventualite> result = eeventualiteService.partialUpdate(eeventualite);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eeventualite.getId().toString())
        );
    }

    /**
     * {@code GET  /eeventualites} : get all the eeventualites.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eeventualites in body.
     */
    @GetMapping("/eeventualites")
    public ResponseEntity<List<Eeventualite>> getAllEeventualites(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Eeventualites");
        Page<Eeventualite> page = eeventualiteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /eeventualites/:id} : get the "id" eeventualite.
     *
     * @param id the id of the eeventualite to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eeventualite, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/eeventualites/{id}")
    public ResponseEntity<Eeventualite> getEeventualite(@PathVariable Long id) {
        log.debug("REST request to get Eeventualite : {}", id);
        Optional<Eeventualite> eeventualite = eeventualiteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eeventualite);
    }

    /**
     * {@code DELETE  /eeventualites/:id} : delete the "id" eeventualite.
     *
     * @param id the id of the eeventualite to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/eeventualites/{id}")
    public ResponseEntity<Void> deleteEeventualite(@PathVariable Long id) {
        log.debug("REST request to delete Eeventualite : {}", id);
        eeventualiteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/eeventualites?query=:query} : search for the eeventualite corresponding
     * to the query.
     *
     * @param query the query of the eeventualite search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/eeventualites")
    public ResponseEntity<List<Eeventualite>> searchEeventualites(
        @RequestParam String query,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Eeventualites for query {}", query);
        Page<Eeventualite> page = eeventualiteService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
