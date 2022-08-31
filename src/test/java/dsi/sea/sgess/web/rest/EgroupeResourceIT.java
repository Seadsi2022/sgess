package dsi.sea.sgess.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dsi.sea.sgess.IntegrationTest;
import dsi.sea.sgess.domain.Egroupe;
import dsi.sea.sgess.repository.EgroupeRepository;
import dsi.sea.sgess.repository.search.EgroupeSearchRepository;
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
 * Integration tests for the {@link EgroupeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EgroupeResourceIT {

    private static final String DEFAULT_TITREGROUPE = "AAAAAAAAAA";
    private static final String UPDATED_TITREGROUPE = "BBBBBBBBBB";

    private static final Long DEFAULT_ORDREGROUPE = 1L;
    private static final Long UPDATED_ORDREGROUPE = 2L;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/egroupes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/egroupes";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EgroupeRepository egroupeRepository;

    @Autowired
    private EgroupeSearchRepository egroupeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEgroupeMockMvc;

    private Egroupe egroupe;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Egroupe createEntity(EntityManager em) {
        Egroupe egroupe = new Egroupe().titregroupe(DEFAULT_TITREGROUPE).ordregroupe(DEFAULT_ORDREGROUPE).isActive(DEFAULT_IS_ACTIVE);
        return egroupe;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Egroupe createUpdatedEntity(EntityManager em) {
        Egroupe egroupe = new Egroupe().titregroupe(UPDATED_TITREGROUPE).ordregroupe(UPDATED_ORDREGROUPE).isActive(UPDATED_IS_ACTIVE);
        return egroupe;
    }

    @BeforeEach
    public void initTest() {
        egroupeSearchRepository.deleteAll();
        egroupe = createEntity(em);
    }

    @Test
    @Transactional
    void createEgroupe() throws Exception {
        int databaseSizeBeforeCreate = egroupeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(egroupeSearchRepository.findAll());
        // Create the Egroupe
        restEgroupeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(egroupe)))
            .andExpect(status().isCreated());

        // Validate the Egroupe in the database
        List<Egroupe> egroupeList = egroupeRepository.findAll();
        assertThat(egroupeList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(egroupeSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Egroupe testEgroupe = egroupeList.get(egroupeList.size() - 1);
        assertThat(testEgroupe.getTitregroupe()).isEqualTo(DEFAULT_TITREGROUPE);
        assertThat(testEgroupe.getOrdregroupe()).isEqualTo(DEFAULT_ORDREGROUPE);
        assertThat(testEgroupe.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createEgroupeWithExistingId() throws Exception {
        // Create the Egroupe with an existing ID
        egroupe.setId(1L);

        int databaseSizeBeforeCreate = egroupeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(egroupeSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restEgroupeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(egroupe)))
            .andExpect(status().isBadRequest());

        // Validate the Egroupe in the database
        List<Egroupe> egroupeList = egroupeRepository.findAll();
        assertThat(egroupeList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(egroupeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllEgroupes() throws Exception {
        // Initialize the database
        egroupeRepository.saveAndFlush(egroupe);

        // Get all the egroupeList
        restEgroupeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(egroupe.getId().intValue())))
            .andExpect(jsonPath("$.[*].titregroupe").value(hasItem(DEFAULT_TITREGROUPE)))
            .andExpect(jsonPath("$.[*].ordregroupe").value(hasItem(DEFAULT_ORDREGROUPE.intValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getEgroupe() throws Exception {
        // Initialize the database
        egroupeRepository.saveAndFlush(egroupe);

        // Get the egroupe
        restEgroupeMockMvc
            .perform(get(ENTITY_API_URL_ID, egroupe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(egroupe.getId().intValue()))
            .andExpect(jsonPath("$.titregroupe").value(DEFAULT_TITREGROUPE))
            .andExpect(jsonPath("$.ordregroupe").value(DEFAULT_ORDREGROUPE.intValue()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingEgroupe() throws Exception {
        // Get the egroupe
        restEgroupeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEgroupe() throws Exception {
        // Initialize the database
        egroupeRepository.saveAndFlush(egroupe);

        int databaseSizeBeforeUpdate = egroupeRepository.findAll().size();
        egroupeSearchRepository.save(egroupe);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(egroupeSearchRepository.findAll());

        // Update the egroupe
        Egroupe updatedEgroupe = egroupeRepository.findById(egroupe.getId()).get();
        // Disconnect from session so that the updates on updatedEgroupe are not directly saved in db
        em.detach(updatedEgroupe);
        updatedEgroupe.titregroupe(UPDATED_TITREGROUPE).ordregroupe(UPDATED_ORDREGROUPE).isActive(UPDATED_IS_ACTIVE);

        restEgroupeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEgroupe.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEgroupe))
            )
            .andExpect(status().isOk());

        // Validate the Egroupe in the database
        List<Egroupe> egroupeList = egroupeRepository.findAll();
        assertThat(egroupeList).hasSize(databaseSizeBeforeUpdate);
        Egroupe testEgroupe = egroupeList.get(egroupeList.size() - 1);
        assertThat(testEgroupe.getTitregroupe()).isEqualTo(UPDATED_TITREGROUPE);
        assertThat(testEgroupe.getOrdregroupe()).isEqualTo(UPDATED_ORDREGROUPE);
        assertThat(testEgroupe.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(egroupeSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Egroupe> egroupeSearchList = IterableUtils.toList(egroupeSearchRepository.findAll());
                Egroupe testEgroupeSearch = egroupeSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testEgroupeSearch.getTitregroupe()).isEqualTo(UPDATED_TITREGROUPE);
                assertThat(testEgroupeSearch.getOrdregroupe()).isEqualTo(UPDATED_ORDREGROUPE);
                assertThat(testEgroupeSearch.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
            });
    }

    @Test
    @Transactional
    void putNonExistingEgroupe() throws Exception {
        int databaseSizeBeforeUpdate = egroupeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(egroupeSearchRepository.findAll());
        egroupe.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEgroupeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, egroupe.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(egroupe))
            )
            .andExpect(status().isBadRequest());

        // Validate the Egroupe in the database
        List<Egroupe> egroupeList = egroupeRepository.findAll();
        assertThat(egroupeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(egroupeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchEgroupe() throws Exception {
        int databaseSizeBeforeUpdate = egroupeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(egroupeSearchRepository.findAll());
        egroupe.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEgroupeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(egroupe))
            )
            .andExpect(status().isBadRequest());

        // Validate the Egroupe in the database
        List<Egroupe> egroupeList = egroupeRepository.findAll();
        assertThat(egroupeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(egroupeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEgroupe() throws Exception {
        int databaseSizeBeforeUpdate = egroupeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(egroupeSearchRepository.findAll());
        egroupe.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEgroupeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(egroupe)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Egroupe in the database
        List<Egroupe> egroupeList = egroupeRepository.findAll();
        assertThat(egroupeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(egroupeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateEgroupeWithPatch() throws Exception {
        // Initialize the database
        egroupeRepository.saveAndFlush(egroupe);

        int databaseSizeBeforeUpdate = egroupeRepository.findAll().size();

        // Update the egroupe using partial update
        Egroupe partialUpdatedEgroupe = new Egroupe();
        partialUpdatedEgroupe.setId(egroupe.getId());

        partialUpdatedEgroupe.isActive(UPDATED_IS_ACTIVE);

        restEgroupeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEgroupe.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEgroupe))
            )
            .andExpect(status().isOk());

        // Validate the Egroupe in the database
        List<Egroupe> egroupeList = egroupeRepository.findAll();
        assertThat(egroupeList).hasSize(databaseSizeBeforeUpdate);
        Egroupe testEgroupe = egroupeList.get(egroupeList.size() - 1);
        assertThat(testEgroupe.getTitregroupe()).isEqualTo(DEFAULT_TITREGROUPE);
        assertThat(testEgroupe.getOrdregroupe()).isEqualTo(DEFAULT_ORDREGROUPE);
        assertThat(testEgroupe.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateEgroupeWithPatch() throws Exception {
        // Initialize the database
        egroupeRepository.saveAndFlush(egroupe);

        int databaseSizeBeforeUpdate = egroupeRepository.findAll().size();

        // Update the egroupe using partial update
        Egroupe partialUpdatedEgroupe = new Egroupe();
        partialUpdatedEgroupe.setId(egroupe.getId());

        partialUpdatedEgroupe.titregroupe(UPDATED_TITREGROUPE).ordregroupe(UPDATED_ORDREGROUPE).isActive(UPDATED_IS_ACTIVE);

        restEgroupeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEgroupe.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEgroupe))
            )
            .andExpect(status().isOk());

        // Validate the Egroupe in the database
        List<Egroupe> egroupeList = egroupeRepository.findAll();
        assertThat(egroupeList).hasSize(databaseSizeBeforeUpdate);
        Egroupe testEgroupe = egroupeList.get(egroupeList.size() - 1);
        assertThat(testEgroupe.getTitregroupe()).isEqualTo(UPDATED_TITREGROUPE);
        assertThat(testEgroupe.getOrdregroupe()).isEqualTo(UPDATED_ORDREGROUPE);
        assertThat(testEgroupe.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingEgroupe() throws Exception {
        int databaseSizeBeforeUpdate = egroupeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(egroupeSearchRepository.findAll());
        egroupe.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEgroupeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, egroupe.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(egroupe))
            )
            .andExpect(status().isBadRequest());

        // Validate the Egroupe in the database
        List<Egroupe> egroupeList = egroupeRepository.findAll();
        assertThat(egroupeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(egroupeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEgroupe() throws Exception {
        int databaseSizeBeforeUpdate = egroupeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(egroupeSearchRepository.findAll());
        egroupe.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEgroupeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(egroupe))
            )
            .andExpect(status().isBadRequest());

        // Validate the Egroupe in the database
        List<Egroupe> egroupeList = egroupeRepository.findAll();
        assertThat(egroupeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(egroupeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEgroupe() throws Exception {
        int databaseSizeBeforeUpdate = egroupeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(egroupeSearchRepository.findAll());
        egroupe.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEgroupeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(egroupe)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Egroupe in the database
        List<Egroupe> egroupeList = egroupeRepository.findAll();
        assertThat(egroupeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(egroupeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteEgroupe() throws Exception {
        // Initialize the database
        egroupeRepository.saveAndFlush(egroupe);
        egroupeRepository.save(egroupe);
        egroupeSearchRepository.save(egroupe);

        int databaseSizeBeforeDelete = egroupeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(egroupeSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the egroupe
        restEgroupeMockMvc
            .perform(delete(ENTITY_API_URL_ID, egroupe.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Egroupe> egroupeList = egroupeRepository.findAll();
        assertThat(egroupeList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(egroupeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchEgroupe() throws Exception {
        // Initialize the database
        egroupe = egroupeRepository.saveAndFlush(egroupe);
        egroupeSearchRepository.save(egroupe);

        // Search the egroupe
        restEgroupeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + egroupe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(egroupe.getId().intValue())))
            .andExpect(jsonPath("$.[*].titregroupe").value(hasItem(DEFAULT_TITREGROUPE)))
            .andExpect(jsonPath("$.[*].ordregroupe").value(hasItem(DEFAULT_ORDREGROUPE.intValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }
}
