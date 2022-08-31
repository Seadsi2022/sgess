package dsi.sea.sgess.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dsi.sea.sgess.IntegrationTest;
import dsi.sea.sgess.domain.Eunite;
import dsi.sea.sgess.repository.EuniteRepository;
import dsi.sea.sgess.repository.search.EuniteSearchRepository;
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
 * Integration tests for the {@link EuniteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EuniteResourceIT {

    private static final String DEFAULT_NOMUNITE = "AAAAAAAAAA";
    private static final String UPDATED_NOMUNITE = "BBBBBBBBBB";

    private static final String DEFAULT_SYMBOLEUNITE = "AAAAAAAAAA";
    private static final String UPDATED_SYMBOLEUNITE = "BBBBBBBBBB";

    private static final Long DEFAULT_FACTEUR = 1L;
    private static final Long UPDATED_FACTEUR = 2L;

    private static final String ENTITY_API_URL = "/api/eunites";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/eunites";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EuniteRepository euniteRepository;

    @Autowired
    private EuniteSearchRepository euniteSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEuniteMockMvc;

    private Eunite eunite;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Eunite createEntity(EntityManager em) {
        Eunite eunite = new Eunite().nomunite(DEFAULT_NOMUNITE).symboleunite(DEFAULT_SYMBOLEUNITE).facteur(DEFAULT_FACTEUR);
        return eunite;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Eunite createUpdatedEntity(EntityManager em) {
        Eunite eunite = new Eunite().nomunite(UPDATED_NOMUNITE).symboleunite(UPDATED_SYMBOLEUNITE).facteur(UPDATED_FACTEUR);
        return eunite;
    }

    @BeforeEach
    public void initTest() {
        euniteSearchRepository.deleteAll();
        eunite = createEntity(em);
    }

    @Test
    @Transactional
    void createEunite() throws Exception {
        int databaseSizeBeforeCreate = euniteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(euniteSearchRepository.findAll());
        // Create the Eunite
        restEuniteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eunite)))
            .andExpect(status().isCreated());

        // Validate the Eunite in the database
        List<Eunite> euniteList = euniteRepository.findAll();
        assertThat(euniteList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(euniteSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Eunite testEunite = euniteList.get(euniteList.size() - 1);
        assertThat(testEunite.getNomunite()).isEqualTo(DEFAULT_NOMUNITE);
        assertThat(testEunite.getSymboleunite()).isEqualTo(DEFAULT_SYMBOLEUNITE);
        assertThat(testEunite.getFacteur()).isEqualTo(DEFAULT_FACTEUR);
    }

    @Test
    @Transactional
    void createEuniteWithExistingId() throws Exception {
        // Create the Eunite with an existing ID
        eunite.setId(1L);

        int databaseSizeBeforeCreate = euniteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(euniteSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restEuniteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eunite)))
            .andExpect(status().isBadRequest());

        // Validate the Eunite in the database
        List<Eunite> euniteList = euniteRepository.findAll();
        assertThat(euniteList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(euniteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllEunites() throws Exception {
        // Initialize the database
        euniteRepository.saveAndFlush(eunite);

        // Get all the euniteList
        restEuniteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eunite.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomunite").value(hasItem(DEFAULT_NOMUNITE)))
            .andExpect(jsonPath("$.[*].symboleunite").value(hasItem(DEFAULT_SYMBOLEUNITE)))
            .andExpect(jsonPath("$.[*].facteur").value(hasItem(DEFAULT_FACTEUR.intValue())));
    }

    @Test
    @Transactional
    void getEunite() throws Exception {
        // Initialize the database
        euniteRepository.saveAndFlush(eunite);

        // Get the eunite
        restEuniteMockMvc
            .perform(get(ENTITY_API_URL_ID, eunite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eunite.getId().intValue()))
            .andExpect(jsonPath("$.nomunite").value(DEFAULT_NOMUNITE))
            .andExpect(jsonPath("$.symboleunite").value(DEFAULT_SYMBOLEUNITE))
            .andExpect(jsonPath("$.facteur").value(DEFAULT_FACTEUR.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingEunite() throws Exception {
        // Get the eunite
        restEuniteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEunite() throws Exception {
        // Initialize the database
        euniteRepository.saveAndFlush(eunite);

        int databaseSizeBeforeUpdate = euniteRepository.findAll().size();
        euniteSearchRepository.save(eunite);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(euniteSearchRepository.findAll());

        // Update the eunite
        Eunite updatedEunite = euniteRepository.findById(eunite.getId()).get();
        // Disconnect from session so that the updates on updatedEunite are not directly saved in db
        em.detach(updatedEunite);
        updatedEunite.nomunite(UPDATED_NOMUNITE).symboleunite(UPDATED_SYMBOLEUNITE).facteur(UPDATED_FACTEUR);

        restEuniteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEunite.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEunite))
            )
            .andExpect(status().isOk());

        // Validate the Eunite in the database
        List<Eunite> euniteList = euniteRepository.findAll();
        assertThat(euniteList).hasSize(databaseSizeBeforeUpdate);
        Eunite testEunite = euniteList.get(euniteList.size() - 1);
        assertThat(testEunite.getNomunite()).isEqualTo(UPDATED_NOMUNITE);
        assertThat(testEunite.getSymboleunite()).isEqualTo(UPDATED_SYMBOLEUNITE);
        assertThat(testEunite.getFacteur()).isEqualTo(UPDATED_FACTEUR);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(euniteSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Eunite> euniteSearchList = IterableUtils.toList(euniteSearchRepository.findAll());
                Eunite testEuniteSearch = euniteSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testEuniteSearch.getNomunite()).isEqualTo(UPDATED_NOMUNITE);
                assertThat(testEuniteSearch.getSymboleunite()).isEqualTo(UPDATED_SYMBOLEUNITE);
                assertThat(testEuniteSearch.getFacteur()).isEqualTo(UPDATED_FACTEUR);
            });
    }

    @Test
    @Transactional
    void putNonExistingEunite() throws Exception {
        int databaseSizeBeforeUpdate = euniteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(euniteSearchRepository.findAll());
        eunite.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEuniteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eunite.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eunite))
            )
            .andExpect(status().isBadRequest());

        // Validate the Eunite in the database
        List<Eunite> euniteList = euniteRepository.findAll();
        assertThat(euniteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(euniteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchEunite() throws Exception {
        int databaseSizeBeforeUpdate = euniteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(euniteSearchRepository.findAll());
        eunite.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEuniteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eunite))
            )
            .andExpect(status().isBadRequest());

        // Validate the Eunite in the database
        List<Eunite> euniteList = euniteRepository.findAll();
        assertThat(euniteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(euniteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEunite() throws Exception {
        int databaseSizeBeforeUpdate = euniteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(euniteSearchRepository.findAll());
        eunite.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEuniteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eunite)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Eunite in the database
        List<Eunite> euniteList = euniteRepository.findAll();
        assertThat(euniteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(euniteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateEuniteWithPatch() throws Exception {
        // Initialize the database
        euniteRepository.saveAndFlush(eunite);

        int databaseSizeBeforeUpdate = euniteRepository.findAll().size();

        // Update the eunite using partial update
        Eunite partialUpdatedEunite = new Eunite();
        partialUpdatedEunite.setId(eunite.getId());

        partialUpdatedEunite.nomunite(UPDATED_NOMUNITE).symboleunite(UPDATED_SYMBOLEUNITE);

        restEuniteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEunite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEunite))
            )
            .andExpect(status().isOk());

        // Validate the Eunite in the database
        List<Eunite> euniteList = euniteRepository.findAll();
        assertThat(euniteList).hasSize(databaseSizeBeforeUpdate);
        Eunite testEunite = euniteList.get(euniteList.size() - 1);
        assertThat(testEunite.getNomunite()).isEqualTo(UPDATED_NOMUNITE);
        assertThat(testEunite.getSymboleunite()).isEqualTo(UPDATED_SYMBOLEUNITE);
        assertThat(testEunite.getFacteur()).isEqualTo(DEFAULT_FACTEUR);
    }

    @Test
    @Transactional
    void fullUpdateEuniteWithPatch() throws Exception {
        // Initialize the database
        euniteRepository.saveAndFlush(eunite);

        int databaseSizeBeforeUpdate = euniteRepository.findAll().size();

        // Update the eunite using partial update
        Eunite partialUpdatedEunite = new Eunite();
        partialUpdatedEunite.setId(eunite.getId());

        partialUpdatedEunite.nomunite(UPDATED_NOMUNITE).symboleunite(UPDATED_SYMBOLEUNITE).facteur(UPDATED_FACTEUR);

        restEuniteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEunite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEunite))
            )
            .andExpect(status().isOk());

        // Validate the Eunite in the database
        List<Eunite> euniteList = euniteRepository.findAll();
        assertThat(euniteList).hasSize(databaseSizeBeforeUpdate);
        Eunite testEunite = euniteList.get(euniteList.size() - 1);
        assertThat(testEunite.getNomunite()).isEqualTo(UPDATED_NOMUNITE);
        assertThat(testEunite.getSymboleunite()).isEqualTo(UPDATED_SYMBOLEUNITE);
        assertThat(testEunite.getFacteur()).isEqualTo(UPDATED_FACTEUR);
    }

    @Test
    @Transactional
    void patchNonExistingEunite() throws Exception {
        int databaseSizeBeforeUpdate = euniteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(euniteSearchRepository.findAll());
        eunite.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEuniteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eunite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eunite))
            )
            .andExpect(status().isBadRequest());

        // Validate the Eunite in the database
        List<Eunite> euniteList = euniteRepository.findAll();
        assertThat(euniteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(euniteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEunite() throws Exception {
        int databaseSizeBeforeUpdate = euniteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(euniteSearchRepository.findAll());
        eunite.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEuniteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eunite))
            )
            .andExpect(status().isBadRequest());

        // Validate the Eunite in the database
        List<Eunite> euniteList = euniteRepository.findAll();
        assertThat(euniteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(euniteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEunite() throws Exception {
        int databaseSizeBeforeUpdate = euniteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(euniteSearchRepository.findAll());
        eunite.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEuniteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(eunite)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Eunite in the database
        List<Eunite> euniteList = euniteRepository.findAll();
        assertThat(euniteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(euniteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteEunite() throws Exception {
        // Initialize the database
        euniteRepository.saveAndFlush(eunite);
        euniteRepository.save(eunite);
        euniteSearchRepository.save(eunite);

        int databaseSizeBeforeDelete = euniteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(euniteSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the eunite
        restEuniteMockMvc
            .perform(delete(ENTITY_API_URL_ID, eunite.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Eunite> euniteList = euniteRepository.findAll();
        assertThat(euniteList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(euniteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchEunite() throws Exception {
        // Initialize the database
        eunite = euniteRepository.saveAndFlush(eunite);
        euniteSearchRepository.save(eunite);

        // Search the eunite
        restEuniteMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + eunite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eunite.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomunite").value(hasItem(DEFAULT_NOMUNITE)))
            .andExpect(jsonPath("$.[*].symboleunite").value(hasItem(DEFAULT_SYMBOLEUNITE)))
            .andExpect(jsonPath("$.[*].facteur").value(hasItem(DEFAULT_FACTEUR.intValue())));
    }
}
