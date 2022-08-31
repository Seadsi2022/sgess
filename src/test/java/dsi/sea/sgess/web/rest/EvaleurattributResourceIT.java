package dsi.sea.sgess.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dsi.sea.sgess.IntegrationTest;
import dsi.sea.sgess.domain.Evaleurattribut;
import dsi.sea.sgess.repository.EvaleurattributRepository;
import dsi.sea.sgess.repository.search.EvaleurattributSearchRepository;
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
 * Integration tests for the {@link EvaleurattributResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EvaleurattributResourceIT {

    private static final String DEFAULT_VALEUR = "AAAAAAAAAA";
    private static final String UPDATED_VALEUR = "BBBBBBBBBB";

    private static final String DEFAULT_VALEURDISPLAYNAME = "AAAAAAAAAA";
    private static final String UPDATED_VALEURDISPLAYNAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/evaleurattributs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/evaleurattributs";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EvaleurattributRepository evaleurattributRepository;

    @Autowired
    private EvaleurattributSearchRepository evaleurattributSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEvaleurattributMockMvc;

    private Evaleurattribut evaleurattribut;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Evaleurattribut createEntity(EntityManager em) {
        Evaleurattribut evaleurattribut = new Evaleurattribut().valeur(DEFAULT_VALEUR).valeurdisplayname(DEFAULT_VALEURDISPLAYNAME);
        return evaleurattribut;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Evaleurattribut createUpdatedEntity(EntityManager em) {
        Evaleurattribut evaleurattribut = new Evaleurattribut().valeur(UPDATED_VALEUR).valeurdisplayname(UPDATED_VALEURDISPLAYNAME);
        return evaleurattribut;
    }

    @BeforeEach
    public void initTest() {
        evaleurattributSearchRepository.deleteAll();
        evaleurattribut = createEntity(em);
    }

    @Test
    @Transactional
    void createEvaleurattribut() throws Exception {
        int databaseSizeBeforeCreate = evaleurattributRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(evaleurattributSearchRepository.findAll());
        // Create the Evaleurattribut
        restEvaleurattributMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(evaleurattribut))
            )
            .andExpect(status().isCreated());

        // Validate the Evaleurattribut in the database
        List<Evaleurattribut> evaleurattributList = evaleurattributRepository.findAll();
        assertThat(evaleurattributList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(evaleurattributSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Evaleurattribut testEvaleurattribut = evaleurattributList.get(evaleurattributList.size() - 1);
        assertThat(testEvaleurattribut.getValeur()).isEqualTo(DEFAULT_VALEUR);
        assertThat(testEvaleurattribut.getValeurdisplayname()).isEqualTo(DEFAULT_VALEURDISPLAYNAME);
    }

    @Test
    @Transactional
    void createEvaleurattributWithExistingId() throws Exception {
        // Create the Evaleurattribut with an existing ID
        evaleurattribut.setId(1L);

        int databaseSizeBeforeCreate = evaleurattributRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(evaleurattributSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restEvaleurattributMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(evaleurattribut))
            )
            .andExpect(status().isBadRequest());

        // Validate the Evaleurattribut in the database
        List<Evaleurattribut> evaleurattributList = evaleurattributRepository.findAll();
        assertThat(evaleurattributList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(evaleurattributSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllEvaleurattributs() throws Exception {
        // Initialize the database
        evaleurattributRepository.saveAndFlush(evaleurattribut);

        // Get all the evaleurattributList
        restEvaleurattributMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(evaleurattribut.getId().intValue())))
            .andExpect(jsonPath("$.[*].valeur").value(hasItem(DEFAULT_VALEUR)))
            .andExpect(jsonPath("$.[*].valeurdisplayname").value(hasItem(DEFAULT_VALEURDISPLAYNAME)));
    }

    @Test
    @Transactional
    void getEvaleurattribut() throws Exception {
        // Initialize the database
        evaleurattributRepository.saveAndFlush(evaleurattribut);

        // Get the evaleurattribut
        restEvaleurattributMockMvc
            .perform(get(ENTITY_API_URL_ID, evaleurattribut.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(evaleurattribut.getId().intValue()))
            .andExpect(jsonPath("$.valeur").value(DEFAULT_VALEUR))
            .andExpect(jsonPath("$.valeurdisplayname").value(DEFAULT_VALEURDISPLAYNAME));
    }

    @Test
    @Transactional
    void getNonExistingEvaleurattribut() throws Exception {
        // Get the evaleurattribut
        restEvaleurattributMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEvaleurattribut() throws Exception {
        // Initialize the database
        evaleurattributRepository.saveAndFlush(evaleurattribut);

        int databaseSizeBeforeUpdate = evaleurattributRepository.findAll().size();
        evaleurattributSearchRepository.save(evaleurattribut);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(evaleurattributSearchRepository.findAll());

        // Update the evaleurattribut
        Evaleurattribut updatedEvaleurattribut = evaleurattributRepository.findById(evaleurattribut.getId()).get();
        // Disconnect from session so that the updates on updatedEvaleurattribut are not directly saved in db
        em.detach(updatedEvaleurattribut);
        updatedEvaleurattribut.valeur(UPDATED_VALEUR).valeurdisplayname(UPDATED_VALEURDISPLAYNAME);

        restEvaleurattributMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEvaleurattribut.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEvaleurattribut))
            )
            .andExpect(status().isOk());

        // Validate the Evaleurattribut in the database
        List<Evaleurattribut> evaleurattributList = evaleurattributRepository.findAll();
        assertThat(evaleurattributList).hasSize(databaseSizeBeforeUpdate);
        Evaleurattribut testEvaleurattribut = evaleurattributList.get(evaleurattributList.size() - 1);
        assertThat(testEvaleurattribut.getValeur()).isEqualTo(UPDATED_VALEUR);
        assertThat(testEvaleurattribut.getValeurdisplayname()).isEqualTo(UPDATED_VALEURDISPLAYNAME);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(evaleurattributSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Evaleurattribut> evaleurattributSearchList = IterableUtils.toList(evaleurattributSearchRepository.findAll());
                Evaleurattribut testEvaleurattributSearch = evaleurattributSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testEvaleurattributSearch.getValeur()).isEqualTo(UPDATED_VALEUR);
                assertThat(testEvaleurattributSearch.getValeurdisplayname()).isEqualTo(UPDATED_VALEURDISPLAYNAME);
            });
    }

    @Test
    @Transactional
    void putNonExistingEvaleurattribut() throws Exception {
        int databaseSizeBeforeUpdate = evaleurattributRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(evaleurattributSearchRepository.findAll());
        evaleurattribut.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEvaleurattributMockMvc
            .perform(
                put(ENTITY_API_URL_ID, evaleurattribut.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(evaleurattribut))
            )
            .andExpect(status().isBadRequest());

        // Validate the Evaleurattribut in the database
        List<Evaleurattribut> evaleurattributList = evaleurattributRepository.findAll();
        assertThat(evaleurattributList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(evaleurattributSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchEvaleurattribut() throws Exception {
        int databaseSizeBeforeUpdate = evaleurattributRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(evaleurattributSearchRepository.findAll());
        evaleurattribut.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvaleurattributMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(evaleurattribut))
            )
            .andExpect(status().isBadRequest());

        // Validate the Evaleurattribut in the database
        List<Evaleurattribut> evaleurattributList = evaleurattributRepository.findAll();
        assertThat(evaleurattributList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(evaleurattributSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEvaleurattribut() throws Exception {
        int databaseSizeBeforeUpdate = evaleurattributRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(evaleurattributSearchRepository.findAll());
        evaleurattribut.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvaleurattributMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(evaleurattribut))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Evaleurattribut in the database
        List<Evaleurattribut> evaleurattributList = evaleurattributRepository.findAll();
        assertThat(evaleurattributList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(evaleurattributSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateEvaleurattributWithPatch() throws Exception {
        // Initialize the database
        evaleurattributRepository.saveAndFlush(evaleurattribut);

        int databaseSizeBeforeUpdate = evaleurattributRepository.findAll().size();

        // Update the evaleurattribut using partial update
        Evaleurattribut partialUpdatedEvaleurattribut = new Evaleurattribut();
        partialUpdatedEvaleurattribut.setId(evaleurattribut.getId());

        partialUpdatedEvaleurattribut.valeur(UPDATED_VALEUR);

        restEvaleurattributMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvaleurattribut.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEvaleurattribut))
            )
            .andExpect(status().isOk());

        // Validate the Evaleurattribut in the database
        List<Evaleurattribut> evaleurattributList = evaleurattributRepository.findAll();
        assertThat(evaleurattributList).hasSize(databaseSizeBeforeUpdate);
        Evaleurattribut testEvaleurattribut = evaleurattributList.get(evaleurattributList.size() - 1);
        assertThat(testEvaleurattribut.getValeur()).isEqualTo(UPDATED_VALEUR);
        assertThat(testEvaleurattribut.getValeurdisplayname()).isEqualTo(DEFAULT_VALEURDISPLAYNAME);
    }

    @Test
    @Transactional
    void fullUpdateEvaleurattributWithPatch() throws Exception {
        // Initialize the database
        evaleurattributRepository.saveAndFlush(evaleurattribut);

        int databaseSizeBeforeUpdate = evaleurattributRepository.findAll().size();

        // Update the evaleurattribut using partial update
        Evaleurattribut partialUpdatedEvaleurattribut = new Evaleurattribut();
        partialUpdatedEvaleurattribut.setId(evaleurattribut.getId());

        partialUpdatedEvaleurattribut.valeur(UPDATED_VALEUR).valeurdisplayname(UPDATED_VALEURDISPLAYNAME);

        restEvaleurattributMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvaleurattribut.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEvaleurattribut))
            )
            .andExpect(status().isOk());

        // Validate the Evaleurattribut in the database
        List<Evaleurattribut> evaleurattributList = evaleurattributRepository.findAll();
        assertThat(evaleurattributList).hasSize(databaseSizeBeforeUpdate);
        Evaleurattribut testEvaleurattribut = evaleurattributList.get(evaleurattributList.size() - 1);
        assertThat(testEvaleurattribut.getValeur()).isEqualTo(UPDATED_VALEUR);
        assertThat(testEvaleurattribut.getValeurdisplayname()).isEqualTo(UPDATED_VALEURDISPLAYNAME);
    }

    @Test
    @Transactional
    void patchNonExistingEvaleurattribut() throws Exception {
        int databaseSizeBeforeUpdate = evaleurattributRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(evaleurattributSearchRepository.findAll());
        evaleurattribut.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEvaleurattributMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, evaleurattribut.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(evaleurattribut))
            )
            .andExpect(status().isBadRequest());

        // Validate the Evaleurattribut in the database
        List<Evaleurattribut> evaleurattributList = evaleurattributRepository.findAll();
        assertThat(evaleurattributList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(evaleurattributSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEvaleurattribut() throws Exception {
        int databaseSizeBeforeUpdate = evaleurattributRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(evaleurattributSearchRepository.findAll());
        evaleurattribut.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvaleurattributMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(evaleurattribut))
            )
            .andExpect(status().isBadRequest());

        // Validate the Evaleurattribut in the database
        List<Evaleurattribut> evaleurattributList = evaleurattributRepository.findAll();
        assertThat(evaleurattributList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(evaleurattributSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEvaleurattribut() throws Exception {
        int databaseSizeBeforeUpdate = evaleurattributRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(evaleurattributSearchRepository.findAll());
        evaleurattribut.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvaleurattributMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(evaleurattribut))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Evaleurattribut in the database
        List<Evaleurattribut> evaleurattributList = evaleurattributRepository.findAll();
        assertThat(evaleurattributList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(evaleurattributSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteEvaleurattribut() throws Exception {
        // Initialize the database
        evaleurattributRepository.saveAndFlush(evaleurattribut);
        evaleurattributRepository.save(evaleurattribut);
        evaleurattributSearchRepository.save(evaleurattribut);

        int databaseSizeBeforeDelete = evaleurattributRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(evaleurattributSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the evaleurattribut
        restEvaleurattributMockMvc
            .perform(delete(ENTITY_API_URL_ID, evaleurattribut.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Evaleurattribut> evaleurattributList = evaleurattributRepository.findAll();
        assertThat(evaleurattributList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(evaleurattributSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchEvaleurattribut() throws Exception {
        // Initialize the database
        evaleurattribut = evaleurattributRepository.saveAndFlush(evaleurattribut);
        evaleurattributSearchRepository.save(evaleurattribut);

        // Search the evaleurattribut
        restEvaleurattributMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + evaleurattribut.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(evaleurattribut.getId().intValue())))
            .andExpect(jsonPath("$.[*].valeur").value(hasItem(DEFAULT_VALEUR)))
            .andExpect(jsonPath("$.[*].valeurdisplayname").value(hasItem(DEFAULT_VALEURDISPLAYNAME)));
    }
}
