package dsi.sea.sgess.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dsi.sea.sgess.IntegrationTest;
import dsi.sea.sgess.domain.Eeventualite;
import dsi.sea.sgess.repository.EeventualiteRepository;
import dsi.sea.sgess.repository.search.EeventualiteSearchRepository;
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
 * Integration tests for the {@link EeventualiteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EeventualiteResourceIT {

    private static final String DEFAULT_EVENTUALITEVALUE = "AAAAAAAAAA";
    private static final String UPDATED_EVENTUALITEVALUE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/eeventualites";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/eeventualites";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EeventualiteRepository eeventualiteRepository;

    @Autowired
    private EeventualiteSearchRepository eeventualiteSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEeventualiteMockMvc;

    private Eeventualite eeventualite;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Eeventualite createEntity(EntityManager em) {
        Eeventualite eeventualite = new Eeventualite().eventualitevalue(DEFAULT_EVENTUALITEVALUE).isActive(DEFAULT_IS_ACTIVE);
        return eeventualite;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Eeventualite createUpdatedEntity(EntityManager em) {
        Eeventualite eeventualite = new Eeventualite().eventualitevalue(UPDATED_EVENTUALITEVALUE).isActive(UPDATED_IS_ACTIVE);
        return eeventualite;
    }

    @BeforeEach
    public void initTest() {
        eeventualiteSearchRepository.deleteAll();
        eeventualite = createEntity(em);
    }

    @Test
    @Transactional
    void createEeventualite() throws Exception {
        int databaseSizeBeforeCreate = eeventualiteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eeventualiteSearchRepository.findAll());
        // Create the Eeventualite
        restEeventualiteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eeventualite)))
            .andExpect(status().isCreated());

        // Validate the Eeventualite in the database
        List<Eeventualite> eeventualiteList = eeventualiteRepository.findAll();
        assertThat(eeventualiteList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(eeventualiteSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Eeventualite testEeventualite = eeventualiteList.get(eeventualiteList.size() - 1);
        assertThat(testEeventualite.getEventualitevalue()).isEqualTo(DEFAULT_EVENTUALITEVALUE);
        assertThat(testEeventualite.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createEeventualiteWithExistingId() throws Exception {
        // Create the Eeventualite with an existing ID
        eeventualite.setId(1L);

        int databaseSizeBeforeCreate = eeventualiteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eeventualiteSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restEeventualiteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eeventualite)))
            .andExpect(status().isBadRequest());

        // Validate the Eeventualite in the database
        List<Eeventualite> eeventualiteList = eeventualiteRepository.findAll();
        assertThat(eeventualiteList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(eeventualiteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllEeventualites() throws Exception {
        // Initialize the database
        eeventualiteRepository.saveAndFlush(eeventualite);

        // Get all the eeventualiteList
        restEeventualiteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eeventualite.getId().intValue())))
            .andExpect(jsonPath("$.[*].eventualitevalue").value(hasItem(DEFAULT_EVENTUALITEVALUE)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getEeventualite() throws Exception {
        // Initialize the database
        eeventualiteRepository.saveAndFlush(eeventualite);

        // Get the eeventualite
        restEeventualiteMockMvc
            .perform(get(ENTITY_API_URL_ID, eeventualite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eeventualite.getId().intValue()))
            .andExpect(jsonPath("$.eventualitevalue").value(DEFAULT_EVENTUALITEVALUE))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingEeventualite() throws Exception {
        // Get the eeventualite
        restEeventualiteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEeventualite() throws Exception {
        // Initialize the database
        eeventualiteRepository.saveAndFlush(eeventualite);

        int databaseSizeBeforeUpdate = eeventualiteRepository.findAll().size();
        eeventualiteSearchRepository.save(eeventualite);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eeventualiteSearchRepository.findAll());

        // Update the eeventualite
        Eeventualite updatedEeventualite = eeventualiteRepository.findById(eeventualite.getId()).get();
        // Disconnect from session so that the updates on updatedEeventualite are not directly saved in db
        em.detach(updatedEeventualite);
        updatedEeventualite.eventualitevalue(UPDATED_EVENTUALITEVALUE).isActive(UPDATED_IS_ACTIVE);

        restEeventualiteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEeventualite.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEeventualite))
            )
            .andExpect(status().isOk());

        // Validate the Eeventualite in the database
        List<Eeventualite> eeventualiteList = eeventualiteRepository.findAll();
        assertThat(eeventualiteList).hasSize(databaseSizeBeforeUpdate);
        Eeventualite testEeventualite = eeventualiteList.get(eeventualiteList.size() - 1);
        assertThat(testEeventualite.getEventualitevalue()).isEqualTo(UPDATED_EVENTUALITEVALUE);
        assertThat(testEeventualite.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(eeventualiteSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Eeventualite> eeventualiteSearchList = IterableUtils.toList(eeventualiteSearchRepository.findAll());
                Eeventualite testEeventualiteSearch = eeventualiteSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testEeventualiteSearch.getEventualitevalue()).isEqualTo(UPDATED_EVENTUALITEVALUE);
                assertThat(testEeventualiteSearch.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
            });
    }

    @Test
    @Transactional
    void putNonExistingEeventualite() throws Exception {
        int databaseSizeBeforeUpdate = eeventualiteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eeventualiteSearchRepository.findAll());
        eeventualite.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEeventualiteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eeventualite.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eeventualite))
            )
            .andExpect(status().isBadRequest());

        // Validate the Eeventualite in the database
        List<Eeventualite> eeventualiteList = eeventualiteRepository.findAll();
        assertThat(eeventualiteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(eeventualiteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchEeventualite() throws Exception {
        int databaseSizeBeforeUpdate = eeventualiteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eeventualiteSearchRepository.findAll());
        eeventualite.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEeventualiteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eeventualite))
            )
            .andExpect(status().isBadRequest());

        // Validate the Eeventualite in the database
        List<Eeventualite> eeventualiteList = eeventualiteRepository.findAll();
        assertThat(eeventualiteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(eeventualiteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEeventualite() throws Exception {
        int databaseSizeBeforeUpdate = eeventualiteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eeventualiteSearchRepository.findAll());
        eeventualite.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEeventualiteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eeventualite)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Eeventualite in the database
        List<Eeventualite> eeventualiteList = eeventualiteRepository.findAll();
        assertThat(eeventualiteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(eeventualiteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateEeventualiteWithPatch() throws Exception {
        // Initialize the database
        eeventualiteRepository.saveAndFlush(eeventualite);

        int databaseSizeBeforeUpdate = eeventualiteRepository.findAll().size();

        // Update the eeventualite using partial update
        Eeventualite partialUpdatedEeventualite = new Eeventualite();
        partialUpdatedEeventualite.setId(eeventualite.getId());

        partialUpdatedEeventualite.eventualitevalue(UPDATED_EVENTUALITEVALUE).isActive(UPDATED_IS_ACTIVE);

        restEeventualiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEeventualite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEeventualite))
            )
            .andExpect(status().isOk());

        // Validate the Eeventualite in the database
        List<Eeventualite> eeventualiteList = eeventualiteRepository.findAll();
        assertThat(eeventualiteList).hasSize(databaseSizeBeforeUpdate);
        Eeventualite testEeventualite = eeventualiteList.get(eeventualiteList.size() - 1);
        assertThat(testEeventualite.getEventualitevalue()).isEqualTo(UPDATED_EVENTUALITEVALUE);
        assertThat(testEeventualite.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateEeventualiteWithPatch() throws Exception {
        // Initialize the database
        eeventualiteRepository.saveAndFlush(eeventualite);

        int databaseSizeBeforeUpdate = eeventualiteRepository.findAll().size();

        // Update the eeventualite using partial update
        Eeventualite partialUpdatedEeventualite = new Eeventualite();
        partialUpdatedEeventualite.setId(eeventualite.getId());

        partialUpdatedEeventualite.eventualitevalue(UPDATED_EVENTUALITEVALUE).isActive(UPDATED_IS_ACTIVE);

        restEeventualiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEeventualite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEeventualite))
            )
            .andExpect(status().isOk());

        // Validate the Eeventualite in the database
        List<Eeventualite> eeventualiteList = eeventualiteRepository.findAll();
        assertThat(eeventualiteList).hasSize(databaseSizeBeforeUpdate);
        Eeventualite testEeventualite = eeventualiteList.get(eeventualiteList.size() - 1);
        assertThat(testEeventualite.getEventualitevalue()).isEqualTo(UPDATED_EVENTUALITEVALUE);
        assertThat(testEeventualite.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingEeventualite() throws Exception {
        int databaseSizeBeforeUpdate = eeventualiteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eeventualiteSearchRepository.findAll());
        eeventualite.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEeventualiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eeventualite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eeventualite))
            )
            .andExpect(status().isBadRequest());

        // Validate the Eeventualite in the database
        List<Eeventualite> eeventualiteList = eeventualiteRepository.findAll();
        assertThat(eeventualiteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(eeventualiteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEeventualite() throws Exception {
        int databaseSizeBeforeUpdate = eeventualiteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eeventualiteSearchRepository.findAll());
        eeventualite.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEeventualiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eeventualite))
            )
            .andExpect(status().isBadRequest());

        // Validate the Eeventualite in the database
        List<Eeventualite> eeventualiteList = eeventualiteRepository.findAll();
        assertThat(eeventualiteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(eeventualiteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEeventualite() throws Exception {
        int databaseSizeBeforeUpdate = eeventualiteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eeventualiteSearchRepository.findAll());
        eeventualite.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEeventualiteMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(eeventualite))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Eeventualite in the database
        List<Eeventualite> eeventualiteList = eeventualiteRepository.findAll();
        assertThat(eeventualiteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(eeventualiteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteEeventualite() throws Exception {
        // Initialize the database
        eeventualiteRepository.saveAndFlush(eeventualite);
        eeventualiteRepository.save(eeventualite);
        eeventualiteSearchRepository.save(eeventualite);

        int databaseSizeBeforeDelete = eeventualiteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eeventualiteSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the eeventualite
        restEeventualiteMockMvc
            .perform(delete(ENTITY_API_URL_ID, eeventualite.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Eeventualite> eeventualiteList = eeventualiteRepository.findAll();
        assertThat(eeventualiteList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(eeventualiteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchEeventualite() throws Exception {
        // Initialize the database
        eeventualite = eeventualiteRepository.saveAndFlush(eeventualite);
        eeventualiteSearchRepository.save(eeventualite);

        // Search the eeventualite
        restEeventualiteMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + eeventualite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eeventualite.getId().intValue())))
            .andExpect(jsonPath("$.[*].eventualitevalue").value(hasItem(DEFAULT_EVENTUALITEVALUE)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }
}
