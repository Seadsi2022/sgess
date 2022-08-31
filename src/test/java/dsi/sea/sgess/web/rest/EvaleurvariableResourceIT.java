package dsi.sea.sgess.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dsi.sea.sgess.IntegrationTest;
import dsi.sea.sgess.domain.Evaleurvariable;
import dsi.sea.sgess.repository.EvaleurvariableRepository;
import dsi.sea.sgess.repository.search.EvaleurvariableSearchRepository;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.apache.commons.collections4.IterableUtils;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EvaleurvariableResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EvaleurvariableResourceIT {

    private static final String DEFAULT_VALEUR = "AAAAAAAAAA";
    private static final String UPDATED_VALEUR = "BBBBBBBBBB";

    private static final Long DEFAULT_LIGNE = 1L;
    private static final Long UPDATED_LIGNE = 2L;

    private static final Long DEFAULT_COLONNE = 1L;
    private static final Long UPDATED_COLONNE = 2L;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/evaleurvariables";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/evaleurvariables";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EvaleurvariableRepository evaleurvariableRepository;

    @Autowired
    private EvaleurvariableSearchRepository evaleurvariableSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEvaleurvariableMockMvc;

    private Evaleurvariable evaleurvariable;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Evaleurvariable createEntity(EntityManager em) {
        Evaleurvariable evaleurvariable = new Evaleurvariable()
            .valeur(DEFAULT_VALEUR)
            .ligne(DEFAULT_LIGNE)
            .colonne(DEFAULT_COLONNE)
            .isActive(DEFAULT_IS_ACTIVE);
        return evaleurvariable;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Evaleurvariable createUpdatedEntity(EntityManager em) {
        Evaleurvariable evaleurvariable = new Evaleurvariable()
            .valeur(UPDATED_VALEUR)
            .ligne(UPDATED_LIGNE)
            .colonne(UPDATED_COLONNE)
            .isActive(UPDATED_IS_ACTIVE);
        return evaleurvariable;
    }

    @BeforeEach
    public void initTest() {
        evaleurvariableSearchRepository.deleteAll();
        evaleurvariable = createEntity(em);
    }

    @Test
    @Transactional
    void createEvaleurvariable() throws Exception {
        int databaseSizeBeforeCreate = evaleurvariableRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(evaleurvariableSearchRepository.findAll());
        // Create the Evaleurvariable
        restEvaleurvariableMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(evaleurvariable))
            )
            .andExpect(status().isCreated());

        // Validate the Evaleurvariable in the database
        List<Evaleurvariable> evaleurvariableList = evaleurvariableRepository.findAll();
        assertThat(evaleurvariableList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(evaleurvariableSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Evaleurvariable testEvaleurvariable = evaleurvariableList.get(evaleurvariableList.size() - 1);
        assertThat(testEvaleurvariable.getValeur()).isEqualTo(DEFAULT_VALEUR);
        assertThat(testEvaleurvariable.getLigne()).isEqualTo(DEFAULT_LIGNE);
        assertThat(testEvaleurvariable.getColonne()).isEqualTo(DEFAULT_COLONNE);
        assertThat(testEvaleurvariable.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createEvaleurvariableWithExistingId() throws Exception {
        // Create the Evaleurvariable with an existing ID
        evaleurvariable.setId(1L);

        int databaseSizeBeforeCreate = evaleurvariableRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(evaleurvariableSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restEvaleurvariableMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(evaleurvariable))
            )
            .andExpect(status().isBadRequest());

        // Validate the Evaleurvariable in the database
        List<Evaleurvariable> evaleurvariableList = evaleurvariableRepository.findAll();
        assertThat(evaleurvariableList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(evaleurvariableSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllEvaleurvariables() throws Exception {
        // Initialize the database
        evaleurvariableRepository.saveAndFlush(evaleurvariable);

        // Get all the evaleurvariableList
        restEvaleurvariableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(evaleurvariable.getId().intValue())))
            .andExpect(jsonPath("$.[*].valeur").value(hasItem(DEFAULT_VALEUR)))
            .andExpect(jsonPath("$.[*].ligne").value(hasItem(DEFAULT_LIGNE.intValue())))
            .andExpect(jsonPath("$.[*].colonne").value(hasItem(DEFAULT_COLONNE.intValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getEvaleurvariable() throws Exception {
        // Initialize the database
        evaleurvariableRepository.saveAndFlush(evaleurvariable);

        // Get the evaleurvariable
        restEvaleurvariableMockMvc
            .perform(get(ENTITY_API_URL_ID, evaleurvariable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(evaleurvariable.getId().intValue()))
            .andExpect(jsonPath("$.valeur").value(DEFAULT_VALEUR))
            .andExpect(jsonPath("$.ligne").value(DEFAULT_LIGNE.intValue()))
            .andExpect(jsonPath("$.colonne").value(DEFAULT_COLONNE.intValue()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingEvaleurvariable() throws Exception {
        // Get the evaleurvariable
        restEvaleurvariableMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEvaleurvariable() throws Exception {
        // Initialize the database
        evaleurvariableRepository.saveAndFlush(evaleurvariable);

        int databaseSizeBeforeUpdate = evaleurvariableRepository.findAll().size();
        evaleurvariableSearchRepository.save(evaleurvariable);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(evaleurvariableSearchRepository.findAll());

        // Update the evaleurvariable
        Evaleurvariable updatedEvaleurvariable = evaleurvariableRepository.findById(evaleurvariable.getId()).get();
        // Disconnect from session so that the updates on updatedEvaleurvariable are not directly saved in db
        em.detach(updatedEvaleurvariable);
        updatedEvaleurvariable.valeur(UPDATED_VALEUR).ligne(UPDATED_LIGNE).colonne(UPDATED_COLONNE).isActive(UPDATED_IS_ACTIVE);

        restEvaleurvariableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEvaleurvariable.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEvaleurvariable))
            )
            .andExpect(status().isOk());

        // Validate the Evaleurvariable in the database
        List<Evaleurvariable> evaleurvariableList = evaleurvariableRepository.findAll();
        assertThat(evaleurvariableList).hasSize(databaseSizeBeforeUpdate);
        Evaleurvariable testEvaleurvariable = evaleurvariableList.get(evaleurvariableList.size() - 1);
        assertThat(testEvaleurvariable.getValeur()).isEqualTo(UPDATED_VALEUR);
        assertThat(testEvaleurvariable.getLigne()).isEqualTo(UPDATED_LIGNE);
        assertThat(testEvaleurvariable.getColonne()).isEqualTo(UPDATED_COLONNE);
        assertThat(testEvaleurvariable.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(evaleurvariableSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Evaleurvariable> evaleurvariableSearchList = IterableUtils.toList(evaleurvariableSearchRepository.findAll());
                Evaleurvariable testEvaleurvariableSearch = evaleurvariableSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testEvaleurvariableSearch.getValeur()).isEqualTo(UPDATED_VALEUR);
                assertThat(testEvaleurvariableSearch.getLigne()).isEqualTo(UPDATED_LIGNE);
                assertThat(testEvaleurvariableSearch.getColonne()).isEqualTo(UPDATED_COLONNE);
                assertThat(testEvaleurvariableSearch.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
            });
    }

    @Test
    @Transactional
    void putNonExistingEvaleurvariable() throws Exception {
        int databaseSizeBeforeUpdate = evaleurvariableRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(evaleurvariableSearchRepository.findAll());
        evaleurvariable.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEvaleurvariableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, evaleurvariable.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(evaleurvariable))
            )
            .andExpect(status().isBadRequest());

        // Validate the Evaleurvariable in the database
        List<Evaleurvariable> evaleurvariableList = evaleurvariableRepository.findAll();
        assertThat(evaleurvariableList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(evaleurvariableSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchEvaleurvariable() throws Exception {
        int databaseSizeBeforeUpdate = evaleurvariableRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(evaleurvariableSearchRepository.findAll());
        evaleurvariable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvaleurvariableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(evaleurvariable))
            )
            .andExpect(status().isBadRequest());

        // Validate the Evaleurvariable in the database
        List<Evaleurvariable> evaleurvariableList = evaleurvariableRepository.findAll();
        assertThat(evaleurvariableList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(evaleurvariableSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEvaleurvariable() throws Exception {
        int databaseSizeBeforeUpdate = evaleurvariableRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(evaleurvariableSearchRepository.findAll());
        evaleurvariable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvaleurvariableMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(evaleurvariable))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Evaleurvariable in the database
        List<Evaleurvariable> evaleurvariableList = evaleurvariableRepository.findAll();
        assertThat(evaleurvariableList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(evaleurvariableSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateEvaleurvariableWithPatch() throws Exception {
        // Initialize the database
        evaleurvariableRepository.saveAndFlush(evaleurvariable);

        int databaseSizeBeforeUpdate = evaleurvariableRepository.findAll().size();

        // Update the evaleurvariable using partial update
        Evaleurvariable partialUpdatedEvaleurvariable = new Evaleurvariable();
        partialUpdatedEvaleurvariable.setId(evaleurvariable.getId());

        restEvaleurvariableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvaleurvariable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEvaleurvariable))
            )
            .andExpect(status().isOk());

        // Validate the Evaleurvariable in the database
        List<Evaleurvariable> evaleurvariableList = evaleurvariableRepository.findAll();
        assertThat(evaleurvariableList).hasSize(databaseSizeBeforeUpdate);
        Evaleurvariable testEvaleurvariable = evaleurvariableList.get(evaleurvariableList.size() - 1);
        assertThat(testEvaleurvariable.getValeur()).isEqualTo(DEFAULT_VALEUR);
        assertThat(testEvaleurvariable.getLigne()).isEqualTo(DEFAULT_LIGNE);
        assertThat(testEvaleurvariable.getColonne()).isEqualTo(DEFAULT_COLONNE);
        assertThat(testEvaleurvariable.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateEvaleurvariableWithPatch() throws Exception {
        // Initialize the database
        evaleurvariableRepository.saveAndFlush(evaleurvariable);

        int databaseSizeBeforeUpdate = evaleurvariableRepository.findAll().size();

        // Update the evaleurvariable using partial update
        Evaleurvariable partialUpdatedEvaleurvariable = new Evaleurvariable();
        partialUpdatedEvaleurvariable.setId(evaleurvariable.getId());

        partialUpdatedEvaleurvariable.valeur(UPDATED_VALEUR).ligne(UPDATED_LIGNE).colonne(UPDATED_COLONNE).isActive(UPDATED_IS_ACTIVE);

        restEvaleurvariableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvaleurvariable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEvaleurvariable))
            )
            .andExpect(status().isOk());

        // Validate the Evaleurvariable in the database
        List<Evaleurvariable> evaleurvariableList = evaleurvariableRepository.findAll();
        assertThat(evaleurvariableList).hasSize(databaseSizeBeforeUpdate);
        Evaleurvariable testEvaleurvariable = evaleurvariableList.get(evaleurvariableList.size() - 1);
        assertThat(testEvaleurvariable.getValeur()).isEqualTo(UPDATED_VALEUR);
        assertThat(testEvaleurvariable.getLigne()).isEqualTo(UPDATED_LIGNE);
        assertThat(testEvaleurvariable.getColonne()).isEqualTo(UPDATED_COLONNE);
        assertThat(testEvaleurvariable.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingEvaleurvariable() throws Exception {
        int databaseSizeBeforeUpdate = evaleurvariableRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(evaleurvariableSearchRepository.findAll());
        evaleurvariable.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEvaleurvariableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, evaleurvariable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(evaleurvariable))
            )
            .andExpect(status().isBadRequest());

        // Validate the Evaleurvariable in the database
        List<Evaleurvariable> evaleurvariableList = evaleurvariableRepository.findAll();
        assertThat(evaleurvariableList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(evaleurvariableSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEvaleurvariable() throws Exception {
        int databaseSizeBeforeUpdate = evaleurvariableRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(evaleurvariableSearchRepository.findAll());
        evaleurvariable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvaleurvariableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(evaleurvariable))
            )
            .andExpect(status().isBadRequest());

        // Validate the Evaleurvariable in the database
        List<Evaleurvariable> evaleurvariableList = evaleurvariableRepository.findAll();
        assertThat(evaleurvariableList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(evaleurvariableSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEvaleurvariable() throws Exception {
        int databaseSizeBeforeUpdate = evaleurvariableRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(evaleurvariableSearchRepository.findAll());
        evaleurvariable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvaleurvariableMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(evaleurvariable))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Evaleurvariable in the database
        List<Evaleurvariable> evaleurvariableList = evaleurvariableRepository.findAll();
        assertThat(evaleurvariableList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(evaleurvariableSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteEvaleurvariable() throws Exception {
        // Initialize the database
        evaleurvariableRepository.saveAndFlush(evaleurvariable);
        evaleurvariableRepository.save(evaleurvariable);
        evaleurvariableSearchRepository.save(evaleurvariable);

        int databaseSizeBeforeDelete = evaleurvariableRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(evaleurvariableSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the evaleurvariable
        restEvaleurvariableMockMvc
            .perform(delete(ENTITY_API_URL_ID, evaleurvariable.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Evaleurvariable> evaleurvariableList = evaleurvariableRepository.findAll();
        assertThat(evaleurvariableList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(evaleurvariableSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchEvaleurvariable() throws Exception {
        // Initialize the database
        evaleurvariable = evaleurvariableRepository.saveAndFlush(evaleurvariable);
        evaleurvariableSearchRepository.save(evaleurvariable);

        // Search the evaleurvariable
        restEvaleurvariableMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + evaleurvariable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(evaleurvariable.getId().intValue())))
            .andExpect(jsonPath("$.[*].valeur").value(hasItem(DEFAULT_VALEUR)))
            .andExpect(jsonPath("$.[*].ligne").value(hasItem(DEFAULT_LIGNE.intValue())))
            .andExpect(jsonPath("$.[*].colonne").value(hasItem(DEFAULT_COLONNE.intValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }
}
