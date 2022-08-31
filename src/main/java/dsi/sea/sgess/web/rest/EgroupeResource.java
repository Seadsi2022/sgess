package dsi.sea.sgess.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import dsi.sea.sgess.domain.Egroupe;
import dsi.sea.sgess.repository.EgroupeRepository;
import dsi.sea.sgess.service.EgroupeService;
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
 * REST controller for managing {@link dsi.sea.sgess.domain.Egroupe}.
 */
@RestController
@RequestMapping("/api")
public class EgroupeResource {

    private final Logger log = LoggerFactory.getLogger(EgroupeResource.class);

    private static final String ENTITY_NAME = "egroupe";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EgroupeService egroupeService;

    private final EgroupeRepository egroupeRepository;

    public EgroupeResource(EgroupeService egroupeService, EgroupeRepository egroupeRepository) {
        this.egroupeService = egroupeService;
        this.egroupeRepository = egroupeRepository;
    }

    /**
     * {@code POST  /egroupes} : Create a new egroupe.
     *
     * @param egroupe the egroupe to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new egroupe, or with status {@code 400 (Bad Request)} if the egroupe has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/egroupes")
    public ResponseEntity<Egroupe> createEgroupe(@RequestBody Egroupe egroupe) throws URISyntaxException {
        log.debug("REST request to save Egroupe : {}", egroupe);
        if (egroupe.getId() != null) {
            throw new BadRequestAlertException("A new egroupe cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Egroupe result = egroupeService.save(egroupe);
        return ResponseEntity
            .created(new URI("/api/egroupes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /egroupes/:id} : Updates an existing egroupe.
     *
     * @param id the id of the egroupe to save.
     * @param egroupe the egroupe to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated egroupe,
     * or with status {@code 400 (Bad Request)} if the egroupe is not valid,
     * or with status {@code 500 (Internal Server Error)} if the egroupe couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/egroupes/{id}")
    public ResponseEntity<Egroupe> updateEgroupe(@PathVariable(value = "id", required = false) final Long id, @RequestBody Egroupe egroupe)
        throws URISyntaxException {
        log.debug("REST request to update Egroupe : {}, {}", id, egroupe);
        if (egroupe.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, egroupe.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!egroupeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Egroupe result = egroupeService.update(egroupe);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, egroupe.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /egroupes/:id} : Partial updates given fields of an existing egroupe, field will ignore if it is null
     *
     * @param id the id of the egroupe to save.
     * @param egroupe the egroupe to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated egroupe,
     * or with status {@code 400 (Bad Request)} if the egroupe is not valid,
     * or with status {@code 404 (Not Found)} if the egroupe is not found,
     * or with status {@code 500 (Internal Server Error)} if the egroupe couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/egroupes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Egroupe> partialUpdateEgroupe(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Egroupe egroupe
    ) throws URISyntaxException {
        log.debug("REST request to partial update Egroupe partially : {}, {}", id, egroupe);
        if (egroupe.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, egroupe.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!egroupeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Egroupe> result = egroupeService.partialUpdate(egroupe);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, egroupe.getId().toString())
        );
    }

    /**
     * {@code GET  /egroupes} : get all the egroupes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of egroupes in body.
     */
    @GetMapping("/egroupes")
    public ResponseEntity<List<Egroupe>> getAllEgroupes(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Egroupes");
        Page<Egroupe> page = egroupeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /egroupes/:id} : get the "id" egroupe.
     *
     * @param id the id of the egroupe to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the egroupe, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/egroupes/{id}")
    public ResponseEntity<Egroupe> getEgroupe(@PathVariable Long id) {
        log.debug("REST request to get Egroupe : {}", id);
        Optional<Egroupe> egroupe = egroupeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(egroupe);
    }

    /**
     * {@code DELETE  /egroupes/:id} : delete the "id" egroupe.
     *
     * @param id the id of the egroupe to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/egroupes/{id}")
    public ResponseEntity<Void> deleteEgroupe(@PathVariable Long id) {
        log.debug("REST request to delete Egroupe : {}", id);
        egroupeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/egroupes?query=:query} : search for the egroupe corresponding
     * to the query.
     *
     * @param query the query of the egroupe search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/egroupes")
    public ResponseEntity<List<Egroupe>> searchEgroupes(
        @RequestParam String query,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Egroupes for query {}", query);
        Page<Egroupe> page = egroupeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
