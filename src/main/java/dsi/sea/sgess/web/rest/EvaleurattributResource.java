package dsi.sea.sgess.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import dsi.sea.sgess.domain.Evaleurattribut;
import dsi.sea.sgess.repository.EvaleurattributRepository;
import dsi.sea.sgess.service.EvaleurattributService;
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
 * REST controller for managing {@link dsi.sea.sgess.domain.Evaleurattribut}.
 */
@RestController
@RequestMapping("/api")
public class EvaleurattributResource {

    private final Logger log = LoggerFactory.getLogger(EvaleurattributResource.class);

    private static final String ENTITY_NAME = "evaleurattribut";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EvaleurattributService evaleurattributService;

    private final EvaleurattributRepository evaleurattributRepository;

    public EvaleurattributResource(EvaleurattributService evaleurattributService, EvaleurattributRepository evaleurattributRepository) {
        this.evaleurattributService = evaleurattributService;
        this.evaleurattributRepository = evaleurattributRepository;
    }

    /**
     * {@code POST  /evaleurattributs} : Create a new evaleurattribut.
     *
     * @param evaleurattribut the evaleurattribut to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new evaleurattribut, or with status {@code 400 (Bad Request)} if the evaleurattribut has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/evaleurattributs")
    public ResponseEntity<Evaleurattribut> createEvaleurattribut(@RequestBody Evaleurattribut evaleurattribut) throws URISyntaxException {
        log.debug("REST request to save Evaleurattribut : {}", evaleurattribut);
        if (evaleurattribut.getId() != null) {
            throw new BadRequestAlertException("A new evaleurattribut cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Evaleurattribut result = evaleurattributService.save(evaleurattribut);
        return ResponseEntity
            .created(new URI("/api/evaleurattributs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /evaleurattributs/:id} : Updates an existing evaleurattribut.
     *
     * @param id the id of the evaleurattribut to save.
     * @param evaleurattribut the evaleurattribut to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated evaleurattribut,
     * or with status {@code 400 (Bad Request)} if the evaleurattribut is not valid,
     * or with status {@code 500 (Internal Server Error)} if the evaleurattribut couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/evaleurattributs/{id}")
    public ResponseEntity<Evaleurattribut> updateEvaleurattribut(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Evaleurattribut evaleurattribut
    ) throws URISyntaxException {
        log.debug("REST request to update Evaleurattribut : {}, {}", id, evaleurattribut);
        if (evaleurattribut.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, evaleurattribut.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!evaleurattributRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Evaleurattribut result = evaleurattributService.update(evaleurattribut);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, evaleurattribut.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /evaleurattributs/:id} : Partial updates given fields of an existing evaleurattribut, field will ignore if it is null
     *
     * @param id the id of the evaleurattribut to save.
     * @param evaleurattribut the evaleurattribut to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated evaleurattribut,
     * or with status {@code 400 (Bad Request)} if the evaleurattribut is not valid,
     * or with status {@code 404 (Not Found)} if the evaleurattribut is not found,
     * or with status {@code 500 (Internal Server Error)} if the evaleurattribut couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/evaleurattributs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Evaleurattribut> partialUpdateEvaleurattribut(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Evaleurattribut evaleurattribut
    ) throws URISyntaxException {
        log.debug("REST request to partial update Evaleurattribut partially : {}, {}", id, evaleurattribut);
        if (evaleurattribut.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, evaleurattribut.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!evaleurattributRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Evaleurattribut> result = evaleurattributService.partialUpdate(evaleurattribut);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, evaleurattribut.getId().toString())
        );
    }

    /**
     * {@code GET  /evaleurattributs} : get all the evaleurattributs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of evaleurattributs in body.
     */
    @GetMapping("/evaleurattributs")
    public ResponseEntity<List<Evaleurattribut>> getAllEvaleurattributs(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Evaleurattributs");
        Page<Evaleurattribut> page = evaleurattributService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /evaleurattributs/:id} : get the "id" evaleurattribut.
     *
     * @param id the id of the evaleurattribut to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the evaleurattribut, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/evaleurattributs/{id}")
    public ResponseEntity<Evaleurattribut> getEvaleurattribut(@PathVariable Long id) {
        log.debug("REST request to get Evaleurattribut : {}", id);
        Optional<Evaleurattribut> evaleurattribut = evaleurattributService.findOne(id);
        return ResponseUtil.wrapOrNotFound(evaleurattribut);
    }

    /**
     * {@code DELETE  /evaleurattributs/:id} : delete the "id" evaleurattribut.
     *
     * @param id the id of the evaleurattribut to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/evaleurattributs/{id}")
    public ResponseEntity<Void> deleteEvaleurattribut(@PathVariable Long id) {
        log.debug("REST request to delete Evaleurattribut : {}", id);
        evaleurattributService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/evaleurattributs?query=:query} : search for the evaleurattribut corresponding
     * to the query.
     *
     * @param query the query of the evaleurattribut search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/evaleurattributs")
    public ResponseEntity<List<Evaleurattribut>> searchEvaleurattributs(
        @RequestParam String query,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Evaleurattributs for query {}", query);
        Page<Evaleurattribut> page = evaleurattributService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
