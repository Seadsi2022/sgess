package dsi.sea.sgess.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dsi.sea.sgess.IntegrationTest;
import dsi.sea.sgess.domain.Setablissement;
import dsi.sea.sgess.repository.SetablissementRepository;
import dsi.sea.sgess.repository.search.SetablissementSearchRepository;
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
 * Integration tests for the {@link SetablissementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SetablissementResourceIT {

    private static final String DEFAULT_CODEADMINISTRATIF = "AAAAAAAAAA";
    private static final String UPDATED_CODEADMINISTRATIF = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/setablissements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/setablissements";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SetablissementRepository setablissementRepository;

    @Autowired
    private SetablissementSearchRepository setablissementSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSetablissementMockMvc;

    private Setablissement setablissement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Setablissement createEntity(EntityManager em) {
        Setablissement setablissement = new Setablissement().codeadministratif(DEFAULT_CODEADMINISTRATIF);
        return setablissement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Setablissement createUpdatedEntity(EntityManager em) {
        Setablissement setablissement = new Setablissement().codeadministratif(UPDATED_CODEADMINISTRATIF);
        return setablissement;
    }

    @BeforeEach
    public void initTest() {
        setablissementSearchRepository.deleteAll();
        setablissement = createEntity(em);
    }

    @Test
    @Transactional
    void createSetablissement() throws Exception {
        int databaseSizeBeforeCreate = setablissementRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(setablissementSearchRepository.findAll());
        // Create the Setablissement
        restSetablissementMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(setablissement))
            )
            .andExpect(status().isCreated());

        // Validate the Setablissement in the database
        List<Setablissement> setablissementList = setablissementRepository.findAll();
        assertThat(setablissementList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(setablissementSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Setablissement testSetablissement = setablissementList.get(setablissementList.size() - 1);
        assertThat(testSetablissement.getCodeadministratif()).isEqualTo(DEFAULT_CODEADMINISTRATIF);
    }

    @Test
    @Transactional
    void createSetablissementWithExistingId() throws Exception {
        // Create the Setablissement with an existing ID
        setablissement.setId(1L);

        int databaseSizeBeforeCreate = setablissementRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(setablissementSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restSetablissementMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(setablissement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Setablissement in the database
        List<Setablissement> setablissementList = setablissementRepository.findAll();
        assertThat(setablissementList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(setablissementSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllSetablissements() throws Exception {
        // Initialize the database
        setablissementRepository.saveAndFlush(setablissement);

        // Get all the setablissementList
        restSetablissementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(setablissement.getId().intValue())))
            .andExpect(jsonPath("$.[*].codeadministratif").value(hasItem(DEFAULT_CODEADMINISTRATIF)));
    }

    @Test
    @Transactional
    void getSetablissement() throws Exception {
        // Initialize the database
        setablissementRepository.saveAndFlush(setablissement);

        // Get the setablissement
        restSetablissementMockMvc
            .perform(get(ENTITY_API_URL_ID, setablissement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(setablissement.getId().intValue()))
            .andExpect(jsonPath("$.codeadministratif").value(DEFAULT_CODEADMINISTRATIF));
    }

    @Test
    @Transactional
    void getNonExistingSetablissement() throws Exception {
        // Get the setablissement
        restSetablissementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSetablissement() throws Exception {
        // Initialize the database
        setablissementRepository.saveAndFlush(setablissement);

        int databaseSizeBeforeUpdate = setablissementRepository.findAll().size();
        setablissementSearchRepository.save(setablissement);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(setablissementSearchRepository.findAll());

        // Update the setablissement
        Setablissement updatedSetablissement = setablissementRepository.findById(setablissement.getId()).get();
        // Disconnect from session so that the updates on updatedSetablissement are not directly saved in db
        em.detach(updatedSetablissement);
        updatedSetablissement.codeadministratif(UPDATED_CODEADMINISTRATIF);

        restSetablissementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSetablissement.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSetablissement))
            )
            .andExpect(status().isOk());

        // Validate the Setablissement in the database
        List<Setablissement> setablissementList = setablissementRepository.findAll();
        assertThat(setablissementList).hasSize(databaseSizeBeforeUpdate);
        Setablissement testSetablissement = setablissementList.get(setablissementList.size() - 1);
        assertThat(testSetablissement.getCodeadministratif()).isEqualTo(UPDATED_CODEADMINISTRATIF);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(setablissementSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Setablissement> setablissementSearchList = IterableUtils.toList(setablissementSearchRepository.findAll());
                Setablissement testSetablissementSearch = setablissementSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testSetablissementSearch.getCodeadministratif()).isEqualTo(UPDATED_CODEADMINISTRATIF);
            });
    }

    @Test
    @Transactional
    void putNonExistingSetablissement() throws Exception {
        int databaseSizeBeforeUpdate = setablissementRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(setablissementSearchRepository.findAll());
        setablissement.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSetablissementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, setablissement.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(setablissement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Setablissement in the database
        List<Setablissement> setablissementList = setablissementRepository.findAll();
        assertThat(setablissementList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(setablissementSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchSetablissement() throws Exception {
        int databaseSizeBeforeUpdate = setablissementRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(setablissementSearchRepository.findAll());
        setablissement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSetablissementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(setablissement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Setablissement in the database
        List<Setablissement> setablissementList = setablissementRepository.findAll();
        assertThat(setablissementList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(setablissementSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSetablissement() throws Exception {
        int databaseSizeBeforeUpdate = setablissementRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(setablissementSearchRepository.findAll());
        setablissement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSetablissementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(setablissement)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Setablissement in the database
        List<Setablissement> setablissementList = setablissementRepository.findAll();
        assertThat(setablissementList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(setablissementSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateSetablissementWithPatch() throws Exception {
        // Initialize the database
        setablissementRepository.saveAndFlush(setablissement);

        int databaseSizeBeforeUpdate = setablissementRepository.findAll().size();

        // Update the setablissement using partial update
        Setablissement partialUpdatedSetablissement = new Setablissement();
        partialUpdatedSetablissement.setId(setablissement.getId());

        partialUpdatedSetablissement.codeadministratif(UPDATED_CODEADMINISTRATIF);

        restSetablissementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSetablissement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSetablissement))
            )
            .andExpect(status().isOk());

        // Validate the Setablissement in the database
        List<Setablissement> setablissementList = setablissementRepository.findAll();
        assertThat(setablissementList).hasSize(databaseSizeBeforeUpdate);
        Setablissement testSetablissement = setablissementList.get(setablissementList.size() - 1);
        assertThat(testSetablissement.getCodeadministratif()).isEqualTo(UPDATED_CODEADMINISTRATIF);
    }

    @Test
    @Transactional
    void fullUpdateSetablissementWithPatch() throws Exception {
        // Initialize the database
        setablissementRepository.saveAndFlush(setablissement);

        int databaseSizeBeforeUpdate = setablissementRepository.findAll().size();

        // Update the setablissement using partial update
        Setablissement partialUpdatedSetablissement = new Setablissement();
        partialUpdatedSetablissement.setId(setablissement.getId());

        partialUpdatedSetablissement.codeadministratif(UPDATED_CODEADMINISTRATIF);

        restSetablissementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSetablissement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSetablissement))
            )
            .andExpect(status().isOk());

        // Validate the Setablissement in the database
        List<Setablissement> setablissementList = setablissementRepository.findAll();
        assertThat(setablissementList).hasSize(databaseSizeBeforeUpdate);
        Setablissement testSetablissement = setablissementList.get(setablissementList.size() - 1);
        assertThat(testSetablissement.getCodeadministratif()).isEqualTo(UPDATED_CODEADMINISTRATIF);
    }

    @Test
    @Transactional
    void patchNonExistingSetablissement() throws Exception {
        int databaseSizeBeforeUpdate = setablissementRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(setablissementSearchRepository.findAll());
        setablissement.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSetablissementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, setablissement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(setablissement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Setablissement in the database
        List<Setablissement> setablissementList = setablissementRepository.findAll();
        assertThat(setablissementList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(setablissementSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSetablissement() throws Exception {
        int databaseSizeBeforeUpdate = setablissementRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(setablissementSearchRepository.findAll());
        setablissement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSetablissementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(setablissement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Setablissement in the database
        List<Setablissement> setablissementList = setablissementRepository.findAll();
        assertThat(setablissementList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(setablissementSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSetablissement() throws Exception {
        int databaseSizeBeforeUpdate = setablissementRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(setablissementSearchRepository.findAll());
        setablissement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSetablissementMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(setablissement))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Setablissement in the database
        List<Setablissement> setablissementList = setablissementRepository.findAll();
        assertThat(setablissementList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(setablissementSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteSetablissement() throws Exception {
        // Initialize the database
        setablissementRepository.saveAndFlush(setablissement);
        setablissementRepository.save(setablissement);
        setablissementSearchRepository.save(setablissement);

        int databaseSizeBeforeDelete = setablissementRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(setablissementSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the setablissement
        restSetablissementMockMvc
            .perform(delete(ENTITY_API_URL_ID, setablissement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Setablissement> setablissementList = setablissementRepository.findAll();
        assertThat(setablissementList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(setablissementSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchSetablissement() throws Exception {
        // Initialize the database
        setablissement = setablissementRepository.saveAndFlush(setablissement);
        setablissementSearchRepository.save(setablissement);

        // Search the setablissement
        restSetablissementMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + setablissement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(setablissement.getId().intValue())))
            .andExpect(jsonPath("$.[*].codeadministratif").value(hasItem(DEFAULT_CODEADMINISTRATIF)));
    }
}
