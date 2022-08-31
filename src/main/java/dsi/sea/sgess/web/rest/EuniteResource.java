package dsi.sea.sgess.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import dsi.sea.sgess.domain.Eunite;
import dsi.sea.sgess.repository.EuniteRepository;
import dsi.sea.sgess.service.EuniteService;
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
 * REST controller for managing {@link dsi.sea.sgess.domain.Eunite}.
 */
@RestController
@RequestMapping("/api")
public class EuniteResource {

    private final Logger log = LoggerFactory.getLogger(EuniteResource.class);

    private static final String ENTITY_NAME = "eunite";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EuniteService euniteService;

    private final EuniteRepository euniteRepository;

    public EuniteResource(EuniteService euniteService, EuniteRepository euniteRepository) {
        this.euniteService = euniteService;
        this.euniteRepository = euniteRepository;
    }

    /**
     * {@code POST  /eunites} : Create a new eunite.
     *
     * @param eunite the eunite to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eunite, or with status {@code 400 (Bad Request)} if the eunite has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/eunites")
    public ResponseEntity<Eunite> createEunite(@RequestBody Eunite eunite) throws URISyntaxException {
        log.debug("REST request to save Eunite : {}", eunite);
        if (eunite.getId() != null) {
            throw new BadRequestAlertException("A new eunite cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Eunite result = euniteService.save(eunite);
        return ResponseEntity
            .created(new URI("/api/eunites/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /eunites/:id} : Updates an existing eunite.
     *
     * @param id the id of the eunite to save.
     * @param eunite the eunite to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eunite,
     * or with status {@code 400 (Bad Request)} if the eunite is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eunite couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/eunites/{id}")
    public ResponseEntity<Eunite> updateEunite(@PathVariable(value = "id", required = false) final Long id, @RequestBody Eunite eunite)
        throws URISyntaxException {
        log.debug("REST request to update Eunite : {}, {}", id, eunite);
        if (eunite.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eunite.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!euniteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Eunite result = euniteService.update(eunite);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eunite.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /eunites/:id} : Partial updates given fields of an existing eunite, field will ignore if it is null
     *
     * @param id the id of the eunite to save.
     * @param eunite the eunite to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eunite,
     * or with status {@code 400 (Bad Request)} if the eunite is not valid,
     * or with status {@code 404 (Not Found)} if the eunite is not found,
     * or with status {@code 500 (Internal Server Error)} if the eunite couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/eunites/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Eunite> partialUpdateEunite(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Eunite eunite
    ) throws URISyntaxException {
        log.debug("REST request to partial update Eunite partially : {}, {}", id, eunite);
        if (eunite.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eunite.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!euniteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Eunite> result = euniteService.partialUpdate(eunite);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eunite.getId().toString())
        );
    }

    /**
     * {@code GET  /eunites} : get all the eunites.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eunites in body.
     */
    @GetMapping("/eunites")
    public ResponseEntity<List<Eunite>> getAllEunites(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Eunites");
        Page<Eunite> page = euniteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /eunites/:id} : get the "id" eunite.
     *
     * @param id the id of the eunite to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eunite, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/eunites/{id}")
    public ResponseEntity<Eunite> getEunite(@PathVariable Long id) {
        log.debug("REST request to get Eunite : {}", id);
        Optional<Eunite> eunite = euniteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eunite);
    }

    /**
     * {@code DELETE  /eunites/:id} : delete the "id" eunite.
     *
     * @param id the id of the eunite to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/eunites/{id}")
    public ResponseEntity<Void> deleteEunite(@PathVariable Long id) {
        log.debug("REST request to delete Eunite : {}", id);
        euniteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/eunites?query=:query} : search for the eunite corresponding
     * to the query.
     *
     * @param query the query of the eunite search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/eunites")
    public ResponseEntity<List<Eunite>> searchEunites(
        @RequestParam String query,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Eunites for query {}", query);
        Page<Eunite> page = euniteService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
