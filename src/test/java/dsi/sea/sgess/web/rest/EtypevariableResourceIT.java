package dsi.sea.sgess.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dsi.sea.sgess.IntegrationTest;
import dsi.sea.sgess.domain.Etypevariable;
import dsi.sea.sgess.repository.EtypevariableRepository;
import dsi.sea.sgess.repository.search.EtypevariableSearchRepository;
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
 * Integration tests for the {@link EtypevariableResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EtypevariableResourceIT {

    private static final String DEFAULT_NOMTYPEVAR = "AAAAAAAAAA";
    private static final String UPDATED_NOMTYPEVAR = "BBBBBBBBBB";

    private static final String DEFAULT_DESCTYPEVARIABLE = "AAAAAAAAAA";
    private static final String UPDATED_DESCTYPEVARIABLE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/etypevariables";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/etypevariables";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EtypevariableRepository etypevariableRepository;

    @Autowired
    private EtypevariableSearchRepository etypevariableSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEtypevariableMockMvc;

    private Etypevariable etypevariable;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Etypevariable createEntity(EntityManager em) {
        Etypevariable etypevariable = new Etypevariable()
            .nomtypevar(DEFAULT_NOMTYPEVAR)
            .desctypevariable(DEFAULT_DESCTYPEVARIABLE)
            .isActive(DEFAULT_IS_ACTIVE);
        return etypevariable;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Etypevariable createUpdatedEntity(EntityManager em) {
        Etypevariable etypevariable = new Etypevariable()
            .nomtypevar(UPDATED_NOMTYPEVAR)
            .desctypevariable(UPDATED_DESCTYPEVARIABLE)
            .isActive(UPDATED_IS_ACTIVE);
        return etypevariable;
    }

    @BeforeEach
    public void initTest() {
        etypevariableSearchRepository.deleteAll();
        etypevariable = createEntity(em);
    }

    @Test
    @Transactional
    void createEtypevariable() throws Exception {
        int databaseSizeBeforeCreate = etypevariableRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(etypevariableSearchRepository.findAll());
        // Create the Etypevariable
        restEtypevariableMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(etypevariable)))
            .andExpect(status().isCreated());

        // Validate the Etypevariable in the database
        List<Etypevariable> etypevariableList = etypevariableRepository.findAll();
        assertThat(etypevariableList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(etypevariableSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Etypevariable testEtypevariable = etypevariableList.get(etypevariableList.size() - 1);
        assertThat(testEtypevariable.getNomtypevar()).isEqualTo(DEFAULT_NOMTYPEVAR);
        assertThat(testEtypevariable.getDesctypevariable()).isEqualTo(DEFAULT_DESCTYPEVARIABLE);
        assertThat(testEtypevariable.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createEtypevariableWithExistingId() throws Exception {
        // Create the Etypevariable with an existing ID
        etypevariable.setId(1L);

        int databaseSizeBeforeCreate = etypevariableRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(etypevariableSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restEtypevariableMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(etypevariable)))
            .andExpect(status().isBadRequest());

        // Validate the Etypevariable in the database
        List<Etypevariable> etypevariableList = etypevariableRepository.findAll();
        assertThat(etypevariableList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(etypevariableSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllEtypevariables() throws Exception {
        // Initialize the database
        etypevariableRepository.saveAndFlush(etypevariable);

        // Get all the etypevariableList
        restEtypevariableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(etypevariable.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomtypevar").value(hasItem(DEFAULT_NOMTYPEVAR)))
            .andExpect(jsonPath("$.[*].desctypevariable").value(hasItem(DEFAULT_DESCTYPEVARIABLE)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getEtypevariable() throws Exception {
        // Initialize the database
        etypevariableRepository.saveAndFlush(etypevariable);

        // Get the etypevariable
        restEtypevariableMockMvc
            .perform(get(ENTITY_API_URL_ID, etypevariable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(etypevariable.getId().intValue()))
            .andExpect(jsonPath("$.nomtypevar").value(DEFAULT_NOMTYPEVAR))
            .andExpect(jsonPath("$.desctypevariable").value(DEFAULT_DESCTYPEVARIABLE))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingEtypevariable() throws Exception {
        // Get the etypevariable
        restEtypevariableMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEtypevariable() throws Exception {
        // Initialize the database
        etypevariableRepository.saveAndFlush(etypevariable);

        int databaseSizeBeforeUpdate = etypevariableRepository.findAll().size();
        etypevariableSearchRepository.save(etypevariable);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(etypevariableSearchRepository.findAll());

        // Update the etypevariable
        Etypevariable updatedEtypevariable = etypevariableRepository.findById(etypevariable.getId()).get();
        // Disconnect from session so that the updates on updatedEtypevariable are not directly saved in db
        em.detach(updatedEtypevariable);
        updatedEtypevariable.nomtypevar(UPDATED_NOMTYPEVAR).desctypevariable(UPDATED_DESCTYPEVARIABLE).isActive(UPDATED_IS_ACTIVE);

        restEtypevariableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEtypevariable.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEtypevariable))
            )
            .andExpect(status().isOk());

        // Validate the Etypevariable in the database
        List<Etypevariable> etypevariableList = etypevariableRepository.findAll();
        assertThat(etypevariableList).hasSize(databaseSizeBeforeUpdate);
        Etypevariable testEtypevariable = etypevariableList.get(etypevariableList.size() - 1);
        assertThat(testEtypevariable.getNomtypevar()).isEqualTo(UPDATED_NOMTYPEVAR);
        assertThat(testEtypevariable.getDesctypevariable()).isEqualTo(UPDATED_DESCTYPEVARIABLE);
        assertThat(testEtypevariable.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(etypevariableSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Etypevariable> etypevariableSearchList = IterableUtils.toList(etypevariableSearchRepository.findAll());
                Etypevariable testEtypevariableSearch = etypevariableSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testEtypevariableSearch.getNomtypevar()).isEqualTo(UPDATED_NOMTYPEVAR);
                assertThat(testEtypevariableSearch.getDesctypevariable()).isEqualTo(UPDATED_DESCTYPEVARIABLE);
                assertThat(testEtypevariableSearch.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
            });
    }

    @Test
    @Transactional
    void putNonExistingEtypevariable() throws Exception {
        int databaseSizeBeforeUpdate = etypevariableRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(etypevariableSearchRepository.findAll());
        etypevariable.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEtypevariableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, etypevariable.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(etypevariable))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etypevariable in the database
        List<Etypevariable> etypevariableList = etypevariableRepository.findAll();
        assertThat(etypevariableList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(etypevariableSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchEtypevariable() throws Exception {
        int databaseSizeBeforeUpdate = etypevariableRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(etypevariableSearchRepository.findAll());
        etypevariable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtypevariableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(etypevariable))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etypevariable in the database
        List<Etypevariable> etypevariableList = etypevariableRepository.findAll();
        assertThat(etypevariableList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(etypevariableSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEtypevariable() throws Exception {
        int databaseSizeBeforeUpdate = etypevariableRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(etypevariableSearchRepository.findAll());
        etypevariable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtypevariableMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(etypevariable)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Etypevariable in the database
        List<Etypevariable> etypevariableList = etypevariableRepository.findAll();
        assertThat(etypevariableList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(etypevariableSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateEtypevariableWithPatch() throws Exception {
        // Initialize the database
        etypevariableRepository.saveAndFlush(etypevariable);

        int databaseSizeBeforeUpdate = etypevariableRepository.findAll().size();

        // Update the etypevariable using partial update
        Etypevariable partialUpdatedEtypevariable = new Etypevariable();
        partialUpdatedEtypevariable.setId(etypevariable.getId());

        partialUpdatedEtypevariable.nomtypevar(UPDATED_NOMTYPEVAR).desctypevariable(UPDATED_DESCTYPEVARIABLE);

        restEtypevariableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEtypevariable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEtypevariable))
            )
            .andExpect(status().isOk());

        // Validate the Etypevariable in the database
        List<Etypevariable> etypevariableList = etypevariableRepository.findAll();
        assertThat(etypevariableList).hasSize(databaseSizeBeforeUpdate);
        Etypevariable testEtypevariable = etypevariableList.get(etypevariableList.size() - 1);
        assertThat(testEtypevariable.getNomtypevar()).isEqualTo(UPDATED_NOMTYPEVAR);
        assertThat(testEtypevariable.getDesctypevariable()).isEqualTo(UPDATED_DESCTYPEVARIABLE);
        assertThat(testEtypevariable.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateEtypevariableWithPatch() throws Exception {
        // Initialize the database
        etypevariableRepository.saveAndFlush(etypevariable);

        int databaseSizeBeforeUpdate = etypevariableRepository.findAll().size();

        // Update the etypevariable using partial update
        Etypevariable partialUpdatedEtypevariable = new Etypevariable();
        partialUpdatedEtypevariable.setId(etypevariable.getId());

        partialUpdatedEtypevariable.nomtypevar(UPDATED_NOMTYPEVAR).desctypevariable(UPDATED_DESCTYPEVARIABLE).isActive(UPDATED_IS_ACTIVE);

        restEtypevariableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEtypevariable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEtypevariable))
            )
            .andExpect(status().isOk());

        // Validate the Etypevariable in the database
        List<Etypevariable> etypevariableList = etypevariableRepository.findAll();
        assertThat(etypevariableList).hasSize(databaseSizeBeforeUpdate);
        Etypevariable testEtypevariable = etypevariableList.get(etypevariableList.size() - 1);
        assertThat(testEtypevariable.getNomtypevar()).isEqualTo(UPDATED_NOMTYPEVAR);
        assertThat(testEtypevariable.getDesctypevariable()).isEqualTo(UPDATED_DESCTYPEVARIABLE);
        assertThat(testEtypevariable.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingEtypevariable() throws Exception {
        int databaseSizeBeforeUpdate = etypevariableRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(etypevariableSearchRepository.findAll());
        etypevariable.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEtypevariableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, etypevariable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(etypevariable))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etypevariable in the database
        List<Etypevariable> etypevariableList = etypevariableRepository.findAll();
        assertThat(etypevariableList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(etypevariableSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEtypevariable() throws Exception {
        int databaseSizeBeforeUpdate = etypevariableRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(etypevariableSearchRepository.findAll());
        etypevariable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtypevariableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(etypevariable))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etypevariable in the database
        List<Etypevariable> etypevariableList = etypevariableRepository.findAll();
        assertThat(etypevariableList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(etypevariableSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEtypevariable() throws Exception {
        int databaseSizeBeforeUpdate = etypevariableRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(etypevariableSearchRepository.findAll());
        etypevariable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtypevariableMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(etypevariable))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Etypevariable in the database
        List<Etypevariable> etypevariableList = etypevariableRepository.findAll();
        assertThat(etypevariableList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(etypevariableSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteEtypevariable() throws Exception {
        // Initialize the database
        etypevariableRepository.saveAndFlush(etypevariable);
        etypevariableRepository.save(etypevariable);
        etypevariableSearchRepository.save(etypevariable);

        int databaseSizeBeforeDelete = etypevariableRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(etypevariableSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the etypevariable
        restEtypevariableMockMvc
            .perform(delete(ENTITY_API_URL_ID, etypevariable.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Etypevariable> etypevariableList = etypevariableRepository.findAll();
        assertThat(etypevariableList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(etypevariableSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchEtypevariable() throws Exception {
        // Initialize the database
        etypevariable = etypevariableRepository.saveAndFlush(etypevariable);
        etypevariableSearchRepository.save(etypevariable);

        // Search the etypevariable
        restEtypevariableMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + etypevariable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(etypevariable.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomtypevar").value(hasItem(DEFAULT_NOMTYPEVAR)))
            .andExpect(jsonPath("$.[*].desctypevariable").value(hasItem(DEFAULT_DESCTYPEVARIABLE)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }
}
