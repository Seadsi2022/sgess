package dsi.sea.sgess.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dsi.sea.sgess.IntegrationTest;
import dsi.sea.sgess.domain.Slocalite;
import dsi.sea.sgess.repository.SlocaliteRepository;
import dsi.sea.sgess.repository.search.SlocaliteSearchRepository;
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
 * Integration tests for the {@link SlocaliteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SlocaliteResourceIT {

    private static final Long DEFAULT_CODELOCALITE = 1L;
    private static final Long UPDATED_CODELOCALITE = 2L;

    private static final String DEFAULT_NOMLOCALITE = "AAAAAAAAAA";
    private static final String UPDATED_NOMLOCALITE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/slocalites";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/slocalites";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SlocaliteRepository slocaliteRepository;

    @Autowired
    private SlocaliteSearchRepository slocaliteSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSlocaliteMockMvc;

    private Slocalite slocalite;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Slocalite createEntity(EntityManager em) {
        Slocalite slocalite = new Slocalite().codelocalite(DEFAULT_CODELOCALITE).nomlocalite(DEFAULT_NOMLOCALITE);
        return slocalite;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Slocalite createUpdatedEntity(EntityManager em) {
        Slocalite slocalite = new Slocalite().codelocalite(UPDATED_CODELOCALITE).nomlocalite(UPDATED_NOMLOCALITE);
        return slocalite;
    }

    @BeforeEach
    public void initTest() {
        slocaliteSearchRepository.deleteAll();
        slocalite = createEntity(em);
    }

    @Test
    @Transactional
    void createSlocalite() throws Exception {
        int databaseSizeBeforeCreate = slocaliteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(slocaliteSearchRepository.findAll());
        // Create the Slocalite
        restSlocaliteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(slocalite)))
            .andExpect(status().isCreated());

        // Validate the Slocalite in the database
        List<Slocalite> slocaliteList = slocaliteRepository.findAll();
        assertThat(slocaliteList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(slocaliteSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Slocalite testSlocalite = slocaliteList.get(slocaliteList.size() - 1);
        assertThat(testSlocalite.getCodelocalite()).isEqualTo(DEFAULT_CODELOCALITE);
        assertThat(testSlocalite.getNomlocalite()).isEqualTo(DEFAULT_NOMLOCALITE);
    }

    @Test
    @Transactional
    void createSlocaliteWithExistingId() throws Exception {
        // Create the Slocalite with an existing ID
        slocalite.setId(1L);

        int databaseSizeBeforeCreate = slocaliteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(slocaliteSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restSlocaliteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(slocalite)))
            .andExpect(status().isBadRequest());

        // Validate the Slocalite in the database
        List<Slocalite> slocaliteList = slocaliteRepository.findAll();
        assertThat(slocaliteList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(slocaliteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllSlocalites() throws Exception {
        // Initialize the database
        slocaliteRepository.saveAndFlush(slocalite);

        // Get all the slocaliteList
        restSlocaliteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(slocalite.getId().intValue())))
            .andExpect(jsonPath("$.[*].codelocalite").value(hasItem(DEFAULT_CODELOCALITE.intValue())))
            .andExpect(jsonPath("$.[*].nomlocalite").value(hasItem(DEFAULT_NOMLOCALITE)));
    }

    @Test
    @Transactional
    void getSlocalite() throws Exception {
        // Initialize the database
        slocaliteRepository.saveAndFlush(slocalite);

        // Get the slocalite
        restSlocaliteMockMvc
            .perform(get(ENTITY_API_URL_ID, slocalite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(slocalite.getId().intValue()))
            .andExpect(jsonPath("$.codelocalite").value(DEFAULT_CODELOCALITE.intValue()))
            .andExpect(jsonPath("$.nomlocalite").value(DEFAULT_NOMLOCALITE));
    }

    @Test
    @Transactional
    void getNonExistingSlocalite() throws Exception {
        // Get the slocalite
        restSlocaliteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSlocalite() throws Exception {
        // Initialize the database
        slocaliteRepository.saveAndFlush(slocalite);

        int databaseSizeBeforeUpdate = slocaliteRepository.findAll().size();
        slocaliteSearchRepository.save(slocalite);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(slocaliteSearchRepository.findAll());

        // Update the slocalite
        Slocalite updatedSlocalite = slocaliteRepository.findById(slocalite.getId()).get();
        // Disconnect from session so that the updates on updatedSlocalite are not directly saved in db
        em.detach(updatedSlocalite);
        updatedSlocalite.codelocalite(UPDATED_CODELOCALITE).nomlocalite(UPDATED_NOMLOCALITE);

        restSlocaliteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSlocalite.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSlocalite))
            )
            .andExpect(status().isOk());

        // Validate the Slocalite in the database
        List<Slocalite> slocaliteList = slocaliteRepository.findAll();
        assertThat(slocaliteList).hasSize(databaseSizeBeforeUpdate);
        Slocalite testSlocalite = slocaliteList.get(slocaliteList.size() - 1);
        assertThat(testSlocalite.getCodelocalite()).isEqualTo(UPDATED_CODELOCALITE);
        assertThat(testSlocalite.getNomlocalite()).isEqualTo(UPDATED_NOMLOCALITE);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(slocaliteSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Slocalite> slocaliteSearchList = IterableUtils.toList(slocaliteSearchRepository.findAll());
                Slocalite testSlocaliteSearch = slocaliteSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testSlocaliteSearch.getCodelocalite()).isEqualTo(UPDATED_CODELOCALITE);
                assertThat(testSlocaliteSearch.getNomlocalite()).isEqualTo(UPDATED_NOMLOCALITE);
            });
    }

    @Test
    @Transactional
    void putNonExistingSlocalite() throws Exception {
        int databaseSizeBeforeUpdate = slocaliteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(slocaliteSearchRepository.findAll());
        slocalite.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSlocaliteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, slocalite.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(slocalite))
            )
            .andExpect(status().isBadRequest());

        // Validate the Slocalite in the database
        List<Slocalite> slocaliteList = slocaliteRepository.findAll();
        assertThat(slocaliteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(slocaliteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchSlocalite() throws Exception {
        int databaseSizeBeforeUpdate = slocaliteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(slocaliteSearchRepository.findAll());
        slocalite.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSlocaliteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(slocalite))
            )
            .andExpect(status().isBadRequest());

        // Validate the Slocalite in the database
        List<Slocalite> slocaliteList = slocaliteRepository.findAll();
        assertThat(slocaliteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(slocaliteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSlocalite() throws Exception {
        int databaseSizeBeforeUpdate = slocaliteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(slocaliteSearchRepository.findAll());
        slocalite.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSlocaliteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(slocalite)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Slocalite in the database
        List<Slocalite> slocaliteList = slocaliteRepository.findAll();
        assertThat(slocaliteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(slocaliteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateSlocaliteWithPatch() throws Exception {
        // Initialize the database
        slocaliteRepository.saveAndFlush(slocalite);

        int databaseSizeBeforeUpdate = slocaliteRepository.findAll().size();

        // Update the slocalite using partial update
        Slocalite partialUpdatedSlocalite = new Slocalite();
        partialUpdatedSlocalite.setId(slocalite.getId());

        partialUpdatedSlocalite.codelocalite(UPDATED_CODELOCALITE).nomlocalite(UPDATED_NOMLOCALITE);

        restSlocaliteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSlocalite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSlocalite))
            )
            .andExpect(status().isOk());

        // Validate the Slocalite in the database
        List<Slocalite> slocaliteList = slocaliteRepository.findAll();
        assertThat(slocaliteList).hasSize(databaseSizeBeforeUpdate);
        Slocalite testSlocalite = slocaliteList.get(slocaliteList.size() - 1);
        assertThat(testSlocalite.getCodelocalite()).isEqualTo(UPDATED_CODELOCALITE);
        assertThat(testSlocalite.getNomlocalite()).isEqualTo(UPDATED_NOMLOCALITE);
    }

    @Test
    @Transactional
    void fullUpdateSlocaliteWithPatch() throws Exception {
        // Initialize the database
        slocaliteRepository.saveAndFlush(slocalite);

        int databaseSizeBeforeUpdate = slocaliteRepository.findAll().size();

        // Update the slocalite using partial update
        Slocalite partialUpdatedSlocalite = new Slocalite();
        partialUpdatedSlocalite.setId(slocalite.getId());

        partialUpdatedSlocalite.codelocalite(UPDATED_CODELOCALITE).nomlocalite(UPDATED_NOMLOCALITE);

        restSlocaliteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSlocalite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSlocalite))
            )
            .andExpect(status().isOk());

        // Validate the Slocalite in the database
        List<Slocalite> slocaliteList = slocaliteRepository.findAll();
        assertThat(slocaliteList).hasSize(databaseSizeBeforeUpdate);
        Slocalite testSlocalite = slocaliteList.get(slocaliteList.size() - 1);
        assertThat(testSlocalite.getCodelocalite()).isEqualTo(UPDATED_CODELOCALITE);
        assertThat(testSlocalite.getNomlocalite()).isEqualTo(UPDATED_NOMLOCALITE);
    }

    @Test
    @Transactional
    void patchNonExistingSlocalite() throws Exception {
        int databaseSizeBeforeUpdate = slocaliteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(slocaliteSearchRepository.findAll());
        slocalite.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSlocaliteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, slocalite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(slocalite))
            )
            .andExpect(status().isBadRequest());

        // Validate the Slocalite in the database
        List<Slocalite> slocaliteList = slocaliteRepository.findAll();
        assertThat(slocaliteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(slocaliteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSlocalite() throws Exception {
        int databaseSizeBeforeUpdate = slocaliteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(slocaliteSearchRepository.findAll());
        slocalite.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSlocaliteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(slocalite))
            )
            .andExpect(status().isBadRequest());

        // Validate the Slocalite in the database
        List<Slocalite> slocaliteList = slocaliteRepository.findAll();
        assertThat(slocaliteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(slocaliteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSlocalite() throws Exception {
        int databaseSizeBeforeUpdate = slocaliteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(slocaliteSearchRepository.findAll());
        slocalite.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSlocaliteMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(slocalite))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Slocalite in the database
        List<Slocalite> slocaliteList = slocaliteRepository.findAll();
        assertThat(slocaliteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(slocaliteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteSlocalite() throws Exception {
        // Initialize the database
        slocaliteRepository.saveAndFlush(slocalite);
        slocaliteRepository.save(slocalite);
        slocaliteSearchRepository.save(slocalite);

        int databaseSizeBeforeDelete = slocaliteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(slocaliteSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the slocalite
        restSlocaliteMockMvc
            .perform(delete(ENTITY_API_URL_ID, slocalite.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Slocalite> slocaliteList = slocaliteRepository.findAll();
        assertThat(slocaliteList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(slocaliteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchSlocalite() throws Exception {
        // Initialize the database
        slocalite = slocaliteRepository.saveAndFlush(slocalite);
        slocaliteSearchRepository.save(slocalite);

        // Search the slocalite
        restSlocaliteMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + slocalite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(slocalite.getId().intValue())))
            .andExpect(jsonPath("$.[*].codelocalite").value(hasItem(DEFAULT_CODELOCALITE.intValue())))
            .andExpect(jsonPath("$.[*].nomlocalite").value(hasItem(DEFAULT_NOMLOCALITE)));
    }
}
