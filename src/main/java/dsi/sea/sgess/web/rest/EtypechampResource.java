package dsi.sea.sgess.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import dsi.sea.sgess.domain.Etypechamp;
import dsi.sea.sgess.repository.EtypechampRepository;
import dsi.sea.sgess.service.EtypechampService;
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
 * REST controller for managing {@link dsi.sea.sgess.domain.Etypechamp}.
 */
@RestController
@RequestMapping("/api")
public class EtypechampResource {

    private final Logger log = LoggerFactory.getLogger(EtypechampResource.class);

    private static final String ENTITY_NAME = "etypechamp";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EtypechampService etypechampService;

    private final EtypechampRepository etypechampRepository;

    public EtypechampResource(EtypechampService etypechampService, EtypechampRepository etypechampRepository) {
        this.etypechampService = etypechampService;
        this.etypechampRepository = etypechampRepository;
    }

    /**
     * {@code POST  /etypechamps} : Create a new etypechamp.
     *
     * @param etypechamp the etypechamp to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new etypechamp, or with status {@code 400 (Bad Request)} if the etypechamp has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/etypechamps")
    public ResponseEntity<Etypechamp> createEtypechamp(@RequestBody Etypechamp etypechamp) throws URISyntaxException {
        log.debug("REST request to save Etypechamp : {}", etypechamp);
        if (etypechamp.getId() != null) {
            throw new BadRequestAlertException("A new etypechamp cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Etypechamp result = etypechampService.save(etypechamp);
        return ResponseEntity
            .created(new URI("/api/etypechamps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /etypechamps/:id} : Updates an existing etypechamp.
     *
     * @param id the id of the etypechamp to save.
     * @param etypechamp the etypechamp to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated etypechamp,
     * or with status {@code 400 (Bad Request)} if the etypechamp is not valid,
     * or with status {@code 500 (Internal Server Error)} if the etypechamp couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/etypechamps/{id}")
    public ResponseEntity<Etypechamp> updateEtypechamp(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Etypechamp etypechamp
    ) throws URISyntaxException {
        log.debug("REST request to update Etypechamp : {}, {}", id, etypechamp);
        if (etypechamp.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, etypechamp.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!etypechampRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Etypechamp result = etypechampService.update(etypechamp);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, etypechamp.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /etypechamps/:id} : Partial updates given fields of an existing etypechamp, field will ignore if it is null
     *
     * @param id the id of the etypechamp to save.
     * @param etypechamp the etypechamp to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated etypechamp,
     * or with status {@code 400 (Bad Request)} if the etypechamp is not valid,
     * or with status {@code 404 (Not Found)} if the etypechamp is not found,
     * or with status {@code 500 (Internal Server Error)} if the etypechamp couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/etypechamps/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Etypechamp> partialUpdateEtypechamp(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Etypechamp etypechamp
    ) throws URISyntaxException {
        log.debug("REST request to partial update Etypechamp partially : {}, {}", id, etypechamp);
        if (etypechamp.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, etypechamp.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!etypechampRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Etypechamp> result = etypechampService.partialUpdate(etypechamp);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, etypechamp.getId().toString())
        );
    }

    /**
     * {@code GET  /etypechamps} : get all the etypechamps.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of etypechamps in body.
     */
    @GetMapping("/etypechamps")
    public ResponseEntity<List<Etypechamp>> getAllEtypechamps(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of Etypechamps");
        Page<Etypechamp> page;
        if (eagerload) {
            page = etypechampService.findAllWithEagerRelationships(pageable);
        } else {
            page = etypechampService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /etypechamps/:id} : get the "id" etypechamp.
     *
     * @param id the id of the etypechamp to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the etypechamp, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/etypechamps/{id}")
    public ResponseEntity<Etypechamp> getEtypechamp(@PathVariable Long id) {
        log.debug("REST request to get Etypechamp : {}", id);
        Optional<Etypechamp> etypechamp = etypechampService.findOne(id);
        return ResponseUtil.wrapOrNotFound(etypechamp);
    }

    /**
     * {@code DELETE  /etypechamps/:id} : delete the "id" etypechamp.
     *
     * @param id the id of the etypechamp to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/etypechamps/{id}")
    public ResponseEntity<Void> deleteEtypechamp(@PathVariable Long id) {
        log.debug("REST request to delete Etypechamp : {}", id);
        etypechampService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/etypechamps?query=:query} : search for the etypechamp corresponding
     * to the query.
     *
     * @param query the query of the etypechamp search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/etypechamps")
    public ResponseEntity<List<Etypechamp>> searchEtypechamps(
        @RequestParam String query,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Etypechamps for query {}", query);
        Page<Etypechamp> page = etypechampService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
