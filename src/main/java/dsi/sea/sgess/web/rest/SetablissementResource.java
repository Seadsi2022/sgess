package dsi.sea.sgess.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import dsi.sea.sgess.domain.Setablissement;
import dsi.sea.sgess.repository.SetablissementRepository;
import dsi.sea.sgess.service.SetablissementService;
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
 * REST controller for managing {@link dsi.sea.sgess.domain.Setablissement}.
 */
@RestController
@RequestMapping("/api")
public class SetablissementResource {

    private final Logger log = LoggerFactory.getLogger(SetablissementResource.class);

    private static final String ENTITY_NAME = "setablissement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SetablissementService setablissementService;

    private final SetablissementRepository setablissementRepository;

    public SetablissementResource(SetablissementService setablissementService, SetablissementRepository setablissementRepository) {
        this.setablissementService = setablissementService;
        this.setablissementRepository = setablissementRepository;
    }

    /**
     * {@code POST  /setablissements} : Create a new setablissement.
     *
     * @param setablissement the setablissement to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new setablissement, or with status {@code 400 (Bad Request)} if the setablissement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/setablissements")
    public ResponseEntity<Setablissement> createSetablissement(@RequestBody Setablissement setablissement) throws URISyntaxException {
        log.debug("REST request to save Setablissement : {}", setablissement);
        if (setablissement.getId() != null) {
            throw new BadRequestAlertException("A new setablissement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Setablissement result = setablissementService.save(setablissement);
        return ResponseEntity
            .created(new URI("/api/setablissements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /setablissements/:id} : Updates an existing setablissement.
     *
     * @param id the id of the setablissement to save.
     * @param setablissement the setablissement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated setablissement,
     * or with status {@code 400 (Bad Request)} if the setablissement is not valid,
     * or with status {@code 500 (Internal Server Error)} if the setablissement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/setablissements/{id}")
    public ResponseEntity<Setablissement> updateSetablissement(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Setablissement setablissement
    ) throws URISyntaxException {
        log.debug("REST request to update Setablissement : {}, {}", id, setablissement);
        if (setablissement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, setablissement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!setablissementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Setablissement result = setablissementService.update(setablissement);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, setablissement.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /setablissements/:id} : Partial updates given fields of an existing setablissement, field will ignore if it is null
     *
     * @param id the id of the setablissement to save.
     * @param setablissement the setablissement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated setablissement,
     * or with status {@code 400 (Bad Request)} if the setablissement is not valid,
     * or with status {@code 404 (Not Found)} if the setablissement is not found,
     * or with status {@code 500 (Internal Server Error)} if the setablissement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/setablissements/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Setablissement> partialUpdateSetablissement(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Setablissement setablissement
    ) throws URISyntaxException {
        log.debug("REST request to partial update Setablissement partially : {}, {}", id, setablissement);
        if (setablissement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, setablissement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!setablissementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Setablissement> result = setablissementService.partialUpdate(setablissement);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, setablissement.getId().toString())
        );
    }

    /**
     * {@code GET  /setablissements} : get all the setablissements.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of setablissements in body.
     */
    @GetMapping("/setablissements")
    public ResponseEntity<List<Setablissement>> getAllSetablissements(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Setablissements");
        Page<Setablissement> page = setablissementService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /setablissements/:id} : get the "id" setablissement.
     *
     * @param id the id of the setablissement to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the setablissement, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/setablissements/{id}")
    public ResponseEntity<Setablissement> getSetablissement(@PathVariable Long id) {
        log.debug("REST request to get Setablissement : {}", id);
        Optional<Setablissement> setablissement = setablissementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(setablissement);
    }

    /**
     * {@code DELETE  /setablissements/:id} : delete the "id" setablissement.
     *
     * @param id the id of the setablissement to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/setablissements/{id}")
    public ResponseEntity<Void> deleteSetablissement(@PathVariable Long id) {
        log.debug("REST request to delete Setablissement : {}", id);
        setablissementService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/setablissements?query=:query} : search for the setablissement corresponding
     * to the query.
     *
     * @param query the query of the setablissement search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/setablissements")
    public ResponseEntity<List<Setablissement>> searchSetablissements(
        @RequestParam String query,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Setablissements for query {}", query);
        Page<Setablissement> page = setablissementService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
