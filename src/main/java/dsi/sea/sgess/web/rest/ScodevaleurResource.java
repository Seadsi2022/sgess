package dsi.sea.sgess.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import dsi.sea.sgess.domain.Scodevaleur;
import dsi.sea.sgess.repository.ScodevaleurRepository;
import dsi.sea.sgess.service.ScodevaleurService;
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
 * REST controller for managing {@link dsi.sea.sgess.domain.Scodevaleur}.
 */
@RestController
@RequestMapping("/api")
public class ScodevaleurResource {

    private final Logger log = LoggerFactory.getLogger(ScodevaleurResource.class);

    private static final String ENTITY_NAME = "scodevaleur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ScodevaleurService scodevaleurService;

    private final ScodevaleurRepository scodevaleurRepository;

    public ScodevaleurResource(ScodevaleurService scodevaleurService, ScodevaleurRepository scodevaleurRepository) {
        this.scodevaleurService = scodevaleurService;
        this.scodevaleurRepository = scodevaleurRepository;
    }

    /**
     * {@code POST  /scodevaleurs} : Create a new scodevaleur.
     *
     * @param scodevaleur the scodevaleur to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new scodevaleur, or with status {@code 400 (Bad Request)} if the scodevaleur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/scodevaleurs")
    public ResponseEntity<Scodevaleur> createScodevaleur(@RequestBody Scodevaleur scodevaleur) throws URISyntaxException {
        log.debug("REST request to save Scodevaleur : {}", scodevaleur);
        if (scodevaleur.getId() != null) {
            throw new BadRequestAlertException("A new scodevaleur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Scodevaleur result = scodevaleurService.save(scodevaleur);
        return ResponseEntity
            .created(new URI("/api/scodevaleurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /scodevaleurs/:id} : Updates an existing scodevaleur.
     *
     * @param id the id of the scodevaleur to save.
     * @param scodevaleur the scodevaleur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated scodevaleur,
     * or with status {@code 400 (Bad Request)} if the scodevaleur is not valid,
     * or with status {@code 500 (Internal Server Error)} if the scodevaleur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/scodevaleurs/{id}")
    public ResponseEntity<Scodevaleur> updateScodevaleur(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Scodevaleur scodevaleur
    ) throws URISyntaxException {
        log.debug("REST request to update Scodevaleur : {}, {}", id, scodevaleur);
        if (scodevaleur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, scodevaleur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!scodevaleurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Scodevaleur result = scodevaleurService.update(scodevaleur);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, scodevaleur.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /scodevaleurs/:id} : Partial updates given fields of an existing scodevaleur, field will ignore if it is null
     *
     * @param id the id of the scodevaleur to save.
     * @param scodevaleur the scodevaleur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated scodevaleur,
     * or with status {@code 400 (Bad Request)} if the scodevaleur is not valid,
     * or with status {@code 404 (Not Found)} if the scodevaleur is not found,
     * or with status {@code 500 (Internal Server Error)} if the scodevaleur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/scodevaleurs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Scodevaleur> partialUpdateScodevaleur(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Scodevaleur scodevaleur
    ) throws URISyntaxException {
        log.debug("REST request to partial update Scodevaleur partially : {}, {}", id, scodevaleur);
        if (scodevaleur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, scodevaleur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!scodevaleurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Scodevaleur> result = scodevaleurService.partialUpdate(scodevaleur);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, scodevaleur.getId().toString())
        );
    }

    /**
     * {@code GET  /scodevaleurs} : get all the scodevaleurs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of scodevaleurs in body.
     */
    @GetMapping("/scodevaleurs")
    public ResponseEntity<List<Scodevaleur>> getAllScodevaleurs(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Scodevaleurs");
        Page<Scodevaleur> page = scodevaleurService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /scodevaleurs/:id} : get the "id" scodevaleur.
     *
     * @param id the id of the scodevaleur to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the scodevaleur, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/scodevaleurs/{id}")
    public ResponseEntity<Scodevaleur> getScodevaleur(@PathVariable Long id) {
        log.debug("REST request to get Scodevaleur : {}", id);
        Optional<Scodevaleur> scodevaleur = scodevaleurService.findOne(id);
        return ResponseUtil.wrapOrNotFound(scodevaleur);
    }

    /**
     * {@code DELETE  /scodevaleurs/:id} : delete the "id" scodevaleur.
     *
     * @param id the id of the scodevaleur to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/scodevaleurs/{id}")
    public ResponseEntity<Void> deleteScodevaleur(@PathVariable Long id) {
        log.debug("REST request to delete Scodevaleur : {}", id);
        scodevaleurService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/scodevaleurs?query=:query} : search for the scodevaleur corresponding
     * to the query.
     *
     * @param query the query of the scodevaleur search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/scodevaleurs")
    public ResponseEntity<List<Scodevaleur>> searchScodevaleurs(
        @RequestParam String query,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Scodevaleurs for query {}", query);
        Page<Scodevaleur> page = scodevaleurService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
