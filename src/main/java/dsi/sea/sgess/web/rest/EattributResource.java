package dsi.sea.sgess.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import dsi.sea.sgess.domain.Eattribut;
import dsi.sea.sgess.repository.EattributRepository;
import dsi.sea.sgess.service.EattributService;
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
 * REST controller for managing {@link dsi.sea.sgess.domain.Eattribut}.
 */
@RestController
@RequestMapping("/api")
public class EattributResource {

    private final Logger log = LoggerFactory.getLogger(EattributResource.class);

    private static final String ENTITY_NAME = "eattribut";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EattributService eattributService;

    private final EattributRepository eattributRepository;

    public EattributResource(EattributService eattributService, EattributRepository eattributRepository) {
        this.eattributService = eattributService;
        this.eattributRepository = eattributRepository;
    }

    /**
     * {@code POST  /eattributs} : Create a new eattribut.
     *
     * @param eattribut the eattribut to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eattribut, or with status {@code 400 (Bad Request)} if the eattribut has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/eattributs")
    public ResponseEntity<Eattribut> createEattribut(@RequestBody Eattribut eattribut) throws URISyntaxException {
        log.debug("REST request to save Eattribut : {}", eattribut);
        if (eattribut.getId() != null) {
            throw new BadRequestAlertException("A new eattribut cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Eattribut result = eattributService.save(eattribut);
        return ResponseEntity
            .created(new URI("/api/eattributs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /eattributs/:id} : Updates an existing eattribut.
     *
     * @param id the id of the eattribut to save.
     * @param eattribut the eattribut to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eattribut,
     * or with status {@code 400 (Bad Request)} if the eattribut is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eattribut couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/eattributs/{id}")
    public ResponseEntity<Eattribut> updateEattribut(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Eattribut eattribut
    ) throws URISyntaxException {
        log.debug("REST request to update Eattribut : {}, {}", id, eattribut);
        if (eattribut.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eattribut.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eattributRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Eattribut result = eattributService.update(eattribut);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eattribut.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /eattributs/:id} : Partial updates given fields of an existing eattribut, field will ignore if it is null
     *
     * @param id the id of the eattribut to save.
     * @param eattribut the eattribut to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eattribut,
     * or with status {@code 400 (Bad Request)} if the eattribut is not valid,
     * or with status {@code 404 (Not Found)} if the eattribut is not found,
     * or with status {@code 500 (Internal Server Error)} if the eattribut couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/eattributs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Eattribut> partialUpdateEattribut(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Eattribut eattribut
    ) throws URISyntaxException {
        log.debug("REST request to partial update Eattribut partially : {}, {}", id, eattribut);
        if (eattribut.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eattribut.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eattributRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Eattribut> result = eattributService.partialUpdate(eattribut);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eattribut.getId().toString())
        );
    }

    /**
     * {@code GET  /eattributs} : get all the eattributs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eattributs in body.
     */
    @GetMapping("/eattributs")
    public ResponseEntity<List<Eattribut>> getAllEattributs(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Eattributs");
        Page<Eattribut> page = eattributService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /eattributs/:id} : get the "id" eattribut.
     *
     * @param id the id of the eattribut to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eattribut, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/eattributs/{id}")
    public ResponseEntity<Eattribut> getEattribut(@PathVariable Long id) {
        log.debug("REST request to get Eattribut : {}", id);
        Optional<Eattribut> eattribut = eattributService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eattribut);
    }

    /**
     * {@code DELETE  /eattributs/:id} : delete the "id" eattribut.
     *
     * @param id the id of the eattribut to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/eattributs/{id}")
    public ResponseEntity<Void> deleteEattribut(@PathVariable Long id) {
        log.debug("REST request to delete Eattribut : {}", id);
        eattributService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/eattributs?query=:query} : search for the eattribut corresponding
     * to the query.
     *
     * @param query the query of the eattribut search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/eattributs")
    public ResponseEntity<List<Eattribut>> searchEattributs(
        @RequestParam String query,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Eattributs for query {}", query);
        Page<Eattribut> page = eattributService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
