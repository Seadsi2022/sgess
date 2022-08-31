package dsi.sea.sgess.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dsi.sea.sgess.IntegrationTest;
import dsi.sea.sgess.domain.Scode;
import dsi.sea.sgess.repository.ScodeRepository;
import dsi.sea.sgess.repository.search.ScodeSearchRepository;
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
 * Integration tests for the {@link ScodeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ScodeResourceIT {

    private static final String DEFAULT_CODE_LIB = "AAAAAAAAAA";
    private static final String UPDATED_CODE_LIB = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/scodes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/scodes";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ScodeRepository scodeRepository;

    @Autowired
    private ScodeSearchRepository scodeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restScodeMockMvc;

    private Scode scode;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Scode createEntity(EntityManager em) {
        Scode scode = new Scode().codeLib(DEFAULT_CODE_LIB);
        return scode;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Scode createUpdatedEntity(EntityManager em) {
        Scode scode = new Scode().codeLib(UPDATED_CODE_LIB);
        return scode;
    }

    @BeforeEach
    public void initTest() {
        scodeSearchRepository.deleteAll();
        scode = createEntity(em);
    }

    @Test
    @Transactional
    void createScode() throws Exception {
        int databaseSizeBeforeCreate = scodeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(scodeSearchRepository.findAll());
        // Create the Scode
        restScodeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(scode)))
            .andExpect(status().isCreated());

        // Validate the Scode in the database
        List<Scode> scodeList = scodeRepository.findAll();
        assertThat(scodeList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(scodeSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Scode testScode = scodeList.get(scodeList.size() - 1);
        assertThat(testScode.getCodeLib()).isEqualTo(DEFAULT_CODE_LIB);
    }

    @Test
    @Transactional
    void createScodeWithExistingId() throws Exception {
        // Create the Scode with an existing ID
        scode.setId(1L);

        int databaseSizeBeforeCreate = scodeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(scodeSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restScodeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(scode)))
            .andExpect(status().isBadRequest());

        // Validate the Scode in the database
        List<Scode> scodeList = scodeRepository.findAll();
        assertThat(scodeList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(scodeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllScodes() throws Exception {
        // Initialize the database
        scodeRepository.saveAndFlush(scode);

        // Get all the scodeList
        restScodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scode.getId().intValue())))
            .andExpect(jsonPath("$.[*].codeLib").value(hasItem(DEFAULT_CODE_LIB)));
    }

    @Test
    @Transactional
    void getScode() throws Exception {
        // Initialize the database
        scodeRepository.saveAndFlush(scode);

        // Get the scode
        restScodeMockMvc
            .perform(get(ENTITY_API_URL_ID, scode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(scode.getId().intValue()))
            .andExpect(jsonPath("$.codeLib").value(DEFAULT_CODE_LIB));
    }

    @Test
    @Transactional
    void getNonExistingScode() throws Exception {
        // Get the scode
        restScodeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewScode() throws Exception {
        // Initialize the database
        scodeRepository.saveAndFlush(scode);

        int databaseSizeBeforeUpdate = scodeRepository.findAll().size();
        scodeSearchRepository.save(scode);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(scodeSearchRepository.findAll());

        // Update the scode
        Scode updatedScode = scodeRepository.findById(scode.getId()).get();
        // Disconnect from session so that the updates on updatedScode are not directly saved in db
        em.detach(updatedScode);
        updatedScode.codeLib(UPDATED_CODE_LIB);

        restScodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedScode.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedScode))
            )
            .andExpect(status().isOk());

        // Validate the Scode in the database
        List<Scode> scodeList = scodeRepository.findAll();
        assertThat(scodeList).hasSize(databaseSizeBeforeUpdate);
        Scode testScode = scodeList.get(scodeList.size() - 1);
        assertThat(testScode.getCodeLib()).isEqualTo(UPDATED_CODE_LIB);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(scodeSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Scode> scodeSearchList = IterableUtils.toList(scodeSearchRepository.findAll());
                Scode testScodeSearch = scodeSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testScodeSearch.getCodeLib()).isEqualTo(UPDATED_CODE_LIB);
            });
    }

    @Test
    @Transactional
    void putNonExistingScode() throws Exception {
        int databaseSizeBeforeUpdate = scodeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(scodeSearchRepository.findAll());
        scode.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, scode.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(scode))
            )
            .andExpect(status().isBadRequest());

        // Validate the Scode in the database
        List<Scode> scodeList = scodeRepository.findAll();
        assertThat(scodeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(scodeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchScode() throws Exception {
        int databaseSizeBeforeUpdate = scodeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(scodeSearchRepository.findAll());
        scode.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(scode))
            )
            .andExpect(status().isBadRequest());

        // Validate the Scode in the database
        List<Scode> scodeList = scodeRepository.findAll();
        assertThat(scodeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(scodeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamScode() throws Exception {
        int databaseSizeBeforeUpdate = scodeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(scodeSearchRepository.findAll());
        scode.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScodeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(scode)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Scode in the database
        List<Scode> scodeList = scodeRepository.findAll();
        assertThat(scodeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(scodeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateScodeWithPatch() throws Exception {
        // Initialize the database
        scodeRepository.saveAndFlush(scode);

        int databaseSizeBeforeUpdate = scodeRepository.findAll().size();

        // Update the scode using partial update
        Scode partialUpdatedScode = new Scode();
        partialUpdatedScode.setId(scode.getId());

        partialUpdatedScode.codeLib(UPDATED_CODE_LIB);

        restScodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedScode))
            )
            .andExpect(status().isOk());

        // Validate the Scode in the database
        List<Scode> scodeList = scodeRepository.findAll();
        assertThat(scodeList).hasSize(databaseSizeBeforeUpdate);
        Scode testScode = scodeList.get(scodeList.size() - 1);
        assertThat(testScode.getCodeLib()).isEqualTo(UPDATED_CODE_LIB);
    }

    @Test
    @Transactional
    void fullUpdateScodeWithPatch() throws Exception {
        // Initialize the database
        scodeRepository.saveAndFlush(scode);

        int databaseSizeBeforeUpdate = scodeRepository.findAll().size();

        // Update the scode using partial update
        Scode partialUpdatedScode = new Scode();
        partialUpdatedScode.setId(scode.getId());

        partialUpdatedScode.codeLib(UPDATED_CODE_LIB);

        restScodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedScode))
            )
            .andExpect(status().isOk());

        // Validate the Scode in the database
        List<Scode> scodeList = scodeRepository.findAll();
        assertThat(scodeList).hasSize(databaseSizeBeforeUpdate);
        Scode testScode = scodeList.get(scodeList.size() - 1);
        assertThat(testScode.getCodeLib()).isEqualTo(UPDATED_CODE_LIB);
    }

    @Test
    @Transactional
    void patchNonExistingScode() throws Exception {
        int databaseSizeBeforeUpdate = scodeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(scodeSearchRepository.findAll());
        scode.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, scode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(scode))
            )
            .andExpect(status().isBadRequest());

        // Validate the Scode in the database
        List<Scode> scodeList = scodeRepository.findAll();
        assertThat(scodeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(scodeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchScode() throws Exception {
        int databaseSizeBeforeUpdate = scodeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(scodeSearchRepository.findAll());
        scode.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(scode))
            )
            .andExpect(status().isBadRequest());

        // Validate the Scode in the database
        List<Scode> scodeList = scodeRepository.findAll();
        assertThat(scodeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(scodeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamScode() throws Exception {
        int databaseSizeBeforeUpdate = scodeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(scodeSearchRepository.findAll());
        scode.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScodeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(scode)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Scode in the database
        List<Scode> scodeList = scodeRepository.findAll();
        assertThat(scodeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(scodeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteScode() throws Exception {
        // Initialize the database
        scodeRepository.saveAndFlush(scode);
        scodeRepository.save(scode);
        scodeSearchRepository.save(scode);

        int databaseSizeBeforeDelete = scodeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(scodeSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the scode
        restScodeMockMvc
            .perform(delete(ENTITY_API_URL_ID, scode.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Scode> scodeList = scodeRepository.findAll();
        assertThat(scodeList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(scodeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchScode() throws Exception {
        // Initialize the database
        scode = scodeRepository.saveAndFlush(scode);
        scodeSearchRepository.save(scode);

        // Search the scode
        restScodeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + scode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scode.getId().intValue())))
            .andExpect(jsonPath("$.[*].codeLib").value(hasItem(DEFAULT_CODE_LIB)));
    }
}
