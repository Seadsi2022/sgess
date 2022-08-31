package dsi.sea.sgess.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dsi.sea.sgess.IntegrationTest;
import dsi.sea.sgess.domain.Eeventualitevariable;
import dsi.sea.sgess.repository.EeventualitevariableRepository;
import dsi.sea.sgess.repository.search.EeventualitevariableSearchRepository;
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
 * Integration tests for the {@link EeventualitevariableResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EeventualitevariableResourceIT {

    private static final String ENTITY_API_URL = "/api/eeventualitevariables";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/eeventualitevariables";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EeventualitevariableRepository eeventualitevariableRepository;

    @Autowired
    private EeventualitevariableSearchRepository eeventualitevariableSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEeventualitevariableMockMvc;

    private Eeventualitevariable eeventualitevariable;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Eeventualitevariable createEntity(EntityManager em) {
        Eeventualitevariable eeventualitevariable = new Eeventualitevariable();
        return eeventualitevariable;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Eeventualitevariable createUpdatedEntity(EntityManager em) {
        Eeventualitevariable eeventualitevariable = new Eeventualitevariable();
        return eeventualitevariable;
    }

    @BeforeEach
    public void initTest() {
        eeventualitevariableSearchRepository.deleteAll();
        eeventualitevariable = createEntity(em);
    }

    @Test
    @Transactional
    void createEeventualitevariable() throws Exception {
        int databaseSizeBeforeCreate = eeventualitevariableRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eeventualitevariableSearchRepository.findAll());
        // Create the Eeventualitevariable
        restEeventualitevariableMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eeventualitevariable))
            )
            .andExpect(status().isCreated());

        // Validate the Eeventualitevariable in the database
        List<Eeventualitevariable> eeventualitevariableList = eeventualitevariableRepository.findAll();
        assertThat(eeventualitevariableList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(eeventualitevariableSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Eeventualitevariable testEeventualitevariable = eeventualitevariableList.get(eeventualitevariableList.size() - 1);
    }

    @Test
    @Transactional
    void createEeventualitevariableWithExistingId() throws Exception {
        // Create the Eeventualitevariable with an existing ID
        eeventualitevariable.setId(1L);

        int databaseSizeBeforeCreate = eeventualitevariableRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eeventualitevariableSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restEeventualitevariableMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eeventualitevariable))
            )
            .andExpect(status().isBadRequest());

        // Validate the Eeventualitevariable in the database
        List<Eeventualitevariable> eeventualitevariableList = eeventualitevariableRepository.findAll();
        assertThat(eeventualitevariableList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(eeventualitevariableSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllEeventualitevariables() throws Exception {
        // Initialize the database
        eeventualitevariableRepository.saveAndFlush(eeventualitevariable);

        // Get all the eeventualitevariableList
        restEeventualitevariableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eeventualitevariable.getId().intValue())));
    }

    @Test
    @Transactional
    void getEeventualitevariable() throws Exception {
        // Initialize the database
        eeventualitevariableRepository.saveAndFlush(eeventualitevariable);

        // Get the eeventualitevariable
        restEeventualitevariableMockMvc
            .perform(get(ENTITY_API_URL_ID, eeventualitevariable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eeventualitevariable.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingEeventualitevariable() throws Exception {
        // Get the eeventualitevariable
        restEeventualitevariableMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEeventualitevariable() throws Exception {
        // Initialize the database
        eeventualitevariableRepository.saveAndFlush(eeventualitevariable);

        int databaseSizeBeforeUpdate = eeventualitevariableRepository.findAll().size();
        eeventualitevariableSearchRepository.save(eeventualitevariable);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eeventualitevariableSearchRepository.findAll());

        // Update the eeventualitevariable
        Eeventualitevariable updatedEeventualitevariable = eeventualitevariableRepository.findById(eeventualitevariable.getId()).get();
        // Disconnect from session so that the updates on updatedEeventualitevariable are not directly saved in db
        em.detach(updatedEeventualitevariable);

        restEeventualitevariableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEeventualitevariable.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEeventualitevariable))
            )
            .andExpect(status().isOk());

        // Validate the Eeventualitevariable in the database
        List<Eeventualitevariable> eeventualitevariableList = eeventualitevariableRepository.findAll();
        assertThat(eeventualitevariableList).hasSize(databaseSizeBeforeUpdate);
        Eeventualitevariable testEeventualitevariable = eeventualitevariableList.get(eeventualitevariableList.size() - 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(eeventualitevariableSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Eeventualitevariable> eeventualitevariableSearchList = IterableUtils.toList(
                    eeventualitevariableSearchRepository.findAll()
                );
                Eeventualitevariable testEeventualitevariableSearch = eeventualitevariableSearchList.get(searchDatabaseSizeAfter - 1);
            });
    }

    @Test
    @Transactional
    void putNonExistingEeventualitevariable() throws Exception {
        int databaseSizeBeforeUpdate = eeventualitevariableRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eeventualitevariableSearchRepository.findAll());
        eeventualitevariable.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEeventualitevariableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eeventualitevariable.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eeventualitevariable))
            )
            .andExpect(status().isBadRequest());

        // Validate the Eeventualitevariable in the database
        List<Eeventualitevariable> eeventualitevariableList = eeventualitevariableRepository.findAll();
        assertThat(eeventualitevariableList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(eeventualitevariableSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchEeventualitevariable() throws Exception {
        int databaseSizeBeforeUpdate = eeventualitevariableRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eeventualitevariableSearchRepository.findAll());
        eeventualitevariable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEeventualitevariableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eeventualitevariable))
            )
            .andExpect(status().isBadRequest());

        // Validate the Eeventualitevariable in the database
        List<Eeventualitevariable> eeventualitevariableList = eeventualitevariableRepository.findAll();
        assertThat(eeventualitevariableList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(eeventualitevariableSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEeventualitevariable() throws Exception {
        int databaseSizeBeforeUpdate = eeventualitevariableRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eeventualitevariableSearchRepository.findAll());
        eeventualitevariable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEeventualitevariableMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eeventualitevariable))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Eeventualitevariable in the database
        List<Eeventualitevariable> eeventualitevariableList = eeventualitevariableRepository.findAll();
        assertThat(eeventualitevariableList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(eeventualitevariableSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateEeventualitevariableWithPatch() throws Exception {
        // Initialize the database
        eeventualitevariableRepository.saveAndFlush(eeventualitevariable);

        int databaseSizeBeforeUpdate = eeventualitevariableRepository.findAll().size();

        // Update the eeventualitevariable using partial update
        Eeventualitevariable partialUpdatedEeventualitevariable = new Eeventualitevariable();
        partialUpdatedEeventualitevariable.setId(eeventualitevariable.getId());

        restEeventualitevariableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEeventualitevariable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEeventualitevariable))
            )
            .andExpect(status().isOk());

        // Validate the Eeventualitevariable in the database
        List<Eeventualitevariable> eeventualitevariableList = eeventualitevariableRepository.findAll();
        assertThat(eeventualitevariableList).hasSize(databaseSizeBeforeUpdate);
        Eeventualitevariable testEeventualitevariable = eeventualitevariableList.get(eeventualitevariableList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateEeventualitevariableWithPatch() throws Exception {
        // Initialize the database
        eeventualitevariableRepository.saveAndFlush(eeventualitevariable);

        int databaseSizeBeforeUpdate = eeventualitevariableRepository.findAll().size();

        // Update the eeventualitevariable using partial update
        Eeventualitevariable partialUpdatedEeventualitevariable = new Eeventualitevariable();
        partialUpdatedEeventualitevariable.setId(eeventualitevariable.getId());

        restEeventualitevariableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEeventualitevariable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEeventualitevariable))
            )
            .andExpect(status().isOk());

        // Validate the Eeventualitevariable in the database
        List<Eeventualitevariable> eeventualitevariableList = eeventualitevariableRepository.findAll();
        assertThat(eeventualitevariableList).hasSize(databaseSizeBeforeUpdate);
        Eeventualitevariable testEeventualitevariable = eeventualitevariableList.get(eeventualitevariableList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingEeventualitevariable() throws Exception {
        int databaseSizeBeforeUpdate = eeventualitevariableRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eeventualitevariableSearchRepository.findAll());
        eeventualitevariable.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEeventualitevariableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eeventualitevariable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eeventualitevariable))
            )
            .andExpect(status().isBadRequest());

        // Validate the Eeventualitevariable in the database
        List<Eeventualitevariable> eeventualitevariableList = eeventualitevariableRepository.findAll();
        assertThat(eeventualitevariableList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(eeventualitevariableSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEeventualitevariable() throws Exception {
        int databaseSizeBeforeUpdate = eeventualitevariableRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eeventualitevariableSearchRepository.findAll());
        eeventualitevariable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEeventualitevariableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eeventualitevariable))
            )
            .andExpect(status().isBadRequest());

        // Validate the Eeventualitevariable in the database
        List<Eeventualitevariable> eeventualitevariableList = eeventualitevariableRepository.findAll();
        assertThat(eeventualitevariableList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(eeventualitevariableSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEeventualitevariable() throws Exception {
        int databaseSizeBeforeUpdate = eeventualitevariableRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eeventualitevariableSearchRepository.findAll());
        eeventualitevariable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEeventualitevariableMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eeventualitevariable))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Eeventualitevariable in the database
        List<Eeventualitevariable> eeventualitevariableList = eeventualitevariableRepository.findAll();
        assertThat(eeventualitevariableList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(eeventualitevariableSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteEeventualitevariable() throws Exception {
        // Initialize the database
        eeventualitevariableRepository.saveAndFlush(eeventualitevariable);
        eeventualitevariableRepository.save(eeventualitevariable);
        eeventualitevariableSearchRepository.save(eeventualitevariable);

        int databaseSizeBeforeDelete = eeventualitevariableRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eeventualitevariableSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the eeventualitevariable
        restEeventualitevariableMockMvc
            .perform(delete(ENTITY_API_URL_ID, eeventualitevariable.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Eeventualitevariable> eeventualitevariableList = eeventualitevariableRepository.findAll();
        assertThat(eeventualitevariableList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(eeventualitevariableSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchEeventualitevariable() throws Exception {
        // Initialize the database
        eeventualitevariable = eeventualitevariableRepository.saveAndFlush(eeventualitevariable);
        eeventualitevariableSearchRepository.save(eeventualitevariable);

        // Search the eeventualitevariable
        restEeventualitevariableMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + eeventualitevariable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eeventualitevariable.getId().intValue())));
    }
}
