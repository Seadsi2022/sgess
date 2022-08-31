package dsi.sea.sgess.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dsi.sea.sgess.IntegrationTest;
import dsi.sea.sgess.domain.Eattributvariable;
import dsi.sea.sgess.repository.EattributvariableRepository;
import dsi.sea.sgess.repository.search.EattributvariableSearchRepository;
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
 * Integration tests for the {@link EattributvariableResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EattributvariableResourceIT {

    private static final String DEFAULT_ATTRNAME = "AAAAAAAAAA";
    private static final String UPDATED_ATTRNAME = "BBBBBBBBBB";

    private static final String DEFAULT_ATTRVALUE = "AAAAAAAAAA";
    private static final String UPDATED_ATTRVALUE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/eattributvariables";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/eattributvariables";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EattributvariableRepository eattributvariableRepository;

    @Autowired
    private EattributvariableSearchRepository eattributvariableSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEattributvariableMockMvc;

    private Eattributvariable eattributvariable;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Eattributvariable createEntity(EntityManager em) {
        Eattributvariable eattributvariable = new Eattributvariable()
            .attrname(DEFAULT_ATTRNAME)
            .attrvalue(DEFAULT_ATTRVALUE)
            .isActive(DEFAULT_IS_ACTIVE);
        return eattributvariable;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Eattributvariable createUpdatedEntity(EntityManager em) {
        Eattributvariable eattributvariable = new Eattributvariable()
            .attrname(UPDATED_ATTRNAME)
            .attrvalue(UPDATED_ATTRVALUE)
            .isActive(UPDATED_IS_ACTIVE);
        return eattributvariable;
    }

    @BeforeEach
    public void initTest() {
        eattributvariableSearchRepository.deleteAll();
        eattributvariable = createEntity(em);
    }

    @Test
    @Transactional
    void createEattributvariable() throws Exception {
        int databaseSizeBeforeCreate = eattributvariableRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eattributvariableSearchRepository.findAll());
        // Create the Eattributvariable
        restEattributvariableMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eattributvariable))
            )
            .andExpect(status().isCreated());

        // Validate the Eattributvariable in the database
        List<Eattributvariable> eattributvariableList = eattributvariableRepository.findAll();
        assertThat(eattributvariableList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(eattributvariableSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Eattributvariable testEattributvariable = eattributvariableList.get(eattributvariableList.size() - 1);
        assertThat(testEattributvariable.getAttrname()).isEqualTo(DEFAULT_ATTRNAME);
        assertThat(testEattributvariable.getAttrvalue()).isEqualTo(DEFAULT_ATTRVALUE);
        assertThat(testEattributvariable.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createEattributvariableWithExistingId() throws Exception {
        // Create the Eattributvariable with an existing ID
        eattributvariable.setId(1L);

        int databaseSizeBeforeCreate = eattributvariableRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eattributvariableSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restEattributvariableMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eattributvariable))
            )
            .andExpect(status().isBadRequest());

        // Validate the Eattributvariable in the database
        List<Eattributvariable> eattributvariableList = eattributvariableRepository.findAll();
        assertThat(eattributvariableList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(eattributvariableSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllEattributvariables() throws Exception {
        // Initialize the database
        eattributvariableRepository.saveAndFlush(eattributvariable);

        // Get all the eattributvariableList
        restEattributvariableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eattributvariable.getId().intValue())))
            .andExpect(jsonPath("$.[*].attrname").value(hasItem(DEFAULT_ATTRNAME)))
            .andExpect(jsonPath("$.[*].attrvalue").value(hasItem(DEFAULT_ATTRVALUE)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getEattributvariable() throws Exception {
        // Initialize the database
        eattributvariableRepository.saveAndFlush(eattributvariable);

        // Get the eattributvariable
        restEattributvariableMockMvc
            .perform(get(ENTITY_API_URL_ID, eattributvariable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eattributvariable.getId().intValue()))
            .andExpect(jsonPath("$.attrname").value(DEFAULT_ATTRNAME))
            .andExpect(jsonPath("$.attrvalue").value(DEFAULT_ATTRVALUE))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingEattributvariable() throws Exception {
        // Get the eattributvariable
        restEattributvariableMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEattributvariable() throws Exception {
        // Initialize the database
        eattributvariableRepository.saveAndFlush(eattributvariable);

        int databaseSizeBeforeUpdate = eattributvariableRepository.findAll().size();
        eattributvariableSearchRepository.save(eattributvariable);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eattributvariableSearchRepository.findAll());

        // Update the eattributvariable
        Eattributvariable updatedEattributvariable = eattributvariableRepository.findById(eattributvariable.getId()).get();
        // Disconnect from session so that the updates on updatedEattributvariable are not directly saved in db
        em.detach(updatedEattributvariable);
        updatedEattributvariable.attrname(UPDATED_ATTRNAME).attrvalue(UPDATED_ATTRVALUE).isActive(UPDATED_IS_ACTIVE);

        restEattributvariableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEattributvariable.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEattributvariable))
            )
            .andExpect(status().isOk());

        // Validate the Eattributvariable in the database
        List<Eattributvariable> eattributvariableList = eattributvariableRepository.findAll();
        assertThat(eattributvariableList).hasSize(databaseSizeBeforeUpdate);
        Eattributvariable testEattributvariable = eattributvariableList.get(eattributvariableList.size() - 1);
        assertThat(testEattributvariable.getAttrname()).isEqualTo(UPDATED_ATTRNAME);
        assertThat(testEattributvariable.getAttrvalue()).isEqualTo(UPDATED_ATTRVALUE);
        assertThat(testEattributvariable.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(eattributvariableSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Eattributvariable> eattributvariableSearchList = IterableUtils.toList(eattributvariableSearchRepository.findAll());
                Eattributvariable testEattributvariableSearch = eattributvariableSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testEattributvariableSearch.getAttrname()).isEqualTo(UPDATED_ATTRNAME);
                assertThat(testEattributvariableSearch.getAttrvalue()).isEqualTo(UPDATED_ATTRVALUE);
                assertThat(testEattributvariableSearch.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
            });
    }

    @Test
    @Transactional
    void putNonExistingEattributvariable() throws Exception {
        int databaseSizeBeforeUpdate = eattributvariableRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eattributvariableSearchRepository.findAll());
        eattributvariable.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEattributvariableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eattributvariable.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eattributvariable))
            )
            .andExpect(status().isBadRequest());

        // Validate the Eattributvariable in the database
        List<Eattributvariable> eattributvariableList = eattributvariableRepository.findAll();
        assertThat(eattributvariableList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(eattributvariableSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchEattributvariable() throws Exception {
        int databaseSizeBeforeUpdate = eattributvariableRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eattributvariableSearchRepository.findAll());
        eattributvariable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEattributvariableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eattributvariable))
            )
            .andExpect(status().isBadRequest());

        // Validate the Eattributvariable in the database
        List<Eattributvariable> eattributvariableList = eattributvariableRepository.findAll();
        assertThat(eattributvariableList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(eattributvariableSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEattributvariable() throws Exception {
        int databaseSizeBeforeUpdate = eattributvariableRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eattributvariableSearchRepository.findAll());
        eattributvariable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEattributvariableMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eattributvariable))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Eattributvariable in the database
        List<Eattributvariable> eattributvariableList = eattributvariableRepository.findAll();
        assertThat(eattributvariableList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(eattributvariableSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateEattributvariableWithPatch() throws Exception {
        // Initialize the database
        eattributvariableRepository.saveAndFlush(eattributvariable);

        int databaseSizeBeforeUpdate = eattributvariableRepository.findAll().size();

        // Update the eattributvariable using partial update
        Eattributvariable partialUpdatedEattributvariable = new Eattributvariable();
        partialUpdatedEattributvariable.setId(eattributvariable.getId());

        partialUpdatedEattributvariable.attrvalue(UPDATED_ATTRVALUE);

        restEattributvariableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEattributvariable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEattributvariable))
            )
            .andExpect(status().isOk());

        // Validate the Eattributvariable in the database
        List<Eattributvariable> eattributvariableList = eattributvariableRepository.findAll();
        assertThat(eattributvariableList).hasSize(databaseSizeBeforeUpdate);
        Eattributvariable testEattributvariable = eattributvariableList.get(eattributvariableList.size() - 1);
        assertThat(testEattributvariable.getAttrname()).isEqualTo(DEFAULT_ATTRNAME);
        assertThat(testEattributvariable.getAttrvalue()).isEqualTo(UPDATED_ATTRVALUE);
        assertThat(testEattributvariable.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateEattributvariableWithPatch() throws Exception {
        // Initialize the database
        eattributvariableRepository.saveAndFlush(eattributvariable);

        int databaseSizeBeforeUpdate = eattributvariableRepository.findAll().size();

        // Update the eattributvariable using partial update
        Eattributvariable partialUpdatedEattributvariable = new Eattributvariable();
        partialUpdatedEattributvariable.setId(eattributvariable.getId());

        partialUpdatedEattributvariable.attrname(UPDATED_ATTRNAME).attrvalue(UPDATED_ATTRVALUE).isActive(UPDATED_IS_ACTIVE);

        restEattributvariableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEattributvariable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEattributvariable))
            )
            .andExpect(status().isOk());

        // Validate the Eattributvariable in the database
        List<Eattributvariable> eattributvariableList = eattributvariableRepository.findAll();
        assertThat(eattributvariableList).hasSize(databaseSizeBeforeUpdate);
        Eattributvariable testEattributvariable = eattributvariableList.get(eattributvariableList.size() - 1);
        assertThat(testEattributvariable.getAttrname()).isEqualTo(UPDATED_ATTRNAME);
        assertThat(testEattributvariable.getAttrvalue()).isEqualTo(UPDATED_ATTRVALUE);
        assertThat(testEattributvariable.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingEattributvariable() throws Exception {
        int databaseSizeBeforeUpdate = eattributvariableRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eattributvariableSearchRepository.findAll());
        eattributvariable.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEattributvariableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eattributvariable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eattributvariable))
            )
            .andExpect(status().isBadRequest());

        // Validate the Eattributvariable in the database
        List<Eattributvariable> eattributvariableList = eattributvariableRepository.findAll();
        assertThat(eattributvariableList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(eattributvariableSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEattributvariable() throws Exception {
        int databaseSizeBeforeUpdate = eattributvariableRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eattributvariableSearchRepository.findAll());
        eattributvariable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEattributvariableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eattributvariable))
            )
            .andExpect(status().isBadRequest());

        // Validate the Eattributvariable in the database
        List<Eattributvariable> eattributvariableList = eattributvariableRepository.findAll();
        assertThat(eattributvariableList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(eattributvariableSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEattributvariable() throws Exception {
        int databaseSizeBeforeUpdate = eattributvariableRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eattributvariableSearchRepository.findAll());
        eattributvariable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEattributvariableMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eattributvariable))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Eattributvariable in the database
        List<Eattributvariable> eattributvariableList = eattributvariableRepository.findAll();
        assertThat(eattributvariableList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(eattributvariableSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteEattributvariable() throws Exception {
        // Initialize the database
        eattributvariableRepository.saveAndFlush(eattributvariable);
        eattributvariableRepository.save(eattributvariable);
        eattributvariableSearchRepository.save(eattributvariable);

        int databaseSizeBeforeDelete = eattributvariableRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eattributvariableSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the eattributvariable
        restEattributvariableMockMvc
            .perform(delete(ENTITY_API_URL_ID, eattributvariable.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Eattributvariable> eattributvariableList = eattributvariableRepository.findAll();
        assertThat(eattributvariableList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(eattributvariableSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchEattributvariable() throws Exception {
        // Initialize the database
        eattributvariable = eattributvariableRepository.saveAndFlush(eattributvariable);
        eattributvariableSearchRepository.save(eattributvariable);

        // Search the eattributvariable
        restEattributvariableMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + eattributvariable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eattributvariable.getId().intValue())))
            .andExpect(jsonPath("$.[*].attrname").value(hasItem(DEFAULT_ATTRNAME)))
            .andExpect(jsonPath("$.[*].attrvalue").value(hasItem(DEFAULT_ATTRVALUE)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }
}
