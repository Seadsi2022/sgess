package dsi.sea.sgess.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import dsi.sea.sgess.domain.Equestionnaire;
import dsi.sea.sgess.repository.EquestionnaireRepository;
import dsi.sea.sgess.service.EquestionnaireService;
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
 * REST controller for managing {@link dsi.sea.sgess.domain.Equestionnaire}.
 */
@RestController
@RequestMapping("/api")
public class EquestionnaireResource {

    private final Logger log = LoggerFactory.getLogger(EquestionnaireResource.class);

    private static final String ENTITY_NAME = "equestionnaire";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EquestionnaireService equestionnaireService;

    private final EquestionnaireRepository equestionnaireRepository;

    public EquestionnaireResource(EquestionnaireService equestionnaireService, EquestionnaireRepository equestionnaireRepository) {
        this.equestionnaireService = equestionnaireService;
        this.equestionnaireRepository = equestionnaireRepository;
    }

    /**
     * {@code POST  /equestionnaires} : Create a new equestionnaire.
     *
     * @param equestionnaire the equestionnaire to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new equestionnaire, or with status {@code 400 (Bad Request)} if the equestionnaire has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/equestionnaires")
    public ResponseEntity<Equestionnaire> createEquestionnaire(@RequestBody Equestionnaire equestionnaire) throws URISyntaxException {
        log.debug("REST request to save Equestionnaire : {}", equestionnaire);
        if (equestionnaire.getId() != null) {
            throw new BadRequestAlertException("A new equestionnaire cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Equestionnaire result = equestionnaireService.save(equestionnaire);
        return ResponseEntity
            .created(new URI("/api/equestionnaires/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /equestionnaires/:id} : Updates an existing equestionnaire.
     *
     * @param id the id of the equestionnaire to save.
     * @param equestionnaire the equestionnaire to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated equestionnaire,
     * or with status {@code 400 (Bad Request)} if the equestionnaire is not valid,
     * or with status {@code 500 (Internal Server Error)} if the equestionnaire couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/equestionnaires/{id}")
    public ResponseEntity<Equestionnaire> updateEquestionnaire(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Equestionnaire equestionnaire
    ) throws URISyntaxException {
        log.debug("REST request to update Equestionnaire : {}, {}", id, equestionnaire);
        if (equestionnaire.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, equestionnaire.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!equestionnaireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Equestionnaire result = equestionnaireService.update(equestionnaire);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, equestionnaire.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /equestionnaires/:id} : Partial updates given fields of an existing equestionnaire, field will ignore if it is null
     *
     * @param id the id of the equestionnaire to save.
     * @param equestionnaire the equestionnaire to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated equestionnaire,
     * or with status {@code 400 (Bad Request)} if the equestionnaire is not valid,
     * or with status {@code 404 (Not Found)} if the equestionnaire is not found,
     * or with status {@code 500 (Internal Server Error)} if the equestionnaire couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/equestionnaires/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Equestionnaire> partialUpdateEquestionnaire(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Equestionnaire equestionnaire
    ) throws URISyntaxException {
        log.debug("REST request to partial update Equestionnaire partially : {}, {}", id, equestionnaire);
        if (equestionnaire.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, equestionnaire.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!equestionnaireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Equestionnaire> result = equestionnaireService.partialUpdate(equestionnaire);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, equestionnaire.getId().toString())
        );
    }

    /**
     * {@code GET  /equestionnaires} : get all the equestionnaires.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of equestionnaires in body.
     */
    @GetMapping("/equestionnaires")
    public ResponseEntity<List<Equestionnaire>> getAllEquestionnaires(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Equestionnaires");
        Page<Equestionnaire> page = equestionnaireService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /equestionnaires/:id} : get the "id" equestionnaire.
     *
     * @param id the id of the equestionnaire to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the equestionnaire, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/equestionnaires/{id}")
    public ResponseEntity<Equestionnaire> getEquestionnaire(@PathVariable Long id) {
        log.debug("REST request to get Equestionnaire : {}", id);
        Optional<Equestionnaire> equestionnaire = equestionnaireService.findOne(id);
        return ResponseUtil.wrapOrNotFound(equestionnaire);
    }

    /**
     * {@code DELETE  /equestionnaires/:id} : delete the "id" equestionnaire.
     *
     * @param id the id of the equestionnaire to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/equestionnaires/{id}")
    public ResponseEntity<Void> deleteEquestionnaire(@PathVariable Long id) {
        log.debug("REST request to delete Equestionnaire : {}", id);
        equestionnaireService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/equestionnaires?query=:query} : search for the equestionnaire corresponding
     * to the query.
     *
     * @param query the query of the equestionnaire search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/equestionnaires")
    public ResponseEntity<List<Equestionnaire>> searchEquestionnaires(
        @RequestParam String query,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Equestionnaires for query {}", query);
        Page<Equestionnaire> page = equestionnaireService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
