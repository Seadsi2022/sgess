package dsi.sea.sgess.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dsi.sea.sgess.IntegrationTest;
import dsi.sea.sgess.domain.Eattribut;
import dsi.sea.sgess.repository.EattributRepository;
import dsi.sea.sgess.repository.search.EattributSearchRepository;
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
 * Integration tests for the {@link EattributResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EattributResourceIT {

    private static final String DEFAULT_ATTRNAME = "AAAAAAAAAA";
    private static final String UPDATED_ATTRNAME = "BBBBBBBBBB";

    private static final String DEFAULT_ATTRDISPLAYNAME = "AAAAAAAAAA";
    private static final String UPDATED_ATTRDISPLAYNAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/eattributs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/eattributs";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EattributRepository eattributRepository;

    @Autowired
    private EattributSearchRepository eattributSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEattributMockMvc;

    private Eattribut eattribut;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Eattribut createEntity(EntityManager em) {
        Eattribut eattribut = new Eattribut().attrname(DEFAULT_ATTRNAME).attrdisplayname(DEFAULT_ATTRDISPLAYNAME);
        return eattribut;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Eattribut createUpdatedEntity(EntityManager em) {
        Eattribut eattribut = new Eattribut().attrname(UPDATED_ATTRNAME).attrdisplayname(UPDATED_ATTRDISPLAYNAME);
        return eattribut;
    }

    @BeforeEach
    public void initTest() {
        eattributSearchRepository.deleteAll();
        eattribut = createEntity(em);
    }

    @Test
    @Transactional
    void createEattribut() throws Exception {
        int databaseSizeBeforeCreate = eattributRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eattributSearchRepository.findAll());
        // Create the Eattribut
        restEattributMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eattribut)))
            .andExpect(status().isCreated());

        // Validate the Eattribut in the database
        List<Eattribut> eattributList = eattributRepository.findAll();
        assertThat(eattributList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(eattributSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Eattribut testEattribut = eattributList.get(eattributList.size() - 1);
        assertThat(testEattribut.getAttrname()).isEqualTo(DEFAULT_ATTRNAME);
        assertThat(testEattribut.getAttrdisplayname()).isEqualTo(DEFAULT_ATTRDISPLAYNAME);
    }

    @Test
    @Transactional
    void createEattributWithExistingId() throws Exception {
        // Create the Eattribut with an existing ID
        eattribut.setId(1L);

        int databaseSizeBeforeCreate = eattributRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eattributSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restEattributMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eattribut)))
            .andExpect(status().isBadRequest());

        // Validate the Eattribut in the database
        List<Eattribut> eattributList = eattributRepository.findAll();
        assertThat(eattributList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(eattributSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllEattributs() throws Exception {
        // Initialize the database
        eattributRepository.saveAndFlush(eattribut);

        // Get all the eattributList
        restEattributMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eattribut.getId().intValue())))
            .andExpect(jsonPath("$.[*].attrname").value(hasItem(DEFAULT_ATTRNAME)))
            .andExpect(jsonPath("$.[*].attrdisplayname").value(hasItem(DEFAULT_ATTRDISPLAYNAME)));
    }

    @Test
    @Transactional
    void getEattribut() throws Exception {
        // Initialize the database
        eattributRepository.saveAndFlush(eattribut);

        // Get the eattribut
        restEattributMockMvc
            .perform(get(ENTITY_API_URL_ID, eattribut.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eattribut.getId().intValue()))
            .andExpect(jsonPath("$.attrname").value(DEFAULT_ATTRNAME))
            .andExpect(jsonPath("$.attrdisplayname").value(DEFAULT_ATTRDISPLAYNAME));
    }

    @Test
    @Transactional
    void getNonExistingEattribut() throws Exception {
        // Get the eattribut
        restEattributMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEattribut() throws Exception {
        // Initialize the database
        eattributRepository.saveAndFlush(eattribut);

        int databaseSizeBeforeUpdate = eattributRepository.findAll().size();
        eattributSearchRepository.save(eattribut);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eattributSearchRepository.findAll());

        // Update the eattribut
        Eattribut updatedEattribut = eattributRepository.findById(eattribut.getId()).get();
        // Disconnect from session so that the updates on updatedEattribut are not directly saved in db
        em.detach(updatedEattribut);
        updatedEattribut.attrname(UPDATED_ATTRNAME).attrdisplayname(UPDATED_ATTRDISPLAYNAME);

        restEattributMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEattribut.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEattribut))
            )
            .andExpect(status().isOk());

        // Validate the Eattribut in the database
        List<Eattribut> eattributList = eattributRepository.findAll();
        assertThat(eattributList).hasSize(databaseSizeBeforeUpdate);
        Eattribut testEattribut = eattributList.get(eattributList.size() - 1);
        assertThat(testEattribut.getAttrname()).isEqualTo(UPDATED_ATTRNAME);
        assertThat(testEattribut.getAttrdisplayname()).isEqualTo(UPDATED_ATTRDISPLAYNAME);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(eattributSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Eattribut> eattributSearchList = IterableUtils.toList(eattributSearchRepository.findAll());
                Eattribut testEattributSearch = eattributSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testEattributSearch.getAttrname()).isEqualTo(UPDATED_ATTRNAME);
                assertThat(testEattributSearch.getAttrdisplayname()).isEqualTo(UPDATED_ATTRDISPLAYNAME);
            });
    }

    @Test
    @Transactional
    void putNonExistingEattribut() throws Exception {
        int databaseSizeBeforeUpdate = eattributRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eattributSearchRepository.findAll());
        eattribut.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEattributMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eattribut.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eattribut))
            )
            .andExpect(status().isBadRequest());

        // Validate the Eattribut in the database
        List<Eattribut> eattributList = eattributRepository.findAll();
        assertThat(eattributList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(eattributSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchEattribut() throws Exception {
        int databaseSizeBeforeUpdate = eattributRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eattributSearchRepository.findAll());
        eattribut.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEattributMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eattribut))
            )
            .andExpect(status().isBadRequest());

        // Validate the Eattribut in the database
        List<Eattribut> eattributList = eattributRepository.findAll();
        assertThat(eattributList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(eattributSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEattribut() throws Exception {
        int databaseSizeBeforeUpdate = eattributRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eattributSearchRepository.findAll());
        eattribut.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEattributMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eattribut)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Eattribut in the database
        List<Eattribut> eattributList = eattributRepository.findAll();
        assertThat(eattributList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(eattributSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateEattributWithPatch() throws Exception {
        // Initialize the database
        eattributRepository.saveAndFlush(eattribut);

        int databaseSizeBeforeUpdate = eattributRepository.findAll().size();

        // Update the eattribut using partial update
        Eattribut partialUpdatedEattribut = new Eattribut();
        partialUpdatedEattribut.setId(eattribut.getId());

        partialUpdatedEattribut.attrname(UPDATED_ATTRNAME);

        restEattributMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEattribut.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEattribut))
            )
            .andExpect(status().isOk());

        // Validate the Eattribut in the database
        List<Eattribut> eattributList = eattributRepository.findAll();
        assertThat(eattributList).hasSize(databaseSizeBeforeUpdate);
        Eattribut testEattribut = eattributList.get(eattributList.size() - 1);
        assertThat(testEattribut.getAttrname()).isEqualTo(UPDATED_ATTRNAME);
        assertThat(testEattribut.getAttrdisplayname()).isEqualTo(DEFAULT_ATTRDISPLAYNAME);
    }

    @Test
    @Transactional
    void fullUpdateEattributWithPatch() throws Exception {
        // Initialize the database
        eattributRepository.saveAndFlush(eattribut);

        int databaseSizeBeforeUpdate = eattributRepository.findAll().size();

        // Update the eattribut using partial update
        Eattribut partialUpdatedEattribut = new Eattribut();
        partialUpdatedEattribut.setId(eattribut.getId());

        partialUpdatedEattribut.attrname(UPDATED_ATTRNAME).attrdisplayname(UPDATED_ATTRDISPLAYNAME);

        restEattributMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEattribut.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEattribut))
            )
            .andExpect(status().isOk());

        // Validate the Eattribut in the database
        List<Eattribut> eattributList = eattributRepository.findAll();
        assertThat(eattributList).hasSize(databaseSizeBeforeUpdate);
        Eattribut testEattribut = eattributList.get(eattributList.size() - 1);
        assertThat(testEattribut.getAttrname()).isEqualTo(UPDATED_ATTRNAME);
        assertThat(testEattribut.getAttrdisplayname()).isEqualTo(UPDATED_ATTRDISPLAYNAME);
    }

    @Test
    @Transactional
    void patchNonExistingEattribut() throws Exception {
        int databaseSizeBeforeUpdate = eattributRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eattributSearchRepository.findAll());
        eattribut.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEattributMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eattribut.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eattribut))
            )
            .andExpect(status().isBadRequest());

        // Validate the Eattribut in the database
        List<Eattribut> eattributList = eattributRepository.findAll();
        assertThat(eattributList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(eattributSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEattribut() throws Exception {
        int databaseSizeBeforeUpdate = eattributRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eattributSearchRepository.findAll());
        eattribut.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEattributMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eattribut))
            )
            .andExpect(status().isBadRequest());

        // Validate the Eattribut in the database
        List<Eattribut> eattributList = eattributRepository.findAll();
        assertThat(eattributList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(eattributSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEattribut() throws Exception {
        int databaseSizeBeforeUpdate = eattributRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eattributSearchRepository.findAll());
        eattribut.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEattributMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(eattribut))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Eattribut in the database
        List<Eattribut> eattributList = eattributRepository.findAll();
        assertThat(eattributList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(eattributSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteEattribut() throws Exception {
        // Initialize the database
        eattributRepository.saveAndFlush(eattribut);
        eattributRepository.save(eattribut);
        eattributSearchRepository.save(eattribut);

        int databaseSizeBeforeDelete = eattributRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eattributSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the eattribut
        restEattributMockMvc
            .perform(delete(ENTITY_API_URL_ID, eattribut.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Eattribut> eattributList = eattributRepository.findAll();
        assertThat(eattributList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(eattributSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchEattribut() throws Exception {
        // Initialize the database
        eattribut = eattributRepository.saveAndFlush(eattribut);
        eattributSearchRepository.save(eattribut);

        // Search the eattribut
        restEattributMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + eattribut.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eattribut.getId().intValue())))
            .andExpect(jsonPath("$.[*].attrname").value(hasItem(DEFAULT_ATTRNAME)))
            .andExpect(jsonPath("$.[*].attrdisplayname").value(hasItem(DEFAULT_ATTRDISPLAYNAME)));
    }
}
