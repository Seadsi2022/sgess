package dsi.sea.sgess.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dsi.sea.sgess.IntegrationTest;
import dsi.sea.sgess.domain.Egroupevariable;
import dsi.sea.sgess.repository.EgroupevariableRepository;
import dsi.sea.sgess.repository.search.EgroupevariableSearchRepository;
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
 * Integration tests for the {@link EgroupevariableResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EgroupevariableResourceIT {

    private static final Long DEFAULT_ORDREVARIABLE = 1L;
    private static final Long UPDATED_ORDREVARIABLE = 2L;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/egroupevariables";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/egroupevariables";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EgroupevariableRepository egroupevariableRepository;

    @Autowired
    private EgroupevariableSearchRepository egroupevariableSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEgroupevariableMockMvc;

    private Egroupevariable egroupevariable;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Egroupevariable createEntity(EntityManager em) {
        Egroupevariable egroupevariable = new Egroupevariable().ordrevariable(DEFAULT_ORDREVARIABLE).isActive(DEFAULT_IS_ACTIVE);
        return egroupevariable;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Egroupevariable createUpdatedEntity(EntityManager em) {
        Egroupevariable egroupevariable = new Egroupevariable().ordrevariable(UPDATED_ORDREVARIABLE).isActive(UPDATED_IS_ACTIVE);
        return egroupevariable;
    }

    @BeforeEach
    public void initTest() {
        egroupevariableSearchRepository.deleteAll();
        egroupevariable = createEntity(em);
    }

    @Test
    @Transactional
    void createEgroupevariable() throws Exception {
        int databaseSizeBeforeCreate = egroupevariableRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(egroupevariableSearchRepository.findAll());
        // Create the Egroupevariable
        restEgroupevariableMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(egroupevariable))
            )
            .andExpect(status().isCreated());

        // Validate the Egroupevariable in the database
        List<Egroupevariable> egroupevariableList = egroupevariableRepository.findAll();
        assertThat(egroupevariableList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(egroupevariableSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Egroupevariable testEgroupevariable = egroupevariableList.get(egroupevariableList.size() - 1);
        assertThat(testEgroupevariable.getOrdrevariable()).isEqualTo(DEFAULT_ORDREVARIABLE);
        assertThat(testEgroupevariable.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createEgroupevariableWithExistingId() throws Exception {
        // Create the Egroupevariable with an existing ID
        egroupevariable.setId(1L);

        int databaseSizeBeforeCreate = egroupevariableRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(egroupevariableSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restEgroupevariableMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(egroupevariable))
            )
            .andExpect(status().isBadRequest());

        // Validate the Egroupevariable in the database
        List<Egroupevariable> egroupevariableList = egroupevariableRepository.findAll();
        assertThat(egroupevariableList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(egroupevariableSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllEgroupevariables() throws Exception {
        // Initialize the database
        egroupevariableRepository.saveAndFlush(egroupevariable);

        // Get all the egroupevariableList
        restEgroupevariableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(egroupevariable.getId().intValue())))
            .andExpect(jsonPath("$.[*].ordrevariable").value(hasItem(DEFAULT_ORDREVARIABLE.intValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getEgroupevariable() throws Exception {
        // Initialize the database
        egroupevariableRepository.saveAndFlush(egroupevariable);

        // Get the egroupevariable
        restEgroupevariableMockMvc
            .perform(get(ENTITY_API_URL_ID, egroupevariable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(egroupevariable.getId().intValue()))
            .andExpect(jsonPath("$.ordrevariable").value(DEFAULT_ORDREVARIABLE.intValue()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingEgroupevariable() throws Exception {
        // Get the egroupevariable
        restEgroupevariableMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEgroupevariable() throws Exception {
        // Initialize the database
        egroupevariableRepository.saveAndFlush(egroupevariable);

        int databaseSizeBeforeUpdate = egroupevariableRepository.findAll().size();
        egroupevariableSearchRepository.save(egroupevariable);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(egroupevariableSearchRepository.findAll());

        // Update the egroupevariable
        Egroupevariable updatedEgroupevariable = egroupevariableRepository.findById(egroupevariable.getId()).get();
        // Disconnect from session so that the updates on updatedEgroupevariable are not directly saved in db
        em.detach(updatedEgroupevariable);
        updatedEgroupevariable.ordrevariable(UPDATED_ORDREVARIABLE).isActive(UPDATED_IS_ACTIVE);

        restEgroupevariableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEgroupevariable.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEgroupevariable))
            )
            .andExpect(status().isOk());

        // Validate the Egroupevariable in the database
        List<Egroupevariable> egroupevariableList = egroupevariableRepository.findAll();
        assertThat(egroupevariableList).hasSize(databaseSizeBeforeUpdate);
        Egroupevariable testEgroupevariable = egroupevariableList.get(egroupevariableList.size() - 1);
        assertThat(testEgroupevariable.getOrdrevariable()).isEqualTo(UPDATED_ORDREVARIABLE);
        assertThat(testEgroupevariable.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(egroupevariableSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Egroupevariable> egroupevariableSearchList = IterableUtils.toList(egroupevariableSearchRepository.findAll());
                Egroupevariable testEgroupevariableSearch = egroupevariableSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testEgroupevariableSearch.getOrdrevariable()).isEqualTo(UPDATED_ORDREVARIABLE);
                assertThat(testEgroupevariableSearch.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
            });
    }

    @Test
    @Transactional
    void putNonExistingEgroupevariable() throws Exception {
        int databaseSizeBeforeUpdate = egroupevariableRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(egroupevariableSearchRepository.findAll());
        egroupevariable.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEgroupevariableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, egroupevariable.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(egroupevariable))
            )
            .andExpect(status().isBadRequest());

        // Validate the Egroupevariable in the database
        List<Egroupevariable> egroupevariableList = egroupevariableRepository.findAll();
        assertThat(egroupevariableList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(egroupevariableSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchEgroupevariable() throws Exception {
        int databaseSizeBeforeUpdate = egroupevariableRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(egroupevariableSearchRepository.findAll());
        egroupevariable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEgroupevariableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(egroupevariable))
            )
            .andExpect(status().isBadRequest());

        // Validate the Egroupevariable in the database
        List<Egroupevariable> egroupevariableList = egroupevariableRepository.findAll();
        assertThat(egroupevariableList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(egroupevariableSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEgroupevariable() throws Exception {
        int databaseSizeBeforeUpdate = egroupevariableRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(egroupevariableSearchRepository.findAll());
        egroupevariable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEgroupevariableMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(egroupevariable))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Egroupevariable in the database
        List<Egroupevariable> egroupevariableList = egroupevariableRepository.findAll();
        assertThat(egroupevariableList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(egroupevariableSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateEgroupevariableWithPatch() throws Exception {
        // Initialize the database
        egroupevariableRepository.saveAndFlush(egroupevariable);

        int databaseSizeBeforeUpdate = egroupevariableRepository.findAll().size();

        // Update the egroupevariable using partial update
        Egroupevariable partialUpdatedEgroupevariable = new Egroupevariable();
        partialUpdatedEgroupevariable.setId(egroupevariable.getId());

        restEgroupevariableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEgroupevariable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEgroupevariable))
            )
            .andExpect(status().isOk());

        // Validate the Egroupevariable in the database
        List<Egroupevariable> egroupevariableList = egroupevariableRepository.findAll();
        assertThat(egroupevariableList).hasSize(databaseSizeBeforeUpdate);
        Egroupevariable testEgroupevariable = egroupevariableList.get(egroupevariableList.size() - 1);
        assertThat(testEgroupevariable.getOrdrevariable()).isEqualTo(DEFAULT_ORDREVARIABLE);
        assertThat(testEgroupevariable.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateEgroupevariableWithPatch() throws Exception {
        // Initialize the database
        egroupevariableRepository.saveAndFlush(egroupevariable);

        int databaseSizeBeforeUpdate = egroupevariableRepository.findAll().size();

        // Update the egroupevariable using partial update
        Egroupevariable partialUpdatedEgroupevariable = new Egroupevariable();
        partialUpdatedEgroupevariable.setId(egroupevariable.getId());

        partialUpdatedEgroupevariable.ordrevariable(UPDATED_ORDREVARIABLE).isActive(UPDATED_IS_ACTIVE);

        restEgroupevariableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEgroupevariable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEgroupevariable))
            )
            .andExpect(status().isOk());

        // Validate the Egroupevariable in the database
        List<Egroupevariable> egroupevariableList = egroupevariableRepository.findAll();
        assertThat(egroupevariableList).hasSize(databaseSizeBeforeUpdate);
        Egroupevariable testEgroupevariable = egroupevariableList.get(egroupevariableList.size() - 1);
        assertThat(testEgroupevariable.getOrdrevariable()).isEqualTo(UPDATED_ORDREVARIABLE);
        assertThat(testEgroupevariable.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingEgroupevariable() throws Exception {
        int databaseSizeBeforeUpdate = egroupevariableRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(egroupevariableSearchRepository.findAll());
        egroupevariable.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEgroupevariableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, egroupevariable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(egroupevariable))
            )
            .andExpect(status().isBadRequest());

        // Validate the Egroupevariable in the database
        List<Egroupevariable> egroupevariableList = egroupevariableRepository.findAll();
        assertThat(egroupevariableList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(egroupevariableSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEgroupevariable() throws Exception {
        int databaseSizeBeforeUpdate = egroupevariableRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(egroupevariableSearchRepository.findAll());
        egroupevariable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEgroupevariableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(egroupevariable))
            )
            .andExpect(status().isBadRequest());

        // Validate the Egroupevariable in the database
        List<Egroupevariable> egroupevariableList = egroupevariableRepository.findAll();
        assertThat(egroupevariableList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(egroupevariableSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEgroupevariable() throws Exception {
        int databaseSizeBeforeUpdate = egroupevariableRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(egroupevariableSearchRepository.findAll());
        egroupevariable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEgroupevariableMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(egroupevariable))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Egroupevariable in the database
        List<Egroupevariable> egroupevariableList = egroupevariableRepository.findAll();
        assertThat(egroupevariableList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(egroupevariableSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteEgroupevariable() throws Exception {
        // Initialize the database
        egroupevariableRepository.saveAndFlush(egroupevariable);
        egroupevariableRepository.save(egroupevariable);
        egroupevariableSearchRepository.save(egroupevariable);

        int databaseSizeBeforeDelete = egroupevariableRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(egroupevariableSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the egroupevariable
        restEgroupevariableMockMvc
            .perform(delete(ENTITY_API_URL_ID, egroupevariable.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Egroupevariable> egroupevariableList = egroupevariableRepository.findAll();
        assertThat(egroupevariableList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(egroupevariableSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchEgroupevariable() throws Exception {
        // Initialize the database
        egroupevariable = egroupevariableRepository.saveAndFlush(egroupevariable);
        egroupevariableSearchRepository.save(egroupevariable);

        // Search the egroupevariable
        restEgroupevariableMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + egroupevariable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(egroupevariable.getId().intValue())))
            .andExpect(jsonPath("$.[*].ordrevariable").value(hasItem(DEFAULT_ORDREVARIABLE.intValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }
}
